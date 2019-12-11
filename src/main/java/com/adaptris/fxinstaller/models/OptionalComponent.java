package com.adaptris.fxinstaller.models;

import org.gradle.internal.impldep.org.apache.commons.lang.StringUtils;

public class OptionalComponent implements Comparable<OptionalComponent> {

  private String id;
  private String name;
  private String description;
  private String tags;
  private String icon;
  private String url;
  private String externalUrl;
  private String license;
  private String deprecatedText;

  public OptionalComponent(String id) {
    this(id, null, null, null);
  }

  public OptionalComponent(String id, String name, String description, String tags) {
    this.id = id;
    icon = id + ".png";
    this.name = name;
    this.description = description;
    this.tags = tags;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return StringUtils.defaultIfBlank(name, id);
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getExternalUrl() {
    return externalUrl;
  }

  public void setExternalUrl(String externalUrl) {
    this.externalUrl = externalUrl;
  }

  public String getLicense() {
    return license;
  }

  public void setLicense(String license) {
    this.license = license;
  }

  public String getDeprecatedText() {
    return deprecatedText;
  }

  public void setDeprecatedText(String deprecatedText) {
    this.deprecatedText = deprecatedText;
  }

  @Override
  public int compareTo(OptionalComponent other) {
    return getName().compareTo(other.getName());
  }

}
