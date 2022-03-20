package com.mpsg.student.batch.config.exportbatch.processor;

import com.mpsg.student.batch.database.entity.StudentDbo;
import com.mpsg.student.batch.domain.Student;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
public class ExportStudentProcessor implements ItemProcessor<StudentDbo, Student> {
  @Override
  public Student process(StudentDbo studentDbo) {
    return Student.builder()
                  .firstName(studentDbo.getFirstName())
                  .lastName(studentDbo.getLastName())
                  .birthDate(LocalDate.ofInstant(studentDbo.getBirthDate().toInstant(), ZoneId.systemDefault()))
                  .build();
  }
}