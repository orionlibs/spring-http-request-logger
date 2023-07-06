package com.github.orionlibs.spring_http_request_logger;

import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

@NoArgsConstructor
public class LoggingInterceptor implements HandlerInterceptor
{
    private static Logger log;

    static
    {
        try
        {
            LogManager.getLogManager().readConfiguration(LoggingInterceptor.class.getResourceAsStream("/orion-spring-http-request-logger.properties"));
            log = Logger.getLogger(LoggingInterceptor.class.getName());
        }
        catch(IOException e)
        {
            System.err.println("Could not setup logger configuration for the Orion Spring HTTP Request Logger Plugin: " + e.toString());
        }
    }

    public LoggingInterceptor(Logger log)
    {
        this.log = log;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    {
        StringBuilder sb = new StringBuilder("IP: ");
        sb.append(request.getRemoteAddr());
        sb.append(", URI: ");
        sb.append(request.getMethod());
        sb.append(" ");
        sb.append(request.getRequestURI());
        log.info(sb.toString());
        return true;
    }
}
