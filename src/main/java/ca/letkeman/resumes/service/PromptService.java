package ca.letkeman.resumes.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for loading prompts with hybrid approach:
 * 1. Check external prompts directory (if configured)
 * 2. Fall back to bundled resources in JAR
 *
 * This allows prompts to be updated without recompiling the application.
 * Can be used as a Spring component or instantiated directly.
 */
@Service
public class PromptService {

  private static final Logger LOGGER = LoggerFactory.getLogger(PromptService.class);

  @Value("${prompts.external-dir:}")
  private String externalPromptsDir;

  /**
   * Load a prompt by name (e.g., "RESUME" or "COVER").
   * Tries external directory first, then falls back to bundled resources.
   *
   * @param promptName the name of the prompt file (without extension, e.g., "RESUME")
   * @return the prompt content as a string
   */
  public String loadPrompt(String promptName) {
    if (promptName == null || promptName.trim().isEmpty()) {
      LOGGER.error("Prompt name cannot be null or empty");
      return "";
    }

    String fileName = promptName + ".md";

    // Try external directory first (if configured)
    if (externalPromptsDir != null && !externalPromptsDir.trim().isEmpty()) {
      String externalPrompt = loadFromExternalDirectory(externalPromptsDir, fileName);
      if (externalPrompt != null) {
        LOGGER.info("Loaded prompt from external directory: {}", fileName);
        return externalPrompt;
      }
    }

    // Fall back to bundled resource
    String bundledPrompt = loadFromBundledResources(fileName);
    if (bundledPrompt != null) {
      LOGGER.info("Loaded prompt from bundled resources: {}", fileName);
      return bundledPrompt;
    }

    LOGGER.error("Could not load prompt: {}", fileName);
    return "";
  }

  /**
   * Load prompt from external file system directory.
   *
   * @param externalDir the external directory path
   * @param fileName the prompt file name
   * @return the prompt content, or null if not found
   */
  private String loadFromExternalDirectory(String externalDir, String fileName) {
    try {
      Path promptPath = Paths.get(externalDir).resolve(fileName);

      if (Files.exists(promptPath) && Files.isRegularFile(promptPath)) {
        return Files.readString(promptPath, StandardCharsets.UTF_8);
      }
    } catch (IOException e) {
      LOGGER.debug(
          "Could not load prompt from external directory: {} ({})",
          fileName,
          e.getMessage());
    }

    return null;
  }

  /**
   * Load prompt from bundled resources (in JAR).
   *
   * @param fileName the prompt file name
   * @return the prompt content, or null if not found
   */
  private String loadFromBundledResources(String fileName) {
    try {
      // Try using ClassLoader to load from resources
      ClassLoader classLoader = getClass().getClassLoader();
      java.net.URL resource = classLoader.getResource("prompts/" + fileName);

      if (resource != null) {
        return new String(
            Files.readAllBytes(
                Paths.get(resource.toURI())),
            StandardCharsets.UTF_8);
      }
    } catch (Exception e) {
      LOGGER.debug(
          "Could not load prompt from bundled resources: {} ({})",
          fileName,
          e.getMessage());
    }

    return null;
  }
}
