package org.example;

import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SearchResult {
  private long initTime;
  private List<SearchData> result;

  public SearchResult(long initTime, List<SearchData> result) {
    this.initTime = initTime;
    this.result = result;
  }

  // Getters and setters for initTime and result
  public long getInitTime() {
    return initTime;
  }

  public void setInitTime(long initTime) {
    this.initTime = initTime;
  }

  public List<SearchData> getResult() {
    return result;
  }

  public void setResult(List<SearchData> result) {
    this.result = result;
  }

  // Method to print the result to console
  public void printToConsole() {
    Gson gson = new Gson();
    String json = gson.toJson(this);
    System.out.println(json);
  }

  // Method to write the result to an output file
  public void writeToFile(String filename) throws IOException {
    Gson gson = new Gson();
    try (FileWriter writer = new FileWriter(filename)) {
      gson.toJson(this, writer);
    }
  }
  public static class SearchData {
    private String search;
    private List<Integer> result;
    private long time;

    public SearchData(String search, List<Integer> result, long time) {
      this.search = search;
      this.result = result;
      this.time = time;
    }

    public String getSearch() {
      return search;
    }

    public void setSearch(String search) {
      this.search = search;
    }

    public List<Integer> getResult() {
      return result;
    }

    public void setResult(List<Integer> result) {
      this.result = result;
    }

    public long getTime() {
      return time;
    }

    public void setTime(long time) {
      this.time = time;
    }
  }
}

