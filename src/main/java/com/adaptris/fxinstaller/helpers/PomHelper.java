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
  private LogHelper log = LogHelper.getInstance();

  private static final String NAME_XPATH = "artifact.project.name.xpath";
  private static final String DESCRIPTION_XPATH = "artifact.project.description.xpath";
  private static final String URL_XPATH = "artifact.project.url.xpath";
  private static final String PROPERTIES_XPATH = "artifact.project.properties.xpath";

  private final Properties properties;

  public PomHelper(Properties properties) {
    Objects.requireNonNull(properties);
    this.properties = properties;
  }

  public void populateProjectDetails(Document pom, OptionalComponent component) {
    XPathFactory xPathfactory = XPathFactory.newInstance();
    component.setName(getString(getProperty(NAME_XPATH), xPathfactory, pom));
    component.setDescription(getString(getProperty(DESCRIPTION_XPATH), xPathfactory, pom));
    component.setUrl(getString(getProperty(URL_XPATH), xPathfactory, pom));

    component.setDeprecatedText(getPomProperty("deprecated", pom, xPathfactory));
    component.setLicense(getPomProperty("license", pom, xPathfactory));
    component.setTags(getPomProperty("tags", pom, xPathfactory));

    if (StringUtils.isBlank(component.getUrl())) {
      component.setUrl(getPomProperty("url", pom, xPathfactory));
    }
  }

  private String getPomProperty(String propertyName, Document pom, XPathFactory xPathfactory) {
    String value = getString(getProperty(PROPERTIES_XPATH) + "/" + propertyName, xPathfactory, pom);
    if (StringUtils.isBlank(value)) {
      value = getString(getProperty(PROPERTIES_XPATH) + "/property[@name='" + propertyName + "']/@value", xPathfactory,
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
      log.error("Unable to eval " + xpathString + ": " + expt.getLocalizedMessage());
    }

    return StringUtils.defaultIfBlank(strValue, null);
  }

  private String getProperty(String key) {
    return properties.getProperty(key);
  }

}
