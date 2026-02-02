package ca.letkeman.resumes.optimizer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MessageTest {

  @Test
  void testDefaultConstructor() {
    Message message = new Message();
    Assertions.assertNull(message.getRole());
    Assertions.assertNull(message.getContent());
  }

  @Test
  void testParameterizedConstructor() {
    String role = "user";
    String content = "Hello, world!";

    Message message = new Message(role, content);

    Assertions.assertEquals(role, message.getRole());
    Assertions.assertEquals(content, message.getContent());
  }

  @Test
  void testParameterizedConstructorWithNulls() {
    Message message = new Message(null, null);
    Assertions.assertNull(message.getRole());
    Assertions.assertNull(message.getContent());
  }

  @Test
  void testSetAndGetRole() {
    Message message = new Message();
    String role = "assistant";

    message.setRole(role);

    Assertions.assertEquals(role, message.getRole());
  }

  @Test
  void testSetAndGetRoleWithNull() {
    Message message = new Message("user", "content");
    message.setRole(null);
    Assertions.assertNull(message.getRole());
  }

  @Test
  void testSetAndGetRoleWithEmptyString() {
    Message message = new Message();
    message.setRole("");
    Assertions.assertEquals("", message.getRole());
  }

  @Test
  void testSetAndGetContent() {
    Message message = new Message();
    String content = "This is a test message.";

    message.setContent(content);

    Assertions.assertEquals(content, message.getContent());
  }

  @Test
  void testSetAndGetContentWithNull() {
    Message message = new Message("user", "initial content");
    message.setContent(null);
    Assertions.assertNull(message.getContent());
  }

  @Test
  void testSetAndGetContentWithEmptyString() {
    Message message = new Message();
    message.setContent("");
    Assertions.assertEquals("", message.getContent());
  }

  @Test
  void testMultipleSetOperations() {
    Message message = new Message();

    message.setRole("user");
    message.setContent("First message");
    Assertions.assertEquals("user", message.getRole());
    Assertions.assertEquals("First message", message.getContent());

    message.setRole("assistant");
    message.setContent("Second message");
    Assertions.assertEquals("assistant", message.getRole());
    Assertions.assertEquals("Second message", message.getContent());
  }

  @Test
  void testSystemRole() {
    Message message = new Message("system", "You are a helpful assistant.");
    Assertions.assertEquals("system", message.getRole());
    Assertions.assertEquals("You are a helpful assistant.", message.getContent());
  }

  @Test
  void testLongContent() {
    String longContent = "Lorem ipsum ".repeat(100);
    Message message = new Message("user", longContent);
    Assertions.assertEquals("user", message.getRole());
    Assertions.assertEquals(longContent, message.getContent());
  }

  @Test
  void testSpecialCharactersInContent() {
    String specialContent = "Special chars: !@#$%^&*()_+-={}[]|\\:;\"'<>?,./";
    Message message = new Message("user", specialContent);
    Assertions.assertEquals(specialContent, message.getContent());
  }

  @Test
  void testUnicodeInContent() {
    String unicodeContent = "Hello 世界 🌍";
    Message message = new Message("user", unicodeContent);
    Assertions.assertEquals(unicodeContent, message.getContent());
  }
}
