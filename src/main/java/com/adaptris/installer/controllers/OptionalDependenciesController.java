package com.adaptris.installer.controllers;

import com.adaptris.installer.InstallerDataHolder;
import com.adaptris.installer.OptionalComponentCell;
import com.adaptris.installer.helpers.LogHelper;
import com.adaptris.installer.utils.FxUtils;
import com.adaptris.installer.utils.MatchUtils;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.gradle.internal.impldep.org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OptionalDependenciesController extends CancelAwareInstallerController {

  private LogHelper log = LogHelper.getInstance();

  @FXML
  private Label errorFormat;

  @FXML
  private TextArea depTextarea;

  /**
   * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
   */
  @FXML
  private void initialize() {
//    depTextarea.textProperty().addListener((observable, oldText, newText) -> {
//      InstallerDataHolder.getInstance().setInstallDir(newText);
//      nextButton.setDisable(StringUtils.isBlank(newText));
//    });
//    chooseDirTextField.setText(InstallerDataHolder.getInstance().getInstallDir());
      errorFormat.setVisible(false);
      depTextarea.setPromptText("groupId:artifactId:version");
  }

  @FXML
  private void handleInstallInterlok(ActionEvent event) throws IOException {
      log.info("values 123 - " + depTextarea.getText());
      List<String> lines = sanitizeLines(depTextarea.getText());
      errorFormat.setVisible(false);
      log.info("is valid - " + isValid(lines));
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
              log.info("str1 - "+ values[0]+", 1 - " + values[0].matches("^[a-zA-Z.]*$"));
              log.info("str2 - "+ values[1]+", 2 - " + values[1].matches("^[a-zA-Z.-]*$"));
              log.info("str3 - "+ values[2]+", 3 - " + values[2].matches("^[0-9.]*$"));
              if (!values[0].matches("^[a-zA-Z.]*$")) return false;
              if (!values[1].matches("^[a-zA-Z.-]*$")) return false;
              if (!values[2].matches("^[0-9.]*$")) return false;
          }
      }
      return true;
  }

}
