package com.adaptris.installer.helpers;

import java.io.IOException;

import com.adaptris.installer.utils.ResourceUtils;

public class LicenseLoader {

  public String load() throws IOException {
    return ResourceUtils.toString("/LICENSE.txt");
  }

}
