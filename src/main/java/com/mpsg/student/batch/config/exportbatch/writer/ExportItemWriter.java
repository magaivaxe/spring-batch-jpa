package com.mpsg.student.batch.config.exportbatch.writer;

import com.mpsg.student.batch.domain.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamWriter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ExportItemWriter {

  private final JacksonJsonObjectMarshaller<Student> marshaller;

  public SynchronizedItemStreamWriter<Student> itemWriter() {
    var writer = new JsonFileItemWriterBuilder<Student>()
      .name("jsonFileItemWriter")
      .jsonObjectMarshaller(marshaller)
      .resource(new FileSystemResource("export.txt"))
      .saveState(false)
      .build();
    var syncWriter = new SynchronizedItemStreamWriter<Student>();
    syncWriter.setDelegate(writer);
    return syncWriter;
  }

}