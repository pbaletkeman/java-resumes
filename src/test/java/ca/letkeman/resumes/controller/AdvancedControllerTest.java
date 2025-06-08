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
class AdvancedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test_successful_markdown_to_pdf_conversion() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.md", "text/markdown", "# Test Markdown".getBytes());
        mockMvc.perform(multipart("/markdownFile2PDF").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("file successfully converted"));
    }

    @Test
    void test_unsuccessful_markdown_to_pdf_conversion() throws Exception {
        // Simulate a file that will fail conversion (e.g., empty or invalid content)
        MockMultipartFile file = new MockMultipartFile(
                "file", "bad.md", "text/markdown", new byte[0]);
        mockMvc.perform(multipart("/markdownFile2PDF").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No file/invalid file provided"));
    }

    @Test
    void test_handles_null_file_parameter() throws Exception {
        mockMvc.perform(multipart("/markdownFile2PDF"))
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
        mockMvc.perform(multipart("/upload")
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


        mockMvc.perform(multipart("/upload")
                .file(resume)
                .file(job)
                .param("optimize", optimizeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("generating"));
    }

    @Test
    void test_getListFiles_with_files() throws Exception {
        mockMvc.perform(get("/get-files"))
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
        mockMvc.perform(multipart("/upload")
                .file(resume)
                .file(job)
                .param("optimize", invalidOptimize))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("invalid optimize parameter"));
    }

    @Test
    void test_getListFiles_empty() throws Exception {
        // This test assumes the storage is empty; may need to clear storage before running
        mockMvc.perform(get("/get-files"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void test_getFile() throws Exception {
        // This test assumes a file named "test.md" exists in storage
        mockMvc.perform(get("/files/test.md"))
                .andExpect(status().isOk());
    }

    @Test
    void test_deleteFile_success() throws Exception {
        // This test assumes a file named "resume.pdf" exists in storage
        mockMvc.perform(delete("/files/resume.pdf"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Delete the file successfully")));
    }

    @Test
    void test_deleteFile_not_found() throws Exception {
        mockMvc.perform(delete("/files/nonexistent.pdf"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("The file does not exist!"));
    }
}
