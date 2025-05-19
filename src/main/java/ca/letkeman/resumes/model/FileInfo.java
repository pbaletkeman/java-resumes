package ca.letkeman.resumes.model;

import java.io.File;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileInfo {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileInfo.class);

  private String name;
  private String url;
  private String size;

  public FileInfo(String name, String url, String root) {
    this.name = name;
    this.url = url;
    this.size = calcFileSize(name,root);
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

  private String calcFileSize(String root, String fileName){
    long ONE_MB = 1048576;
    long ONE_KB = 1024;
    File file = new File(Paths.get(fileName, root).toAbsolutePath().toUri());
    if (!file.exists() || !file.isFile()) {
      LOGGER.error("Invalid file path");
      return "0 bytes";
    } else {
      long fileSize = file.length();
      if (fileSize > ONE_MB){
        return fileSize/ONE_MB + " mb";
      } else if (fileSize > ONE_KB){
        return fileSize/ONE_KB + " kb";
      } else {
        return fileSize + " bytes";
      }
    }
  }
}
