package com.mpsg.student.batch.config.processor;

import com.mpsg.student.batch.database.entity.StudentDbo;
import com.mpsg.student.batch.domain.Student;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class StudentProcessor implements ItemProcessor<Student, StudentDbo> {
  @Override
  public StudentDbo process(Student student) {
    return StudentDbo.builder()
                     .firstName(student.getFirstName())
                     .lastName(student.getLastName())
                     .birthDate(Date.valueOf(student.getBirthDate()))
                     .build();
  }
}