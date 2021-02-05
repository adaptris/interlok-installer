package com.adaptris.fxinstaller;

import java.util.Objects;

import javafx.scene.Parent;
import javafx.scene.Scene;

public class InstallerWizard {

  private Parent prepareInstaller;
  private Parent licenseAgreement;
  private Parent installDirectory;
  private Parent optionalComponents;
  private Parent installProgress;

  private FxmlLoader fxmlLoader;

  private static InstallerWizard INSTANCE = new InstallerWizard();

  public static InstallerWizard getInstance() {
    return INSTANCE;
  }

  public InstallerWizard() {
    this(new FxmlLoader());
  }

  protected InstallerWizard(FxmlLoader fxmlLoader) {
    this.fxmlLoader = fxmlLoader;
  }

  public void goToPrepareInstaller(Scene scene) {
    if (Objects.isNull(prepareInstaller)) {
      prepareInstaller = loadPrepareInstallerNode();
    }
    scene.setRoot(prepareInstaller);
  }

  public Parent loadPrepareInstallerNode() {
    return fxmlLoader.loadOrExit("/views/prepare_installer.fxml");
  }

  public void goToLicenseAgreement(Scene scene) {
    if (Objects.isNull(licenseAgreement)) {
      licenseAgreement = loadLicenseAgreementNode();
    }
    scene.setRoot(licenseAgreement);
  }

  public Parent loadLicenseAgreementNode() {
    return fxmlLoader.loadOrExit("/views/license_agreement.fxml");
  }

  public void goToInstallDirectory(Scene scene) {
    if (Objects.isNull(installDirectory)) {
      installDirectory = loadInstallDirectoryNode();
    }
    scene.setRoot(installDirectory);
  }

  public Parent loadInstallDirectoryNode() {
    return fxmlLoader.loadOrExit("/views/install_directory.fxml");
  }

  public void goToOptionalComponents(Scene scene) {
    if (Objects.isNull(optionalComponents)) {
      optionalComponents = loadOptionalComponentsNode();
    }
    scene.setRoot(optionalComponents);
  }

  public Parent loadOptionalComponentsNode() {
    return fxmlLoader.loadOrExit("/views/optional_components.fxml");
  }

  public void goToInstallProgress(Scene scene) {
    if (Objects.isNull(installProgress)) {
      installProgress = loadInstallProgressNode();
    }
    scene.setRoot(installProgress);
  }

  public Parent loadInstallProgressNode() {
    return fxmlLoader.loadOrExit("/views/install_progress.fxml");
  }

}
