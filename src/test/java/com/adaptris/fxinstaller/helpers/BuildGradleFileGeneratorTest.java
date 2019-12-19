package com.adaptris.fxinstaller.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Properties;

import org.junit.Test;

import com.adaptris.TestUtils;
import com.adaptris.fxinstaller.models.InterlokProject;

public class BuildGradleFileGeneratorTest {

  @Test
  public void testGenerate() throws Exception {
    Path resourcesPath = Paths.get(getClass().getResource("/interlok-json.xml").toURI()).getParent();
    Path interlokProjectPath = resourcesPath.resolve("test-project");

    InterlokProject interlokProject = new InterlokProject();
    interlokProject.setOptionalComponents(Collections.singletonList(TestUtils.buildOptionalComponent()));
    interlokProject.setDirectory(interlokProjectPath.toAbsolutePath().toString());
    interlokProject.setVersion("3.9.2-RELEASE");

    Path buildGradleDirPath = new BuildGradleFileGenerator().generate(interlokProject);

    Properties gradleProperties = new Properties();
    gradleProperties.load(Files.newInputStream(buildGradleDirPath.resolve("gradle.properties")));

    assertTrue(Files.isRegularFile(buildGradleDirPath.resolve("build.gradle")));
    assertFalse(new String(Files.readAllBytes(buildGradleDirPath.resolve("build.gradle")), StandardCharsets.UTF_8).isEmpty());
    assertTrue(Files.isRegularFile(buildGradleDirPath.resolve("gradle.properties")));
    assertEquals(interlokProject.getDirectory().replaceAll("\\\\", "/") + "", gradleProperties.get("interlokDistDirectory"));
    assertEquals(interlokProject.getVersion(), gradleProperties.get("interlokVersion"));
    assertEquals(interlokProject.getAdditionalNexusBaseUrl(), gradleProperties.get("additionalNexusBaseUrl"));
    assertNull(gradleProperties.get("additionalNexusBaseUrl"));
  }

  @Test
  public void testGenerateWithAdditionalNexusBAseUrl() throws Exception {
    Path resourcesPath = Paths.get(getClass().getResource("/interlok-json.xml").toURI()).getParent();
    Path interlokProjectPath = resourcesPath.resolve("test-project");

    InterlokProject interlokProject = new InterlokProject();
    interlokProject.setOptionalComponents(Collections.singletonList(TestUtils.buildOptionalComponent()));
    interlokProject.setDirectory(interlokProjectPath.toAbsolutePath().toString());
    interlokProject.setVersion("3.9.2-RELEASE");
    interlokProject.setAdditionalNexusBaseUrl("https://nexus.adaptris.net");

    Path buildGradleDirPath = new BuildGradleFileGenerator().generate(interlokProject);

    Properties gradleProperties = new Properties();
    gradleProperties.load(Files.newInputStream(buildGradleDirPath.resolve("gradle.properties")));

    assertTrue(Files.isRegularFile(buildGradleDirPath.resolve("build.gradle")));
    assertFalse(new String(Files.readAllBytes(buildGradleDirPath.resolve("build.gradle")), StandardCharsets.UTF_8).isEmpty());
    assertTrue(Files.isRegularFile(buildGradleDirPath.resolve("gradle.properties")));
    assertEquals(interlokProject.getDirectory().replaceAll("\\\\", "/") + "", gradleProperties.get("interlokDistDirectory"));
    assertEquals(interlokProject.getVersion(), gradleProperties.get("interlokVersion"));
    assertEquals(interlokProject.getAdditionalNexusBaseUrl(), gradleProperties.get("additionalNexusBaseUrl"));
    assertNotNull(gradleProperties.get("additionalNexusBaseUrl"));
  }

}
