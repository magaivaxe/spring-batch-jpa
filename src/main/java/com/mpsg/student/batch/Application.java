package com.mpsg.student.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class Application implements CommandLineRunner {

  private final JobLauncher jobLauncher;
  private final Job studentJob;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    jobLauncher.run(studentJob, new JobParametersBuilder()
      .addLong("time", System.nanoTime())
      .toJobParameters());
  }
}