package com.adaptris.installer;

import com.adaptris.installer.helpers.LogHelper;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class FxmlLoader {
  private LogHelper log = LogHelper.getInstance();

  public Parent loadOrExit(String name) {
    Parent root = null;
    try {
      root = FXMLLoader.load(InstallerApp.class.getResource(name));
    } catch (Exception expt) {
      log.error("Could not load " + name, expt);
      Platform.exit();
    }
    return root;
  }

}
