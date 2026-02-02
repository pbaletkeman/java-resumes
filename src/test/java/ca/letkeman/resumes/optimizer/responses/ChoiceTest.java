package ca.letkeman.resumes.optimizer.responses;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ChoiceTest {

  @Test
  void testDefaultConstructor() {
    Choice choice = new Choice();
    Assertions.assertNotNull(choice);
  }

  @Test
  void testParameterizedConstructor() {
    Message message = new Message("user", "test content");
    Choice choice = new Choice(0, "null", "stop", message);

    Assertions.assertEquals(0, choice.getIndex());
    Assertions.assertEquals("null", choice.getLogprobs());
    Assertions.assertEquals("stop", choice.getFinishReason());
    Assertions.assertEquals(message, choice.getMessage());
  }

  @Test
  void testSettersAndGetters() {
    Choice choice = new Choice();
    Message message = new Message("assistant", "response");

    choice.setIndex(1);
    choice.setLogprobs("logprobs-data");
    choice.setFinishReason("length");
    choice.setMessage(message);

    Assertions.assertEquals(1, choice.getIndex());
    Assertions.assertEquals("logprobs-data", choice.getLogprobs());
    Assertions.assertEquals("length", choice.getFinishReason());
    Assertions.assertEquals(message, choice.getMessage());
  }

  @Test
  void testEquals() {
    Message message = new Message("user", "test");
    Choice choice1 = new Choice(0, null, "stop", message);
    Choice choice2 = new Choice(0, null, "stop", message);
    Choice choice3 = new Choice(1, null, "stop", message);

    Assertions.assertEquals(choice1, choice1);
    Assertions.assertEquals(choice1, choice2);
    Assertions.assertNotEquals(choice1, choice3);
    Assertions.assertNotEquals(choice1, null);
    Assertions.assertNotEquals(choice1, new Object());
  }

  @Test
  void testHashCode() {
    Message message = new Message("user", "test");
    Choice choice1 = new Choice(0, null, "stop", message);
    Choice choice2 = new Choice(0, null, "stop", message);

    Assertions.assertEquals(choice1.hashCode(), choice2.hashCode());
  }

  @Test
  void testToString() {
    Message message = new Message("user", "test content");
    Choice choice = new Choice(0, "null", "stop", message);

    String toString = choice.toString();
    Assertions.assertNotNull(toString);
    Assertions.assertTrue(toString.contains("index=0"));
    Assertions.assertTrue(toString.contains("finishReason='stop'"));
    Assertions.assertTrue(toString.contains("logprobs='null'"));
  }

  @Test
  void testWithNullMessage() {
    Choice choice = new Choice();
    choice.setMessage(null);
    Assertions.assertNull(choice.getMessage());
  }

  @Test
  void testWithDifferentFinishReasons() {
    Choice choice1 = new Choice();
    choice1.setFinishReason("stop");
    Assertions.assertEquals("stop", choice1.getFinishReason());

    Choice choice2 = new Choice();
    choice2.setFinishReason("length");
    Assertions.assertEquals("length", choice2.getFinishReason());

    Choice choice3 = new Choice();
    choice3.setFinishReason("content_filter");
    Assertions.assertEquals("content_filter", choice3.getFinishReason());
  }
}
