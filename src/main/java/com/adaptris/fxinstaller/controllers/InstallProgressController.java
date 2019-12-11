package com.adaptris.fxinstaller.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.function.Function;

import com.adaptris.fxinstaller.InstallerDataHolder;
import com.adaptris.fxinstaller.helpers.InterlokInstaller;
import com.adaptris.fxinstaller.models.InterlokProject;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;

public class InstallProgressController extends AbstractInstallerController {

  @FXML
  private ProgressBar progressBar;
  @FXML
  private TextArea textArea;
  @FXML
  private Button exitButton;
  @FXML
  private Label finishedLabel;

  /**
   * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
   *
   * @throws IOException
   * @throws URISyntaxException
   */
  @FXML
  private void initialize() throws IOException, URISyntaxException {
    InterlokProject interlokProject = buildProject();
    installInterlok(interlokProject);
  }

  @FXML
  private void handleExit(ActionEvent event) {
    handleCancel(event);
  }

  private InterlokProject buildProject() {
    InterlokProject interlokProject = new InterlokProject();
    interlokProject.setVersion(InstallerDataHolder.getInstance().getVersion());
    interlokProject.setDirectory(InstallerDataHolder.getInstance().getInstallDir());
    interlokProject.setAdditionalNexusBaseUrl(InstallerDataHolder.getInstance().getAdditionalNexusBaseUrl());
    interlokProject.setOptionalComponents(InstallerDataHolder.getInstance().getSelectedOptionalComponents());
    return interlokProject;
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

      Function<Double, Void> updateProgressFunction = p -> {
        updateProgress(p, 100);
        return null;
      };
      Function<String, Void> updateMessageFunction = m -> {
        updateMessage(m);
        return null;
      };

      new InterlokInstaller().install(interlokProject, updateProgressFunction, updateMessageFunction);

      updateProgress(10, 10);

      return null;
    }

    @Override
    protected void failed() {
      updateMessage("Interlok failed to installed");
      finishedLabel.setText("Interlok failed to installed");
      exitButton.setDisable(false);
    }

    @Override
    protected void succeeded() {
      updateMessage("Interlok successfully installed in '" + interlokProject.getDirectory() + "'");
      finishedLabel.setText("Interlok successfully installed in '" + interlokProject.getDirectory() + "'");
      exitButton.setDisable(false);
    }
  }

}
