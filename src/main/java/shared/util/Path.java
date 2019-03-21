package shared.util;

import java.io.File;

/**
 * Filepath parser for cross-systems
 */
public class Path {

  /**
   * Parses a string array into a valid filepath for the user's system
   * @param args String array of folder names
   * @return String representing valid filepath
   */
  public static String parse(String... args) {
    String temp = "";
    for (int i = 0; i < args.length - 1; i++) {
      temp += args[i] + File.separator;
    }
    temp += args[args.length - 1];
    return temp;
  }

  /**
   * Parses a filepath using File.seperator
   * @param path Filepath to parse
   * @return Filepath valid for user's system
   */
  public static String convert(String path) {
    return path.replace('/', File.separatorChar).trim();
  }
}
