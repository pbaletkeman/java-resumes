package ca.letkeman.resumes.controller;

import ca.letkeman.resumes.BackgroundResume;
import ca.letkeman.resumes.model.Optimize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicResumeController {

  @PostMapping(value = "/optimizer", consumes = "application/json", produces = "application/json")
  public String optimize(@RequestBody Optimize optimize, String somekey, String apikey, String root) {

    Thread thread = new Thread(new BackgroundResume(optimize, somekey, apikey, root));
    thread.start();

    return "Pete";
  }
}
