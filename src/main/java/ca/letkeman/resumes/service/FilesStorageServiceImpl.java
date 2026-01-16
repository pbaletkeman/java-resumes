package ca.letkeman.resumes.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public final class FilesStorageServiceImpl implements FilesStorageService {

  private static final Logger LOGGER = LoggerFactory.getLogger(FilesStorageServiceImpl.class);

  private String configRoot;

  private Path root;

  @Override
  public void setConfigRoot(String root) {
    this.configRoot = root;
  }

  @Override
  public String getConfigRoot() {
    return configRoot;
  }


  @Override
  public void init(String configRoot) {
    try {
      root = Paths.get(configRoot);
      Files.createDirectories(root);
    } catch (IOException e) {
      LOGGER.error("Could not initialize folder for upload!");
    }
  }

  @Override
  public void save(MultipartFile file) {
    try {
      Files.copy(
          file.getInputStream(),
          this.root.resolve(Objects.requireNonNull(file.getOriginalFilename())),
          StandardCopyOption.REPLACE_EXISTING);
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
    }
  }

  @Override
  public Resource load(String filename) {
    try {
      Path file = root.resolve(filename);

      Resource resource = new UrlResource(file.toUri());

      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        LOGGER.error("Could not read the file!");
      }
    } catch (Exception e) {
      LOGGER.error("Error:\n{}", e.getMessage());
    }
    return null;
  }

  @Override
  public boolean delete(String filename) {
    try {
      Path file = root.resolve(filename);
      return Files.deleteIfExists(file);
    } catch (IOException e) {
      LOGGER.error("Error:\n{}", e.getMessage());
    }
    return false;
  }

  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(root.toFile());
  }

  @Override
  public Stream<Path> loadAll() {
    try {
      return Files.walk(this.root, 1)
          .filter(path -> !path.equals(this.root) && !Files.isDirectory(path))
          .map(this.root::relativize);
    } catch (IOException e) {
      LOGGER.error("Could not load the files!");
    }
    return Stream.empty();
  }
}
