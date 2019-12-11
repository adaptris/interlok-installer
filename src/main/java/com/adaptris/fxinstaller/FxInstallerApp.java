package com.adaptris.fxinstaller;

import java.io.IOException;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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

    Scene scene = new Scene(loadPrepareInstallerNode(), 700, 470);

    scene.getStylesheets().add("/css/style.css");
    stage.setScene(scene);
    stage.show();
  }

  private static Parent loadPrepareInstallerNode() throws IOException {
    return FXMLLoader.load(FxInstallerApp.class.getResource("/views/prepare_installer.fxml"));
  }

  public static void goToLicenseAgreement(Scene scene) throws IOException {
    if (Objects.isNull(licenseAgreement)) {
      licenseAgreement = loadLicenseAgreementNode();
    }
    scene.setRoot(licenseAgreement);
  }

  private static Parent loadLicenseAgreementNode() throws IOException {
    return FXMLLoader.load(FxInstallerApp.class.getResource("/views/license_agreement.fxml"));
  }

  public static void goToInstallDirectory(Scene scene) throws IOException {
    if (Objects.isNull(installDirectory)) {
      installDirectory = loadInstallDirectoryNode();
    }
    scene.setRoot(installDirectory);
  }

  private static Parent loadInstallDirectoryNode() throws IOException {
    return FXMLLoader.load(FxInstallerApp.class.getResource("/views/install_directory.fxml"));
  }

  public static void goToOptionalComponents(Scene scene) throws IOException {
    if (Objects.isNull(optionalComponents)) {
      optionalComponents = loadOptionalComponentsNode();
    }
    scene.setRoot(optionalComponents);
  }

  private static Parent loadOptionalComponentsNode() throws IOException {
    return FXMLLoader.load(FxInstallerApp.class.getResource("/views/optional_components.fxml"));
  }

  public static void goToInstallProgress(Scene scene) throws IOException {
    if (Objects.isNull(installProgress)) {
      installProgress = loadInstallProgressNode();
    }
    scene.setRoot(installProgress);
  }

  private static Parent loadInstallProgressNode() throws IOException {
    return FXMLLoader.load(FxInstallerApp.class.getResource("/views/install_progress.fxml"));
  }

  public static void main(String[] args) {
    launch();
  }

}
