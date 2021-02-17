package com.adaptris.installer.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.adaptris.installer.models.OptionalComponent;

public class OptionalComponentsLoaderTest {

  @Test
  public void testLoad() throws Exception {
    List<OptionalComponent> optionalComponents = new OptionalComponentsLoader().load();

    assertFalse(optionalComponents.isEmpty(), "Should have some optional component");
  }

  @Test
  public void testLoadAndCheckOptionalComponent() throws Exception {
    List<OptionalComponent> optionalComponents = new OptionalComponentsLoader(new TestInstallerProperties()).load();

    assertFalse(optionalComponents.isEmpty(), "Should have some optional component");
    boolean found = false;
    for (OptionalComponent optionalComponent : optionalComponents) {
      if ("interlok-json".equals(optionalComponent.getId())) {
        assertEquals("Interlok/JSON", optionalComponent.getName());
        assertEquals("Everything JSON related; transformations, schemas, json-path (xpath-alike), splitting",
            optionalComponent.getDescription());
        assertEquals("https://interlok.adaptris.net/interlok-docs/cookbook-json-transform.html", optionalComponent.getUrl());
        assertEquals("json,transform,jdbc", optionalComponent.getTags());
        assertEquals("interlok-json.png", optionalComponent.getIcon());
        assertEquals("false", optionalComponent.getLicense());
        found = true;
      }
    }
    assertTrue(found, "interlok-json should be in the loaded optional components");
  }

  @Test
  public void testLoadArtifactAndAddToList() throws Exception {
    List<OptionalComponent> optionalComponents = new ArrayList<>();

    new OptionalComponentsLoader(new TestInstallerProperties()).loadArtifactAndAddToList("interlok-json", optionalComponents);

    assertFalse(optionalComponents.isEmpty(), "Should have some optional component");
    OptionalComponent optionalComponent = optionalComponents.get(0);
    assertEquals("interlok-json", optionalComponent.getId());
    assertEquals("Interlok/JSON", optionalComponent.getName());
    assertEquals("Everything JSON related; transformations, schemas, json-path (xpath-alike), splitting",
        optionalComponent.getDescription());
    assertEquals("https://interlok.adaptris.net/interlok-docs/cookbook-json-transform.html", optionalComponent.getUrl());
    assertEquals("json,transform,jdbc", optionalComponent.getTags());
    assertEquals("interlok-json.png", optionalComponent.getIcon());
    assertEquals("false", optionalComponent.getLicense());
  }

  @Test
  public void testLoadArtifactAndAddToListInvalidArtifactId() throws Exception {
    List<OptionalComponent> optionalComponents = new ArrayList<>();

    new OptionalComponentsLoader(new TestInstallerProperties()).loadArtifactAndAddToList("interlok-invalid", optionalComponents);

    assertTrue(optionalComponents.isEmpty(), "Should not have some optional component");
  }

  @Test
  public void testLoadEmpty() throws Exception {

    assertTrue(new OptionalComponentsLoader(new InvalidVersionInstallerProperties()).load().isEmpty());
  }

  // We override the version so we know the test will work
  public static class TestInstallerProperties extends InstallerProperties {

    public TestInstallerProperties() {
      // We can use this line when we get a 4.0.0-RELEASE version
      // properties.setProperty(INTERLOK_VERSION, TestUtils.INTERLOK_VERSION);
      properties.setProperty(INTERLOK_VERSION, "3.11.1-RELEASE");
    }

  }

  // We override the version so the loader won't be able to find any optional component
  public static class InvalidVersionInstallerProperties extends InstallerProperties {

    public InvalidVersionInstallerProperties() {
      properties.setProperty(INTERLOK_VERSION, "444");
    }

  }

}
