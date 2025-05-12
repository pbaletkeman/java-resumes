package ca.letkeman.resumes;

import ca.letkeman.resumes.responses.LLMResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiCall {

  private static final Logger logger = LoggerFactory.getLogger(ApiCall.class);
  private String model;
  private Double temperature;
  private PROMPT_TYPE promptType;
  private String url; // = "http://localhost:1234/v1/chat/completions";   // API URL
  private String jobDescription;
  private String resume;
  private ChatBody chatBody;

  public ApiCall(String model, double temperature, PROMPT_TYPE promptType, String url, ChatBody chatBody) {
    setModel(model);
    setTemperature(temperature);
    setPromptType(promptType);
    setUrl(url);
    setChatBody(chatBody);
  }

  // JSON String which will be sent to the API.
//  private String dataToSend;

  public ApiCall() {
    setModel("gemma-3-4b-it");
    setUrl("http://localhost:1234/v1/chat/completions");
    setTemperature(0.7);
    setPromptType(PROMPT_TYPE.RESUME);
    setChatBody(new ChatBody());

//    dataToSend = "{\"model\": \"gemma-3-4b-it\", \n\"messages\": [ \n{ \"role\": \"system\", \"content\": \"Always answer in rhymes. Today is Thursday\" }, \n{ \"role\": \"user\", \"content\": \"What day is it today?\" } \n], \n\"temperature\": 0.7, \n\"max_tokens\": -1, \n\"stream\": false \n}";
  }

  private static String readFileAsString(String fileName) {
    String data = "";
    try {
      data = new String(Files.readAllBytes(Paths.get(fileName)));
    } catch (IOException e) {
      logger.error(e.toString());
    }
    return data;
  }

  public static void main(String[] args) {

    /*
    rb.setResume(readFileAsString("sample" + File.separator +"resume.md"));
    rb.setJobDescription(readFileAsString("sample" + File.separator +"PointClickCare-Software Engineer.txt"));
     */
    ApiCall apiCall = new ApiCall();
    LLMResponse llmResponse = apiCall.execute();
    Gson res = new Gson();
    System.out.println(res.toJson(llmResponse));

    System.out.println("done");
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public Double getTemperature() {
    return temperature;
  }

  public void setTemperature(Double temperature) {
    this.temperature = temperature;
  }

  public PROMPT_TYPE getPromptType() {
    return promptType;
  }

  public void setPromptType(PROMPT_TYPE promptType) {
    this.promptType = promptType;
  }

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

  public ChatBody getChatBody() {
    return chatBody;
  }

  public void setChatBody(ChatBody chatBody) {
    this.chatBody = chatBody;
  }

  public String getUrl() {
    return url;
  }

  /*
  public String getDataToSend() {
    return dataToSend;
  }

  public void setDataToSend(String dataToSend) {
    this.dataToSend = dataToSend;
  }
*/

  public void setUrl(String url) {
    this.url = url;
  }

  public String processResponse(HttpURLConnection connection) throws IOException {
    StringBuilder response = new StringBuilder();

    try (
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))
    ) {
      String line;
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
    } catch (IOException e) {
      // Handle the IOException appropriately - logging, re-throwing, etc.
      logger.error("Error reading response:{}", e.getMessage());
    }

    // Now you can use the 'response' StringBuilder containing the entire response
    return response.toString();
  }

  private String setupJSON(String jobDescription, String resume) {

//    String promptData = readFileAsString("prompts" + File.separator + getPromptType().name() + ".md");
    String promptData = readFileAsString("prompts" + File.separator + PROMPT_TYPE.RESUME.name() + ".md");

    promptData = promptData.replace("{resume_string}", resume).replace("{jd_string}", jobDescription);

    chatBody.setModel(getModel());
    Message systemMessage = new Message();
    systemMessage.setRole("system");
    systemMessage.setContent("Expert resume writer");
    Message userMessage = new Message();
    userMessage.setRole("user");
    userMessage.setContent(promptData);
    chatBody.setMessages(List.of(systemMessage, userMessage));

    chatBody.setTemperature(getTemperature());
    chatBody.setMaxTokens(-1);
    chatBody.setStream(false);

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    return gson.toJson(chatBody);
  }

  public LLMResponse execute() {

    try {
      HttpURLConnection connection = getConnection();

      // Get response code and handle response
      int responseCode = connection != null ? connection.getResponseCode() : 500;
      System.out.println(connection.getErrorStream());

      if (responseCode == HttpURLConnection.HTTP_OK) {
        String response = processResponse(connection);

        LLMResponse llmResponse;
        Gson gson = new Gson();
        llmResponse = gson.fromJson(response, LLMResponse.class);
        connection.disconnect();
        return llmResponse;
      } else {
        logger.error("Error: HTTP Response code - {}", responseCode);
      }
    } catch (IOException e) {

      e.printStackTrace();
      // If any error occurs during api call it will go into catch block
      logger.error(e.getMessage());
    }
    return null;
  }

  private HttpURLConnection getConnection() {
    URL obj = null; // Making an object to point to the API URL
    try {
      obj = new URI(url).toURL();
    } catch (MalformedURLException | URISyntaxException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }

    // attempts to establish a connection to the URL represented by the obj.
    HttpURLConnection connection = null;
    try {
      if (obj != null) {
        connection = (HttpURLConnection) obj.openConnection();
        connection.setConnectTimeout(7200);
        connection.setReadTimeout(7200);
      }
    } catch (IOException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }

    // Set request method and enable writing to the connection
    try {
      if (connection != null) {
        connection.setRequestMethod("POST");
      }
    } catch (ProtocolException e) {
      logger.error(e.getMessage());
    }
    if (connection != null) {
      connection.setDoOutput(true);
    }

    // Set content type header,
    // input (Content-Type) is in JSON (application/json) format.
    if (connection != null) {
      connection.setRequestProperty("Content-Type", "application/json");
    }
    String dataToSend = "{\"model\": \"gemma-3-4b-it\", \n\"messages\": [ \n{ \"role\": \"system\", \"content\": \"Always answer in rhymes. Today is Thursday\" }, \n{ \"role\": \"user\", \"content\": \"What day is it today?\" } \n], \n\"temperature\": 0.7, \n\"max_tokens\": -1, \n\"stream\": false \n}";
    String x = setupJSON(readFileAsString("sample" + File.separator + "resume.md"),
        readFileAsString("sample" + File.separator + "PointClickCare-Software Engineer.txt"));

//    System.out.println(x);

    // Calling the API and send request data
    // connection.getOutputStream() purpose is to obtain an output stream for sending data to the server.
    if (connection != null) {
      try (DataOutputStream os = new DataOutputStream(connection.getOutputStream())) {
//        os.writeBytes(dataToSend);
        os.writeBytes(x);
        os.flush();
      } catch (IOException e) {
        logger.error(e.getMessage());
      }
    }
    return connection;
  }

  public enum PROMPT_TYPE {
    COVER,
    RESUME
  }
}

