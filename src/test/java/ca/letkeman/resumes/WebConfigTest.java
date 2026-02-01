package ca.letkeman.resumes;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * Test class for WebConfig.
 */
class WebConfigTest {

  @Test
  void testWebConfigCreation() {
    WebConfig webConfig = new WebConfig();
    assertNotNull(webConfig, "WebConfig should be created successfully");
  }

  @Test
  void testAddCorsMappings() {
    WebConfig webConfig = new WebConfig();
    CorsRegistry registry = mock(CorsRegistry.class);
    CorsRegistration registration = mock(CorsRegistration.class);

    when(registry.addMapping(anyString())).thenReturn(registration);
    when(registration.allowedOrigins(anyString(), anyString(), anyString(), 
        anyString(), anyString(), anyString())).thenReturn(registration);
    when(registration.allowedMethods(anyString(), anyString(), anyString(), 
        anyString(), anyString(), anyString())).thenReturn(registration);
    when(registration.allowedHeaders(anyString())).thenReturn(registration);
    when(registration.allowCredentials(true)).thenReturn(registration);
    when(registration.maxAge(3600L)).thenReturn(registration);

    webConfig.addCorsMappings(registry);

    verify(registry).addMapping("/api/**");
  }

  @Test
  void testCorsAllowsMultipleOrigins() {
    WebConfig webConfig = new WebConfig();
    CorsRegistry registry = mock(CorsRegistry.class);
    CorsRegistration registration = mock(CorsRegistration.class);

    when(registry.addMapping(anyString())).thenReturn(registration);
    when(registration.allowedOrigins(anyString(), anyString(), anyString(), 
        anyString(), anyString(), anyString())).thenReturn(registration);
    when(registration.allowedMethods(anyString(), anyString(), anyString(), 
        anyString(), anyString(), anyString())).thenReturn(registration);
    when(registration.allowedHeaders(anyString())).thenReturn(registration);
    when(registration.allowCredentials(true)).thenReturn(registration);
    when(registration.maxAge(3600L)).thenReturn(registration);

    webConfig.addCorsMappings(registry);

    verify(registration).allowedOrigins(anyString(), anyString(), anyString(), 
        anyString(), anyString(), anyString());
  }

  @Test
  void testCorsAllowsAllHttpMethods() {
    WebConfig webConfig = new WebConfig();
    CorsRegistry registry = mock(CorsRegistry.class);
    CorsRegistration registration = mock(CorsRegistration.class);

    when(registry.addMapping(anyString())).thenReturn(registration);
    when(registration.allowedOrigins(anyString(), anyString(), anyString(), 
        anyString(), anyString(), anyString())).thenReturn(registration);
    when(registration.allowedMethods(anyString(), anyString(), anyString(), 
        anyString(), anyString(), anyString())).thenReturn(registration);
    when(registration.allowedHeaders(anyString())).thenReturn(registration);
    when(registration.allowCredentials(true)).thenReturn(registration);
    when(registration.maxAge(3600L)).thenReturn(registration);

    webConfig.addCorsMappings(registry);

    verify(registration).allowedMethods(anyString(), anyString(), anyString(), 
        anyString(), anyString(), anyString());
  }

  @Test
  void testCorsMaxAge() {
    WebConfig webConfig = new WebConfig();
    CorsRegistry registry = mock(CorsRegistry.class);
    CorsRegistration registration = mock(CorsRegistration.class);

    when(registry.addMapping(anyString())).thenReturn(registration);
    when(registration.allowedOrigins(anyString(), anyString(), anyString(), 
        anyString(), anyString(), anyString())).thenReturn(registration);
    when(registration.allowedMethods(anyString(), anyString(), anyString(), 
        anyString(), anyString(), anyString())).thenReturn(registration);
    when(registration.allowedHeaders(anyString())).thenReturn(registration);
    when(registration.allowCredentials(true)).thenReturn(registration);
    when(registration.maxAge(3600L)).thenReturn(registration);

    webConfig.addCorsMappings(registry);

    verify(registration).maxAge(3600L);
  }

  @Test
  void testCorsAllowsCredentials() {
    WebConfig webConfig = new WebConfig();
    CorsRegistry registry = mock(CorsRegistry.class);
    CorsRegistration registration = mock(CorsRegistration.class);

    when(registry.addMapping(anyString())).thenReturn(registration);
    when(registration.allowedOrigins(anyString(), anyString(), anyString(), 
        anyString(), anyString(), anyString())).thenReturn(registration);
    when(registration.allowedMethods(anyString(), anyString(), anyString(), 
        anyString(), anyString(), anyString())).thenReturn(registration);
    when(registration.allowedHeaders(anyString())).thenReturn(registration);
    when(registration.allowCredentials(true)).thenReturn(registration);
    when(registration.maxAge(3600L)).thenReturn(registration);

    webConfig.addCorsMappings(registry);

    verify(registration).allowCredentials(true);
  }
}
