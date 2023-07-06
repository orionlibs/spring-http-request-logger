package com.github.orionlibs.spring_http_request_logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import org.springframework.core.env.Environment;

class OrionConfiguration extends Properties
{
    void loadDefaultAndCustomConfiguration(InputStream propertiesFileInput, Environment springEnv) throws IOException
    {
        Properties tempProperties = new Properties();
        tempProperties.load(propertiesFileInput);
        Map<String, String> allProperties = new HashMap<>();
        for(Entry<Object, Object> prop : tempProperties.entrySet())
        {
            String value = springEnv.getProperty((String)prop.getKey());
            if(value == null)
            {
                value = (String)prop.getValue();
            }
            allProperties.put((String)prop.getKey(), value);
        }
        putAll(allProperties);
    }


    InputStream getAsInputStream() throws IOException
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        store(output, null);
        return new ByteArrayInputStream(output.toByteArray());
    }
}
