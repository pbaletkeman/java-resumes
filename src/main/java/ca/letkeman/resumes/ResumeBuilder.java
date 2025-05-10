package ca.letkeman.resumes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class ResumeBuilder {
  private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

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
      return "http://localhost:1234/v1/chat/completions";
    }
    return serverURL;
  }

  public void setServerURL(String serverURL) {
    this.serverURL = serverURL;
  }

  public ResumeBuilder() {
    this.setPromptType(PROMPT_TYPE.RESUME);
    this.setTemperature(0.7);
    this.setServerURL("http://localhost:1234/v1/chat/completions");
    this.setModel("gemma-3-4b-it");
  }

  private static boolean isNullOrEmpty(String value){
    return (value == null || value.isBlank());
  }

  private static String readFileAsString(String fileName)  {
    String data = "";
    try {
      data = new String(Files.readAllBytes(Paths.get(fileName)));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return data;
  }

  String getResponse(String json)  {
    final OkHttpClient client = new OkHttpClient();
    RequestBody body = RequestBody.create(json, JSON);
    Request request = new Builder()
        .url(getServerURL())
        .post(body)
        .build();
    try (Response response = client.newCall(request).execute()) {
      return response.body() != null ? response.body().string() : "";
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  String getResponse(){
    return getResponse(setupJSON());
  }


  private String setupJSON(){
    return setupJSON(this.jobDescription, this.resume);
  }

  private String setupJSON(String jobDescription, String resume) {
    String promptData = readFileAsString("prompts" + File.separator + getPromptType().name() + ".md");
    String retval = "{\n"
        + "    \"model\": \"" + getModel() + "\",\n"
        + "    \"messages\": [\n"
        + "        {\n"
        + "            \"role\": \"system\",\n"
        + "            \"content\": \"Expert resume writer\"\n"
        + "        },\n"
        + "        {\n"
        + "            \"role\": \"user\",\n"
        + "            \"content\": \"" + promptData  + "\"\n"
        + "        }\n"
        + "    ],\n"
        + "    \"temperature\": " + getTemperature() + ",\n"
        + "    \"max_tokens\": -1,\n"
        + "    \"stream\": false\n"
        + "}";
    return retval.replace("{resume_string}", resume).replace("{jd_string}",jobDescription);
  }


  public static void main(String[] args) throws IOException {
    ResumeBuilder rb = new ResumeBuilder();
    rb.setResume(readFileAsString("sample" + File.separator +"resume.md"));
    rb.setJobDescription(readFileAsString("sample" + File.separator +"PointClickCare-Software Engineer.txt"));


    System.out.println(rb.setupJSON());
//
//    PostExample example = new PostExample();
//    String json = example.bowlingJson("gemma-3-4b-it",0.7f);
//    String response = example.post(json);
//    System.out.println(response);
  }
}