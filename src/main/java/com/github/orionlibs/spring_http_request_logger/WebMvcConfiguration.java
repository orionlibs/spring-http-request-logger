package com.github.orionlibs.spring_http_request_logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfiguration implements WebMvcConfigurer
{
    public static final String LOGGER_CONFIGURATION_FILE = "/com/github/orionlibs/spring_http_request_logger/configuration/orion-spring-http-request-logger.prop";
    public static final String FEATURE_CONFIGURATION_FILE = "/com/github/orionlibs/spring_http_request_logger/configuration/feature-configuration.prop";
    private final Environment env;
    private final LoggingInterceptor loggingInterceptor;
    private final OrionConfiguration loggerConfiguration;
    private final OrionConfiguration featureConfiguration;


    @Autowired
    public WebMvcConfiguration(final Environment env, LoggingInterceptor loggingInterceptor) throws IOException
    {
        this.env = env;
        this.loggingInterceptor = loggingInterceptor;
        this.loggerConfiguration = new OrionConfiguration();
        this.featureConfiguration = new OrionConfiguration();
        loadLoggerConfiguration();
        loadFeatureConfiguration();
        //System.out.println("1-------com.github.orionlibs.spring_http_request_logger.level=" + LogManager.getLogManager().getProperty("com.github.orionlibs.spring_http_request_logger.level"));
        //System.out.println("2-------com.github.orionlibs.spring_http_request_logger.level=" + env.getProperty("com.github.orionlibs.spring_http_request_logger.level"));
    }


    private void loadLoggerConfiguration() throws IOException
    {
        InputStream defaultConfigStream = LoggingInterceptor.class.getResourceAsStream(LOGGER_CONFIGURATION_FILE);
        try
        {
            loggerConfiguration.loadDefaultAndCustomConfiguration(defaultConfigStream, env);
            LogManager.getLogManager().readConfiguration(loggerConfiguration.getAsInputStream());
        }
        catch(IOException e)
        {
            throw new IOException("Could not setup logger configuration for the Orion Spring HTTP Request Logger Plugin: ", e);
        }
    }


    private void loadFeatureConfiguration() throws IOException
    {
        InputStream defaultConfigStream = LoggingInterceptor.class.getResourceAsStream(FEATURE_CONFIGURATION_FILE);
        try
        {
            featureConfiguration.loadDefaultAndCustomConfiguration(defaultConfigStream, env);
        }
        catch(IOException e)
        {
            throw new IOException("Could not setup feature configuration for the Orion Spring HTTP Request Logger Plugin: ", e);
        }
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        boolean enableInterceptor = Boolean.parseBoolean(featureConfiguration.getProperty("orionlibs.spring_http_request_logger.interceptor.enabled"));
        if(enableInterceptor)
        {
            registry.addInterceptor(loggingInterceptor);
        }
    }
}