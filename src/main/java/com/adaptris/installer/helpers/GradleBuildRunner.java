package com.adaptris.installer.helpers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;

import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.events.ProgressListener;

public class GradleBuildRunner {

  private static final String CLEAN_TASK = "clean";
  private static final String ASSEMBLE_TASK = "assemble";

  private OutputStream standardOutput;
  private OutputStream standardError;
  private ProgressListener progressListener;

  public GradleBuildRunner(OutputStream standardOutput, OutputStream standardError, ProgressListener progressListener) {
    this.standardOutput = standardOutput;
    this.standardError = standardError;
    this.progressListener = progressListener;
  }

  public void run(Path buildGradleDirPath) throws URISyntaxException, IOException {
    GradleConnector connector = GradleConnector.newConnector().forProjectDirectory(buildGradleDirPath.toFile());

    BuildLauncher buildLauncher = connector.connect().newBuild()
        .setStandardOutput(standardOutput)
        .setStandardError(standardError)
        .addProgressListener(progressListener)
        .setColorOutput(true);
    buildLauncher.forTasks(CLEAN_TASK, ASSEMBLE_TASK).run();
  }

}
