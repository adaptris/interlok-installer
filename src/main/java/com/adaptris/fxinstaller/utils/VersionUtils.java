package com.adaptris.fxinstaller.utils;

import org.gradle.internal.impldep.org.apache.commons.lang.StringUtils;

public class VersionUtils {

  private VersionUtils() {
  }

  public static boolean isSnapshot(String version) {
    return StringUtils.trimToEmpty(version).toUpperCase().endsWith("-SNAPSHOT");
  }

}
