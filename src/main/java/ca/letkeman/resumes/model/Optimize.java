package ca.letkeman.resumes.model;

import ca.letkeman.resumes.optimizer.ApiService.PROMPT_TYPE;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

  @Override
  public String toString() {
    return "Optimize{" + "promptType=" + promptType
        + ", temperature=" + temperature
        + ", model='" + model + '\''
        + ", resume='" + resume + '\''
        + ", jobDescription='" + jobDescription + '\''
        + ", jobTitle='" + jobTitle + '\''
        + ", company='" + company + '\''
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Optimize optimize = (Optimize) o;

    return new EqualsBuilder().append(getTemperature(), optimize.getTemperature())
        .append(getPromptType(), optimize.getPromptType()).append(getModel(), optimize.getModel())
        .append(getResume(), optimize.getResume()).append(getJobDescription(), optimize.getJobDescription())
        .append(getJobTitle(), optimize.getJobTitle()).append(getCompany(), optimize.getCompany()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(getPromptType()).append(getTemperature()).append(getModel())
        .append(getResume()).append(getJobDescription()).append(getJobTitle()).append(getCompany()).toHashCode();
  }

  /*
  check to see if properties are somewhat present and somewhat correct
   */
  public boolean isValid(){
    return getResume() != null && !getResume().isBlank() && !getResume().isEmpty() &&
        getJobDescription() != null && !getJobDescription().isBlank() && !getJobDescription().isEmpty() &&
        getTemperature() > 0 && getTemperature() < 2 &&
        getPromptType() != null &&
        getCompany() != null && !getCompany().isBlank() && !getCompany().isEmpty() &&
        getJobTitle() != null && !getJobTitle().isBlank() && !getJobTitle().isEmpty() &&
        getModel() != null && !getModel().isBlank() && !getModel().isEmpty();
  }
}
