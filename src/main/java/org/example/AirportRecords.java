package org.example;

import javax.xml.transform.Templates;
import java.util.List;

public class AirportRecords {
  private final List<Integer> rowNumber;
  private final String value;

  public AirportRecords( List<Integer>rowNumber, String value) {
    this.rowNumber = rowNumber;
    this.value = value;
  }

  public String getValue() { return value; }
  public List<Integer> getRowNumber() { return rowNumber; }
}
