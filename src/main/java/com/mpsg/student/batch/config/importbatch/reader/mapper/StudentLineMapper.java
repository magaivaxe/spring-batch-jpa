package com.mpsg.student.batch.config.importbatch.reader.mapper;

import com.mpsg.student.batch.domain.Student;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static com.mpsg.student.batch.config.importbatch.reader.mapper.Positions.*;

@Component
public class StudentLineMapper implements LineMapper<Student> {

  @Override
  public Student mapLine(String line, int lineNumber) throws Exception {
    var mapper = defaultLineMapper();
    mapper.setFieldSetMapper(fieldSet -> Student
                               .builder()
                               .firstName(fieldSet.readString(FIRST_NAME.getColumn()))
                               .lastName(fieldSet.readString(LAST_NAME.getColumn()))
                               .birthDate(toLocalDate(fieldSet.readDate(BIRTH_DATE.getColumn())))
                               .build()
                            );
    return mapper.mapLine(line, lineNumber);
  }

  private FixedLengthTokenizer fixedLengthTokenizer() {
    var tokenizer = new FixedLengthTokenizer();
    var ranges = Positions.getRangeLineFile();
    tokenizer.setColumns(ranges);
    return tokenizer;
  }

  private DefaultLineMapper<Student> defaultLineMapper() {
    var mapper = new DefaultLineMapper<Student>();
    mapper.setLineTokenizer(fixedLengthTokenizer());
    return mapper;
  }

  private LocalDate toLocalDate(Date date) {
    return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }
}