package com.adaptris.fxinstaller.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Properties;

import org.junit.jupiter.api.Test;

public class InterlokInstallDirTest {

  private Properties properties = InstallerProperties.getInstance().getProperties();

  @Test
  public void testFindAndGetDir() {
    String osName = System.getProperty("os.name");
    try {
      System.setProperty("os.name", "windows");
      String installDir = InterlokInstallDir.find().getDir(properties);

      assertEquals("C:\\Adaptris\\Interlok", installDir);
    } finally {
      System.setProperty("os.name", osName);
    }
  }

  @Test
  public void testFindAndGetDirWindows() {
    String installDir = InterlokInstallDir.find("windows").getDir(properties);

    assertEquals("C:\\Adaptris\\Interlok", installDir);
  }

  @Test
  public void testWindowsGetDir() {
    String installDir = InterlokInstallDir.WIN.getDir(properties);

    assertEquals("C:\\Adaptris\\Interlok", installDir);
  }

  @Test
  public void testFindAndGetDirMac() {
    String installDir = InterlokInstallDir.find("mac").getDir(properties);

    assertTrue(installDir.contains("/Adaptris/Interlok"));
  }

  @Test
  public void testMacGetDir() {
    String installDir = InterlokInstallDir.MAC.getDir(properties);

    assertTrue(installDir.contains("/Adaptris/Interlok"));
  }

  @Test
  public void testFindAndGetDirLinux() {
    String installDir = InterlokInstallDir.find("linux").getDir(properties);

    assertEquals("/opt/Adaptris/Interlok", installDir);
  }

  @Test
  public void testLinuxGetDir() {
    String installDir = InterlokInstallDir.LINUX.getDir(properties);

    assertEquals("/opt/Adaptris/Interlok", installDir);
  }

  @Test
  public void testFindAndGetDirInvalid() {
    String installDir = InterlokInstallDir.find("invalid").getDir(properties);

    assertEquals("/opt/Adaptris/Interlok", installDir);
  }

}
