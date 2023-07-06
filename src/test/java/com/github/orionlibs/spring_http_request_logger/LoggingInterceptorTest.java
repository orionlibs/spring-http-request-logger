package com.github.orionlibs.spring_http_request_logger;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.orionlibs.spring_http_request_logger.config.FakeTestingSpringConfiguration;
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
        boolean messageLogged = listLogHandler.getLogRecords().stream()
                        .anyMatch(record -> record.getMessage().contains("IP: 127.0.0.1, URI: GET /"));
        assertTrue(messageLogged);
    }


    @Test
    void test_preHandle_interceptorDisabled() throws Exception
    {
        mockMvc = MockMvcBuilders
                        .standaloneSetup(new MockController())
                        .build();
        mockMvc.perform(get("/")).andExpect(status().isOk());
        boolean messageLogged = listLogHandler.getLogRecords().stream()
                        .anyMatch(record -> record.getMessage().contains("IP: 127.0.0.1, URI: GET /"));
        assertFalse(messageLogged);
    }
}
