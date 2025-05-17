package ca.letkeman.resumes;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.letkeman.resumes.optimizer.ApiService;

@RestController
public class ResumeController {

  @PostMapping(
      value = "/optimizer", consumes = "application/json", produces = "application/json")
  public String optimize(@RequestBody Optimize optimize) {

//    String resume = readFileAsString("sample" + File.separator + "resume.md");
//    String jobDescription = readFileAsString("sample" + File.separator + "PointClickCare-Software Engineer.txt");

    ApiService apiService = new ApiService();
    apiService.produceFiles(optimize.promptType.name(), optimize.resume, optimize.jobDescription, optimize.jobTitle, optimize.company);

    return "Pete";
  }
}
