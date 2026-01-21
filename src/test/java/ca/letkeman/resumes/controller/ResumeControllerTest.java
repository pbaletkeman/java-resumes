package ca.letkeman.resumes.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ResumeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test_successful_markdown_to_pdf_conversion() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.md", "text/markdown", "# Test Markdown".getBytes());
        mockMvc.perform(multipart("/api/markdownFile2PDF").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("file successfully converted"));
    }

    @Test
    void test_successful_markdown_to_docx_conversion() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.md", "text/markdown", "# Test Markdown\n\n## Section\n\nContent here".getBytes());
        mockMvc.perform(multipart("/api/markdownFile2DOCX").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("file successfully converted"));
    }

    @Test
    void test_unsuccessful_markdown_to_docx_conversion() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "bad.md", "text/markdown", new byte[0]);
        mockMvc.perform(multipart("/api/markdownFile2DOCX").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No file/invalid file provided"));
    }

    @Test
    void test_handles_null_file_parameter_for_docx() throws Exception {
        mockMvc.perform(multipart("/api/markdownFile2DOCX"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No file/invalid file provided"));
    }

    @Test
    void test_invalidOptimize() throws Exception {
        MockMultipartFile resume = new MockMultipartFile(
            "resume", "resume.pdf", "application/pdf", "dummy resume".getBytes());
        MockMultipartFile job = new MockMultipartFile(
            "job", "jd.pdf", "application/pdf", "dummy job".getBytes());
        String optimizeJson = "{\"resume\":\"\",\"jobDescription\":\"\"}";
        mockMvc.perform(multipart("/api/upload")
                .file(resume)
                .file(job)
                .param("optimize", optimizeJson))
            .andExpect(status().isExpectationFailed())
            .andExpect(jsonPath("$.message").value("Required property missing or invalid."));
    }

    @Test
    void test_optimizeResume_with_valid_files() throws Exception {
        MockMultipartFile resume = new MockMultipartFile(
                "resume", "resume.pdf", "application/pdf", "dummy resume".getBytes());
        MockMultipartFile job = new MockMultipartFile(
                "job", "jd.pdf", "application/pdf", "dummy job".getBytes());
        String optimizeJson = "{\"company\":\"company\",\"jobTitle\":\"title\",\"model\":\"model\",\"temperature\":0.01,\"promptType\":[\"Resume\"],\"jobDescription\":\"\",\"resume\":\"\"}";


        mockMvc.perform(multipart("/api/upload")
                .file(resume)
                .file(job)
                .param("optimize", optimizeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("generating"));
    }

    @Test
    void test_getListFiles_with_files() throws Exception {
        mockMvc.perform(get("/api/files"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void test_optimizeResume_with_invalid_optimize() throws Exception {
        MockMultipartFile resume = new MockMultipartFile(
                "resume", "resume.pdf", "application/pdf", "dummy resume".getBytes());
        MockMultipartFile job = new MockMultipartFile(
                "job", "jd.pdf", "application/pdf", "dummy job".getBytes());
        String invalidOptimize = "{invalid}";
        mockMvc.perform(multipart("/api/upload")
                .file(resume)
                .file(job)
                .param("optimize", invalidOptimize))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("invalid optimize parameter"));
    }

    @Test
    void test_getListFiles_empty() throws Exception {
        // This test assumes the storage is empty; may need to clear storage before running
        mockMvc.perform(get("/api/files"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetFile() throws Exception {
        // Test requesting a non-existent file; should return 404
        mockMvc.perform(get("/api/files/nonexistent.md"))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_deleteFile_success() throws Exception {
        // This test assumes a file named "resume.pdf" exists in storage
        mockMvc.perform(delete("/api/files/resume.pdf"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Delete the file successfully")));
    }

    @Test
    void test_deleteFile_not_found() throws Exception {
        mockMvc.perform(delete("/api/files/nonexistent.pdf"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("The file does not exist!"));
    }

    @Test
    void test_processSkills_with_valid_job() throws Exception {
        MockMultipartFile job = new MockMultipartFile(
                "job", "jd.txt", "text/plain", "Java Spring Developer position".getBytes());
        String optimizeJson = "{\"company\":\"TechCorp\",\"jobTitle\":\"Java Developer\",\"model\":\"mistral\",\"temperature\":0.15,\"promptType\":[\"SKILLS\"],\"jobDescription\":\"\",\"resume\":\"\"}";

        mockMvc.perform(multipart("/api/process/skills")
                .file(job)
                .param("optimize", optimizeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Skills suggestion generation started"));
    }

    @Test
    void test_processSkills_with_invalid_optimize() throws Exception {
        MockMultipartFile job = new MockMultipartFile(
                "job", "jd.txt", "text/plain", "Java Developer position".getBytes());
        String invalidOptimize = "{invalid json}";

        mockMvc.perform(multipart("/api/process/skills")
                .file(job)
                .param("optimize", invalidOptimize))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("invalid optimize parameter"));
    }

    @Test
    void test_processSkills_without_job_description() throws Exception {
        String optimizeJson = "{\"company\":\"TechCorp\",\"jobTitle\":\"Java Developer\",\"model\":\"mistral\",\"temperature\":0.15,\"promptType\":[\"SKILLS\"],\"jobDescription\":\"\",\"resume\":\"\"}";

        mockMvc.perform(multipart("/api/process/skills")
                .param("optimize", optimizeJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("job description is required"));
    }
}
