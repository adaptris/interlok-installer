package com.adaptris.installer.controllers;

import com.adaptris.installer.InstallerWizard;

public abstract class AbstractInstallerController {

  protected final InstallerWizard installerWizard;

  public AbstractInstallerController() {
    installerWizard = InstallerWizard.getInstance();
  }

}
