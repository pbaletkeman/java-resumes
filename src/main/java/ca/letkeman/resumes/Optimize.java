package ca.letkeman.resumes;

import ca.letkeman.resumes.optimizer.ApiService.PROMPT_TYPE;

public class Optimize {
  PROMPT_TYPE promptType = PROMPT_TYPE.RESUME;
  double temperature = 0.15;
  String model = "gemma-3-4b-it";
  String resume;
  String jobDescription;
  String jobTitle;
  String company;

  public Optimize(PROMPT_TYPE promptType, double temperature, String model, String resume, String jobDescription,
      String jobTitle, String company) {
    this.promptType = promptType;
    this.temperature = temperature;
    this.model = model;
    this.resume = resume;
    this.jobDescription = jobDescription;
    this.jobTitle = jobTitle;
    this.company = company;
  }

  public Optimize() {
  }

  public PROMPT_TYPE getPromptType() {
    return promptType;
  }

  public void setPromptType(PROMPT_TYPE promptType) {
    this.promptType = promptType;
  }

  public double getTemperature() {
    return temperature;
  }

  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getResume() {
    return resume;
  }

  public void setResume(String resume) {
    this.resume = resume;
  }

  public String getJobDescription() {
    return jobDescription;
  }

  public void setJobDescription(String jobDescription) {
    this.jobDescription = jobDescription;
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }
}
