package com.adaptris.fxinstaller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.adaptris.TestUtils;
import com.adaptris.fxinstaller.models.OptionalComponent;

public class OptionalComponentCellTest {

  @Test
  public void testNew() {
    OptionalComponent optionalComponent = TestUtils.buildOptionalComponent();
    OptionalComponentCell optionalComponentCell = new OptionalComponentCell(optionalComponent);

    assertEquals(optionalComponent.getName(), optionalComponentCell.getName());
    assertEquals(optionalComponent.getDescription(), optionalComponentCell.getDescription());
    assertEquals(optionalComponent.getTags(), optionalComponentCell.getTags());
    assertNotNull(optionalComponentCell.getIcon());
    assertEquals(optionalComponent, optionalComponentCell.getOptionalComponent());
  }

  @Test
  public void testNoImage() {
    OptionalComponent optionalComponent = TestUtils.buildOptionalComponent();
    optionalComponent.setIcon("doesntexist.png");
    OptionalComponentCell optionalComponentCell = new OptionalComponentCell(optionalComponent);

    assertNotNull(optionalComponentCell.getIcon());
  }

  @Test
  public void testSetName() {
    OptionalComponent optionalComponent = TestUtils.buildOptionalComponent();
    OptionalComponentCell optionalComponentCell = new OptionalComponentCell(optionalComponent);
    optionalComponentCell.setName("New Name");

    assertEquals("New Name", optionalComponentCell.getName());
  }

  @Test
  public void testSetDescription() {
    OptionalComponent optionalComponent = TestUtils.buildOptionalComponent();
    OptionalComponentCell optionalComponentCell = new OptionalComponentCell(optionalComponent);
    optionalComponentCell.setDescription("New Description");

    assertEquals("New Description", optionalComponentCell.getDescription());
  }

  @Test
  public void testSetTags() {
    OptionalComponent optionalComponent = TestUtils.buildOptionalComponent();
    OptionalComponentCell optionalComponentCell = new OptionalComponentCell(optionalComponent);
    optionalComponentCell.setTags("New Tags");

    assertEquals("New Tags", optionalComponentCell.getTags());
  }

  @Test
  public void testSelected() {
    OptionalComponent optionalComponent = TestUtils.buildOptionalComponent();
    OptionalComponentCell optionalComponentCell = new OptionalComponentCell(optionalComponent);

    assertFalse(optionalComponentCell.getSelected());

    optionalComponentCell.setSelected(true);

    assertTrue(optionalComponentCell.getSelected());
  }

}
