package ca.letkeman.resumes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class RestServiceApplicationTest {

  @Autowired
  private ApplicationContext applicationContext;

  @Test
  void contextLoads() {
    Assertions.assertNotNull(applicationContext, "Application context should not be null");
  }

  @Test
  void testApplicationHasRequiredBeans() {
    Assertions.assertTrue(applicationContext.containsBean("webConfig"),
        "WebConfig bean should be present");
  }

  @Test
  void testApplicationStartsSuccessfully() {
    // If we reach this point, the application started successfully
    Assertions.assertNotNull(applicationContext);
    Assertions.assertTrue(applicationContext.getBeanDefinitionCount() > 0,
        "Application context should have beans");
  }

  @Test
  void testStorageServiceBeanExists() {
    Assertions.assertTrue(applicationContext.containsBean("filesStorageServiceImpl"),
        "FilesStorageService bean should be present");
  }

  @Test
  void testResumeControllerBeanExists() {
    Assertions.assertTrue(applicationContext.containsBean("resumeController"),
        "ResumeController bean should be present");
  }

  @Test
  void testSpringBootApplicationAnnotationPresent() {
    Assertions.assertTrue(
        RestServiceApplication.class.isAnnotationPresent(SpringBootApplication.class),
        "RestServiceApplication should have @SpringBootApplication annotation");
  }

  @Test
  void testMainMethodExists() {
    try {
      RestServiceApplication.class.getMethod("main", String[].class);
    } catch (NoSuchMethodException e) {
      Assertions.fail("main method should exist");
    }
  }
}
