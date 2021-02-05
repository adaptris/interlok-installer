package com.adaptris.fxinstaller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class FxInstallerApp extends Application {

  private static InstallerWizard installerWizard;

  public FxInstallerApp() {
    installerWizard = InstallerWizard.getInstance();
  }

  @Override
  public void start(Stage stage) throws Exception {
    stage.setTitle("Interlok FX Installer");
    stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo.png")));
    stage.setResizable(false);

    Scene scene = new Scene(installerWizard.loadPrepareInstallerNode(), 750, 510);

    scene.getStylesheets().add("/css/style.css");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }

}
