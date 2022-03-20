package com.mpsg.student.batch.config.importbatch.reader.mapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.transform.Range;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum Positions {
  FIRST_NAME(0, 1, 50, 50),
  LAST_NAME(1, 51, 100, 50),
  BIRTH_DATE(2, 101, 110, 10);

  private final int column;
  private final int startRange;
  private final int endRange;
  private final int size;

  public static Range[] getRangeLineFile() {
    return Arrays.stream(Positions.values()).map(position -> new Range(position.startRange, position.endRange))
                 .collect(Collectors.toList()).toArray(Range[]::new);
  }
}