package org.example;

public class Main {


  public static void main(String[] args) {
    try {
      CommandLineArgs cmdArgs = new CommandLineArgs(args);
      AirportSearchApp airportSearchApp = new AirportSearchApp(cmdArgs);
      airportSearchApp.findSubstr("Bow");
      System.out.println("Bow");
    } catch (Exception e){
      e.printStackTrace();
    }

  }

}