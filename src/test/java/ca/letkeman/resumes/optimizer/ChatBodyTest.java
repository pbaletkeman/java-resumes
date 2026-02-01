package ca.letkeman.resumes.optimizer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChatBodyTest {

  private ChatBody chatBody;

  @BeforeEach
  void setUp() {
    chatBody = new ChatBody();
  }

  @Test
  void testSetAndGetModel() {
    String model = "gpt-4";
    chatBody.setModel(model);
    assertEquals(model, chatBody.getModel());
  }

  @Test
  void testSetAndGetModelWithNull() {
    chatBody.setModel(null);
    assertNull(chatBody.getModel());
  }

  @Test
  void testSetAndGetMessages() {
    List<Message> messages = new ArrayList<>();
    messages.add(new Message("user", "Hello"));
    messages.add(new Message("assistant", "Hi there!"));

    chatBody.setMessages(messages);
    assertEquals(messages, chatBody.getMessages());
    assertEquals(2, chatBody.getMessages().size());
  }

  @Test
  void testSetAndGetMessagesWithEmptyList() {
    List<Message> emptyMessages = new ArrayList<>();
    chatBody.setMessages(emptyMessages);
    assertEquals(emptyMessages, chatBody.getMessages());
    assertTrue(chatBody.getMessages().isEmpty());
  }

  @Test
  void testSetAndGetMessagesWithNull() {
    chatBody.setMessages(null);
    assertNull(chatBody.getMessages());
  }

  @Test
  void testSetAndGetTemperature() {
    double temperature = 0.7;
    chatBody.setTemperature(temperature);
    assertEquals(temperature, chatBody.getTemperature(), 0.001);
  }

  @Test
  void testSetAndGetTemperatureWithZero() {
    chatBody.setTemperature(0.0);
    assertEquals(0.0, chatBody.getTemperature(), 0.001);
  }

  @Test
  void testSetAndGetTemperatureWithMax() {
    chatBody.setTemperature(2.0);
    assertEquals(2.0, chatBody.getTemperature(), 0.001);
  }

  @Test
  void testSetAndGetMaxTokens() {
    int maxTokens = 1000;
    chatBody.setMaxTokens(maxTokens);
    assertEquals(maxTokens, chatBody.getMaxTokens());
  }

  @Test
  void testSetAndGetMaxTokensWithZero() {
    chatBody.setMaxTokens(0);
    assertEquals(0, chatBody.getMaxTokens());
  }

  @Test
  void testSetAndGetMaxTokensWithNegative() {
    chatBody.setMaxTokens(-1);
    assertEquals(-1, chatBody.getMaxTokens());
  }

  @Test
  void testSetAndGetStream() {
    chatBody.setStream(true);
    assertTrue(chatBody.getStream());

    chatBody.setStream(false);
    assertFalse(chatBody.getStream());
  }

  @Test
  void testCompleteConfiguration() {
    String model = "gpt-4";
    List<Message> messages = new ArrayList<>();
    messages.add(new Message("user", "Test message"));
    double temperature = 0.8;
    int maxTokens = 2000;
    boolean stream = true;

    chatBody.setModel(model);
    chatBody.setMessages(messages);
    chatBody.setTemperature(temperature);
    chatBody.setMaxTokens(maxTokens);
    chatBody.setStream(stream);

    assertEquals(model, chatBody.getModel());
    assertEquals(messages, chatBody.getMessages());
    assertEquals(temperature, chatBody.getTemperature(), 0.001);
    assertEquals(maxTokens, chatBody.getMaxTokens());
    assertTrue(chatBody.getStream());
  }

  @Test
  void testDefaultValues() {
    ChatBody newChatBody = new ChatBody();
    assertNull(newChatBody.getModel());
    assertNull(newChatBody.getMessages());
    assertEquals(0.0, newChatBody.getTemperature(), 0.001);
    assertEquals(0, newChatBody.getMaxTokens());
    assertFalse(newChatBody.getStream());
  }
}
