package com.mpsg.student.batch.config.exportbatch.reader;

import com.mpsg.student.batch.database.entity.StudentDbo;
import com.mpsg.student.batch.database.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;

@RequiredArgsConstructor
@Component
public class ExportItemReader {

  private final StudentRepository studentRepository;
  @Value("${batch.configuration.chunkSize}")
  private int chunkSize;

  public RepositoryItemReader<StudentDbo> itemReader() {
    var reader = new RepositoryItemReader<StudentDbo>();
    reader.setRepository(studentRepository);
    reader.setPageSize(chunkSize);
    reader.setName("exportItemReader");
    reader.setMethodName("findByBirthDateBetween");
    reader.setArguments(Arrays.asList(Date.valueOf(LocalDate.of(2022, 1, 1)),
                                      Date.valueOf(LocalDate.now().plusDays(1))));
    reader.setSort(new HashMap<>());
    reader.setSaveState(false);
    return reader;
  }
}