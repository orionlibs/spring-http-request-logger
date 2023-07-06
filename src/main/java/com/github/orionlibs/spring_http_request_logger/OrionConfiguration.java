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

/**
 * Properties-based class that holds configuration.
 */
class OrionConfiguration extends Properties
{
    /**
     * It takes default configuration and custom configuration from the Spring environment.
     * For each default configuration property, it registers that one if there is no custom
     * Spring configuration for that property. Otherwise it registers the custom one.
     * @param defaultConfiguration
     * @param springEnv
     * @throws IOException if an error occurred when reading from the input stream
     */
    void loadDefaultAndCustomConfiguration(InputStream defaultConfiguration, Environment springEnv) throws IOException
    {
        Properties tempProperties = new Properties();
        tempProperties.load(defaultConfiguration);
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


    /**
     * It converts the configuration this object holds into an InputStream
     * @return an InputStream of the configuration this object holds
     * @throws IOException if writing this property list to the specified output stream throws an IOException
     */
    InputStream getAsInputStream() throws IOException
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        store(output, null);
        return new ByteArrayInputStream(output.toByteArray());
    }
}
