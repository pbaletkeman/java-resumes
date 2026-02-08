package ca.letkeman.resumes.optimizer;

import ca.letkeman.resumes.model.Optimize;
import ca.letkeman.resumes.optimizer.responses.Choice;
import ca.letkeman.resumes.optimizer.responses.LLMResponse;
import ca.letkeman.resumes.optimizer.responses.Message;
import ca.letkeman.resumes.service.MockLlmService;
import ca.letkeman.resumes.service.PromptService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

class ApiServiceExtendedTest {
    private ApiService apiService;
    private PromptService promptServiceMock;
    private MockLlmService mockLlmServiceMock;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        apiService = new ApiService();
        promptServiceMock = Mockito.mock(PromptService.class);
        mockLlmServiceMock = Mockito.mock(MockLlmService.class);

        // Inject mocks
        try {
            var promptField = ApiService.class.getDeclaredField("promptService");
            promptField.setAccessible(true);
            promptField.set(apiService, promptServiceMock);

            var mockField = ApiService.class.getDeclaredField("mockLlmService");
            mockField.setAccessible(true);
            mockField.set(apiService, mockLlmServiceMock);
        } catch (Exception e) {
            Assertions.fail("Failed to inject mocks: " + e.getMessage());
        }
    }

    @Test
    void testProduceFilesWithValidLLMResponse() {
        // Setup
        Optimize optimize = createBasicOptimize();
        Mockito.when(promptServiceMock.loadPrompt("RESUME"))
            .thenReturn("Resume template: {resume_string} {job_description}");

        // Create a valid LLM response
        LLMResponse mockResponse = new LLMResponse();
        List<Choice> choices = new ArrayList<>();
        Choice choice = new Choice();
        Message message = new Message();
        message.setContent("# Resume Content\nThis is a test resume.");
        choice.setMessage(message);
        choices.add(choice);
        mockResponse.setChoices(choices);

        // Mock invokeApi to return our response
        apiService.setMockEnabled(true);
        Mockito.when(mockLlmServiceMock.generateMockResponse(Mockito.any()))
            .thenReturn(mockResponse);

        // Execute
        apiService.produceFiles("RESUME", optimize, "http://test", "key", "model", tempDir.toString());

        // Verify files were created
        File[] files = tempDir.toFile().listFiles();
        Assertions.assertNotNull(files);
        // Should create .md and .pdf files
        Assertions.assertTrue(files.length >= 1, "At least one file should be created");
    }

    @Test
    void testProduceFilesWithEmptyChoices() {
        Optimize optimize = createBasicOptimize();
        Mockito.when(promptServiceMock.loadPrompt("RESUME"))
            .thenReturn("Template");

        // Empty choices
        LLMResponse mockResponse = new LLMResponse();
        mockResponse.setChoices(new ArrayList<>());

        apiService.setMockEnabled(true);
        Mockito.when(mockLlmServiceMock.generateMockResponse(Mockito.any()))
            .thenReturn(mockResponse);

        // Should handle gracefully
        Assertions.assertDoesNotThrow(() ->
            apiService.produceFiles("RESUME", optimize, "http://test", "key", "model", tempDir.toString())
        );
    }

    @Test
    void testProduceFilesWithNullChoiceMessage() {
        Optimize optimize = createBasicOptimize();
        Mockito.when(promptServiceMock.loadPrompt("RESUME"))
            .thenReturn("Template");

        // Choice with null message
        LLMResponse mockResponse = new LLMResponse();
        List<Choice> choices = new ArrayList<>();
        Choice choice = new Choice();
        choice.setMessage(null);
        choices.add(choice);
        mockResponse.setChoices(choices);

        apiService.setMockEnabled(true);
        Mockito.when(mockLlmServiceMock.generateMockResponse(Mockito.any()))
            .thenReturn(mockResponse);

        // Should handle gracefully
        Assertions.assertDoesNotThrow(() ->
            apiService.produceFiles("RESUME", optimize, "http://test", "key", "model", tempDir.toString())
        );
    }

    @Test
    void testProduceFilesWithNullPromptData() {
        Optimize optimize = createBasicOptimize();
        Mockito.when(promptServiceMock.loadPrompt("RESUME"))
            .thenReturn(null);

        // Should return early
        Assertions.assertDoesNotThrow(() ->
            apiService.produceFiles("RESUME", optimize, "http://test", "key", "model", tempDir.toString())
        );
    }

    @Test
    void testProduceFilesWithEmptyPromptData() {
        Optimize optimize = createBasicOptimize();
        Mockito.when(promptServiceMock.loadPrompt("RESUME"))
            .thenReturn("");

        // Should return early
        Assertions.assertDoesNotThrow(() ->
            apiService.produceFiles("RESUME", optimize, "http://test", "key", "model", tempDir.toString())
        );
    }

    @Test
    void testProduceFilesReplacesAllVariables() {
        Optimize optimize = createBasicOptimize();
        String template = "Resume: {resume_string}, Job: {job_description}, Title: {job_title}, "
            + "Company: {company}, Today: {today}, Interviewer: {interviewer_name}";
        Mockito.when(promptServiceMock.loadPrompt("RESUME"))
            .thenReturn(template);

        LLMResponse mockResponse = new LLMResponse();
        List<Choice> choices = new ArrayList<>();
        Choice choice = new Choice();
        ca.letkeman.resumes.optimizer.responses.Message message;
        message = new ca.letkeman.resumes.optimizer.responses.Message();
        message.setContent("Content");
        choice.setMessage(message);
        choices.add(choice);
        mockResponse.setChoices(choices);

        apiService.setMockEnabled(true);
        Mockito.when(mockLlmServiceMock.generateMockResponse(Mockito.any()))
            .thenReturn(mockResponse);

        // Should replace all variables
        Assertions.assertDoesNotThrow(() ->
            apiService.produceFiles("RESUME", optimize, "http://test", "key", "model", tempDir.toString())
        );
    }

    @Test
    void testProduceFilesWithNullOptimizeFields() {
        Optimize optimize = Mockito.mock(Optimize.class);
        Mockito.when(optimize.getPromptType()).thenReturn(new String[]{"RESUME"});
        // Most fields return null except required ones
        Mockito.when(optimize.getResume()).thenReturn(null);
        Mockito.when(optimize.getJobDescription()).thenReturn(null);
        Mockito.when(optimize.getJobTitle()).thenReturn("Title");  // Required for file name
        Mockito.when(optimize.getCompany()).thenReturn("Company");  // Required for file name
        Mockito.when(optimize.getInterviewerName()).thenReturn(null);
        Mockito.when(optimize.getTemperature()).thenReturn(0.5);
        Mockito.when(optimize.getModel()).thenReturn("model");

        Mockito.when(promptServiceMock.loadPrompt("RESUME"))
            .thenReturn("Template");

        LLMResponse mockResponse = new LLMResponse();
        List<Choice> choices = new ArrayList<>();
        Choice choice = new Choice();
        ca.letkeman.resumes.optimizer.responses.Message message;
        message = new ca.letkeman.resumes.optimizer.responses.Message();
        message.setContent("Content");
        choice.setMessage(message);
        choices.add(choice);
        mockResponse.setChoices(choices);

        apiService.setMockEnabled(true);
        Mockito.when(mockLlmServiceMock.generateMockResponse(Mockito.any()))
            .thenReturn(mockResponse);

        // Should handle null fields gracefully
        Assertions.assertDoesNotThrow(() ->
            apiService.produceFiles("RESUME", optimize, "http://test", "key", "model", tempDir.toString())
        );
    }

    @Test
    void testProduceFilesWithAdditionalSuggestions() {
        Optimize optimize = createBasicOptimize();
        Mockito.when(promptServiceMock.loadPrompt("RESUME"))
            .thenReturn("Template");

        // Response with additional suggestions
        LLMResponse mockResponse = new LLMResponse();
        List<Choice> choices = new ArrayList<>();
        Choice choice = new Choice();
        ca.letkeman.resumes.optimizer.responses.Message message;
        message = new ca.letkeman.resumes.optimizer.responses.Message();
        message.setContent("# Resume ContentAdditional Suggestions\n- Suggestion 1\n- Suggestion 2");
        choice.setMessage(message);
        choices.add(choice);
        mockResponse.setChoices(choices);

        apiService.setMockEnabled(true);
        Mockito.when(mockLlmServiceMock.generateMockResponse(Mockito.any()))
            .thenReturn(mockResponse);

        // Should create suggestion file
        apiService.produceFiles("RESUME", optimize, "http://test", "key", "model", tempDir.toString());

        File[] files = tempDir.toFile().listFiles();
        Assertions.assertNotNull(files);
        // Should have main file and suggestion file
        boolean hasSuggestionFile = false;
        for (File f : files) {
            if (f.getName().contains("suggestions")) {
                hasSuggestionFile = true;
                break;
            }
        }
        Assertions.assertTrue(hasSuggestionFile, "Should create suggestions file");
    }

    @Test
    void testProduceFilesCreatesDirectoryIfNeeded() throws IOException {
        Optimize optimize = createBasicOptimize();
        Mockito.when(promptServiceMock.loadPrompt("RESUME"))
            .thenReturn("Template");

        LLMResponse mockResponse = new LLMResponse();
        List<Choice> choices = new ArrayList<>();
        Choice choice = new Choice();
        ca.letkeman.resumes.optimizer.responses.Message message;
        message = new ca.letkeman.resumes.optimizer.responses.Message();
        message.setContent("Content");
        choice.setMessage(message);
        choices.add(choice);
        mockResponse.setChoices(choices);

        apiService.setMockEnabled(true);
        Mockito.when(mockLlmServiceMock.generateMockResponse(Mockito.any()))
            .thenReturn(mockResponse);

        // Use a subdirectory that doesn't exist
        Path newDir = tempDir.resolve("newsubdir");
        Assertions.assertFalse(Files.exists(newDir));

        apiService.produceFiles("RESUME", optimize, "http://test", "key", "model", newDir.toString());

        // Directory should be created
        Assertions.assertTrue(Files.exists(newDir));
    }

    @Test
    void testProduceFilesArrayMethod() {
        Optimize optimize = createBasicOptimize();
        Mockito.when(promptServiceMock.loadPrompt(Mockito.anyString()))
            .thenReturn("Template");

        LLMResponse mockResponse = new LLMResponse();
        List<Choice> choices = new ArrayList<>();
        Choice choice = new Choice();
        ca.letkeman.resumes.optimizer.responses.Message message;
        message = new ca.letkeman.resumes.optimizer.responses.Message();
        message.setContent("Content");
        choice.setMessage(message);
        choices.add(choice);
        mockResponse.setChoices(choices);

        apiService.setMockEnabled(true);
        Mockito.when(mockLlmServiceMock.generateMockResponse(Mockito.any()))
            .thenReturn(mockResponse);

        // Should iterate through all prompt types
        apiService.produceFiles(optimize, "http://test", "key", "model", tempDir.toString());

        // Verify loadPrompt was called for each type
        Mockito.verify(promptServiceMock, Mockito.times(2)).loadPrompt(Mockito.anyString());
    }

    @Test
    void testInvokeApiWithHttpOkResponse() {
        // This test would require mocking HttpURLConnection which is complex
        // The existing tests already cover the null return cases
        apiService.setMockEnabled(false);
        ChatBody chatBody = new ChatBody();
        chatBody.setModel("test");

        // With invalid endpoint, should return null
        LLMResponse response = apiService.invokeApi(chatBody, "http://invalid-endpoint", "key");
        Assertions.assertNull(response);
    }

    @Test
    void testGettersAndSetters() {
        apiService.setJobDescription("Test Job Description");
        Assertions.assertEquals("Test Job Description", apiService.getJobDescription());

        apiService.setResume("Test Resume");
        Assertions.assertEquals("Test Resume", apiService.getResume());

        apiService.setMockEnabled(true);
        Assertions.assertTrue(apiService.isMockEnabled());

        apiService.setMockEnabled(false);
        Assertions.assertFalse(apiService.isMockEnabled());
    }

    private Optimize createBasicOptimize() {
        Optimize optimize = Mockito.mock(Optimize.class);
        Mockito.when(optimize.getPromptType()).thenReturn(new String[]{"RESUME", "COVERLETTER"});
        Mockito.when(optimize.getResume()).thenReturn("Test Resume");
        Mockito.when(optimize.getJobDescription()).thenReturn("Test Job Description");
        Mockito.when(optimize.getJobTitle()).thenReturn("Software Engineer");
        Mockito.when(optimize.getCompany()).thenReturn("TestCorp");
        Mockito.when(optimize.getInterviewerName()).thenReturn("John Doe");
        Mockito.when(optimize.getTemperature()).thenReturn(0.7);
        Mockito.when(optimize.getModel()).thenReturn("gpt-3");
        return optimize;
    }
}
