package com.adaptris.installer.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Properties;

import org.junit.jupiter.api.Test;

import com.adaptris.TestUtils;

public class InstallerPropertiesTest {

  @Test
  public void testGetVersion() {
    String version = InstallerProperties.getInstance().getVersion();

    assertTrue(version.matches("(4)\\.(\\d{1,2})(?:.(\\d{1,2}))?(?:B(\\d{1,2}))?-(RELEASE|SNAPSHOT)"),
        "Should match pattern but was " + version);
  }

  @Test
  public void testGetRelease() {
    String repository = InstallerProperties.getInstance().getRepository();

    // We can't really test the value as it will depends if the build is a snapshot or a release
    assertNotNull(repository);
  }

  @Test
  public void testGetRepositoryRelease() {
    String repository = InstallerProperties.getInstance().getRepository(TestUtils.INTERLOK_VERSION);

    assertEquals("releases", repository);
  }

  @Test
  public void testGetRepositorySnapshot() {
    String repository = InstallerProperties.getInstance().getRepository("4.0-SNAPSHOT");

    assertEquals("snapshots", repository);
  }

  @Test
  public void testInstallDir() {
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
    assertEquals(18, properties.size());
  }

}
