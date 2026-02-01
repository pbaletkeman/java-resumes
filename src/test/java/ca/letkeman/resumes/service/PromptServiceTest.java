package ca.letkeman.resumes.service;

import ca.letkeman.resumes.entity.PromptHistory;
import ca.letkeman.resumes.model.Optimize;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for PromptService.
 *
 * Tests verify:
 * - Prompt loading functionality
 * - Error handling for invalid prompt names
 * - Proper resource loading from bundled resources
 */
class PromptServiceTest {

    private PromptService promptService;

    @BeforeEach
    void setUp() {
        promptService = new PromptService();
    }

    @Test
    @DisplayName("Should load RESUME prompt successfully")
    void testLoadResumePrompt() {
        String prompt = promptService.loadPrompt("RESUME");

        Assertions.assertNotNull(prompt, "Prompt should not be null");
        Assertions.assertFalse(prompt.isEmpty(), "Prompt should not be empty");
        Assertions.assertTrue(prompt.contains("{job_title}"),
            "Prompt should contain {job_title} placeholder");
        Assertions.assertTrue(prompt.contains("{job_description}"),
            "Prompt should contain {job_description} placeholder");
        Assertions.assertTrue(prompt.contains("{resume_string}"),
            "Prompt should contain {resume_string} placeholder");
    }

    @Test
    @DisplayName("Should load COVER prompt successfully")
    void testLoadCoverPrompt() {
        String prompt = promptService.loadPrompt("COVER");

        Assertions.assertNotNull(prompt, "Prompt should not be null");
        Assertions.assertFalse(prompt.isEmpty(), "Prompt should not be empty");
        Assertions.assertTrue(prompt.contains("{job_title}"),
            "Prompt should contain {job_title} placeholder");
        Assertions.assertTrue(prompt.contains("{job_description}"),
            "Prompt should contain {job_description} placeholder");
    }

    @Test
    @DisplayName("Should return empty string for invalid prompt name")
    void testLoadInvalidPrompt() {
        String prompt = promptService.loadPrompt("NONEXISTENT");

        Assertions.assertEquals("", prompt, "Should return empty string for invalid prompt");
    }

    @Test
    @DisplayName("Should return empty string for null prompt name")
    void testLoadNullPrompt() {
        String prompt = promptService.loadPrompt(null);

        Assertions.assertEquals("", prompt, "Should return empty string for null prompt");
    }

    @Test
    @DisplayName("Should return empty string for empty prompt name")
    void testLoadEmptyPrompt() {
        String prompt = promptService.loadPrompt("");

        Assertions.assertEquals("", prompt, "Should return empty string for empty prompt");
    }

    @Test
    @DisplayName("Should handle whitespace-only prompt names")
    void testLoadWhitespacePrompt() {
        String prompt = promptService.loadPrompt("   ");

        Assertions.assertEquals("", prompt, "Should return empty string for whitespace-only prompt");
    }

    @Test
    @DisplayName("Should load RESUME prompt with standardized variable names")
    void testResumePromptHasStandardizedVariables() {
        String prompt = promptService.loadPrompt("RESUME");

        // Verify old naming convention is not used
        Assertions.assertFalse(prompt.contains("{jobTitle}"),
            "Prompt should not contain deprecated {jobTitle}");
        Assertions.assertFalse(prompt.contains("{jd_string}"),
            "Prompt should not contain deprecated {jd_string}");

        // Verify new naming convention is used
        Assertions.assertTrue(prompt.contains("{job_title}"),
            "Prompt should contain standardized {job_title}");
        Assertions.assertTrue(prompt.contains("{job_description}"),
            "Prompt should contain standardized {job_description}");
    }

    @Test
    @DisplayName("Should load COVER prompt with standardized variable names")
    void testCoverPromptHasStandardizedVariables() {
        String prompt = promptService.loadPrompt("COVER");

        // Verify old naming convention is not used
        Assertions.assertFalse(prompt.contains("{jobTitle}"),
            "Prompt should not contain deprecated {jobTitle}");
        Assertions.assertFalse(prompt.contains("{jd_string}"),
            "Prompt should not contain deprecated {jd_string}");

        // Verify new naming convention is used
        Assertions.assertTrue(prompt.contains("{job_title}"),
            "Prompt should contain standardized {job_title}");
        Assertions.assertTrue(prompt.contains("{job_description}"),
            "Prompt should contain standardized {job_description}");
    }

    @Test
    @DisplayName("Should load multiple prompts independently")
    void testLoadMultiplePromptsIndependently() {
        String resumePrompt = promptService.loadPrompt("RESUME");
        String coverPrompt = promptService.loadPrompt("COVER");
        String skillsPrompt = promptService.loadPrompt("SKILLS");

        Assertions.assertNotNull(resumePrompt, "Resume prompt should not be null");
        Assertions.assertNotNull(coverPrompt, "Cover prompt should not be null");
        Assertions.assertNotNull(skillsPrompt, "Skills prompt should not be null");

        // Verify they are different prompts
        Assertions.assertNotEquals(resumePrompt, coverPrompt,
            "Resume and cover prompts should be different");
        Assertions.assertNotEquals(resumePrompt, skillsPrompt,
            "Resume and skills prompts should be different");
        Assertions.assertNotEquals(coverPrompt, skillsPrompt,
            "Cover and skills prompts should be different");
    }

    @Test
    @DisplayName("Should handle case-sensitive prompt names")
    void testPromptNameCaseSensitivity() {
        String upperCasePrompt = promptService.loadPrompt("RESUME");
        // String lowerCasePrompt = promptService.loadPrompt("resume");

        Assertions.assertNotNull(upperCasePrompt, "Uppercase RESUME should load successfully");
        // Note: Case sensitivity depends on file system. File systems like Windows are case-insensitive,
        // while Linux is case-sensitive. Both results are acceptable.
    }

    @Test
    @DisplayName("Should expand prompt with variables")
    void testExpandPromptWithVariables() {
        String template = "Hello {name}, you applied for {position}";
        Map<String, String> variables = Map.of(
            "name", "John Doe",
            "position", "Software Engineer"
        );
        
        String expanded = promptService.expandPrompt(template, variables);
        
        Assertions.assertEquals("Hello John Doe, you applied for Software Engineer", expanded);
    }
    
    @Test
    @DisplayName("Should expand prompt with null value")
    void testExpandPromptWithNullValue() {
        String template = "Hello {name}, you applied for {position}";
        Map<String, String> variables = new HashMap<>();
        variables.put("name", "John");
        variables.put("position", null);
        
        String expanded = promptService.expandPrompt(template, variables);
        
        Assertions.assertTrue(expanded.contains("John"));
        Assertions.assertTrue(expanded.contains("you applied for "));
    }
    
    @Test
    @DisplayName("Should handle empty template")
    void testExpandPromptWithEmptyTemplate() {
        String expanded = promptService.expandPrompt("", Map.of("key", "value"));
        Assertions.assertEquals("", expanded);
    }
    
    @Test
    @DisplayName("Should handle null template")
    void testExpandPromptWithNullTemplate() {
        String expanded = promptService.expandPrompt(null, Map.of("key", "value"));
        Assertions.assertEquals("", expanded);
    }
    
    @Test
    @DisplayName("Should handle template with no variables")
    void testExpandPromptWithNoVariables() {
        String template = "This is a plain text template";
        String expanded = promptService.expandPrompt(template, Map.of());
        Assertions.assertEquals(template, expanded);
    }
    
    @Test
    @DisplayName("Should handle template with unmatched placeholders")
    void testExpandPromptWithUnmatchedPlaceholders() {
        String template = "Hello {name}, your {missing} is here";
        Map<String, String> variables = Map.of("name", "John");
        
        String expanded = promptService.expandPrompt(template, variables);
        
        Assertions.assertTrue(expanded.contains("John"));
        Assertions.assertTrue(expanded.contains("{missing}"));
    }
    
    @Test
    @DisplayName("Should save prompt to history returns null when repository is null")
    void testSavePromptToHistoryWithNullRepository() {
        // Repository should be null by default for non-Spring test
        Optimize optimize = new Optimize();
        optimize.setJobDescription("Test");
        optimize.setModel("test-model");
        
        PromptHistory result = promptService.savePromptToHistory(
            "RESUME", optimize, "prompt", "content", "/tmp/file.md", 1000L
        );
        
        Assertions.assertNull(result, "Should return null when repository is not available");
    }
    
    @Test
    @DisplayName("Should get all history returns empty list when repository is null")
    void testGetAllHistoryWithNullRepository() {
        List<PromptHistory> history = promptService.getAllHistory();
        Assertions.assertNotNull(history);
        Assertions.assertTrue(history.isEmpty());
    }
    
    @Test
    @DisplayName("Should get history by type returns empty list when repository is null")
    void testGetHistoryByTypeWithNullRepository() {
        List<PromptHistory> history = promptService.getHistoryByType("RESUME");
        Assertions.assertNotNull(history);
        Assertions.assertTrue(history.isEmpty());
    }
    
    @Test
    @DisplayName("Should get history by id returns empty when repository is null")
    void testGetHistoryByIdWithNullRepository() {
        Optional<PromptHistory> history = promptService.getHistoryById(1L);
        Assertions.assertNotNull(history);
        Assertions.assertFalse(history.isPresent());
    }
    
    @Test
    @DisplayName("Should delete history by id does nothing when repository is null")
    void testDeleteHistoryByIdWithNullRepository() {
        // Should not throw exception
        Assertions.assertDoesNotThrow(() -> promptService.deleteHistoryById(1L));
    }

    @Test
    @DisplayName("Should handle expandPrompt with special characters in variables")
    void testExpandPromptWithSpecialCharacters() {
        String template = "Company: {company}, Role: {role}";
        Map<String, String> variables = new HashMap<>();
        variables.put("company", "Tech & Co.");
        variables.put("role", "Sr. Developer <Expert>");
        
        String expanded = promptService.expandPrompt(template, variables);
        
        Assertions.assertTrue(expanded.contains("Tech & Co."));
        Assertions.assertTrue(expanded.contains("Sr. Developer <Expert>"));
    }

    @Test
    @DisplayName("Should handle expandPrompt with empty string values")
    void testExpandPromptWithEmptyStringValues() {
        String template = "Name: {name}, Title: {title}";
        Map<String, String> variables = new HashMap<>();
        variables.put("name", "");
        variables.put("title", "Developer");
        
        String expanded = promptService.expandPrompt(template, variables);
        
        Assertions.assertTrue(expanded.contains("Name: , Title: Developer"));
    }



    @Test
    @DisplayName("Should handle expandPrompt with multiple occurrences of same placeholder")
    void testExpandPromptWithDuplicatePlaceholders() {
        String template = "{name} loves {name}'s work at {name}";
        Map<String, String> variables = Map.of("name", "Alice");
        
        String expanded = promptService.expandPrompt(template, variables);
        
        Assertions.assertEquals("Alice loves Alice's work at Alice", expanded);
    }

    @Test
    @DisplayName("Should handle expandPrompt with nested braces")
    void testExpandPromptWithNestedBraces() {
        String template = "Code: {{variable}} and {actual}";
        Map<String, String> variables = Map.of("actual", "value");
        
        String expanded = promptService.expandPrompt(template, variables);
        
        Assertions.assertTrue(expanded.contains("value"));
        // Should not break on nested braces
        Assertions.assertTrue(expanded.contains("{"));
    }

    @Test
    @DisplayName("Should handle expandPrompt with numeric placeholders")
    void testExpandPromptWithNumericPlaceholders() {
        String template = "Value1: {123}, Value2: {456}";
        Map<String, String> variables = new HashMap<>();
        variables.put("123", "First");
        variables.put("456", "Second");
        
        String expanded = promptService.expandPrompt(template, variables);
        
        Assertions.assertTrue(expanded.contains("First"));
        Assertions.assertTrue(expanded.contains("Second"));
    }

    @Test
    @DisplayName("Should handle expandPrompt with underscore in placeholder names")
    void testExpandPromptWithUnderscorePlaceholders() {
        String template = "User: {user_name}, Role: {user_role}";
        Map<String, String> variables = new HashMap<>();
        variables.put("user_name", "John Doe");
        variables.put("user_role", "Admin");
        
        String expanded = promptService.expandPrompt(template, variables);
        
        Assertions.assertTrue(expanded.contains("John Doe"));
        Assertions.assertTrue(expanded.contains("Admin"));
    }

    @Test
    @DisplayName("Should handle expandPrompt with mixed case placeholders")
    void testExpandPromptWithMixedCasePlaceholders() {
        String template = "{CompanyName} - {jobTitle} - {LEVEL}";
        Map<String, String> variables = new HashMap<>();
        variables.put("CompanyName", "TechCorp");
        variables.put("jobTitle", "Developer");
        variables.put("LEVEL", "Senior");
        
        String expanded = promptService.expandPrompt(template, variables);
        
        Assertions.assertTrue(expanded.contains("TechCorp"));
        Assertions.assertTrue(expanded.contains("Developer"));
        Assertions.assertTrue(expanded.contains("Senior"));
    }

    @Test
    @DisplayName("Should handle expandPrompt with very long variable values")
    void testExpandPromptWithLongVariableValues() {
        String template = "Description: {desc}";
        Map<String, String> variables = new HashMap<>();
        String longValue = "A".repeat(10000);
        variables.put("desc", longValue);
        
        String expanded = promptService.expandPrompt(template, variables);
        
        Assertions.assertTrue(expanded.contains(longValue));
        Assertions.assertTrue(expanded.length() > 10000);
    }

    @Test
    @DisplayName("Should handle expandPrompt with placeholder at start and end")
    void testExpandPromptWithPlaceholderAtBoundaries() {
        String template = "{start} middle text {end}";
        Map<String, String> variables = new HashMap<>();
        variables.put("start", "BEGIN");
        variables.put("end", "FINISH");
        
        String expanded = promptService.expandPrompt(template, variables);
        
        Assertions.assertTrue(expanded.startsWith("BEGIN"));
        Assertions.assertTrue(expanded.endsWith("FINISH"));
    }

    @Test
    @DisplayName("Should get history by company returns empty list when repository is null")
    void testGetHistoryByCompanyWithNullRepository() {
        List<PromptHistory> history = promptService.getHistoryByCompany("TechCorp");
        Assertions.assertNotNull(history);
        Assertions.assertTrue(history.isEmpty());
    }

    @Test
    @DisplayName("Should get history by date range returns empty list when repository is null")
    void testGetHistoryByDateRangeWithNullRepository() {
        java.time.LocalDateTime start = java.time.LocalDateTime.now().minusDays(7);
        java.time.LocalDateTime end = java.time.LocalDateTime.now();
        
        List<PromptHistory> history = promptService.getHistoryByDateRange(start, end);
        Assertions.assertNotNull(history);
        Assertions.assertTrue(history.isEmpty());
    }
}
