package ca.letkeman.resumes.optimizer;

import ca.letkeman.resumes.model.Optimize;
import ca.letkeman.resumes.optimizer.responses.LLMResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiServiceTest {
    private ApiService apiService;

    @BeforeEach
    void setUp() {
        apiService = new ApiService();
    }

    @Test
    void testGetAndSetJobDescription() {
        apiService.setJobDescription("Test Job");
        assertEquals("Test Job", apiService.getJobDescription());
    }

    @Test
    void testGetAndSetResume() {
        apiService.setResume("Test Resume");
        assertEquals("Test Resume", apiService.getResume());
    }

    @Test
    void testInvokeApiReturnsNullOnException() {
        // Invalid endpoint to force exception
        LLMResponse response = apiService.invokeApi(new ChatBody(), "http://invalid-endpoint", "fakekey");
        assertNull(response);
    }

    @Test
    void testProduceFilesHandlesNullLLMResponse() {
        Optimize optimize = mock(Optimize.class);
        when(optimize.getPromptType()).thenReturn(new String[]{"resume"});
        when(optimize.getResume()).thenReturn("resume");
        when(optimize.getJobDescription()).thenReturn("desc");
        when(optimize.getCompany()).thenReturn("company");
        when(optimize.getJobTitle()).thenReturn("title");
        when(optimize.getTemperature()).thenReturn(0.5);
        when(optimize.getModel()).thenReturn("gpt-3");

        // Mock Utility.readFileAsString to return a template
        try (MockedStatic<ca.letkeman.resumes.Utility> utilMock = Mockito.mockStatic(ca.letkeman.resumes.Utility.class)) {
            utilMock.when(() -> ca.letkeman.resumes.Utility.readFileAsString(anyString())).thenReturn("Prompt: {resume_string} {jd_string} {today}");
            // This will use the real ApiService, but invokeApi will return null (no real HTTP call)
            apiService.produceFiles("resume", optimize, "http://invalid-endpoint", "fakekey", System.getProperty("java.io.tmpdir"));
        }
    }

    // More tests can be added for edge cases, file creation, and response parsing

    @Test
    void testSetJobDescriptionNullAndEmpty() {
        apiService.setJobDescription(null);
        assertNull(apiService.getJobDescription());
        apiService.setJobDescription("");
        assertEquals("", apiService.getJobDescription());
    }

    @Test
    void testSetResumeNullAndEmpty() {
        apiService.setResume(null);
        assertNull(apiService.getResume());
        apiService.setResume("");
        assertEquals("", apiService.getResume());
    }

    @Test
    void testInvokeApiWithNulls() {
        assertNull(apiService.invokeApi(null, null, null));
        assertNull(apiService.invokeApi(null, "", ""));
    }

    @Test
    void testProduceFilesWithNullOptimize() {
        // Should not throw exception
        assertDoesNotThrow(() -> apiService.produceFiles(null, "endpoint", "apikey", "/tmp"));
    }

    @Test
    void testProduceFilesWithEmptyPromptType() {
        Optimize optimize = mock(Optimize.class);
        when(optimize.getPromptType()).thenReturn(new String[]{});
        assertDoesNotThrow(() -> apiService.produceFiles(optimize, "endpoint", "apikey", "/tmp"));
    }
}
