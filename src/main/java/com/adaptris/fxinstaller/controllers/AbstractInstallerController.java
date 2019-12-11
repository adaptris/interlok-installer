package com.adaptris.fxinstaller.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public abstract class AbstractInstallerController {

  @FXML
  protected void handleCancel(ActionEvent event) {
    Platform.exit();
  }

}
