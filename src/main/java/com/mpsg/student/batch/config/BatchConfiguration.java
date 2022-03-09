package com.mpsg.student.batch.config;

import com.mpsg.student.batch.config.importation.listener.ImportReaderListener;
import com.mpsg.student.batch.config.importation.listener.ImportWriterListener;
import com.mpsg.student.batch.config.importation.processor.ImportStudentProcessor;
import com.mpsg.student.batch.config.importation.reader.ImportStudentReader;
import com.mpsg.student.batch.config.importation.writer.ImportStudentWriter;
import com.mpsg.student.batch.database.entity.StudentDbo;
import com.mpsg.student.batch.domain.Student;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@EnableBatchProcessing
@Configuration
public class BatchConfiguration {

  @Value("${batch.configuration.concurrencyLimit}")
  private int concurrencyLimit;
  @Value("${batch.configuration.chunkSize}")
  private int chunkSize;

  @Bean
  public Job studentJob(JobBuilderFactory jobBuilderFactory,
                        @Qualifier("studentImportStep") Step studentImportStep) {
    return jobBuilderFactory.get("studentJob")
                            .start(studentImportStep)
                            .build();
  }

  @Bean
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
      .taskExecutor(stepTaskExecutor())
      .throttleLimit(concurrencyLimit)
      .build();
  }

  @Bean
  public TaskExecutor stepTaskExecutor() {
    var taskExecutor = new SimpleAsyncTaskExecutor("student-batch");
    taskExecutor.setConcurrencyLimit(concurrencyLimit);
    return taskExecutor;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
                                                                     JpaVendorAdapter jpaVendorAdapter) {
    var entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactory.setPackagesToScan("com.mpsg.student.batch.database.entity");
    entityManagerFactory.setDataSource(dataSource);
    entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
    entityManagerFactory.setJpaProperties(new Properties());
    return entityManagerFactory;
  }

  @Bean
  public DataSource dataSource(@Value("${spring.datasource.url}") String databaseUrl,
                               @Value("${spring.datasource.username}") String databaseUsername,
                               @Value("${spring.datasource.password}") String databasePassword,
                               @Value("${spring.datasource.driver-class-name}") String databaseDriver,
                               @Value("${schema.name}") String schemaName) {
    var dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(databaseDriver);
    dataSource.setUrl(databaseUrl);
    dataSource.setUsername(databaseUsername);
    dataSource.setPassword(databasePassword);
    dataSource.setSchema(schemaName);
    return dataSource;
  }

  @Bean
  public JpaVendorAdapter jpaVendorAdapter(@Value("${spring.jpa.database-platform}") String databasePlatform) {
    var jpaVendorAdapter = new HibernateJpaVendorAdapter();
    jpaVendorAdapter.setDatabase(Database.MYSQL);
    jpaVendorAdapter.setGenerateDdl(false);
    jpaVendorAdapter.setShowSql(false);
    jpaVendorAdapter.setDatabasePlatform(databasePlatform);
    return jpaVendorAdapter;
  }

}