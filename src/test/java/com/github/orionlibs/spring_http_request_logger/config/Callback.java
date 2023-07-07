package com.github.orionlibs.spring_http_request_logger.config;

import java.util.logging.Logger;

public class Callback implements Runnable
{
    public static Logger log = Logger.getLogger(Callback.class.getName());


    @Override public void run()
    {
        log.info("callback has been called");
    }
}
