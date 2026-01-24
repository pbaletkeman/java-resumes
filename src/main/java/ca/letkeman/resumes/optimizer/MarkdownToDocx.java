package ca.letkeman.resumes.optimizer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MarkdownToDocx {

  private static Logger logger = LoggerFactory.getLogger(MarkdownToDocx.class);

  private String markdownFilePath;
  private String docxFilePath;
  private String markdownContent;

  public static Logger getLogger() {
    return logger;
  }

  public static void setLogger(Logger logger) {
    MarkdownToDocx.logger = logger;
  }

  public String getMarkdownContent() {
    return markdownContent;
  }

  public void setMarkdownContent(String markdownContent) {
    this.markdownContent = markdownContent;
  }

  public String getMarkdownFilePath() {
    return markdownFilePath;
  }

  public void setMarkdownFilePath(String markdownFilePath) {
    this.markdownFilePath = markdownFilePath;
  }

  public String getDocxFilePath() {
    return docxFilePath;
  }

  public void setDocxFilePath(String docxFilePath) {
    this.docxFilePath = docxFilePath;
  }

  public MarkdownToDocx() {}

  public MarkdownToDocx(String markdownFilePath, String docxFilePath, String markdownContent) {
    if (markdownContent == null || markdownContent.isBlank()) {
      setMarkdownFilePath(markdownFilePath);
    } else {
      setMarkdownContent(markdownContent);
    }
    setDocxFilePath(docxFilePath);
  }

  public boolean convertFile() {
    String content = "";
    if (getMarkdownContent() == null || getMarkdownContent().isBlank()) {
      try {
        content = new String(Files.readAllBytes(Paths.get(getMarkdownFilePath())));
      } catch (IOException e) {
        logger.error("Error unable to read markdown file:\n{}", e.toString());
        return false;
      }
    } else {
      content = getMarkdownContent();
    }

    try {
      markdownToDocx(content, getDocxFilePath());
      logger.info("DOCX saved to: {}", getDocxFilePath());
      return true;
    } catch (Exception e) {
      logger.error("Error unable to save DOCX file:\n{}", e.toString());
      return false;
    }
  }

  private void markdownToDocx(String markdownContent, String outputPath) throws IOException {
    XWPFDocument document = new XWPFDocument();

    // Split content by double newlines to identify paragraphs
    String[] blocks = markdownContent.split("\\n(?=\\n)");

    for (String block : blocks) {
      block = block.trim();
      if (block.isEmpty()) {
        continue;
      }

      if (block.startsWith("# ")) {
        // H1 heading
        XWPFParagraph para = document.createParagraph();
        XWPFRun run = para.createRun();
        run.setText(block.replaceFirst("^# ", ""));
        run.setBold(true);
        run.setFontSize(28);
        para.setAlignment(ParagraphAlignment.LEFT);
      } else if (block.startsWith("## ")) {
        // H2 heading
        XWPFParagraph para = document.createParagraph();
        XWPFRun run = para.createRun();
        run.setText(block.replaceFirst("^## ", ""));
        run.setBold(true);
        run.setFontSize(24);
        para.setAlignment(ParagraphAlignment.LEFT);
      } else if (block.startsWith("### ")) {
        // H3 heading
        XWPFParagraph para = document.createParagraph();
        XWPFRun run = para.createRun();
        run.setText(block.replaceFirst("^### ", ""));
        run.setBold(true);
        run.setFontSize(20);
        para.setAlignment(ParagraphAlignment.LEFT);
      } else if (block.startsWith("- ") || block.startsWith("* ")) {
        // Bullet point
        XWPFParagraph para = document.createParagraph();
        para.setIndentationLeft(720); // indent for bullet
        XWPFRun run = para.createRun();
        run.setText("• " + block.replaceFirst("^[-*] ", ""));
      } else {
        // Regular paragraph
        XWPFParagraph para = document.createParagraph();
        String processedText = processInlineFormatting(block);
        addTextWithFormatting(para, processedText);
      }
    }

    // Write to file
    try (FileOutputStream out = new FileOutputStream(new File(outputPath))) {
      document.write(out);
    }
    document.close();
  }

  private String processInlineFormatting(String text) {
    // Handle **bold** and __bold__
    text = text.replaceAll("\\*\\*(.*?)\\*\\*", "$1");
    text = text.replaceAll("__(.*?)__", "$1");
    // Handle *italic* and _italic_
    text = text.replaceAll("\\*(.*?)\\*", "$1");
    text = text.replaceAll("_(.*?)_", "$1");
    // Handle `code`
    text = text.replaceAll("`(.*?)`", "$1");
    // Handle [link](url)
    text = text.replaceAll("\\[(.*?)\\]\\((.*?)\\)", "$1");
    return text;
  }

  private void addTextWithFormatting(XWPFParagraph para, String text) {
    // Simple approach: add text with basic formatting detection
    Pattern boldPattern = Pattern.compile("\\*\\*(.*?)\\*\\*");
    Pattern italicPattern = Pattern.compile("\\*(.*?)\\*");

    Matcher matcher = boldPattern.matcher(text);
    int lastEnd = 0;

    while (matcher.find()) {
      // Add normal text before the match
      if (matcher.start() > lastEnd) {
        XWPFRun run = para.createRun();
        run.setText(text.substring(lastEnd, matcher.start()));
      }

      // Add bold text
      XWPFRun boldRun = para.createRun();
      boldRun.setText(matcher.group(1));
      boldRun.setBold(true);

      lastEnd = matcher.end();
    }

    // Add remaining text
    if (lastEnd < text.length()) {
      XWPFRun run = para.createRun();
      run.setText(text.substring(lastEnd));
    }

    // If no formatting found, just add the text
    if (lastEnd == 0) {
      XWPFRun run = para.createRun();
      run.setText(text);
    }
  }

  /*
   * sample usage:
   * MarkdownToDocx mdToDocx = new MarkdownToDocx("sample/resume.md","output.docx",
   * "");
   * mdToDocx.convertFile();
   * --------------------
   * MarkdownToDocx mdToDocx = new MarkdownToDocx("","output.docx",
   * "**markdown content**");
   * mdToDocx.convertFile();
   */
}
