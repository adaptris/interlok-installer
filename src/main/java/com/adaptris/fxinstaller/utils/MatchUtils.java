package com.adaptris.fxinstaller.utils;

import org.gradle.internal.impldep.org.apache.commons.lang.StringUtils;

public class MatchUtils {

  private MatchUtils() {
  }

  public static boolean match(String str, String... matchers) {
    if (StringUtils.isBlank(str)) {
      return true;
    }

    String lowerCaseFilter = str.toLowerCase();

    for (String matcher : matchers) {
      boolean match = matcher.toLowerCase().contains(lowerCaseFilter);
      if (match) {
        return true;
      }
    }

    return false;
  }

}
