package com.adaptris.installer.controllers;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

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
  @FXML
  private CheckBox dependenciesCheckBox ;
  @FXML
  private Button nextButton;

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

  public void renderInstall() {
    selectColumn.getTableView().getItems().forEach(
            cell -> cell.setSelected(false)
    );

    nextButton.setText("Install");
  }

  public void renderUpgrade() {
    List<String> artifactIds = new ArrayList<>();

    if(installerWizard.getInstallDirectoryPath() != null){
      String installDirectoryPath = installerWizard.getInstallDirectoryPath();

      String searchPattern = "META-INF/adaptris-version";

      List<File> jarFiles = getJarFilesFromDirectory(installDirectoryPath+File.separator+"lib");

      List<File> filteredJars = filterJarsByContainedFile(jarFiles, searchPattern);

      for (File jar : filteredJars) {
        try {
          // For text files
          String artifactId = readFileFromJar(jar.getAbsolutePath(), searchPattern);
          if (artifactId != null) {
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

    nextButton.setText("Upgrade");
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
      nextButton.setText("Next");
    else
      nextButton.setText("Install");
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

  private String readFileFromJar(String jarFilePath, String filePathInJar) throws IOException {
    try (JarFile jarFile = new JarFile(jarFilePath)) {
      // Get the JAR entry for the specified file
      JarEntry entry = jarFile.getJarEntry(filePathInJar);

      if (entry == null) {
        return null; // File not found in the JAR
      }

      // Open an input stream to read the file
      try (InputStream inputStream = jarFile.getInputStream(entry);
           BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

        StringBuilder content = new StringBuilder();
        String line;

        // Read the file line by line
        while ((line = reader.readLine()) != null) {
          if(line.startsWith("artifactId="))
            content.append(line.replace("artifactId=", "").trim());
        }

        return content.toString();
      }
    }
  }

  public static List<File> filterJarsByContainedFile(List<File> jarFiles, String fileNamePattern) {
    List<File> filteredJars = new ArrayList<>();

    for (File jarFile : jarFiles) {
      try (JarFile jar = new JarFile(jarFile)) {
        Enumeration<JarEntry> entries = jar.entries();

        while (entries.hasMoreElements()) {
          JarEntry entry = entries.nextElement();
          String entryName = entry.getName();

          if (!entry.isDirectory() && entryName.contains(fileNamePattern)) {
            filteredJars.add(jarFile);
            break; // Found a match, no need to check other entries
          }
        }
      } catch (IOException e) {
        System.err.println("Error processing JAR file: " + jarFile.getName());
        e.printStackTrace();
      }
    }

    return filteredJars;
  }

  private static List<File> getJarFilesFromDirectory(String directoryPath) {
    List<File> jarFiles = new ArrayList<>();
    File directory = new File(directoryPath);

    if (directory.exists() && directory.isDirectory()) {
      File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".jar"));
      if (files != null) {
        for (File file : files) {
          jarFiles.add(file);
        }
      }
    }

    return jarFiles;
  }
}
