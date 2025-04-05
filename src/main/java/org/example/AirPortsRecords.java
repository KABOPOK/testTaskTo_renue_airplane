//package org.example;
//import java.io.*;
//import java.util.*;
//
//public class AirportSearchApp {
//  private final List<AirportRecord> records;
//  private final List<String> a= n
//  private final boolean isColumnNumeric;
//  private long initTime;
//
//  public AirportSearchApp(String dataFile, int indexedColumnId) throws IOException {
//    long startTime = System.currentTimeMillis();
//    this.records = new ArrayList<>();
//    boolean isNumericColumn = true;
//
//    try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
//      String line;
//      while ((line = reader.readLine()) != null) {
//        String[] columns = parseCsvLine(line);
//        if (columns.length < indexedColumnId) continue;
//
//        int rowNumber = Integer.parseInt(columns[0].replace("\"", ""));
//        String value = columns[indexedColumnId - 1].replace("\"", "");
//
//        double numericValue = 0;
//        boolean isNumeric = false;
//        try {
//          numericValue = Double.parseDouble(value);
//          isNumeric = true;
//        } catch (NumberFormatException e) {
//          isNumericColumn = false;
//        }
//
//        records.add(new AirportRecord(rowNumber, value, isNumeric, numericValue));
//      }
//    }
//
//    this.isColumnNumeric = isNumericColumn;
//    sortRecords();
//    this.initTime = System.currentTimeMillis() - startTime;
//  }
//
//  public List<Integer> search(String prefix) {
//    long startTime = System.currentTimeMillis();
//    List<Integer> result = new ArrayList<>();
//
//    if (isColumnNumeric) {
//      try {
//        double numericPrefix = Double.parseDouble(prefix);
//        int index = Collections.binarySearch(records,
//                new AirportRecord(0, prefix, true, numericPrefix),
//                Comparator.comparingDouble(AirportRecord::getNumericValue));
//
//        if (index >= 0) {
//          result.add(records.get(index).getRowNumber());
//        }
//      } catch (NumberFormatException ignored) {}
//    } else {
//      int index = findFirstPrefixIndex(prefix);
//      if (index != -1) {
//        while (index < records.size() && records.get(index).getValue().startsWith(prefix)) {
//          result.add(records.get(index).getRowNumber());
//          index++;
//        }
//      }
//    }
//
//    return result;
//  }
//
//  // Остальные методы (sortRecords, findFirstPrefixIndex, parseCsvLine) остаются без изменений
//  // ...
//
//  public long getInitTime() {
//    return initTime;
//  }
//}
