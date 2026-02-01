package ca.letkeman.resumes.message;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ResponseMessageTest {

  @Test
  void testConstructorSetsMessage() {
    ResponseMessage message = new ResponseMessage("Success");
    Assertions.assertEquals("Success", message.getMessage());
  }

  @Test
  void testSetMessageUpdatesValue() {
    ResponseMessage message = new ResponseMessage("Initial");
    message.setMessage("Updated");
    Assertions.assertEquals("Updated", message.getMessage());
  }

  @Test
  void testConstructorWithNullMessage() {
    ResponseMessage message = new ResponseMessage(null);
    Assertions.assertNull(message.getMessage());
  }

  @Test
  void testSetMessageWithEmptyString() {
    ResponseMessage message = new ResponseMessage("Original");
    message.setMessage("");
    Assertions.assertEquals("", message.getMessage());
  }

  @Test
  void testMultipleMessageUpdates() {
    ResponseMessage message = new ResponseMessage("First");
    Assertions.assertEquals("First", message.getMessage());

    message.setMessage("Second");
    Assertions.assertEquals("Second", message.getMessage());

    message.setMessage("Third");
    Assertions.assertEquals("Third", message.getMessage());
  }

  @Test
  void testMessageWithSpecialCharacters() {
    String specialMessage = "Test!@#$%^&*()_+-=[]{}|;':\",./<>?";
    ResponseMessage message = new ResponseMessage(specialMessage);
    Assertions.assertEquals(specialMessage, message.getMessage());
  }

  @Test
  void testMessageWithUnicode() {
    String unicodeMessage = "Test 你好 世界 🌍";
    ResponseMessage message = new ResponseMessage(unicodeMessage);
    Assertions.assertEquals(unicodeMessage, message.getMessage());
  }

  @Test
  void testLongMessage() {
    String longMessage = "A".repeat(10000);
    ResponseMessage message = new ResponseMessage(longMessage);
    Assertions.assertEquals(longMessage, message.getMessage());
    Assertions.assertEquals(10000, message.getMessage().length());
  }
}
