package com.github.orionlibs.spring_http_request_logger;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.orionlibs.spring_http_request_logger.config.FakeTestingSpringConfiguration;
import com.github.orionlibs.spring_http_request_logger.config.ListLogHandler;
import com.github.orionlibs.spring_http_request_logger.config.MockController;
import java.io.IOException;
import java.util.logging.LogManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("testing")
@ContextConfiguration(classes = FakeTestingSpringConfiguration.FakeConfiguration.class)
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class LoggingInterceptorTest
{
    private ListLogHandler listLogHandler;
    private MockMvc mockMvc;


    @BeforeEach
    void setUp()
    {
        try
        {
            listLogHandler = new ListLogHandler();
            LogManager.getLogManager().readConfiguration(LoggingInterceptorTest.class.getResourceAsStream("/com/github/orionlibs/spring_http_request_logger/configuration/orion-spring-http-request-logger.prop"));
            LoggingInterceptor.log.addHandler(listLogHandler);
            mockMvc = MockMvcBuilders
                            .standaloneSetup(new MockController())
                            .addInterceptors(new LoggingInterceptor())
                            .build();
        }
        catch(IOException e)
        {
            System.err.println("Could not setup logger configuration for the Orion Spring HTTP Request Logger Plugin: " + e.toString());
        }
    }


    @AfterEach
    public void teardown()
    {
        LoggingInterceptor.log.removeHandler(listLogHandler);
    }


    @Test
    void test_preHandle_interceptorEnabled() throws Exception
    {
        mockMvc.perform(get("/")).andExpect(status().isOk());
        assertTrue(listLogHandler.getLogRecords().stream()
                        .anyMatch(record -> record.getMessage().contains("IP: 127.0.0.1, URI: GET /")));
    }


    @Test
    void test_preHandle_interceptorDisabled() throws Exception
    {
        mockMvc = MockMvcBuilders
                        .standaloneSetup(new MockController())
                        .build();
        mockMvc.perform(get("/")).andExpect(status().isOk());
        assertFalse(listLogHandler.getLogRecords().stream()
                        .anyMatch(record -> record.getMessage().contains("IP: 127.0.0.1, URI: GET /")));
    }


    @Test
    void test_preHandle_IPAddressLoggingDisabled() throws Exception
    {
        ConfigurationService.updateProp("orionlibs.spring_http_request_logger.log.ip.address.enabled", "false");
        mockMvc.perform(get("/")).andExpect(status().isOk());
        assertTrue(listLogHandler.getLogRecords().stream()
                        .anyMatch(record -> record.getMessage().contains("URI: GET /")));
        ConfigurationService.updateProp("orionlibs.spring_http_request_logger.log.ip.address.enabled", "true");
    }


    @Test
    void test_preHandle_HTTPMethodLoggingDisabled() throws Exception
    {
        ConfigurationService.updateProp("orionlibs.spring_http_request_logger.log.http.method.enabled", "false");
        mockMvc.perform(get("/")).andExpect(status().isOk());
        assertTrue(listLogHandler.getLogRecords().stream()
                        .anyMatch(record -> record.getMessage().contains("IP: 127.0.0.1, URI: /")));
        ConfigurationService.updateProp("orionlibs.spring_http_request_logger.log.http.method.enabled", "true");
    }


    @Test
    void test_preHandle_URILoggingDisabled() throws Exception
    {
        ConfigurationService.updateProp("orionlibs.spring_http_request_logger.log.uri.enabled", "false");
        mockMvc.perform(get("/")).andExpect(status().isOk());
        assertTrue(listLogHandler.getLogRecords().stream()
                        .anyMatch(record -> record.getMessage().contains("IP: 127.0.0.1, URI: GET")));
        ConfigurationService.updateProp("orionlibs.spring_http_request_logger.log.uri.enabled", "true");
    }


    @Test
    void test_preHandle_IPAddressAndURILoggingDisabled() throws Exception
    {
        ConfigurationService.updateProp("orionlibs.spring_http_request_logger.log.ip.address.enabled", "false");
        ConfigurationService.updateProp("orionlibs.spring_http_request_logger.log.uri.enabled", "false");
        mockMvc.perform(get("/")).andExpect(status().isOk());
        assertTrue(listLogHandler.getLogRecords().stream()
                        .anyMatch(record -> record.getMessage().contains("URI: GET")));
        ConfigurationService.updateProp("orionlibs.spring_http_request_logger.log.ip.address.enabled", "true");
        ConfigurationService.updateProp("orionlibs.spring_http_request_logger.log.uri.enabled", "true");
    }


    @Test
    void test_preHandle_IPAddressAndHTTPMethodLoggingDisabled() throws Exception
    {
        ConfigurationService.updateProp("orionlibs.spring_http_request_logger.log.ip.address.enabled", "false");
        ConfigurationService.updateProp("orionlibs.spring_http_request_logger.log.http.method.enabled", "false");
        mockMvc.perform(get("/")).andExpect(status().isOk());
        assertTrue(listLogHandler.getLogRecords().stream()
                        .anyMatch(record -> record.getMessage().contains("URI: /")));
        ConfigurationService.updateProp("orionlibs.spring_http_request_logger.log.ip.address.enabled", "true");
        ConfigurationService.updateProp("orionlibs.spring_http_request_logger.log.http.method.enabled", "true");
    }


    @Test
    void test_preHandle_specificHTTPMethods() throws Exception
    {
        ConfigurationService.updateProp("orionlibs.spring_http_request_logger.log.http.methods.logged", "GET");
        mockMvc.perform(get("/")).andExpect(status().isOk());
        assertTrue(listLogHandler.getLogRecords().stream()
                        .anyMatch(record -> record.getMessage().contains("IP: 127.0.0.1, URI: GET /")));
        ConfigurationService.updateProp("orionlibs.spring_http_request_logger.log.http.methods.logged", "GET,POST");
        mockMvc.perform(get("/")).andExpect(status().isOk());
        assertTrue(listLogHandler.getLogRecords().stream()
                        .anyMatch(record -> record.getMessage().contains("IP: 127.0.0.1, URI: GET /")));
        ConfigurationService.updateProp("orionlibs.spring_http_request_logger.log.http.methods.logged", "POST");
        mockMvc.perform(get("/")).andExpect(status().isOk());
        assertTrue(listLogHandler.getLogRecords().stream()
                        .anyMatch(record -> record.getMessage().contains("IP: 127.0.0.1")));
        ConfigurationService.updateProp("orionlibs.spring_http_request_logger.log.http.methods.logged", "POST,PUT");
        mockMvc.perform(get("/")).andExpect(status().isOk());
        assertTrue(listLogHandler.getLogRecords().stream()
                        .anyMatch(record -> record.getMessage().contains("IP: 127.0.0.1")));
        ConfigurationService.updateProp("orionlibs.spring_http_request_logger.log.http.methods.logged", "*");
    }


    @Test
    void test_preHandle_specificURIPatterns() throws Exception
    {
        ConfigurationService.updateProp("orionlibs.spring_http_request_logger.log.uris.logged.pattern", "^(/[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*)$");
        mockMvc.perform(get("/")).andExpect(status().isOk());
        assertTrue(listLogHandler.getLogRecords().stream()
                        .anyMatch(record -> record.getMessage().contains("IP: 127.0.0.1, URI: GET /")));
        ConfigurationService.updateProp("orionlibs.spring_http_request_logger.log.uris.logged.pattern", "^(/api/v1/[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*)$");
        mockMvc.perform(get("/")).andExpect(status().isOk());
        assertTrue(listLogHandler.getLogRecords().stream()
                        .anyMatch(record -> record.getMessage().contains("IP: 127.0.0.1, URI: GET /")));
        mockMvc.perform(get("/api/v1/users")).andExpect(status().isOk());
        assertTrue(listLogHandler.getLogRecords().stream()
                        .anyMatch(record -> record.getMessage().contains("IP: 127.0.0.1, URI: GET /api/v1/users")));
        ConfigurationService.updateProp("orionlibs.spring_http_request_logger.log.uris.logged.pattern", ".*/users/.*");
        mockMvc.perform(get("/api/v1/users")).andExpect(status().isOk());
        assertTrue(listLogHandler.getLogRecords().stream()
                        .anyMatch(record -> record.getMessage().contains("IP: 127.0.0.1, URI: GET /api/v1/users")));
        ConfigurationService.updateProp("orionlibs.spring_http_request_logger.log.uris.logged.pattern", "*");
    }


    @Test
    void test_preHandle_queryParameters() throws Exception
    {
        mockMvc.perform(get("/search?query=hello+there&options=45")).andExpect(status().isOk());
        assertTrue(listLogHandler.getLogRecords().stream()
                        .anyMatch(record -> record.getMessage().contains("IP: 127.0.0.1, URI: GET /search?query=hello+there&options=45")));
    }
}
