package org.example;

public class CommandLineArgs {

  private final String[] args = new String[4];

  CommandLineArgs(String[] cliArgs){
    if(!isValid(cliArgs)) throw new RuntimeException("Usage: --data <file.csv> --indexed-column-id <N> --input-file <in.txt> --output-file <out.json>");
    setArgs(cliArgs);
  }
  private void setArgs(String[] cliArgs){
    for (int i = 0; i < cliArgs.length; ++i) {
      String arg = cliArgs[i];
      if (arg.equals("--data") && i + 1 < cliArgs.length) {
        args[0] = cliArgs[++i];
      } else if (arg.equals("--indexed-column-id") && i + 1 < cliArgs.length) {
        args[1] = cliArgs[++i];
      } else if (arg.equals("--input-file") && i + 1 < cliArgs.length) {
        args[2] = cliArgs[++i];
      } else if (arg.equals("--output-file") && i + 1 < cliArgs.length) {
        args[3] = cliArgs[++i];
      }
    }
  }
  public boolean validate(){
    return false;
  }
  private static boolean isValid(String[] cliArgs) {
    return cliArgs[0].equals("--data") &&
            cliArgs[2].equals("--indexed-column-id") &&
            cliArgs[4].equals("--input-file") &&
            cliArgs[6].equals("--output-file");
  }
  public String getDataFile() { return args[0]; }
  public int getColumnIndex() { return Integer.parseInt(args[1]) - 1; }
  public String getInputFile() { return args[2]; }
  public String getOutputFile() { return args[3]; }

}
