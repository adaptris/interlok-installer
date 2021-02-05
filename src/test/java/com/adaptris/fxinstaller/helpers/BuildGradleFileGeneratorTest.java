package com.adaptris.fxinstaller.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import com.adaptris.TestUtils;
import com.adaptris.fxinstaller.models.InterlokProject;

public class BuildGradleFileGeneratorTest {

  @Test
  public void testGenerate() throws Exception {
    Path resourcesPath = Paths.get(getClass().getResource("/interlok-json.xml").toURI()).getParent();
    Path interlokProjectPath = resourcesPath.resolve("test-project");

    InterlokProject interlokProject = newProject(interlokProjectPath);

    Path buildGradleDirPath = new BuildGradleFileGenerator().generate(interlokProject);

    Properties gradleProperties = new Properties();
    gradleProperties.load(Files.newInputStream(buildGradleDirPath.resolve(BuildGradleFileGenerator.GRADLE_PROPERTIES)));

    assertGradle(interlokProject, buildGradleDirPath, gradleProperties);
    // never null
    assertNotNull(gradleProperties.get(BuildGradleFileGenerator.INTERLOK_BASE_FILESYSTEM_URL));
    assertNull(gradleProperties.get(BuildGradleFileGenerator.ADDITIONAL_NEXUS_BASE_URL));
  }

  @Test
  public void testGenerateBetaVersion() throws Exception {
    Path resourcesPath = Paths.get(getClass().getResource("/interlok-json.xml").toURI()).getParent();
    Path interlokProjectPath = resourcesPath.resolve("test-project");

    InterlokProject interlokProject = newProject(interlokProjectPath);
    interlokProject.setVersion("4.0.0B1-RELEASE");

    Path buildGradleDirPath = new BuildGradleFileGenerator().generate(interlokProject);

    Properties gradleProperties = new Properties();
    gradleProperties.load(Files.newInputStream(buildGradleDirPath.resolve(BuildGradleFileGenerator.GRADLE_PROPERTIES)));

    assertGradle(interlokProject, buildGradleDirPath, gradleProperties);
    // never null
    assertNotNull(gradleProperties.get(BuildGradleFileGenerator.INTERLOK_BASE_FILESYSTEM_URL));
    assertNull(gradleProperties.get(BuildGradleFileGenerator.ADDITIONAL_NEXUS_BASE_URL));
  }

  @Test
  public void testGenerateSnapshotVersion() throws Exception {
    Path resourcesPath = Paths.get(getClass().getResource("/interlok-json.xml").toURI()).getParent();
    Path interlokProjectPath = resourcesPath.resolve("test-project");

    InterlokProject interlokProject = newProject(interlokProjectPath);
    interlokProject.setVersion("4.0-SNAPSHOT");

    Path buildGradleDirPath = new BuildGradleFileGenerator().generate(interlokProject);

    Properties gradleProperties = new Properties();
    gradleProperties.load(Files.newInputStream(buildGradleDirPath.resolve(BuildGradleFileGenerator.GRADLE_PROPERTIES)));

    assertGradle(interlokProject, buildGradleDirPath, gradleProperties);
    // never null
    assertNotNull(gradleProperties.get(BuildGradleFileGenerator.INTERLOK_BASE_FILESYSTEM_URL));
    assertNull(gradleProperties.get(BuildGradleFileGenerator.ADDITIONAL_NEXUS_BASE_URL));
  }

  @Test
  public void testGenerateWithAdditionalNexusBaseUrl() throws Exception {
    Path resourcesPath = Paths.get(getClass().getResource("/interlok-json.xml").toURI()).getParent();
    Path interlokProjectPath = resourcesPath.resolve("test-project");

    InterlokProject interlokProject = newProject(interlokProjectPath);
    interlokProject.setAdditionalNexusBaseUrl("https://nexus.adaptris.net");

    Path buildGradleDirPath = new BuildGradleFileGenerator().generate(interlokProject);

    Properties gradleProperties = new Properties();
    gradleProperties.load(Files.newInputStream(buildGradleDirPath.resolve(BuildGradleFileGenerator.GRADLE_PROPERTIES)));

    assertGradle(interlokProject, buildGradleDirPath, gradleProperties);
    // never null
    assertNotNull(gradleProperties.get(BuildGradleFileGenerator.INTERLOK_BASE_FILESYSTEM_URL));
    assertNotNull(gradleProperties.get(BuildGradleFileGenerator.ADDITIONAL_NEXUS_BASE_URL));
  }

  @Test
  public void testDownloadGradleFiles() throws Exception {
    Path resourcesPath = Paths.get(getClass().getResource("/interlok-json.xml").toURI()).getParent();
    Path interlokProjectPath = resourcesPath.resolve("test-project");

    InterlokProject interlokProject = newProject(interlokProjectPath);

    BuildGradleFileGenerator buildGradleFileGenerator = new BuildGradleFileGenerator(resourcesPath);
    // We generate the tmp file first
    Path buildGradleDirPath = buildGradleFileGenerator.generate(interlokProject);
    try {
      assertTrue(Files.isRegularFile(buildGradleDirPath.resolve(BuildGradleFileGenerator.BUILD_GRADLE)));

      buildGradleFileGenerator.downloadGradleFiles(interlokProject, resourcesPath.toFile());

      assertTrue(Files.isRegularFile(resourcesPath.resolve("interlok-gradle-files-" + TestUtils.INTERLOK_VERSION + ".zip")));
    } finally {
      Files.walk(buildGradleDirPath).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
      Files.deleteIfExists(buildGradleDirPath);
      Files.deleteIfExists(resourcesPath.resolve("interlok-gradle-files-" + TestUtils.INTERLOK_VERSION + ".zip"));
    }
  }

  @Test
  public void testDownloadGradleFilesInstallerTmpDirDoesntExist() throws Exception {
    Path resourcesPath = Paths.get(getClass().getResource("/interlok-json.xml").toURI()).getParent();
    Path interlokProjectPath = resourcesPath.resolve("test-project");

    InterlokProject interlokProject = newProject(interlokProjectPath);

    new BuildGradleFileGenerator(resourcesPath).downloadGradleFiles(interlokProject, resourcesPath.toFile());

    assertFalse(Files.isRegularFile(resourcesPath.resolve("interlok-gradle-files-" + TestUtils.INTERLOK_VERSION + ".zip")));

    // TODO At the moment we rely on testGenerate to be ran before.
    // We need to fix that and also test if the file don't exist
  }

  private InterlokProject newProject(Path interlokProjectPath) {
    InterlokProject interlokProject = new InterlokProject();
    interlokProject.setOptionalComponents(Collections.singletonList(TestUtils.buildOptionalComponent()));
    interlokProject.setDirectory(interlokProjectPath.toAbsolutePath().toString());
    interlokProject.setVersion(TestUtils.INTERLOK_VERSION);
    return interlokProject;
  }

  private void assertGradle(InterlokProject interlokProject, Path buildGradleDirPath, Properties gradleProperties) throws IOException {
    assertTrue(Files.isRegularFile(buildGradleDirPath.resolve(BuildGradleFileGenerator.BUILD_GRADLE)));
    assertFalse(Files.readString(buildGradleDirPath.resolve(BuildGradleFileGenerator.BUILD_GRADLE)).isBlank());
    assertTrue(Files.isRegularFile(buildGradleDirPath.resolve(BuildGradleFileGenerator.GRADLE_PROPERTIES)));
    assertEquals(interlokProject.getDirectory().replaceAll("\\\\", "/") + "",
        gradleProperties.get(BuildGradleFileGenerator.INTERLOK_DIST_DIRECTORY));
    assertEquals(interlokProject.getVersion(), gradleProperties.get(BuildGradleFileGenerator.INTERLOK_VERSION));
    assertEquals(interlokProject.getAdditionalNexusBaseUrl(), gradleProperties.get(BuildGradleFileGenerator.ADDITIONAL_NEXUS_BASE_URL));
  }

}
