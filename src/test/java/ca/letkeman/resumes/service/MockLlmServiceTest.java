package ca.letkeman.resumes.service;

import ca.letkeman.resumes.optimizer.ChatBody;
import ca.letkeman.resumes.optimizer.Message;
import ca.letkeman.resumes.optimizer.responses.LLMResponse;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
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

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
        Assertions.assertTrue(response.getId().startsWith("chatcmpl-mock-"));
        Assertions.assertEquals("chat.completion", response.getObject());
        Assertions.assertNotNull(response.getCreated());
        Assertions.assertNotNull(response.getModel());
        Assertions.assertNotNull(response.getChoices());
        Assertions.assertEquals(1, response.getChoices().size());

        String content = response.getChoices().get(0).getMessage().getContent();
        Assertions.assertNotNull(content);
        Assertions.assertTrue(content.contains("Professional Resume") || content.contains("Summary"));
    }

    @Test
    void testGenerateMockResponseWithCoverLetterPrompt() {
        ChatBody chatBody = createChatBody("Generate a cover letter for this position");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        Assertions.assertNotNull(response);
        String content = response.getChoices().get(0).getMessage().getContent();
        Assertions.assertTrue(content.contains("Dear Hiring Manager") || content.contains("Sincerely"));
    }

    @Test
    void testGenerateMockResponseWithInterviewHRPrompt() {
        ChatBody chatBody = createChatBody("Generate interview HR questions and answers");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        Assertions.assertNotNull(response);
        String content = response.getChoices().get(0).getMessage().getContent();
        Assertions.assertTrue(content.contains("Interview") || content.contains("Tell me about yourself"));
    }

    @Test
    void testGenerateMockResponseWithInterviewJobSpecificPrompt() {
        ChatBody chatBody = createChatBody("Generate interview job specific questions");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        Assertions.assertNotNull(response);
        String content = response.getChoices().get(0).getMessage().getContent();
        Assertions.assertFalse(content.isEmpty());
    }

    @Test
    void testGenerateMockResponseWithReverseInterviewPrompt() {
        ChatBody chatBody = createChatBody("Generate reverse interview questions");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        Assertions.assertNotNull(response);
        String content = response.getChoices().get(0).getMessage().getContent();
        Assertions.assertFalse(content.isEmpty());
    }

    @Test
    void testGenerateMockResponseWithColdEmailPrompt() {
        ChatBody chatBody = createChatBody("Generate a cold email for outreach");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        Assertions.assertNotNull(response);
        String content = response.getChoices().get(0).getMessage().getContent();
        Assertions.assertTrue(content.contains("Subject") || content.contains("email"));
    }

    @Test
    void testGenerateMockResponseWithLinkedInPrompt() {
        ChatBody chatBody = createChatBody("Generate a LinkedIn message for networking");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        Assertions.assertNotNull(response);
        String content = response.getChoices().get(0).getMessage().getContent();
        Assertions.assertTrue(content.contains("LinkedIn") || content.contains("connect"));
    }

    @Test
    void testGenerateMockResponseWithThankYouPrompt() {
        ChatBody chatBody = createChatBody("Generate a thank you email after interview");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        Assertions.assertNotNull(response);
        String content = response.getChoices().get(0).getMessage().getContent();
        Assertions.assertTrue(content.contains("Thank you") || content.contains("interview"));
    }

    @Test
    void testGenerateMockResponseWithSkillsPrompt() {
        ChatBody chatBody = createChatBody("Generate skills analysis");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        Assertions.assertNotNull(response);
        String content = response.getChoices().get(0).getMessage().getContent();
        Assertions.assertTrue(content.contains("Skills") || content.contains("skill"));
    }

    @Test
    void testGenerateMockResponseWithGenericPrompt() {
        ChatBody chatBody = createChatBody("This is a generic prompt");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        Assertions.assertNotNull(response);
        String content = response.getChoices().get(0).getMessage().getContent();
        Assertions.assertFalse(content.isEmpty());
        Assertions.assertTrue(content.contains("Mock LLM Response") || content.length() > 0);
    }

    @Test
    void testGenerateMockResponseWithEmptyChatBody() {
        ChatBody chatBody = new ChatBody();
        chatBody.setModel("test-model");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("test-model", response.getModel());
    }

    @Test
    void testGenerateMockResponseVerifyUsageStats() {
        ChatBody chatBody = createChatBody("Test prompt for token counting");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        Assertions.assertNotNull(response.getUsage());
        Assertions.assertTrue(response.getUsage().getPromptTokens() > 0);
        Assertions.assertTrue(response.getUsage().getCompletionTokens() > 0);
        Assertions.assertEquals(
                response.getUsage().getPromptTokens() + response.getUsage().getCompletionTokens(),
                response.getUsage().getTotalTokens()
        );
    }

    @Test
    void testGenerateMockResponseVerifyMetadata() {
        ChatBody chatBody = createChatBody("Test prompt");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        Assertions.assertNotNull(response.getId());
        Assertions.assertTrue(response.getId().startsWith("chatcmpl-mock-"));
        Assertions.assertEquals("chat.completion", response.getObject());
        Assertions.assertTrue(response.getCreated() > 0);
        Assertions.assertNotNull(response.getSystemFingerprint());
        Assertions.assertTrue(response.getSystemFingerprint().startsWith("mock-fp-"));
    }

    @Test
    void testGenerateMockResponseVerifyChoice() {
        ChatBody chatBody = createChatBody("Test prompt");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        Assertions.assertNotNull(response.getChoices());
        Assertions.assertEquals(1, response.getChoices().size());
        Assertions.assertEquals(0, response.getChoices().get(0).getIndex());
        Assertions.assertEquals("stop", response.getChoices().get(0).getFinishReason());
        Assertions.assertNotNull(response.getChoices().get(0).getMessage());
        Assertions.assertEquals("assistant", response.getChoices().get(0).getMessage().getRole());
        Assertions.assertNotNull(response.getChoices().get(0).getMessage().getContent());
    }

    @Test
    void testGenerateMockResponseWithCustomModel() {
        ChatBody chatBody = createChatBody("Test prompt");
        chatBody.setModel("custom-model-name");

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        Assertions.assertEquals("custom-model-name", response.getModel());
    }

    @Test
    void testGenerateMockResponseWithoutModel() {
        ChatBody chatBody = createChatBody("Test prompt");
        chatBody.setModel(null);

        LLMResponse response = mockLlmService.generateMockResponse(chatBody);

        Assertions.assertEquals("mistral-mock", response.getModel());
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

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getChoices());
        Assertions.assertEquals(1, response.getChoices().size());
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
