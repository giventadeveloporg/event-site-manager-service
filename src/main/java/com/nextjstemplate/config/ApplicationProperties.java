package com.nextjstemplate.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Nextjs Template Boot.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Cors cors = new Cors();
    private final Security security = new Security();
    private final Monitoring monitoring = new Monitoring();
    private final Urls urls = new Urls();

    public Cors getCors() {
        return cors;
    }

    public Security getSecurity() {
        return security;
    }

    public Monitoring getMonitoring() {
        return monitoring;
    }

    public Urls getUrls() {
        return urls;
    }

    public static class Cors {
        private String allowedOrigins = "http://localhost:3000,http://localhost:4200,http://localhost:8080";
        private String allowedMethods = "GET,POST,PUT,DELETE,OPTIONS,HEAD,PATCH";
        private String allowedHeaders = "*";
        private boolean allowCredentials = true;
        private int maxAge = 3600;

        public String getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(String allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }

        public String getAllowedMethods() {
            return allowedMethods;
        }

        public void setAllowedMethods(String allowedMethods) {
            this.allowedMethods = allowedMethods;
        }

        public String getAllowedHeaders() {
            return allowedHeaders;
        }

        public void setAllowedHeaders(String allowedHeaders) {
            this.allowedHeaders = allowedHeaders;
        }

        public boolean isAllowCredentials() {
            return allowCredentials;
        }

        public void setAllowCredentials(boolean allowCredentials) {
            this.allowCredentials = allowCredentials;
        }

        public int getMaxAge() {
            return maxAge;
        }

        public void setMaxAge(int maxAge) {
            this.maxAge = maxAge;
        }
    }

    public static class Security {
        private final RateLimiting rateLimiting = new RateLimiting();

        public RateLimiting getRateLimiting() {
            return rateLimiting;
        }

        public static class RateLimiting {
            private boolean enabled = false;
            private int requestsPerMinute = 1000;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public int getRequestsPerMinute() {
                return requestsPerMinute;
            }

            public void setRequestsPerMinute(int requestsPerMinute) {
                this.requestsPerMinute = requestsPerMinute;
            }
        }
    }

    public static class Monitoring {
        private boolean enableTenantMetrics = true;
        private boolean enablePerformanceTracking = true;
        private boolean enableDebugLogging = true;

        public boolean isEnableTenantMetrics() {
            return enableTenantMetrics;
        }

        public void setEnableTenantMetrics(boolean enableTenantMetrics) {
            this.enableTenantMetrics = enableTenantMetrics;
        }

        public boolean isEnablePerformanceTracking() {
            return enablePerformanceTracking;
        }

        public void setEnablePerformanceTracking(boolean enablePerformanceTracking) {
            this.enablePerformanceTracking = enablePerformanceTracking;
        }

        public boolean isEnableDebugLogging() {
            return enableDebugLogging;
        }

        public void setEnableDebugLogging(boolean enableDebugLogging) {
            this.enableDebugLogging = enableDebugLogging;
        }
    }

    public static class Urls {
        private String qrcodeScanHostPrefix = "http://localhost:3000/admin";
        private String emailHostUrlPrefix = "http://localhost:3000";

        public String getQrcodeScanHostPrefix() {
            return qrcodeScanHostPrefix;
        }

        public void setQrcodeScanHostPrefix(String qrcodeScanHostPrefix) {
            this.qrcodeScanHostPrefix = qrcodeScanHostPrefix;
        }

        public String getEmailHostUrlPrefix() {
            return emailHostUrlPrefix;
        }

        public void setEmailHostUrlPrefix(String emailHostUrlPrefix) {
            this.emailHostUrlPrefix = emailHostUrlPrefix;
        }
    }

    // jhipster-needle-application-properties-property
    // jhipster-needle-application-properties-property-getter
    // jhipster-needle-application-properties-property-class
}
