package ca.letkeman.resumes.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

/**
 * Test class for PromptHistory entity.
 */
class PromptHistoryEntityTest {

  @Test
  void testDefaultConstructor() {
    PromptHistory history = new PromptHistory();
    assertNotNull(history);
    assertNotNull(history.getCreatedAt());
    assertNotNull(history.getUpdatedAt());
    assertEquals("completed", history.getStatus());
    assertEquals("markdown", history.getOutputFormat());
    assertEquals(0.7, history.getTemperature());
  }

  @Test
  void testSetAndGetId() {
    PromptHistory history = new PromptHistory();
    history.setId(1L);
    assertEquals(1L, history.getId());
  }

  @Test
  void testSetAndGetRequestId() {
    PromptHistory history = new PromptHistory();
    history.setRequestId("req-123");
    assertEquals("req-123", history.getRequestId());
  }

  @Test
  void testSetAndGetPromptType() {
    PromptHistory history = new PromptHistory();
    history.setPromptType("interview-hr-questions");
    assertEquals("interview-hr-questions", history.getPromptType());
  }

  @Test
  void testSetAndGetJobDescription() {
    PromptHistory history = new PromptHistory();
    history.setJobDescription("Job description text");
    assertEquals("Job description text", history.getJobDescription());
  }

  @Test
  void testSetAndGetCompany() {
    PromptHistory history = new PromptHistory();
    history.setCompany("TechCorp");
    assertEquals("TechCorp", history.getCompany());
  }

  @Test
  void testSetAndGetJobTitle() {
    PromptHistory history = new PromptHistory();
    history.setJobTitle("Software Engineer");
    assertEquals("Software Engineer", history.getJobTitle());
  }

  @Test
  void testSetAndGetInterviewerName() {
    PromptHistory history = new PromptHistory();
    history.setInterviewerName("John Doe");
    assertEquals("John Doe", history.getInterviewerName());
  }

  @Test
  void testSetAndGetTemperature() {
    PromptHistory history = new PromptHistory();
    history.setTemperature(0.8);
    assertEquals(0.8, history.getTemperature());
  }

  @Test
  void testSetAndGetModel() {
    PromptHistory history = new PromptHistory();
    history.setModel("gpt-4");
    assertEquals("gpt-4", history.getModel());
  }

  @Test
  void testSetAndGetExpandedPromptJson() {
    PromptHistory history = new PromptHistory();
    history.setExpandedPromptJson("{\"key\": \"value\"}");
    assertEquals("{\"key\": \"value\"}", history.getExpandedPromptJson());
  }

  @Test
  void testSetAndGetGeneratedContent() {
    PromptHistory history = new PromptHistory();
    history.setGeneratedContent("Generated content here");
    assertEquals("Generated content here", history.getGeneratedContent());
  }

  @Test
  void testSetAndGetGeneratedFilePath() {
    PromptHistory history = new PromptHistory();
    history.setGeneratedFilePath("/path/to/file.md");
    assertEquals("/path/to/file.md", history.getGeneratedFilePath());
  }

  @Test
  void testSetAndGetOutputFormat() {
    PromptHistory history = new PromptHistory();
    history.setOutputFormat("pdf");
    assertEquals("pdf", history.getOutputFormat());
  }

  @Test
  void testSetAndGetCreatedAt() {
    PromptHistory history = new PromptHistory();
    LocalDateTime now = LocalDateTime.now();
    history.setCreatedAt(now);
    assertEquals(now, history.getCreatedAt());
  }

  @Test
  void testSetAndGetUpdatedAt() {
    PromptHistory history = new PromptHistory();
    LocalDateTime now = LocalDateTime.now();
    history.setUpdatedAt(now);
    assertEquals(now, history.getUpdatedAt());
  }

  @Test
  void testSetAndGetFileSizeBytes() {
    PromptHistory history = new PromptHistory();
    history.setFileSizeBytes(1024L);
    assertEquals(1024L, history.getFileSizeBytes());
  }

  @Test
  void testSetAndGetLlmResponseTimeMs() {
    PromptHistory history = new PromptHistory();
    history.setLlmResponseTimeMs(500L);
    assertEquals(500L, history.getLlmResponseTimeMs());
  }

  @Test
  void testSetAndGetTokenUsageEstimate() {
    PromptHistory history = new PromptHistory();
    history.setTokenUsageEstimate(1000);
    assertEquals(1000, history.getTokenUsageEstimate());
  }

  @Test
  void testSetAndGetStatus() {
    PromptHistory history = new PromptHistory();
    history.setStatus("failed");
    assertEquals("failed", history.getStatus());
  }

  @Test
  void testSetAndGetErrorMessage() {
    PromptHistory history = new PromptHistory();
    history.setErrorMessage("Error occurred");
    assertEquals("Error occurred", history.getErrorMessage());
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
    assertNotNull(result);
    assert(result.contains("id=1"));
    assert(result.contains("requestId='req-123'"));
    assert(result.contains("promptType='interview-hr-questions'"));
    assert(result.contains("company='TechCorp'"));
    assert(result.contains("jobTitle='Software Engineer'"));
    assert(result.contains("status='completed'"));
  }

  @Test
  void testNullValues() {
    PromptHistory history = new PromptHistory();
    assertNull(history.getId());
    assertNull(history.getRequestId());
    assertNull(history.getPromptType());
    assertNull(history.getJobDescription());
    assertNull(history.getCompany());
    assertNull(history.getJobTitle());
    assertNull(history.getInterviewerName());
    assertNull(history.getModel());
    assertNull(history.getExpandedPromptJson());
    assertNull(history.getGeneratedContent());
    assertNull(history.getGeneratedFilePath());
    assertNull(history.getFileSizeBytes());
    assertNull(history.getLlmResponseTimeMs());
    assertNull(history.getTokenUsageEstimate());
    assertNull(history.getErrorMessage());
  }
}
