package com.adaptris.installer.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.adaptris.installer.OptionalComponentCell;
import com.adaptris.installer.models.OptionalComponent;

import javafx.scene.control.TableRow;

public class FxUtilsTest {

  @Test
  public void testConvertToCells() {
    OptionalComponent oc1 = newOptionalComponent("interlok-json", "Interlok Transform/JSON",
        "Everything JSON related; transformations, schemas, json-path (xpath-alike), splitting", "json,transform,jdbc");
    OptionalComponent oc2 = newOptionalComponent("interlok-jslt", "Interlok Transform/JSLT", "Support for JSON transforms via JSLT",
        "jslt,json");

    List<OptionalComponentCell> cells = FxUtils.convertToCells(List.of(oc1, oc2));

    assertEquals(2, cells.size());
    assertEquals(oc2.getName(), cells.get(0).getName());
    assertEquals(oc1.getName(), cells.get(1).getName());
  }

  @Test
  public void testSort() {
    OptionalComponentCell occ1 = newOptionalComponentCell("interlok-json", "Interlok Transform/JSON",
        "Everything JSON related; transformations, schemas, json-path (xpath-alike), splitting", "json,transform,jdbc");
    OptionalComponentCell occ2 = newOptionalComponentCell("interlok-jslt", "Interlok Transform/JSLT",
        "Support for JSON transforms via JSLT", "jslt,json");

    assertEquals(0, FxUtils.sort(occ1, occ1));
    assertTrue(FxUtils.sort(occ1, occ2) > 0);
    assertTrue(FxUtils.sort(occ2, occ1) < 0);
  }

  @Test
  public void testGetIdOrName() {
    OptionalComponentCell occ1 = newOptionalComponentCell("interlok-json", "Interlok Transform/JSON",
        "Everything JSON related; transformations, schemas, json-path (xpath-alike), splitting", "json,transform,jdbc");

    String idOrName = FxUtils.getIdOrName(occ1, "Interlok Transform/JSON");

    assertEquals(occ1.getOptionalComponent().getId(), idOrName);
  }

  @Test
  public void testGetIdOrNameNullOptionalComponent() {
    OptionalComponentCell occ1 = null;

    String idOrName = FxUtils.getIdOrName(occ1, "Interlok Transform/JSON");

    assertEquals("Interlok Transform/JSON", idOrName);
  }

  private OptionalComponentCell newOptionalComponentCell(String id, String name, String description, String tags) {
    return new OptionalComponentCell(new OptionalComponent(id, name, description, tags));
  }

  private OptionalComponent newOptionalComponent(String id, String name, String description, String tags) {
    return new OptionalComponent(id, name, description, tags);
  }

  public static class TestTableRow extends TableRow<OptionalComponentCell> {
    public TestTableRow() {
    }
  }

}
