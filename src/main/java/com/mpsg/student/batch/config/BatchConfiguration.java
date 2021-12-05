package com.mpsg.student.batch.config;

import com.mpsg.student.batch.config.listener.ReaderListener;
import com.mpsg.student.batch.config.listener.WriteListener;
import com.mpsg.student.batch.config.reader.StudentReader;
import com.mpsg.student.batch.config.writer.StudentWriter;
import com.mpsg.student.batch.entity.Student;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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

@EnableAutoConfiguration
@EnableBatchProcessing
@Configuration
public class BatchConfiguration {

  @Value("${spring.datasource.url}")
  private String databaseUrl;
  @Value("${spring.datasource.username}")
  private String databaseUsername;
  @Value("${spring.datasource.password}")
  private String databasePassword;
  @Value("${spring.datasource.driver-class-name}")
  private String databaseDriver;
  @Value("${spring.jpa.database-platform}")
  private String databasePlatform;
  @Value(("${schema.name}"))
  private String schemaName;

  @Bean
  public Job studentJob(JobBuilderFactory jobBuilderFactory,
                        @Qualifier("studentStep") Step studentStep) {
    return jobBuilderFactory.get("studentJob")
                            .start(studentStep)
                            .build();
  }

  @Bean
  public TaskExecutor stepTaskExecutor() {
    var taskExecutor = new SimpleAsyncTaskExecutor("student-batch");
    taskExecutor.setConcurrencyLimit(4);
    return taskExecutor;
  }

  @Bean
  public Step studentStep(StepBuilderFactory stepBuilderFactory,
                          ReaderListener readerListener,
                          WriteListener writeListener,
                          StudentReader studentReader,
                          StudentWriter studentWriter) {
    return stepBuilderFactory.get("studentStep")
      .<Student, Student>chunk(100)
      .reader(studentReader).faultTolerant().skip(Exception.class).skipLimit(Integer.MAX_VALUE)
      .listener(readerListener)
      .writer(studentWriter).faultTolerant().skip(Exception.class).skipLimit(Integer.MAX_VALUE)
      .listener(writeListener)
      .taskExecutor(stepTaskExecutor())
      .throttleLimit(4)
      .build();
  }

//  @Bean
//  public FlatFileItemReader<Student> studentReader(StudentLineMapper studentLineMapper) {
//    return new FlatFileItemReaderBuilder<Student>()
//      .name("studentItemReader")
//      .resource(new ClassPathResource("samples/sample_test.csv"))
//      .lineMapper(studentLineMapper)
//      .build();
//  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
                                                                     JpaVendorAdapter jpaVendorAdapter) {
    var entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactory.setPackagesToScan("com.mpsg.student.batch");
    entityManagerFactory.setDataSource(dataSource);
    entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
    entityManagerFactory.setJpaProperties(new Properties());
    return entityManagerFactory;
  }

  @Bean
  public DataSource dataSource() {
    var dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(databaseDriver);
    dataSource.setUrl(databaseUrl);
    dataSource.setUsername(databaseUsername);
    dataSource.setPassword(databasePassword);
    dataSource.setSchema(schemaName);
    return dataSource;
  }

  @Bean
  public JpaVendorAdapter jpaVendorAdapter() {
    var jpaVendorAdapter = new HibernateJpaVendorAdapter();
    jpaVendorAdapter.setDatabase(Database.MYSQL);
    jpaVendorAdapter.setGenerateDdl(false);
    jpaVendorAdapter.setShowSql(false);
    jpaVendorAdapter.setDatabasePlatform(databasePlatform);
    return jpaVendorAdapter;
  }


}