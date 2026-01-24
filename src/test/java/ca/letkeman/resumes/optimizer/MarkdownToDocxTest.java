package ca.letkeman.resumes.optimizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
  void testConvertMarkdownContentToDocx() {
    String markdownContent = "# Test Resume\n\n## Skills\n\n- Java\n- Spring Boot\n\n"
        + "## Experience\n\nSoftware Engineer at Tech Corp";

    MarkdownToDocx converter = new MarkdownToDocx("", testDocxFile, markdownContent);
    boolean result = converter.convertFile();

    Assertions.assertTrue(result, "Conversion should succeed");
    Assertions.assertTrue(Files.exists(Paths.get(testDocxFile)), "DOCX file should be created");
  }

  @Test
  void testConvertMarkdownFileToDocx() throws IOException {
    // Create a test markdown file
    String markdownContent = "# Sample Resume\n\n## Experience\n\nSoftware Developer";
    Files.writeString(Paths.get(testMarkdownFile), markdownContent);

    MarkdownToDocx converter = new MarkdownToDocx(testMarkdownFile, testDocxFile, "");
    boolean result = converter.convertFile();

    Assertions.assertTrue(result, "Conversion should succeed");
    Assertions.assertTrue(Files.exists(Paths.get(testDocxFile)), "DOCX file should be created");
  }

  @Test
  void testConvertEmptyMarkdownToDocx() {
    MarkdownToDocx converter = new MarkdownToDocx("", testDocxFile, "");
    boolean result = converter.convertFile();

    Assertions.assertFalse(result, "Conversion should fail with empty content");
  }

  @Test
  void testConvertMarkdownWithFormatting() {
    String markdownContent = "# Title\n\n**Bold text** and *italic text*\n\n- Point 1\n- Point 2";

    MarkdownToDocx converter = new MarkdownToDocx("", testDocxFile, markdownContent);
    boolean result = converter.convertFile();

    Assertions.assertTrue(result, "Conversion should succeed with formatted content");
    Assertions.assertTrue(Files.exists(Paths.get(testDocxFile)), "DOCX file should be created");
  }

  @Test
  void testConvertMarkdownWithHeaders() {
    String markdownContent = "# H1 Header\n\n## H2 Header\n\n### H3 Header\n\nParagraph text";

    MarkdownToDocx converter = new MarkdownToDocx("", testDocxFile, markdownContent);
    boolean result = converter.convertFile();

    Assertions.assertTrue(result, "Conversion should succeed with headers");
    Assertions.assertTrue(Files.exists(Paths.get(testDocxFile)), "DOCX file should be created");
  }

  @Test
  void testDocxGettersAndSetters() {
    MarkdownToDocx converter = new MarkdownToDocx();

    converter.setMarkdownFilePath(testMarkdownFile);
    converter.setDocxFilePath(testDocxFile);
    converter.setMarkdownContent("Test content");

    Assertions.assertTrue(converter.getMarkdownFilePath().equals(testMarkdownFile));
    Assertions.assertTrue(converter.getDocxFilePath().equals(testDocxFile));
    Assertions.assertTrue(converter.getMarkdownContent().equals("Test content"));
  }

  @Test
  void testNonExistentFileFails() {
    MarkdownToDocx converter = new MarkdownToDocx(
        testDir + File.separator + "nonexistent.md",
        testDocxFile, "");
    boolean result = converter.convertFile();

    Assertions.assertFalse(result, "Conversion should fail for non-existent file");
  }
}
