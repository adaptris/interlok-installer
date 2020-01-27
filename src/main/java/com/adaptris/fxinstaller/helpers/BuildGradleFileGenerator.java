package com.adaptris.fxinstaller.helpers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import org.gradle.internal.impldep.org.apache.commons.lang.StringUtils;

import com.adaptris.fxinstaller.models.InterlokProject;
import com.adaptris.fxinstaller.models.OptionalComponent;
import com.adaptris.fxinstaller.utils.ResourceUtils;

public class BuildGradleFileGenerator {

  private static final String INTERLOK_FX_INSTALLER_TMP_DIR = "interlok-fx-installer-tmp-";
  private static final String BUILD_GRADLE_TEMPLATE = "/templates/build.gradle.template";
  private static final String BUILD_GRADLE = "build.gradle";
  private static final String GRADLE_PROPERTIES = "gradle.properties";
  private static final String INTERLOK_VERSION = "interlokVersion";
  private static final String INTERLOK_DIST_DIRECTORY = "interlokDistDirectory";
  private static final String ADDITIONAL_NEXUS_BASE_URL = "additionalNexusBaseUrl";

  public Path generate(InterlokProject interlokProject) throws IOException {
    Path tmpDirPath = Paths.get(System.getProperty("java.io.tmpdir"));
    Path destDir = Files.createDirectories(tmpDirPath.resolve(INTERLOK_FX_INSTALLER_TMP_DIR + interlokProject.getVersion()));

    // TODO Use better template engine
    createBuildGradleFile(interlokProject.getOptionalComponents(), BUILD_GRADLE_TEMPLATE, destDir);
    createGradlePropertiesFile(interlokProject.getVersion(), interlokProject.getDirectory(), interlokProject.getAdditionalNexusBaseUrl(), destDir);

    return destDir;
  }

  private void createBuildGradleFile(List<OptionalComponent> optionalComponents, String buildGradleTemplateName, Path destDirPath)
      throws IOException {
    Path buildGradlePath = destDirPath.resolve(BUILD_GRADLE);

    StringBuilder interlokRuntimeSb = new StringBuilder();
    StringBuilder interlokJavadocsSb = new StringBuilder();

    for (OptionalComponent optionalComponent : optionalComponents) {
      interlokRuntimeSb.append("interlokRuntime (\"com.adaptris:").append(optionalComponent.getId())
      .append(":$interlokVersion\") { changing=true }").append(System.lineSeparator());
      interlokJavadocsSb.append("interlokJavadocs (\"com.adaptris:").append(optionalComponent.getId())
      .append(":$interlokVersion:javadoc\") { changing=true; transitive=false }").append(System.lineSeparator());
    }

    String buildGradleTemplate = ResourceUtils.toString(buildGradleTemplateName);

    String buildGradleContent = buildGradleTemplate.replace("#{interlokRuntime}", interlokRuntimeSb.toString());
    buildGradleContent = buildGradleContent.replace("#{interlokJavadocs}", interlokJavadocsSb.toString());

    Files.write(buildGradlePath, buildGradleContent.getBytes(StandardCharsets.UTF_8));
  }

  private void createGradlePropertiesFile(String interlokVersion, String interlokDistDirectory, String additionalNexusBaseUrl,
      Path destDirPath)
          throws IOException {
    Path gradlePropertiesPath = destDirPath.resolve(GRADLE_PROPERTIES);

    Properties properties = new Properties();
    properties.put(INTERLOK_VERSION, interlokVersion);
    properties.put(INTERLOK_DIST_DIRECTORY, interlokDistDirectory.replaceAll("\\\\", "/"));
    if (StringUtils.isNotBlank(additionalNexusBaseUrl)) {
      properties.put(ADDITIONAL_NEXUS_BASE_URL, additionalNexusBaseUrl);
    }
    properties.store(Files.newOutputStream(gradlePropertiesPath), "");
  }

}
