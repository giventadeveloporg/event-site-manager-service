package com.eventsitemanager.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for Clerk authentication integration.
 */
@Configuration
@ConfigurationProperties(prefix = "clerk")
public class ClerkProperties {

    private String publishableKey;
    private String secretKey;
    private String webhookSecret;
    private String apiVersion = "v1";
    private String baseUrl = "https://api.clerk.com";
    private String frontendApi; // Frontend API URL for OAuth

    public String getPublishableKey() {
        return publishableKey;
    }

    public void setPublishableKey(String publishableKey) {
        this.publishableKey = publishableKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getWebhookSecret() {
        return webhookSecret;
    }

    public void setWebhookSecret(String webhookSecret) {
        this.webhookSecret = webhookSecret;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiUrl() {
        return baseUrl + "/" + apiVersion;
    }

    /**
     * Clerk Frontend API URL (for OAuth and user-facing operations)
     * Example: https://humble-monkey-3.clerk.accounts.dev
     */
    public String getFrontendApi() {
        return frontendApi;
    }

    public void setFrontendApi(String frontendApi) {
        this.frontendApi = frontendApi;
    }
}
