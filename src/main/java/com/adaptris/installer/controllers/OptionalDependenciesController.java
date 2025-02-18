package com.adaptris.installer.controllers;

import com.adaptris.installer.InstallerDataHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.gradle.internal.impldep.org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OptionalDependenciesController extends CancelAwareInstallerController {

  @FXML
  private Label errorFormat;

  @FXML
  private TextArea depTextarea;

  /**
   * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
   */
  @FXML
  private void initialize() {
      errorFormat.setVisible(false);
      depTextarea.setPromptText("groupId:artifactId:version");
  }

  @FXML
  private void handleInstallInterlok(ActionEvent event) throws IOException {
      List<String> lines = sanitizeLines(depTextarea.getText());
      errorFormat.setVisible(false);
      if(!isValid(lines)) {
          //Mark the area as red
          errorFormat.setVisible(true);
          return;
      }
      InstallerDataHolder.getInstance().setOptionalDependencies(lines);
      installerWizard.goToInstallProgress(((Button) event.getSource()).getScene());
  }

  private List<String> sanitizeLines(String lines) {
      return Arrays.stream(depTextarea.getText().split("\n")).
              map(String::trim).filter(line -> !line.isEmpty()).
              collect(Collectors.toList());
  }

  @FXML
  private void handlePrevious(ActionEvent event) throws IOException {
      errorFormat.setVisible(false);
      installerWizard.goToOptionalComponents(((Button) event.getSource()).getScene());
  }

  private boolean isValid(List<String> lines) {
      for (String line : lines) {
          if(StringUtils.isNotBlank(line)) {
              String[] values = StringUtils.split(line, ":");
              if (values.length != 3) {
                  return false;
              }
              if (!values[0].matches("^[a-zA-Z.]*$")) return false;
              if (!values[1].matches("^[a-zA-Z.-]*$")) return false;
              if (!values[2].matches("^[0-9.]*$")) return false;
          }
      }
      return true;
  }

}
