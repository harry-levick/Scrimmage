package shared.util;

import java.io.File;

public class Path {

  public static String parse(String... args) {
    String temp = "";
    for (int i = 0; i < args.length - 1; i++) {
      temp += args[i] + File.separator;
    }
    temp += args[args.length - 1];
    return temp;
  }

  public static String convert(String path) {
    return path.replace('/', File.separatorChar).trim();
  }
}
