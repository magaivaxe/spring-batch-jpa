package com.mpsg.student.batch.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import static com.mpsg.student.batch.config.importation.reader.mapper.Positions.FIRST_NAME;
import static com.mpsg.student.batch.config.importation.reader.mapper.Positions.LAST_NAME;

public class GenerateTestFile {

  public static void main(String[] args) throws IOException {
    var listStudents = new ArrayList<String>();

    for (int i = 0; i < 10000; i++) {
      var studentLine = FIRST_NAME.name()
                                  .concat(String.valueOf(i))
                                  .concat(generateSpaces(i, FIRST_NAME.name(), FIRST_NAME.getSize()))
                                  .concat(LAST_NAME.name())
                                  .concat(String.valueOf(i))
                                  .concat(generateSpaces(i, LAST_NAME.name(), LAST_NAME.getSize()))
                                  .concat(generateDate());
      listStudents.add(studentLine);
    }
    var path = Paths.get("src/main/resources/samples/sample_test.csv");
    Files.write(path, listStudents);
  }

  private static String generateDate() {
    var minDate = Date.from(LocalDate.of(1950, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
    var maxDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
    return betweenDates(minDate, maxDate);
  }

  private static String betweenDates(Date minDate, Date maxDate) {
    var start = minDate.getTime();
    var end = maxDate.getTime();
    var random = ThreadLocalRandom.current().nextLong(start, end);
    var randomDate = new Date(random);
    return new SimpleDateFormat("yyyy-MM-dd").format(randomDate);
  }

  private static String generateSpaces(int i, String text, int fieldSize) {
    var totalSize = String.valueOf(i).length() + text.length();
    var totalSpaces = fieldSize - totalSize;
    var spaces = "";
    for (int j = 0; j < totalSpaces; j++) {
      spaces = spaces.concat(" ");
    }
    return spaces;
  }
}