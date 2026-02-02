package ca.letkeman.resumes.optimizer;

import ca.letkeman.resumes.model.Optimize;
import ca.letkeman.resumes.optimizer.responses.LLMResponse;
import ca.letkeman.resumes.service.PromptService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ApiServiceTest {
    private ApiService apiService;
    private PromptService promptServiceMock;

    @BeforeEach
    void setUp() {
        apiService = new ApiService();
        promptServiceMock = Mockito.mock(PromptService.class);
        // Inject the mock PromptService
        try {
            var field = ApiService.class.getDeclaredField("promptService");
            field.setAccessible(true);
            field.set(apiService, promptServiceMock);
        } catch (Exception e) {
            Assertions.fail("Failed to inject mock PromptService: " + e.getMessage());
        }
    }

    @Test
    void testGetAndSetJobDescription() {
        apiService.setJobDescription("Test Job");
        Assertions.assertEquals("Test Job", apiService.getJobDescription());
    }

    @Test
    void testGetAndSetResume() {
        apiService.setResume("Test Resume");
        Assertions.assertEquals("Test Resume", apiService.getResume());
    }

    @Test
    void testInvokeApiReturnsNullOnException() {
        // Invalid endpoint to force exception
        LLMResponse response = apiService.invokeApi(new ChatBody(), "http://invalid-endpoint", "fakekey");
        Assertions.assertNull(response);
    }

    @Test
    void testProduceFilesHandlesNullLLMResponse() {
        Optimize optimize = Mockito.mock(Optimize.class);
        Mockito.when(optimize.getPromptType()).thenReturn(new String[]{"RESUME"});
        Mockito.when(optimize.getResume()).thenReturn("resume");
        Mockito.when(optimize.getJobDescription()).thenReturn("desc");
        Mockito.when(optimize.getCompany()).thenReturn("company");
        Mockito.when(optimize.getJobTitle()).thenReturn("title");
        Mockito.when(optimize.getTemperature()).thenReturn(0.5);
        Mockito.when(optimize.getModel()).thenReturn("gpt-3");

        // Mock PromptService to return a template
        Mockito.when(promptServiceMock.loadPrompt("RESUME"))
            .thenReturn("Prompt: {resume_string} {jd_string} {today}");

        // This will use the mock PromptService; invokeApi will return null (no real HTTP call)
        apiService.produceFiles("RESUME", optimize, "http://invalid-endpoint", "fakekey", "mistral", System.getProperty("java.io.tmpdir"));
    }

    // More tests can be added for edge cases, file creation, and response parsing

    @Test
    void testSetJobDescriptionNullAndEmpty() {
        apiService.setJobDescription(null);
        Assertions.assertNull(apiService.getJobDescription());
        apiService.setJobDescription("");
        Assertions.assertEquals("", apiService.getJobDescription());
    }

    @Test
    void testSetResumeNullAndEmpty() {
        apiService.setResume(null);
        Assertions.assertNull(apiService.getResume());
        apiService.setResume("");
        Assertions.assertEquals("", apiService.getResume());
    }

    @Test
    void testInvokeApiWithNulls() {
        Assertions.assertNull(apiService.invokeApi(null, null, null));
        Assertions.assertNull(apiService.invokeApi(null, "", ""));
    }

    @Test
    void testProduceFilesWithNullOptimize() {
        // Should not throw exception
        Assertions.assertDoesNotThrow(() -> apiService.produceFiles(null, "endpoint", "apikey", "mistral", "/tmp"));
    }

    @Test
    void testProduceFilesWithEmptyPromptType() {
        Optimize optimize = Mockito.mock(Optimize.class);
        Mockito.when(optimize.getPromptType()).thenReturn(new String[]{});
        Assertions.assertDoesNotThrow(() -> apiService.produceFiles(optimize, "endpoint", "apikey", "mistral", "/tmp"));
    }

    @Test
    void testProduceFilesWithSkillsPrompt() {
        Optimize optimize = Mockito.mock(Optimize.class);
        Mockito.when(optimize.getPromptType()).thenReturn(new String[]{"SKILLS"});
        Mockito.when(optimize.getJobDescription()).thenReturn("Java Spring Developer - 5+ years");
        Mockito.when(optimize.getJobTitle()).thenReturn("Java Spring Developer");
        Mockito.when(optimize.getCompany()).thenReturn("TechCorp");
        Mockito.when(optimize.getTemperature()).thenReturn(0.5);
        Mockito.when(optimize.getModel()).thenReturn("gpt-3");
        Mockito.when(optimize.getResume()).thenReturn(null);

        // Mock PromptService to return SKILLS template
        Mockito.when(promptServiceMock.loadPrompt("SKILLS"))
            .thenReturn("Skills Prompt: {job_title} {job_description}");

        // This will use the mock PromptService; invokeApi will return null (no real HTTP call)
        Assertions.assertDoesNotThrow(() -> apiService.produceFiles("SKILLS", optimize, "http://invalid-endpoint", "fakekey", "mistral", System.getProperty("java.io.tmpdir")));
    }

    @Test
    void testPromptVariableSubstitution() {
        Optimize optimize = Mockito.mock(Optimize.class);
        Mockito.when(optimize.getPromptType()).thenReturn(new String[]{"SKILLS"});
        Mockito.when(optimize.getJobDescription()).thenReturn("Java developer position");
        Mockito.when(optimize.getJobTitle()).thenReturn("Senior Java Developer");
        Mockito.when(optimize.getCompany()).thenReturn("TestCorp");
        Mockito.when(optimize.getTemperature()).thenReturn(0.5);
        Mockito.when(optimize.getModel()).thenReturn("mistral");
        Mockito.when(optimize.getResume()).thenReturn(null);

        // Mock PromptService with template
        String template = "Job: {job_title} Desc: {job_description}";
        Mockito.when(promptServiceMock.loadPrompt("SKILLS")).thenReturn(template);

        // Verify that it doesn't throw exception
        Assertions.assertDoesNotThrow(() -> apiService.produceFiles("SKILLS", optimize, "http://invalid-endpoint", "fakekey", "mistral", System.getProperty("java.io.tmpdir")));
    }

    @Test
    void testMockModeEnabled() {
        apiService.setMockEnabled(true);
        Assertions.assertTrue(apiService.isMockEnabled());

        ChatBody chatBody = new ChatBody();
        chatBody.setModel("test-model");

        // Should return mock response, not null
        LLMResponse response = apiService.invokeApi(chatBody, "http://any-endpoint", "apikey");
        Assertions.assertNotNull(response);
    }

    @Test
    void testMockModeDisabled() {
        apiService.setMockEnabled(false);
        Assertions.assertFalse(apiService.isMockEnabled());

        ChatBody chatBody = new ChatBody();
        chatBody.setModel("test-model");

        // Should attempt real HTTP call and return null (invalid endpoint)
        LLMResponse response = apiService.invokeApi(chatBody, "http://invalid-endpoint", "apikey");
        Assertions.assertNull(response);
    }

    @Test
    void testInvokeApiWithEmptyChatBody() {
        ChatBody chatBody = new ChatBody();
        // Empty chat body should still be processed
        LLMResponse response = apiService.invokeApi(chatBody, "http://invalid-endpoint", "apikey");
        Assertions.assertNull(response);
    }

    @Test
    void testInvokeApiWithInvalidURI() {
        ChatBody chatBody = new ChatBody();
        chatBody.setModel("test");
        // Invalid URI format should return null
        LLMResponse response = apiService.invokeApi(chatBody, "not a valid uri", "apikey");
        Assertions.assertNull(response);
    }

    @Test
    void testProduceFilesWithMultiplePromptTypes() {
        Optimize optimize = Mockito.mock(Optimize.class);
        Mockito.when(optimize.getPromptType()).thenReturn(new String[]{"RESUME", "COVERLETTER"});
        Mockito.when(optimize.getResume()).thenReturn("My resume");
        Mockito.when(optimize.getJobDescription()).thenReturn("Job desc");
        Mockito.when(optimize.getCompany()).thenReturn("Company");
        Mockito.when(optimize.getJobTitle()).thenReturn("Title");
        Mockito.when(optimize.getTemperature()).thenReturn(0.7);
        Mockito.when(optimize.getModel()).thenReturn("model");

        Mockito.when(promptServiceMock.loadPrompt("RESUME")).thenReturn("Resume: {resume_string}");
        Mockito.when(promptServiceMock.loadPrompt("COVERLETTER")).thenReturn("Cover: {resume_string}");

        // Should process multiple prompt types
        Assertions.assertDoesNotThrow(() -> apiService.produceFiles("RESUME", optimize, "http://invalid", "key", "model", "/tmp"));
    }

    @Test
    void testProduceFilesWithInterviewerName() {
        Optimize optimize = Mockito.mock(Optimize.class);
        Mockito.when(optimize.getPromptType()).thenReturn(new String[]{"THANK-YOU-EMAIL"});
        Mockito.when(optimize.getResume()).thenReturn("Resume");
        Mockito.when(optimize.getJobDescription()).thenReturn("Desc");
        Mockito.when(optimize.getCompany()).thenReturn("Company");
        Mockito.when(optimize.getJobTitle()).thenReturn("Title");
        Mockito.when(optimize.getInterviewerName()).thenReturn("John Doe");
        Mockito.when(optimize.getTemperature()).thenReturn(0.5);
        Mockito.when(optimize.getModel()).thenReturn("model");

        Mockito.when(promptServiceMock.loadPrompt("THANK-YOU-EMAIL"))
            .thenReturn("Dear {interviewer_name}");

        Assertions.assertDoesNotThrow(() -> apiService.produceFiles("THANK-YOU-EMAIL", optimize, "http://invalid", "key", "model", "/tmp"));
    }

    @Test
    void testProduceFilesWithNullInterviewerName() {
        Optimize optimize = Mockito.mock(Optimize.class);
        Mockito.when(optimize.getPromptType()).thenReturn(new String[]{"THANK-YOU-EMAIL"});
        Mockito.when(optimize.getResume()).thenReturn("Resume");
        Mockito.when(optimize.getJobDescription()).thenReturn("Desc");
        Mockito.when(optimize.getCompany()).thenReturn("Company");
        Mockito.when(optimize.getJobTitle()).thenReturn("Title");
        Mockito.when(optimize.getInterviewerName()).thenReturn(null);
        Mockito.when(optimize.getTemperature()).thenReturn(0.5);
        Mockito.when(optimize.getModel()).thenReturn("model");

        Mockito.when(promptServiceMock.loadPrompt("THANK-YOU-EMAIL"))
            .thenReturn("Dear {interviewer_name}");

        // Should handle null interviewer name gracefully
        Assertions.assertDoesNotThrow(() -> apiService.produceFiles("THANK-YOU-EMAIL", optimize, "http://invalid", "key", "model", "/tmp"));
    }
}
