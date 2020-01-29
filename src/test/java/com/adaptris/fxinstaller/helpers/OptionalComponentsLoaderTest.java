package com.adaptris.fxinstaller.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.adaptris.fxinstaller.models.OptionalComponent;

public class OptionalComponentsLoaderTest {

  @Test
  public void testLoad() throws Exception {
    List<OptionalComponent> optionalComponents = new OptionalComponentsLoader().load();

    assertFalse(optionalComponents.isEmpty());
  }

  @Test
  public void testLoadAndCheckOptionalComponent() throws Exception {
    List<OptionalComponent> optionalComponents = new OptionalComponentsLoader(new TestInstallerProperties()).load();

    assertFalse(optionalComponents.isEmpty());
    boolean found = false;
    for (OptionalComponent optionalComponent : optionalComponents) {
      if ("interlok-json".equals(optionalComponent.getId())) {
        assertFalse(optionalComponents.isEmpty());
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
    assertTrue("interlok-json should be in the loaded optional components", found);
  }

  @Test
  public void testLoadEmpty() throws Exception {

    assertTrue(new OptionalComponentsLoader(new InvalidVersionInstallerProperties()).load().isEmpty());
  }

  // We override the version so we know the test will work
  public static class TestInstallerProperties extends InstallerProperties {

    public TestInstallerProperties() {
      properties.setProperty(INTERLOK_VERSION, "3.9.2-RELEASE");
    }

  }

  // We override the version so the loader won't be able to find any optional component
  public static class InvalidVersionInstallerProperties extends InstallerProperties {

    public InvalidVersionInstallerProperties() {
      properties.setProperty(INTERLOK_VERSION, "333");
    }

  }

}
