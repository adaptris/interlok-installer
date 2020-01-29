package com.adaptris.fxinstaller.helpers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.adaptris.fxinstaller.models.InterlokProject;
import com.adaptris.fxinstaller.utils.NumberUtils;

public class InterlokInstaller {
  private LogHelper log = LogHelper.getInstance();

  public void install(InterlokProject interlokProject, Function<Double, Void> updateProgress, Function<String, Void> updateMessage) throws URISyntaxException, IOException {
    log.info("Installing Interlok in '" + interlokProject.getDirectory() + "'");

    Path buildGradleDirPath = new BuildGradleFileGenerator().generate(interlokProject);
    new GradleBuildRunner(new GradleOutputStream(updateProgress, updateMessage), System.out).run(buildGradleDirPath);

    log.info("Interlok successfully installed in '" + interlokProject.getDirectory() + "'");
  }

  public class GradleOutputStream extends ByteArrayOutputStream {

    private static final String PERCENT_PATTERN = "\\<=*.*-*\\> (\\d{1,3})\\% ";
    private Function<Double, Void> updateProgress;
    private Function<String, Void> updateMessage;

    public GradleOutputStream(Function<Double, Void> updateProgress, Function<String, Void> updateMessage) {
      super();
      this.updateProgress = updateProgress;
      this.updateMessage = updateMessage;
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) {
      super.write(b, off, len);
      String progressString = new String(b, off, len)
          .replace("> IDLE ", "")
          .replace(" root project >", "");
      Matcher matcher = Pattern.compile(PERCENT_PATTERN).matcher(progressString);
      if (matcher.find()) {
        String progressPercent = matcher.group(1);
        updateProgress.apply(NumberUtils.toDouble(progressPercent));
        updateMessage.apply(progressString.replaceFirst("\\<=*.*-*\\>", ""));
      } else {
        updateMessage.apply(progressString);
        log.info(progressString);
      }
    }

  }

}
