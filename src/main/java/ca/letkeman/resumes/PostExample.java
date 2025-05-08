package ca.letkeman.resumes;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostExample {
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

  String bowlingJson() {
    String y = "{\n"
        + "    \"model\": \"gemma-3-4b-it\",\n"
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
        + "    \"temperature\": 0.7,\n"
        + "    \"max_tokens\": -1,\n"
        + "    \"stream\": false\n"
        + "}";
    return y;
  }

  public static void main(String[] args) throws IOException {
    PostExample example = new PostExample();
    String json = example.bowlingJson();
    String response = example.post(json);
    System.out.println(response);
  }
}