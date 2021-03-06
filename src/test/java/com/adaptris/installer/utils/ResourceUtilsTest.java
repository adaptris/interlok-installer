package com.adaptris.installer.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

public class ResourceUtilsTest {

  @Test
  public void testToString() throws IOException, URISyntaxException {
    String fileContent = ResourceUtils.toString("/installer.properties");

    // Just the first few chars to not deal with the end of line characters
    assertEquals(Files.readString(Paths.get(getClass().getResource("/installer.properties").toURI())).substring(0, 10),
        fileContent.substring(0, 10));
  }

  @Test
  public void testToStringNullName() throws IOException, URISyntaxException {
    assertThrows(NullPointerException.class, () -> {
      ResourceUtils.toString(null);
    });
  }

}
