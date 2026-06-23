package com.eventsitemanager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jhipster", ignoreUnknownFields = true)
public class CustomJHipsterProperties {
    // Empty class that ignores unknown fields
}
