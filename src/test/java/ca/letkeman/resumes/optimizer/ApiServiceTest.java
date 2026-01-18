package ca.letkeman.resumes.optimizer;

import ca.letkeman.resumes.model.Optimize;
import ca.letkeman.resumes.optimizer.responses.LLMResponse;
import ca.letkeman.resumes.service.PromptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiServiceTest {
    private ApiService apiService;
    private PromptService promptServiceMock;

    @BeforeEach
    void setUp() {
        apiService = new ApiService();
        promptServiceMock = mock(PromptService.class);
        // Inject the mock PromptService
        try {
            var field = ApiService.class.getDeclaredField("promptService");
            field.setAccessible(true);
            field.set(apiService, promptServiceMock);
        } catch (Exception e) {
            fail("Failed to inject mock PromptService: " + e.getMessage());
        }
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
        when(optimize.getPromptType()).thenReturn(new String[]{"RESUME"});
        when(optimize.getResume()).thenReturn("resume");
        when(optimize.getJobDescription()).thenReturn("desc");
        when(optimize.getCompany()).thenReturn("company");
        when(optimize.getJobTitle()).thenReturn("title");
        when(optimize.getTemperature()).thenReturn(0.5);
        when(optimize.getModel()).thenReturn("gpt-3");

        // Mock PromptService to return a template
        when(promptServiceMock.loadPrompt("RESUME"))
            .thenReturn("Prompt: {resume_string} {jd_string} {today}");

        // This will use the mock PromptService; invokeApi will return null (no real HTTP call)
        apiService.produceFiles("RESUME", optimize, "http://invalid-endpoint", "fakekey", "mistral", System.getProperty("java.io.tmpdir"));
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
        assertDoesNotThrow(() -> apiService.produceFiles(null, "endpoint", "apikey", "mistral", "/tmp"));
    }

    @Test
    void testProduceFilesWithEmptyPromptType() {
        Optimize optimize = mock(Optimize.class);
        when(optimize.getPromptType()).thenReturn(new String[]{});
        assertDoesNotThrow(() -> apiService.produceFiles(optimize, "endpoint", "apikey", "mistral", "/tmp"));
    }

    @Test
    void testProduceFilesWithSkillsPrompt() {
        Optimize optimize = mock(Optimize.class);
        when(optimize.getPromptType()).thenReturn(new String[]{"SKILLS"});
        when(optimize.getJobDescription()).thenReturn("Java Spring Developer - 5+ years");
        when(optimize.getJobTitle()).thenReturn("Java Spring Developer");
        when(optimize.getCompany()).thenReturn("TechCorp");
        when(optimize.getTemperature()).thenReturn(0.5);
        when(optimize.getModel()).thenReturn("gpt-3");
        when(optimize.getResume()).thenReturn(null);

        // Mock PromptService to return SKILLS template
        when(promptServiceMock.loadPrompt("SKILLS"))
            .thenReturn("Skills Prompt: {job_title} {job_description}");

        // This will use the mock PromptService; invokeApi will return null (no real HTTP call)
        assertDoesNotThrow(() -> apiService.produceFiles("SKILLS", optimize, "http://invalid-endpoint", "fakekey", "mistral", System.getProperty("java.io.tmpdir")));
    }

    @Test
    void testPromptVariableSubstitution() {
        Optimize optimize = mock(Optimize.class);
        when(optimize.getPromptType()).thenReturn(new String[]{"SKILLS"});
        when(optimize.getJobDescription()).thenReturn("Java developer position");
        when(optimize.getJobTitle()).thenReturn("Senior Java Developer");
        when(optimize.getCompany()).thenReturn("TestCorp");
        when(optimize.getTemperature()).thenReturn(0.5);
        when(optimize.getModel()).thenReturn("mistral");
        when(optimize.getResume()).thenReturn(null);

        // Mock PromptService with template
        String template = "Job: {job_title} Desc: {job_description}";
        when(promptServiceMock.loadPrompt("SKILLS")).thenReturn(template);

        // Verify that it doesn't throw exception
        assertDoesNotThrow(() -> apiService.produceFiles("SKILLS", optimize, "http://invalid-endpoint", "fakekey", "mistral", System.getProperty("java.io.tmpdir")));
    }
}
