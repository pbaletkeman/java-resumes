package ca.letkeman.resumes;

import ca.letkeman.resumes.model.Optimize;
import ca.letkeman.resumes.optimizer.ApiService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BackgroundResume implements Runnable {

  private static Logger logger = LoggerFactory.getLogger(BackgroundResume.class);

  private Optimize optimize;

  public Optimize getOptimize() {
    // Return defensive copy to prevent external mutation
    if (optimize != null) {
      return new Optimize(
          optimize.getPromptType().clone(),
          optimize.getTemperature(),
          optimize.getModel(),
          optimize.getResume(),
          optimize.getJobDescription(),
          optimize.getJobTitle(),
          optimize.getCompany()
      );
    }
    return null;
  }

  private String endpoint;
  private String apikey;
  private String model;
  private String root;

  public void setOptimize(Optimize optimize) {
    // Defensive copy to prevent external mutation
    if (optimize != null) {
      this.optimize = new Optimize(
          optimize.getPromptType().clone(),
          optimize.getTemperature(),
          optimize.getModel(),
          optimize.getResume(),
          optimize.getJobDescription(),
          optimize.getJobTitle(),
          optimize.getCompany()
      );
    } else {
      this.optimize = null;
    }
  }

  public BackgroundResume(Optimize optimize, String root) {
    // Try to load config from app.config.path system property first
    String configPath = System.getProperty("app.config.path");
    String configStr;

    if (configPath != null && !configPath.isEmpty()) {
      configStr = Utility.readFileAsString(configPath, "config.json");
    } else {
      configStr = Utility.readFileAsString("config.json");
    }

    Config c = new Gson().fromJson(configStr, Config.class);
    // Defensive copy to prevent external mutation
    if (optimize != null) {
      this.optimize = new Optimize(
          optimize.getPromptType().clone(),
          optimize.getTemperature(),
          optimize.getModel(),
          optimize.getResume(),
          optimize.getJobDescription(),
          optimize.getJobTitle(),
          optimize.getCompany()
      );
    } else {
      this.optimize = null;
    }

    // Support environment variable overrides for LLM configuration
    // This allows flexibility across different deployment scenarios (Docker, local, cloud)
    String envEndpoint = System.getenv("LLM_ENDPOINT");
    String envApikey = System.getenv("LLM_APIKEY");
    String envModel = System.getenv("LLM_MODEL");

    this.endpoint = (envEndpoint != null && !envEndpoint.isEmpty()) ? envEndpoint : c.getEndpoint();
    this.apikey = (envApikey != null && !envApikey.isEmpty()) ? envApikey : c.getApikey();
    this.model = (envModel != null && !envModel.isEmpty()) ? envModel : c.getModel();
    this.root = root;
  }

  public BackgroundResume() {
  }

  @Override
  public void run() {
    ApiService apiService = new ApiService();
    apiService.produceFiles(optimize, endpoint, apikey, model, root);
    logger.info("all done");
  }
}
