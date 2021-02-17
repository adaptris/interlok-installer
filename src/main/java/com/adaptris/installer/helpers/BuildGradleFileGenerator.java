package com.adaptris.installer.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.gradle.internal.impldep.org.apache.commons.lang.StringUtils;

import com.adaptris.installer.models.InterlokProject;
import com.adaptris.installer.models.OptionalComponent;
import com.adaptris.installer.utils.ResourceUtils;
import com.adaptris.installer.utils.VersionUtils;
import com.adaptris.installer.utils.ZipUtils;

public class BuildGradleFileGenerator {

  protected static final String BUILD_GRADLE = "build.gradle";
  protected static final String GRADLE_PROPERTIES = "gradle.properties";
  protected static final String INTERLOK_VERSION = "interlokVersion";
  protected static final String INTERLOK_DIST_DIRECTORY = "interlokDistDirectory";
  protected static final String INTERLOK_BASE_FILESYSTEM_URL = "interlokBaseFilesystemUrl";
  protected static final String ADDITIONAL_NEXUS_BASE_URL = "additionalNexusBaseUrl";

  private static final String INTERLOK_INSTALLER_TMP_DIR = "interlok-installer-tmp-";
  private static final String BUILD_GRADLE_TEMPLATE = "/templates/build.gradle.template";
  private static final String INTERLOK_GRADLE_FILES = "interlok-gradle-files-";
  private static final String ZIP = ".zip";
  private static final String INDENT = "  ";

  private static final String INSTALLER_PROPERTY_SNAPSHOT_FILESYSTEM_URL = "snapshot.filesystem.url";
  private static final String INSTALLER_PROPERTY_BETA_FILESYSTEM_URL = "beta.filesystem.url";
  private static final String INSTALLER_PROPERTY_RELEASE_FILESYSTEM_URL = "release.filesystem.url";

  private final Path tmpDirPath;
  private final InstallerProperties installerProperties;

  private final List<BaseFilesystemBuilder> BASE_URL_PARSERS =
      Collections.unmodifiableList(
          Arrays.asList(new BaseFilesystemBuilder[] {
              (interlokVersion) -> snapshotUrl(interlokVersion),
              (interlokVersion) -> betaUrl(interlokVersion),
              (interlokVersion) -> releaseUrl(interlokVersion)
          }));

  public BuildGradleFileGenerator() {
    this(Paths.get(System.getProperty("java.io.tmpdir")));
  }

  public BuildGradleFileGenerator(Path tmpDirPath) {
    this.tmpDirPath = tmpDirPath;
    installerProperties = InstallerProperties.getInstance();
  }

  public Path generate(InterlokProject interlokProject) throws IOException {
    Path destDir = Files.createDirectories(tmpDirPath.resolve(installerTmpDirName(interlokProject)));

    // TODO Use better template engine
    createBuildGradleFile(interlokProject.getOptionalComponents(), BUILD_GRADLE_TEMPLATE, destDir);
    createGradlePropertiesFile(interlokProject.getVersion(), interlokProject.getDirectory(), interlokProject.getAdditionalNexusBaseUrl(), destDir);

    return destDir;
  }

  public void downloadGradleFiles(InterlokProject interlokProject, File downloadTo) throws IOException {
    Path destDir = tmpDirPath.resolve(installerTmpDirName(interlokProject));

    if (Files.isDirectory(destDir)) {
      Path buildGradlePath = destDir.resolve(BUILD_GRADLE);
      Path gradlePropertiesPath = destDir.resolve(GRADLE_PROPERTIES);

      try (FileOutputStream outputStream = new FileOutputStream(
          new File(downloadTo, INTERLOK_GRADLE_FILES + interlokProject.getVersion() + ZIP))) {
        List<File> files = Arrays.asList(buildGradlePath.toFile(), gradlePropertiesPath.toFile());
        ZipUtils.zipFileList(files, outputStream);
      }
    }
  }

  protected static String installerTmpDirName(InterlokProject interlokProject) {
    return INTERLOK_INSTALLER_TMP_DIR + interlokProject.getVersion();
  }

  private void createBuildGradleFile(List<OptionalComponent> optionalComponents, String buildGradleTemplateName, Path destDirPath)
      throws IOException {
    Path buildGradlePath = destDirPath.resolve(BUILD_GRADLE);

    String interlokRuntime = toLinesString(optionalComponents, oc -> {
      return INDENT + "interlokRuntime (\"com.adaptris:" + oc.getId() + ":$interlokVersion\") { changing=true }";
    });

    String interlokJavadocs = toLinesString(optionalComponents, oc -> {
      return INDENT + "interlokJavadocs (\"com.adaptris:" + oc.getId() + ":$interlokVersion:javadoc\") { changing=true; transitive=false }";
    });

    String buildGradleTemplate = ResourceUtils.toString(buildGradleTemplateName);

    String buildGradleContent = buildGradleTemplate.replace("#{interlokRuntime}", interlokRuntime);
    buildGradleContent = buildGradleContent.replace("#{interlokJavadocs}", interlokJavadocs);

    Files.writeString(buildGradlePath, buildGradleContent);
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
    Optional<String> baseUrl =
        BASE_URL_PARSERS.stream().map((p) -> p.build(interlokVersion)).filter((o) -> o.isPresent()).findFirst()
        .orElse(Optional.empty());
    baseUrl.ifPresent((val) -> properties.put(INTERLOK_BASE_FILESYSTEM_URL, val));

    try (OutputStream outputStream = Files.newOutputStream(gradlePropertiesPath)) {
      properties.store(outputStream, "");
    }
  }

  private Optional<String> betaUrl(String interlokVersion) {
    if (VersionUtils.isBeta(interlokVersion)) {
      String rawVersion = interlokVersion.replace("-RELEASE", "");
      return Optional.of(installerProperties.getProperty(INSTALLER_PROPERTY_BETA_FILESYSTEM_URL).replace("${release}", rawVersion));
    }
    return Optional.empty();
  }

  private Optional<String> snapshotUrl(String interlokVersion) {
    if (VersionUtils.isSnapshot(interlokVersion)) {
      return Optional.of(installerProperties.getProperty(INSTALLER_PROPERTY_SNAPSHOT_FILESYSTEM_URL).replace("${today}",
          LocalDate.now().minusDays(1).toString()));
    }
    return Optional.empty();
  }

  private Optional<String> releaseUrl(String interlokVersion) {
    String rawVersion = interlokVersion.replace("-RELEASE", "");
    return Optional
        .of(installerProperties.getProperty(INSTALLER_PROPERTY_RELEASE_FILESYSTEM_URL).replace("${release}", rawVersion));
  }

  private String toLinesString(List<OptionalComponent> optionalComponents, Function<OptionalComponent, String> func) {
    return String.join(System.lineSeparator(), optionalComponents.stream().map(func).collect(Collectors.toList()));
  }

  @FunctionalInterface
  private interface BaseFilesystemBuilder {
    Optional<String> build(String s);
  }

}
