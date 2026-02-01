package ca.letkeman.resumes.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

/**
 * Test class for PromptHistory entity.
 */
class PromptHistoryTest {

    @Test
    void testPromptHistoryDefaultConstructor() {
        PromptHistory history = new PromptHistory();

        assertNotNull(history);
        assertNull(history.getId());
        assertNull(history.getRequestId());
        assertNull(history.getPromptType());
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

        assertEquals(1L, history.getId());
        assertEquals("test-request-id", history.getRequestId());
        assertEquals("Resume", history.getPromptType());
        assertEquals("Test Company", history.getCompany());
        assertEquals("Software Engineer", history.getJobTitle());
        assertEquals("Job description", history.getJobDescription());
        assertEquals("John Doe", history.getInterviewerName());
        assertEquals(0.7, history.getTemperature());
        assertEquals("mistral", history.getModel());
        assertEquals("{\"key\":\"value\"}", history.getExpandedPromptJson());
        assertEquals("Generated content", history.getGeneratedContent());
        assertEquals("file1.pdf", history.getGeneratedFilePath());
        assertEquals("pdf", history.getOutputFormat());
        assertEquals("completed", history.getStatus());
        assertNull(history.getErrorMessage());
        assertEquals(1500L, history.getLlmResponseTimeMs());
        assertEquals(150, history.getTokenUsageEstimate());
        assertEquals(now, history.getCreatedAt());
        assertEquals(now, history.getUpdatedAt());
    }

    @Test
    void testPromptHistoryWithErrorMessage() {
        PromptHistory history = new PromptHistory();

        history.setStatus("failed");
        history.setErrorMessage("Error occurred during processing");

        assertEquals("failed", history.getStatus());
        assertEquals("Error occurred during processing", history.getErrorMessage());
    }

    @Test
    void testPromptHistoryWithMinimalFields() {
        PromptHistory history = new PromptHistory();

        history.setRequestId("minimal-request");
        history.setPromptType("CoverLetter");
        history.setStatus("pending");

        assertEquals("minimal-request", history.getRequestId());
        assertEquals("CoverLetter", history.getPromptType());
        assertEquals("pending", history.getStatus());
        assertNull(history.getCompany());
        assertNull(history.getJobTitle());
    }
}
