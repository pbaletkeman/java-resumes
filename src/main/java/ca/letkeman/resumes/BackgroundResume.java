package ca.letkeman.resumes;

import ca.letkeman.resumes.model.Optimize;
import ca.letkeman.resumes.optimizer.ApiService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BackgroundResume implements Runnable {

  private static Logger LOGGER = LoggerFactory.getLogger(BackgroundResume.class);

  private Optimize optimize;

  public Optimize getOptimize() {
    return optimize;
  }

  private String endpoint;
  private String apikey;
  private String root;

  public void setOptimize(Optimize optimize) {
    this.optimize = optimize;
  }

  public BackgroundResume(Optimize optimize, String root) {
    String configStr = Utility.readFileAsString("config.json");
    Config c = new Gson().fromJson(configStr, Config.class);
    this.optimize = optimize;
    this.endpoint = c.getEndpoint();
    this.apikey = c.getApikey();
    this.root = root;
  }

  public BackgroundResume() {
  }

  @Override
  public void run() {
    ApiService apiService = new ApiService();
    apiService.produceFiles(optimize, endpoint, apikey, root);
    LOGGER.info("all done");
  }
}
