package com.adaptris.installer.helpers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gradle.tooling.events.ProgressEvent;
import org.gradle.tooling.events.ProgressListener;

import com.adaptris.installer.models.InterlokProject;
import com.adaptris.installer.utils.GradleConsoleUtils;
import com.adaptris.installer.utils.NumberUtils;

public class InterlokInstaller {

  private LogHelper log = LogHelper.getInstance();

  private final InstallerProperties installerProperties = InstallerProperties.getInstance();

  public static final String FILE_ADAPTER_XML = "install.file.adapter";
  public static final String FILE_ADAPTER_BACKUP_XML = "install.file.adapter.backup";
  public static final String FOLDER_CONFIG = "install.directory.config";
  public static final String FOLDER_CONFIG_BACKUP = "install.directory.config.backup";
  public static final String FOLDER_UI_RESOURCES = "install.directory.ui.resources";
  public static final String FOLDER_UI_RESOURCES_BACKUP = "install.directory.ui.resources.backup";
  public static final String FOLDER_ADAPTER_GUI_DB = "install.directory.adapter.gui.db";
  public static final String FOLDER_CONFIG_PROJECT_STORE = "install.directory.config.project.store";


  public void install(InterlokProject interlokProject, Consumer<Double> updateProgress, Consumer<String> updateMessage, Boolean isUpgrade) throws IOException {
    log.info("Installing Interlok in '" + interlokProject.getDirectory() + "'");

    Path buildGradleDirPath = new BuildGradleFileGenerator().generate(interlokProject);
    //Pre-process steps before installation
    preProcess(interlokProject, buildGradleDirPath, isUpgrade);
    new GradleBuildRunner(new GradleOutputStream(updateProgress), System.out, new GradleProgressListener(updateMessage))
    .run(buildGradleDirPath);
    //Post-process steps after installation
    postProcess(interlokProject, buildGradleDirPath, isUpgrade);

    log.info("Interlok successfully installed in '" + interlokProject.getDirectory() + "'");
  }

  public class GradleOutputStream extends ByteArrayOutputStream {

    private static final String PERCENT_PATTERN = "\\<=*-*\\> (\\d{1,3})\\% ";
    private Consumer<Double> updateProgress;

    public GradleOutputStream(Consumer<Double> updateProgress) {
      this.updateProgress = updateProgress;
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) {
      super.write(b, off, len);
      String progressString = GradleConsoleUtils.clearAnsiEscapeCode(new String(b, off, len))
          .replaceFirst("^\\> ", "")
          .replace("IDLE> ", "")
          .replace("root project > ", "");
      Matcher matcher = Pattern.compile(PERCENT_PATTERN).matcher(progressString);
      if (matcher.find()) {
        String progressPercent = matcher.group(1);
        updateProgress.accept(NumberUtils.toDouble(progressPercent));
      } else {
        log.info(progressString);
      }
    }

  }

  public static class GradleProgressListener implements ProgressListener {

    private Consumer<String> updateMessage;

    public GradleProgressListener(Consumer<String> updateMessage) {
      this.updateMessage = updateMessage;
    }

    @Override
    public void statusChanged(ProgressEvent event) {
      if (!event.getDisplayName().startsWith("Register task") && !event.getDisplayName().startsWith("Apply plugin")) {
        updateMessage.accept(event.getDisplayName());
      }
    }

  }

  /*
   * Preprocess steps before performing installation/upgrade process
   */
  private void preProcess(InterlokProject interlokProject, Path buildGradleDirPath, Boolean isUpgrade) throws IOException {
    log.info("Preprocessing Installing Interlok in '" + interlokProject.getDirectory() + "'");
    if(isUpgrade) {
      //back up config directory
      backupFiles(interlokProject, buildGradleDirPath, getProperty(FOLDER_CONFIG), getProperty(FOLDER_CONFIG_BACKUP));
      //back up ui-resource file
      backupFiles(interlokProject,  buildGradleDirPath, getProperty(FOLDER_UI_RESOURCES), getProperty(FOLDER_UI_RESOURCES_BACKUP));
    }
  }

  /*
   * Post process steps before performing installation/upgrade process
   */
  private void postProcess(InterlokProject interlokProject, Path buildGradleDirPath, Boolean isUpgrade) throws IOException {
    log.info("Postprocessing Interlok Installation in '" + interlokProject.getDirectory() + "'");
    if(isUpgrade) {
      //Copy relevant files into config directory
      updateConfigurations(interlokProject, buildGradleDirPath);
      //Copy relevant files into ui-resources directory
      updateUiResources(interlokProject, buildGradleDirPath);
    }
  }

  /*
   * Performs backup operations from current interlok project directory to build gradle directory
   */
  private void backupFiles(InterlokProject interlokProject, Path buildGradleDirPath, String folderConfig, String folderConfigBackup) {
    File configFile = new File(interlokProject.getDirectory(), folderConfig);
    File oldConfigFile = new File(buildGradleDirPath.toFile(), folderConfigBackup);
    if (configFile.exists()) {
      boolean success = configFile.renameTo(oldConfigFile);
      log.info("Status - " + success + " for backing up file '" + configFile.getAbsolutePath() + "' to '" + oldConfigFile.getAbsolutePath() + "'");
    }
  }

  /*
   * Update Configurations folder from backed-up in build generated files
   */
  private void updateConfigurations(InterlokProject interlokProject, Path buildGradleDirPath) throws IOException {
    File backupConfigTempFile = new File(buildGradleDirPath.toFile(), getProperty(FOLDER_CONFIG_BACKUP));
    File configFile = new File(interlokProject.getDirectory(), getProperty(FOLDER_CONFIG));
    File backupConfigTargetFile = new File(interlokProject.getDirectory(), FOLDER_CONFIG_BACKUP);

    File adapterXmlFile = new File(configFile, getProperty(FILE_ADAPTER_XML));
    File adapterBackupXmlFile = new File(configFile, getProperty(FILE_ADAPTER_BACKUP_XML));
    File oldAdapterXmlFile = new File(backupConfigTargetFile, getProperty(FILE_ADAPTER_XML));

    if(backupConfigTempFile.exists()) {
      log.info("Updating configurations in '" + interlokProject.getDirectory() + "'");
      //Move backed-up config directory from build directory to current Interlok folder
      backupConfigTempFile.renameTo(backupConfigTargetFile);
      //Move current adapter.xml file to adapter-backup.xml file
      adapterXmlFile.renameTo(adapterBackupXmlFile);
      //Copy adapter.xml from backed-up config directory to config directory
      if(oldAdapterXmlFile.exists()) {
        Files.copy(oldAdapterXmlFile.toPath(), adapterXmlFile.toPath());
      }
    }
  }

  /*
   * Update Ui-Resources folder from backed-up in build generated files
   */
  private void updateUiResources(InterlokProject interlokProject, Path buildGradleDirPath) throws IOException {
    File backupUiResourcesTempFile = new File(buildGradleDirPath.toFile(), getProperty(FOLDER_UI_RESOURCES_BACKUP));
    File uiResourcesFile = new File(interlokProject.getDirectory(), getProperty(FOLDER_UI_RESOURCES));
    File backupUiResourcesTargetFile = new File(interlokProject.getDirectory(), getProperty(FOLDER_UI_RESOURCES_BACKUP));

    File oldAdapterGuiDbFile = new File(backupUiResourcesTargetFile, getProperty(FOLDER_ADAPTER_GUI_DB));
    File adapterGuiDbFile = new File(uiResourcesFile, getProperty(FOLDER_ADAPTER_GUI_DB));

    File oldConfigProjectStoreFile = new File(backupUiResourcesTargetFile, getProperty(FOLDER_CONFIG_PROJECT_STORE));
    File configProjectStoreFile = new File(uiResourcesFile, getProperty(FOLDER_CONFIG_PROJECT_STORE));

    if(backupUiResourcesTempFile.exists()) {
      log.info("Updating UI Resources in '" + interlokProject.getDirectory() + "'");
      //Move backed-up ui-resources directory from build directory to current Interlok folder
      backupUiResourcesTempFile.renameTo(backupUiResourcesTargetFile);
      //Copy adapter_gui_db files from backed-up ui-resources directory to current ui-resources directory
      if(oldAdapterGuiDbFile.exists()) {
        Files.copy(oldAdapterGuiDbFile.toPath(), adapterGuiDbFile.toPath());
      }
      //Copy config-project-store files from backed-up ui-resources directory to current ui-resources directory
      if(oldConfigProjectStoreFile.exists()) {
        Files.copy(oldConfigProjectStoreFile.toPath(), configProjectStoreFile.toPath());
      }
    }
  }

  private String getProperty(String key) {
    return installerProperties.getProperty(key);
  }
}
