package com.adaptris.installer.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import com.adaptris.TestUtils;
import com.adaptris.installer.models.OptionalComponent;

public class OptionalComponentTest {

  @Test
  public void testNew() {
    OptionalComponent optionalComponent = TestUtils.buildOptionalComponent();

    assertEquals("interlok-json", optionalComponent.getId());
    assertEquals("Interlok/JSON", optionalComponent.getName());
    assertEquals("Everything JSON related; transformations, schemas, json-path (xpath-alike), splitting", optionalComponent.getDescription());
    assertEquals("json,transform,jdbc", optionalComponent.getTags());
    assertEquals("interlok-json.png", optionalComponent.getIcon());
  }

  @Test
  public void testUrl() {
    OptionalComponent optionalComponent = new OptionalComponent("interlok-json");
    optionalComponent.setUrl("http://component-url.com");

    assertEquals("http://component-url.com", optionalComponent.getUrl());
  }

  @Test
  public void testLicense() {
    OptionalComponent optionalComponent = new OptionalComponent("interlok-json");
    optionalComponent.setLicense("true");

    assertEquals("true", optionalComponent.getLicense());
  }

  @Test
  public void testDeprecatedText() {
    OptionalComponent optionalComponent = new OptionalComponent("interlok-json");
    optionalComponent.setDeprecatedText("Deprecated");

    assertEquals("Deprecated", optionalComponent.getDeprecatedText());
  }

  @Test
  public void testCompareSame() {
    OptionalComponent optionalComponentOne = TestUtils.buildOptionalComponent();
    OptionalComponent optionalComponentTwo = new OptionalComponent("interlok-json");
    optionalComponentTwo.setName("Interlok/JSON");

    assertEquals(0, optionalComponentOne.compareTo(optionalComponentTwo));
  }

  @Test
  public void testCompareDifferent() {
    OptionalComponent optionalComponentOne = TestUtils.buildOptionalComponent();
    OptionalComponent optionalComponentTwo = new OptionalComponent("interlok-csv");
    optionalComponentTwo.setName("Interlok CSV");

    assertNotEquals(0, optionalComponentOne.compareTo(optionalComponentTwo));
  }

}
