package ca.letkeman.resumes.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Advanced test cases for ResumeController to improve coverage.
 * Tests focus on interview/networking endpoints and error scenarios.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ResumeControllerAdvancedTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws IOException {
        Path uploadsPath = Paths.get("uploads");
        if (!Files.exists(uploadsPath)) {
            Files.createDirectories(uploadsPath);
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        Path uploadsPath = Paths.get("uploads");
        if (Files.exists(uploadsPath)) {
            Files.walk(uploadsPath)
                .sorted(java.util.Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        // Ignore
                    }
                });
        }
    }

    // Interview HR Questions endpoint tests
    @Test
    void testGenerateInterviewHRQuestionsWithInvalidJson() throws Exception {
        String optimizeJson = "{invalid json}";
        
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/generate/interview-hr-questions")
                .param("optimize", optimizeJson))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("invalid optimize parameter"));
    }

    @Test
    void testGenerateInterviewHRQuestionsWithMissingJobDescription() throws Exception {
        String optimizeJson = "{\"company\":\"TechCorp\"}";
        
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/generate/interview-hr-questions")
                .param("optimize", optimizeJson))
            .andExpect(MockMvcResultMatchers.status().isExpectationFailed())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required property missing or invalid."));
    }

    // Interview Job-Specific Questions endpoint tests
    @Test
    void testGenerateInterviewJobSpecificQuestionsWithInvalidJson() throws Exception {
        String optimizeJson = "not a json";
        
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/generate/interview-job-specific")
                .param("optimize", optimizeJson))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("invalid optimize parameter"));
    }

    // Cold Email endpoint tests
    @Test
    void testGenerateColdEmailWithInvalidJson() throws Exception {
        String optimizeJson = "{{{";
        
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/generate/cold-email")
                .param("optimize", optimizeJson))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("invalid optimize parameter"));
    }

    @Test
    void testGenerateColdEmailWithMissingRequired() throws Exception {
        String optimizeJson = "{\"company\":\"TestCo\"}";
        
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/generate/cold-email")
                .param("optimize", optimizeJson))
            .andExpect(MockMvcResultMatchers.status().isExpectationFailed())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required property missing or invalid."));
    }

    // Cold LinkedIn Message endpoint tests
    @Test
    void testGenerateColdLinkedInMessageWithInvalidJson() throws Exception {
        String optimizeJson = "}invalid{";
        
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/generate/cold-linkedin-message")
                .param("optimize", optimizeJson))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("invalid optimize parameter"));
    }

    // Thank You Email endpoint tests
    @Test
    void testGenerateThankYouEmailWithInvalidJson() throws Exception {
        String optimizeJson = "{bad json";
        
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/generate/thank-you-email")
                .param("optimize", optimizeJson))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("invalid optimize parameter"));
    }

    // Error handling tests for file operations
    @Test
    void testProcessSkillsEndpointWithValidInput() throws Exception {
        String optimizeJson = "{\"jobDescription\":\"Test Job Description\"}";
        
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/process/skills")
                .param("optimize", optimizeJson))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Skills suggestion generation started"));
    }

    @Test
    void testProcessSkillsWithInvalidJson() throws Exception {
        String optimizeJson = "invalid";
        
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/process/skills")
                .param("optimize", optimizeJson))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("invalid optimize parameter"));
    }

    @Test
    void testProcessSkillsWithoutJobDescription() throws Exception {
        String optimizeJson = "{\"company\":\"TestCo\"}";
        
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/process/skills")
                .param("optimize", optimizeJson))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("job description is required"));
    }
}
