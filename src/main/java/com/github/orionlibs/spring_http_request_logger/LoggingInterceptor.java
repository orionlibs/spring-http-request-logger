package com.github.orionlibs.spring_http_request_logger;

import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@NoArgsConstructor
public class LoggingInterceptor implements HandlerInterceptor
{
    public static Logger log;

    static
    {
        log = Logger.getLogger(LoggingInterceptor.class.getName());
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


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                    @Nullable ModelAndView modelAndView) throws Exception
    {
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                    @Nullable Exception ex) throws Exception
    {
    }
}
