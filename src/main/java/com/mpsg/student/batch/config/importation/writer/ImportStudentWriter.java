package com.mpsg.student.batch.config.importation.writer;

import com.mpsg.student.batch.database.entity.StudentDbo;
import com.mpsg.student.batch.database.repository.StudentRepository;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Component
public class ImportStudentWriter extends JpaItemWriter<StudentDbo> {

  private final StudentRepository repository;

  public ImportStudentWriter(StudentRepository repository, EntityManagerFactory entityManagerFactory) {
    this.repository = repository;
    super.setEntityManagerFactory(entityManagerFactory);
  }

  @Override
  public void write(List<? extends StudentDbo> students) {
    repository.saveAll(students);
  }
}