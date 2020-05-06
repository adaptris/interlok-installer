package com.adaptris.fxinstaller.utils;

import org.gradle.internal.impldep.org.apache.commons.lang.StringUtils;

public class VersionUtils {

  private VersionUtils() {
  }

  public static boolean isSnapshot(String version) {
    return StringUtils.trimToEmpty(version).toUpperCase().endsWith("-SNAPSHOT");
  }

  public static boolean isBeta(String version) {
    return StringUtils.trimToEmpty(version).toUpperCase().matches(".*B[0-9]+.*-RELEASE");
  }

}
