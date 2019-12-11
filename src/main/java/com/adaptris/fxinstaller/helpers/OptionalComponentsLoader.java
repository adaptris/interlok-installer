package com.adaptris.fxinstaller.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.adaptris.fxinstaller.models.OptionalComponent;
import com.adaptris.fxinstaller.utils.XmlUtils;

public class OptionalComponentsLoader {

  public static final String PROPERTIES_FILE = "installer.properties";

  private static final String REPOSITORY_RELEASE = "repository.release";
  private static final String NEXUS_BASE_URL = "nexus.base.url";
  public static final String ARTIFACT_INDEX_URL = "artifact.index.url";
  public static final String ARTIFACT_MAVEN_LATEST_URL = "artifact.maven.latest.url";
  public static final String ARTIFACT_MAVEN_POM_URL = "artifact.maven.pom.url";

  public static final String ARTIFACT_INDEX_ARTIFACT_ID_XPATH = "artifact.index.artifactId.xpath";

  public static final String UNWANTED_ARTIFACTS = "artifact.unwanted";

  private static final String NEXUS_BASE_URL_TKN = "${nexus.base.url}";
  private static final String REPOSITORY_TKN = "${repository}";
  private static final String ARTIFACT_NAME_TKN = "${artifact.name}";
  private static final String ARTIFACT_VERSION_TKN = "${artifact.version}";

  private List<String> unwantedArtifacts = new ArrayList<>();

  private final InstallerProperties installerProperties;

  public OptionalComponentsLoader() {
    this(InstallerProperties.getInstance());
  }

  public OptionalComponentsLoader(InstallerProperties installerProperties) {
    this.installerProperties = installerProperties;
    unwantedArtifacts.addAll(Arrays.asList(getUnwantedArtifacts()));
  }

  public List<OptionalComponent> load() throws Exception {
    List<OptionalComponent> optionalComponents = new ArrayList<>();
    loadAndAddTo(optionalComponents);
    return optionalComponents;
  }

  public void loadAndAddTo(List<OptionalComponent> optionalComponents) throws Exception {
    // optionalComponents.add(new OptionalComponent("interlok-json", "Interlok Json",
    // "Everything JSON related; transformations, schemas, json-path (xpath-alike), splitting", "json,transform,jdbc"));

    List<String> artifactIds = loadArtifacts();
    int nbThreads = artifactIds.size() / Math.min(artifactIds.size(), 5);
    ExecutorService executorService = Executors.newFixedThreadPool(nbThreads);
    for (String artifactId : artifactIds) {
      executorService.execute(new Runnable() {
        @Override
        public void run() {
          loadArtifactAndAdd(artifactId, optionalComponents);
        }
      });
    }
    executorService.shutdown();
    // We wait a bit here to enjoy the progress bar
    executorService.awaitTermination(30, TimeUnit.SECONDS);
  }

  private List<String> loadArtifacts() throws Exception {
    List<String> artifacts = extractArtifacts(XmlUtils.getDocument(getNexusIndexUrl()));
    Collections.sort(artifacts);
    return artifacts;
  }

  private void loadArtifactAndAdd(String artifactId, List<OptionalComponent> optionalComponents) {
    try {
      OptionalComponent component = loadArtifact(artifactId);
      optionalComponents.add(component);
    } catch (Exception expt) {
      System.out.println("Could not load information for artifact " + artifactId);
      expt.printStackTrace();
    }
  }

  private OptionalComponent loadArtifact(String artifactId) throws Exception {
    OptionalComponent component = new OptionalComponent(artifactId);
    new PomHelper(installerProperties.getProperties()).populateProjectDetails(XmlUtils.getDocument(getNexusArtifactPomUrl(artifactId)),
        component);
    return component;
  }

  protected final String getNexusIndexUrl() {
    return getProperty(ARTIFACT_INDEX_URL).replace(NEXUS_BASE_URL_TKN, getProperty(NEXUS_BASE_URL))
        .replace(REPOSITORY_TKN, getProperty(REPOSITORY_RELEASE))
        .replace(ARTIFACT_VERSION_TKN, installerProperties.getVersion());
  }

  protected final String getNexusArtifactPomUrl(String artifactId) {
    return getProperty(ARTIFACT_MAVEN_POM_URL).replace(NEXUS_BASE_URL_TKN, getProperty(NEXUS_BASE_URL))
        .replace(REPOSITORY_TKN, getProperty(REPOSITORY_RELEASE))
        .replace(ARTIFACT_VERSION_TKN, installerProperties.getVersion()).replace(ARTIFACT_NAME_TKN, artifactId);
  }

  private String[] getUnwantedArtifacts() {
    return installerProperties.getProperty(UNWANTED_ARTIFACTS, "").split(",");
  }

  private String getProperty(String key) {
    return installerProperties.getProperty(key);
  }

  protected final List<String> extractArtifacts(Document index) throws Exception {
    List<String> artifacts = new ArrayList<>();
    XPathFactory xPathfactory = XPathFactory.newInstance();
    XPathExpression expr = xPathfactory.newXPath().compile(getProperty(ARTIFACT_INDEX_ARTIFACT_ID_XPATH));
    NodeList nodes = (NodeList) expr.evaluate(index, XPathConstants.NODESET);
    for (int i = 0; i < nodes.getLength(); i++) {
      String artifactId = nodes.item(i).getNodeValue();
      if (notUnwanted(artifactId)) {
        artifacts.add(artifactId);
      }
    }
    return artifacts;
  }

  private boolean notUnwanted(String artifactId) {
    return !unwantedArtifacts.contains(artifactId);
  }

}
