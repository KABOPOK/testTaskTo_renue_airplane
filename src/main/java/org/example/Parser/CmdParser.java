package org.example.Parser;

import org.example.Classes.DataArgs;

public class CmdParser implements cmdArgumentParser {

  @Override
  public DataArgs getData(String[] cliArgs){
    DataArgs dataArgs = new DataArgs();
    for (int i = 0; i < cliArgs.length; ++i) {
      String arg = cliArgs[i];
      if (arg.equals("--data") && i + 1 < cliArgs.length) {
        dataArgs.setInFile(cliArgs[++i]);
      } else if (arg.equals("--indexed-column-id") && i + 1 < cliArgs.length) {
        dataArgs.setColumn(cliArgs[++i]);
      } else if (arg.equals("--input-file") && i + 1 < cliArgs.length) {
        dataArgs.setTargetFile(cliArgs[++i]);
      } else if (arg.equals("--output-file") && i + 1 < cliArgs.length) {
        dataArgs.setOutFile(cliArgs[++i]);
      }
    }
    return dataArgs;
  }
  @Override
  public boolean isValid(String[] cmdArgs) {
    return cmdArgs[0].equals("--data") &&
            cmdArgs[2].equals("--indexed-column-id") &&
            cmdArgs[4].equals("--input-file") &&
            cmdArgs[6].equals("--output-file");
  }

}
