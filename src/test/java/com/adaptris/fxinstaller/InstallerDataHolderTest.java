package com.adaptris.fxinstaller;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;

import com.adaptris.fxinstaller.models.OptionalComponent;

public class InstallerDataHolderTest {

  private InstallerDataHolder installerDataHolder = InstallerDataHolder.getInstance();

  @Test
  public void testVersion() {
    installerDataHolder.setVersion("3.9.2-RELEASE");

    assertEquals("3.9.2-RELEASE", installerDataHolder.getVersion());
  }

  @Test
  public void testInstallDir() {
    installerDataHolder.setInstallDir("/path/to/interlok");

    assertEquals("/path/to/interlok", installerDataHolder.getInstallDir());
  }

  @Test
  public void testAdditionalNexusBaseUrl() {
    installerDataHolder.setAdditionalNexusBaseUrl("https://nexus.adaptris.net");

    assertEquals("https://nexus.adaptris.net", installerDataHolder.getAdditionalNexusBaseUrl());
  }

  @Test
  public void testOptionalComponents() {
    installerDataHolder.setOptionalComponents(Collections.singletonList(new OptionalComponent("interlok-json")));

    assertEquals(1, installerDataHolder.getOptionalComponents().size());
  }

  @Test
  public void testSelectedOptionalComponents() {
    installerDataHolder.setSelectedOptionalComponents(Collections.singletonList(new OptionalComponent("interlok-json")));

    assertEquals(1, installerDataHolder.getSelectedOptionalComponents().size());
  }

}
