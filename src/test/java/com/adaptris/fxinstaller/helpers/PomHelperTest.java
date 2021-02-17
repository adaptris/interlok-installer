package com.adaptris.fxinstaller.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Properties;

import org.junit.Test;
import org.w3c.dom.Document;

import com.adaptris.fxinstaller.models.OptionalComponent;
import com.adaptris.fxinstaller.utils.XmlUtils;

public class PomHelperTest {

  @Test
  public void testPopulateProjectDetails() throws Exception {
    PomHelper pomHelper = new PomHelper(InstallerProperties.getInstance().getProperties());

    Document pom = XmlUtils.getDocument(getClass().getResource("/interlok-json.xml").toExternalForm());
    OptionalComponent component = new OptionalComponent("interlok-json");

    pomHelper.populateProjectDetails(pom, component);

    assertEquals("Interlok/JSON", component.getName());
    assertEquals("Everything JSON related; transformations, schemas, json-path (xpath-alike), splitting", component.getDescription());
    assertEquals("json,transform,jdbc", component.getTags());
    assertEquals("https://interlok.adaptris.net/interlok-docs/cookbook-json-transform.html", component.getUrl());
    assertEquals("false", component.getLicense());
    assertNull(component.getDeprecatedText());
  }

  @Test
  public void testPopulateProjectDetailsPropertyTag() throws Exception {
    PomHelper pomHelper = new PomHelper(InstallerProperties.getInstance().getProperties());

    Document pom = XmlUtils.getDocument(getClass().getResource("/interlok-json-property-tag.xml").toExternalForm());
    OptionalComponent component = new OptionalComponent("interlok-json");

    pomHelper.populateProjectDetails(pom, component);

    assertEquals("Interlok/JSON", component.getName());
    assertEquals("Everything JSON related; transformations, schemas, json-path (xpath-alike), splitting", component.getDescription());
    assertEquals("json,transform,jdbc", component.getTags());
    assertEquals("https://interlok.adaptris.net/interlok-docs/cookbook-json-transform.html", component.getUrl());
    assertEquals("false", component.getLicense());
    assertNull(component.getDeprecatedText());
  }

  @Test
  public void testPopulateProjectDetailsInvalidXpath() throws Exception {
    Properties properties = InstallerProperties.getInstance().getProperties();
    properties.setProperty("artifact.project.name.xpath", ">\\]@#$invalidXpath");
    properties.setProperty("artifact.project.description.xpath", ">\\]@#$invalidXpath");
    PomHelper pomHelper = new PomHelper(properties);

    Document pom = XmlUtils.getDocument(getClass().getResource("/interlok-json.xml").toExternalForm());
    OptionalComponent component = new OptionalComponent("interlok-json");

    pomHelper.populateProjectDetails(pom, component);

    assertEquals("interlok-json", component.getName());
    assertNull(component.getDescription());
  }

}
