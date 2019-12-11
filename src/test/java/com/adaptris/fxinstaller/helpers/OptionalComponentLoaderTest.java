package com.adaptris.fxinstaller.helpers;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class OptionalComponentLoaderTest {

  @Test
  public void testLoad() throws Exception {
    assertFalse(new OptionalComponentsLoader().load().isEmpty());
  }

}
