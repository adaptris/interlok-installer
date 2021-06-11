package com.adaptris.installer.helpers;

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

import com.adaptris.installer.models.OptionalComponent;
import com.adaptris.installer.utils.XmlUtils;

public class OptionalComponentsLoader {
  private LogHelper log = LogHelper.getInstance();

  public static final String PROPERTIES_FILE = "installer.properties";

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

    loadArtifactsAndAddToList(loadArtifactIds(), optionalComponents);
  }

  private List<String> loadArtifactIds() throws Exception {
    String nexusIndexUrl = getNexusIndexUrl();
    log.info("Loading artifact ids from URL [" + nexusIndexUrl + "]");
    List<String> artifacts = extractArtifacts(XmlUtils.getDocument(nexusIndexUrl));
    Collections.sort(artifacts);
    return artifacts;
  }

  private void loadArtifactsAndAddToList(List<String> artifactIds, List<OptionalComponent> optionalComponents) throws InterruptedException {
    if (artifactIds.isEmpty()) {
      // Should not happen if we can connect to nexus with the right url and version
      log.info("No artifact could be found.");
    } else {
      int nbThreads = artifactIds.size() / Math.min(artifactIds.size(), 5);
      ExecutorService executorService = Executors.newFixedThreadPool(nbThreads);
      for (String artifactId : artifactIds) {
        executorService.execute(new Runnable() {
          @Override
          public void run() {
            loadArtifactAndAddToList(artifactId, optionalComponents);
          }
        });
      }
      executorService.shutdown();
      // We wait a bit here to enjoy the progress bar
      executorService.awaitTermination(30, TimeUnit.SECONDS);
    }
  }

  protected final void loadArtifactAndAddToList(String artifactId, List<OptionalComponent> optionalComponents) {
    try {
      OptionalComponent component = loadArtifact(artifactId);
      optionalComponents.add(component);
    } catch (Exception expt) {
      log.error("Could not load information for artifact " + artifactId, expt);
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
        .replace(REPOSITORY_TKN, installerProperties.getRepository())
        .replace(ARTIFACT_VERSION_TKN, installerProperties.getVersion());
  }

  protected final String getNexusArtifactPomUrl(String artifactId) {
    return getProperty(ARTIFACT_MAVEN_POM_URL).replace(NEXUS_BASE_URL_TKN, getProperty(NEXUS_BASE_URL))
        .replace(REPOSITORY_TKN, installerProperties.getRepository())
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
