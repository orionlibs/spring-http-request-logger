package com.github.orionlibs.spring_http_request_logger;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Spring MVC interceptor whose job is to log HTTP requests
 */
@NoArgsConstructor
public class LoggingInterceptor implements HandlerInterceptor
{
    public static Logger log;

    static
    {
        log = Logger.getLogger(LoggingInterceptor.class.getName());
    }

    /**
     * It logs this HTTP request's data before it is handled.
     * @param request HTTP request
     * @param response HTTP response
     * @param handler
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    {
        List<String> logElements = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if(ConfigurationService.getBooleanProp("orionlibs.spring_http_request_logger.log.ip.address.enabled"))
        {
            logElements.add("IP: " + request.getRemoteAddr());
        }
        if(ConfigurationService.getBooleanProp("orionlibs.spring_http_request_logger.log.http.method.enabled")
                        && ConfigurationService.getBooleanProp("orionlibs.spring_http_request_logger.log.uri.enabled"))
        {
            logElements.add("URI: " + request.getMethod() + " " + request.getRequestURI());
        }
        else if(!ConfigurationService.getBooleanProp("orionlibs.spring_http_request_logger.log.http.method.enabled")
                        && ConfigurationService.getBooleanProp("orionlibs.spring_http_request_logger.log.uri.enabled"))
        {
            logElements.add("URI: " + request.getRequestURI());
        }
        else if(ConfigurationService.getBooleanProp("orionlibs.spring_http_request_logger.log.http.method.enabled")
                        && !ConfigurationService.getBooleanProp("orionlibs.spring_http_request_logger.log.uri.enabled"))
        {
            logElements.add("URI: " + request.getMethod());
        }
        log.info(String.join(", ", logElements.toArray(new String[0])));
        return true;
    }


    /**
     * It logs this HTTP request's data after it is handled and before the response is built.
     * @param request HTTP request
     * @param response HTTP response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                    @Nullable ModelAndView modelAndView) throws Exception
    {
    }


    /**
     * It logs this HTTP request's data after it is handled and after the response is built.
     * @param request HTTP request
     * @param response HTTP response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                    @Nullable Exception ex) throws Exception
    {
    }
}
