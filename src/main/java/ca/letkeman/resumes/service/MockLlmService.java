package ca.letkeman.resumes.service;

import ca.letkeman.resumes.optimizer.ChatBody;
import ca.letkeman.resumes.optimizer.responses.Choice;
import ca.letkeman.resumes.optimizer.responses.LLMResponse;
import ca.letkeman.resumes.optimizer.responses.Usage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Mock LLM service that simulates Ollama responses without making network calls.
 * Used for testing and development when Ollama service is not available.
 */
@Service
public class MockLlmService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MockLlmService.class);

  /**
   * Generate a mock LLM response based on the chat body request.
   *
   * @param chatBody the request body containing messages and configuration
   * @return a simulated LLM response
   */
  public LLMResponse generateMockResponse(ChatBody chatBody) {
    LOGGER.info("Generating mock LLM response (no network call to Ollama)");

    LLMResponse response = new LLMResponse();
    response.setId("chatcmpl-mock-" + UUID.randomUUID().toString().substring(0, 8));
    response.setObject("chat.completion");
    response.setCreated(System.currentTimeMillis() / 1000);
    response.setModel(chatBody.getModel() != null ? chatBody.getModel() : "mistral-mock");

    // Extract user prompt to tailor response
    String userPrompt = extractUserPrompt(chatBody);
    String mockContent = generateMockContent(userPrompt);

    // Create choice with mock message
    ca.letkeman.resumes.optimizer.responses.Message responseMessage =
        new ca.letkeman.resumes.optimizer.responses.Message();
    responseMessage.setRole("assistant");
    responseMessage.setContent(mockContent);

    Choice choice = new Choice();
    choice.setIndex(0);
    choice.setMessage(responseMessage);
    choice.setFinishReason("stop");

    List<Choice> choices = new ArrayList<>();
    choices.add(choice);
    response.setChoices(choices);

    // Set mock usage stats
    Usage usage = new Usage();
    usage.setPromptTokens(estimateTokens(userPrompt));
    usage.setCompletionTokens(estimateTokens(mockContent));
    usage.setTotalTokens(usage.getPromptTokens() + usage.getCompletionTokens());
    response.setUsage(usage);

    response.setSystemFingerprint("mock-fp-" + UUID.randomUUID().toString().substring(0, 8));

    LOGGER.info("Mock LLM response generated successfully");
    return response;
  }

  /**
   * Extract user prompt from chat body messages.
   */
  private String extractUserPrompt(ChatBody chatBody) {
    if (chatBody == null || chatBody.getMessages() == null) {
      return "";
    }

    for (ca.letkeman.resumes.optimizer.Message msg : chatBody.getMessages()) {
      if ("user".equals(msg.getRole())) {
        return msg.getContent() != null ? msg.getContent() : "";
      }
    }
    return "";
  }

  /**
   * Generate realistic mock content based on the prompt type.
   */
  private String generateMockContent(String prompt) {
    String lowerPrompt = prompt.toLowerCase();

    // Detect prompt type and generate appropriate mock response
    if (lowerPrompt.contains("resume") && lowerPrompt.contains("optimize")) {
      return generateMockResume();
    } else if (lowerPrompt.contains("cover letter")) {
      return generateMockCoverLetter();
    } else if (lowerPrompt.contains("interview") && lowerPrompt.contains("hr")) {
      return generateMockInterviewHRQuestions();
    } else if (lowerPrompt.contains("interview") && lowerPrompt.contains("job")) {
      return generateMockInterviewJobSpecific();
    } else if (lowerPrompt.contains("interview") && lowerPrompt.contains("reverse")) {
      return generateMockReverseInterviewQuestions();
    } else if (lowerPrompt.contains("cold") && lowerPrompt.contains("email")) {
      return generateMockColdEmail();
    } else if (lowerPrompt.contains("linkedin")) {
      return generateMockLinkedInMessage();
    } else if (lowerPrompt.contains("thank") && lowerPrompt.contains("you")) {
      return generateMockThankYouEmail();
    } else if (lowerPrompt.contains("skill")) {
      return generateMockSkills();
    } else {
      return generateGenericMockResponse();
    }
  }

  private String generateMockResume() {
    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
    return """
        # Professional Resume

        ## Summary
        Experienced professional with expertise in software development and team leadership.
        Proven track record of delivering high-quality solutions and driving innovation.

        ## Professional Experience

        ### Senior Software Engineer
        **Mock Company** | %s

        - Led development of enterprise applications using Java and Spring Boot
        - Mentored junior developers and conducted code reviews
        - Improved system performance by 40%% through optimization
        - Implemented CI/CD pipelines reducing deployment time by 60%%

        ## Skills

        - **Programming**: Java, Python, JavaScript, TypeScript
        - **Frameworks**: Spring Boot, React, Node.js
        - **Tools**: Git, Docker, Kubernetes, Jenkins
        - **Databases**: PostgreSQL, MongoDB, Redis

        ## Education

        **Bachelor of Science in Computer Science**
        Mock University | 2018

        ## Certifications

        - AWS Certified Solutions Architect
        - Oracle Certified Professional Java SE Developer

        """.formatted(date);
  }

  private String generateMockCoverLetter() {
    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
    return """
        %s

        Dear Hiring Manager,

        I am writing to express my strong interest in the position at your organization.
        With my background in software development and proven track record of success,
        I am confident I would be a valuable addition to your team.

        Throughout my career, I have demonstrated expertise in developing scalable
        applications, leading cross-functional teams, and delivering innovative solutions
        that drive business value. My experience aligns well with the requirements
        outlined in the job description.

        Key highlights of my qualifications include:

        - 5+ years of experience in full-stack development
        - Strong proficiency in Java, Spring Boot, and modern web technologies
        - Proven ability to architect and implement enterprise-grade solutions
        - Experience with Agile methodologies and DevOps practices

        I am excited about the opportunity to contribute to your organization and would
        welcome the chance to discuss how my skills and experience align with your needs.

        Thank you for your consideration.

        Sincerely,
        [Your Name]
        """.formatted(date);
  }

  private String generateMockInterviewHRQuestions() {
    return """
        # Common HR Interview Questions and Suggested Responses

        ## 1. Tell me about yourself
        **Suggested Response:**
        I'm a software engineer with 5 years of experience specializing in backend development.
        I've worked on enterprise applications, led technical initiatives, and mentored junior
        developers. I'm passionate about clean code and solving complex problems.

        ## 2. What are your greatest strengths?
        **Suggested Response:**
        My greatest strengths are problem-solving and communication. I excel at breaking down
        complex technical challenges and explaining solutions to both technical and non-technical
        stakeholders.

        ## 3. What are your weaknesses?
        **Suggested Response:**
        I sometimes get too focused on perfecting details, but I've learned to balance quality
        with pragmatic delivery timelines through Agile practices.

        ## 4. Why do you want to work here?
        **Suggested Response:**
        I'm impressed by your company's commitment to innovation and technical excellence.
        The opportunity to work on challenging problems with a talented team aligns perfectly
        with my career goals.

        ## 5. Where do you see yourself in 5 years?
        **Suggested Response:**
        I see myself growing into a technical leadership role, mentoring others, and contributing
        to architectural decisions while staying hands-on with development.
        """;
  }

  private String generateMockInterviewJobSpecific() {
    return """
        # Job-Specific Technical Interview Questions

        ## Technical Questions

        ### 1. Explain your experience with microservices architecture
        **Response:** I have extensive experience designing and implementing microservices
        using Spring Boot and Docker. I've worked on breaking down monoliths, implementing
        service discovery, and handling distributed transactions.

        ### 2. How do you handle database optimization?
        **Response:** I use a combination of indexing strategies, query optimization,
        and caching. I analyze query execution plans and implement database-specific
        optimizations based on workload patterns.

        ### 3. Describe your approach to code review
        **Response:** I focus on code quality, maintainability, and adherence to standards.
        I look for potential bugs, security issues, and opportunities for improvement while
        being constructive and educational in my feedback.

        ### 4. How do you ensure application security?
        **Response:** I follow security best practices including input validation,
        authentication/authorization, SQL injection prevention, and regular security
        audits. I stay updated on OWASP guidelines.

        ### 5. What's your experience with CI/CD?
        **Response:** I've implemented CI/CD pipelines using Jenkins and GitHub Actions,
        including automated testing, security scanning, and deployment automation.
        """;
  }

  private String generateMockReverseInterviewQuestions() {
    return """
        # Questions to Ask the Interviewer

        ## Team and Culture

        1. **Team Structure:** Can you describe the team structure and how engineers collaborate?

        2. **Development Process:** What does your development process look like? Do you follow
           Agile/Scrum methodologies?

        3. **Code Quality:** How does the team ensure code quality? What are your practices
           around code reviews and testing?

        ## Technical Environment

        4. **Technology Stack:** What technologies and frameworks does the team primarily work with?

        5. **Technical Challenges:** What are the most interesting technical challenges
           the team is currently facing?

        6. **Infrastructure:** What does your infrastructure look like? Cloud providers,
           deployment strategies?

        ## Growth and Development

        7. **Professional Development:** What opportunities are there for learning and
           professional growth?

        8. **Career Path:** What does a typical career progression look like for this role?

        ## Success Metrics

        9. **Success Definition:** How would you define success for this role in the first
           6 months?

        10. **Team Challenges:** What are the biggest challenges the team or company is
            currently facing?
        """;
  }

  private String generateMockColdEmail() {
    return """
        Subject: Exploring Opportunities at [Company Name]

        Hi [Recipient Name],

        I hope this email finds you well. My name is [Your Name], and I'm a software
        engineer with expertise in backend development and distributed systems.

        I've been following [Company Name]'s work in [specific area] and am impressed
        by your innovative approach. With my background in [relevant skills], I believe
        I could contribute meaningfully to your team.

        Would you be open to a brief conversation to explore potential opportunities
        or learn more about your work? I'm particularly interested in [specific area
        of interest].

        Thank you for your time and consideration.

        Best regards,
        [Your Name]
        [LinkedIn Profile]
        [Email]
        """;
  }

  private String generateMockLinkedInMessage() {
    return """
        # LinkedIn Connection Message

        Hi [Name],

        I came across your profile while researching [Company Name] and was impressed
        by your work in [specific area]. I'm currently exploring opportunities in
        [field/industry] and would love to connect and learn from your experience.

        Looking forward to connecting!

        Best,
        [Your Name]

        ---

        # Follow-up Message (After Connection)

        Hi [Name],

        Thanks for connecting! I'd love to learn more about your experience at
        [Company Name] and any advice you might have for someone interested in
        [specific area].

        Would you be open to a brief chat sometime?

        Best regards,
        [Your Name]
        """;
  }

  private String generateMockThankYouEmail() {
    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
    return """
        %s

        Dear [Interviewer Name],

        Thank you for taking the time to meet with me yesterday to discuss the
        [Position] role at [Company Name]. I thoroughly enjoyed our conversation
        and learning more about the team and the exciting projects you're working on.

        I was particularly intrigued by [specific topic discussed] and believe my
        experience with [relevant skill/experience] would allow me to contribute
        effectively to your team's goals.

        Our discussion reinforced my enthusiasm for this opportunity, and I'm
        excited about the possibility of joining [Company Name] and contributing
        to [specific initiative or project].

        Please don't hesitate to reach out if you need any additional information.
        I look forward to hearing from you about the next steps.

        Thank you again for your time and consideration.

        Best regards,
        [Your Name]
        [Phone]
        [Email]
        """.formatted(date);
  }

  private String generateMockSkills() {
    return """
        # Technical Skills Assessment

        Based on the job description, here are the key skills to highlight:

        ## Core Technical Skills
        - Java (Spring Boot, Hibernate, JPA)
        - RESTful API design and implementation
        - Database design and optimization (SQL, NoSQL)
        - Cloud platforms (AWS, Azure, or GCP)
        - Containerization (Docker, Kubernetes)

        ## Development Practices
        - Test-driven development (TDD)
        - Continuous Integration/Continuous Deployment (CI/CD)
        - Agile/Scrum methodologies
        - Code review and pair programming
        - Version control (Git)

        ## Soft Skills
        - Problem-solving and analytical thinking
        - Team collaboration and communication
        - Technical documentation
        - Mentoring and knowledge sharing
        - Time management and prioritization

        ## Additional Skills
        - Frontend technologies (React, Angular, or Vue.js)
        - Message queues (RabbitMQ, Kafka)
        - Caching strategies (Redis, Memcached)
        - Security best practices (OWASP)
        - Performance optimization and monitoring
        """;
  }

  private String generateGenericMockResponse() {
    return """
        # Mock LLM Response

        This is a simulated response from the mock LLM service.

        ## Key Points

        - Mock responses are generated locally without network calls
        - Responses are tailored based on prompt content
        - This enables testing without requiring Ollama service

        ## Benefits

        1. **No Dependencies**: Works without Ollama installation
        2. **Fast**: No network latency
        3. **Predictable**: Consistent responses for testing
        4. **Offline**: No internet connection required

        ## Usage

        The mock service is automatically used when `llm.mock.enabled=true`
        is set in application configuration.
        """;
  }

  /**
   * Estimate token count (rough approximation: 1 token ≈ 4 characters).
   */
  private int estimateTokens(String text) {
    if (text == null || text.isEmpty()) {
      return 0;
    }
    return Math.max(1, text.length() / 4);
  }
}
