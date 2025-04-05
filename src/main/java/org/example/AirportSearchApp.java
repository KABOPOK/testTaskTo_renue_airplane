package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AirportSearchApp {
  CommandLineArgs cmdArgs;
  private AirportRecords records;
  private boolean isColumnNumeric;
  private long initTime;
  private boolean isSorted;

  AirportSearchApp(CommandLineArgs cmdArgs) throws IOException {
    this.cmdArgs = cmdArgs;
    records = loadData(cmdArgs.getDataFile(), cmdArgs.getColumnIndex());
  }

  private static AirportRecords loadData(String csvFilePath, int columnId) throws IOException {
    if (columnId < 0) {
      throw new IllegalArgumentException("Column index must be non-negative");
    }
    int lineCount = 0;
    try (BufferedReader counter = new BufferedReader(new FileReader(csvFilePath))) {
      while (counter.readLine() != null) {
        ++lineCount;
      }
    }
    IntegerVector rowNumbers = new IntegerVector(lineCount * 21);
    StringBuilder valueBuilder = new StringBuilder(lineCount * 21);
    try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
      String line;
      int validLines = 0;
      while ((line = reader.readLine()) != null) {
        if (line.trim().isEmpty()) {
          continue;
        }
        String[] values = parseCSVLine(line);
        try {
          int rowNumber = Integer.parseInt(values[0].replace("\"", "").trim());
          for (int i = validLines; i <= validLines + values[columnId].length(); ++i) {
            rowNumbers.add(rowNumber);
          }
          valueBuilder.append(values[columnId]).append(" ");
          ++validLines;
        } catch (NumberFormatException e) {
          throw new IllegalArgumentException("First column must contain valid integer row number in line: " + line);
        }
      }
    }
    return new AirportRecords(rowNumbers, valueBuilder.toString());
  }

  private static String[] parseCSVLine(String line) {
    int estimatedFields = 1;
    for (int i = 0; i < line.length(); i++) {
      if (line.charAt(i) == ',') estimatedFields++;
    }
    String[] result = new String[estimatedFields];
    int fieldIndex = 0;
    StringBuilder currentField = new StringBuilder(line.length() / estimatedFields);
    boolean inQuotes = false;
    for (int i = 0; i < line.length(); i++) {
      char c = line.charAt(i);
      if (c == '"') {
        if (inQuotes && i < line.length() - 1 && line.charAt(i + 1) == '"') {
          currentField.append('"');
          i++;
        } else {
          inQuotes = !inQuotes;
        }
      } else if (c == ',' && !inQuotes) {
        result[fieldIndex++] = currentField.toString();
        currentField.setLength(0);
      } else {
        currentField.append(c);
      }
    }
    result[fieldIndex] = currentField.toString();
    if (fieldIndex < estimatedFields - 1) {
      String[] trimmedResult = new String[fieldIndex + 1];
      System.arraycopy(result, 0, trimmedResult, 0, fieldIndex + 1);
      return trimmedResult;
    }
    return result;
  }

  public void findSubstr(String substr) {
    SuffixArray suffixArray = new SuffixArray(records.getValue());
    List<Integer> anw = suffixArray.findSubstring(substr);
    for (Integer integer : anw) {
      System.out.println(records.getRowNumber().get(integer));
    }
  }
}
