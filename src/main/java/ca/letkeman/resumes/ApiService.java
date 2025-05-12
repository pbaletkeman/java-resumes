package ca.letkeman.resumes;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.net.HttpURLConnection;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ApiService {

  private static final Logger logger = LoggerFactory.getLogger(ApiService.class);


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

  private static String readFileAsString(String fileName) {
    String data = "";
    try {
      data = new String(Files.readAllBytes(Paths.get(fileName)));
    } catch (IOException e) {
      logger.error("Error reading file: {}\n{}", fileName, e.toString());
    }
    return data;
  }

  public ApiService() {
    this.serverURL = "http://localhost:1234/v1/chat/completions";
  }

  public CompletableFuture<String> invokeApi(ChatBody chatBody) {
    String jsonBody = new Gson().toJson(chatBody);
    return CompletableFuture.supplyAsync(() -> {
      try {
        URL url = new URL(getServerURL());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        attachJSONBody(jsonBody, conn);
        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
          StringBuilder response = new StringBuilder();
          String responseLine = null;
          while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
          }
          logger.info("API Response:\n{}", response);
        }

        return String.valueOf(conn.getResponseCode());
      } catch (Exception e) {
        logger.error(e.toString());
      }
      return null;
    });
  }
  private static void attachJSONBody(String jsonBody, HttpURLConnection conn) {
    byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
    try (OutputStream os = conn.getOutputStream()) {
      try {
        os.write(input, 0, input.length);
      } catch (IOException e) {
        logger.error("Problem attaching JSON body:\n{}", e.toString());
      }
    } catch (IOException e) {
      logger.error("Problem getting output steam:\n{}", e.toString());
    }
  }

  public static void main(String[] args) {
    ApiService service = new ApiService();

    String promptData = readFileAsString("prompts" + File.separator + PROMPT_TYPE.RESUME.name() + ".md");

    String resume = readFileAsString("sample" + File.separator + "resume.md");
    String jobDescription = readFileAsString("sample" + File.separator + "PointClickCare-Software Engineer.txt");
    promptData = promptData.replace("{resume_string}", resume).replace("{jd_string}", jobDescription);

    ChatBody chatBody = new ChatBody();
    chatBody.setTemperature(0.7);
    chatBody.setModel("gemma-3-4b-it");
    chatBody.setMaxTokens(-1);
    chatBody.setStream(false);

    Message systemMessage = new Message();
    systemMessage.setRole("system");
    systemMessage.setContent("Expert resume writer");
    Message userMessage = new Message();
    userMessage.setRole("user");
    userMessage.setContent(promptData);
    chatBody.setMessages(List.of(systemMessage, userMessage));

    CompletableFuture<String> apiFuture = service.invokeApi(chatBody);

    // Block and wait for the API request to complete
    apiFuture.thenAccept(response -> logger.info("API Response Code: {}", response)).join(); // This will wait for the API response
  }
}
