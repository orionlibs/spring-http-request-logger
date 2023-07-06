package com.github.orionlibs.spring_http_request_logger;

import static com.github.orionlibs.spring_http_request_logger.WebMvcConfiguration.LOGGER_CONFIGURATION_FILE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.orionlibs.spring_http_request_logger.config.FakeTestingSpringConfiguration;
import com.github.orionlibs.spring_http_request_logger.spring.FakeSpringEnvironment;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("testing")
@ContextConfiguration(classes = FakeTestingSpringConfiguration.FakeConfiguration.class)
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class OrionConfigurationTest
{
    private InputStream configStream;
    private FakeSpringEnvironment env;
    private OrionConfiguration configuration;


    @BeforeEach
    void setUp()
    {
        this.configuration = new OrionConfiguration();
        configStream = OrionConfigurationTest.class.getResourceAsStream(LOGGER_CONFIGURATION_FILE);
    }


    @Test
    void test_loadDefaultAndCustomConfiguration_customConfigurationOverridesDefault() throws Exception
    {
        buildFakeSpringEnvironmentWithProperties();
        configuration.loadDefaultAndCustomConfiguration(configStream, env);
        assertEquals("SEVERE", configuration.getProperty("com.github.orionlibs.spring_http_request_logger.level"));
    }


    @Test
    void test_loadDefaultAndCustomConfiguration_noCustomConfigurationOverridesDefault() throws Exception
    {
        buildFakeSpringEnvironmentWithoutProperties();
        configuration.loadDefaultAndCustomConfiguration(configStream, env);
        assertEquals("INFO", configuration.getProperty("com.github.orionlibs.spring_http_request_logger.level"));
    }


    @Test
    void test_getAsInputStream() throws Exception
    {
        buildFakeSpringEnvironmentWithoutProperties();
        configuration.loadDefaultAndCustomConfiguration(configStream, env);
        OrionConfiguration configurationCopy = new OrionConfiguration();
        configurationCopy.loadDefaultAndCustomConfiguration(configuration.getAsInputStream(), env);
        assertEquals("INFO", configuration.getProperty("com.github.orionlibs.spring_http_request_logger.level"));
        assertEquals("INFO", configurationCopy.getProperty("com.github.orionlibs.spring_http_request_logger.level"));
    }


    private void buildFakeSpringEnvironmentWithProperties()
    {
        Map<String, String> properties = new HashMap<>();
        properties.put("com.github.orionlibs.spring_http_request_logger.level", "SEVERE");
        this.env = new FakeSpringEnvironment(properties);
    }


    private void buildFakeSpringEnvironmentWithoutProperties()
    {
        this.env = new FakeSpringEnvironment();
    }
}
