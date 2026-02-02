package ca.letkeman.resumes.controller;

import ca.letkeman.resumes.BackgroundResume;
import ca.letkeman.resumes.Utility;
import ca.letkeman.resumes.message.ResponseMessage;
import ca.letkeman.resumes.model.FileInfo;
import ca.letkeman.resumes.model.Optimize;
import ca.letkeman.resumes.optimizer.HtmlToPdf;
import ca.letkeman.resumes.optimizer.MarkdownToDocx;
import ca.letkeman.resumes.service.FilesStorageService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:80",
    "http://127.0.0.1:3000", "http://127.0.0.1:5173", "http://127.0.0.1:80"},
    allowCredentials = "true",
    maxAge = 3600,
    methods = {org.springframework.web.bind.annotation.RequestMethod.GET,
        org.springframework.web.bind.annotation.RequestMethod.POST,
        org.springframework.web.bind.annotation.RequestMethod.DELETE,
        org.springframework.web.bind.annotation.RequestMethod.OPTIONS})
@RestController
@RequestMapping("/api")
public final class ResumeController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ResumeController.class);

  private final FilesStorageService storageService;

  @Value("${upload.path}")
  private String root;


  @SuppressWarnings("EI_EXPOSE_REP2")
  public ResumeController(FilesStorageService storageService) {
    this.storageService = storageService;
  }

  @PostMapping(path = "/markdownFile2PDF")
  public ResponseEntity<ResponseMessage> file2PDF(@RequestParam(name = "file", required = false) MultipartFile file) {
    if (file == null || file.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("No file/invalid file provided"));
    }
    try {
      storageService.setConfigRoot(root);
      storageService.save(file);
      String outputFile = root + File.separator
          + Utility.removeFileExtension(file.getOriginalFilename(), true) + ".pdf";
      HtmlToPdf htmlToPdf = new HtmlToPdf(
          root + File.separator + file.getOriginalFilename(),
          outputFile, "");
      if (htmlToPdf.convertFile()) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(new ResponseMessage("file successfully converted"));
      } else {
        return ResponseEntity.status(HttpStatus.OK)
            .body(new ResponseMessage("unable to convert file"));
      }
    } catch (Exception e) {
      LOGGER.error("Could not upload the resume: {}. Error:\n{}",
          file.getOriginalFilename(), e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ResponseMessage("problem with conversion"));
    }
  }

  @PostMapping(path = "/markdownFile2DOCX")
  public ResponseEntity<ResponseMessage> file2DOCX(@RequestParam(name = "file", required = false) MultipartFile file) {
    if (file == null || file.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("No file/invalid file provided"));
    }
    try {
      storageService.setConfigRoot(root);
      storageService.save(file);
      String outputFile = root + File.separator
          + Utility.removeFileExtension(file.getOriginalFilename(), true) + ".docx";
      MarkdownToDocx mdToDocx = new MarkdownToDocx(
          root + File.separator + file.getOriginalFilename(),
          outputFile, "");
      if (mdToDocx.convertFile()) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(new ResponseMessage("file successfully converted"));
      } else {
        return ResponseEntity.status(HttpStatus.OK)
            .body(new ResponseMessage("unable to convert file"));
      }
    } catch (Exception e) {
      LOGGER.error("Could not upload the file: {}. Error:\n{}",
          file.getOriginalFilename(), e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ResponseMessage("problem with conversion"));
    }
  }

  @PostMapping(path = "/process/cover-letter")
  public ResponseEntity<ResponseMessage> processCoverLetter(
      @RequestParam(name = "coverLetter", required = false) MultipartFile coverLetter,
      @RequestParam(name = "job", required = false) MultipartFile job,
      @RequestParam(name = "optimize", required = false) String opt) {
    return optimizeResume(coverLetter, job, opt);
  }

  @PostMapping(path = "/process/resume")
  public ResponseEntity<ResponseMessage> processResume(
      @RequestParam(name = "resume", required = false) MultipartFile resume,
      @RequestParam(name = "job", required = false) MultipartFile job,
      @RequestParam(name = "optimize", required = false) String opt) {
    return optimizeResume(resume, job, opt);
  }

  @PostMapping(path = "/upload")
  public ResponseEntity<ResponseMessage> optimizeResume(
      @RequestParam(name = "resume", required = false) MultipartFile resume,
      @RequestParam(name = "job", required = false) MultipartFile job,
      @RequestParam(name = "optimize", required = false) String opt) {
    Optimize optimize = null;
    if (opt != null && !opt.isBlank()) {
      try {
        optimize = new Gson().fromJson(opt, Optimize.class);
      } catch (JsonSyntaxException e) {
        LOGGER.error("Invalid optimize JSON: {}", opt);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("invalid optimize parameter"));
      }
    } else {
      optimize = new Optimize();
    }

    if ((optimize.getResume() == null || optimize.getResume().isBlank()) && resume != null) {
      try {
        storageService.setConfigRoot(root);
        storageService.save(resume);
        optimize.setResume(Utility.readFileAsString(root + File.separator + resume.getOriginalFilename()));
      } catch (Exception e) {
        LOGGER.error("Could not upload the resume: {}. Error:\n{}", resume.getOriginalFilename(), e.getMessage());
      }
    }

    if ((optimize.getJobDescription() == null || optimize.getJobDescription().isBlank()) && job != null) {
      try {
        storageService.setConfigRoot(root);
        storageService.save(job);
        optimize.setJobDescription(
            Utility.readFileAsString(root + File.separator + job.getOriginalFilename()));
      } catch (Exception e) {
        LOGGER.error("Could not upload the job: {}. Error:\n{}", job.getOriginalFilename(), e.getMessage());
      }
    }
    if (optimize.getJobDescription() != null) {
      optimize.setJobDescription(Utility.convertLineEndings(optimize.getJobDescription()));
    }
    if (optimize.getResume() != null) {
      optimize.setResume(Utility.convertLineEndings(optimize.getResume()));
    }
    LOGGER.debug("optimize: {}", optimize);
    if (optimize.isValid()) {
      // start background task here
      Thread thread = new Thread(new BackgroundResume(optimize,  root));
      thread.start();
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("generating"));
    } else {
      LOGGER.warn("Validation failed");
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
          .body(new ResponseMessage("Required property missing or invalid."));
    }
  }

  @PostMapping(path = "/process/skills")
  public ResponseEntity<ResponseMessage> processSkills(
      @RequestParam(name = "job", required = false) MultipartFile job,
      @RequestParam(name = "optimize", required = false) String opt) {
    Optimize optimize = null;
    if (opt != null && !opt.isBlank()) {
      try {
        optimize = new Gson().fromJson(opt, Optimize.class);
      } catch (JsonSyntaxException e) {
        LOGGER.error("Invalid optimize JSON: {}", opt);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("invalid optimize parameter"));
      }
    } else {
      optimize = new Optimize();
    }

    if ((optimize.getJobDescription() == null || optimize.getJobDescription().isBlank()) && job != null) {
      try {
        storageService.setConfigRoot(root);
        storageService.save(job);
        optimize.setJobDescription(
            Utility.readFileAsString(root + File.separator + job.getOriginalFilename()));
      } catch (Exception e) {
        LOGGER.error("Could not upload the job: {}. Error:\n{}", job.getOriginalFilename(), e.getMessage());
      }
    }

    if (optimize.getJobDescription() != null && !optimize.getJobDescription().isBlank()) {
      try {
        BackgroundResume bg = new BackgroundResume(optimize, root);
        Thread t = new Thread(bg);
        t.start();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Skills suggestion generation started"));
      } catch (Exception e) {
        LOGGER.error("Error processing skills: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ResponseMessage("Error processing skills"));
      }
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("job description is required"));
    }
  }

  @GetMapping("/files")
  public ResponseEntity<List<FileInfo>> getListFiles() {
    storageService.setConfigRoot(root);
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    List<FileInfo> fileInfos = new java.util.ArrayList<>(storageService.loadAll().map(path -> {
      String date = getFileDate(path);
      String filename = path.getFileName().toString();
      String url = MvcUriComponentsBuilder
          .fromMethodName(ResumeController.class, "getFile", path.getFileName().toString()).build().toString();
      return new FileInfo(filename, url, root, date);
    }).toList());
    if (!fileInfos.isEmpty()) {
      fileInfos.sort(Comparator.comparing(o -> LocalDateTime.parse(o.getDate(), formatter)));
      Collections.reverse(fileInfos);
    }
    return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
  }

  private String getFileDate(Path path) {
    try {
      String p = root + File.separator + path.toString();
      LocalDateTime x = LocalDateTime.ofInstant(
          Files.getLastModifiedTime(Path.of(p)).toInstant(),
          ZoneId.systemDefault());
      return x.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    } catch (Exception e) {
      LOGGER.error(e.toString());
    }
    return "";
  }

  @GetMapping("/files/{filename:.+}")
  public ResponseEntity<Resource> getFile(@PathVariable(name = "filename") String filename) {
    try {
      // URL decode the filename to handle encoded special characters and spaces
      String decodedFilename = java.net.URLDecoder.decode(filename, "UTF-8");
      storageService.setConfigRoot(root);
      Resource file = storageService.load(decodedFilename);
      if (file == null || !file.exists()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    } catch (Exception e) {
      LOGGER.error("Error retrieving file: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @DeleteMapping("/files/{filename:.+}")
  public ResponseEntity<ResponseMessage> deleteFile(@PathVariable(name = "filename") String filename) {
    String message = "";
    try {
      // URL decode the filename to handle encoded special characters and spaces
      String decodedFilename = java.net.URLDecoder.decode(filename, "UTF-8");
      storageService.setConfigRoot(root);
      boolean existed = storageService.delete(decodedFilename);
      if (existed) {
        message = "Delete the file successfully: " + decodedFilename;
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
      }
      message = "The file does not exist!";
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
    } catch (Exception e) {
      message = "Could not delete the file: " + filename + ". Error: " + e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
    }
  }

  @PostMapping(path = "/generate/interview-hr-questions")
  public ResponseEntity<ResponseMessage> generateInterviewHrQuestions(
      @RequestParam(name = "job", required = false) MultipartFile job,
      @RequestParam(name = "optimize", required = false) String opt) {
    return processPromptRequest(job, opt, "interview-hr-questions");
  }

  @PostMapping(path = "/generate/interview-job-specific")
  public ResponseEntity<ResponseMessage> generateInterviewJobSpecific(
      @RequestParam(name = "job", required = false) MultipartFile job,
      @RequestParam(name = "optimize", required = false) String opt) {
    return processPromptRequest(job, opt, "interview-job-specific");
  }

  @PostMapping(path = "/generate/interview-reverse")
  public ResponseEntity<ResponseMessage> generateInterviewReverse(
      @RequestParam(name = "job", required = false) MultipartFile job,
      @RequestParam(name = "optimize", required = false) String opt) {
    return processPromptRequest(job, opt, "interview-reverse");
  }

  @PostMapping(path = "/generate/cold-email")
  public ResponseEntity<ResponseMessage> generateColdEmail(
      @RequestParam(name = "job", required = false) MultipartFile job,
      @RequestParam(name = "optimize", required = false) String opt) {
    return processPromptRequest(job, opt, "cold-email");
  }

  @PostMapping(path = "/generate/cold-linkedin-message")
  public ResponseEntity<ResponseMessage> generateColdLinkedInMessage(
      @RequestParam(name = "job", required = false) MultipartFile job,
      @RequestParam(name = "optimize", required = false) String opt) {
    return processPromptRequest(job, opt, "cold-linkedin-message");
  }

  @PostMapping(path = "/generate/thank-you-email")
  public ResponseEntity<ResponseMessage> generateThankYouEmail(
      @RequestParam(name = "job", required = false) MultipartFile job,
      @RequestParam(name = "optimize", required = false) String opt) {
    return processPromptRequest(job, opt, "thank-you-email");
  }

  private ResponseEntity<ResponseMessage> processPromptRequest(
      MultipartFile job, String opt, String promptType) {
    Optimize optimize = null;
    if (opt != null && !opt.isBlank()) {
      try {
        optimize = new Gson().fromJson(opt, Optimize.class);
      } catch (JsonSyntaxException e) {
        LOGGER.error("Invalid optimize JSON: {}", opt);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ResponseMessage("invalid optimize parameter"));
      }
    } else {
      optimize = new Optimize();
    }

    // Set the prompt type
    optimize.setPromptType(new String[]{promptType});

    // Handle job description file upload
    if ((optimize.getJobDescription() == null || optimize.getJobDescription().isBlank()) && job != null) {
      try {
        storageService.setConfigRoot(root);
        storageService.save(job);
        optimize.setJobDescription(
            Utility.readFileAsString(root + File.separator + job.getOriginalFilename()));
      } catch (Exception e) {
        LOGGER.error("Could not upload the job: {}. Error:\n{}", job.getOriginalFilename(), e.getMessage());
      }
    }

    if (optimize.getJobDescription() != null) {
      optimize.setJobDescription(Utility.convertLineEndings(optimize.getJobDescription()));
    }

    LOGGER.debug("Prompt type: {}, optimize: {}", promptType, optimize);

    if (optimize.isValid()) {
      // Start background task
      Thread thread = new Thread(new BackgroundResume(optimize, root));
      thread.start();
      return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseMessage("generating"));
    } else {
      LOGGER.warn("Validation failed for prompt type: {}", promptType);
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
          .body(new ResponseMessage("Required property missing or invalid."));
    }
  }

  @GetMapping("/health")
  public ResponseEntity<Map<String, Object>> healthCheck() {
    Map<String, Object> health = new HashMap<>();
    health.put("status", "UP");
    health.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
    health.put("service", "Resume Optimization API");
    return ResponseEntity.ok(health);
  }
}
