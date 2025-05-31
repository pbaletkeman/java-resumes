package ca.letkeman.resumes.controller;

import ca.letkeman.resumes.BackgroundResume;
import ca.letkeman.resumes.Utility;
import ca.letkeman.resumes.model.Optimize;

import ca.letkeman.resumes.optimizer.HtmlToPdf;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

@CrossOrigin(origins = "*")
@RestController
public class AdvancedController {

  private static final Logger LOGGER = LoggerFactory.getLogger(AdvancedController.class);

  private final FilesStorageService storageService;

  @Value("${upload.path}")
  private String root;

  @Value("${llm.endpoint}")
  private String endpoint;

  @Value("${llm.apikey}")
  private String apikey;

  @Autowired
  public AdvancedController( FilesStorageService storageService){
    this.storageService = storageService;

  }

  @PostMapping(path = "/markdownFile2PDF")
  public ResponseEntity<ResponseMessage> file2PDF(@RequestParam(name = "file", required = false) MultipartFile file){
    try {
      storageService.setConfigRoot(root);
      storageService.save(file);
      String outputFile = root + File.separator + Utility.removeFileExtension( file.getOriginalFilename() , true) + ".pdf";
      HtmlToPdf htmlToPdf = new HtmlToPdf(root + File.separator + file.getOriginalFilename(),outputFile, "");
      if (htmlToPdf.convertFile()){
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("file successfully converted"));
      } else {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("unable to convert file"));
      }
    } catch (Exception e) {
      LOGGER.error("Could not upload the resume: {}. Error:\n{}", file.getOriginalFilename(), e.getMessage());
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("problem with conversion"));
  }

  @PostMapping(path = "/upload")
  public ResponseEntity<ResponseMessage> optimizeResume(
      @RequestParam(name = "resume", required = false) MultipartFile resume,
      @RequestParam(name = "job", required = false) MultipartFile job,
      @RequestParam(name="optimize", required = false) String opt) {
    Optimize optimize = new Gson().fromJson(opt, Optimize.class);

    if ((optimize.getResume() == null || optimize.getResume().isBlank() || optimize.getResume().isEmpty())
        && resume != null) {
      try {
        storageService.setConfigRoot(root);
        storageService.save(resume);
        optimize.setResume(Utility.readFileAsString(root + File.separator + resume.getOriginalFilename()));
      } catch (Exception e) {
        LOGGER.error("Could not upload the resume: {}. Error:\n{}", resume.getOriginalFilename(), e.getMessage());
      }
    }

    if ((optimize.getJobDescription() == null || optimize.getJobDescription().isBlank() || optimize.getJobDescription().isEmpty()) && job != null) {
      try {
          storageService.setConfigRoot(root);
          storageService.save(job);
          optimize.setJobDescription(Utility.readFileAsString(root + File.separator + job.getOriginalFilename()));
        } catch (Exception e) {
          LOGGER.error("Could not upload the job: {}. Error:\n{}", job.getOriginalFilename(),e.getMessage());
        }
    }
    optimize.setJobDescription(Utility.convertLineEndings(optimize.getJobDescription()));
    optimize.setResume(Utility.convertLineEndings(optimize.getResume()));
    LOGGER.info("optimize: {}",optimize);
    if (optimize.isValid()){
      // start background task here
      Thread thread = new Thread(new BackgroundResume(optimize, endpoint, apikey, root));
      thread.start();
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("generating"));
    } else {
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage("Required property missing or invalid."));
    }
  }

  @GetMapping("/get-files")
  public ResponseEntity<List<FileInfo>> getListFiles() {
    storageService.setConfigRoot(root);
    List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
      String filename = path.getFileName().toString();
        String url = MvcUriComponentsBuilder
            .fromMethodName(AdvancedController.class, "getFile", path.getFileName().toString()).build().toString();

        return new FileInfo(filename, url, root);
    }).toList();

    return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
  }

  @GetMapping("/files/{filename:.+}")
  public ResponseEntity<Resource> getFile(@PathVariable String filename) {
    storageService.setConfigRoot(root);
    Resource file = storageService.load(filename);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  @DeleteMapping("/files/{filename:.+}")
  public ResponseEntity<ResponseMessage> deleteFile(@PathVariable String filename) {
    String message = "";

    try {
      storageService.setConfigRoot(root);
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
