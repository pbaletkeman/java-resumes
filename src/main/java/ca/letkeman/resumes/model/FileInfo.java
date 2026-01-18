package ca.letkeman.resumes.model;

import java.io.File;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileInfo {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileInfo.class);

  private String name;
  private String url;
  private String size;
  private String date;

  public FileInfo(String name, String url, String root, String date) {
    this.name = name;
    this.url = url;
    this.size = calcFileSize(root, name);
    this.date = date;
  }

  public FileInfo(String name, String url) {
    this.name = name;
    this.url = url;
    this.size = "10mb";
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  private String calcFileSize(String root, String fileName) {
    long oneMb = 1048576;
    long oneKb = 1024;
    File file = new File(Paths.get(root, fileName).toAbsolutePath().toUri());
    if (!file.exists() || !file.isFile()) {
      LOGGER.error("Invalid file path");
      return "0 bytes";
    } else {
      long fileSize = file.length();
      if (fileSize > oneMb) {
        return fileSize / oneMb + " mb";
      } else if (fileSize > oneKb) {
        return fileSize / oneKb + " kb";
      } else {
        return fileSize + " bytes";
      }
    }
  }
}
