package com.adaptris.fxinstaller.helpers;

import java.io.IOException;

import com.adaptris.fxinstaller.utils.ResourceUtils;

public class LicenseLoader {

  public String load() throws IOException {
    return ResourceUtils.toString("/LICENSE.txt");
  }

}
