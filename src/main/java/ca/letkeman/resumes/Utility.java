package ca.letkeman.resumes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utility {

  private Utility(){}

  private static Logger LOGGER = LoggerFactory.getLogger(Utility.class);

  public static String readFileAsString(String fileName) {
    String data = "";
    try {
      data = Files.readString(Paths.get(fileName), StandardCharsets.ISO_8859_1);
    } catch (IOException e) {
      LOGGER.error("Error reading file: {}\n{}", fileName, e.toString());
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
}
