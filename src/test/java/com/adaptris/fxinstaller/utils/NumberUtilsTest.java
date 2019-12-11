package com.adaptris.fxinstaller.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NumberUtilsTest {

  @Test
  public void testToDouble() {
    Double dble = NumberUtils.toDouble("1.1");

    assertEquals(Double.valueOf(1.1), dble);
  }

  @Test
  public void testToDoubleSingleDigit() {
    Double dble = NumberUtils.toDouble("1");

    assertEquals(Double.valueOf(1), dble);
  }

  @Test
  public void testToDoubleSInvalid() {
    Double dble = NumberUtils.toDouble("invalid");

    assertNull(dble);
  }

}
