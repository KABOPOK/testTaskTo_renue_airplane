package org.example;

public class Main {


  public static void main(String[] args) {
    try {
      AirportSearchApp airportSearchApp = new AirportSearchApp(args);
      airportSearchApp.findSubstrsAndWriteToFile();
      System.out.println("Bow");
    } catch (Exception e){
      e.printStackTrace();
    }

  }

}