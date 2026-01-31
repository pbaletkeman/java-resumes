package ca.letkeman.resumes.service;

import ca.letkeman.resumes.entity.PromptHistory;
import ca.letkeman.resumes.repository.PromptHistoryRepository;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for loading prompts with hybrid approach:
 * 1. Check external prompts directory (if configured)
 * 2. Fall back to bundled resources in JAR
 *
 * Also provides database persistence for prompt history tracking.
 * Can be used as a Spring component or instantiated directly.
 */
@Service
public class PromptService {

  private static final Logger LOGGER = LoggerFactory.getLogger(PromptService.class);

  @Value("${prompts.external-dir:}")
  private String externalPromptsDir;

  @Autowired(required = false)
  private PromptHistoryRepository promptHistoryRepository;

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

  /**
   * Expand a prompt template by replacing variables with actual values.
   *
   * @param promptTemplate the template content with {variable} placeholders
   * @param variables map of variable names to values
   * @return the expanded prompt with variables replaced
   */
  public String expandPrompt(String promptTemplate, Map<String, String> variables) {
    if (promptTemplate == null || promptTemplate.isEmpty()) {
      return "";
    }

    String expanded = promptTemplate;
    for (Map.Entry<String, String> entry : variables.entrySet()) {
      String placeholder = "{" + entry.getKey() + "}";
      String value = entry.getValue() != null ? entry.getValue() : "";
      expanded = expanded.replace(placeholder, value);
    }

    return expanded;
  }

  /**
   * Save prompt generation to history database.
   *
   * @param promptType type of prompt generated
   * @param jobDescription job description input
   * @param company company name
   * @param jobTitle job title
   * @param interviewerName interviewer name (optional)
   * @param temperature LLM temperature setting
   * @param model LLM model used
   * @param expandedPrompt the full expanded prompt sent to LLM
   * @param generatedContent the LLM response
   * @param filePath path to the generated file
   * @param llmResponseTimeMs LLM response time in milliseconds
   * @return the saved PromptHistory entity
   */
  public PromptHistory savePromptToHistory(
      String promptType,
      String jobDescription,
      String company,
      String jobTitle,
      String interviewerName,
      Double temperature,
      String model,
      String expandedPrompt,
      String generatedContent,
      String filePath,
      Long llmResponseTimeMs) {

    if (promptHistoryRepository == null) {
      LOGGER.warn("PromptHistoryRepository not available, skipping history save");
      return null;
    }

    PromptHistory history = new PromptHistory();
    history.setRequestId(UUID.randomUUID().toString());
    history.setPromptType(promptType);
    history.setJobDescription(jobDescription);
    history.setCompany(company);
    history.setJobTitle(jobTitle);
    history.setInterviewerName(interviewerName);
    history.setTemperature(temperature);
    history.setModel(model);
    history.setExpandedPromptJson(expandedPrompt);
    history.setGeneratedContent(generatedContent);
    history.setGeneratedFilePath(filePath);
    history.setLlmResponseTimeMs(llmResponseTimeMs);
    history.setStatus("completed");

    try {
      return promptHistoryRepository.save(history);
    } catch (Exception e) {
      LOGGER.error("Failed to save prompt history: {}", e.getMessage());
      return null;
    }
  }

  /**
   * Get all prompt history records.
   *
   * @return list of all prompt history records
   */
  public List<PromptHistory> getAllHistory() {
    if (promptHistoryRepository == null) {
      LOGGER.warn("PromptHistoryRepository not available");
      return List.of();
    }
    return promptHistoryRepository.findAllByOrderByCreatedAtDesc();
  }

  /**
   * Get prompt history records by type.
   *
   * @param promptType the prompt type to filter by
   * @return list of prompt history records of the specified type
   */
  public List<PromptHistory> getHistoryByType(String promptType) {
    if (promptHistoryRepository == null) {
      LOGGER.warn("PromptHistoryRepository not available");
      return List.of();
    }
    return promptHistoryRepository.findByPromptTypeOrderByCreatedAtDesc(promptType);
  }

  /**
   * Get a specific prompt history record by ID.
   *
   * @param id the history record ID
   * @return Optional containing the history record if found
   */
  public Optional<PromptHistory> getHistoryById(Long id) {
    if (promptHistoryRepository == null) {
      LOGGER.warn("PromptHistoryRepository not available");
      return Optional.empty();
    }
    return promptHistoryRepository.findById(id);
  }

  /**
   * Delete a prompt history record by ID.
   *
   * @param id the history record ID to delete
   */
  public void deleteHistoryById(Long id) {
    if (promptHistoryRepository == null) {
      LOGGER.warn("PromptHistoryRepository not available");
      return;
    }
    promptHistoryRepository.deleteById(id);
  }

  /**
   * Get prompt history records by company.
   *
   * @param company the company name
   * @return list of prompt history records for the company
   */
  public List<PromptHistory> getHistoryByCompany(String company) {
    if (promptHistoryRepository == null) {
      LOGGER.warn("PromptHistoryRepository not available");
      return List.of();
    }
    return promptHistoryRepository.findByCompany(company);
  }

  /**
   * Get prompt history records within a date range.
   *
   * @param startDate start date/time
   * @param endDate end date/time
   * @return list of prompt history records in the date range
   */
  public List<PromptHistory> getHistoryByDateRange(
      LocalDateTime startDate, LocalDateTime endDate) {
    if (promptHistoryRepository == null) {
      LOGGER.warn("PromptHistoryRepository not available");
      return List.of();
    }
    return promptHistoryRepository.findByCreatedAtBetween(startDate, endDate);
  }
}
