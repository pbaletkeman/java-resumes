package ca.letkeman.resumes;

import ca.letkeman.resumes.responses.LLMResponse;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class apiCall {

  private static final Logger logger = LoggerFactory.getLogger(apiCall.class);

  // API URL
  private String url; // = "http://localhost:1234/v1/chat/completions";

  // JSON String which will be sent to the API.
  private String dataToSend;

  public apiCall(String url, String dataToSend) {
    this.url = url;
    this.dataToSend = dataToSend;
  }

  public apiCall() {
    dataToSend = "{\"model\": \"gemma-3-4b-it\", \n\"messages\": [ \n{ \"role\": \"system\", \"content\": \"Always answer in rhymes. Today is Thursday\" }, \n{ \"role\": \"user\", \"content\": \"What day is it today?\" } \n], \n\"temperature\": 0.7, \n\"max_tokens\": -1, \n\"stream\": false \n}";
    url = "http://localhost:1234/v1/chat/completions";
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getDataToSend() {
    return dataToSend;
  }

  public void setDataToSend(String dataToSend) {
    this.dataToSend = dataToSend;
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
      logger.error("Error reading response:{}" , e.getMessage());
    }

    // Now you can use the 'response' StringBuilder containing the entire response
    return response.toString();
  }


  public LLMResponse execute() {

    try {
      HttpURLConnection connection = getConnection();

      // Get response code and handle response
      int responseCode = connection != null ? connection.getResponseCode() : 500;

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
      logger.error(e.getMessage());
    }

    // attempts to establish a connection to the URL represented by the obj.
    HttpURLConnection connection = null;
    try {
      if (obj != null) {
        connection = (HttpURLConnection) obj.openConnection();
      }
    } catch (IOException e) {
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

    // Calling the API and send request data
    // connection.getOutputStream() purpose is to obtain an output stream for sending data to the server.
    if (connection != null) {
      try (DataOutputStream os = new DataOutputStream(connection.getOutputStream())) {
        os.writeBytes(dataToSend);
        os.flush();
      } catch (IOException e) {
        logger.error(e.getMessage());
      }
    }
    return connection;
  }
}

