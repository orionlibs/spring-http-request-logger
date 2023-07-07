package com.github.orionlibs.spring_http_request_logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        String ipAddressLog = null;
        String httpMethodLog = null;
        String uriLog = null;
        String queryParametersLog = null;
        String logRecordPattern = ConfigurationService.getProp("orionlibs.spring_http_request_logger.log.pattern.for.each.log.record.element");
        if(ConfigurationService.getBooleanProp("orionlibs.spring_http_request_logger.log.ip.address.enabled"))
        {
            ipAddressLog = String.format(logRecordPattern, "IP", request.getRemoteAddr());
        }
        if(ConfigurationService.getBooleanProp("orionlibs.spring_http_request_logger.log.http.method.enabled"))
        {
            String httpMethodsToLogPattern = ConfigurationService.getProp("orionlibs.spring_http_request_logger.log.http.methods.logged");
            String[] httpMethodsToLog = httpMethodsToLogPattern.split(",");
            if("*".equals(httpMethodsToLogPattern)
                            || Arrays.stream(httpMethodsToLog).anyMatch(m -> m.equalsIgnoreCase(request.getMethod())))
            {
                httpMethodLog = request.getMethod();
            }
        }
        if(ConfigurationService.getBooleanProp("orionlibs.spring_http_request_logger.log.uri.enabled"))
        {
            String uriPatternExpression = ConfigurationService.getProp("orionlibs.spring_http_request_logger.log.uris.logged.pattern");
            if("*".equals(uriPatternExpression))
            {
                uriLog = request.getRequestURI();
            }
            else
            {
                Pattern uriPattern = Pattern.compile(uriPatternExpression);
                Matcher matcher = uriPattern.matcher(request.getRequestURI());
                if(matcher.matches())
                {
                    uriLog = request.getRequestURI();
                }
            }
        }
        if(ConfigurationService.getBooleanProp("orionlibs.spring_http_request_logger.log.uri.query.params.enabled"))
        {
            queryParametersLog = "?" + request.getQueryString();
        }
        List<String> logElements = new ArrayList<>();
        if(ipAddressLog != null)
        {
            logElements.add(ipAddressLog);
        }
        if(httpMethodLog != null && uriLog != null)
        {
            logElements.add(String.format(logRecordPattern, "URI", httpMethodLog + " " + buildCompleteURILog(uriLog, queryParametersLog)));
        }
        else if(httpMethodLog == null && uriLog != null)
        {
            logElements.add(String.format(logRecordPattern, "URI", buildCompleteURILog(uriLog, queryParametersLog)));
        }
        else if(httpMethodLog != null && uriLog == null)
        {
            logElements.add(String.format(logRecordPattern, "URI", httpMethodLog));
        }
        if(!logElements.isEmpty())
        {
            log.info(String.join(", ", logElements.toArray(new String[0])));
        }
        if(ConfigurationService.getBooleanProp("orionlibs.spring_http_request_logger.log.request.processing.duration.enabled"))
        {
            long startTime = System.nanoTime();
            request.setAttribute("orionlibs.spring_http_request_logger", startTime);
        }
        return true;
    }


    private String buildCompleteURILog(String uriLog, String queryParametersLog)
    {
        String completeURILog = uriLog;
        if(queryParametersLog != null)
        {
            completeURILog += queryParametersLog;
        }
        return completeURILog;
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
        if(ConfigurationService.getBooleanProp("orionlibs.spring_http_request_logger.log.request.processing.duration.enabled"))
        {
            long startTime = (Long)request.getAttribute("orionlibs.spring_http_request_logger");
            long endTime = System.nanoTime();
            long executeTime = endTime - startTime;
            log.info("Handler: " + handler + " took " + executeTime + "ns");
        }
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
