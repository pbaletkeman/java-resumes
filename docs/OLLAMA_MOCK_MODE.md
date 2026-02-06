# Ollama Mock Mode Documentation

- [Ollama Mock Mode Documentation](#ollama-mock-mode-documentation)
  - [Overview](#overview)
  - [How It Works](#how-it-works)
    - [Architecture](#architecture)
  - [Configuration](#configuration)
    - [Enable Mock Mode](#enable-mock-mode)
    - [Environment Variable](#environment-variable)
    - [Configuration Profiles](#configuration-profiles)
  - [Mock Response Types](#mock-response-types)
  - [Example Usage](#example-usage)
    - [Java Code](#java-code)
    - [Testing](#testing)
  - [Mock Response Format](#mock-response-format)
  - [Benefits](#benefits)
    - [1. No External Dependencies](#1-no-external-dependencies)
    - [2. Fast Execution](#2-fast-execution)
    - [3. Predictable Testing](#3-predictable-testing)
    - [4. Cost Reduction](#4-cost-reduction)
  - [Limitations](#limitations)
    - [Mock Content Quality](#mock-content-quality)
    - [No Real Intelligence](#no-real-intelligence)
    - [Testing Scope](#testing-scope)
  - [Best Practices](#best-practices)
    - [1. Use Mocks for Unit Tests](#1-use-mocks-for-unit-tests)
    - [2. Use Real LLM for Integration Tests](#2-use-real-llm-for-integration-tests)
    - [3. Default to Disabled in Production](#3-default-to-disabled-in-production)
    - [4. Environment-Specific Configuration](#4-environment-specific-configuration)
  - [Troubleshooting](#troubleshooting)
    - [Mock Mode Not Working](#mock-mode-not-working)
    - [Still Making Network Calls](#still-making-network-calls)
    - [Mock Responses Don't Match Expected Format](#mock-responses-dont-match-expected-format)
  - [Advanced Configuration](#advanced-configuration)
    - [Custom Mock Responses](#custom-mock-responses)
    - [Conditional Mocking](#conditional-mocking)
  - [See Also](#see-also)

---

## ✨ Overview

The java-resumes application supports a **mock mode** that simulates LLM responses without making actual network calls to Ollama. This is particularly useful for:

- **Testing**: Run tests without requiring Ollama service
- **Development**: Develop offline without network dependencies
- **CI/CD**: Run automated tests in environments where Ollama isn't available
- **Debugging**: Get predictable, reproducible responses

## 🔧 How It Works

When mock mode is enabled, the `ApiService` detects this and routes requests to `MockLlmService` instead of making HTTP calls to the configured Ollama endpoint. The mock service analyzes the prompt content and generates appropriate fake responses that match the expected format.

### Architecture

```
Request  ApiService.invokeApi()

     [Mock enabled?]

    Yes  MockLlmService.generateMockResponse()

    No  HTTP call to Ollama

        Response
```

## ⚙️ Configuration

### Enable Mock Mode

Edit `application.yml`:

```yaml
llm:
  endpoint: http://127.0.0.1:11434/v1/chat/completions
  apikey: 1234567890
  mock:
    enabled: true # Set to true to enable mock mode
```

### Environment Variable

You can also enable mock mode via environment variable:

```bash
export LLM_MOCK_ENABLED=true
java -jar resume-optimizer.jar
```

### Configuration Profiles

For testing, create `application-test.yml`:

```yaml
llm:
  mock:
    enabled: true
```

Then run with test profile:

```bash
java -jar -Dspring.profiles.active=test resume-optimizer.jar
```

## 📋 Mock Response Types

The mock service generates tailored responses based on prompt content:

| Prompt Type            | Detection Keywords     | Response Type               |
| ---------------------- | ---------------------- | --------------------------- |
| Resume                 | "resume", "optimize"   | Mock optimized resume       |
| Cover Letter           | "cover letter"         | Mock cover letter           |
| Interview HR           | "interview", "hr"      | 5 HR interview questions    |
| Interview Job-Specific | "interview", "job"     | 5 technical questions       |
| Reverse Interview      | "interview", "reverse" | 10 candidate questions      |
| Cold Email             | "cold", "email"        | Professional outreach email |
| LinkedIn Message       | "linkedin"             | Connection request messages |
| Thank You Email        | "thank", "you"         | Post-interview thank you    |
| Skills                 | "skill"                | Skills assessment           |
| Generic                | Other                  | General mock response       |

## 💡 Example Usage

### Java Code

```java
@Autowired
private ApiService apiService;

// Mock mode is automatically detected from configuration
ChatBody chatBody = new ChatBody();
chatBody.setModel("mistral");
// ... configure chatBody

LLMResponse response = apiService.invokeApi(chatBody, endpoint, apikey);
// Returns mock response if llm.mock.enabled=true
```

### Testing

```java
@SpringBootTest
@TestPropertySource(properties = {
    "llm.mock.enabled=true"
})
class MyServiceTest {

    @Autowired
    private ApiService apiService;

    @Test
    void testWithMockLlm() {
        // No Ollama service required
        // Mock responses are returned automatically
        LLMResponse response = apiService.invokeApi(chatBody, endpoint, apikey);
        assertNotNull(response);
        assertNotNull(response.getChoices());
    }
}
```

## 📦 Mock Response Format

Mock responses follow the OpenAI/Ollama API format:

```json
{
  "id": "chatcmpl-mock-a1b2c3d4",
  "object": "chat.completion",
  "created": 1706727600,
  "model": "mistral-mock",
  "choices": [
    {
      "index": 0,
      "message": {
        "role": "assistant",
        "content": "Mock response content..."
      },
      "finish_reason": "stop"
    }
  ],
  "usage": {
    "prompt_tokens": 150,
    "completion_tokens": 400,
    "total_tokens": 550
  },
  "system_fingerprint": "mock-fp-e5f6g7h8"
}
```

## ✨ Benefits

### 1. No External Dependencies

- Works without Ollama installation
- No network connectivity required
- Reduces infrastructure complexity

### 2. Fast Execution

- Instant responses (no network latency)
- Speeds up test suites significantly
- Enables rapid development cycles

### 3. Predictable Testing

- Consistent responses for same inputs
- Easier to write deterministic tests
- Reproducible results across environments

### 4. Cost Reduction

- No API usage costs
- No compute resources for LLM inference
- Lower CI/CD costs

## ⚠️ Limitations

### Mock Content Quality

- Mock responses are generic templates
- Not tailored to specific job descriptions
- Don't reflect actual LLM capabilities
- Should not be used for production

### No Real Intelligence

- Responses are predetermined patterns
- No actual understanding of context
- Limited variation in outputs

### Testing Scope

- Tests with mocks validate integration flow
- Don't test actual LLM response quality
- Can't detect LLM-specific issues

## 🌟 Best Practices

### 1. Use Mocks for Unit Tests

```java
// Enable mock mode for fast unit tests
@TestPropertySource(properties = {"llm.mock.enabled=true"})
class UnitTest { }
```

### 2. Use Real LLM for Integration Tests

```java
// Disable mock mode for integration tests
@TestPropertySource(properties = {"llm.mock.enabled=false"})
class IntegrationTest { }
```

### 3. Default to Disabled in Production

```yaml
# production application.yml
llm:
  mock:
    enabled: false # Always use real LLM in production
```

### 4. Environment-Specific Configuration

```bash
# Development
export LLM_MOCK_ENABLED=true

# Staging
export LLM_MOCK_ENABLED=false

# Production
export LLM_MOCK_ENABLED=false
```

## 🐛 Troubleshooting

### Mock Mode Not Working

1. **Check Configuration**

   ```bash
   # Verify configuration is loaded
   grep -r "llm.mock.enabled" src/main/resources/
   ```

2. **Check Logs**

   ```
   # Look for mock mode indicator in logs
   Mock mode enabled - returning simulated LLM response
   ```

3. **Verify Property Injection**

   ```java
   @Value("${llm.mock.enabled:false}")
   private boolean mockEnabled;

   LOGGER.info("Mock mode: {}", mockEnabled);
   ```

### Still Making Network Calls

- Ensure configuration property is spelled correctly: `llm.mock.enabled`
- Check application profile is loading correct configuration
- Verify no code is bypassing ApiService.invokeApi()

### Mock Responses Don't Match Expected Format

- Check that ChatBody is properly configured
- Verify message structure matches expected format
- Ensure prompt contains keywords for detection

## 🚀 Advanced Configuration

### Custom Mock Responses

You can extend `MockLlmService` to provide custom responses:

```java
@Service
public class CustomMockLlmService extends MockLlmService {

    @Override
    public LLMResponse generateMockResponse(ChatBody chatBody) {
        // Custom logic here
        return super.generateMockResponse(chatBody);
    }
}
```

### Conditional Mocking

Enable mock mode only for specific tests:

```java
@SpringBootTest
class ConditionalMockTest {

    @Autowired
    private ApiService apiService;

    @Test
    void testWithMock() {
        apiService.setMockEnabled(true);
        // Test with mock
    }

    @Test
    void testWithReal() {
        apiService.setMockEnabled(false);
        // Test with real Ollama (if available)
    }
}
```

## 📚 See Also

- [Ollama Setup Guide](OLLAMA_SETUP.md)
- [Testing Documentation](docs/TESTING.md)
- [Configuration Guide](docs/CONFIGURATION.md)
- [API Service Implementation](src/main/java/ca/letkeman/resumes/optimizer/ApiService.java)
- [Mock LLM Service](src/main/java/ca/letkeman/resumes/service/MockLlmService.java)

---

**Last Updated:** February 2, 2026
**Maintained By:** java-resumes development team
