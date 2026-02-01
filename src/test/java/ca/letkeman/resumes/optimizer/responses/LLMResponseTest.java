package ca.letkeman.resumes.optimizer.responses;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class LLMResponseTest {

  @Test
  void testDefaultConstructor() {
    LLMResponse response = new LLMResponse();
    assertNotNull(response);
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
    
    assertEquals("test-id", response.getId());
    assertEquals("chat.completion", response.getObject());
    assertEquals(1234567890L, response.getCreated());
    assertEquals("test-model", response.getModel());
    assertEquals(choices, response.getChoices());
    assertEquals(usage, response.getUsage());
    assertEquals("test-fingerprint", response.getSystemFingerprint());
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
    
    assertEquals("id-123", response.getId());
    assertEquals("chat.completion", response.getObject());
    assertEquals(9876543210L, response.getCreated());
    assertEquals("gpt-4", response.getModel());
    assertEquals(choices, response.getChoices());
    assertEquals(usage, response.getUsage());
    assertEquals("fp-456", response.getSystemFingerprint());
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
    
    assertEquals(response1, response1);
    assertEquals(response1, response2);
    assertNotEquals(response1, response3);
    assertNotEquals(response1, null);
    assertNotEquals(response1, new Object());
  }

  @Test
  void testHashCode() {
    Message message = new Message("user", "test");
    Choice choice = new Choice(0, null, "stop", message);
    List<Choice> choices = Arrays.asList(choice);
    Usage usage = new Usage(10, 20, 30);
    
    LLMResponse response1 = new LLMResponse("id", "obj", 123L, "model", choices, usage, "fp");
    LLMResponse response2 = new LLMResponse("id", "obj", 123L, "model", choices, usage, "fp");
    
    assertEquals(response1.hashCode(), response2.hashCode());
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
    assertNotNull(json);
    assertTrue(json.contains("test-id"));
    assertTrue(json.contains("test-model"));
    assertTrue(json.contains("system_fingerprint"));
  }

  @Test
  void testWithNullChoices() {
    LLMResponse response = new LLMResponse();
    response.setChoices(null);
    assertNull(response.getChoices());
  }

  @Test
  void testWithNullUsage() {
    LLMResponse response = new LLMResponse();
    response.setUsage(null);
    assertNull(response.getUsage());
  }
}
