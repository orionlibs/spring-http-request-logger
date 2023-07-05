package com.github.orionlibs.spring_http_request_logger;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class ListLogHandler extends Handler
{
    private final List<LogRecord> logRecords = new ArrayList<>();


    @Override
    public void publish(LogRecord record)
    {
        logRecords.add(record);
    }


    @Override
    public void flush()
    {
    }


    @Override
    public void close() throws SecurityException
    {
    }


    public List<LogRecord> getLogRecords()
    {
        return logRecords;
    }
}
