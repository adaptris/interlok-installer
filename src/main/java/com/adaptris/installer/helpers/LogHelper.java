package com.adaptris.installer.helpers;

public class LogHelper {

  private static LogHelper INSTANCE = new LogHelper();

  private LogHelper() {
  }

  public static LogHelper getInstance() {
    return INSTANCE;
  }

  public void info(String str) {
    System.out.println("INFO: " + str);
  }

  public void error(String str) {
    System.out.println("ERROR: " + str);
  }

  public void error(String str, Throwable throwable) {
    error(str);
    if (throwable != null) {
      throwable.printStackTrace();
    }
  }

}
