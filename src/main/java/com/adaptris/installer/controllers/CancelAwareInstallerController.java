package com.adaptris.installer.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public abstract class CancelAwareInstallerController extends AbstractInstallerController {

  @FXML
  protected void handleCancel(ActionEvent event) {
    Platform.exit();
  }

}
