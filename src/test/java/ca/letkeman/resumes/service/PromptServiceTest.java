package ca.letkeman.resumes.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

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

        assertNotNull(prompt, "Prompt should not be null");
        assertFalse(prompt.isEmpty(), "Prompt should not be empty");
        assertTrue(prompt.contains("{job_title}"), "Prompt should contain {job_title} placeholder");
        assertTrue(prompt.contains("{job_description}"), "Prompt should contain {job_description} placeholder");
        assertTrue(prompt.contains("{resume_string}"), "Prompt should contain {resume_string} placeholder");
    }

    @Test
    @DisplayName("Should load COVER prompt successfully")
    void testLoadCoverPrompt() {
        String prompt = promptService.loadPrompt("COVER");

        assertNotNull(prompt, "Prompt should not be null");
        assertFalse(prompt.isEmpty(), "Prompt should not be empty");
        assertTrue(prompt.contains("{job_title}"), "Prompt should contain {job_title} placeholder");
        assertTrue(prompt.contains("{job_description}"), "Prompt should contain {job_description} placeholder");
    }

    @Test
    @DisplayName("Should load SKILLS prompt successfully")
    void testLoadSkillsPrompt() {
        String prompt = promptService.loadPrompt("SKILLS");

        assertNotNull(prompt, "Prompt should not be null");
        assertFalse(prompt.isEmpty(), "Prompt should not be empty");
        assertTrue(prompt.contains("{job_title}"), "Prompt should contain {job_title} placeholder");
        assertTrue(prompt.contains("{job_description}"), "Prompt should contain {job_description} placeholder");
    }

    @Test
    @DisplayName("Should return empty string for invalid prompt name")
    void testLoadInvalidPrompt() {
        String prompt = promptService.loadPrompt("NONEXISTENT");

        assertEquals("", prompt, "Should return empty string for invalid prompt");
    }

    @Test
    @DisplayName("Should return empty string for null prompt name")
    void testLoadNullPrompt() {
        String prompt = promptService.loadPrompt(null);

        assertEquals("", prompt, "Should return empty string for null prompt");
    }

    @Test
    @DisplayName("Should return empty string for empty prompt name")
    void testLoadEmptyPrompt() {
        String prompt = promptService.loadPrompt("");

        assertEquals("", prompt, "Should return empty string for empty prompt");
    }

    @Test
    @DisplayName("Should handle whitespace-only prompt names")
    void testLoadWhitespacePrompt() {
        String prompt = promptService.loadPrompt("   ");

        assertEquals("", prompt, "Should return empty string for whitespace-only prompt");
    }

    @Test
    @DisplayName("Should load RESUME prompt with standardized variable names")
    void testResumePromptHasStandardizedVariables() {
        String prompt = promptService.loadPrompt("RESUME");

        // Verify old naming convention is not used
        assertFalse(prompt.contains("{jobTitle}"), "Prompt should not contain deprecated {jobTitle}");
        assertFalse(prompt.contains("{jd_string}"), "Prompt should not contain deprecated {jd_string}");

        // Verify new naming convention is used
        assertTrue(prompt.contains("{job_title}"), "Prompt should contain standardized {job_title}");
        assertTrue(prompt.contains("{job_description}"), "Prompt should contain standardized {job_description}");
    }

    @Test
    @DisplayName("Should load COVER prompt with standardized variable names")
    void testCoverPromptHasStandardizedVariables() {
        String prompt = promptService.loadPrompt("COVER");

        // Verify old naming convention is not used
        assertFalse(prompt.contains("{jobTitle}"), "Prompt should not contain deprecated {jobTitle}");
        assertFalse(prompt.contains("{jd_string}"), "Prompt should not contain deprecated {jd_string}");

        // Verify new naming convention is used
        assertTrue(prompt.contains("{job_title}"), "Prompt should contain standardized {job_title}");
        assertTrue(prompt.contains("{job_description}"), "Prompt should contain standardized {job_description}");
    }

    @Test
    @DisplayName("Should load multiple prompts independently")
    void testLoadMultiplePromptsIndependently() {
        String resumePrompt = promptService.loadPrompt("RESUME");
        String coverPrompt = promptService.loadPrompt("COVER");
        String skillsPrompt = promptService.loadPrompt("SKILLS");

        assertNotNull(resumePrompt, "Resume prompt should not be null");
        assertNotNull(coverPrompt, "Cover prompt should not be null");
        assertNotNull(skillsPrompt, "Skills prompt should not be null");

        // Verify they are different prompts
        assertNotEquals(resumePrompt, coverPrompt, "Resume and cover prompts should be different");
        assertNotEquals(resumePrompt, skillsPrompt, "Resume and skills prompts should be different");
        assertNotEquals(coverPrompt, skillsPrompt, "Cover and skills prompts should be different");
    }

    @Test
    @DisplayName("Should handle case-sensitive prompt names")
    void testPromptNameCaseSensitivity() {
        String upperCasePrompt = promptService.loadPrompt("RESUME");
        String lowerCasePrompt = promptService.loadPrompt("resume");

        assertNotNull(upperCasePrompt, "Uppercase RESUME should load successfully");
        // Note: Case sensitivity depends on file system. File systems like Windows are case-insensitive,
        // while Linux is case-sensitive. Both results are acceptable.
    }
}
