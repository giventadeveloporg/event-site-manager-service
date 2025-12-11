package com.nextjstemplate.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for batch jobs service integration.
 */
@Configuration
@ConfigurationProperties(prefix = "batch-jobs.service")
public class BatchJobProperties {

    private String url = "http://localhost:8081/batch-jobs";
    private Integer timeout = 30000;
    private Boolean enabled = true;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
