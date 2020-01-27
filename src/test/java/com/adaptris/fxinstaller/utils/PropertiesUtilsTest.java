package com.adaptris.fxinstaller.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Properties;

import org.junit.Test;

public class PropertiesUtilsTest {

  @Test
  public void testLoadFromStreamQuietly() {
    Properties properties = PropertiesUtils.loadFromStreamQuietly(getClass().getClassLoader(), "installer.properties");

    assertEquals("com.adaptris", properties.getProperty("group.id"));
  }

  @Test
  public void testLoadFromStreamQuietlyInvalid() {
    Properties properties = PropertiesUtils.loadFromStreamQuietly(getClass().getClassLoader(), "invalid.properties");

    assertNull(properties.getProperty("group.id"));
  }

  @Test
  public void testLoadFromStreamQuietlyNullName() {
    Properties properties = PropertiesUtils.loadFromStreamQuietly(getClass().getClassLoader(), null);

    assertNull(properties.getProperty("group.id"));
  }

}
