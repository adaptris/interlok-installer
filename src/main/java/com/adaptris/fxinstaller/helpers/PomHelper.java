package com.adaptris.fxinstaller.helpers;

import java.util.Objects;
import java.util.Properties;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.gradle.internal.impldep.org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.adaptris.fxinstaller.models.OptionalComponent;

public class PomHelper {

  private final Properties properties;

  public PomHelper(Properties properties) {
    Objects.requireNonNull(properties);
    this.properties = properties;
  }

  public void populateProjectDetails(Document pom, OptionalComponent component) {
    XPathFactory xPathfactory = XPathFactory.newInstance();
    component.setName(getString(getProperty("artifact.project.name.xpath"), xPathfactory, pom));
    component.setDescription(getString(getProperty("artifact.project.description.xpath"), xPathfactory, pom));
    component.setUrl(getString(getProperty("artifact.project.url.xpath"), xPathfactory, pom));

    component.setDeprecatedText(getPomProperty("deprecated", pom, xPathfactory));
    component.setLicense(getPomProperty("license", pom, xPathfactory));
    component.setTags(getPomProperty("tags", pom, xPathfactory));

    if (StringUtils.isBlank(component.getUrl())) {
      component.setUrl(getPomProperty("url", pom, xPathfactory));
    }
  }

  private String getPomProperty(String propertyName, Document pom, XPathFactory xPathfactory) {
    String value = getString(getProperty("artifact.project.properties.xpath") + "/" + propertyName, xPathfactory, pom);
    if (StringUtils.isBlank(value)) {
      value = getString(getProperty("artifact.project.properties.xpath") + "/property[@name='" + propertyName + "']/@value", xPathfactory,
          pom);
    }
    return value;
  }

  protected final String getString(String xpathString, XPathFactory xPathfactory, Node node) {
    String strValue = null;
    try {
      XPathExpression expr = xPathfactory.newXPath().compile(xpathString);
      strValue = (String) expr.evaluate(node, XPathConstants.STRING);
    } catch (Exception expt) {
      System.out.println("Unable to eval " + xpathString + ": " + expt.getLocalizedMessage());
    }

    return StringUtils.defaultIfBlank(strValue, null);
  }

  private String getProperty(String key) {
    return properties.getProperty(key);
  }

}
