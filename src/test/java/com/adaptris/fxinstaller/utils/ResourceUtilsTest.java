package com.adaptris.fxinstaller.utils;

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
    String licenseText = ResourceUtils.toString("/LICENSE.txt");

    // Just the first few chars to not deal with the end of line characters
    assertEquals(Files.readString(Paths.get(getClass().getResource("/LICENSE.txt").toURI())).substring(0, 10),
        licenseText.substring(0, 10));
  }

  @Test
  public void testToStringNullName() throws IOException, URISyntaxException {
    assertThrows(NullPointerException.class, () -> {
      ResourceUtils.toString(null);
    });
  }

}
