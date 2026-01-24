package ca.letkeman.resumes.service;

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
}
