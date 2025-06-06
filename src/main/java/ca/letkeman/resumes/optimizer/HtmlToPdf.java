package ca.letkeman.resumes.optimizer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.pdf.ITextRenderer;

public final class HtmlToPdf {

  private static Logger logger = LoggerFactory.getLogger(HtmlToPdf.class);

  private String markdownFilePath;
  private String pdfFilePath;
  private String markdownContent;


  public static Logger getLogger() {
    return logger;
  }

  public static void setLogger(Logger logger) {
    HtmlToPdf.logger = logger;
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

  public String getPdfFilePath() {
    return pdfFilePath;
  }

  public void setPdfFilePath(String pdfFilePath) {
    this.pdfFilePath = pdfFilePath;
  }

  public HtmlToPdf(){}

  public HtmlToPdf(String markdownFilePath, String pdfFilePath, String markdownContent) {
    if (markdownContent.isBlank()) {
      setMarkdownFilePath(markdownFilePath);
    } else {
      setMarkdownContent(markdownContent);
    }
    setPdfFilePath(pdfFilePath);
  }

  private static String htmlToXhtml(String html) {
    Document document = Jsoup.parse(html);
    document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
    return document.html();
  }

  private static void xhtmlToPdf(String xhtml, String outFileName) throws IOException {
    File output = new File(outFileName);
    ITextRenderer iTextRenderer = new ITextRenderer();
    iTextRenderer.setDocumentFromString(xhtml);
    iTextRenderer.layout();
    OutputStream os = new FileOutputStream(output);
    iTextRenderer.createPDF(os);
    os.close();
  }

  public boolean convertFile (){
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
    Parser parser = Parser.builder().build();
    Node document = parser.parse(content);
    HtmlRenderer renderer = HtmlRenderer.builder().build();

    try {
      xhtmlToPdf(htmlToXhtml(renderer.render(document)), getPdfFilePath());
      logger.info("PDF saved to: {}", getPdfFilePath());
    } catch (Exception e) {
      logger.error("Error unable to save PDF file:\n{}", e.toString());
      return false;
    }
    return true;
  }

/*
    sample usage:
    HtmlToPdf htmlToPdf = new HtmlToPdf("sample/resume.md","output.pdf", "");
    htmlToPdf.convertFile();
    --------------------
    HtmlToPdf htmlToPdf = new HtmlToPdf("","output.pdf", "**markdown content**");
    htmlToPdf.convertFile();
*/
}