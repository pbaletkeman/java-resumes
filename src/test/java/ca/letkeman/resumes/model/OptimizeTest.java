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
}