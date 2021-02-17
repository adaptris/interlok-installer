package com.adaptris.installer.utils;

import com.adaptris.installer.helpers.LogHelper;

public class NumberUtils {
  private static LogHelper LOG = LogHelper.getInstance();

  private NumberUtils() {
  }

  public static Double toDouble(String str) {
    try {
      return Double.parseDouble(str);
    } catch (Exception expts) {
      LOG.error(str + " is not a valid Double: " + expts.getLocalizedMessage());
    }
    return null;
  }

}
