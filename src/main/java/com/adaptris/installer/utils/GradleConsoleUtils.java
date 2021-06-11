package com.adaptris.installer.utils;

public class GradleConsoleUtils {

  private GradleConsoleUtils() {
  }

  /**
   * Clear ANSI escape code for color, style or cursor move:
   * <ul>
   * <li>Reset: \u001b[0m</li>
   * <li>Black: \u001b[30m</li>
   * <li>Red: \u001b[31m</li>
   * <li>Green: \u001b[32m</li>
   * <li>Yellow: \u001b[33m</li>
   * <li>Blue: \u001b[34m</li>
   * <li>Magenta: \u001b[35m</li>
   * <li>Cyan: \u001b[36m</li>
   * <li>White: \u001b[37m</li>
   * <li>Reset: \u001b[0m</li>
   * <li>Background Black: \u001b[40m</li>
   * <li>Background Red: \u001b[41m</li>
   * <li>Background Green: \u001b[42m</li>
   * <li>Background Yellow: \u001b[43m</li>
   * <li>Background Blue: \u001b[44m</li>
   * <li>Background Magenta: \u001b[45m</li>
   * <li>Background Cyan: \u001b[46m</li>
   * <li>Background White: \u001b[47m</li>
   * <li>Bold: \u001b[1m</li>
   * <li>Underline: \u001b[4m</li>
   * <li>Reversed: \u001b[7m</li>
   * <li>Up: \u001b[{n}A</li>
   * <li>Down: \u001b[{n}B</li>
   * <li>Right: \u001b[{n}C</li>
   * <li>Left: \u001b[{n}D</li>
   * <li>...</li>
   * </ul>
   *
   * @param str
   * @return string with no ANSI escape code
   */
  public static String clearAnsiEscapeCode(String str) {
    if (str != null) {
      return str.replaceAll("\u001b\\[[;\\dA-Z]*m?", "");
    }
    return null;
  }

  /**
   * Remove progress bar from gradle console log. e.g. <==-------->
   *
   * @param str
   * @return string with no progress bar
   */
  public static String clearProgressBar(String str) {
    if (str != null) {
      return str.replaceFirst("\\<=*.*-*\\>\\s?", "");
    }
    return null;
  }

}
