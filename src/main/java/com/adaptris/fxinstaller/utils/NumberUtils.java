package com.adaptris.fxinstaller.utils;

public class NumberUtils {

  private NumberUtils() {
  }

  public static Double toDouble(String str) {
    try {
      return Double.parseDouble(str);
    } catch (Exception expts) {
      System.out.println(str + " is not a valid Double: " + expts.getLocalizedMessage());
    }
    return null;
  }

}
