package com.adaptris.installer;

import java.util.ArrayList;
import java.util.List;

import com.adaptris.installer.models.InterlokProject;
import com.adaptris.installer.models.OptionalComponent;

public class InstallerDataHolder {

  private static InstallerDataHolder INSTANCE = new InstallerDataHolder();

  private String version;
  private String installDir;
  private String additionalNexusBaseUrl;
  private List<OptionalComponent> optionalComponents = new ArrayList<>();
  private List<OptionalComponent> selectedOptionalComponents = new ArrayList<>();

  private InstallerDataHolder() {
  }

  public static InstallerDataHolder getInstance() {
    return INSTANCE;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getInstallDir() {
    return installDir;
  }

  public void setInstallDir(String installDir) {
    this.installDir = installDir;
  }

  public String getAdditionalNexusBaseUrl() {
    return additionalNexusBaseUrl;
  }

  public void setAdditionalNexusBaseUrl(String additionalNexusBaseUrl) {
    this.additionalNexusBaseUrl = additionalNexusBaseUrl;
  }

  public List<OptionalComponent> getOptionalComponents() {
    return optionalComponents;
  }

  public void setOptionalComponents(List<OptionalComponent> optionalComponents) {
    this.optionalComponents.clear();
    this.optionalComponents.addAll(optionalComponents);
  }

  public List<OptionalComponent> getSelectedOptionalComponents() {
    return selectedOptionalComponents;
  }

  public void setSelectedOptionalComponents(List<OptionalComponent> selectedOptionalComponents) {
    this.selectedOptionalComponents.clear();
    this.selectedOptionalComponents.addAll(selectedOptionalComponents);
  }

  public InterlokProject buildProject() {
    InterlokProject interlokProject = new InterlokProject();
    interlokProject.setVersion(getVersion());
    interlokProject.setDirectory(getInstallDir());
    interlokProject.setAdditionalNexusBaseUrl(getAdditionalNexusBaseUrl());
    interlokProject.setOptionalComponents(getSelectedOptionalComponents());
    return interlokProject;
  }

}
