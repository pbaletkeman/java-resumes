package ca.letkeman.resumes.optimizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

class MarkdownToDocxTest {

  private String testDir;
  private String testMarkdownFile;
  private String testDocxFile;

  @BeforeEach
  void setUp() {
    testDir = "test_output";
    testMarkdownFile = testDir + File.separator + "test.md";
    testDocxFile = testDir + File.separator + "test.docx";

    // Create test directory
    new File(testDir).mkdirs();
  }

  @AfterEach
  void tearDown() {
    // Clean up test files
    try {
      Path path = Paths.get(testDir);
      if (Files.exists(path)) {
        Files.walk(path)
            .sorted((p1, p2) -> p2.compareTo(p1))
            .forEach(p -> {
              try {
                Files.delete(p);
              } catch (IOException e) {
                // Ignore
              }
            });
      }
    } catch (IOException e) {
      // Ignore cleanup errors
    }
  }

  @Test
  void test_convert_markdown_content_to_docx() {
    String markdownContent = "# Test Resume\n\n## Skills\n\n- Java\n- Spring Boot\n\n## Experience\n\nSoftware Engineer at Tech Corp";

    MarkdownToDocx converter = new MarkdownToDocx("", testDocxFile, markdownContent);
    boolean result = converter.convertFile();

    assertTrue(result, "Conversion should succeed");
    assertTrue(Files.exists(Paths.get(testDocxFile)), "DOCX file should be created");
  }

  @Test
  void test_convert_markdown_file_to_docx() throws IOException {
    // Create a test markdown file
    String markdownContent = "# Sample Resume\n\n## Experience\n\nSoftware Developer";
    Files.writeString(Paths.get(testMarkdownFile), markdownContent);

    MarkdownToDocx converter = new MarkdownToDocx(testMarkdownFile, testDocxFile, "");
    boolean result = converter.convertFile();

    assertTrue(result, "Conversion should succeed");
    assertTrue(Files.exists(Paths.get(testDocxFile)), "DOCX file should be created");
  }

  @Test
  void test_convert_empty_markdown_to_docx() {
    MarkdownToDocx converter = new MarkdownToDocx("", testDocxFile, "");
    boolean result = converter.convertFile();

    assertFalse(result, "Conversion should fail with empty content");
  }

  @Test
  void test_convert_markdown_with_formatting() {
    String markdownContent = "# Title\n\n**Bold text** and *italic text*\n\n- Point 1\n- Point 2";

    MarkdownToDocx converter = new MarkdownToDocx("", testDocxFile, markdownContent);
    boolean result = converter.convertFile();

    assertTrue(result, "Conversion should succeed with formatted content");
    assertTrue(Files.exists(Paths.get(testDocxFile)), "DOCX file should be created");
  }

  @Test
  void test_convert_markdown_with_headers() {
    String markdownContent = "# H1 Header\n\n## H2 Header\n\n### H3 Header\n\nParagraph text";

    MarkdownToDocx converter = new MarkdownToDocx("", testDocxFile, markdownContent);
    boolean result = converter.convertFile();

    assertTrue(result, "Conversion should succeed with headers");
    assertTrue(Files.exists(Paths.get(testDocxFile)), "DOCX file should be created");
  }

  @Test
  void test_docx_getters_and_setters() {
    MarkdownToDocx converter = new MarkdownToDocx();

    converter.setMarkdownFilePath(testMarkdownFile);
    converter.setDocxFilePath(testDocxFile);
    converter.setMarkdownContent("Test content");

    assertTrue(converter.getMarkdownFilePath().equals(testMarkdownFile));
    assertTrue(converter.getDocxFilePath().equals(testDocxFile));
    assertTrue(converter.getMarkdownContent().equals("Test content"));
  }

  @Test
  void test_non_existent_file_fails() {
    MarkdownToDocx converter = new MarkdownToDocx(
        testDir + File.separator + "nonexistent.md",
        testDocxFile, "");
    boolean result = converter.convertFile();

    assertFalse(result, "Conversion should fail for non-existent file");
  }
}
