package com.adaptris.fxinstaller;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import javafx.scene.Parent;

public class FxmlLoaderTest {

  // We can't really test that without starting the java fx toolkit
  //  @Test
  //  public void testLoadOrExit() {
  // Parent root = new FxmlLoader().loadOrExit("/views/prepare_installer.fxml");
  //
  //    assertNotNull(root);
  //  }

  @Test
  public void testLoadOrExitInvalidFxml() {
    Parent root = new FxmlLoader().loadOrExit("/interlok-json.xml");

    assertNull(root);
  }

  @Test
  public void testLoadOrExitDoesntExist() {
    Parent root = new FxmlLoader().loadOrExit("doesntexist");

    assertNull(root);
  }

}
