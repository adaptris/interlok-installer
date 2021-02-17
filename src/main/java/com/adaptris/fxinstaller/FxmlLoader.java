package com.adaptris.fxinstaller;

import com.adaptris.fxinstaller.helpers.LogHelper;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class FxmlLoader {
  private LogHelper log = LogHelper.getInstance();

  public Parent loadOrExit(String name) {
    Parent root = null;
    try {
      root = FXMLLoader.load(FxInstallerApp.class.getResource(name));
    } catch (Exception expt) {
      log.error("Could not load " + name, expt);
      Platform.exit();
    }
    return root;
  }

}
