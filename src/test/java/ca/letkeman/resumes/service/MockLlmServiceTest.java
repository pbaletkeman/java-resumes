package ca.letkeman.resumes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.letkeman.resumes.optimizer.ChatBody;
import ca.letkeman.resumes.optimizer.Message;
import ca.letkeman.resumes.optimizer.responses.LLMResponse;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for MockLlmService.
 */
class MockLlmServiceTest {

    private MockLlmService mockLlmService;

    @BeforeEach
    void setUp() {
        mockLlmService = new MockLlmService();
    }

    @Test
    void testGenerateMockResponseWithResumePrompt() {
        ChatBody chatBody = createChatBody("Please optimize this resume for a software engineer position");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertTrue(response.getId().startsWith("chatcmpl-mock-"));
        assertEquals("chat.completion", response.getObject());
        assertNotNull(response.getCreated());
        assertNotNull(response.getModel());
        assertNotNull(response.getChoices());
        assertEquals(1, response.getChoices().size());

        String content = response.getChoices().get(0).getMessage().getContent();
        assertNotNull(content);
        assertTrue(content.contains("Professional Resume") || content.contains("Summary"));
    }

    @Test
    void testGenerateMockResponseWithCoverLetterPrompt() {
        ChatBody chatBody = createChatBody("Generate a cover letter for this position");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        assertNotNull(response);
        String content = response.getChoices().get(0).getMessage().getContent();
        assertTrue(content.contains("Dear Hiring Manager") || content.contains("Sincerely"));
    }

    @Test
    void testGenerateMockResponseWithInterviewHRPrompt() {
        ChatBody chatBody = createChatBody("Generate interview HR questions and answers");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        assertNotNull(response);
        String content = response.getChoices().get(0).getMessage().getContent();
        assertTrue(content.contains("Interview") || content.contains("Tell me about yourself"));
    }

    @Test
    void testGenerateMockResponseWithInterviewJobSpecificPrompt() {
        ChatBody chatBody = createChatBody("Generate interview job specific questions");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        assertNotNull(response);
        String content = response.getChoices().get(0).getMessage().getContent();
        assertFalse(content.isEmpty());
    }

    @Test
    void testGenerateMockResponseWithReverseInterviewPrompt() {
        ChatBody chatBody = createChatBody("Generate reverse interview questions");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        assertNotNull(response);
        String content = response.getChoices().get(0).getMessage().getContent();
        assertFalse(content.isEmpty());
    }

    @Test
    void testGenerateMockResponseWithColdEmailPrompt() {
        ChatBody chatBody = createChatBody("Generate a cold email for outreach");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        assertNotNull(response);
        String content = response.getChoices().get(0).getMessage().getContent();
        assertTrue(content.contains("Subject") || content.contains("email"));
    }

    @Test
    void testGenerateMockResponseWithLinkedInPrompt() {
        ChatBody chatBody = createChatBody("Generate a LinkedIn message for networking");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        assertNotNull(response);
        String content = response.getChoices().get(0).getMessage().getContent();
        assertTrue(content.contains("LinkedIn") || content.contains("connect"));
    }

    @Test
    void testGenerateMockResponseWithThankYouPrompt() {
        ChatBody chatBody = createChatBody("Generate a thank you email after interview");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        assertNotNull(response);
        String content = response.getChoices().get(0).getMessage().getContent();
        assertTrue(content.contains("Thank you") || content.contains("interview"));
    }

    @Test
    void testGenerateMockResponseWithSkillsPrompt() {
        ChatBody chatBody = createChatBody("Generate skills analysis");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        assertNotNull(response);
        String content = response.getChoices().get(0).getMessage().getContent();
        assertTrue(content.contains("Skills") || content.contains("skill"));
    }

    @Test
    void testGenerateMockResponseWithGenericPrompt() {
        ChatBody chatBody = createChatBody("This is a generic prompt");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        assertNotNull(response);
        String content = response.getChoices().get(0).getMessage().getContent();
        assertFalse(content.isEmpty());
        assertTrue(content.contains("Mock LLM Response") || content.length() > 0);
    }

    @Test
    void testGenerateMockResponseWithEmptyChatBody() {
        ChatBody chatBody = new ChatBody();
        chatBody.setModel("test-model");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        assertNotNull(response);
        assertEquals("test-model", response.getModel());
    }

    @Test
    void testGenerateMockResponseVerifyUsageStats() {
        ChatBody chatBody = createChatBody("Test prompt for token counting");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        assertNotNull(response.getUsage());
        assertTrue(response.getUsage().getPromptTokens() > 0);
        assertTrue(response.getUsage().getCompletionTokens() > 0);
        assertEquals(
                response.getUsage().getPromptTokens() + response.getUsage().getCompletionTokens(),
                response.getUsage().getTotalTokens()
        );
    }

    @Test
    void testGenerateMockResponseVerifyMetadata() {
        ChatBody chatBody = createChatBody("Test prompt");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        assertNotNull(response.getId());
        assertTrue(response.getId().startsWith("chatcmpl-mock-"));
        assertEquals("chat.completion", response.getObject());
        assertTrue(response.getCreated() > 0);
        assertNotNull(response.getSystemFingerprint());
        assertTrue(response.getSystemFingerprint().startsWith("mock-fp-"));
    }

    @Test
    void testGenerateMockResponseVerifyChoice() {
        ChatBody chatBody = createChatBody("Test prompt");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        assertNotNull(response.getChoices());
        assertEquals(1, response.getChoices().size());
        assertEquals(0, response.getChoices().get(0).getIndex());
        assertEquals("stop", response.getChoices().get(0).getFinishReason());
        assertNotNull(response.getChoices().get(0).getMessage());
        assertEquals("assistant", response.getChoices().get(0).getMessage().getRole());
        assertNotNull(response.getChoices().get(0).getMessage().getContent());
    }

    @Test
    void testGenerateMockResponseWithCustomModel() {
        ChatBody chatBody = createChatBody("Test prompt");
        chatBody.setModel("custom-model-name");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        assertEquals("custom-model-name", response.getModel());
    }

    @Test
    void testGenerateMockResponseWithoutModel() {
        ChatBody chatBody = createChatBody("Test prompt");
        chatBody.setModel(null);

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        assertEquals("mistral-mock", response.getModel());
    }

    @Test
    void testGenerateMockResponseWithMultipleMessages() {
        ChatBody chatBody = new ChatBody();
        List<Message> messages = new ArrayList<>();

        Message systemMsg = new Message();
        systemMsg.setRole("system");
        systemMsg.setContent("You are a helpful assistant");
        messages.add(systemMsg);

        Message userMsg = new Message();
        userMsg.setRole("user");
        userMsg.setContent("Generate a resume");
        messages.add(userMsg);

        chatBody.setMessages(messages);
        chatBody.setModel("test-model");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        assertNotNull(response);
        assertNotNull(response.getChoices());
        assertEquals(1, response.getChoices().size());
    }

    /**
     * Helper method to create a ChatBody with a single user message.
     */
    private ChatBody createChatBody(String userMessage) {
        ChatBody chatBody = new ChatBody();
        List<Message> messages = new ArrayList<>();

        Message message = new Message();
        message.setRole("user");
        message.setContent(userMessage);
        messages.add(message);

        chatBody.setMessages(messages);
        chatBody.setModel("test-model");

        return chatBody;
    }
}
