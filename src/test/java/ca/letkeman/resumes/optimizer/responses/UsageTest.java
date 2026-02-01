package ca.letkeman.resumes.optimizer.responses;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UsageTest {

  @Test
  void testDefaultConstructor() {
    Usage usage = new Usage();
    assertNotNull(usage);
  }

  @Test
  void testParameterizedConstructor() {
    Usage usage = new Usage(100, 200, 300);
    
    assertEquals(100, usage.getPromptTokens());
    assertEquals(200, usage.getCompletionTokens());
    assertEquals(300, usage.getTotalTokens());
  }

  @Test
  void testSettersAndGetters() {
    Usage usage = new Usage();
    
    usage.setPromptTokens(50);
    usage.setCompletionTokens(150);
    usage.setTotalTokens(200);
    
    assertEquals(50, usage.getPromptTokens());
    assertEquals(150, usage.getCompletionTokens());
    assertEquals(200, usage.getTotalTokens());
  }

  @Test
  void testWithZeroValues() {
    Usage usage = new Usage(0, 0, 0);
    
    assertEquals(0, usage.getPromptTokens());
    assertEquals(0, usage.getCompletionTokens());
    assertEquals(0, usage.getTotalTokens());
  }

  @Test
  void testWithLargeValues() {
    Usage usage = new Usage(10000, 20000, 30000);
    
    assertEquals(10000, usage.getPromptTokens());
    assertEquals(20000, usage.getCompletionTokens());
    assertEquals(30000, usage.getTotalTokens());
  }

  @Test
  void testTokensSumCorrectly() {
    Usage usage = new Usage();
    usage.setPromptTokens(75);
    usage.setCompletionTokens(125);
    usage.setTotalTokens(200);
    
    assertEquals(200, usage.getPromptTokens() + usage.getCompletionTokens());
    assertEquals(200, usage.getTotalTokens());
  }
}
