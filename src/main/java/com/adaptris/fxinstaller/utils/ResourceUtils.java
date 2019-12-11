package com.adaptris.fxinstaller.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ResourceUtils {

  private ResourceUtils() {
  }

  public static String toString(String name) throws IOException {
    InputStream inputStream = ResourceUtils.class.getResourceAsStream(name);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
      return br.lines().collect(Collectors.joining(System.lineSeparator()));
    }
  }

}
