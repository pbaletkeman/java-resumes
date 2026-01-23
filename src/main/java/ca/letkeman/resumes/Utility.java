package ca.letkeman.resumes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Utility {

  private Utility() {}

  private static Logger logger = LoggerFactory.getLogger(Utility.class);

  /**
   * Read a file as a string from the file system.
   * Attempts to load from an external path if configured via system property.
   *
   * @param fileName the name of the file to read
   * @return the file contents as a string, or empty string if not found
   */
  public static String readFileAsString(String fileName) {
    // Try external path first (if configured)
    String externalPath = System.getProperty("app.config.path");
    if (externalPath != null && !externalPath.isEmpty()) {
      try {
        Path externalFile = Paths.get(externalPath, fileName);
        if (Files.exists(externalFile)) {
          String data = Files.readString(externalFile, StandardCharsets.ISO_8859_1);
          logger.info("Loaded file from external path: {}", externalFile);
          return data;
        }
      } catch (IOException e) {
        logger.debug("Could not load from external path: {}/{}", externalPath, fileName);
      }
    }

    // Fall back to current working directory
    String data = "";
    try {
      data = Files.readString(Paths.get(fileName), StandardCharsets.ISO_8859_1);
      logger.debug("Loaded file from current directory: {}", fileName);
    } catch (IOException e) {
      logger.error("Error reading file: {}", e.toString());
    }
    return data;
  }

  /**
   * Read a file as a string from a specified directory.
   *
   * @param directory the directory path
   * @param fileName the name of the file to read
   * @return the file contents as a string, or empty string if not found
   */
  public static String readFileAsString(String directory, String fileName) {
    String data = "";
    try {
      Path filePath = Paths.get(directory, fileName);
      data = Files.readString(filePath, StandardCharsets.ISO_8859_1);
      logger.debug("Loaded file from directory: {}/{}", directory, fileName);
    } catch (IOException e) {
      logger.error("Error reading file from directory {}: {}", directory, e.toString());
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
