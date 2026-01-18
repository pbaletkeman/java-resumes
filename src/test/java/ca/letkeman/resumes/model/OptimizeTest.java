package ca.letkeman.resumes.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class OptimizeTest {
  @Test
  void test_valid_optimize_returns_true() {
    String[] promptType = {"Resume"};
    double temperature = 0.5;
    String model = "gemma-3-4b-it";
    String resume = "Professional resume content";
    String jobDescription = "Job description content";
    String jobTitle = "Software Engineer";
    String company = "Tech Company";

    Optimize optimize = new Optimize(promptType, temperature, model, resume, jobDescription, jobTitle, company);

    assertTrue(optimize.isValid());
  }

  @Test
  void test_null_resume_returns_false() {
    String[] promptType = {"Resume"};
    double temperature = 0.5;
    String model = "gemma-3-4b-it";
    String jobDescription = "Job description content";
    String jobTitle = "Software Engineer";
    String company = "Tech Company";

    Optimize optimize = new Optimize();
    optimize.setPromptType(promptType);
    optimize.setTemperature(temperature);
    optimize.setModel(model);
    optimize.setResume(null);
    optimize.setJobDescription(jobDescription);
    optimize.setJobTitle(jobTitle);
    optimize.setCompany(company);

    assertFalse(optimize.isValid());
  }

  @Test
  void test_null_description_returns_false() {
    String[] promptType = {"Resume"};
    double temperature = 0.5;
    String model = "gemma-3-4b-it";
    String resume = "Professional resume content";
    String jobTitle = "Software Engineer";
    String company = "Tech Company";

    Optimize optimize = new Optimize();
    optimize.setPromptType(promptType);
    optimize.setTemperature(temperature);
    optimize.setModel(model);
    optimize.setResume(resume);
    optimize.setJobDescription(null);
    optimize.setJobTitle(jobTitle);
    optimize.setCompany(company);

    assertFalse(optimize.isValid());
  }

  @Test
  void test_null_model_returns_false() {
    String[] promptType = {"Resume"};
    double temperature = 0.5;
    String resume = "Professional resume content";
    String jobTitle = "Software Engineer";
    String company = "Tech Company";
    String jobDescription = "Job description content";

    Optimize optimize = new Optimize();
    optimize.setPromptType(promptType);
    optimize.setTemperature(temperature);
    optimize.setModel(null);
    optimize.setResume(resume);
    optimize.setJobDescription(jobDescription);
    optimize.setJobTitle(jobTitle);
    optimize.setCompany(company);

    assertFalse(optimize.isValid());
  }
  /**/
  @Test
  void test_temperature_out_of_bounds_returns_false() {
    String[] promptType = {"Resume"};
    String model = "gemma-3-4b-it";
    String resume = "Professional resume content";
    String jobDescription = "Job description content";
    String jobTitle = "Software Engineer";
    String company = "Tech Company";

    Optimize optimize = new Optimize();
    optimize.setPromptType(promptType);

    optimize.setModel(model);
    optimize.setResume(resume);
    optimize.setJobDescription(jobDescription);
    optimize.setJobTitle(jobTitle);
    optimize.setCompany(company);

    optimize.setTemperature(-1);
    assertFalse(optimize.isValid());

    optimize.setTemperature(3);
    assertFalse(optimize.isValid());
  }

  @Test
  void test_null_jobTitle_returns_false() {
    String[] promptType = {"Resume"};
    double temperature = 0.5;
    String model = "gemma-3-4b-it";
    String resume = "Professional resume content";
    String jobTitle = null;
    String company = "Tech Company";
    String jobDescription = "Job description content";
    Optimize optimize = new Optimize();
    optimize.setPromptType(promptType);
    optimize.setTemperature(temperature);
    optimize.setModel(model);
    optimize.setResume(resume);
    optimize.setJobDescription(jobDescription);
    optimize.setJobTitle(jobTitle);
    optimize.setCompany(company);

    assertFalse(optimize.isValid());
  }

  @Test
  void test_null_company_returns_false() {
    String[] promptType = {"Resume"};
    double temperature = 0.5;
    String model = "gemma-3-4b-it";
    String resume = "Professional resume content";
    String jobTitle = "Software Engineer";
    String company = null;
    String jobDescription = "Job description content";
    Optimize optimize = new Optimize();
    optimize.setPromptType(promptType);
    optimize.setTemperature(temperature);
    optimize.setModel(model);
    optimize.setResume(resume);
    optimize.setJobDescription(jobDescription);
    optimize.setJobTitle(jobTitle);
    optimize.setCompany(company);

    assertFalse(optimize.isValid());
  }

  @Test
  void test_null_promptType_returns_false() {
    String[] promptType = null;
    double temperature = 0.5;
    String model = "gemma-3-4b-it";
    String resume = "Professional resume content";
    String jobTitle = "Software Engineer";
    String company = "Tech Company";
    String jobDescription = "Job description content";
    Optimize optimize = new Optimize();
    optimize.setPromptType(promptType);
    optimize.setTemperature(temperature);
    optimize.setModel(model);
    optimize.setResume(resume);
    optimize.setJobDescription(jobDescription);
    optimize.setJobTitle(jobTitle);
    optimize.setCompany(company);

    assertFalse(optimize.isValid());
  }

  @Test
  void test_promptType_empty_returns_false() {
    String[] promptType = {};
    double temperature = 0.5;
    String model = "gemma-3-4b-it";
    String resume = "Professional resume content";
    String jobTitle = "Software Engineer";
    String company = "Tech Company";
    String jobDescription = "Job description content";
    Optimize optimize = new Optimize();
    optimize.setPromptType(promptType);
    optimize.setTemperature(temperature);
    optimize.setModel(model);
    optimize.setResume(resume);
    optimize.setJobDescription(jobDescription);
    optimize.setJobTitle(jobTitle);
    optimize.setCompany(company);

    assertFalse(optimize.isValid());
  }

  @Test
  void test_promptType_out_of_bounds_returns_false() {
    String[] promptType = {"Resume1223"};
    double temperature = 0.5;
    String model = "gemma-3-4b-it";
    String resume = "Professional resume content";
    String jobTitle = "Software Engineer";
    String company = "Tech Company";
    String jobDescription = "Job description content";
    Optimize optimize = new Optimize();
    optimize.setPromptType(promptType);
    optimize.setTemperature(temperature);
    optimize.setModel(model);
    optimize.setResume(resume);
    optimize.setJobDescription(jobDescription);
    optimize.setJobTitle(jobTitle);
    optimize.setCompany(company);

    assertFalse(optimize.isValid());
  }

  @Test
  void test_valid_skills_prompt_without_resume() {
    String[] promptType = {"SKILLS"};
    double temperature = 0.5;
    String model = "gemma-3-4b-it";
    String jobDescription = "Java Spring Developer position requiring 5+ years";
    String jobTitle = "Java Spring Developer";
    String company = "Tech Company";

    Optimize optimize = new Optimize();
    optimize.setPromptType(promptType);
    optimize.setTemperature(temperature);
    optimize.setModel(model);
    optimize.setResume(null);  // SKILLS prompt doesn't require resume
    optimize.setJobDescription(jobDescription);
    optimize.setJobTitle(jobTitle);
    optimize.setCompany(company);

    assertTrue(optimize.isValid(), "SKILLS prompt should be valid without resume content");
  }

  @Test
  void test_skills_prompt_type_recognition() {
    String[] promptType = {"SKILLS"};
    Optimize optimize = new Optimize();
    optimize.setPromptType(promptType);

    assertTrue(optimize.isSkillsPrompt(), "Should recognize SKILLS prompt type");
    assertFalse(optimize.hasResumeOrCoverPrompt(), "Should not be Resume or Cover prompt");
  }

  @Test
  void test_resume_prompt_type_recognition() {
    String[] promptType = {"Resume"};
    Optimize optimize = new Optimize();
    optimize.setPromptType(promptType);

    assertFalse(optimize.isSkillsPrompt(), "Should not be SKILLS prompt");
    assertTrue(optimize.hasResumeOrCoverPrompt(), "Should be Resume or Cover prompt");
  }

  @Test
  void test_valid_prompt_type_includes_skills() {
    String[] promptType = {"SKILLS"};
    Optimize optimize = new Optimize();
    optimize.setPromptType(promptType);

    assertTrue(optimize.isValidPromptType(), "SKILLS should be a valid prompt type");
  }

  @Test
  void test_invalid_prompt_type() {
    String[] promptType = {"INVALID"};
    Optimize optimize = new Optimize();
    optimize.setPromptType(promptType);

    assertFalse(optimize.isValidPromptType(), "INVALID should not be a valid prompt type");
  }

  @Test
  void test_skills_prompt_requires_job_description() {
    String[] promptType = {"SKILLS"};
    double temperature = 0.5;
    String model = "gemma-3-4b-it";
    String jobTitle = "Java Developer";
    String company = "Tech Company";

    Optimize optimize = new Optimize();
    optimize.setPromptType(promptType);
    optimize.setTemperature(temperature);
    optimize.setModel(model);
    optimize.setJobDescription(null);  // Missing required field
    optimize.setJobTitle(jobTitle);
    optimize.setCompany(company);

    assertFalse(optimize.isValid(), "SKILLS prompt should require job description");
  }

  @Test
  void test_case_insensitive_prompt_type_matching() {
    // Test lowercase skills
    String[] promptTypeSkillsLower = {"skills"};
    Optimize optimizeLower = new Optimize();
    optimizeLower.setPromptType(promptTypeSkillsLower);
    assertTrue(optimizeLower.isSkillsPrompt(), "Should recognize 'skills' in lowercase");

    // Test mixed case
    String[] promptTypeSkillsMixed = {"Skills"};
    Optimize optimizeMixed = new Optimize();
    optimizeMixed.setPromptType(promptTypeSkillsMixed);
    assertTrue(optimizeMixed.isSkillsPrompt(), "Should recognize 'Skills' in mixed case");
  }
}
