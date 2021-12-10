package com.mpsg.student.batch.config.reader.mapper;

import com.mpsg.student.batch.database.entity.StudentDbo;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.stereotype.Component;

import static com.mpsg.student.batch.config.reader.mapper.Positions.*;

@Component
public class StudentLineMapper implements LineMapper<StudentDbo> {

  @Override
  public StudentDbo mapLine(String line, int lineNumber) throws Exception {
    DefaultLineMapper<StudentDbo> mapper = defaultLineMapper();
    mapper.setFieldSetMapper(fieldSet -> StudentDbo
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

  private DefaultLineMapper<StudentDbo> defaultLineMapper() {
    var mapper = new DefaultLineMapper<StudentDbo>();
    mapper.setLineTokenizer(fixedLengthTokenizer());
    return mapper;
  }
}