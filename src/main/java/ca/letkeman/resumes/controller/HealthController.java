package ca.letkeman.resumes.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Health check endpoints for system diagnostics and monitoring.
 * Provides detailed health information about various system components.
 */
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:80",
    "http://127.0.0.1:3000", "http://127.0.0.1:5173", "http://127.0.0.1:80"},
    allowCredentials = "true",
    maxAge = 3600)
@RestController
@RequestMapping("/api/health")
public final class HealthController {

  private static final Logger LOGGER = LoggerFactory.getLogger(HealthController.class);

  @Value("${spring.datasource.url:}")
  private String datasourceUrl;

  @Value("${spring.datasource.username:}")
  private String datasourceUsername;

  @Value("${spring.datasource.password:}")
  private String datasourcePassword;

  @Value("${llm.endpoint:}")
  private String llmEndpoint;

  @Value("${upload.path:./uploads}")
  private String uploadPath;

  /**
   * Overall system health check.
   * Returns the status of all components.
   *
   * @return JSON with overall status and component health
   */
  @GetMapping
  public ResponseEntity<Map<String, Object>> getOverallHealth() {
    Map<String, Object> health = new HashMap<>();
    health.put("status", "UP");
    health.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
    health.put("service", "Resume Optimization API");

    // Check database connectivity
    Map<String, Object> dbHealth = checkDatabase();
    health.put("database", dbHealth);

    // Check LLM service connectivity
    Map<String, Object> llmHealth = checkLLMConnectivity();
    health.put("llm", llmHealth);

    // Check disk space
    Map<String, Object> diskHealth = checkDiskSpace();
    health.put("disk", diskHealth);

    // Set overall status based on critical components
    String overallStatus = "UP";
    if ("DOWN".equals(dbHealth.get("status"))) {
      overallStatus = "DEGRADED";
    }
    if ("DOWN".equals(llmHealth.get("status")) && "DOWN".equals(diskHealth.get("status"))) {
      overallStatus = "DOWN";
    }

    health.put("overall_status", overallStatus);

    HttpStatus statusCode = "UP".equals(overallStatus) || "DEGRADED".equals(overallStatus)
        ? HttpStatus.OK
        : HttpStatus.SERVICE_UNAVAILABLE;

    return ResponseEntity.status(statusCode).body(health);
  }

  /**
   * Check database connectivity.
   * Attempts to establish a connection to verify database availability.
   *
   * @return JSON with database status and connection details
   */
  @GetMapping("/database")
  public ResponseEntity<Map<String, Object>> checkDatabaseEndpoint() {
    Map<String, Object> dbHealth = checkDatabase();
    HttpStatus statusCode =
        "UP".equals(dbHealth.get("status")) ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
    return ResponseEntity.status(statusCode).body(dbHealth);
  }

  /**
   * Check LLM service connectivity.
   * Verifies that the LLM service is reachable and responding.
   *
   * @return JSON with LLM service status
   */
  @GetMapping("/llm")
  public ResponseEntity<Map<String, Object>> checkLLMEndpoint() {
    Map<String, Object> llmHealth = checkLLMConnectivity();
    HttpStatus statusCode =
        "UP".equals(llmHealth.get("status")) ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
    return ResponseEntity.status(statusCode).body(llmHealth);
  }

  /**
   * Check disk space availability.
   * Monitors disk space on the upload directory.
   *
   * @return JSON with disk space status
   */
  @GetMapping("/disk")
  public ResponseEntity<Map<String, Object>> checkDiskEndpoint() {
    Map<String, Object> diskHealth = checkDiskSpace();
    HttpStatus statusCode =
        "UP".equals(diskHealth.get("status")) ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
    return ResponseEntity.status(statusCode).body(diskHealth);
  }

  /**
   * Internal method to check database connectivity.
   *
   * @return Map with database status details
   */
  private Map<String, Object> checkDatabase() {
    Map<String, Object> dbHealth = new HashMap<>();
    dbHealth.put("type", getDatabaseType());
    dbHealth.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

    if (datasourceUrl == null || datasourceUrl.isEmpty()) {
      dbHealth.put("status", "DISABLED");
      dbHealth.put("message", "No datasource configured");
      return dbHealth;
    }

    try {
      // Attempt connection based on database type
      boolean connected = testDatabaseConnection();

      if (connected) {
        dbHealth.put("status", "UP");
        dbHealth.put("message", "Database connection successful");
        dbHealth.put("url", maskSensitiveData(datasourceUrl));
      } else {
        dbHealth.put("status", "DOWN");
        dbHealth.put("message", "Database connection failed");
        dbHealth.put("url", maskSensitiveData(datasourceUrl));
      }
    } catch (Exception e) {
      dbHealth.put("status", "DOWN");
      dbHealth.put("message", "Database connection error: " + e.getMessage());
      dbHealth.put("url", maskSensitiveData(datasourceUrl));
      LOGGER.warn("Database health check failed: {}", e.getMessage());
    }

    return dbHealth;
  }

  /**
   * Test database connection based on the configured datasource URL.
   *
   * @return true if connection is successful, false otherwise
   * @throws Exception if connection fails
   */
  private boolean testDatabaseConnection() throws Exception {
    if (datasourceUrl.contains("sqlite")) {
      // SQLite - check if file exists and is accessible
      String filePath = datasourceUrl.replace("jdbc:sqlite:", "");
      Path path = Paths.get(filePath);
      return Files.isWritable(path.getParent()) || Files.exists(path);
    } else if (datasourceUrl.contains("postgresql")) {
      // PostgreSQL - attempt actual connection
      try (Connection connection = DriverManager.getConnection(datasourceUrl, datasourceUsername,
          datasourcePassword)) {
        return connection.isValid(5);
      }
    } else if (datasourceUrl.contains("mysql")) {
      // MySQL - attempt actual connection
      try (Connection connection = DriverManager.getConnection(datasourceUrl, datasourceUsername,
          datasourcePassword)) {
        return connection.isValid(5);
      }
    }

    return false;
  }

  /**
   * Determine the database type from the datasource URL.
   *
   * @return String representing the database type
   */
  private String getDatabaseType() {
    if (datasourceUrl == null || datasourceUrl.isEmpty()) {
      return "NONE";
    }

    if (datasourceUrl.contains("sqlite")) {
      return "SQLite";
    } else if (datasourceUrl.contains("postgresql")) {
      return "PostgreSQL";
    } else if (datasourceUrl.contains("mysql")) {
      return "MySQL";
    } else if (datasourceUrl.contains("h2")) {
      return "H2";
    }

    return "UNKNOWN";
  }

  /**
   * Internal method to check LLM service connectivity.
   *
   * @return Map with LLM service status details
   */
  private Map<String, Object> checkLLMConnectivity() {
    Map<String, Object> llmHealth = new HashMap<>();
    llmHealth.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

    if (llmEndpoint == null || llmEndpoint.isEmpty()) {
      llmHealth.put("status", "DISABLED");
      llmHealth.put("message", "No LLM endpoint configured");
      return llmHealth;
    }

    try {
      // Try to reach the LLM endpoint with a simple GET request
      java.net.HttpURLConnection connection =
          (java.net.HttpURLConnection) new java.net.URI(llmEndpoint.replace("/v1/chat/completions", "")).toURL()
              .openConnection();
      connection.setConnectTimeout(3000);
      connection.setReadTimeout(3000);
      connection.setRequestMethod("GET");

      int responseCode = connection.getResponseCode();
      connection.disconnect();

      if (responseCode >= 200 && responseCode < 300) {
        llmHealth.put("status", "UP");
        llmHealth.put("message", "LLM service is reachable");
        llmHealth.put("endpoint", maskSensitiveData(llmEndpoint));
        llmHealth.put("response_code", responseCode);
      } else {
        llmHealth.put("status", "DOWN");
        llmHealth.put("message", "LLM service returned error: " + responseCode);
        llmHealth.put("endpoint", maskSensitiveData(llmEndpoint));
        llmHealth.put("response_code", responseCode);
      }
    } catch (java.net.SocketTimeoutException e) {
      llmHealth.put("status", "DOWN");
      llmHealth.put("message", "LLM service connection timeout");
      llmHealth.put("endpoint", maskSensitiveData(llmEndpoint));
      LOGGER.warn("LLM service timeout: {}", e.getMessage());
    } catch (Exception e) {
      llmHealth.put("status", "DOWN");
      llmHealth.put("message", "LLM service connection error: " + e.getMessage());
      llmHealth.put("endpoint", maskSensitiveData(llmEndpoint));
      LOGGER.warn("LLM health check failed: {}", e.getMessage());
    }

    return llmHealth;
  }

  /**
   * Internal method to check disk space availability.
   *
   * @return Map with disk space status details
   */
  private Map<String, Object> checkDiskSpace() {
    Map<String, Object> diskHealth = new HashMap<>();
    diskHealth.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
    diskHealth.put("upload_path", uploadPath);

    try {
      File uploadDir = new File(uploadPath);

      // Create directory if it doesn't exist
      if (!uploadDir.exists()) {
        uploadDir.mkdirs();
      }

      // Get free disk space
      long usableSpace = uploadDir.getUsableSpace();
      long totalSpace = uploadDir.getTotalSpace();
      long freeSpace = uploadDir.getFreeSpace();

      // Convert to MB for readability
      long usableMB = usableSpace / (1024 * 1024);
      long totalMB = totalSpace / (1024 * 1024);
      long freeMB = freeSpace / (1024 * 1024);

      // Consider critical if less than 100MB
      if (usableMB < 100) {
        diskHealth.put("status", "CRITICAL");
        diskHealth.put("message", "Disk space is critically low");
      } else if (usableMB < 500) {
        diskHealth.put("status", "WARNING");
        diskHealth.put("message", "Disk space is running low");
      } else {
        diskHealth.put("status", "UP");
        diskHealth.put("message", "Disk space is available");
      }

      diskHealth.put("usable_mb", usableMB);
      diskHealth.put("total_mb", totalMB);
      diskHealth.put("free_mb", freeMB);
      diskHealth.put("usage_percent", totalMB > 0 ? (totalMB - freeMB) * 100 / totalMB : 0);

    } catch (Exception e) {
      diskHealth.put("status", "DOWN");
      diskHealth.put("message", "Disk space check error: " + e.getMessage());
      LOGGER.warn("Disk health check failed: {}", e.getMessage());
    }

    return diskHealth;
  }

  /**
   * Mask sensitive data in URLs and connection strings.
   *
   * @param data The data to mask
   * @return Masked data with password removed
   */
  private String maskSensitiveData(String data) {
    if (data == null || data.isEmpty()) {
      return data;
    }

    // Mask password and API key in URLs
    return data.replaceAll("(password|apikey|api_key)=[^&]*", "$1=***")
        .replaceAll("://[^:]*:[^@]*@", "://***:***@");
  }
}
