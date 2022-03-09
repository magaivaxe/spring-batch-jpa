package com.mpsg.student.batch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class JpaConfiguration {

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