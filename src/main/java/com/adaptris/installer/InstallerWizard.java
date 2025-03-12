package com.adaptris.installer;

import java.io.IOException;
import java.util.Objects;

import com.adaptris.installer.controllers.OptionalComponentsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class InstallerWizard {

  private Parent prepareInstaller;
  private Parent installDirectory;
  private Parent optionalComponents;
  private Parent installProgress;
  private Parent optionalDependencies;
  private boolean isUpgrade;
  private String installDirectoryPath;

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

  public void goToInstallDirectory(Scene scene) {
    if (Objects.isNull(installDirectory)) {
      installDirectory = loadInstallDirectoryNode();
    }
    scene.setRoot(installDirectory);
  }

  public Parent loadInstallDirectoryNode() {
    return fxmlLoader.loadOrExit("/views/install_directory.fxml");
  }

  public void goToOptionalComponents(Scene scene) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/optional_components.fxml"));

    optionalComponents = fxmlLoader.load();

    OptionalComponentsController controller = fxmlLoader.<OptionalComponentsController>getController();

    if(!isUpgrade()) {
      controller.renderInstall();
    } else {
      controller.renderUpgrade();
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

  public void goToOptionalDependencies(Scene scene) {
    if (Objects.isNull(optionalDependencies)) {
      optionalDependencies = loadOptionalDependenciesNode();
    }
    scene.setRoot(optionalDependencies);
  }

  public Parent loadOptionalDependenciesNode() {
    return fxmlLoader.loadOrExit("/views/optional_dependencies.fxml");
  }

  public boolean isUpgrade() {
    return isUpgrade;
  }
  public void setIsUpgrade(boolean isUpgrade) {
    this.isUpgrade = isUpgrade;
  }

  public String getInstallDirectoryPath() {
    return installDirectoryPath;
  }

  public void setInstallDirectoryPath(String installDirectoryPath) {
    this.installDirectoryPath = installDirectoryPath;
  }
}
