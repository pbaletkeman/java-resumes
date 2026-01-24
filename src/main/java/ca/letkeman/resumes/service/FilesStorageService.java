package ca.letkeman.resumes.service;

import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
  void init(String configRoot);

  void save(MultipartFile file);

  Resource load(String filename);

  boolean delete(String filename);

  void deleteAll();

  Stream<Path> loadAll();

  String getConfigRoot();

  void setConfigRoot(String root);
}
