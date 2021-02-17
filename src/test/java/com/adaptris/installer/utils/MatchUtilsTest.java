package com.adaptris.installer.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class MatchUtilsTest {

  @Test
  public void testMatch() {
    assertTrue(MatchUtils.match("val", "value"));
  }

  @Test
  public void testMatchAtLeastOne() {
    assertTrue(MatchUtils.match("val", "match", "value"));
  }

  @Test
  public void testMatchEmptyString() {
    assertTrue(MatchUtils.match("", "match", "value"));
  }

  @Test
  public void testMatchDoesntMatch() {
    assertFalse(MatchUtils.match("different", "value"));
  }

  @Test
  public void testMatchDoesntMatchAny() {
    assertFalse(MatchUtils.match("different", "match", "value"));
  }

  @Test
  public void testMatchNoMatch() {
    assertFalse(MatchUtils.match("different"));
  }

}
