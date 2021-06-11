package com.adaptris.installer.utils;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

import com.adaptris.installer.helpers.LogHelper;

public class PropertiesUtils {
  private static LogHelper LOG = LogHelper.getInstance();

  private PropertiesUtils() {}

  public static Properties loadFromStreamQuietly(ClassLoader classLoader, String name) {
    Properties properties = new Properties();
    if (Objects.nonNull(name)) {
      try (InputStream inputStream = classLoader.getResourceAsStream(name)) {
        if (Objects.isNull(inputStream)) {
          throw new Exception(name + " not found in classpath");
        }
        properties.load(inputStream);
      } catch (Exception expt) {
        LOG.error("Could not load properties file [" + name + "]. " + expt.getLocalizedMessage());
      }
    }
    return properties;
  }

}
