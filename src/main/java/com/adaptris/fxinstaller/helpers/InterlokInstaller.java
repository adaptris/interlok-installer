package com.adaptris.fxinstaller.helpers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gradle.tooling.events.ProgressEvent;
import org.gradle.tooling.events.ProgressListener;

import com.adaptris.fxinstaller.models.InterlokProject;
import com.adaptris.fxinstaller.utils.GradleConsoleUtils;
import com.adaptris.fxinstaller.utils.NumberUtils;

public class InterlokInstaller {
  private LogHelper log = LogHelper.getInstance();

  public void install(InterlokProject interlokProject, Consumer<Double> updateProgress, Consumer<String> updateMessage)
      throws URISyntaxException, IOException {
    log.info("Installing Interlok in '" + interlokProject.getDirectory() + "'");

    Path buildGradleDirPath = new BuildGradleFileGenerator().generate(interlokProject);
    new GradleBuildRunner(new GradleOutputStream(updateProgress), System.out, new GradleProgressListener(updateMessage))
    .run(buildGradleDirPath);

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

}
