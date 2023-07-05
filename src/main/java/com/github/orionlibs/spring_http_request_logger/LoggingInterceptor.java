package com.github.orionlibs.spring_http_request_logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

@NoArgsConstructor
public class LoggingInterceptor implements HandlerInterceptor
{
    private static Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);


    public LoggingInterceptor(Logger log)
    {
        this.log = log;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    {
        log.info("IP: " + request.getRemoteAddr() + ", URI: " + request.getMethod() + " " + request.getRequestURI());
        return true;
    }
}
