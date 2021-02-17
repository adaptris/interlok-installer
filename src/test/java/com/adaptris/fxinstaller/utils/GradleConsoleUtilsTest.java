package com.adaptris.fxinstaller.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class GradleConsoleUtilsTest {

  @Test
  public void testClearAnsiEscapeCode() {
    String replacedStr = GradleConsoleUtils.clearAnsiEscapeCode("[1A[1m> Starting Daemon > Connecting to Daemon[m[40D[1B");

    assertEquals("> Starting Daemon > Connecting to Daemon", replacedStr);
  }

  @Test
  public void testClearAnsiEscapeCodeNull() {
    String replacedStr = GradleConsoleUtils.clearAnsiEscapeCode(null);

    assertNull(replacedStr);
  }

  @Test
  public void testClearProgressBar() {
    String replacedStr = GradleConsoleUtils.clearProgressBar("<-------------> 0% INITIALIZING [12ms]");

    assertEquals("0% INITIALIZING [12ms]", replacedStr);
  }

  @Test
  public void testClearProgressBarNull() {
    String replacedStr = GradleConsoleUtils.clearProgressBar(null);

    assertNull(replacedStr);
  }

}
