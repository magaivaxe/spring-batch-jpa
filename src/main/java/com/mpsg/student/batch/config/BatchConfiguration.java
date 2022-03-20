package com.mpsg.student.batch.config;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.mpsg.student.batch.config.exportbatch.listener.ExportWriterListener;
import com.mpsg.student.batch.config.exportbatch.processor.ExportStudentProcessor;
import com.mpsg.student.batch.config.exportbatch.reader.ExportItemReader;
import com.mpsg.student.batch.config.exportbatch.writer.ExportItemWriter;
import com.mpsg.student.batch.config.importbatch.listener.ImportReaderListener;
import com.mpsg.student.batch.config.importbatch.listener.ImportWriterListener;
import com.mpsg.student.batch.config.importbatch.processor.ImportStudentProcessor;
import com.mpsg.student.batch.config.importbatch.reader.ImportStudentReader;
import com.mpsg.student.batch.config.importbatch.writer.ImportStudentWriter;
import com.mpsg.student.batch.database.entity.StudentDbo;
import com.mpsg.student.batch.domain.Student;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.support.SynchronizedItemStreamWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@EnableBatchProcessing
@Configuration
public class BatchConfiguration {

  @Value("${batch.configuration.concurrencyLimit}")
  private int concurrencyLimit;
  @Value("${batch.configuration.chunkSize}")
  private int chunkSize;

  @Bean
  public Job studentJob(JobBuilderFactory jobBuilderFactory,
                        @Qualifier("studentImportStep") Step studentImportStep,
                        @Qualifier("studentExportStep") Step studentExportStep) {
    return jobBuilderFactory.get("studentJob")
                            .start(studentImportStep)
                            .next(studentExportStep)
                            .build();
  }

  @Bean
  @JobScope
  public Step studentImportStep(StepBuilderFactory stepBuilderFactory,
                                ImportStudentReader importStudentReader,
                                ImportReaderListener importReaderListener,
                                ImportStudentProcessor importStudentProcessor,
                                ImportWriterListener importWriterListener,
                                ImportStudentWriter importStudentWriter) {
    return stepBuilderFactory.get("studentImport")
      .<Student, StudentDbo>chunk(chunkSize)
      .reader(importStudentReader).faultTolerant().skip(Exception.class).skipLimit(Integer.MAX_VALUE)
      .listener(importReaderListener)
      .processor(importStudentProcessor)
      .writer(importStudentWriter).faultTolerant().skip(Exception.class).skipLimit(Integer.MAX_VALUE)
      .listener(importWriterListener)
      .taskExecutor(stepTaskExecutor("import-step"))
      .throttleLimit(concurrencyLimit)
      .build();
  }

  @Bean
  @JobScope
  public Step studentExportStep(StepBuilderFactory stepBuilderFactory,
                                ExportStudentProcessor exportStudentProcessor,
                                RepositoryItemReader<StudentDbo> repositoryItemReader,
                                SynchronizedItemStreamWriter<Student> itemStreamWriter,
                                ExportWriterListener exportWriterListener) {
    return stepBuilderFactory.get("studentExportStep")
      .<StudentDbo, Student>chunk(chunkSize)
      .reader(repositoryItemReader)
      .processor(exportStudentProcessor)
      .writer(itemStreamWriter)
      .listener(exportWriterListener)
      .taskExecutor(stepTaskExecutor("export-step"))
      .throttleLimit(concurrencyLimit)
      .build();
  }

  @Bean
  @StepScope
  public RepositoryItemReader<StudentDbo> repositoryItemReader(ExportItemReader exportItemReader) {
    return exportItemReader.itemReader();
  }

  @Bean
  @StepScope
  public SynchronizedItemStreamWriter<Student> jsonFileItemWriter(ExportItemWriter exportItemWriter) {
    return exportItemWriter.itemWriter();
  }

  @Bean
  public JacksonJsonObjectMarshaller<Student> jacksonJsonObjectMarshaller() {
    var marshaller = new JacksonJsonObjectMarshaller<Student>();
    var jsonMapper = JsonMapper.builder()
                               .findAndAddModules()
                               .build();
    marshaller.setObjectMapper(jsonMapper);
    return marshaller;
  }

  private TaskExecutor stepTaskExecutor(String taskName) {
    var taskExecutor = new SimpleAsyncTaskExecutor(taskName);
    taskExecutor.setConcurrencyLimit(concurrencyLimit);
    return taskExecutor;
  }

}