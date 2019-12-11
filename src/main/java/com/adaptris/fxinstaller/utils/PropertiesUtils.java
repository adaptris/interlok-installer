package com.adaptris.fxinstaller.utils;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class PropertiesUtils {

  private PropertiesUtils() {}

  public static Properties loadFromStreamQuietly(ClassLoader classLoader, String name) {
    Properties properties = new Properties();
    if (name != null) {
      try (InputStream inputStream = classLoader.getResourceAsStream(name)) {
        if (Objects.isNull(inputStream)) {
          throw new Exception(name + " not found in classpath");
        }
        properties.load(inputStream);
      } catch (Exception expt) {
        System.out.println("Could not load properties file [" + name + "]. " + expt.getLocalizedMessage());
      }
    }
    return properties;
  }

}
