package com.mpsg.student.batch.config.exportbatch.listener;

import com.mpsg.student.batch.domain.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ExportWriterListener implements ItemWriteListener<Student> {
  @Override
  public void beforeWrite(List<? extends Student> items) {
  }

  @Override
  public void afterWrite(List<? extends Student> items) {
    log.info("LIST SIZE: ----> {}", items.size());
  }

  @Override
  public void onWriteError(Exception exception, List<? extends Student> items) {

  }
}