package com.adaptris.installer.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.adaptris.installer.InstallerWizard;
import com.adaptris.installer.helpers.LogHelper;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.gradle.internal.impldep.org.apache.commons.lang.StringUtils;

import com.adaptris.installer.InstallerDataHolder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class InstallDirectoryController extends CancelAwareInstallerController {

  @FXML
  private HBox hBox;
  @FXML
  private Label upgradeWarningText;
  @FXML
  private Label installErrorText;
  @FXML
  private RadioButton radioButtonInstall;
  @FXML
  private RadioButton radioButtonUpgrade;
  @FXML
  private ToggleGroup radioGroup;
  @FXML
  private Button chooseDirButton;
  @FXML
  private TextField chooseDirTextField;
  @FXML
  private TextField nexusBaseUrlTextField;
  @FXML
  private Button nextButton;

  private static final String TEXT_UPGRADE_WARNING = "* This process only upgrades jars, and not configurations. Custom configurations may not work with upgraded version";

  private static final String TEXT_ERROR_INTERLOK_INSTALLED = "Interlok is found installed in the selected location";

  private static final String TEXT_ERROR_INTERLOK_NOT_INSTALLED = "Interlok is not found installed in the selected location";

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
    installerWizard.setInstallDirectoryPath(InstallerDataHolder.getInstance().getInstallDir());

    nexusBaseUrlTextField.textProperty().addListener((observable, oldText, newText) -> {
      InstallerDataHolder.getInstance().setAdditionalNexusBaseUrl(newText);
    });
    nexusBaseUrlTextField.setText(InstallerDataHolder.getInstance().getAdditionalNexusBaseUrl());

    radioGroup.selectedToggleProperty().addListener((observable, oldText, newText) -> {
      nextButton.setDisable(StringUtils.isBlank(newText.toString()));
    });

    upgradeWarningText.setText(TEXT_UPGRADE_WARNING);
  }

  @FXML
  private void handleInstallSelected(ActionEvent event) {
    installerWizard.setIsUpgrade(false);
    upgradeWarningText.setVisible(false);
  }

  @FXML
  private void handleUpgradeSelected(ActionEvent event) {
    installerWizard.setIsUpgrade(true);
    upgradeWarningText.setVisible(true);
  }

  private DirectoryChooser buildInstallDirDirectoryChooser() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
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
      installErrorText.setVisible(false);
    }
  }

  @FXML
  private void handleNext(ActionEvent event) throws IOException {
    if(radioButtonInstall.isSelected()) {
      installerWizard.setIsUpgrade(false);
    } else if(radioButtonUpgrade.isSelected()) {
      installerWizard.setIsUpgrade(true);
    }

    validateAndGoToOptionalComponents(event);
  }

  private void validateAndGoToOptionalComponents(ActionEvent event) throws IOException {
    File directory = new File(chooseDirTextField.getText());

    if (!installerWizard.isUpgrade() && directory.exists()) {
      installErrorText.setText(TEXT_ERROR_INTERLOK_INSTALLED);
      installErrorText.setVisible(true);
    } else if (installerWizard.isUpgrade() && !directory.exists()) {
      installErrorText.setText(TEXT_ERROR_INTERLOK_NOT_INSTALLED);
      installErrorText.setVisible(true);
    } else {
      installerWizard.setInstallDirectoryPath(chooseDirTextField.getText());
      installerWizard.goToOptionalComponents(((Button) event.getSource()).getScene());
      installErrorText.setVisible(false);
    }
  }
}
