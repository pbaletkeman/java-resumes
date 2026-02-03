package ca.letkeman.resumes.model;

import ca.letkeman.resumes.Utility;
import java.util.Arrays;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class Optimize {
  private static final Set<String> VALID_PROMPT_TYPES = Set.of(
      "resume", "cover", "skills",
      "interview-hr-questions", "interview-job-specific", "interview-reverse",
      "cold-email", "cold-linkedin-message", "thank-you-email"
  );

  String[] promptType = {"Resume"};
  double temperature = 0.15;
  String model = "gemma-3-4b-it";
  String resume_string;
  String jobDescription;
  String jobTitle;
  String company_name;
  String interviewerName;

  public  Optimize(String[] promptType, double temperature, String model, String resume_string, String jobDescription,
      String jobTitle, String company_name) {
    this.promptType = promptType != null ? promptType.clone() : new String[]{"Resume"};
    this.temperature = temperature;
    this.model = model;
    this.resume_string = Utility.convertLineEndings(resume_string);
    this.jobDescription = Utility.convertLineEndings(jobDescription);
    this.jobTitle = jobTitle;
    this.company_name = company_name;
    this.interviewerName = null;
  }

  public Optimize() {
  }

  public String[] getPromptType() {
    return promptType != null ? promptType.clone() : new String[]{"Resume"};
  }

  public void setPromptType(String[] promptType) {
    this.promptType = promptType != null ? promptType.clone() : new String[]{"Resume"};
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
    return resume_string;
  }

  public void setResume(String resume_string) {
    this.resume_string = Utility.convertLineEndings(resume_string);
  }

  public String getJobDescription() {
    return jobDescription;
  }

  public void setJobDescription(String jobDescription) {
    this.jobDescription = Utility.convertLineEndings(jobDescription);
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  public String getCompany() {
    return company_name;
  }

  public void setCompany(String company_name) {
    this.company_name = company_name;
  }

  public String getInterviewerName() {
    return interviewerName;
  }

  public void setInterviewerName(String interviewerName) {
    this.interviewerName = interviewerName;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Optimize{");
    sb.append("promptType=").append(Arrays.toString(promptType));
    sb.append(", temperature=").append(temperature);
    sb.append(", model='").append(model).append('\'');
    sb.append(", resume_string='").append(resume_string).append('\'');
    sb.append(", jobDescription='").append(jobDescription).append('\'');
    sb.append(", jobTitle='").append(jobTitle).append('\'');
    sb.append(", company_name='").append(company_name).append('\'');
    sb.append(", interviewerName='").append(interviewerName).append('\'');
    sb.append('}');
    return sb.toString();
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
        .append(getJobTitle(), optimize.getJobTitle()).append(getCompany(), optimize.getCompany())
        .append(getInterviewerName(), optimize.getInterviewerName()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(getPromptType()).append(getTemperature()).append(getModel())
        .append(getResume()).append(getJobDescription()).append(getJobTitle()).append(getCompany())
        .append(getInterviewerName()).toHashCode();
  }

  /*
  check to see if properties are present and somewhat correct
   */
  public boolean isValid() {
    return getJobDescription() != null && !getJobDescription().isBlank()
        && !getJobDescription().isEmpty()
        && !getJobDescription().equals("null") && getTemperature() > 0
        && getTemperature() < 2
        && getPromptType() != null && getPromptType().length > 0
        && getPromptType().length < 3 && isValidPromptType()
        && getCompany() != null && !getCompany().isBlank() && !getCompany().isEmpty()
        && getJobTitle() != null && !getJobTitle().isBlank()
        && !getJobTitle().isEmpty() && !getJobTitle().equals("null")
        && getModel() != null && !getModel().isBlank() && !getModel().isEmpty()
        && !getModel().equals("null")
        && (isSkillsPrompt() || isInterviewOrNetworkingPrompt()
            || getResume() != null && !getResume().isBlank() && !getResume().isEmpty()
                && !getResume().equals("null"));
  }

  public boolean hasResumeOrCoverPrompt() {
    return Arrays.stream(getPromptType())
            .anyMatch(x -> x.equalsIgnoreCase("resume"))
        || Arrays.stream(getPromptType())
            .anyMatch(x -> x.equalsIgnoreCase("cover"));
  }

  public boolean isSkillsPrompt() {
    return Arrays.stream(getPromptType())
        .anyMatch(x -> x.equalsIgnoreCase("skills"));
  }

  public boolean isInterviewOrNetworkingPrompt() {
    return Arrays.stream(getPromptType())
        .anyMatch(x -> x.equalsIgnoreCase("interview-hr-questions")
            || x.equalsIgnoreCase("interview-job-specific")
            || x.equalsIgnoreCase("interview-reverse")
            || x.equalsIgnoreCase("cold-email")
            || x.equalsIgnoreCase("cold-linkedin-message")
            || x.equalsIgnoreCase("thank-you-email"));
  }

  public boolean isValidPromptType() {
    return Arrays.stream(getPromptType())
        .allMatch(x -> VALID_PROMPT_TYPES.contains(x.toLowerCase()));
  }
}
