package com.adaptris.fxinstaller.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

public class LicenseLoaderTest {

  @Test
  public void testLoad() throws IOException, URISyntaxException {
    String licenseText = new LicenseLoader().load();

    String expectedText = new String(Files.readAllBytes(Paths.get(getClass().getResource("/LICENSE.txt").toURI())), StandardCharsets.UTF_8);

    // Just the first few chars to not deal with the end of line characters
    assertEquals(expectedText.substring(0, 10), licenseText.substring(0, 10));
  }

}
