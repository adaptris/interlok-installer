package com.adaptris.fxinstaller.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class InstallerPropertiesTest {

  @Test
  public void testGetVersion() {
    String version = InstallerProperties.getInstance().getVersion();

    assertEquals("3.9.2-RELEASE", version);
  }

  @Test
  public void testInstallDirWindows() {
    String osName = System.getProperty("os.name");
    try {
      System.setProperty("os.name", "windows");
      String installDir = InstallerProperties.getInstance().getInstallDir();

      assertEquals("C:\\Adaptris\\Interlok", installDir);
    } finally {
      System.setProperty("os.name", osName);
    }
  }

  @Test
  public void testGetWindowsInstallDir() {
    String installDir = InstallerProperties.getInstance().getWindowsInstallDir();

    assertEquals("C:\\Adaptris\\Interlok", installDir);
  }

  @Test
  public void testInstallDirLinux() {
    String osName = System.getProperty("os.name");
    try {
      System.setProperty("os.name", "linux");
      String installDir = InstallerProperties.getInstance().getInstallDir();

      assertEquals("/opt/Adaptris/Interlok", installDir);
    } finally {
      System.setProperty("os.name", osName);
    }
  }

  @Test
  public void testGetLinuxInstallDir() {
    String installDir = InstallerProperties.getInstance().getLinuxInstallDir();

    assertEquals("/opt/Adaptris/Interlok", installDir);
  }

  @Test
  public void testGetAdditionalNexusBaseUrl() {
    String additionalNexusBaseUrl = InstallerProperties.getInstance().getAdditionalNexusBaseUrl();

    assertNull(additionalNexusBaseUrl);
  }

  @Test
  public void testGetAdditionalNexusBaseUrlSet() {
    System.setProperty(InstallerProperties.ADDITIONAL_NEXUS_BASE_URL, "https://nexus.adaptris.net");

    try {
      String additionalNexusBaseUrl = InstallerProperties.getInstance().getAdditionalNexusBaseUrl();

      assertEquals("https://nexus.adaptris.net", additionalNexusBaseUrl);
    } finally {
      System.clearProperty(InstallerProperties.ADDITIONAL_NEXUS_BASE_URL);
    }
  }

  @Test
  public void testGetProperty() {
    String property = InstallerProperties.getInstance().getProperty("nexus.base.url");

    assertEquals("https://nexus.adaptris.net/nexus", property);
  }

  @Test
  public void testGetPropertyWithDefault() {
    String property = InstallerProperties.getInstance().getProperty("property.doesnt.exist", "defaultValue");

    assertEquals("defaultValue", property);
  }

}
