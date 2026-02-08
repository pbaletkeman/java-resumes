package ca.letkeman.resumes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class UtilityTest {

    @TempDir
    Path tempDir;

    @Test
    void testRemoveFileExtensionSingleExtension() {
        String result = Utility.removeFileExtension("document.pdf", false);
        Assertions.assertEquals("document", result);
    }

    @Test
    void testRemoveFileExtensionMultipleExtensions() {
        String result = Utility.removeFileExtension("archive.tar.gz", false);
        Assertions.assertEquals("archive.tar", result);
    }

    @Test
    void testRemoveFileExtensionRemoveAllExtensions() {
        String result = Utility.removeFileExtension("archive.tar.gz", true);
        Assertions.assertEquals("archive", result);
    }

    @Test
    void testRemoveFileExtensionNoExtension() {
        String result = Utility.removeFileExtension("document", false);
        Assertions.assertEquals("document", result);
    }

    @Test
    void testRemoveFileExtensionNullFilename() {
        String result = Utility.removeFileExtension(null, false);
        Assertions.assertNull(result);
    }

    @Test
    void testRemoveFileExtensionEmptyFilename() {
        String result = Utility.removeFileExtension("", false);
        Assertions.assertEquals("", result);
    }

    @Test
    void testRemoveFileExtensionOnlyExtension() {
        String result = Utility.removeFileExtension(".hidden", false);
        Assertions.assertEquals(".hidden", result);
    }

    @Test
    void testConvertLineEndingsUnix() {
        String input = "line1\nline2\nline3";
        String result = Utility.convertLineEndings(input);
        Assertions.assertTrue(result.contains("\\n"));
    }

    @Test
    void testConvertLineEndingsWindows() {
        String input = "line1\r\nline2\r\nline3";
        String result = Utility.convertLineEndings(input);
        Assertions.assertTrue(result.contains("\\n"));
    }

    @Test
    void testConvertLineEndingsMac() {
        String input = "line1\rline2\rline3";
        String result = Utility.convertLineEndings(input);
        Assertions.assertTrue(result.contains("\\n"));
    }

    @Test
    void testConvertLineEndingsMixed() {
        String input = "line1\nline2\r\nline3\rline4";
        String result = Utility.convertLineEndings(input);
        Assertions.assertTrue(result.contains("\\n"));
    }

    @Test
    void testConvertLineEndingsNull() {
        String result = Utility.convertLineEndings(null);
        Assertions.assertNull(result);
    }

    @Test
    void testConvertLineEndingsEmpty() {
        String result = Utility.convertLineEndings("");
        Assertions.assertEquals("", result);
    }

    @Test
    void testConvertLineEndingsNoLineEndings() {
        String input = "single line with no breaks";
        String result = Utility.convertLineEndings(input);
        Assertions.assertEquals(input, result);
    }

    @Test
    void testReadFileAsStringFromCurrentDirectory() throws IOException {
        // Create a temp file in the temp directory
        Path testFile = tempDir.resolve("test.txt");
        String content = "Test content";
        Files.writeString(testFile, content);

        // Read from full path (simulating current directory)
        String result = Utility.readFileAsString(testFile.toString());
        Assertions.assertEquals(content, result);
    }

    @Test
    void testReadFileAsStringNonExistentFile() {
        String result = Utility.readFileAsString("nonexistent-file.txt");
        Assertions.assertEquals("", result);
    }

    @Test
    void testReadFileAsStringFromDirectory() throws IOException {
        Path testFile = tempDir.resolve("test2.txt");
        String content = "Test content 2";
        Files.writeString(testFile, content);

        String result = Utility.readFileAsString(tempDir.toString(), "test2.txt");
        Assertions.assertEquals(content, result);
    }

    @Test
    void testReadFileAsStringFromDirectoryNonExistent() {
        String result = Utility.readFileAsString(tempDir.toString(), "nonexistent.txt");
        Assertions.assertEquals("", result);
    }

    @Test
    void testReadFileAsStringWithExternalPath() throws IOException {
        // Set up external path
        Path externalDir = tempDir.resolve("external");
        Files.createDirectories(externalDir);
        Path externalFile = externalDir.resolve("config.json");
        String content = "{\"key\": \"value\"}";
        Files.writeString(externalFile, content);

        // Set system property
        String originalProperty = System.getProperty("app.config.path");
        try {
            System.setProperty("app.config.path", externalDir.toString());
            String result = Utility.readFileAsString("config.json");
            Assertions.assertEquals(content, result);
        } finally {
            // Restore original property
            if (originalProperty != null) {
                System.setProperty("app.config.path", originalProperty);
            } else {
                System.clearProperty("app.config.path");
            }
        }
    }

    @Test
    void testReadFileAsStringExternalPathNotSet() throws IOException {
        Path testFile = tempDir.resolve("test3.txt");
        String content = "Test content 3";
        Files.writeString(testFile, content);

        // Ensure external path is not set
        String originalProperty = System.getProperty("app.config.path");
        try {
            System.clearProperty("app.config.path");
            String result = Utility.readFileAsString(testFile.toString());
            Assertions.assertEquals(content, result);
        } finally {
            if (originalProperty != null) {
                System.setProperty("app.config.path", originalProperty);
            }
        }
    }

    @Test
    void testReadFileAsStringExternalPathEmpty() throws IOException {
        Path testFile = tempDir.resolve("test4.txt");
        String content = "Test content 4";
        Files.writeString(testFile, content);

        String originalProperty = System.getProperty("app.config.path");
        try {
            System.setProperty("app.config.path", "");
            String result = Utility.readFileAsString(testFile.toString());
            Assertions.assertEquals(content, result);
        } finally {
            if (originalProperty != null) {
                System.setProperty("app.config.path", originalProperty);
            } else {
                System.clearProperty("app.config.path");
            }
        }
    }

    @Test
    void testRemoveFileExtensionPathWithDots() {
        String result = Utility.removeFileExtension("path/to/file.name.txt", false);
        Assertions.assertEquals("path/to/file.name", result);
    }

    @Test
    void testRemoveFileExtensionPathWithDotsRemoveAll() {
        String result = Utility.removeFileExtension("path/to/file.name.txt", true);
        Assertions.assertEquals("path/to/file", result);
    }

    @Test
    void testConvertLineEndingsMultipleConsecutiveNewlines() {
        String input = "line1\n\n\nline2";
        String result = Utility.convertLineEndings(input);
        Assertions.assertTrue(result.contains("\\n"));
    }

    @Test
    void testConvertLineEndingsOnlyNewlines() {
        String input = "\n\n\n";
        String result = Utility.convertLineEndings(input);
        // Should convert and trim
        Assertions.assertFalse(result.isEmpty() || result.isBlank());
    }
}
