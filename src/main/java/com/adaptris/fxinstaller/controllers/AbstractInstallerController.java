package com.adaptris.fxinstaller.controllers;

import com.adaptris.fxinstaller.InstallerWizard;

public abstract class AbstractInstallerController {

  protected final InstallerWizard installerWizard;

  public AbstractInstallerController() {
    installerWizard = InstallerWizard.getInstance();
  }

}
