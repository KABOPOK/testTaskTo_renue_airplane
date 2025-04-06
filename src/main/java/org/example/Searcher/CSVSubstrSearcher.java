package org.example.Searcher;

import org.example.Classes.AirportRecords;
import org.example.Classes.IntegerVector;
import org.example.Parser.CSVLineParser;
import org.example.Classes.SearchResult;
import org.example.Classes.SuffixArray;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVSubstrSearcher implements SubstrInStrSearcher {
  private final AirportRecords records;

  private static CSVLineParser csvLineParser;

  private final String dataFile;

  private SuffixArray suffixArray;

  private boolean isBuildSuffixArray;

  private long initTime;

  public CSVSubstrSearcher(String dataFile, String column) throws IOException {
    long startTime = System.currentTimeMillis();
    csvLineParser = new CSVLineParser();
    records = loadData(dataFile, Integer.parseInt(column) - 1);
    this.dataFile = dataFile;
    this.isBuildSuffixArray = false;
    long endTime = System.currentTimeMillis();
    initTime = endTime - startTime;
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
      while ((line = reader.readLine()) != null) {
        if (line.trim().isEmpty()) {
          continue;
        }
        String[] values = csvLineParser.parseCSVLine(line);
        if(!(columnId < values.length)){
          throw new IllegalArgumentException("Invalid Index of column");
        }
        try {
          int rowNumber = Integer.parseInt(values[0].replace("\"", "").trim());
          for (int i = 0; i <=  values[columnId].length(); ++i) {
            rowNumbers.add(rowNumber);
          }
          valueBuilder.append(values[columnId]).append(" ");
        } catch (NumberFormatException e) {
          throw new IllegalArgumentException("First column must contain valid integer row number in line: " + line);
        }
      }
    }
    return new AirportRecords(rowNumbers, valueBuilder.toString());
  }


  @Override
  public List<Integer> findSubstr(String substr) {
    boolean isBuildSuffixArray = false;
    if(!isBuildSuffixArray) {
      long startTime = System.currentTimeMillis();
      suffixArray = new SuffixArray(records.getValue());
      long endTime = System.currentTimeMillis();
      initTime += endTime - startTime;
    }
    List<Integer> anw = suffixArray.findSubstring(substr);
    for (Integer integer : anw) {
      System.out.println(records.getRowNumber().get(integer));
    }
    return anw;
  }

  public SearchResult findSubstrsAndWriteToFile(String targetFile, String outputFile) throws IOException {
    if(!isBuildSuffixArray) {
      isBuildSuffixArray = true;
      long startTime = System.currentTimeMillis();
      suffixArray = new SuffixArray(records.getValue());
      long endTime = System.currentTimeMillis();
      initTime += endTime - startTime;
    }
    List<SearchResult.SearchData> SearchDataset = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(targetFile))) {
      String line;
      while ((line = reader.readLine()) != null) {
        long startTime = System.currentTimeMillis();
        List<Integer> anw = suffixArray.findSubstring(line);
        anw.replaceAll(index -> records.getRowNumber().get(index));
        long endTime = System.currentTimeMillis();
        long searchTime = endTime - startTime;
        System.out.println("Search for: " + line + " | Found: " + anw + " | Time: " + searchTime + " ms");
        SearchDataset.add(new SearchResult.SearchData(line, anw, searchTime));
      }

    } catch (IOException e) {
      e.printStackTrace();
      throw new IOException("Error reading input file", e);
    }
    SearchResult result = new SearchResult(initTime, SearchDataset);
    result.writeToFile(outputFile);
    return result;
  }

}
