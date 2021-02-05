package com.adaptris.fxinstaller.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.gradle.internal.impldep.org.apache.commons.lang.StringUtils;

import com.adaptris.fxinstaller.InstallerDataHolder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class InstallDirectoryController extends CancelAwareInstallerController {

  @FXML
  private HBox hBox;
  @FXML
  private Button chooseDirButton;
  @FXML
  private TextField chooseDirTextField;
  @FXML
  private TextField nexusBaseUrlTextField;
  @FXML
  private Button nextButton;

  /**
   * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
   */
  @FXML
  private void initialize() {
    chooseDirTextField.textProperty().addListener((observable, oldText, newText) -> {
      InstallerDataHolder.getInstance().setInstallDir(newText);
      nextButton.setDisable(StringUtils.isBlank(newText));
    });
    chooseDirTextField.setText(InstallerDataHolder.getInstance().getInstallDir());

    nexusBaseUrlTextField.textProperty().addListener((observable, oldText, newText) -> {
      InstallerDataHolder.getInstance().setAdditionalNexusBaseUrl(newText);
    });
    nexusBaseUrlTextField.setText(InstallerDataHolder.getInstance().getAdditionalNexusBaseUrl());
  }

  private DirectoryChooser buildInstallDirDirectoryChooser() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Install Dir");
    Path path = Paths.get(chooseDirTextField.getText());
    if (StringUtils.isNotBlank(chooseDirTextField.getText()) && Files.isDirectory(path)) {
      directoryChooser.setInitialDirectory(path.toFile());
    }
    return directoryChooser;
  }

  @FXML
  private void handleChooseDirectory(ActionEvent event) {
    Window stage = ((Button) event.getSource()).getScene().getWindow();
    DirectoryChooser directoryChooser = buildInstallDirDirectoryChooser();
    File file = directoryChooser.showDialog(stage);
    if (file != null) {
      chooseDirTextField.setText(file.getAbsolutePath());
    }
  }

  @FXML
  private void handleNext(ActionEvent event) throws IOException {
    installerWizard.goToOptionalComponents(((Button) event.getSource()).getScene());
  }

  @FXML
  private void handlePrevious(ActionEvent event) throws IOException {
    installerWizard.goToLicenseAgreement(((Button) event.getSource()).getScene());
  }

}
