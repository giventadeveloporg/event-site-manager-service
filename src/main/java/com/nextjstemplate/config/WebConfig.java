package com.nextjstemplate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${CORS_ALLOWED_ORIGINS:*}")
    private String corsAllowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        CorsRegistration corsRegistration = registry
            .addMapping("/**")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
            .allowedHeaders(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers",
                "X-XSRF-TOKEN",
                "X-Tenant-ID"
            )
            .exposedHeaders("Authorization", "Link", "X-Total-Count", "X-XSRF-TOKEN")
            .maxAge(3600);

        // Handle wildcard or specific origins
        if ("*".equals(corsAllowedOrigins)) {
            // Use allowedOriginPatterns for wildcard support (Spring 5.3+)
            corsRegistration.allowedOriginPatterns("*");
            // When using wildcard, credentials must be false
            corsRegistration.allowCredentials(false);
        } else {
            // Split comma-separated origins
            String[] origins = corsAllowedOrigins.split(",");
            for (int i = 0; i < origins.length; i++) {
                origins[i] = origins[i].trim();
            }
            corsRegistration.allowedOrigins(origins);
            corsRegistration.allowCredentials(true);
        }
    }
}
