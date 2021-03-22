package com.adaptris.installer.utils;

import java.util.List;
import java.util.stream.Collectors;

import com.adaptris.installer.OptionalComponentCell;
import com.adaptris.installer.models.OptionalComponent;

public class FxUtils {

  private FxUtils() {
  }

  public static int sort(OptionalComponentCell occ1, OptionalComponentCell occ2) {
    return occ1.getName().compareToIgnoreCase(occ2.getName());
  }

  public static List<OptionalComponentCell> convertToCells(List<OptionalComponent> optionalComponents) {
    return optionalComponents.stream().map(oc -> new OptionalComponentCell(oc)).sorted(FxUtils::sort).collect(Collectors.toList());
  }

  public static String getIdOrName(OptionalComponentCell occ, String name) {
    if (occ != null) {
      return occ.getOptionalComponent().getId();
    }
    return name;
  }

}
