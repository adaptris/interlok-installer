package com.adaptris.fxinstaller.helpers;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LogHelperTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final PrintStream originalErr = System.err;

  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @After
  public void restoreStreams() {
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  @Test
  public void testInfo() {
    LogHelper.getInstance().info("Some message");

    assertTrue(outContent.toString().startsWith("INFO: Some message"));
  }

  @Test
  public void testError() {
    LogHelper.getInstance().error("Some error message");

    assertTrue(outContent.toString().startsWith("ERROR: Some error message"));
  }

  @Test
  public void testErrorWithThrowable() {
    LogHelper.getInstance().error("Some error message", new Exception("error"));

    assertTrue(outContent.toString().startsWith("ERROR: Some error message"));
    assertTrue(errContent.toString().startsWith("java.lang.Exception: error"));
  }

  @Test
  public void testErrorWithNullThrowable() {
    LogHelper.getInstance().error("Some error message", null);

    assertTrue(outContent.toString().startsWith("ERROR: Some error message"));
    assertTrue(errContent.toString().isEmpty());
  }

}
