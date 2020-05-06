package com.adaptris.fxinstaller.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.Properties;
import org.junit.Test;

public class InstallerPropertiesTest {

  @Test
  public void testGetVersion() {
    String version = InstallerProperties.getInstance().getVersion();

    assertTrue(version.matches("(3)\\.(\\d{1,2})(?:.(\\d{1,2}))?(?:B(\\d{1,2}))?-(RELEASE|SNAPSHOT)"));
  }

  @Test
  public void testGetRelease() {
    String repository = InstallerProperties.getInstance().getRepository();

    // We can't really test the value as it will depends if the build is a snapshot or a release
    assertNotNull(repository);
  }

  @Test
  public void testGetRepositoryRelease() {
    String repository = InstallerProperties.getInstance().getRepository("3.10.0-RELEASE");

    assertEquals("releases", repository);
  }

  @Test
  public void testGetRepositorySnapshot() {
    String repository = InstallerProperties.getInstance().getRepository("3.10-SNAPSHOT");

    assertEquals("snapshots", repository);
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
  public void testInstallDirMac() {
    String osName = System.getProperty("os.name");
    try {
      System.setProperty("os.name", "mac");
      String installDir = InstallerProperties.getInstance().getInstallDir();

      assertEquals("/Applications/Adaptris/Interlok", installDir);
    } finally {
      System.setProperty("os.name", osName);
    }
  }

  @Test
  public void testGetMacInstallDir() {
    String installDir = InstallerProperties.getInstance().getMacInstallDir();

    assertEquals("/Applications/Adaptris/Interlok", installDir);
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

  @Test
  public void testGetProperties() {
    Properties properties = InstallerProperties.getInstance().getProperties();
    // Bit magic number, this is the number of properties we have atm.
    assertEquals(20, properties.size());
  }

}
