package com.adaptris.fxinstaller.controllers;

import java.io.IOException;

import com.adaptris.fxinstaller.helpers.LicenseLoader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;

public class LicenseAgreementController extends CancelAwareInstallerController {

  @FXML
  private TextArea textArea;
  @FXML
  private CheckBox checkBox;
  @FXML
  private Button nextButton;

  @FXML
  private void initialize() throws IOException {
    String licenseText = new LicenseLoader().load();
    textArea.setText(licenseText);
  }

  @FXML
  private void handleAgree(ActionEvent event) {
    nextButton.setDisable(!checkBox.isSelected());
  }

  @FXML
  private void handleNext(ActionEvent event) throws IOException {
    installerWizard.goToInstallDirectory(((Button) event.getSource()).getScene());
  }

}
