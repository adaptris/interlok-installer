package com.adaptris.fxinstaller;

import static org.junit.Assert.assertNull;

import org.junit.Test;

import javafx.scene.Parent;

public class FxmlLoaderTest {

// We can't really test that without starting the java fx toolkit
//  @Test
//  public void testLoadOrExit() {
//    Parent root = FxmlLoader.loadOrExit("/views/prepare_installer.fxml");
//
//    assertNotNull(root);
//  }

  @Test
  public void testLoadOrExitInvalidFxml() {
    Parent root = FxmlLoader.loadOrExit("/interlok-json.xml");

    assertNull(root);
  }

  @Test
  public void testLoadOrExitDoesntExist() {
    Parent root = FxmlLoader.loadOrExit("doesntexist");

    assertNull(root);
  }

}
