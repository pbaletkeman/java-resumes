package ca.letkeman.resumes.optimizer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MessageTest {

  @Test
  void testDefaultConstructor() {
    Message message = new Message();
    assertNull(message.getRole());
    assertNull(message.getContent());
  }

  @Test
  void testParameterizedConstructor() {
    String role = "user";
    String content = "Hello, world!";

    Message message = new Message(role, content);

    assertEquals(role, message.getRole());
    assertEquals(content, message.getContent());
  }

  @Test
  void testParameterizedConstructorWithNulls() {
    Message message = new Message(null, null);
    assertNull(message.getRole());
    assertNull(message.getContent());
  }

  @Test
  void testSetAndGetRole() {
    Message message = new Message();
    String role = "assistant";

    message.setRole(role);

    assertEquals(role, message.getRole());
  }

  @Test
  void testSetAndGetRoleWithNull() {
    Message message = new Message("user", "content");
    message.setRole(null);
    assertNull(message.getRole());
  }

  @Test
  void testSetAndGetRoleWithEmptyString() {
    Message message = new Message();
    message.setRole("");
    assertEquals("", message.getRole());
  }

  @Test
  void testSetAndGetContent() {
    Message message = new Message();
    String content = "This is a test message.";

    message.setContent(content);

    assertEquals(content, message.getContent());
  }

  @Test
  void testSetAndGetContentWithNull() {
    Message message = new Message("user", "initial content");
    message.setContent(null);
    assertNull(message.getContent());
  }

  @Test
  void testSetAndGetContentWithEmptyString() {
    Message message = new Message();
    message.setContent("");
    assertEquals("", message.getContent());
  }

  @Test
  void testMultipleSetOperations() {
    Message message = new Message();

    message.setRole("user");
    message.setContent("First message");
    assertEquals("user", message.getRole());
    assertEquals("First message", message.getContent());

    message.setRole("assistant");
    message.setContent("Second message");
    assertEquals("assistant", message.getRole());
    assertEquals("Second message", message.getContent());
  }

  @Test
  void testSystemRole() {
    Message message = new Message("system", "You are a helpful assistant.");
    assertEquals("system", message.getRole());
    assertEquals("You are a helpful assistant.", message.getContent());
  }

  @Test
  void testLongContent() {
    String longContent = "Lorem ipsum ".repeat(100);
    Message message = new Message("user", longContent);
    assertEquals("user", message.getRole());
    assertEquals(longContent, message.getContent());
  }

  @Test
  void testSpecialCharactersInContent() {
    String specialContent = "Special chars: !@#$%^&*()_+-={}[]|\\:;\"'<>?,./";
    Message message = new Message("user", specialContent);
    assertEquals(specialContent, message.getContent());
  }

  @Test
  void testUnicodeInContent() {
    String unicodeContent = "Hello 世界 🌍";
    Message message = new Message("user", unicodeContent);
    assertEquals(unicodeContent, message.getContent());
  }
}
