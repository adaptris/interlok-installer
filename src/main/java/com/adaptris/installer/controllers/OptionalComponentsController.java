package com.adaptris.installer.controllers;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import com.adaptris.installer.InstallerDataHolder;
import com.adaptris.installer.OptionalComponentCell;
import com.adaptris.installer.helpers.LogHelper;
import com.adaptris.installer.utils.FileUtils;
import com.adaptris.installer.utils.FxUtils;
import com.adaptris.installer.utils.MatchUtils;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.ImageView;
import org.gradle.internal.impldep.org.apache.commons.lang.StringUtils;

public class OptionalComponentsController extends CancelAwareInstallerController {

  @FXML
  private TextField filterTextField;
  @FXML
  private TableView<OptionalComponentCell> tableView;
  @FXML
  private TableColumn<OptionalComponentCell, ImageView> iconColumn;
  @FXML
  private TableColumn<OptionalComponentCell, String> nameColumn;
  @FXML
  private TableColumn<OptionalComponentCell, String> descriptionColumn;
  @FXML
  private TableColumn<OptionalComponentCell, String> tagsColumn;
  @FXML
  private TableColumn<OptionalComponentCell, Boolean> licensedColumn;
  @FXML
  private TableColumn<OptionalComponentCell, Boolean> selectColumn;
  @FXML
  private CheckBox dependenciesCheckBox ;
  @FXML
  private Button nextButton;

  private static final String LABEL_BTN_NEXT = "Next";
  private static final String LABEL_BTN_INSTALL = "Install";
  private static final String LABEL_BTN_UPGRADE = "Upgrade";

  private static final String PATH_INTERLOK_VERSION = "META-INF/adaptris-version";

  private static final String PARAMS_KEY_ARTIFACT = "artifactId=";
  private static final String PARAMS_EXTENSION = ".jar";

  private LogHelper log = LogHelper.getInstance();

  private final List<OptionalComponentCell> optionalComponentCells = new ArrayList<>();

  /**
   * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
   */
  @FXML
  private void initialize() {
    log.info("inside initialize");


    licensedColumn.setCellFactory(tc -> {
      CheckBoxTableCell<OptionalComponentCell, Boolean> cell = new CheckBoxTableCell<>();
      cell.setAlignment(Pos.CENTER);
      cell.setDisable(true);
      cell.getStyleClass().add("licensed-cell");
      return cell;
    });
    selectColumn.setCellFactory(tc -> {
      CheckBoxTableCell<OptionalComponentCell, Boolean> cell = new CheckBoxTableCell<>();
      cell.setAlignment(Pos.CENTER);
      return cell;
    });

    CheckBox allCheckBox = new CheckBox();
    allCheckBox.setUserData(tableView);
    allCheckBox.setOnAction(handleSelectAllCheckbox());
    selectColumn.setGraphic(allCheckBox);

    nameColumn.setCellFactory(tc -> {
      return new TableCell<>() {
        @Override
        public void updateItem(String name, boolean empty) {
          super.updateItem(name, empty);
          setText(name);
          setTooltip(new Tooltip(getIdOrNameForTooltip(getTableRow(), name)));
        }
      };
    });

    descriptionColumn.setCellFactory(tc -> {
      return new TableCell<>() {
        @Override
        public void updateItem(String desc, boolean empty) {
          super.updateItem(desc, empty);
          setText(desc);
          setTooltip(new Tooltip(desc));
        }
      };
    });

    optionalComponentCells.addAll(FxUtils.convertToCells(InstallerDataHolder.getInstance().getOptionalComponents()));

    FilteredList<OptionalComponentCell> filteredOptionalComponentCells = new FilteredList<>(
        FXCollections.observableList(optionalComponentCells));

    tableView.setItems(filteredOptionalComponentCells);

    filterTextField.textProperty().addListener((observable, oldText, newText) -> {
      filteredOptionalComponentCells.setPredicate(oc -> match(oc, newText));
    });
  }

  private String getIdOrNameForTooltip(TableRow<OptionalComponentCell> tableRow, String name) {
    return tableRow != null ? FxUtils.getIdOrName(tableRow.getItem(), name) : name;
  }

  private boolean match(OptionalComponentCell occ, String str) {
    return MatchUtils.match(str, occ.getName(), occ.getTags());
  }

  @FXML
  private void handleSelectAllCheckbox(ActionEvent event) {
    CheckBox checkBox = (CheckBox) event.getSource();
    for (OptionalComponentCell optionalComponent : tableView.getItems()) {
      optionalComponent.setSelected(checkBox.isSelected());
    }
  }

  private EventHandler<ActionEvent> handleSelectAllCheckbox() {
    return new EventHandler<>() {
      @Override
      public void handle(ActionEvent event) {
        handleSelectAllCheckbox(event);
      }
    };
  }

  @FXML
  private void handleTextChange(ActionEvent event) {
    if(((CheckBox) event.getSource()).isSelected())
      nextButton.setText(LABEL_BTN_NEXT);
    else
      nextButton.setText(LABEL_BTN_INSTALL);
  }

  @FXML
  private void handleInstallInterlok(ActionEvent event) throws IOException {
    InstallerDataHolder.getInstance().setSelectedOptionalComponents(optionalComponentCells.stream()
            .filter(OptionalComponentCell::getSelected).map(OptionalComponentCell::getOptionalComponent).collect(Collectors.toList()));

    if(!dependenciesCheckBox.isSelected()) {
      installerWizard.goToInstallProgress(((Button) event.getSource()).getScene());
    } else {
      installerWizard.goToOptionalDependencies(((Button) event.getSource()).getScene());
    }
  }

  @FXML
  private void handlePrevious(ActionEvent event) throws IOException {
    selectColumn.getTableView().getItems().forEach(
            cell -> cell.setSelected(false)
    );
    installerWizard.goToInstallDirectory(((Button) event.getSource()).getScene());
  }

  /**
   *  Logic to handle rendering changes for install operation. It does the following -
   *
   *  1. Sets all optional components to false
   *  2. Sets the Next Button display to Install
   */
  public void renderInstall() {
    //Remove all selected values
    selectColumn.getTableView().getItems().forEach(
            cell -> cell.setSelected(false)
    );

    nextButton.setText(LABEL_BTN_INSTALL);
  }

  /**
   * Logic to handle rendering changes for update operation. It does the following -
   *
   *  1. Select only relevant optional components matching the existing optional components
   *  2. Sets the Next Button display to Upgrade
   */
  public void renderUpgrade() {
    List<String> artifactIds = new ArrayList<>();

    if(installerWizard.getInstallDirectoryPath() != null){
      String installDirectoryPath = installerWizard.getInstallDirectoryPath();
      List<File> jarFiles = FileUtils.getFilesListFromDirectory(installDirectoryPath+File.separator+"lib", PARAMS_EXTENSION);
      List<File> filteredJars = FileUtils.filterJarsByContainedFile(jarFiles, PATH_INTERLOK_VERSION);

      for (File jar : filteredJars) {
        try {
          String artifactId = FileUtils.readFileFromJar(jar.getAbsolutePath(), PATH_INTERLOK_VERSION, PARAMS_KEY_ARTIFACT);
          if (StringUtils.isNotEmpty(artifactId)) {
            log.info("Found artifact: " + artifactId);
            artifactIds.add(artifactId);
          }
        } catch (IOException e) {
          log.info("Error reading file from JAR:");
        }
      }
    }

    selectColumn.getTableView().getItems().forEach(
            cell -> {
              if(artifactIds.contains(cell.getId()))
                cell.setSelected(true);
            }
    );

    nextButton.setText(LABEL_BTN_UPGRADE);
  }
}
