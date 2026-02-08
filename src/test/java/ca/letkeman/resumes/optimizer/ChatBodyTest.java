package ca.letkeman.resumes.optimizer;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
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
    Assertions.assertEquals(model, chatBody.getModel());
  }

  @Test
  void testSetAndGetModelWithNull() {
    chatBody.setModel(null);
    Assertions.assertNull(chatBody.getModel());
  }

  @Test
  void testSetAndGetMessages() {
    List<Message> messages = new ArrayList<>();
    messages.add(new Message("user", "Hello"));
    messages.add(new Message("assistant", "Hi there!"));

    chatBody.setMessages(messages);
    Assertions.assertEquals(messages, chatBody.getMessages());
    Assertions.assertEquals(2, chatBody.getMessages().size());
  }

  @Test
  void testSetAndGetMessagesWithEmptyList() {
    List<Message> emptyMessages = new ArrayList<>();
    chatBody.setMessages(emptyMessages);
    Assertions.assertEquals(emptyMessages, chatBody.getMessages());
    Assertions.assertTrue(chatBody.getMessages().isEmpty());
  }

  @Test
  void testSetAndGetMessagesWithNull() {
    chatBody.setMessages(null);
    Assertions.assertNull(chatBody.getMessages());
  }

  @Test
  void testSetAndGetTemperature() {
    double temperature = 0.7;
    chatBody.setTemperature(temperature);
    Assertions.assertEquals(temperature, chatBody.getTemperature(), 0.001);
  }

  @Test
  void testSetAndGetTemperatureWithZero() {
    chatBody.setTemperature(0.0);
    Assertions.assertEquals(0.0, chatBody.getTemperature(), 0.001);
  }

  @Test
  void testSetAndGetTemperatureWithMax() {
    chatBody.setTemperature(2.0);
    Assertions.assertEquals(2.0, chatBody.getTemperature(), 0.001);
  }

  @Test
  void testSetAndGetMaxTokens() {
    int maxTokens = 1000;
    chatBody.setMaxTokens(maxTokens);
    Assertions.assertEquals(maxTokens, chatBody.getMaxTokens());
  }

  @Test
  void testSetAndGetMaxTokensWithZero() {
    chatBody.setMaxTokens(0);
    Assertions.assertEquals(0, chatBody.getMaxTokens());
  }

  @Test
  void testSetAndGetMaxTokensWithNegative() {
    chatBody.setMaxTokens(-1);
    Assertions.assertEquals(-1, chatBody.getMaxTokens());
  }

  @Test
  void testSetAndGetStream() {
    chatBody.setStream(true);
    Assertions.assertTrue(chatBody.getStream());

    chatBody.setStream(false);
    Assertions.assertFalse(chatBody.getStream());
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

    Assertions.assertEquals(model, chatBody.getModel());
    Assertions.assertEquals(messages, chatBody.getMessages());
    Assertions.assertEquals(temperature, chatBody.getTemperature(), 0.001);
    Assertions.assertEquals(maxTokens, chatBody.getMaxTokens());
    Assertions.assertTrue(chatBody.getStream());
  }

  @Test
  void testDefaultValues() {
    ChatBody newChatBody = new ChatBody();
    Assertions.assertNull(newChatBody.getModel());
    Assertions.assertNull(newChatBody.getMessages());
    Assertions.assertEquals(0.0, newChatBody.getTemperature(), 0.001);
    Assertions.assertEquals(0, newChatBody.getMaxTokens());
    Assertions.assertFalse(newChatBody.getStream());
  }
}
