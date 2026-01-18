package ca.letkeman.resumes;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global CORS configuration for Spring Boot application.
 * Allows requests from frontend running on localhost:3000, localhost:5173, and localhost:80.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

  /**
   * Configure CORS mappings to allow cross-origin requests from frontend origins.
   *
   * @param registry the CORS registry
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
        .allowedOrigins("http://localhost:3000", "http://localhost:5173", "http://localhost:80",
            "http://127.0.0.1:3000", "http://127.0.0.1:5173", "http://127.0.0.1:80")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
        .allowedHeaders("*")
        .allowCredentials(true)
        .maxAge(3600);
  }
}
