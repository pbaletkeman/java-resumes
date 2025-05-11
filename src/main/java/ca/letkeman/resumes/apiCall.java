package ca.letkeman.resumes;

import ca.letkeman.resumes.responses.LLMResponse;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class apiCall {
  public static void main(String[] args)
      throws URISyntaxException
  {

    // API URL
    String url = "http://localhost:1234/v1/chat/completions";

    // JSON String which will be sent to the API.
//    String data_to_send = "{\"data\": \"Sample Data\"}";
    String data_to_send = "{\"model\": \"gemma-3-4b-it\", \n\"messages\": [ \n{ \"role\": \"system\", \"content\": \"Always answer in rhymes. Today is Thursday\" }, \n{ \"role\": \"user\", \"content\": \"What day is it today?\" } \n], \n\"temperature\": 0.7, \n\"max_tokens\": -1, \n\"stream\": false \n}";
    /*

*/
    try {
      URL obj = new URI(url).toURL(); // Making an object to point to the API URL

      // attempts to establish a connection to the URL represented by the obj.
      HttpURLConnection connection = (HttpURLConnection)obj.openConnection();

      // Set request method and enable writing to the connection
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);

      // Set content type header,
      // input (Content-Type) is in JSON (application/json) format.
      connection.setRequestProperty("Content-Type", "application/json");

      // Calling the API and send request data
      // connection.getOutputStream() purpose is to obtain an output stream for sending data to the server.
      try (DataOutputStream os = new DataOutputStream(connection.getOutputStream())) {
        os.writeBytes(data_to_send);
        os.flush();
      }

      // Get response code and handle response
      int responseCode = connection.getResponseCode();

      if (responseCode == HttpURLConnection.HTTP_OK) {
        // HTTP_OK or 200 response code generally means that the server ran successfully without any errors
        StringBuilder response = new StringBuilder();

        // Read response content
        // connection.getInputStream() purpose is to obtain an input stream for reading the server's response.
        try (
            BufferedReader reader = new BufferedReader( new InputStreamReader( connection.getInputStream()))) {
          String line;
          while ((line = reader.readLine()) != null) {
            response.append(line); // Adds every line to response till the end of file.
          }
        }
        LLMResponse llmResponse = new LLMResponse();
        Gson gson = new Gson();
        llmResponse = gson.fromJson(response.toString(),LLMResponse.class );
        System.out.println("Response: " + response.toString());
      }
      else {
        System.out.println("Error: HTTP Response code - " + responseCode);
      }
      connection.disconnect();
    }
    catch (IOException e) {
      // If any error occurs during api call it will go into catch block
      System.out.println(e.toString());
      e.printStackTrace();
    }
  }
}
