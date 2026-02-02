package ca.letkeman.resumes.optimizer.responses;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LLMResponseTest {

  @Test
  void testDefaultConstructor() {
    LLMResponse response = new LLMResponse();
    Assertions.assertNotNull(response);
  }

  @Test
  void testParameterizedConstructor() {
    Message message = new Message("user", "test content");
    Choice choice = new Choice(0, null, "stop", message);
    List<Choice> choices = Arrays.asList(choice);
    Usage usage = new Usage(10, 20, 30);

    LLMResponse response = new LLMResponse(
        "test-id",
        "chat.completion",
        1234567890L,
        "test-model",
        choices,
        usage,
        "test-fingerprint"
    );

    Assertions.assertEquals("test-id", response.getId());
    Assertions.assertEquals("chat.completion", response.getObject());
    Assertions.assertEquals(1234567890L, response.getCreated());
    Assertions.assertEquals("test-model", response.getModel());
    // Defensive copies are returned, so compare by equality not reference
    Assertions.assertNotNull(response.getChoices());
    Assertions.assertEquals(1, response.getChoices().size());
    Assertions.assertEquals(0, response.getChoices().get(0).getIndex());
    Assertions.assertNotNull(response.getUsage());
    Assertions.assertEquals(10, response.getUsage().getPromptTokens());
    Assertions.assertEquals(20, response.getUsage().getCompletionTokens());
    Assertions.assertEquals(30, response.getUsage().getTotalTokens());
    Assertions.assertEquals("test-fingerprint", response.getSystemFingerprint());
  }

  @Test
  void testSettersAndGetters() {
    LLMResponse response = new LLMResponse();
    Message message = new Message("assistant", "response content");
    Choice choice = new Choice(0, null, "stop", message);
    List<Choice> choices = Arrays.asList(choice);
    Usage usage = new Usage(15, 25, 40);

    response.setId("id-123");
    response.setObject("chat.completion");
    response.setCreated(9876543210L);
    response.setModel("gpt-4");
    response.setChoices(choices);
    response.setUsage(usage);
    response.setSystemFingerprint("fp-456");

    Assertions.assertEquals("id-123", response.getId());
    Assertions.assertEquals("chat.completion", response.getObject());
    Assertions.assertEquals(9876543210L, response.getCreated());
    Assertions.assertEquals("gpt-4", response.getModel());
    // Defensive copies are returned, verify by value not reference
    Assertions.assertNotNull(response.getChoices());
    Assertions.assertEquals(1, response.getChoices().size());
    Assertions.assertEquals(0, response.getChoices().get(0).getIndex());
    Assertions.assertNotNull(response.getUsage());
    Assertions.assertEquals(15, response.getUsage().getPromptTokens());
    Assertions.assertEquals(25, response.getUsage().getCompletionTokens());
    Assertions.assertEquals(40, response.getUsage().getTotalTokens());
    Assertions.assertEquals("fp-456", response.getSystemFingerprint());
  }

  @Test
  void testEquals() {
    Message message = new Message("user", "test");
    Choice choice = new Choice(0, null, "stop", message);
    List<Choice> choices = Arrays.asList(choice);
    Usage usage = new Usage(10, 20, 30);

    LLMResponse response1 = new LLMResponse("id", "obj", 123L, "model", choices, usage, "fp");
    LLMResponse response2 = new LLMResponse("id", "obj", 123L, "model", choices, usage, "fp");
    LLMResponse response3 = new LLMResponse("id2", "obj", 123L, "model", choices, usage, "fp");

    Assertions.assertEquals(response1, response1);
    Assertions.assertEquals(response1, response2);
    Assertions.assertNotEquals(response1, response3);
    Assertions.assertNotEquals(response1, null);
    Assertions.assertNotEquals(response1, new Object());
  }

  @Test
  void testHashCode() {
    Message message = new Message("user", "test");
    Choice choice = new Choice(0, null, "stop", message);
    List<Choice> choices = Arrays.asList(choice);
    Usage usage = new Usage(10, 20, 30);

    LLMResponse response1 = new LLMResponse("id", "obj", 123L, "model", choices, usage, "fp");
    LLMResponse response2 = new LLMResponse("id", "obj", 123L, "model", choices, usage, "fp");

    Assertions.assertEquals(response1.hashCode(), response2.hashCode());
  }

  @Test
  void testGetJSON() {
    Message message = new Message("user", "test content");
    Choice choice = new Choice(0, null, "stop", message);
    List<Choice> choices = Arrays.asList(choice);
    Usage usage = new Usage(10, 20, 30);

    LLMResponse response = new LLMResponse(
        "test-id",
        "chat.completion",
        1234567890L,
        "test-model",
        choices,
        usage,
        "test-fingerprint"
    );

    String json = response.getJSON();
    Assertions.assertNotNull(json);
    Assertions.assertTrue(json.contains("test-id"));
    Assertions.assertTrue(json.contains("test-model"));
    Assertions.assertTrue(json.contains("system_fingerprint"));
  }

  @Test
  void testWithNullChoices() {
    LLMResponse response = new LLMResponse();
    response.setChoices(null);
    Assertions.assertNull(response.getChoices());
  }

  @Test
  void testWithNullUsage() {
    LLMResponse response = new LLMResponse();
    response.setUsage(null);
    Assertions.assertNull(response.getUsage());
  }
}
