package com.mpsg.student.batch.config.reader.mapper;

import com.mpsg.student.batch.entity.Student;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.stereotype.Component;

import static com.mpsg.student.batch.config.reader.mapper.Positions.*;

@Component
public class StudentLineMapper implements LineMapper<Student> {

  private final DefaultLineMapper<Student> mapper = new DefaultLineMapper<>();

  @Override
  public Student mapLine(String line, int lineNumber) throws Exception {
    mapper.setFieldSetMapper(fieldSet -> Student
      .builder()
      .firstName(fieldSet.readString(FIRST_NAME.getEndIndex()))
      .lastName(fieldSet.readString(LAST_NAME.getEndIndex()))
      .birthDate(fieldSet.readDate(BIRTH_DATE.getEndIndex()))
      .build()
    );
    return mapper.mapLine(line, lineNumber);
  }
}