package ca.letkeman.resumes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class PostExample {
  public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

  final OkHttpClient client = new OkHttpClient();

  String post(String json) throws IOException {
    RequestBody body = RequestBody.create(json, JSON);
    Request request = new Builder()
        .url("http://localhost:1234/v1/chat/completions")
        .post(body)
        .build();
    try (Response response = client.newCall(request).execute()) {
      return response.body() != null ? response.body().string() : "";
    }
  }

  static String readFileAsString(String fileName)  {
    String data = "";
    try {
      data = new String(
          Files.readAllBytes(Paths.get(fileName)));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return data;
  }


  class ModelParameters {
    private String model;
    private float temperature;
    private String url;

    public String getModel() {
      return model;
    }

    public void setModel(String model) {
      this.model = model;
    }

    public float getTemperature() {
      return temperature;
    }

    public void setTemperature(float temperature) {
      this.temperature = temperature;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public ModelParameters(String model, float temperature, String url) {
      this.model = model;
      this.temperature = temperature;
      this.url = url;
    }
  }

  String bowlingJson(String model, float temperature) {
    String y = "{\n"
        + "    \"model\": \"" + model + "\",\n"
        + "    \"messages\": [\n"
        + "        {\n"
        + "            \"role\": \"system\",\n"
        + "            \"content\": \"Always answer in rhymes. Today is Thursday\"\n"
        + "        },\n"
        + "        {\n"
        + "            \"role\": \"user\",\n"
        + "            \"content\": \"What day is it today?\"\n"
        + "        }\n"
        + "    ],\n"
        + "    \"temperature\": " + temperature + ",\n"
        + "    \"max_tokens\": -1,\n"
        + "    \"stream\": false\n"
        + "}";
    return y;
  }

  public static void main(String[] args) throws IOException {
    PostExample example = new PostExample();
    String json = example.bowlingJson("gemma-3-4b-it",0.7f);
    String response = example.post(json);
    System.out.println(response);
  }
}