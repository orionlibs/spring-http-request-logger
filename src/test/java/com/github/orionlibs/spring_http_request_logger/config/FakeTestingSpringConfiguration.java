package com.github.orionlibs.spring_http_request_logger.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

public class FakeTestingSpringConfiguration
{
    @Configuration
    @Import(
                    {FakeSpringMVCConfiguration.class})
    public static class FakeConfiguration
    {
    }
}