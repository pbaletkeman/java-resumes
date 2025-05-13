package ca.letkeman.resumes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.extend.FontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

public class HtmlToPdf {
  public static void main(String[] args) throws IOException {

    String markdownContent = "";
    try {
      markdownContent = new String(Files.readAllBytes(Paths.get("sample/resume.md")));
    } catch (IOException e) {
      System.out.println(e);
    }
    Parser parser = Parser.builder().build();
    Node document = parser.parse(markdownContent);
    HtmlRenderer renderer = HtmlRenderer.builder().build();
    String HTMLContent = renderer.render(document);

    String xhtml = htmlToXhtml(HTMLContent);
    xhtmlToPdf(xhtml, "output.pdf");
  }

  private static String htmlToXhtml(String html) {
    Document document = Jsoup.parse(html);
    document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
    return document.html();
  }

  private static void xhtmlToPdf(String xhtml, String outFileName) throws IOException {
    File output = new File(outFileName);
    ITextRenderer iTextRenderer = new ITextRenderer();
    FontResolver resolver = iTextRenderer.getFontResolver();
    iTextRenderer.setDocumentFromString(xhtml);
    iTextRenderer.layout();
    OutputStream os = new FileOutputStream(output);
    iTextRenderer.createPDF(os);
    os.close();
  }
}