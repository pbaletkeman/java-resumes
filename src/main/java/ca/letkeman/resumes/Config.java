package ca.letkeman.resumes;

public   class Config {
  private String endpoint;
  private String apikey;

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

  public Config(String endpoint, String apikey) {
    this.endpoint = endpoint;
    this.apikey = apikey;
  }

  public Config() {
  }
}
