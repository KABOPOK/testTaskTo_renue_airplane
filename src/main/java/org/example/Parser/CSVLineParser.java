package org.example.Parser;

public class CSVLineParser {

  public String[] parseCSVLine(String line) {
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

}
