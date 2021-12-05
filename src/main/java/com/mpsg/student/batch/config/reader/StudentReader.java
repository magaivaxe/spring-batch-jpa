package com.mpsg.student.batch.config.reader;

import com.mpsg.student.batch.config.reader.mapper.StudentLineMapper;
import com.mpsg.student.batch.entity.Student;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class StudentReader extends FlatFileItemReader<Student> {

  public StudentReader(StudentLineMapper studentLineMapper, @Value("${batch.file.path}") String filePath) {
    super.setName("studentItemReader");
    super.setResource(new ClassPathResource(filePath));
    super.setLineMapper(studentLineMapper);
  }
}