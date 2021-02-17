package com.adaptris.installer.models;

import java.util.ArrayList;
import java.util.List;

public class InterlokProject {

  private List<OptionalComponent> optionalComponents = new ArrayList<>();
  private String directory;
  private String version;
  private String additionalNexusBaseUrl;

  public List<OptionalComponent> getOptionalComponents() {
    return optionalComponents;
  }

  public void setOptionalComponents(List<OptionalComponent> optionalComponents) {
    this.optionalComponents.clear();
    this.optionalComponents.addAll(optionalComponents);
  }

  public String getDirectory() {
    return directory;
  }

  public void setDirectory(String directory) {
    this.directory = directory;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getAdditionalNexusBaseUrl() {
    return additionalNexusBaseUrl;
  }

  public void setAdditionalNexusBaseUrl(String additionalNexusBaseUrl) {
    this.additionalNexusBaseUrl = additionalNexusBaseUrl;
  }

}
