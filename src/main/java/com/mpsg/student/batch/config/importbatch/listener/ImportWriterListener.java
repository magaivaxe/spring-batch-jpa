package com.mpsg.student.batch.config.importbatch.listener;

import com.mpsg.student.batch.database.entity.StudentDbo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ImportWriterListener implements ItemWriteListener<StudentDbo> {
  @Override
  public void beforeWrite(List<? extends StudentDbo> items) {
  }

  @Override
  public void afterWrite(List<? extends StudentDbo> items) {
  }

  @Override
  public void onWriteError(Exception exception, List<? extends StudentDbo> students) {
    log.error("Failed writing students in collection size {}: \n first {} \n and last {}",
              students.size(),
              students.get(0).toString(),
              students.get(students.size() - 1).toString());
  }
}