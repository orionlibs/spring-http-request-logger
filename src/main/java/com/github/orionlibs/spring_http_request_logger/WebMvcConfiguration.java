package com.github.orionlibs.spring_http_request_logger;

import java.io.IOException;
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
    private final Environment env;
    private final LoggingInterceptor loggingInterceptor;


    @Autowired
    public WebMvcConfiguration(final Environment env, LoggingInterceptor loggingInterceptor)
    {
        this.env = env;
        this.loggingInterceptor = loggingInterceptor;

        try
        {
            LogManager.getLogManager().readConfiguration(LoggingInterceptor.class.getResourceAsStream("/com/github/orionlibs/spring_http_request_logger/configuration/orion-spring-http-request-logger.prop"));
        }
        catch(IOException e)
        {
            System.err.println("Could not setup logger configuration for the Orion Spring HTTP Request Logger Plugin: " + e.toString());
        }

        //System.out.println("1-------com.github.orionlibs.spring_http_request_logger.level=" + LogManager.getLogManager().getProperty("com.github.orionlibs.spring_http_request_logger.level"));
        //System.out.println("2-------com.github.orionlibs.spring_http_request_logger.level=" + env.getProperty("com.github.orionlibs.spring_http_request_logger.level"));
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(loggingInterceptor);
    }
}