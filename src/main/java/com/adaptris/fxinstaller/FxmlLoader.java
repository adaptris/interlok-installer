package com.adaptris.fxinstaller;

import com.adaptris.fxinstaller.helpers.LogHelper;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class FxmlLoader {
  private static LogHelper LOG = LogHelper.getInstance();

  private FxmlLoader() {
  }

  public static Parent loadOrExit(String name) {
    Parent root = null;
    try {
      root = FXMLLoader.load(FxInstallerApp.class.getResource(name));
    } catch (Exception expt) {
      LOG.error("Could not load " + name, expt);
      Platform.exit();
    }
    return root;
  }

}
