package ca.letkeman.resumes.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OptimizeTest {
  @Test
  void testValidOptimizeReturnsTrue() {
    String[] promptType = {"Resume"};
    double temperature = 0.5;
    String model = "gemma-3-4b-it";
    String resume = "Professional resume content";
    String jobDescription = "Job description content";
    String jobTitle = "Software Engineer";
    String company = "Tech Company";

    Optimize optimize = new Optimize(promptType, temperature, model, resume, jobDescription, jobTitle, company);

    Assertions.assertTrue(optimize.isValid());
  }

  @Test
  void testNullResumeReturnsFalse() {
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

    Assertions.assertFalse(optimize.isValid());
  }

  @Test
  void testNullDescriptionReturnsFalse() {
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

    Assertions.assertFalse(optimize.isValid());
  }

  @Test
  void testNullModelReturnsFalse() {
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

    Assertions.assertFalse(optimize.isValid());
  }

  @Test
  void testTemperatureOutOfBoundsReturnsFalse() {
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
    Assertions.assertFalse(optimize.isValid());

    optimize.setTemperature(3);
    Assertions.assertFalse(optimize.isValid());
  }

  @Test
  void testNullJobTitleReturnsFalse() {
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

    Assertions.assertFalse(optimize.isValid());
  }

  @Test
  void testNullCompanyReturnsFalse() {
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

    Assertions.assertFalse(optimize.isValid());
  }

  @Test
  void testNullPromptTypeReturnsFalse() {
    // When promptType is set to null, it should be converted to default ["Resume"]
    // and isValid should return true if all other fields are valid.
    // This test verifies that null promptType is handled gracefully by defensive copying.
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

    // Null promptType is converted to default ["Resume"], so isValid should return true
    // if all other required fields are present
    Assertions.assertTrue(optimize.isValid());
  }

  @Test
  void testPromptTypeEmptyReturnsFalse() {
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

    Assertions.assertFalse(optimize.isValid());
  }

  @Test
  void testPromptTypeOutOfBoundsReturnsFalse() {
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

    Assertions.assertFalse(optimize.isValid());
  }

  @Test
  void testValidSkillsPromptWithoutResume() {
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

    Assertions.assertTrue(optimize.isValid(), "SKILLS prompt should be valid without resume content");
  }

  @Test
  void testSkillsPromptTypeRecognition() {
    String[] promptType = {"SKILLS"};
    Optimize optimize = new Optimize();
    optimize.setPromptType(promptType);

    Assertions.assertTrue(optimize.isSkillsPrompt(), "Should recognize SKILLS prompt type");
    Assertions.assertFalse(optimize.hasResumeOrCoverPrompt(), "Should not be Resume or Cover prompt");
  }

  @Test
  void testResumePromptTypeRecognition() {
    String[] promptType = {"Resume"};
    Optimize optimize = new Optimize();
    optimize.setPromptType(promptType);

    Assertions.assertFalse(optimize.isSkillsPrompt(), "Should not be SKILLS prompt");
    Assertions.assertTrue(optimize.hasResumeOrCoverPrompt(), "Should be Resume or Cover prompt");
  }

  @Test
  void testValidPromptTypeIncludesSkills() {
    String[] promptType = {"SKILLS"};
    Optimize optimize = new Optimize();
    optimize.setPromptType(promptType);

    Assertions.assertTrue(optimize.isValidPromptType(), "SKILLS should be a valid prompt type");
  }

  @Test
  void testInvalidPromptType() {
    String[] promptType = {"INVALID"};
    Optimize optimize = new Optimize();
    optimize.setPromptType(promptType);

    Assertions.assertFalse(optimize.isValidPromptType(), "INVALID should not be a valid prompt type");
  }

  @Test
  void testSkillsPromptRequiresJobDescription() {
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

    Assertions.assertFalse(optimize.isValid(), "SKILLS prompt should require job description");
  }

  @Test
  void testCaseInsensitivePromptTypeMatching() {
    // Test lowercase skills
    String[] promptTypeSkillsLower = {"skills"};
    Optimize optimizeLower = new Optimize();
    optimizeLower.setPromptType(promptTypeSkillsLower);
    Assertions.assertTrue(optimizeLower.isSkillsPrompt(), "Should recognize 'skills' in lowercase");

    // Test mixed case
    String[] promptTypeSkillsMixed = {"Skills"};
    Optimize optimizeMixed = new Optimize();
    optimizeMixed.setPromptType(promptTypeSkillsMixed);
    Assertions.assertTrue(optimizeMixed.isSkillsPrompt(), "Should recognize 'Skills' in mixed case");
  }
}
