package com.adaptris.fxinstaller.utils;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import com.adaptris.fxinstaller.helpers.LicenseLoader;

public class ResourceUtilsTest {

  @Test
  public void testToString() throws IOException, URISyntaxException {
    String licenseText = new LicenseLoader().load();

    // Just the first few chars to not deal with the end of line characters
    assertEquals(Files.readString(Paths.get(getClass().getResource("/LICENSE.txt").toURI())).substring(0, 10),
        licenseText.substring(0, 10));
  }

}
