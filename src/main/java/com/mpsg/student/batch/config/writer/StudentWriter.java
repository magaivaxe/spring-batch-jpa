package com.mpsg.student.batch.config.writer;

import com.mpsg.student.batch.entity.Student;
import com.mpsg.student.batch.repository.StudentRepository;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Component
public class StudentWriter extends JpaItemWriter<Student> {

  private final StudentRepository repository;

  public StudentWriter(StudentRepository repository, EntityManagerFactory entityManagerFactory) {
    this.repository = repository;
    super.setEntityManagerFactory(entityManagerFactory);
  }

  @Override
  public void write(List<? extends Student> students) {
    repository.saveAll(students);
  }
}