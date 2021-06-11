package com.adaptris.installer.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.adaptris.installer.InstallerDataHolder;
import com.adaptris.installer.OptionalComponentCell;
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

  private final List<OptionalComponentCell> optionalComponentCells = new ArrayList<>();

  /**
   * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
   */
  @FXML
  private void initialize() {
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
  private void handleInstallInterlok(ActionEvent event) throws IOException {
    InstallerDataHolder.getInstance().setSelectedOptionalComponents(optionalComponentCells.stream()
        .filter(OptionalComponentCell::getSelected).map(OptionalComponentCell::getOptionalComponent).collect(Collectors.toList()));

    installerWizard.goToInstallProgress(((Button) event.getSource()).getScene());
  }

  @FXML
  private void handlePrevious(ActionEvent event) throws IOException {
    installerWizard.goToInstallDirectory(((Button) event.getSource()).getScene());
  }

}
