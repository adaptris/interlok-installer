package com.adaptris.fxinstaller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;

import org.junit.Test;

import com.adaptris.fxinstaller.models.InterlokProject;
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

  @Test
  public void testBuildProject() {
    installerDataHolder.setVersion("3.9.2-RELEASE");
    installerDataHolder.setInstallDir("/path/to/interlok");
    installerDataHolder.setAdditionalNexusBaseUrl("https://nexus.adaptris.net");
    installerDataHolder.setSelectedOptionalComponents(Collections.singletonList(new OptionalComponent("interlok-json")));

    InterlokProject interlokProject = installerDataHolder.buildProject();

    assertNotNull(interlokProject);
    assertEquals(installerDataHolder.getVersion(),interlokProject.getVersion());
    assertEquals(installerDataHolder.getInstallDir(),interlokProject.getDirectory());
    assertEquals(installerDataHolder.getAdditionalNexusBaseUrl(),interlokProject.getAdditionalNexusBaseUrl());
    assertEquals(installerDataHolder.getSelectedOptionalComponents(), interlokProject.getOptionalComponents());
  }

}
