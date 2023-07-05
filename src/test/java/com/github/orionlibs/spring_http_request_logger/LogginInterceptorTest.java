package com.github.orionlibs.spring_http_request_logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.github.orionlibs.spring_http_request_logger.config.FakeTestingSpringConfiguration;
import com.github.orionlibs.spring_http_request_logger.config.MockController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
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
public class LogginInterceptorTest
{
    ListAppender<ILoggingEvent> listAppender;
    LoggingInterceptor loggingInterceptor;
    private MockMvc mockMvc;


    @BeforeEach
    void setUp()
    {
        listAppender = new ListAppender<>();
        listAppender.start();
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(LoggingInterceptor.class);
        logger.addAppender(listAppender);
        this.loggingInterceptor = new LoggingInterceptor(logger);
        mockMvc = MockMvcBuilders
                        .standaloneSetup(new MockController())
                        .addInterceptors(loggingInterceptor)
                        .build();
    }


    @Test
    void preHandle() throws Exception
    {
        mockMvc.perform(get("/")).andExpect(status().isOk());
        assertEquals(1, listAppender.list.size());
        assertEquals("IP: 127.0.0.1, URI: GET /", listAppender.list.get(0).getFormattedMessage());
        assertEquals(Level.INFO, listAppender.list.get(0).getLevel());
    }
}
