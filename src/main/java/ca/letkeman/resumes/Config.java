package ca.letkeman.resumes;

public   class Config {
  private String endpoint;
  private String apikey;
  private String model;

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public String getApikey() {
    return apikey;
  }

  public void setApikey(String apikey) {
    this.apikey = apikey;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public Config(String endpoint, String apikey, String model) {
    this.endpoint = endpoint;
    this.apikey = apikey;
    this.model = model;
  }

  public Config() {
  }
}
