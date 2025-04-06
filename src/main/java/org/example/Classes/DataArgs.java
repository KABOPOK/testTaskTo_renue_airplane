package org.example.Classes;

public class DataArgs {

  private String inFile;
  private String targetFile;
  private String outFile;
  private String column;

  public void setInFile(String inFile) {
    this.inFile = inFile;
  }

  public void setTargetFile(String targetFile) {
    this.targetFile = targetFile;
  }

  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }

  public void setColumn(String column) {
    this.column = column;
  }

  public String getInFile() {
    return inFile;
  }

  public String getTargetFile() {
    return targetFile;
  }

  public String getOutFile() {
    return outFile;
  }

  public String getColumn() {
    return column;
  }

}
