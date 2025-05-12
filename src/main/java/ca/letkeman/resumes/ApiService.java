package ca.letkeman.resumes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiService {

  /*

   */


  public CompletableFuture<String> invokeApi() {
    String body = "{\"model\": \"gemma-3-4b-it\", \"messages\": [ { \"role\": \"system\", \"content\": \"Always answer in rhymes. Today is Thursday\" }, { \"role\": \"user\", \"content\": \"What day is it today?\" } ], \"temperature\": 0.7, \"max_tokens\": -1, \"stream\": false }";

    String abc = "{\n"
        + "  \"model\": \"gemma-3-4b-it\",\n"
        + "  \"messages\": [\n"
        + "    {\n"
        + "      \"role\": \"system\",\n"
        + "      \"content\": \"Expert resume writer\"\n"
        + "    },\n"
        + "    {\n"
        + "      \"role\": \"user\",\n"
        + "      \"content\": \"You are a professional resume optimization expert specializing in tailoring resumes to specific job descriptions. Your goal is to optimize my resume and provide actionable suggestions for improvement to align with the target role.\\r\\n\\r\\n### Guidelines:\\r\\n1. **Relevance**:\\r\\n    - **Keep experience in the same chronological order** \\r\\n    - **Do not include target role/job**\\r\\n    - Prioritize experiences, skills, and achievements **most relevant to the job description**.\\r\\n    - Remove or de-emphasize irrelevant details to ensure a **concise** and **targeted** resume.\\r\\n    - Limit work experience section to 2-3 most relevant roles\\r\\n    - Limit bullet points under each role to 2-3 most relevant impacts\\r\\n    - Do not include items not listed on the source resume\\r\\n\\r\\n2. **Action-Driven Results**:\\r\\n    - Use **strong action verbs** and **quantifiable results** (e.g., percentages, revenue, efficiency improvements) to highlight impact.\\r\\n\\r\\n3. **Keyword Optimization**:\\r\\n    - Integrate **keywords** and phrases from the job description naturally to optimize for ATS (Applicant Tracking Systems).\\r\\n\\r\\n4. **Additional Suggestions** *(If Gaps Exist)*:\\r\\n    - If the resume does not fully align with the job description, suggest:\\r\\n        1. **Additional technical or soft skills** that I could add to make my profile stronger.\\r\\n        2. **Certifications or courses** I could pursue to bridge the gap.\\r\\n        3. **Project ideas or experiences** that would better align with the role.\\r\\n\\r\\n5. **Formatting**:\\r\\n    - Output the tailored resume in **clean Markdown format**.\\r\\n    - Include an **\\\"Additional Suggestions\\\"** at the end with actionable improvement recommendations.\\r\\n    - Put an empty line between Linkedin link and GitHub link\\r\\n---\\r\\n\\r\\n### Input:\\r\\n- **My resume**:\\r\\nPointClickCare is a leading North American healthcare technology platform enabling meaningful care collaboration and real‐time patient insights. For over 20 years, the company has been focused on realizing its vision: to help create a world in which providers and plans can confidently deliver frictionless care. Since its inception, PointClickCare has grown exponentially, with over 2,200 employees working to impact millions across North America. Recognized by Forbes as one of the top 100 private cloud companies and acknowledged by Waterstone Human Capital as Canada’s Most Admired Corporate Cultures, PointClickCare leads the way in creating cloud-based healthcare software.\\n\\nAt PointClickCare, we offer a wealth of opportunities and a vibrant culture that empowers our employees. Our dynamic environment is the perfect place to advance your career while engaging in meaningful work alongside incredible colleagues. Here, you’ll discover a space where your talents can thrive, your career can grow, and your work will have a lasting impact on healthcare across North America. We believe that work becomes profoundly fulfilling when driven by a higher purpose.\\n\\nJoin us and be part of a team that is making a real impact.\\n\\nTo learn more about us, check out Life at PointClickCare and connect with us on Glassdoor and LinkedIn.\\n\\nWhat does a typical day look like for a Data Software Engineer?\\n\\n You would be responsible for designing, developing, implementing, and supporting our emerging big data analytics capabilities through the development and maintenance of advanced data ingestion, processing, modeling and reporting capabilities.\\n The majority of your day would be spent working with a cross-functional team of developers, product leaders, to specify, design, develop, test, and implement software.\\n We’re huge on professional development, and so there’s always a training class, hackathon, or an internal course being offered.\\n Have a look at a video by our Engineering department: https://www.youtube.com/watch?v\\u003dBeeSn3lzkwc\\u0026list\\u003dPLwERWsJLXEnEz9j8IwqjUWfqCzQ1SbvGL\\u0026index\\u003d7\\n\\nWhat does the growth potential look like?\\n\\n We focus heavily on employee career progression and provide many internal opportunities for upward advancement. An Intermediate or junior software Engineer will typically progress to Sr. Engineer, Principal Engineer and/or Architect. There are also opportunities to move into other departments depending on your areas of interest.\\n Even if you’re not looking to change jobs, we offer free training on new SW technologies, project management, agile training, leading others, and improving your public speaking / presentation skills to name a few\\n\\nWhat qualifications do you look for?\\n\\n Int. Software engineer with 2+ years of experience in Java, SpringBoot microservices\\n Experience with Jenkins, Docker/Kubernetes on Cloud (Azure or AWS)\\n Experience writing RESTful API endpoints\\n Nice to have hands-on experience with Relational databases (like Microsoft SQL Server), by tuning SQL queries and analyzing query plans\\n Knowledge of non-relational / NoSQL data store: Azure ADLS, DeltaLake, Apache Hudi\\n Excellent organization, critical-thinking\\n Self-starter with the ability to deliver with minimal supervision\\n Being okay with the uncomfortable feeling that comes from learning new things\\n Team player\\n Analytical mind with problem-solving aptitude\\n Proven experience as a great Engineer\\n For the education requirements, we’re looking to hire someone with a Degree in Computer/Software Engineering.\\r\\n\\r\\n- **The job description**:\\r\\n# Peter Letkeman\\n\\n## Certified Python \\u0026 Java Spring Developer\\npete@letkeman.ca | 519-331-1405 | Toronto, ON\\n\\n[https://linkedin.com/in/pete-letkeman-264592a](linkedin.com/in/pete-letkeman-264592a)\\n\\n[https://github.com/pbaletkeman](github.com/pbaletkeman)\\n## Summary:\\nWith over two decades of experience developing software applications and solutions, I\\u0027m a Certified Python 3 and Java Spring Developer with a broad skill set. My expertise includes desktop development, server setup and configuration, and web application back-end development using current technologies.\\n\\n## Skills:\\n\\n- Certified Python 3.x Developer\\n- Certified Java 8 Programmer \\u0026 Certified Java Spring Developer\\n- Elasticsearch, C#, VBScript/ASP Classic, JavaScript, JSON, MySQL, SQLite, NoSQL, PostgreSQL, MongoDB\\n- Database design, implementation, and administration, Data management\\n- Web application implementation, Full lifecycle website/software development\\n- Windows and Linux server administration, UML \\u0026 Class Diagrams\\n- Git/Source Control Systems, Agile/Scrum development processes and methodologies, RESTful API development\\n- Microservices Development, Low Code Development\\n\\n## Experience:\\n\\n**Developer | Rakuten Kobo, Toronto, ON (May 2023  - Present)**\\n\\n- Developed a reporting platform using custom Java and Python RESTFul APIs, seamlessly integrated with Retool, offering easier data access and effectively breaking down of data silos. This helped with fraud detection and system abuse prevention by 20% with early detection of malicious users. As well, manual intervention by developers has been cut by more than 80%.\\n- Engineered user-centric website designs with no-code platforms, boosting overall user satisfaction by 40%.\\n- Created custom LowCode UI for Elasticsearch operations reducing user knowledge barriers and common errors by more than 75%.\\n- Migrated Python 2.x code to Python 3.x and Java 17, ensuring compatibility and future-proofing the solutions.\\n- Successfully deployed websites across various systems and environments.\\n- Mentored junior developers in database modeling and Restful API creation\\n\\n**Senior Software Developer | SystemSoft Technologies, Toronto, ON (August 2021  - April 2023)**\\n\\n- Boosted site performance by 30% through transitioning to no-code web development, enhancing design elements and optimizing processes.\\n- Enhanced user satisfaction with visually appealing website designs using low-code tools for easy navigation.\\n- Strengthened security and added features through the integration of third-party APIs into websites.\\n- Contributed to data modeling solutions for MSSQL \\u0026 Snowflake\\n- Mentored junior developers, fostering professional growth within a supportive learning environment.\\n- Led team meetings with confidence and efficiency, ensuring productive discussions.\\n\\n**Software Developer | Profit Solutions International Inc., Toronto, ON (February 2021  - July 2021)**\\n\\n- Developed and optimized interactive forms and dynamic reports using PowerBuilder, enhancing user experience and data interpretation.\\n- Orchestrated the integration of third-party APIs within the PowerBuilder platform, expanding functionality and enabling seamless data exchange with external systems.\\n- Designed, coded, and managed SQL Anywhere stored procedures, optimizing database performance and ensuring efficient execution of complex queries.\\n\\n**Software Developer | Inspiretec, Toronto, ON (September 2018  - March 2020)**\\n\\n- Engineered innovative travel and vacation planning software features using Java 8, MongoDB, and Elasticsearch. This enhancement significantly improved user experience, providing enhanced functionality and features.\\n- Seamlessly integrated third-party APIs through JSON documents and XML datasets, allowing for more comprehensive data retrieval and utilization within the application.\\n- Successfully identified and resolved over 50 software bugs using Jira and Agile methodologies. This led to improved app performance, resulting in a 20% increase in user engagement.\\n- Conceptualized and presented data-driven campaigns during strategic meetings. These strategies resulted in a 15% increase in customer engagement.\\n- Trained new employees to enhance team efficiency and productivity.\\n- Developed and optimized deployment scripts, allowing for faster and more efficient deployments to different systems and environments minimizing downtime and ensuring seamless application updates.\\n\\n**Software Developer Consultant | Zinnetech Consulting Inc., Toronto, ON (August 2015  - March 2016)**\\n\\n- Manufactured dashboards and websites for financial analytics and other KPI tracking using SAP Design Studio.\\n\\n**Developer \\u0026 System Administrator | Electro-Byte Technologies Inc., Sarnia, ON (November 2000  - July 2015)**\\n\\n- Engineered seamless cross-platform database synchronization services, enhancing data consistency and accessibility across multiple platforms.\\n- Orchestrated the development and successful synchronization of over 200 websites, leveraging databases for efficient data management and scalability.\\n- Created a ticketing system resembling Ticketmaster, enabling users to securely purchase and manage event tickets with ease.\\n- Developed versatile C# applications for both Windows and Linux platforms using the Mono framework, enhancing cross-platform compatibility and functionality.\\n- Engineered REST endpoints in C# and VBScript for mobile and website applications, facilitating seamless data exchange and integration between different components of a system.\\n- Diagnosed and modified software components used in Windows apps and websites, resolving issues and improving overall system performance and functionality.\\n- Conceptualized and implemented scripts for content management systems, significantly enhancing user experience by automating repetitive tasks and improving workflow efficiency.\\n- Collaborated with developers and system administrators to streamline performance issue identification and resolution, ultimately reducing downtime and increasing overall system reliability.\\n- Closely collaborated with the design team to ensure software solutions not only met functional requirements but also improved client-side experiences, resulting in higher customer satisfaction.\\n- Actively participated in project planning, design, and development activities, contributing to the creation of robust solutions that delivered value within the desired timeline.\\n\\n## Education:\\n\\nSoftware Architecture - University of Alberta (Coursera)\\n\\nAssociate\\u0027s Degree: 3D Game Modeler at Centre for Distance Education, Sydney, Nova Scotia\\n\\nAssociate\\u0027s Degree: Computer Programmer/Analyst at Lambton College, Sarnia, Ontario\\r\\n\\r\\n---\\r\\n\\r\\n### Output:\\r\\n1. **Tailored Resume**:\\r\\n    - A resume in **Markdown format** that emphasizes relevant experience, skills, and achievements.\\r\\n    - Incorporates job description **keywords** to optimize for ATS.\\r\\n    - Uses strong language\\r\\n\\r\\n2. **Additional Suggestions** *(if applicable)*:\\r\\n    - List **skills** that could strengthen alignment with the role.\\r\\n    - Recommend **certifications or courses** to pursue.\\r\\n    - Suggest **specific projects or experiences** to develop.\"\n"
        + "    }\n"
        + "  ],\n"
        + "  \"temperature\": 0.7,\n"
        + "  \"maxTokens\": -1,\n"
        + "  \"stream\": false\n"
        + "}";

    return CompletableFuture.supplyAsync(() -> {
      try {
//        URL url = new URL("https://api.restful-api.dev/objects");
        URL url = new URL("http://localhost:1234/v1/chat/completions");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        try(OutputStream os = conn.getOutputStream()) {
          byte[] input = abc.getBytes(StandardCharsets.UTF_8);
          try {
            os.write(input, 0, input.length);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
        try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
          StringBuilder response = new StringBuilder();
          String responseLine = null;
          while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
          }
          System.out.println(response.toString());
        }

        return String.valueOf(conn.getResponseCode());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  public static void main(String[] args) {
    ApiService service = new ApiService();
    CompletableFuture<String> apiFuture = service.invokeApi();

    // Block and wait for the API request to complete
    apiFuture.thenAccept(response -> {
      System.out.println("API Response Code: " + response);
    }).join(); // This will wait for the API response
  }
}

