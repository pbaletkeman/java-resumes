package ca.letkeman.resumes.optimizer;

import ca.letkeman.resumes.Utility;
import ca.letkeman.resumes.model.Optimize;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.net.HttpURLConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public final class ApiService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiService.class);

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

  public ApiService() {
    // default constructor
  }

  /***
   *
   * @param chatBody - the object to send to the LLM endpoint
   * @return - the result from the LLM request
   */
  public LLMResponse invokeApi(ChatBody chatBody, String endpoint, String apikey) {
    String jsonBody = new Gson().toJson(chatBody);
    try {
      URI uri = new URI(endpoint);
      HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();

      conn.setRequestMethod("POST");
      conn.setRequestProperty("Authorization", "Basic " + apikey);
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Accept", "application/json");
      LOGGER.info("Send JSON to LLM Engine");
      conn.setDoOutput(true);

      attachJSONBody(jsonBody, conn);
      if (conn.getResponseCode() != java.net.HttpURLConnection.HTTP_OK) {
        LOGGER.error("Invalid API response");
        if (LOGGER.isErrorEnabled()) {
          LOGGER.error(String.valueOf(conn.getErrorStream()));
        }
      } else {
        String response = getAPIResponse(conn);
        LOGGER.info("Saving response to object.");
        return new Gson().fromJson(response, LLMResponse.class);
      }
    } catch (Exception e) {
      LOGGER.error(e.toString());
    }
    return null;
  }

  /***
   *
   * @param conn - http connection to use
   * @return - api response
   */
  private String getAPIResponse(HttpURLConnection conn) {
    StringBuilder response = new StringBuilder();
    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {

      String responseLine;
      LOGGER.info("Get API Response");
      while ((responseLine = br.readLine()) != null) {
        response.append(responseLine.trim());
      }
    } catch (Exception e) {
      LOGGER.error("Problem with api response.");
      LOGGER.error(e.toString());
    }
    return response.toString();
  }

  /***
   *
   * @param jsonBody - content to add to the http request
   * @param conn - http connection to use
   */
  private void attachJSONBody(String jsonBody, HttpURLConnection conn) {
    byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
    try (OutputStream os = conn.getOutputStream()) {
      encodeBody(os, input);
    } catch (Exception e) {
      LOGGER.error("Problem getting output steam:\n{}", e.toString());
    }
  }

  /***
   *
   * @param os - the output stream containing the data to encode
   * @param input - the data to be encoded into the output stream
   */
  private void encodeBody(OutputStream os, byte[] input) {
    try {
      os.write(input, 0, input.length);
    } catch (IOException e) {
      LOGGER.error("Problem attaching JSON body:\n{}", e.toString());
    }
  }

  /**
   *
   * @param optimize api input parameters
   * @param endpoint - url to post the prompt to
   * @param apikey - openai key
   * @param root - location to upload & save file tp
   */
  public void produceFiles(Optimize optimize, String endpoint, String apikey, String root){
    if (optimize != null) {
      for (String p : optimize.getPromptType()) {
        produceFiles(p, optimize, endpoint, apikey, root);
      }
    } else {
      LOGGER.error("invalid optimize");
    }
  }

  /***
   *
   *
   * @param promptType - cover letter or resume to generate
   * @param optimize - the data to sent to the endpoint
   * @param endpoint - url to post the prompt to
   * @param apikey - openai key
   * @param root - location to upload & save file tp
   */
  public void produceFiles(String promptType, Optimize optimize, String endpoint, String apikey, String root) {
    LocalDate myDateObj = LocalDate.now();
    String today = myDateObj.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));

    String promptData = Utility.readFileAsString("prompts" + File.separator + promptType + ".md");
    promptData = promptData.replace("{resume_string}", optimize.getResume()).replace("{jd_string}", optimize.getJobDescription());
    promptData = promptData.replace("{today}", today);

    ChatBody chatBody = getChatBody(optimize, promptData);

    ApiService apiService = new ApiService();
    LLMResponse llmResponse = apiService.invokeApi(chatBody, endpoint, apikey);

    if (llmResponse == null){
      LOGGER.error("Invalid LLM Response. Please try again.");
      return;
    }

    Result result = null;
    ArrayList<Choice> choices = (ArrayList<Choice>) llmResponse.getChoices();
    try {
      Files.createDirectories(Paths.get(root));
    } catch (Exception e) {
      LOGGER.error("Unable to create output directory.\n{}", e.toString());
    }
    if (choices != null && !choices.isEmpty() && choices.get(0).getMessage() != null
        && choices.get(0).getMessage().getContent() != null) {
      String message = choices.get(0).getMessage().getContent();
      result = getResult(message, optimize.getCompany());
    }

    if (result == null || result.body() == null){
      LOGGER.error("Invalid LLM result from response. Please try again.");
      return;
    }

    String suffixString = DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm").format(LocalDateTime.now());
    String fileName = promptType + "-" + optimize.getCompany() + "-" + optimize.getJobTitle() + "-" + suffixString + ".md";
    createResultFile(fileName, result.body(), root);
    HtmlToPdf html = new HtmlToPdf( root + File.separator + fileName,
        root + File.separator + promptType + "-" + optimize.getCompany() + "-" + optimize.getJobTitle() + "-" + suffixString + ".pdf", "");
    if (!html.convertFile()) {
      LOGGER.error("Unable to save PDF file");
    }

    if (result.suggestion() != null && !result.suggestion().isBlank()) {
      fileName = optimize.getCompany() + "-" + optimize.getJobTitle() + "-" +  suffixString + "-suggestions.md";
      createResultFile(fileName, result.suggestion(),root);
    }

    LOGGER.info("Operation Complete.");
  }

  private ChatBody getChatBody(Optimize optimize, String promptData) {
    ChatBody chatBody = new ChatBody();
    chatBody.setTemperature(optimize.getTemperature());
    chatBody.setModel(optimize.getModel());
    chatBody.setMaxTokens(-1);
    chatBody.setStream(false);

    Message systemMessage = new Message();
    systemMessage.setRole("system");
    systemMessage.setContent("Expert resume writer");
    Message userMessage = new Message();
    userMessage.setRole("user");
    userMessage.setContent(promptData);

    chatBody.setMessages(List.of(systemMessage, userMessage));
    return chatBody;
  }

  /***
   *
   * @param message the message response, considered the source
   * @return formatted response from the source
   */
  private Result getResult(String message, String company) {
    String body;
    String suggestion = null;
    String[] content = message.split("Additional Suggestions");
    if (content.length > 0) {
      body = calcBody(company, content[0].trim());
      suggestion = content.length == 2 ? content[1].trim() : "";
      suggestion = removeTrailingChar(suggestion, "#");
    } else {
      body = trimString(message.replace("Additional Suggestions", ""));
    }
    return new Result(body, (suggestion == null ? "" : suggestion));
  }

  private String calcBody(String company, String content) {
    String body = trimString(content);
    if (!body.isEmpty()){
      body = body.replace("\\n ","\n").replace("\n**","\n\n**");
      int tempLoc = body.indexOf(company);
      if (tempLoc > 0){
        // remove the first line from the output
        tempLoc = body.indexOf("\n");
        body = body.substring(tempLoc).trim();
      }
      tempLoc = body.toLowerCase().indexOf("markdown");
      if (tempLoc > 0){
        // remove the word markdown from the output
        // markdown is not a skill
        body = body.substring(tempLoc).trim();
      }
      while (body.startsWith("`")){
        // remove markdown notation from start of the output
        body = body.substring(1).trim();
      }
    } else {
      body = "";
    }
    return body;
  }

  private record Result(String body, String suggestion) {

  }

  /***
   *
   * @param fileName name of the file to create
   * @param s content to save in the file
   */
  private void createResultFile(String fileName, String s, String root ) {

    if (s != null && !s.isBlank()) {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(root + File.separator + fileName, false))) {
        writer.write(s);
        writer.flush();  // Data is written to OS page cache, not necessarily to the disk immediately
      } catch (Exception e) {
        LOGGER.error("Error writing file: {}\n{}:", fileName, e.toString());
      }
    }
  }

  /***
   *
   * @param body text with extra characters that should be removed
   * @return cleaned up response
   */
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

  /***
   *
   * @param str string with bad characters
   * @param badChar bad character to remove
   * @return cleaned up str
   */
  private String removeTrailingChar(String str, String badChar) {
    if (str == null) return ""; //Handle null input gracefully
    while (str.endsWith(badChar)) {
      str = str.substring(0, str.length() - 1);
    }
    return str.trim();
  }
}
