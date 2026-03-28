package com.nextjstemplate.properties;

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

    /**
     * Credentials used to obtain a JWT via {@code POST /api/authenticate} on this backend.
     * That JWT is sent as {@code Authorization: Bearer} to the batch-jobs microservice (same secret as JHipster JWT).
     * Defaults align with {@code API_JWT_USER}/{@code API_JWT_PASS} used by batch-jobs when calling this app.
     */
    private Auth auth = new Auth();

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

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth != null ? auth : new Auth();
    }

    public static class Auth {

        private String username = "";
        private String password = "";
        /** Base URL of this backend as seen from this process (for self-authenticate). */
        private String baseUrl = "http://localhost:8080";

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }
    }
}
