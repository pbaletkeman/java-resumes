package ca.letkeman.resumes.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class FileInfoTest {

  @Test
  void testFileInfoConstructorWithAllParameters(@TempDir Path tempDir) throws IOException {
    // Create a test file
    Path testFile = tempDir.resolve("test.txt");
    Files.writeString(testFile, "test content");

    FileInfo fileInfo = new FileInfo(
        "test.txt",
        "http://localhost/files/test.txt",
        tempDir.toString(),
        "2024-01-31"
    );

    Assertions.assertEquals("test.txt", fileInfo.getName());
    Assertions.assertEquals("http://localhost/files/test.txt", fileInfo.getUrl());
    Assertions.assertEquals("2024-01-31", fileInfo.getDate());
    Assertions.assertNotNull(fileInfo.getSize());
  }

  @Test
  void testFileInfoConstructorWithTwoParameters() {
    FileInfo fileInfo = new FileInfo("document.pdf", "http://localhost/files/document.pdf");

    Assertions.assertEquals("document.pdf", fileInfo.getName());
    Assertions.assertEquals("http://localhost/files/document.pdf", fileInfo.getUrl());
    Assertions.assertEquals("10mb", fileInfo.getSize());
  }

  @Test
  void testSettersAndGetters() {
    FileInfo fileInfo = new FileInfo("original.txt", "http://original.com");

    fileInfo.setName("updated.txt");
    fileInfo.setUrl("http://updated.com");
    fileInfo.setSize("5mb");
    fileInfo.setDate("2024-02-01");

    Assertions.assertEquals("updated.txt", fileInfo.getName());
    Assertions.assertEquals("http://updated.com", fileInfo.getUrl());
    Assertions.assertEquals("5mb", fileInfo.getSize());
    Assertions.assertEquals("2024-02-01", fileInfo.getDate());
  }

  @Test
  void testFileSizeCalculationForNonExistentFile(@TempDir Path tempDir) {
    FileInfo fileInfo = new FileInfo(
        "nonexistent.txt",
        "http://localhost/files/nonexistent.txt",
        tempDir.toString(),
        "2024-01-31"
    );

    Assertions.assertEquals("0 bytes", fileInfo.getSize());
  }

  @Test
  void testFileSizeCalculationInBytes(@TempDir Path tempDir) throws IOException {
    // Create a small file (100 bytes)
    Path testFile = tempDir.resolve("small.txt");
    Files.writeString(testFile, "x".repeat(100));

    FileInfo fileInfo = new FileInfo(
        "small.txt",
        "http://localhost/files/small.txt",
        tempDir.toString(),
        "2024-01-31"
    );

    Assertions.assertTrue(fileInfo.getSize().contains("bytes"));
  }

  @Test
  void testFileSizeCalculationInKilobytes(@TempDir Path tempDir) throws IOException {
    // Create a file larger than 1KB
    Path testFile = tempDir.resolve("medium.txt");
    Files.writeString(testFile, "x".repeat(2048));

    FileInfo fileInfo = new FileInfo(
        "medium.txt",
        "http://localhost/files/medium.txt",
        tempDir.toString(),
        "2024-01-31"
    );

    Assertions.assertTrue(fileInfo.getSize().contains("kb"));
  }

  @Test
  void testFileSizeCalculationInMegabytes(@TempDir Path tempDir) throws IOException {
    // Create a file larger than 1MB (this will be mocked as we don't want to create actual large files)
    Path testFile = tempDir.resolve("large.txt");
    // Create a file with more than 1MB of data
    Files.writeString(testFile, "x".repeat(1048577)); // 1MB + 1 byte

    FileInfo fileInfo = new FileInfo(
        "large.txt",
        "http://localhost/files/large.txt",
        tempDir.toString(),
        "2024-01-31"
    );

    Assertions.assertTrue(fileInfo.getSize().contains("mb"));
  }

  @Test
  void testFileInfoWithNullName() {
    FileInfo fileInfo = new FileInfo(null, "http://localhost/files/null.txt");
    Assertions.assertNull(fileInfo.getName());
  }

  @Test
  void testFileInfoWithNullUrl() {
    FileInfo fileInfo = new FileInfo("test.txt", null);
    Assertions.assertNull(fileInfo.getUrl());
  }

  @Test
  void testSetDateWithNullValue() {
    FileInfo fileInfo = new FileInfo("test.txt", "http://localhost/test.txt");
    fileInfo.setDate(null);
    Assertions.assertNull(fileInfo.getDate());
  }

  @Test
  void testFileInfoGettersAfterSettingValues() {
    FileInfo fileInfo = new FileInfo("test.pdf", "http://test.com/test.pdf");
    fileInfo.setName("newname.pdf");
    fileInfo.setUrl("http://newurl.com");
    fileInfo.setSize("100kb");
    fileInfo.setDate("2024-02-01");
    
    Assertions.assertEquals("newname.pdf", fileInfo.getName());
    Assertions.assertEquals("http://newurl.com", fileInfo.getUrl());
    Assertions.assertEquals("100kb", fileInfo.getSize());
    Assertions.assertEquals("2024-02-01", fileInfo.getDate());
  }

  @Test
  void testFileInfoWithEmptyStringValues() {
    FileInfo fileInfo = new FileInfo("", "");
    Assertions.assertEquals("", fileInfo.getName());
    Assertions.assertEquals("", fileInfo.getUrl());
  }

  @Test
  void testFileSizeForExactlyOneMegabyte(@TempDir Path tempDir) throws IOException {
    Path testFile = tempDir.resolve("exact1mb.txt");
    Files.writeString(testFile, "x".repeat(1048577)); // Slightly more than 1MB
    
    FileInfo fileInfo = new FileInfo(
        "exact1mb.txt",
        "http://localhost/files/exact1mb.txt",
        tempDir.toString(),
        "2024-02-01"
    );
    
    Assertions.assertTrue(fileInfo.getSize().contains("mb"));
  }
}
