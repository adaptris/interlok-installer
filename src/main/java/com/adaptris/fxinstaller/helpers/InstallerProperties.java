package com.adaptris.fxinstaller.helpers;

import java.util.Properties;

import com.adaptris.fxinstaller.utils.PropertiesUtils;

public class InstallerProperties {

  public static final String PROPERTIES_FILE = "installer.properties";
  public static final String INTERLOK_VERSION = "interlok.version";
  public static final String INSTALL_DIR_WINDOWS = "install.directory.windows";
  public static final String INSTALL_DIR_LINUX = "install.directory.linux";

  public static final String ADDITIONAL_NEXUS_BASE_URL = "additionalNexusBaseUrl";

  private static InstallerProperties INSTANCE = new InstallerProperties();

  private Properties properties;

  public static InstallerProperties getInstance() {
    return INSTANCE;
  }

  private InstallerProperties() {
    properties = PropertiesUtils.loadFromStreamQuietly(getClass().getClassLoader(), PROPERTIES_FILE);
  }

  public String getVersion() {
    return getProperty(INTERLOK_VERSION);
  }

  public String getInstallDir() {
    return getInstallDir(System.getProperty("os.name"));
  }

  private String getInstallDir(String osName) {
    return osName.toLowerCase().contains("win") ? getWindowsInstallDir() : getLinuxInstallDir();
  }

  public String getWindowsInstallDir() {
    return getProperty(INSTALL_DIR_WINDOWS);
  }

  public String getLinuxInstallDir() {
    return getProperty(INSTALL_DIR_LINUX);
  }

  public String getAdditionalNexusBaseUrl() {
    return System.getProperty(ADDITIONAL_NEXUS_BASE_URL);
  }

  public String getProperty(String key) {
    return properties.getProperty(key);
  }

  public String getProperty(String key, String defaultValue) {
    return properties.getProperty(key, defaultValue);
  }

  public Properties getProperties() {
    Properties newProperties = new Properties();
    properties.forEach((key, value) -> {
      newProperties.put(key, value);
    });
    return newProperties;
  }

}
