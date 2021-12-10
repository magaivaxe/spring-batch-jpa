package com.mpsg.student.batch.config.reader;

import com.mpsg.student.batch.config.reader.mapper.StudentLineMapper;
import com.mpsg.student.batch.database.entity.StudentDbo;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class StudentReader extends FlatFileItemReader<StudentDbo> {

  public StudentReader(StudentLineMapper studentLineMapper,
                       @Value("${batch.file.path}") String filePath) {
    super();
    setName("studentItemReader");
    setResource(new ClassPathResource(filePath));
    setLineMapper(studentLineMapper);
  }
}