package ca.letkeman.resumes;

import ca.letkeman.resumes.model.Optimize;
import ca.letkeman.resumes.optimizer.ApiService;

public class BackgroundResume implements Runnable {
  private Optimize optimize;

  public Optimize getOptimize() {
    return optimize;
  }

  public void setOptimize(Optimize optimize) {
    this.optimize = optimize;
  }

  public BackgroundResume(Optimize optimize) {
    this.optimize = optimize;
  }

  public BackgroundResume() {
  }

  @Override
  public void run() {

    ApiService apiService = new ApiService();
    apiService.produceFiles(optimize.getPromptType().name(), optimize.getResume(), optimize.getJobDescription(), optimize.getJobTitle(), optimize.getCompany());

  }
}
