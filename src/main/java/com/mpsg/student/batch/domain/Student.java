package com.mpsg.student.batch.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder(toBuilder = true)
public class Student {
  private final String firstName;
  private final String lastName;
  private final LocalDate birthDate;
}