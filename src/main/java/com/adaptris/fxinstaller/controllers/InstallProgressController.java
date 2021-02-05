package com.adaptris.fxinstaller.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.function.Consumer;

import com.adaptris.fxinstaller.InstallerDataHolder;
import com.adaptris.fxinstaller.helpers.BuildGradleFileGenerator;
import com.adaptris.fxinstaller.helpers.InterlokInstaller;
import com.adaptris.fxinstaller.helpers.LogHelper;
import com.adaptris.fxinstaller.models.InterlokProject;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class InstallProgressController extends CancelAwareInstallerController {
  private LogHelper log = LogHelper.getInstance();

  @FXML
  private ProgressBar progressBar;
  @FXML
  private TextArea textArea;
  @FXML
  private Button downloadGradleFileButton;
  @FXML
  private Button exitButton;
  @FXML
  private Label finishedLabel;

  private InterlokProject interlokProject;

  /**
   * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
   *
   * @throws IOException
   * @throws URISyntaxException
   */
  @FXML
  private void initialize() throws IOException, URISyntaxException {
    interlokProject = InstallerDataHolder.getInstance().buildProject();
    installInterlok(interlokProject);
  }

  @FXML
  private void handleDownloadGradleFile(ActionEvent event) {
    Window stage = ((Button) event.getSource()).getScene().getWindow();
    DirectoryChooser directoryChooser = buildDownloadDirDirectoryChooser();
    File file = directoryChooser.showDialog(stage);
    if (file != null) {
      try {
        new BuildGradleFileGenerator().downloadGradleFiles(interlokProject, file);
      } catch (IOException ioe) {
        finishedLabel.setText("Could not download gradle files.");
        log.error("Could not download gradle files.", ioe);
      }
    }
  }

  @FXML
  private void handleExit(ActionEvent event) {
    handleCancel(event);
  }

  private DirectoryChooser buildDownloadDirDirectoryChooser() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Download Dir");
    directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    return directoryChooser;
  }

  private void installInterlok(InterlokProject interlokProject) throws URISyntaxException, IOException {
    Task<Void> task = new InstallTask(interlokProject);
    progressBar.progressProperty().bind(task.progressProperty());
    task.messageProperty().addListener((obs, ov, nv) -> {
      textArea.appendText(nv);
      textArea.appendText("\n");
    });

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  private class InstallTask extends Task<Void> {

    private InterlokProject interlokProject;

    public InstallTask(InterlokProject interlokProject) {
      this.interlokProject = interlokProject;
    }

    @Override
    protected Void call() throws Exception {
      updateProgress(0, 10);
      updateMessage("Starting Install");

      Consumer<Double> updateProgressFunction = p -> updateProgress(p, 100);
      Consumer<String> updateMessageFunction = m -> updateMessage(m);

      new InterlokInstaller().install(interlokProject, updateProgressFunction, updateMessageFunction);

      updateProgress(10, 10);

      return null;
    }

    @Override
    protected void failed() {
      updateMessage("Interlok failed to installed");
      finishedLabel.setText("Interlok failed to installed");
      downloadGradleFileButton.setDisable(false);
      exitButton.setDisable(false);
    }

    @Override
    protected void succeeded() {
      updateMessage("Interlok successfully installed in '" + interlokProject.getDirectory() + "'");
      finishedLabel.setText("Interlok successfully installed in '" + interlokProject.getDirectory() + "'");
      downloadGradleFileButton.setDisable(false);
      exitButton.setDisable(false);
    }
  }

}
