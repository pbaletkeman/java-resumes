package ca.letkeman.resumes.optimizer;

import ca.letkeman.resumes.optimizer.responses.LLMResponse;
import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for ApiService HTTP error scenarios using MockWebServer.
 * Focus: Testing UNCOVERED code paths only (HTTP error handling).
 */
class ApiServiceHttpErrorTest {
    private ApiService apiService;
    private MockWebServer mockWebServer;

    @BeforeEach
    void setUp() throws IOException {
        apiService = new ApiService();
        apiService.setMockEnabled(false); // Disable mock mode to test real HTTP

        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    /**
     * Test HTTP 404 response - tests uncovered error handling code path.
     * Covers: lines 138-150 (non-200 response code handling)
     */
    @Test
    void testInvokeApiReturns404Error() {
        // Setup mock response with 404
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("{\"error\": \"Not found\"}")
                .addHeader("Content-Type", "application/json"));

        String endpoint = mockWebServer.url("/api/chat").toString();
        ChatBody chatBody = new ChatBody();
        chatBody.setModel("test-model");

        // Invoke API - should return null due to non-200 response
        LLMResponse response = apiService.invokeApi(chatBody, endpoint, "fake-key");

        Assertions.assertNull(response, "Should return null for 404 error");
    }

    /**
     * Test HTTP 500 response - tests uncovered error handling code path.
     * Covers: lines 138-150 (non-200 response code handling)
     */
    @Test
    void testInvokeApiReturns500Error() {
        // Setup mock response with 500
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("{\"error\": \"Internal server error\"}")
                .addHeader("Content-Type", "application/json"));

        String endpoint = mockWebServer.url("/api/chat").toString();
        ChatBody chatBody = new ChatBody();
        chatBody.setModel("test-model");

        // Invoke API - should return null due to server error
        LLMResponse response = apiService.invokeApi(chatBody, endpoint, "fake-key");

        Assertions.assertNull(response, "Should return null for 500 error");
    }

    /**
     * Test HTTP 401 Unauthorized - tests uncovered error handling code path.
     * Covers: lines 138-150 (non-200 response code handling)
     */
    @Test
    void testInvokeApiReturns401Unauthorized() {
        // Setup mock response with 401
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(401)
                .setBody("{\"error\": \"Unauthorized\"}")
                .addHeader("Content-Type", "application/json"));

        String endpoint = mockWebServer.url("/api/chat").toString();
        ChatBody chatBody = new ChatBody();
        chatBody.setModel("test-model");

        // Invoke API - should return null due to auth error
        LLMResponse response = apiService.invokeApi(chatBody, endpoint, "fake-key");

        Assertions.assertNull(response, "Should return null for 401 error");
    }

    /**
     * Test error stream reading - tests uncovered error response handling.
     * Covers: lines 140-149 (error stream reading)
     */
    @Test
    void testInvokeApiReadsErrorStream() {
        // Setup mock response with detailed error message
        String detailedError = "{\"error\": \"Detailed error message\", \"code\": \"ERR_001\"}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(detailedError)
                .addHeader("Content-Type", "application/json"));

        String endpoint = mockWebServer.url("/api/chat").toString();
        ChatBody chatBody = new ChatBody();
        chatBody.setModel("test-model");

        // Invoke API - should read error stream
        LLMResponse response = apiService.invokeApi(chatBody, endpoint, "fake-key");

        Assertions.assertNull(response, "Should return null for 400 error");
        // The error stream is read and logged (lines 140-149)
    }

    /**
     * Test HTTP 429 Too Many Requests - another error code.
     * Covers: lines 138-150 (non-200 response code handling)
     */
    @Test
    void testInvokeApiReturns429TooManyRequests() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(429)
                .setBody("{\"error\": \"Rate limit exceeded\"}")
                .addHeader("Content-Type", "application/json"));

        String endpoint = mockWebServer.url("/api/chat").toString();
        ChatBody chatBody = new ChatBody();
        chatBody.setModel("test-model");

        LLMResponse response = apiService.invokeApi(chatBody, endpoint, "fake-key");

        Assertions.assertNull(response, "Should return null for 429 error");
    }

    /**
     * Test HTTP 503 Service Unavailable.
     * Covers: lines 138-150 (non-200 response code handling)
     */
    @Test
    void testInvokeApiReturns503ServiceUnavailable() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(503)
                .setBody("{\"error\": \"Service temporarily unavailable\"}")
                .addHeader("Content-Type", "application/json"));

        String endpoint = mockWebServer.url("/api/chat").toString();
        ChatBody chatBody = new ChatBody();
        chatBody.setModel("test-model");

        LLMResponse response = apiService.invokeApi(chatBody, endpoint, "fake-key");

        Assertions.assertNull(response, "Should return null for 503 error");
    }

    /**
     * Test empty error stream - edge case.
     * Covers: lines 140-149 (error stream reading with no content)
     */
    @Test
    void testInvokeApiWithEmptyErrorStream() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody("")  // Empty body
                .addHeader("Content-Type", "application/json"));

        String endpoint = mockWebServer.url("/api/chat").toString();
        ChatBody chatBody = new ChatBody();
        chatBody.setModel("test-model");

        LLMResponse response = apiService.invokeApi(chatBody, endpoint, "fake-key");

        Assertions.assertNull(response, "Should return null for error with empty body");
    }

    /**
     * Test successful 200 response with valid JSON.
     * This tests the success path (line 152-154) which should already be covered,
     * but adds integration test with MockWebServer.
     */
    @Test
    void testInvokeApiSuccessfulResponse() {
        // Valid LLM response JSON
        String validResponse = "{\"choices\":[{\"message\":{\"content\":\"Test response\"}}]}";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(validResponse)
                .addHeader("Content-Type", "application/json"));

        String endpoint = mockWebServer.url("/api/chat").toString();
        ChatBody chatBody = new ChatBody();
        chatBody.setModel("test-model");

        LLMResponse response = apiService.invokeApi(chatBody, endpoint, "fake-key");

        Assertions.assertNotNull(response, "Should return valid response for 200 OK");
    }

    /**
     * Test 200 response but with malformed JSON.
     * Covers: exception handling in JSON parsing (line 156-159)
     */
    @Test
    void testInvokeApiWithMalformedJSON() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{ invalid json }")
                .addHeader("Content-Type", "application/json"));

        String endpoint = mockWebServer.url("/api/chat").toString();
        ChatBody chatBody = new ChatBody();
        chatBody.setModel("test-model");

        // Should handle malformed JSON gracefully
        LLMResponse response = apiService.invokeApi(chatBody, endpoint, "fake-key");

        // JSON parsing error should be caught and return null
        Assertions.assertNull(response, "Should return null for malformed JSON");
    }

    /**
     * Test with very large error response.
     * Tests error stream handling with substantial content.
     */
    @Test
    void testInvokeApiWithLargeErrorResponse() {
        // Create a large error message (simulate detailed error)
        StringBuilder largeError = new StringBuilder("{\"error\": \"");
        for (int i = 0; i < 1000; i++) {
            largeError.append("Error detail line ").append(i).append(". ");
        }
        largeError.append("\"}");

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(largeError.toString())
                .addHeader("Content-Type", "application/json"));

        String endpoint = mockWebServer.url("/api/chat").toString();
        ChatBody chatBody = new ChatBody();
        chatBody.setModel("test-model");

        LLMResponse response = apiService.invokeApi(chatBody, endpoint, "fake-key");

        Assertions.assertNull(response, "Should return null and handle large error response");
    }
}
