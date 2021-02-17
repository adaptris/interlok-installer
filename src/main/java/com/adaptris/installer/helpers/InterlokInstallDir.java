package com.adaptris.installer.helpers;

import java.util.Properties;

import org.gradle.internal.impldep.org.apache.commons.lang.StringUtils;

public enum InterlokInstallDir {

  WIN("win") {
    @Override
    public String getDir(Properties properties) {
      return getDir(properties, INSTALL_DIR_WINDOWS);
    }
  },
  MAC("mac") {
    @Override
    public String getDir(Properties properties) {
      return System.getProperty("user.home") + getDir(properties, INSTALL_DIR_MAC);
    }
  },
  LINUX("linux") {
    @Override
    public String getDir(Properties properties) {
      return getDir(properties, INSTALL_DIR_LINUX);
    }
  };

  public static final String INSTALL_DIR_WINDOWS = "install.directory.windows";
  public static final String INSTALL_DIR_MAC = "install.directory.mac";
  public static final String INSTALL_DIR_LINUX = "install.directory.linux";

  private String name;

  private InterlokInstallDir(String name) {
    this.name = name;
  }

  public static InterlokInstallDir find() {
    return find(System.getProperty("os.name"));
  }

  public static InterlokInstallDir find(String osName) {
    for (InterlokInstallDir installDir : values()) {
      if (StringUtils.trimToEmpty(osName).toLowerCase().contains(installDir.name)) {
        return installDir;
      }
    }
    // If we don't know we default to linux!?
    return LINUX;
  }

  public abstract String getDir(Properties properties);

  protected String getDir(Properties properties, String propertyKey) {
    return properties.getProperty(propertyKey);
  }

}
