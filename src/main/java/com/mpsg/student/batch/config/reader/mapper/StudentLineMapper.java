package com.mpsg.student.batch.config.reader.mapper;

import com.mpsg.student.batch.entity.Student;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.stereotype.Component;

import static com.mpsg.student.batch.config.reader.mapper.Positions.*;

@Component
public class StudentLineMapper implements LineMapper<Student> {

  @Override
  public Student mapLine(String line, int lineNumber) throws Exception {
    DefaultLineMapper<Student> mapper = defaultLineMapper();
    mapper.setFieldSetMapper(fieldSet -> Student
                               .builder()
                               .firstName(fieldSet.readString(FIRST_NAME.getColumn()))
                               .lastName(fieldSet.readString(LAST_NAME.getColumn()))
                               .birthDate(fieldSet.readDate(BIRTH_DATE.getColumn()))
                               .build()
                            );
    return mapper.mapLine(line, lineNumber);
  }

  private FixedLengthTokenizer fixedLengthTokenizer() {
    FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
    var ranges = Positions.getRangeLineFile();
    tokenizer.setColumns(ranges);
    return tokenizer;
  }

  private DefaultLineMapper<Student> defaultLineMapper() {
    var mapper = new DefaultLineMapper<Student>();
    mapper.setLineTokenizer(fixedLengthTokenizer());
    return mapper;
  }
}