package ca.letkeman.resumes.entity;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test class for PromptHistory entity.
 */
class PromptHistoryEntityTest {

  @Test
  void testDefaultConstructor() {
    PromptHistory history = new PromptHistory();
    Assertions.assertNotNull(history);
    Assertions.assertNotNull(history.getCreatedAt());
    Assertions.assertNotNull(history.getUpdatedAt());
    Assertions.assertEquals("completed", history.getStatus());
    Assertions.assertEquals("markdown", history.getOutputFormat());
    Assertions.assertEquals(0.7, history.getTemperature());
  }

  @Test
  void testSetAndGetId() {
    PromptHistory history = new PromptHistory();
    history.setId(1L);
    Assertions.assertEquals(1L, history.getId());
  }

  @Test
  void testSetAndGetRequestId() {
    PromptHistory history = new PromptHistory();
    history.setRequestId("req-123");
    Assertions.assertEquals("req-123", history.getRequestId());
  }

  @Test
  void testSetAndGetPromptType() {
    PromptHistory history = new PromptHistory();
    history.setPromptType("interview-hr-questions");
    Assertions.assertEquals("interview-hr-questions", history.getPromptType());
  }

  @Test
  void testSetAndGetJobDescription() {
    PromptHistory history = new PromptHistory();
    history.setJobDescription("Job description text");
    Assertions.assertEquals("Job description text", history.getJobDescription());
  }

  @Test
  void testSetAndGetCompany() {
    PromptHistory history = new PromptHistory();
    history.setCompany("TechCorp");
    Assertions.assertEquals("TechCorp", history.getCompany());
  }

  @Test
  void testSetAndGetJobTitle() {
    PromptHistory history = new PromptHistory();
    history.setJobTitle("Software Engineer");
    Assertions.assertEquals("Software Engineer", history.getJobTitle());
  }

  @Test
  void testSetAndGetInterviewerName() {
    PromptHistory history = new PromptHistory();
    history.setInterviewerName("John Doe");
    Assertions.assertEquals("John Doe", history.getInterviewerName());
  }

  @Test
  void testSetAndGetTemperature() {
    PromptHistory history = new PromptHistory();
    history.setTemperature(0.8);
    Assertions.assertEquals(0.8, history.getTemperature());
  }

  @Test
  void testSetAndGetModel() {
    PromptHistory history = new PromptHistory();
    history.setModel("gpt-4");
    Assertions.assertEquals("gpt-4", history.getModel());
  }

  @Test
  void testSetAndGetExpandedPromptJson() {
    PromptHistory history = new PromptHistory();
    history.setExpandedPromptJson("{\"key\": \"value\"}");
    Assertions.assertEquals("{\"key\": \"value\"}", history.getExpandedPromptJson());
  }

  @Test
  void testSetAndGetGeneratedContent() {
    PromptHistory history = new PromptHistory();
    history.setGeneratedContent("Generated content here");
    Assertions.assertEquals("Generated content here", history.getGeneratedContent());
  }

  @Test
  void testSetAndGetGeneratedFilePath() {
    PromptHistory history = new PromptHistory();
    history.setGeneratedFilePath("/path/to/file.md");
    Assertions.assertEquals("/path/to/file.md", history.getGeneratedFilePath());
  }

  @Test
  void testSetAndGetOutputFormat() {
    PromptHistory history = new PromptHistory();
    history.setOutputFormat("pdf");
    Assertions.assertEquals("pdf", history.getOutputFormat());
  }

  @Test
  void testSetAndGetCreatedAt() {
    PromptHistory history = new PromptHistory();
    LocalDateTime now = LocalDateTime.now();
    history.setCreatedAt(now);
    Assertions.assertEquals(now, history.getCreatedAt());
  }

  @Test
  void testSetAndGetUpdatedAt() {
    PromptHistory history = new PromptHistory();
    LocalDateTime now = LocalDateTime.now();
    history.setUpdatedAt(now);
    Assertions.assertEquals(now, history.getUpdatedAt());
  }

  @Test
  void testSetAndGetFileSizeBytes() {
    PromptHistory history = new PromptHistory();
    history.setFileSizeBytes(1024L);
    Assertions.assertEquals(1024L, history.getFileSizeBytes());
  }

  @Test
  void testSetAndGetLlmResponseTimeMs() {
    PromptHistory history = new PromptHistory();
    history.setLlmResponseTimeMs(500L);
    Assertions.assertEquals(500L, history.getLlmResponseTimeMs());
  }

  @Test
  void testSetAndGetTokenUsageEstimate() {
    PromptHistory history = new PromptHistory();
    history.setTokenUsageEstimate(1000);
    Assertions.assertEquals(1000, history.getTokenUsageEstimate());
  }

  @Test
  void testSetAndGetStatus() {
    PromptHistory history = new PromptHistory();
    history.setStatus("failed");
    Assertions.assertEquals("failed", history.getStatus());
  }

  @Test
  void testSetAndGetErrorMessage() {
    PromptHistory history = new PromptHistory();
    history.setErrorMessage("Error occurred");
    Assertions.assertEquals("Error occurred", history.getErrorMessage());
  }

  @Test
  void testToString() {
    PromptHistory history = new PromptHistory();
    history.setId(1L);
    history.setRequestId("req-123");
    history.setPromptType("interview-hr-questions");
    history.setCompany("TechCorp");
    history.setJobTitle("Software Engineer");
    history.setStatus("completed");

    String result = history.toString();
    Assertions.assertNotNull(result);
    Assertions.assertTrue(result.contains("id=1"));
    Assertions.assertTrue(result.contains("requestId='req-123'"));
    Assertions.assertTrue(result.contains("promptType='interview-hr-questions'"));
    Assertions.assertTrue(result.contains("company='TechCorp'"));
    Assertions.assertTrue(result.contains("jobTitle='Software Engineer'"));
    Assertions.assertTrue(result.contains("status='completed'"));
  }

  @Test
  void testNullValues() {
    PromptHistory history = new PromptHistory();
    Assertions.assertNull(history.getId());
    Assertions.assertNull(history.getRequestId());
    Assertions.assertNull(history.getPromptType());
    Assertions.assertNull(history.getJobDescription());
    Assertions.assertNull(history.getCompany());
    Assertions.assertNull(history.getJobTitle());
    Assertions.assertNull(history.getInterviewerName());
    Assertions.assertNull(history.getModel());
    Assertions.assertNull(history.getExpandedPromptJson());
    Assertions.assertNull(history.getGeneratedContent());
    Assertions.assertNull(history.getGeneratedFilePath());
    Assertions.assertNull(history.getFileSizeBytes());
    Assertions.assertNull(history.getLlmResponseTimeMs());
    Assertions.assertNull(history.getTokenUsageEstimate());
    Assertions.assertNull(history.getErrorMessage());
  }
}
