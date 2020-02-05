package com.adaptris.fxinstaller.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.List;

import com.adaptris.fxinstaller.FxInstallerApp;
import com.adaptris.fxinstaller.InstallerDataHolder;
import com.adaptris.fxinstaller.helpers.InstallerProperties;
import com.adaptris.fxinstaller.helpers.LogHelper;
import com.adaptris.fxinstaller.helpers.OptionalComponentsLoader;
import com.adaptris.fxinstaller.models.OptionalComponent;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;

public class PrepareInstallerController {
  private LogHelper log = LogHelper.getInstance();

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
      } catch (UnknownHostException uhe) {
        log.error("Failed to prepare installer. " + uhe.getLocalizedMessage() + ". Make sure you have an internet connection");
      } catch (Exception expt) {
        log.error("Failed to prepare installer. Make sure you have an internet connection.", expt);
        throw expt;
      }

      return null;
    }

    private void loadOptionalComponents(List<OptionalComponent> optionalComponents) throws Exception {
      new OptionalComponentsLoader().loadAndAddTo(optionalComponents);
    }

    @Override
    protected void failed() {
      log.error("Preparation failed");
      Platform.exit();
    }

    @Override
    protected void succeeded() {
      log.info("Preparation succeeded");
      goToLicenseAgreement();
    }

    private void goToLicenseAgreement() {
      FxInstallerApp.goToLicenseAgreement(progressBar.getScene());
    }

  }

}
