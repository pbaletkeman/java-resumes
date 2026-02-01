package ca.letkeman.resumes.optimizer.responses;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ChoiceTest {

  @Test
  void testDefaultConstructor() {
    Choice choice = new Choice();
    assertNotNull(choice);
  }

  @Test
  void testParameterizedConstructor() {
    Message message = new Message("user", "test content");
    Choice choice = new Choice(0, "null", "stop", message);
    
    assertEquals(0, choice.getIndex());
    assertEquals("null", choice.getLogprobs());
    assertEquals("stop", choice.getFinishReason());
    assertEquals(message, choice.getMessage());
  }

  @Test
  void testSettersAndGetters() {
    Choice choice = new Choice();
    Message message = new Message("assistant", "response");
    
    choice.setIndex(1);
    choice.setLogprobs("logprobs-data");
    choice.setFinishReason("length");
    choice.setMessage(message);
    
    assertEquals(1, choice.getIndex());
    assertEquals("logprobs-data", choice.getLogprobs());
    assertEquals("length", choice.getFinishReason());
    assertEquals(message, choice.getMessage());
  }

  @Test
  void testEquals() {
    Message message = new Message("user", "test");
    Choice choice1 = new Choice(0, null, "stop", message);
    Choice choice2 = new Choice(0, null, "stop", message);
    Choice choice3 = new Choice(1, null, "stop", message);
    
    assertEquals(choice1, choice1);
    assertEquals(choice1, choice2);
    assertNotEquals(choice1, choice3);
    assertNotEquals(choice1, null);
    assertNotEquals(choice1, new Object());
  }

  @Test
  void testHashCode() {
    Message message = new Message("user", "test");
    Choice choice1 = new Choice(0, null, "stop", message);
    Choice choice2 = new Choice(0, null, "stop", message);
    
    assertEquals(choice1.hashCode(), choice2.hashCode());
  }

  @Test
  void testToString() {
    Message message = new Message("user", "test content");
    Choice choice = new Choice(0, "null", "stop", message);
    
    String toString = choice.toString();
    assertNotNull(toString);
    assertTrue(toString.contains("index=0"));
    assertTrue(toString.contains("finishReason='stop'"));
    assertTrue(toString.contains("logprobs='null'"));
  }

  @Test
  void testWithNullMessage() {
    Choice choice = new Choice();
    choice.setMessage(null);
    assertNull(choice.getMessage());
  }

  @Test
  void testWithDifferentFinishReasons() {
    Choice choice1 = new Choice();
    choice1.setFinishReason("stop");
    assertEquals("stop", choice1.getFinishReason());
    
    Choice choice2 = new Choice();
    choice2.setFinishReason("length");
    assertEquals("length", choice2.getFinishReason());
    
    Choice choice3 = new Choice();
    choice3.setFinishReason("content_filter");
    assertEquals("content_filter", choice3.getFinishReason());
  }
}
