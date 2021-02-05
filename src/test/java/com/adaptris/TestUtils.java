package com.adaptris;

import com.adaptris.fxinstaller.models.OptionalComponent;

public class TestUtils {

  public static final String INTERLOK_VERSION = "4.0.0-RELEASE";

  private TestUtils() {
  }

  public static OptionalComponent buildOptionalComponent() {
    return new OptionalComponent("interlok-json", "Interlok/JSON",
        "Everything JSON related; transformations, schemas, json-path (xpath-alike), splitting", "json,transform,jdbc");
  }
}
