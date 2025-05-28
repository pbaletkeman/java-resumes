package ca.letkeman.resumes;

import ca.letkeman.resumes.model.Optimize;
import ca.letkeman.resumes.optimizer.ApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackgroundResume implements Runnable {

  private static Logger LOGGER = LoggerFactory.getLogger(BackgroundResume.class);

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
    apiService.produceFiles(optimize.getPromptType(), optimize.getTemperature(), optimize.getModel(), optimize.getResume(), optimize.getJobDescription(), optimize.getJobTitle(), optimize.getCompany());
    LOGGER.info("all done");
  }
}
