package org.example;

import org.example.Classes.DataArgs;
import org.example.Parser.CmdParser;
import org.example.Searcher.CSVSubstrSearcher;

public class Main {
  public static void main(String[] args) {
    try {
      CmdParser cmdParser = new CmdParser();
      DataArgs data = cmdParser.getData(args);
      CSVSubstrSearcher csvSubstrSearcher = new CSVSubstrSearcher(data.getInFile(), data.getColumn());
      csvSubstrSearcher.findSubstrsAndWriteToFile(data.getTargetFile(), data.getOutFile());
    } catch (Exception e){
      e.printStackTrace();
    }

  }

}