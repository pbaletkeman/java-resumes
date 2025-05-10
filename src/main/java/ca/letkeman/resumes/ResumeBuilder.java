package ca.letkeman.resumes;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.StringJoiner;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public final class ResumeBuilder {

  private static final Logger logger = LoggerFactory.getLogger(ResumeBuilder.class);


  public enum PROMPT_TYPE {
    COVER,
    RESUME
  }

  private String model;
  private Double temperature;
  private PROMPT_TYPE promptType;
  private String jobDescription;
  private String resume;
  private String serverURL;

  private ChatBody chatBody;

  public String getModel() {
    if (isNullOrEmpty(model)){
      return "gemma-3-4b-it";
    }
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public Double getTemperature() {
    if (temperature == null || temperature.isNaN() || temperature.floatValue() < 0.0 || temperature.floatValue() > 2.0){
      return 0.7;
    }
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

  public String getServerURL() {
    if (isNullOrEmpty(serverURL)) {
      return "http://127.0.0.1:1234/v1/chat/completions";
    }
    return serverURL;
  }

  public void setServerURL(String serverURL) {
    this.serverURL = serverURL;
  }

  public ChatBody getChatBody() {
    return chatBody;
  }

  public void setChatBody(ChatBody chatBody) {
    this.chatBody = chatBody;
  }

  public ResumeBuilder() {
    this.setPromptType(PROMPT_TYPE.RESUME);
    this.setTemperature(0.7);
    this.setServerURL("http://localhost:1234/v1/chat/completions");
    this.setModel("gemma-3-4b-it");
    this.chatBody = new ChatBody();
  }

  private static boolean isNullOrEmpty(String value){
    return (value == null || value.isBlank());
  }

  private static String readFileAsString(String fileName)  {
    String data = "";
    try {
      data = new String(Files.readAllBytes (Paths.get(fileName)));
    } catch (IOException e) {
      logger.error(e.toString());
    }
    return data;
  }

  String getResponse(String json)  {
////        .uri(URI.create("https://api.restful-api.dev/objects"))
    final String POST_PARAMS = "userName=Pankaj";

    URL obj = null;
    try {
      obj = new URL("https://api.restful-api.dev/objects");
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
    HttpURLConnection con = null;
    try {
      con = (HttpURLConnection) obj.openConnection();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    try {
      con.setRequestMethod("POST");
    } catch (ProtocolException e) {
      throw new RuntimeException(e);
    }
    con.setRequestProperty("User-Agent", "Mozilla/5.0");
    con.setRequestProperty("Content-Type", "application/json");
    con.setRequestProperty("Accept", "application/json");



    // For POST only - START
    con.setDoOutput(true);
    OutputStream os = null;
    try {
      os = con.getOutputStream();
      System.out.println(con.getResponseMessage());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    try {
      os.write(POST_PARAMS.getBytes());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    try {
      os.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    try {
      os.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    // For POST only - END

    int responseCode = 0;
    try {
      responseCode = con.getResponseCode();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    System.out.println("POST Response Code :: " + responseCode);

    if (responseCode == HttpURLConnection.HTTP_OK) { //success
      BufferedReader in = null;
      try {
        in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      String inputLine;
      StringBuffer response = new StringBuffer();

      while (true) {
        try {
          if (!((inputLine = in.readLine()) != null))
            break;
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        response.append(inputLine);
      }
      try {
        in.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      // print result
      System.out.println(response.toString());
    } else {
      System.out.println("POST request did not work.");
    }
    return "pete";
  }

  String getResponse(){
    return getResponse(setupJSON());
  }


  private String setupJSON(){
    return setupJSON(this.jobDescription, this.resume);
  }

  private String setupJSON(String jobDescription, String resume) {

    String promptData = readFileAsString("prompts" + File.separator + getPromptType().name() + ".md");

    promptData = promptData.replace("{resume_string}", resume).replace("{jd_string}",jobDescription);

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ResumeBuilder that = (ResumeBuilder) o;

    return new EqualsBuilder().append(getModel(), that.getModel())
        .append(getTemperature(), that.getTemperature()).append(getPromptType(), that.getPromptType())
        .append(getJobDescription(), that.getJobDescription()).append(getResume(), that.getResume())
        .append(getServerURL(), that.getServerURL()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(getModel()).append(getTemperature()).append(getPromptType())
        .append(getJobDescription()).append(getResume()).append(getServerURL()).toHashCode();
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", ResumeBuilder.class.getSimpleName() + "[", "]")
        .add("model='" + model + "'")
        .add("temperature=" + temperature)
        .add("promptType=" + promptType)
        .add("jobDescription='" + jobDescription + "'")
        .add("resume='" + resume + "'")
        .add("serverURL='" + serverURL + "'")
        .toString();
  }

  public static void main(String[] args) {
    ResumeBuilder rb = new ResumeBuilder();
    rb.setResume(readFileAsString("sample" + File.separator +"resume.md"));
    rb.setJobDescription(readFileAsString("sample" + File.separator +"PointClickCare-Software Engineer.txt"));

    String res = rb.getResponse();

//    String res = rb.setupJSON();
    logger.info(res);

//
//    PostExample example = new PostExample();
//    String json = example.bowlingJson("gemma-3-4b-it",0.7f);
//    String response = example.post(json);
//    System.out.println(response);
  }
}