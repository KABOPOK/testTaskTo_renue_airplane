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

//  private static AirportRecords loadData(String csvFilePath, int columnId) throws IOException {
//    AirportRecords records = new AirportRecords();
//    if (columnId < 0) {
//      throw new IllegalArgumentException("Column index must be non-negative");
//    }
//    try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
//      String line;
//      int rowNumber = 0;
//      while ((line = reader.readLine()) != null) {
//        // Skip empty lines
//        if (line.trim().isEmpty()) {
//          continue;
//        }
//        String[] values = parseCSVLine(line);
//        // Check if column index is valid
//        String a = Arrays.toString(values);
//        try {
//          rowNumber = Integer.parseInt(values[0].replace("\"", "").trim());
//        } catch (NumberFormatException e) {
//          throw new IllegalArgumentException(
//                  "First column must contain valid integer row number in line: " + line
//          );
//        }
//        if (columnId >= values.length) {
//          throw new IllegalArgumentException(
//                  "Column index " + columnId + " exceeds available columns (" +
//                          values.length + ") at row " + rowNumber
//          );
//        }
////        if(values[columnId].contains("Bow") ||rowNumber == 3600){
////          System.out.println("halo");
////        }
//        records.add(new AirportRecord(rowNumber, values[columnId]));
//      }
//    }
//    return records;
//  }
  private static AirportRecords loadData(String csvFilePath, int columnId) throws IOException {
    if (columnId < 0) {
      throw new IllegalArgumentException("Column index must be non-negative");
    }

    // First pass: count lines to pre-allocate arrays
    int lineCount = 0;
    try (BufferedReader counter = new BufferedReader(new FileReader(csvFilePath))) {
      while (counter.readLine() != null) {
        ++lineCount;
      }
    }

    // Pre-allocate arrays to avoid resizing
    List<Integer> rowNumbers = new ArrayList<Integer>(lineCount * 21);

    // Use StringBuilder with initial capacity for the continuous string
    StringBuilder valueBuilder = new StringBuilder(lineCount * 21); // Assuming average 20 chars per field

    // Second pass: actual data loading
    try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
      String line;
      int validLines = 0;

      while ((line = reader.readLine()) != null) {
        // Skip empty lines
        if (line.trim().isEmpty()) {
          continue;
        }

        String[] values = parseCSVLine(line);

        try {
          // Parse row number from first column
          int rowNumber = Integer.parseInt(values[0].replace("\"", "").trim());

          // Store row number
          for(int i = validLines; i <= validLines + values[columnId].length(); ++i){
            rowNumbers.add(rowNumber);
          }
          valueBuilder.append(values[columnId]).append(" ");
          ++validLines;
        } catch (NumberFormatException e) {
          throw new IllegalArgumentException(
                  "First column must contain valid integer row number in line: " + line
          );
        }
      }
    }

    // Create and return the result
    return new AirportRecords(rowNumbers, valueBuilder.toString());
  }
  private static String[] parseCSVLine(String line) {
    // Pre-allocate result array based on approximate field count
    // Estimating number of fields by counting commas and adding 1
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
        // Fast path: Check for escaped quotes (double quote) directly
        if (inQuotes && i < line.length() - 1 && line.charAt(i + 1) == '"') {
          currentField.append('"');
          i++; // Skip the next quote
        } else {
          // Toggle quote state
          inQuotes = !inQuotes;
        }
      } else if (c == ',' && !inQuotes) {
        // End of field - add to result array directly
        result[fieldIndex++] = currentField.toString();
        currentField.setLength(0);
      } else {
        currentField.append(c);
      }
    }

    // Add the last field
    result[fieldIndex] = currentField.toString();

    // If we overestimated the number of fields, trim the array
    if (fieldIndex < estimatedFields - 1) {
      String[] trimmedResult = new String[fieldIndex + 1];
      System.arraycopy(result, 0, trimmedResult, 0, fieldIndex + 1);
      return trimmedResult;
    }

    return result;
  }

  public void findSubstr(String substr){
    SuffixArray suffixArray = new SuffixArray(records.getValue());
    List<Integer> anw = suffixArray.findSubstring(substr);
    for (Integer integer : anw) {
      System.out.println(records.getRowNumber().get(integer));
    }
  }

//  private void sortRecords() {
//
//    // Sort using appropriate comparator based on data type
//    records.sort(Comparator.comparing(record ->
//            Double.parseDouble(record.getValue())));
//
//    //might be faster. We can add few booleans: isColumnInt, isColumnInt .. and cast below
//  }
//
//  public List<Integer> findSubstring(String substring) {
//    List<Integer> result = new ArrayList<>();
//
//    // If records are not sorted, sort them first
//    if (!isSorted) {
//      sortRecords();
//      isSorted = true;
//    }
//
//    if (records.isEmpty() || substring == null || substring.isEmpty()) {
//      return result;
//    }
//
////    if (isColumnNumeric) {
////      for (AirportRecord record : records) {
////        String value = record.getValue().toString();
////        if (value.contains(searchSubstr)) {
////          result.add(record.getRowNumber());
////        }
////      }
////      return result;
////    }
//    int[] indexes = new int[substring.length()];
//    int i = 0;
//    for(; i < indexes.length; ++i) {
//      int left = 0;
//      int right = records.size() - 1;
//      while (left <= right) {
//        int mid = left + (right - left) / 2;
//        if(right - left <= 1) { indexes[i] = -1; break; }
//        char midValue = records.get(mid).getValue().charAt(i);
//        if(midValue > substring.charAt(i)){
//          right = mid;
//        }
//        else if(midValue < substring.charAt(i)){
//          left = mid;
//        }
//        else if (midValue == substring.charAt(i)){
//          indexes[i] = mid;
//          break;
//        }
//      }
//      if(indexes[i] == -1){break;}
//    }
//    --i;
//    if(indexes[i] != -1) {
//      //down
//      int point = indexes[i];
//      result.add(records.get(indexes[i]).getRowNumber());
//      ++point;
//      while (records.get(point).getValue().length() > i
//              && records.get(point).getValue().charAt(i) == records.get(indexes[i]).getValue().charAt(i)) {
//        result.add(records.get(indexes[point]).getRowNumber());
//        ++point;
//      }
//      //up
//      point = indexes[i];
//      --point;
//      while (records.get(point).getValue().length() > i
//              && records.get(point).getValue().charAt(i) == records.get(indexes[i]).getValue().charAt(i)) {
//        result.add(records.get(indexes[point]).getRowNumber());
//        --point;
//      }
//    }
//    return result;
//  }
//  private boolean isPotentialMatch(String value, String substring) {
//    // The value could contain the substring if it's not lexicographically
//    // smaller than the substring and not lexicographically larger than
//    // the max possible string that could start with this substring
//
//    // Check if value is not less than substring
//    if (value.compareTo(substring) < 0) {
//      return false;
//    }
//
//    // Check if the value could start with the substring
//    // by comparing the prefix of the value with the substring
//    return value.length() >= substring.length() &&
//            value.substring(0, substring.length()).compareTo(substring) >= 0;
//  }
}
