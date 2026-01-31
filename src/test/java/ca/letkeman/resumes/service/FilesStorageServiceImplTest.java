package ca.letkeman.resumes.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * Test class for FilesStorageServiceImpl.
 */
class FilesStorageServiceImplTest {

    private FilesStorageServiceImpl filesStorageService;
    private Path testUploadPath;

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary upload directory for tests
        testUploadPath = Paths.get(System.getProperty("java.io.tmpdir"), "test-file-storage");
        if (!Files.exists(testUploadPath)) {
            Files.createDirectories(testUploadPath);
        }
        filesStorageService = new FilesStorageServiceImpl();
        filesStorageService.init(testUploadPath.toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up test files
        if (Files.exists(testUploadPath)) {
            Files.walk(testUploadPath)
                    .sorted(java.util.Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            // Ignore deletion errors in cleanup
                        }
                    });
        }
    }

    @Test
    void testInit() {
        assertTrue(Files.exists(testUploadPath));
        assertTrue(Files.isDirectory(testUploadPath));
    }

    @Test
    void testSaveFile() {
        MultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Test content".getBytes()
        );

        assertDoesNotThrow(() -> filesStorageService.save(file));

        Path savedFile = testUploadPath.resolve("test.txt");
        assertTrue(Files.exists(savedFile));
    }

    @Test
    void testSaveFileWithNullFile() {
        // The implementation logs errors but doesn't throw exceptions
        // This test verifies that it doesn't crash
        assertDoesNotThrow(() -> {
            filesStorageService.save(null);
        });
    }

    @Test
    void testSaveFileWithEmptyFile() {
        MultipartFile emptyFile = new MockMultipartFile(
                "file",
                "empty.txt",
                "text/plain",
                new byte[0]
        );

        // The implementation allows empty files
        assertDoesNotThrow(() -> {
            filesStorageService.save(emptyFile);
        });
    }

    @Test
    void testLoadExistingFile() throws IOException {
        // Create a test file
        Path testFile = testUploadPath.resolve("load-test.txt");
        Files.writeString(testFile, "Test content");

        Resource resource = filesStorageService.load("load-test.txt");

        assertNotNull(resource);
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());
    }

    @Test
    void testLoadNonExistentFile() {
        Resource resource = filesStorageService.load("non-existent.txt");
        // Returns null for non-existent files
        assertNull(resource);
    }

    @Test
    void testDeleteExistingFile() throws IOException {
        // Create a test file
        Path testFile = testUploadPath.resolve("delete-test.txt");
        Files.writeString(testFile, "Test content");

        assertTrue(Files.exists(testFile));

        filesStorageService.delete("delete-test.txt");

        assertFalse(Files.exists(testFile));
    }

    @Test
    void testDeleteNonExistentFile() {
        // Returns false for non-existent files (doesn't throw exception)
        boolean result = filesStorageService.delete("non-existent.txt");
        assertFalse(result);
    }

    @Test
    void testLoadAll() throws IOException {
        // Create multiple test files
        Files.writeString(testUploadPath.resolve("file1.txt"), "Content 1");
        Files.writeString(testUploadPath.resolve("file2.txt"), "Content 2");
        Files.writeString(testUploadPath.resolve("file3.txt"), "Content 3");

        Stream<Path> paths = filesStorageService.loadAll();

        assertNotNull(paths);
        long count = paths.count();
        assertEquals(3, count);
    }

    @Test
    void testLoadAllEmptyDirectory() {
        Stream<Path> paths = filesStorageService.loadAll();

        assertNotNull(paths);
        long count = paths.count();
        assertEquals(0, count);
    }

    @Test
    void testSaveFileWithSpecialCharacters() {
        MultipartFile file = new MockMultipartFile(
                "file",
                "test file (1).txt",
                "text/plain",
                "Test content".getBytes()
        );

        assertDoesNotThrow(() -> filesStorageService.save(file));
    }

    @Test
    void testSaveMultipleFiles() {
        MultipartFile file1 = new MockMultipartFile(
                "file1",
                "test1.txt",
                "text/plain",
                "Content 1".getBytes()
        );

        MultipartFile file2 = new MockMultipartFile(
                "file2",
                "test2.txt",
                "text/plain",
                "Content 2".getBytes()
        );

        assertDoesNotThrow(() -> {
            filesStorageService.save(file1);
            filesStorageService.save(file2);
        });

        assertTrue(Files.exists(testUploadPath.resolve("test1.txt")));
        assertTrue(Files.exists(testUploadPath.resolve("test2.txt")));
    }

    @Test
    void testSaveOverwritesExistingFile() throws IOException {
        // Create initial file
        MultipartFile file1 = new MockMultipartFile(
                "file",
                "overwrite.txt",
                "text/plain",
                "Original content".getBytes()
        );
        filesStorageService.save(file1);

        // Overwrite with new file
        MultipartFile file2 = new MockMultipartFile(
                "file",
                "overwrite.txt",
                "text/plain",
                "New content".getBytes()
        );
        filesStorageService.save(file2);

        // Verify new content
        Path savedFile = testUploadPath.resolve("overwrite.txt");
        String content = Files.readString(savedFile);
        assertEquals("New content", content);
    }
}
