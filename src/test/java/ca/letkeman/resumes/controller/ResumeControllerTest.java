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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class ResumeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws IOException {
        // Create the uploads directory if it doesn't exist
        Path uploadsPath = Paths.get("uploads");
        if (!Files.exists(uploadsPath)) {
            Files.createDirectories(uploadsPath);
        }
        // Create a dummy resume.pdf file for delete test
        Path resumePath = uploadsPath.resolve("resume.pdf");
        Files.write(resumePath, "dummy resume content".getBytes());
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up test files after each test
        Path uploadsPath = Paths.get("uploads");
        if (Files.exists(uploadsPath)) {
            Files.walk(uploadsPath)
                .sorted(java.util.Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        // Ignore deletion errors
                    }
                });
        }
    }

    @Test
    void testSuccessfulMarkdownToPdfConversion() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.md", "text/markdown", "# Test Markdown".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/markdownFile2PDF").file(file))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("file successfully converted"));
    }

    @Test
    void testSuccessfulMarkdownToDocxConversion() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.md", "text/markdown", "# Test Markdown\n\n## Section\n\nContent here".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/markdownFile2DOCX").file(file))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("file successfully converted"));
    }

    @Test
    void testUnsuccessfulMarkdownToDocxConversion() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "bad.md", "text/markdown", new byte[0]);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/markdownFile2DOCX").file(file))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No file/invalid file provided"));
    }

    @Test
    void testHandlesNullFileParameterForDocx() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/markdownFile2DOCX"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No file/invalid file provided"));
    }

    @Test
    void testInvalidOptimize() throws Exception {
        MockMultipartFile resume = new MockMultipartFile(
            "resume", "resume.pdf", "application/pdf", "dummy resume".getBytes());
        MockMultipartFile job = new MockMultipartFile(
            "job", "jd.pdf", "application/pdf", "dummy job".getBytes());
        String optimizeJson = "{\"resume\":\"\",\"jobDescription\":\"\"}";
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/upload")
                .file(resume)
                .file(job)
                .param("optimize", optimizeJson))
            .andExpect(MockMvcResultMatchers.status().isExpectationFailed())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                .value("Required property missing or invalid."));
    }

    @Test
    void testOptimizeResumeWithValidFiles() throws Exception {
        MockMultipartFile resume = new MockMultipartFile(
                "resume", "resume.pdf", "application/pdf", "dummy resume".getBytes());
        MockMultipartFile job = new MockMultipartFile(
                "job", "jd.pdf", "application/pdf", "dummy job".getBytes());
        String optimizeJson = "{\"company\":\"company\",\"jobTitle\":\"title\",\"model\":\"model\","
            + "\"temperature\":0.01,\"promptType\":[\"Resume\"],\"jobDescription\":\"\",\"resume\":\"\"}";

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/upload")
                .file(resume)
                .file(job)
                .param("optimize", optimizeJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("generating"));
    }

    @Test
    void testGetListFilesWithFiles() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/files"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void testOptimizeResumeWithInvalidOptimize() throws Exception {
        MockMultipartFile resume = new MockMultipartFile(
                "resume", "resume.pdf", "application/pdf", "dummy resume".getBytes());
        MockMultipartFile job = new MockMultipartFile(
                "job", "jd.pdf", "application/pdf", "dummy job".getBytes());
        String invalidOptimize = "{invalid}";
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/upload")
                .file(resume)
                .file(job)
                .param("optimize", invalidOptimize))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                    .value("invalid optimize parameter"));
    }

    @Test
    void testGetListFilesEmpty() throws Exception {
        // This test assumes the storage is empty; may need to clear storage before running
        mockMvc.perform(MockMvcRequestBuilders.get("/api/files"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void testGetFile() throws Exception {
        // Test requesting a non-existent file; should return 404
        mockMvc.perform(MockMvcRequestBuilders.get("/api/files/nonexistent.md"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testDeleteFileSuccess() throws Exception {
        // This test assumes a file named "resume.pdf" exists in storage
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/files/resume.pdf"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                    .value(org.hamcrest.Matchers.containsString("Delete the file successfully")));
    }

    @Test
    void testDeleteFileNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/files/nonexistent.pdf"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                    .value("The file does not exist!"));
    }

    @Test
    void testMarkdownFile2PdfWithNullFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/markdownFile2PDF"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No file/invalid file provided"));
    }

    @Test
    void testMarkdownFile2DocxWithEmptyFile() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.md", "text/markdown", new byte[0]);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/markdownFile2DOCX").file(emptyFile))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No file/invalid file provided"));
    }

    @Test
    void testOptimizeResumeWithBlankOptimize() throws Exception {
        MockMultipartFile resume = new MockMultipartFile(
                "resume", "resume.pdf", "application/pdf", "dummy resume content".getBytes());
        MockMultipartFile job = new MockMultipartFile(
                "job", "job.pdf", "application/pdf", "job description".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/upload")
                .file(resume)
                .file(job)
                .param("optimize", "   "))
                .andExpect(MockMvcResultMatchers.status().isExpectationFailed());
    }

    @Test
    void testOptimizeResumeWithNullOptimize() throws Exception {
        MockMultipartFile resume = new MockMultipartFile(
                "resume", "resume.pdf", "application/pdf", "dummy resume content".getBytes());
        MockMultipartFile job = new MockMultipartFile(
                "job", "job.pdf", "application/pdf", "job description".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/upload")
                .file(resume)
                .file(job))
                .andExpect(MockMvcResultMatchers.status().isExpectationFailed());
    }

    @Test
    void testProcessCoverLetterEndpoint() throws Exception {
        MockMultipartFile coverLetter = new MockMultipartFile(
                "coverLetter", "cover.txt", "text/plain", "cover letter content here".getBytes());
        MockMultipartFile job = new MockMultipartFile(
                "job", "job.txt", "text/plain", "job description here".getBytes());
        String optimizeJson = "{\"company\":\"TechCorp\",\"jobTitle\":\"Engineer\",\"model\":\"gpt-4\","
            + "\"temperature\":0.7,\"promptType\":[\"cover\"]}";

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/process/cover-letter")
                .file(coverLetter)
                .file(job)
                .param("optimize", optimizeJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("generating"));
    }

    @Test
    void testProcessResumeEndpoint() throws Exception {
        MockMultipartFile resume = new MockMultipartFile(
                "resume", "resume.txt", "text/plain", "resume content".getBytes());
        MockMultipartFile job = new MockMultipartFile(
                "job", "job.txt", "text/plain", "job description".getBytes());
        String optimizeJson = "{\"company\":\"TechCorp\",\"jobTitle\":\"Engineer\",\"model\":\"model\","
            + "\"temperature\":0.7,\"promptType\":[\"resume\"]}";

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/process/resume")
                .file(resume)
                .file(job)
                .param("optimize", optimizeJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("generating"));
    }

    @Test
    void testOptimizeResumeWithOnlyResumeFile() throws Exception {
        MockMultipartFile resume = new MockMultipartFile(
                "resume", "resume.pdf", "application/pdf", "resume content".getBytes());
        String optimizeJson = "{\"company\":\"TechCorp\",\"jobTitle\":\"Engineer\",\"model\":\"model\","
            + "\"temperature\":0.7,\"promptType\":[\"Resume\"],\"jobDescription\":\"job desc\",\"resume\":\"\"}";

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/upload")
                .file(resume)
                .param("optimize", optimizeJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("generating"));
    }

    @Test
    void testOptimizeResumeWithPrePopulatedResumeInOptimize() throws Exception {
        String optimizeJson = "{\"company\":\"TechCorp\",\"jobTitle\":\"Engineer\",\"model\":\"model\","
            + "\"temperature\":0.7,\"promptType\":[\"Resume\"],\"jobDescription\":\"job description text\","
            + "\"resume\":\"resume content text\"}";

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/upload")
                .param("optimize", optimizeJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("generating"));
    }

    @Test
    void testHealthCheck() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/health"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("UP"));
    }

    @Test
    void testProcessSkillsWithValidJob() throws Exception {
        MockMultipartFile job = new MockMultipartFile(
                "job", "jd.txt", "text/plain", "Java Spring Developer position".getBytes());
        String optimizeJson = "{\"company\":\"TechCorp\",\"jobTitle\":\"Java Developer\","
                + "\"model\":\"mistral\",\"temperature\":0.15,\"promptType\":[\"SKILLS\"],"
                + "\"jobDescription\":\"\",\"resume\":\"\"}";

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/process/skills")
                .file(job)
                .param("optimize", optimizeJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                    .value("Skills suggestion generation started"));
    }

    @Test
    void testProcessSkillsWithInvalidOptimize() throws Exception {
        MockMultipartFile job = new MockMultipartFile(
                "job", "jd.txt", "text/plain", "Java Developer position".getBytes());
        String invalidOptimize = "{invalid json}";

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/process/skills")
                .file(job)
                .param("optimize", invalidOptimize))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                    .value("invalid optimize parameter"));
    }

    @Test
    void testProcessSkillsWithoutJobDescription() throws Exception {
        String optimizeJson = "{\"company\":\"TechCorp\",\"jobTitle\":\"Java Developer\","
                + "\"model\":\"mistral\",\"temperature\":0.15,\"promptType\":[\"SKILLS\"],"
                + "\"jobDescription\":\"\",\"resume\":\"\"}";

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/process/skills")
                .param("optimize", optimizeJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                    .value("job description is required"));
    }

    @Test
    void testHealthCheckEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/health"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("UP"));
    }

    @Test
    void testDeleteNonExistentFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/files/nonexistent.txt"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testGetListFilesWhenEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/files"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testMarkdownFile2PDFWithSpecialCharacters() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test-file.md", "text/markdown",
                "# Test with special chars: !@#$%^&*()".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/markdownFile2PDF")
                .file(file))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testMarkdownFile2DOCXWithValidFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.md", "text/markdown",
                "# Markdown Content\nParagraph text here.".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/markdownFile2DOCX")
                .file(file))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetFileWithValidFilename() throws Exception {
        // First create a file
        Path uploadsPath = Paths.get("uploads");
        Path testFile = uploadsPath.resolve("test-file.txt");
        Files.write(testFile, "test content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/files/test-file.txt"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testProcessResumeWithInvalidOptimizeJson() throws Exception {
        MockMultipartFile resume = new MockMultipartFile(
                "resume", "resume.pdf", "application/pdf", "Resume Content".getBytes());
        MockMultipartFile job = new MockMultipartFile(
                "job", "jd.txt", "text/plain", "Java Developer position".getBytes());
        String invalidOptimize = "{invalid json}";

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/process/resume")
                .file(resume)
                .file(job)
                .param("optimize", invalidOptimize))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testGetListFilesWithSorting() throws Exception {
        // Create some test files with different dates
        Path uploadsPath = Paths.get("uploads");
        Files.write(uploadsPath.resolve("file1.md"), "content1".getBytes());
        Thread.sleep(10); // Small delay to ensure different timestamps
        Files.write(uploadsPath.resolve("file2.md"), "content2".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/files"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    void testMarkdownFile2PDFWithInvalidContent() throws Exception {
        // Create a file with minimal content
        MockMultipartFile invalidFile = new MockMultipartFile(
                "file", "invalid.md", "text/markdown", "# Test".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/markdownFile2PDF")
                .file(invalidFile))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testMarkdownFile2DOCXWithInvalidContent() throws Exception {
        // Create a file with minimal content
        MockMultipartFile invalidFile = new MockMultipartFile(
                "file", "invalid.md", "text/markdown", "# Test".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/markdownFile2DOCX")
                .file(invalidFile))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testProcessCoverLetterWithInvalidOptimizeJson() throws Exception {
        MockMultipartFile coverLetter = new MockMultipartFile(
                "coverLetter", "cover.md", "text/markdown", "Cover Letter Content".getBytes());
        MockMultipartFile job = new MockMultipartFile(
                "job", "jd.txt", "text/plain", "Job Description".getBytes());
        String invalidOptimize = "{invalid}";

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/process/cover-letter")
                .file(coverLetter)
                .file(job)
                .param("optimize", invalidOptimize))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
