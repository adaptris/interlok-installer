package com.adaptris.installer.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.adaptris.TestUtils;
import com.adaptris.installer.models.InterlokProject;
import com.adaptris.installer.models.OptionalComponent;

public class InterlokProjectTest {

  private InterlokProject interlokProjectTest = new InterlokProject();

  @Test
  public void testVersion() {
    interlokProjectTest.setVersion(TestUtils.INTERLOK_VERSION);

    assertEquals(TestUtils.INTERLOK_VERSION, interlokProjectTest.getVersion());
  }

  @Test
  public void testInstallDir() {
    interlokProjectTest.setDirectory("/path/to/interlok");

    assertEquals("/path/to/interlok", interlokProjectTest.getDirectory());
  }

  @Test
  public void testIncludeWar() {
    interlokProjectTest.setIncludeWar(true);

    assertTrue(interlokProjectTest.getIncludeWar());
    assertEquals("true", interlokProjectTest.includeWar());
  }
  @Test
  public void testAdditionalNexusBaseUrl() {
    interlokProjectTest.setAdditionalNexusBaseUrl("https://nexus.adaptris.net");

    assertEquals("https://nexus.adaptris.net", interlokProjectTest.getAdditionalNexusBaseUrl());
  }

  @Test
  public void testOptionalComponents() {
    interlokProjectTest.setOptionalComponents(Collections.singletonList(new OptionalComponent("interlok-json")));

    assertEquals(1, interlokProjectTest.getOptionalComponents().size());
  }

}
