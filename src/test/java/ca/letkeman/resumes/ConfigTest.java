package ca.letkeman.resumes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ConfigTest {

    @Test
    void testDefaultConstructor() {
        Config config = new Config();
        Assertions.assertNull(config.getEndpoint());
        Assertions.assertNull(config.getApikey());
        Assertions.assertNull(config.getModel());
    }

    @Test
    void testParameterizedConstructor() {
        String endpoint = "http://localhost:11434";
        String apikey = "test-key";
        String model = "mistral";
        
        Config config = new Config(endpoint, apikey, model);
        
        Assertions.assertEquals(endpoint, config.getEndpoint());
        Assertions.assertEquals(apikey, config.getApikey());
        Assertions.assertEquals(model, config.getModel());
    }

    @Test
    void testSettersAndGetters() {
        Config config = new Config();
        
        config.setEndpoint("http://test.com");
        Assertions.assertEquals("http://test.com", config.getEndpoint());
        
        config.setApikey("my-api-key");
        Assertions.assertEquals("my-api-key", config.getApikey());
        
        config.setModel("gpt-4");
        Assertions.assertEquals("gpt-4", config.getModel());
    }

    @Test
    void testSetEndpointWithNull() {
        Config config = new Config("endpoint", "key", "model");
        config.setEndpoint(null);
        Assertions.assertNull(config.getEndpoint());
    }

    @Test
    void testSetApikeyWithNull() {
        Config config = new Config("endpoint", "key", "model");
        config.setApikey(null);
        Assertions.assertNull(config.getApikey());
    }

    @Test
    void testSetModelWithNull() {
        Config config = new Config("endpoint", "key", "model");
        config.setModel(null);
        Assertions.assertNull(config.getModel());
    }

    @Test
    void testSetEndpointWithEmptyString() {
        Config config = new Config();
        config.setEndpoint("");
        Assertions.assertEquals("", config.getEndpoint());
    }

    @Test
    void testSetApikeyWithEmptyString() {
        Config config = new Config();
        config.setApikey("");
        Assertions.assertEquals("", config.getApikey());
    }

    @Test
    void testSetModelWithEmptyString() {
        Config config = new Config();
        config.setModel("");
        Assertions.assertEquals("", config.getModel());
    }

    @Test
    void testConstructorWithNullValues() {
        Config config = new Config(null, null, null);
        Assertions.assertNull(config.getEndpoint());
        Assertions.assertNull(config.getApikey());
        Assertions.assertNull(config.getModel());
    }

    @Test
    void testConstructorWithEmptyStrings() {
        Config config = new Config("", "", "");
        Assertions.assertEquals("", config.getEndpoint());
        Assertions.assertEquals("", config.getApikey());
        Assertions.assertEquals("", config.getModel());
    }

    @Test
    void testSetEndpointWithSpecialCharacters() {
        Config config = new Config();
        String specialEndpoint = "http://localhost:8080/api/v1?param=value&key=123";
        config.setEndpoint(specialEndpoint);
        Assertions.assertEquals(specialEndpoint, config.getEndpoint());
    }

    @Test
    void testSetApikeyWithSpecialCharacters() {
        Config config = new Config();
        String specialKey = "key-with-dashes_and_underscores.123";
        config.setApikey(specialKey);
        Assertions.assertEquals(specialKey, config.getApikey());
    }

    @Test
    void testMultipleSettersOnSameInstance() {
        Config config = new Config();
        
        config.setEndpoint("endpoint1");
        config.setApikey("key1");
        config.setModel("model1");
        
        Assertions.assertEquals("endpoint1", config.getEndpoint());
        Assertions.assertEquals("key1", config.getApikey());
        Assertions.assertEquals("model1", config.getModel());
        
        // Update values
        config.setEndpoint("endpoint2");
        config.setApikey("key2");
        config.setModel("model2");
        
        Assertions.assertEquals("endpoint2", config.getEndpoint());
        Assertions.assertEquals("key2", config.getApikey());
        Assertions.assertEquals("model2", config.getModel());
    }
}
