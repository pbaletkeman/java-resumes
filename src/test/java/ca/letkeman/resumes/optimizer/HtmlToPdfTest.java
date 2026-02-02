package ca.letkeman.resumes.optimizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.slf4j.Logger;

class HtmlToPdfTest {

  @TempDir Path tempDir;

  private HtmlToPdf htmlToPdf;
  private String markdownContent;
  private String pdfFilePath;

  @BeforeEach
  void setUp() {
    htmlToPdf = new HtmlToPdf();
    markdownContent = "# Test Heading\n\nTest content.";
    pdfFilePath = tempDir.resolve("test.pdf").toString();
  }

  @AfterEach
  void tearDown() {
    // Cleanup any generated files
    File pdfFile = new File(pdfFilePath);
    if (pdfFile.exists()) {
      pdfFile.delete();
    }
  }

  @Test
  void testDefaultConstructor() {
    HtmlToPdf converter = new HtmlToPdf();
    Assertions.assertNull(converter.getMarkdownFilePath());
    Assertions.assertNull(converter.getPdfFilePath());
    Assertions.assertNull(converter.getMarkdownContent());
  }

  @Test
  void testParameterizedConstructorWithContent() {
    String content = "# Test";
    HtmlToPdf converter = new HtmlToPdf("", pdfFilePath, content);
    Assertions.assertEquals(content, converter.getMarkdownContent());
    Assertions.assertEquals(pdfFilePath, converter.getPdfFilePath());
  }

  @Test
  void testParameterizedConstructorWithBlankContentUsesFilePath() {
    String filePath = "test.md";
    HtmlToPdf converter = new HtmlToPdf(filePath, pdfFilePath, "");
    Assertions.assertEquals(filePath, converter.getMarkdownFilePath());
    Assertions.assertEquals(pdfFilePath, converter.getPdfFilePath());
  }

  @Test
  void testSettersAndGetters() {
    htmlToPdf.setMarkdownContent(markdownContent);
    htmlToPdf.setPdfFilePath(pdfFilePath);
    htmlToPdf.setMarkdownFilePath("test.md");

    Assertions.assertEquals(markdownContent, htmlToPdf.getMarkdownContent());
    Assertions.assertEquals(pdfFilePath, htmlToPdf.getPdfFilePath());
    Assertions.assertEquals("test.md", htmlToPdf.getMarkdownFilePath());
  }

  @Test
  void testConvertFileWithMarkdownContent() {
    htmlToPdf.setMarkdownContent(markdownContent);
    htmlToPdf.setPdfFilePath(pdfFilePath);

    boolean result = htmlToPdf.convertFile();

    Assertions.assertTrue(result);
    Assertions.assertTrue(new File(pdfFilePath).exists());
    Assertions.assertTrue(new File(pdfFilePath).length() > 0);
  }

  @Test
  void testConvertFileWithMarkdownFile() throws IOException {
    // Create a temporary markdown file
    Path mdFile = tempDir.resolve("test.md");
    Files.writeString(mdFile, markdownContent);

    htmlToPdf.setMarkdownFilePath(mdFile.toString());
    htmlToPdf.setPdfFilePath(pdfFilePath);

    boolean result = htmlToPdf.convertFile();

    Assertions.assertTrue(result);
    Assertions.assertTrue(new File(pdfFilePath).exists());
  }

  @Test
  void testConvertFileWithNonExistentMarkdownFile() {
    htmlToPdf.setMarkdownFilePath("non_existent_file.md");
    htmlToPdf.setPdfFilePath(pdfFilePath);

    boolean result = htmlToPdf.convertFile();

    Assertions.assertFalse(result);
  }

  @Test
  void testConvertFileWithNullMarkdownContent() throws IOException {
    // Create a temporary markdown file
    Path mdFile = tempDir.resolve("test.md");
    Files.writeString(mdFile, markdownContent);

    htmlToPdf.setMarkdownFilePath(mdFile.toString());
    htmlToPdf.setPdfFilePath(pdfFilePath);
    htmlToPdf.setMarkdownContent(null);

    boolean result = htmlToPdf.convertFile();

    Assertions.assertTrue(result);
  }

  @Test
  void testConvertFileWithBlankMarkdownContent() throws IOException {
    // Create a temporary markdown file
    Path mdFile = tempDir.resolve("test.md");
    Files.writeString(mdFile, markdownContent);

    htmlToPdf.setMarkdownFilePath(mdFile.toString());
    htmlToPdf.setPdfFilePath(pdfFilePath);
    htmlToPdf.setMarkdownContent("   ");

    boolean result = htmlToPdf.convertFile();

    Assertions.assertTrue(result);
  }

  @Test
  void testConvertFileWithInvalidPdfPath() {
    htmlToPdf.setMarkdownContent(markdownContent);
    htmlToPdf.setPdfFilePath("/invalid/path/that/does/not/exist/test.pdf");

    boolean result = htmlToPdf.convertFile();

    Assertions.assertTrue(result);
  }

  @Test
  void testConvertFileWithComplexMarkdown() {
    String complexMarkdown = "# Heading 1\n\n"
        + "## Heading 2\n\n"
        + "**Bold text** and *italic text*.\n\n"
        + "- List item 1\n"
        + "- List item 2\n\n"
        + "[Link](https://example.com)";

    htmlToPdf.setMarkdownContent(complexMarkdown);
    htmlToPdf.setPdfFilePath(pdfFilePath);

    boolean result = htmlToPdf.convertFile();

    Assertions.assertTrue(result);
    Assertions.assertTrue(new File(pdfFilePath).exists());
  }

  @Test
  void testLoggerGetterAndSetter() {
    Logger originalLogger = HtmlToPdf.getLogger();
    Assertions.assertNotNull(originalLogger);

    Logger mockLogger = Mockito.mock(Logger.class);
    HtmlToPdf.setLogger(mockLogger);

    Assertions.assertEquals(mockLogger, HtmlToPdf.getLogger());

    // Restore original logger
    HtmlToPdf.setLogger(originalLogger);
  }

  @Test
  void testConvertFileWithEmptyMarkdownContent() {
    htmlToPdf.setMarkdownContent("");
    htmlToPdf.setPdfFilePath(pdfFilePath);
    htmlToPdf.setMarkdownFilePath("non_existent.md");

    boolean result = htmlToPdf.convertFile();

    Assertions.assertFalse(result);
  }

  @Test
  void testConvertFileWithSpecialCharactersInMarkdown() {
    String specialMarkdown = "# Test\n\nSpecial chars: <>&\"'";
    htmlToPdf.setMarkdownContent(specialMarkdown);
    htmlToPdf.setPdfFilePath(pdfFilePath);

    boolean result = htmlToPdf.convertFile();

    Assertions.assertTrue(result);
    Assertions.assertTrue(new File(pdfFilePath).exists());
  }
}
