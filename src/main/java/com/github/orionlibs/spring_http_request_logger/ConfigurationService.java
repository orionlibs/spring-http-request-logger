package com.github.orionlibs.spring_http_request_logger;

public class ConfigurationService
{
    private static OrionConfiguration configurationRegistry;


    static void registerConfiguration(OrionConfiguration configuration)
    {
        configurationRegistry = configuration;
    }


    public static String getProp(String key)
    {
        return configurationRegistry.getProperty(key);
    }


    public static Boolean getBooleanProp(String key)
    {
        return Boolean.parseBoolean(configurationRegistry.getProperty(key));
    }


    public static void updateProp(String key, String value)
    {
        configurationRegistry.updateProp(key, value);
    }
}
