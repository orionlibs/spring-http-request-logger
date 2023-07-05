package com.github.orionlibs.spring_http_request_logger;

import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoggingInterceptor implements HandlerInterceptor
{
    private static final Logger log = Logger.getLogger(LoggingInterceptor.class.getName());


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    {
        log.info("1--" + request.getContextPath());
        log.info("2--" + request.getMethod());
        log.info("3--" + request.getRequestURI());
        log.info("4--" + request.getRemoteAddr());
        log.info("5--" + request.getScheme());
        return true;
    }
}
