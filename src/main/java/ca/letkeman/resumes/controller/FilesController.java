package ca.letkeman.resumes.controller;

import ca.letkeman.resumes.model.Optimize;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import ca.letkeman.resumes.message.ResponseMessage;
import ca.letkeman.resumes.model.FileInfo;
import ca.letkeman.resumes.service.FilesStorageService;

@RestController
public class FilesController {

  private final FilesStorageService storageService;

  @Autowired
  public FilesController( FilesStorageService storageService){
    this.storageService = storageService;
  }

  @PostMapping(path = "/upload")
  public ResponseEntity<ResponseMessage> uploadFile(
      @RequestParam(name = "resume", required = false) MultipartFile resume,
      @RequestParam(name = "job", required = false) MultipartFile job) {
    String resumeMessage = "";
    String jobMessage = "";
    boolean uploadGood = true;
    if (resume != null) {
      try {
        storageService.save(resume);
        resumeMessage = "Uploaded the resume successfully: " + resume.getOriginalFilename();
      } catch (Exception e) {
        resumeMessage = "Could not upload the resume: " + resume.getOriginalFilename() + ". Error: " + e.getMessage();
        uploadGood = false;
      }
    }
    if (job != null) {
      if (uploadGood) {
        try {
          storageService.save(job);
          jobMessage = "Uploaded the job successfully: " + job.getOriginalFilename();
        } catch (Exception e) {
          jobMessage = "Could not upload the job: " + job.getOriginalFilename() + ". Error: " + e.getMessage();
          uploadGood = false;
        }
      }
    }
    if (uploadGood){
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(resumeMessage + "\n" + jobMessage));
    } else {
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(resumeMessage + "\n" + jobMessage));
    }
  }

  @GetMapping("/files")
  public ResponseEntity<List<FileInfo>> getListFiles() {
    List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
      String filename = path.getFileName().toString();
      String url = MvcUriComponentsBuilder
          .fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();

      return new FileInfo(filename, url);
    }).toList();

    return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
  }

  @GetMapping("/files/{filename:.+}")
  public ResponseEntity<Resource> getFile(@PathVariable String filename) {
    Resource file = storageService.load(filename);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  @DeleteMapping("/files/{filename:.+}")
  public ResponseEntity<ResponseMessage> deleteFile(@PathVariable String filename) {
    String message = "";

    try {
      boolean existed = storageService.delete(filename);

      if (existed) {
        message = "Delete the file successfully: " + filename;
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
      }

      message = "The file does not exist!";
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
    } catch (Exception e) {
      message = "Could not delete the file: " + filename + ". Error: " + e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
    }
  }
}
