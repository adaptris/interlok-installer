package com.adaptris.fxinstaller.helpers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;

import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.GradleConnector;

public class GradleBuildRunner {

  private static final String CLEAN_TASK = "clean";
  private static final String ASSEMBLE_TASK = "assemble";

  private OutputStream standardOutput;
  private OutputStream standardError;

  public GradleBuildRunner(OutputStream standardOutput, OutputStream standardError) {
    this.standardOutput = standardOutput;
    this.standardError = standardError;
  }

  public void run(Path buildGradleDirPath) throws URISyntaxException, IOException {
    GradleConnector connector = GradleConnector.newConnector().forProjectDirectory(buildGradleDirPath.toFile());

    BuildLauncher buildLauncher = connector.connect().newBuild()
        .setStandardOutput(standardOutput)
        .setStandardError(standardError)
        .setColorOutput(true);
    buildLauncher.forTasks(CLEAN_TASK, ASSEMBLE_TASK).run();
  }

}
