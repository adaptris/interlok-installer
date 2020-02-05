package com.adaptris.fxinstaller;

import java.util.Objects;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class FxInstallerApp extends Application {

  private static Parent licenseAgreement;
  private static Parent installDirectory;
  private static Parent optionalComponents;
  private static Parent installProgress;

  @Override
  public void start(Stage stage) throws Exception {
    stage.setTitle("Interlok FX Installer");
    stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo.png")));
    stage.setResizable(false);

    Scene scene = new Scene(loadPrepareInstallerNode(), 800, 600);

    scene.getStylesheets().add("/css/style.css");
    stage.setScene(scene);
    stage.show();
  }

  private static Parent loadPrepareInstallerNode() {
    return FxmlLoader.loadOrExit("/views/prepare_installer.fxml");
  }

  public static void goToLicenseAgreement(Scene scene) {
    if (Objects.isNull(licenseAgreement)) {
      licenseAgreement = loadLicenseAgreementNode();
    }
    scene.setRoot(licenseAgreement);
  }

  private static Parent loadLicenseAgreementNode() {
    return FxmlLoader.loadOrExit("/views/license_agreement.fxml");
  }

  public static void goToInstallDirectory(Scene scene) {
    if (Objects.isNull(installDirectory)) {
      installDirectory = loadInstallDirectoryNode();
    }
    scene.setRoot(installDirectory);
  }

  private static Parent loadInstallDirectoryNode() {
    return FxmlLoader.loadOrExit("/views/install_directory.fxml");
  }

  public static void goToOptionalComponents(Scene scene) {
    if (Objects.isNull(optionalComponents)) {
      optionalComponents = loadOptionalComponentsNode();
    }
    scene.setRoot(optionalComponents);
  }

  private static Parent loadOptionalComponentsNode() {
    return FxmlLoader.loadOrExit("/views/optional_components.fxml");
  }

  public static void goToInstallProgress(Scene scene) {
    if (Objects.isNull(installProgress)) {
      installProgress = loadInstallProgressNode();
    }
    scene.setRoot(installProgress);
  }

  private static Parent loadInstallProgressNode() {
    return FxmlLoader.loadOrExit("/views/install_progress.fxml");
  }

  public static void main(String[] args) {
    launch();
  }

}
