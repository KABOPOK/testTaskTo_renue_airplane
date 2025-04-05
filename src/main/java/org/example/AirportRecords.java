package org.example;

import javax.xml.transform.Templates;
import java.util.List;

public class AirportRecords {
  private final IntegerVector rowNumber;
  private final String value;

  public AirportRecords(IntegerVector rowNumber, String value) {
    this.rowNumber = rowNumber;
    this.value = value;
  }

  public String getValue() { return value; }
  public IntegerVector getRowNumber() { return rowNumber; }
}
