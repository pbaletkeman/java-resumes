package ca.letkeman.resumes.optimizer;

import static java.net.HttpURLConnection.HTTP_OK;

import ca.letkeman.resumes.Utility;
import ca.letkeman.resumes.optimizer.responses.LLMResponse;
import ca.letkeman.resumes.optimizer.responses.Choice;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.net.HttpURLConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ApiService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiService.class);
  private static final String OUTPUT_DIR = "output";


  public enum PROMPT_TYPE {
    COVER,
    RESUME
  }

  private String serverURL;
  private String jobDescription;
  private String resume;

  public String getJobDescription() {
    return jobDescription;
  }

  public void setJobDescription(String jobDescription) {
    this.jobDescription = jobDescription;
  }

  public String getResume() {
    return resume;
  }

  public void setResume(String resume) {
    this.resume = resume;
  }

  public String getServerURL() {
    return serverURL;
  }

  public void setServerURL(String serverURL) {
    this.serverURL = serverURL;
  }

  public ApiService(String serverURL) {
    setServerURL(serverURL);
  }

  public ApiService() {
    this.serverURL = "http://localhost:1234/v1/chat/completions";
  }

  public LLMResponse invokeApi(ChatBody chatBody) {
    String jsonBody = new Gson().toJson(chatBody);
    try {
      URI uri = new URI(getServerURL());
      HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Accept", "application/json");
      LOGGER.info("Send JSON to LLM Engine");
      conn.setDoOutput(true);

      attachJSONBody(jsonBody, conn);
      StringBuilder response = new StringBuilder();
      try (BufferedReader br = new BufferedReader(
          new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {

        String responseLine = null;
        LOGGER.info("Get API Response");
        while ((responseLine = br.readLine()) != null) {
          response.append(responseLine.trim());
        }
      }
      if (conn.getResponseCode() != HTTP_OK) {
        LOGGER.error("Invalid API response");
        LOGGER.error(String.valueOf(conn.getErrorStream()));
      } else {
        LOGGER.info("Response saved to object.");
        return new Gson().fromJson(response.toString(), LLMResponse.class);
      }
    } catch (Exception e) {
      LOGGER.error(e.toString());
    }
    return null;
  }

  private void attachJSONBody(String jsonBody, HttpURLConnection conn) {
    byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
    try (OutputStream os = conn.getOutputStream()) {
      encodeBody(os, input);
    } catch (IOException e) {
      LOGGER.error("Problem getting output steam:\n{}", e.toString());
    }
  }

  private void encodeBody(OutputStream os, byte[] input) {
    try {
      os.write(input, 0, input.length);
    } catch (IOException e) {
      LOGGER.error("Problem attaching JSON body:\n{}", e.toString());
    }
  }

  public void produceFiles(String promptType, String resume, String jobDescription, String jobTitle, String company) {
    produceFiles(promptType, 0.7, "gemma-3-4b-it", resume, jobDescription, jobTitle, company);
  }

  public void produceFiles(String promptType, double temperature, String model, String resume, String jobDescription, String jobTitle, String company) {
    String promptData = Utility.readFileAsString("prompts" + File.separator + promptType + ".md");
    promptData = promptData.replace("{resume_string}", resume).replace("{jd_string}", jobDescription);

    LocalDate myDateObj = LocalDate.now();
    String today = myDateObj.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
    promptData = promptData.replace("{today}", today);

    ChatBody chatBody = new ChatBody();
    chatBody.setTemperature(temperature);
    chatBody.setModel(model);
    chatBody.setMaxTokens(-1);
    chatBody.setStream(false);

    Message systemMessage = new Message();
    systemMessage.setRole("system");
    systemMessage.setContent("Expert resume writer");
    Message userMessage = new Message();
    userMessage.setRole("user");
    userMessage.setContent(promptData);

    chatBody.setMessages(List.of(systemMessage, userMessage));

    LLMResponse llmResponse = new ApiService().invokeApi(chatBody);

    String body = "";
    String suggestion = "";

    if (llmResponse != null) {
      ArrayList<Choice> choices = (ArrayList<Choice>) llmResponse.getChoices();
      try {
        Files.createDirectories(Paths.get(OUTPUT_DIR));
      } catch (Exception e) {
        LOGGER.error("Unable to create output directory.\n{}", e.toString());
      }
      if (choices != null && !choices.isEmpty() && choices.get(0).getMessage() != null && choices.get(0).getMessage().getContent() != null){
        String message = choices.get(0).getMessage().getContent();
        int chopStart = message.indexOf("```");
        if (chopStart > -1){
          message = message.substring(chopStart);
          chopStart = message.indexOf("\n");
          message = message.substring(chopStart);
          String[] content = message.split("Additional Suggestions");
          if (content.length > 0) {
            body = content[0].trim();
            body = trimString(body);
            body = !body.isEmpty() ? body : "";
            suggestion = content.length == 2 ? content[1].trim() : "";
            suggestion = removeTrailingChar(suggestion,"#");
          } else {
            body = trimString(message.replace("Additional Suggestions", ""));
          }
        } else {
          body = message;
        }
      }
    }

    String fileName = promptType + "-" + company + "-" + jobTitle + ".md";
    createResultFile(fileName, body);
    HtmlToPdf html = new HtmlToPdf(OUTPUT_DIR + File.separator + fileName, OUTPUT_DIR + File.separator + promptType + "-" + company + "-" + jobTitle +".pdf" ,"");
    if (!html.convertFile()) {
      LOGGER.error("Unable to save PDF file");
    }

    if (suggestion != null && !suggestion.isBlank()) {
      fileName = company + "-" + jobTitle + "-suggestions.md";
      createResultFile(fileName, suggestion);
    }

  }

  private static void createResultFile(String fileName, String s ) {

    if (s != null && !s.isBlank()) {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_DIR + File.separator + fileName, false))) {
        writer.write(s);
        writer.flush();  // Data is written to OS page cache, not necessarily to the disk immediately
      } catch (Exception e) {
        LOGGER.error("Error writing file: {}\n{}:", fileName, e.toString());
      }
    }
  }

  private String trimString(String body) {
    if (body != null) {
      while ((body.endsWith("#")) || (body.endsWith("`")) || (body.endsWith("-"))) {
        body = removeTrailingChar(body, "#");
        body = removeTrailingChar(body, "`");
        body = removeTrailingChar(body, "-");
      }
      body = body.trim();
    }
    return body == null ? "" : body;
  }

  private String removeTrailingChar(String str, String badChar) {
    if (str == null) return str; //Handle null input gracefully
    while (str.endsWith(badChar)) {
      str = str.substring(0, str.length() - 1);
    }
    return str.trim();
  }

  public static void main(String[] args) {

    String resume = Utility.readFileAsString("sample" + File.separator + "resume.md");
    String jobDescription = Utility.readFileAsString("sample" + File.separator + "PointClickCare-Software Engineer.txt");

    ApiService apiService = new ApiService();
    apiService.produceFiles(PROMPT_TYPE.RESUME.name(), resume, jobDescription, "developer", "point click care");

  }
}


