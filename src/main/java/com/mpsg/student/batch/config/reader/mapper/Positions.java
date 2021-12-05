package com.mpsg.student.batch.config.reader.mapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Positions {
  FIRST_NAME(49),
  LAST_NAME(99),
  BIRTH_DATE(109);

  private final int endIndex;
}