package ca.letkeman.resumes.optimizer;

import static java.net.HttpURLConnection.HTTP_OK;

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
public class ApiService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiService.class);
  private static final String OUTPUT_DIR = "output";

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
//      URI uri = new URI("http://localhost:1234/v1/chat/completions");
      HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();

      conn.setRequestMethod("POST");
      conn.setRequestProperty("Authorization", "Basic " + apikey);
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
        LOGGER.info("Saving response to object.");
        return new Gson().fromJson(response.toString(), LLMResponse.class);
      }
    } catch (Exception e) {
      LOGGER.error(e.toString());
    }
    return null;
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
    } catch (IOException e) {
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
   */
  public void produceFiles(Optimize optimize, String endpoint, String apikey){

    for (String p: optimize.getPromptType()) {
      produceFiles(p, optimize.getTemperature(), optimize.getModel(), optimize.getResume(), optimize.getJobDescription(), optimize.getJobTitle(), optimize.getCompany(), endpoint, apikey);
    }
  }

  /***
   *
   * @param promptType Cover or Resume prompt request type
   * @param temperature values from 0.0 to 2.0, the higher the value the more creative the response
   * @param model llm model to use
   * @param resume resume content for the request
   * @param jobDescription job description to send to the endpoint
   * @param jobTitle job title of the posting
   * @param company company who posted the job
   */
  public void produceFiles(String promptType, double temperature, String model, String resume, String jobDescription, String jobTitle, String company, String endpoint, String apikey) {
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

    ApiService apiService = new ApiService();
    LLMResponse llmResponse = apiService.invokeApi(chatBody, endpoint, apikey);

    if (llmResponse == null){
      LOGGER.error("Invalid LLM Response. Please try again.");
      return;
    }

    Result result = null;
    ArrayList<Choice> choices = (ArrayList<Choice>) llmResponse.getChoices();
    try {
      Files.createDirectories(Paths.get(OUTPUT_DIR));
    } catch (Exception e) {
      LOGGER.error("Unable to create output directory.\n{}", e.toString());
    }
    if (choices != null && !choices.isEmpty() && choices.get(0).getMessage() != null
        && choices.get(0).getMessage().getContent() != null) {
      String message = choices.get(0).getMessage().getContent();
      result = getResult(message, company);
    }

    if (result == null || result.body() == null){
      LOGGER.error("Invalid LLM result from response. Please try again.");
      return;
    }

    String suffixString = DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm").format(LocalDateTime.now());
    String fileName = promptType + "-" + company + "-" + jobTitle + "-" + suffixString + ".md";
    createResultFile(fileName, result.body());
    HtmlToPdf html = new HtmlToPdf(OUTPUT_DIR + File.separator + fileName,
        OUTPUT_DIR + File.separator + promptType + "-" + company + "-" + jobTitle + "-" + suffixString + ".pdf", "");
    if (!html.convertFile()) {
      LOGGER.error("Unable to save PDF file");
    }

    if (result.suggestion() != null && !result.suggestion().isBlank()) {
      fileName = company + "-" + jobTitle + "-" +  suffixString + "-suggestions.md";
      createResultFile(fileName, result.suggestion());
    }

    LOGGER.info("Operation Complete.");
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
      body = content[0].trim();
      body = trimString(body);
      if (!body.isEmpty()){
        body = body.replace("\\n ","\n").replace("\n**","\n\n**");
        int companyLoc = body.indexOf(company);
        if (companyLoc > 0){
          LOGGER.info("company");
          companyLoc = body.indexOf("\n");
          body = body.substring(companyLoc).trim();
        }
      } else {
        body = "";
      }

      suggestion = content.length == 2 ? content[1].trim() : "";
      suggestion = removeTrailingChar(suggestion, "#");
    } else {
      body = trimString(message.replace("Additional Suggestions", ""));
    }
    return new Result(body, (suggestion == null ? "" : suggestion));
  }

  private record Result(String body, String suggestion) {

  }


  /***
   *
   * @param fileName name of the file to create
   * @param s content to save in the file
   */
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
    if (str == null) return str; //Handle null input gracefully
    while (str.endsWith(badChar)) {
      str = str.substring(0, str.length() - 1);
    }
    return str.trim();
  }
}


