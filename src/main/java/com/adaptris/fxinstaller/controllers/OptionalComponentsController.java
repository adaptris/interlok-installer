package com.adaptris.fxinstaller.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.adaptris.fxinstaller.FxInstallerApp;
import com.adaptris.fxinstaller.InstallerDataHolder;
import com.adaptris.fxinstaller.OptionalComponentCell;
import com.adaptris.fxinstaller.models.OptionalComponent;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.ImageView;

public class OptionalComponentsController extends AbstractInstallerController {

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
  private TableColumn<OptionalComponentCell, Boolean> selectColumn;

  private final List<OptionalComponentCell> optionalComponentCells = new ArrayList<>();

  /**
   * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
   */
  @FXML
  private void initialize() {
    selectColumn.setCellFactory(tc -> {
      CheckBoxTableCell<OptionalComponentCell, Boolean> cell = new CheckBoxTableCell<>();
      cell.setAlignment(Pos.CENTER);
      return cell;
    });

    CheckBox allCheckBox = new CheckBox();
    allCheckBox.setUserData(tableView);
    allCheckBox.setOnAction(handleSelectAllCheckbox());
    selectColumn.setGraphic(allCheckBox);

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

    optionalComponentCells.addAll(convertToCells(InstallerDataHolder.getInstance().getOptionalComponents()));

    tableView.setItems(FXCollections.observableList(optionalComponentCells));
  }

  private List<OptionalComponentCell> convertToCells(List<OptionalComponent> optionalComponents) {
    return optionalComponents.stream().map(oc -> new OptionalComponentCell(oc)).collect(Collectors.toList());
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
  private void handleInstallInterlok(ActionEvent event) throws IOException {
    for (OptionalComponentCell optionalComponentCell : optionalComponentCells) {
      if (optionalComponentCell.getSelected()) {
        InstallerDataHolder.getInstance().addSelectedOptionalComponent(optionalComponentCell.getOptionalComponent());
      }
    }
    FxInstallerApp.goToInstallProgress(((Button) event.getSource()).getScene());
  }

  @FXML
  private void handlePrevious(ActionEvent event) throws IOException {
    FxInstallerApp.goToInstallDirectory(((Button) event.getSource()).getScene());
  }

}
