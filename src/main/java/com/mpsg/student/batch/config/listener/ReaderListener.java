package com.mpsg.student.batch.config.listener;

import com.mpsg.student.batch.database.entity.StudentDbo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReaderListener implements ItemReadListener<StudentDbo> {
  @Override
  public void beforeRead() {
  }

  @Override
  public void afterRead(StudentDbo item) {
  }

  @Override
  public void onReadError(Exception ex) {
    log.error("Error : {} \n cause: {}", ex.getMessage(), ex.getCause());
  }
}