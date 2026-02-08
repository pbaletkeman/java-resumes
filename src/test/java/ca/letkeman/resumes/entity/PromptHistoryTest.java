package ca.letkeman.resumes.entity;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test class for PromptHistory entity.
 */
class PromptHistoryTest {

    @Test
    void testPromptHistoryDefaultConstructor() {
        PromptHistory history = new PromptHistory();

        Assertions.assertNotNull(history);
        Assertions.assertNull(history.getId());
        Assertions.assertNull(history.getRequestId());
        Assertions.assertNull(history.getPromptType());
    }

    @Test
    void testPromptHistorySettersAndGetters() {
        PromptHistory history = new PromptHistory();

        history.setId(1L);
        history.setRequestId("test-request-id");
        history.setPromptType("Resume");
        history.setCompany("Test Company");
        history.setJobTitle("Software Engineer");
        history.setJobDescription("Job description");
        history.setInterviewerName("John Doe");
        history.setTemperature(0.7);
        history.setModel("mistral");
        history.setExpandedPromptJson("{\"key\":\"value\"}");
        history.setGeneratedContent("Generated content");
        history.setGeneratedFilePath("file1.pdf");
        history.setOutputFormat("pdf");
        history.setStatus("completed");
        history.setErrorMessage(null);
        history.setLlmResponseTimeMs(1500L);
        history.setTokenUsageEstimate(150);

        LocalDateTime now = LocalDateTime.now();
        history.setCreatedAt(now);
        history.setUpdatedAt(now);

        Assertions.assertEquals(1L, history.getId());
        Assertions.assertEquals("test-request-id", history.getRequestId());
        Assertions.assertEquals("Resume", history.getPromptType());
        Assertions.assertEquals("Test Company", history.getCompany());
        Assertions.assertEquals("Software Engineer", history.getJobTitle());
        Assertions.assertEquals("Job description", history.getJobDescription());
        Assertions.assertEquals("John Doe", history.getInterviewerName());
        Assertions.assertEquals(0.7, history.getTemperature());
        Assertions.assertEquals("mistral", history.getModel());
        Assertions.assertEquals("{\"key\":\"value\"}", history.getExpandedPromptJson());
        Assertions.assertEquals("Generated content", history.getGeneratedContent());
        Assertions.assertEquals("file1.pdf", history.getGeneratedFilePath());
        Assertions.assertEquals("pdf", history.getOutputFormat());
        Assertions.assertEquals("completed", history.getStatus());
        Assertions.assertNull(history.getErrorMessage());
        Assertions.assertEquals(1500L, history.getLlmResponseTimeMs());
        Assertions.assertEquals(150, history.getTokenUsageEstimate());
        Assertions.assertEquals(now, history.getCreatedAt());
        Assertions.assertEquals(now, history.getUpdatedAt());
    }

    @Test
    void testPromptHistoryWithErrorMessage() {
        PromptHistory history = new PromptHistory();

        history.setStatus("failed");
        history.setErrorMessage("Error occurred during processing");

        Assertions.assertEquals("failed", history.getStatus());
        Assertions.assertEquals("Error occurred during processing", history.getErrorMessage());
    }

    @Test
    void testPromptHistoryWithMinimalFields() {
        PromptHistory history = new PromptHistory();

        history.setRequestId("minimal-request");
        history.setPromptType("CoverLetter");
        history.setStatus("pending");

        Assertions.assertEquals("minimal-request", history.getRequestId());
        Assertions.assertEquals("CoverLetter", history.getPromptType());
        Assertions.assertEquals("pending", history.getStatus());
        Assertions.assertNull(history.getCompany());
        Assertions.assertNull(history.getJobTitle());
    }
}
