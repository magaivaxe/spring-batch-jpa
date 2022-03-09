package com.mpsg.student.batch.config.importation.reader;

import com.mpsg.student.batch.config.importation.reader.mapper.StudentLineMapper;
import com.mpsg.student.batch.domain.Student;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class ImportStudentReader extends FlatFileItemReader<Student> {

  public ImportStudentReader(StudentLineMapper studentLineMapper,
                             @Value("${batch.file.path}") String filePath) {
    super();
    setName("studentItemReader");
    setResource(new ClassPathResource(filePath));
    setLineMapper(studentLineMapper);
  }
}