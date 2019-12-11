package com.adaptris.fxinstaller.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import com.adaptris.fxinstaller.FxInstallerApp;
import com.adaptris.fxinstaller.InstallerDataHolder;
import com.adaptris.fxinstaller.helpers.InstallerProperties;
import com.adaptris.fxinstaller.helpers.OptionalComponentsLoader;
import com.adaptris.fxinstaller.models.OptionalComponent;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;

public class PrepareInstallerController {

  @FXML
  private ProgressBar progressBar;

  /**
   * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
   *
   * @throws IOException
   * @throws URISyntaxException
   */
  @FXML
  private void initialize() throws IOException, URISyntaxException {
    prepareInstaller();
  }

  private void prepareInstaller() throws URISyntaxException, IOException {
    Task<Void> task = new PrepareTask();
    progressBar.progressProperty().bind(task.progressProperty());

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }

  private class PrepareTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {
      try {
        InstallerDataHolder.getInstance().setVersion(InstallerProperties.getInstance().getVersion());
        InstallerDataHolder.getInstance().setInstallDir(InstallerProperties.getInstance().getInstallDir());
        InstallerDataHolder.getInstance().setAdditionalNexusBaseUrl(InstallerProperties.getInstance().getAdditionalNexusBaseUrl());
        loadOptionalComponents(InstallerDataHolder.getInstance().getOptionalComponents());
      } catch (Exception expt) {
        expt.printStackTrace();
        throw expt;
      }

      FxInstallerApp.goToLicenseAgreement(progressBar.getScene());
      return null;
    }

    private void loadOptionalComponents(List<OptionalComponent> optionalComponents) throws Exception {
      new OptionalComponentsLoader().loadAndAddTo(optionalComponents);
    }

    @Override
    protected void failed() {
      System.out.println("Preparation failed");
      Platform.exit();
    }

    @Override
    protected void succeeded() {
      System.out.println("Preparation succeeded");
    }

  }

}
