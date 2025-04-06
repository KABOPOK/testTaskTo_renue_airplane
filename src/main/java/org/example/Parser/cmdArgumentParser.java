package org.example.Parser;

public interface cmdArgumentParser {

  Object getData(String[] cliArgs);
  default boolean isValid(String[] cmdArgs){
    return true;
  }

}
