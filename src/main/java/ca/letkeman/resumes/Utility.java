package ca.letkeman.resumes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Utility {

  private Utility() {}

  private static Logger logger = LoggerFactory.getLogger(Utility.class);

  public static String readFileAsString(String fileName) {
    String data = "";
    try {
      data = Files.readString(Paths.get(fileName), StandardCharsets.ISO_8859_1);
    } catch (IOException e) {
      logger.error("Error reading file: {}", e.toString());
    }
    return data;
  }

  public static String removeFileExtension(String filename, boolean removeAllExtensions) {
    if (filename == null || filename.isEmpty()) {
      return filename;
    }

    String extPattern = "(?<!^)[.]" + (removeAllExtensions ? ".*" : "[^.]*$");
    return filename.replaceAll(extPattern, "");
  }

  /**
   * Replaces all line ending characters (CR, LF, CR+LF) within a string
   * with the '\n' character.
   *
   * @param inputString The string to process.  Can be null or empty.
   * @return A new string with all line endings replaced by '\n', or the original
   *         string if it was null or empty.
   */
  public static String convertLineEndings(String inputString) {
    if (inputString == null || inputString.isEmpty()) {
      return inputString; // Handle null and empty strings gracefully
    }

    // Use a regular expression to replace all line ending characters.
    // The pattern matches CR, LF, or CRLF.  The 'replaceAll' method is efficient.
    return inputString.replaceAll("[\r\n]+", "\\\\n ").trim();
  }
}
