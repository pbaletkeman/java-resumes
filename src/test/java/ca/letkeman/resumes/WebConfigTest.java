package ca.letkeman.resumes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * Test class for WebConfig.
 */
class WebConfigTest {

  @Test
  void testWebConfigCreation() {
    WebConfig webConfig = new WebConfig();
    Assertions.assertNotNull(webConfig, "WebConfig should be created successfully");
  }

  @Test
  void testAddCorsMappings() {
    WebConfig webConfig = new WebConfig();
    CorsRegistry registry = Mockito.mock(CorsRegistry.class);
    CorsRegistration registration = Mockito.mock(CorsRegistration.class);

    Mockito.when(registry.addMapping(ArgumentMatchers.anyString())).thenReturn(registration);
    Mockito.when(registration.allowedOrigins(
        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
        .thenReturn(registration);
    Mockito.when(registration.allowedMethods(
        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
        .thenReturn(registration);
    Mockito.when(registration.allowedHeaders(ArgumentMatchers.anyString())).thenReturn(registration);
    Mockito.when(registration.allowCredentials(true)).thenReturn(registration);
    Mockito.when(registration.maxAge(3600L)).thenReturn(registration);

    webConfig.addCorsMappings(registry);

    Mockito.verify(registry).addMapping("/api/**");
  }

  @Test
  void testCorsAllowsMultipleOrigins() {
    WebConfig webConfig = new WebConfig();
    CorsRegistry registry = Mockito.mock(CorsRegistry.class);
    CorsRegistration registration = Mockito.mock(CorsRegistration.class);

    Mockito.when(registry.addMapping(ArgumentMatchers.anyString())).thenReturn(registration);
    Mockito.when(registration.allowedOrigins(
        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
        .thenReturn(registration);
    Mockito.when(registration.allowedMethods(
        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
        .thenReturn(registration);
    Mockito.when(registration.allowedHeaders(ArgumentMatchers.anyString())).thenReturn(registration);
    Mockito.when(registration.allowCredentials(true)).thenReturn(registration);
    Mockito.when(registration.maxAge(3600L)).thenReturn(registration);

    webConfig.addCorsMappings(registry);

    Mockito.verify(registration).allowedOrigins(
        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
  }

  @Test
  void testCorsAllowsAllHttpMethods() {
    WebConfig webConfig = new WebConfig();
    CorsRegistry registry = Mockito.mock(CorsRegistry.class);
    CorsRegistration registration = Mockito.mock(CorsRegistration.class);

    Mockito.when(registry.addMapping(ArgumentMatchers.anyString())).thenReturn(registration);
    Mockito.when(registration.allowedOrigins(
        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
        .thenReturn(registration);
    Mockito.when(registration.allowedMethods(
        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
        .thenReturn(registration);
    Mockito.when(registration.allowedHeaders(ArgumentMatchers.anyString())).thenReturn(registration);
    Mockito.when(registration.allowCredentials(true)).thenReturn(registration);
    Mockito.when(registration.maxAge(3600L)).thenReturn(registration);

    webConfig.addCorsMappings(registry);

    Mockito.verify(registration).allowedMethods(
        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
  }

  @Test
  void testCorsMaxAge() {
    WebConfig webConfig = new WebConfig();
    CorsRegistry registry = Mockito.mock(CorsRegistry.class);
    CorsRegistration registration = Mockito.mock(CorsRegistration.class);

    Mockito.when(registry.addMapping(ArgumentMatchers.anyString())).thenReturn(registration);
    Mockito.when(registration.allowedOrigins(
        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
        .thenReturn(registration);
    Mockito.when(registration.allowedMethods(
        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
        .thenReturn(registration);
    Mockito.when(registration.allowedHeaders(ArgumentMatchers.anyString())).thenReturn(registration);
    Mockito.when(registration.allowCredentials(true)).thenReturn(registration);
    Mockito.when(registration.maxAge(3600L)).thenReturn(registration);

    webConfig.addCorsMappings(registry);

    Mockito.verify(registration).maxAge(3600L);
  }

  @Test
  void testCorsAllowsCredentials() {
    WebConfig webConfig = new WebConfig();
    CorsRegistry registry = Mockito.mock(CorsRegistry.class);
    CorsRegistration registration = Mockito.mock(CorsRegistration.class);

    Mockito.when(registry.addMapping(ArgumentMatchers.anyString())).thenReturn(registration);
    Mockito.when(registration.allowedOrigins(
        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
        .thenReturn(registration);
    Mockito.when(registration.allowedMethods(
        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
        ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
        .thenReturn(registration);
    Mockito.when(registration.allowedHeaders(ArgumentMatchers.anyString())).thenReturn(registration);
    Mockito.when(registration.allowCredentials(true)).thenReturn(registration);
    Mockito.when(registration.maxAge(3600L)).thenReturn(registration);

    webConfig.addCorsMappings(registry);

    Mockito.verify(registration).allowCredentials(true);
  }
}
