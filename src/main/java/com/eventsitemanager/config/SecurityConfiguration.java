package com.eventsitemanager.config;

import com.eventsitemanager.security.ClerkJwtAuthenticationFilter;
import com.eventsitemanager.web.filter.TenantContextFilter;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class SecurityConfiguration {

    @Value("${jwt-api-auth.username:admin}")
    private String jwtApiAuthUsername;

    @Value("${jwt-api-auth.password:admin}")
    private String jwtApiAuthPassword;

    @Value("${CORS_ALLOWED_ORIGINS:*}")
    private String corsAllowedOrigins;

    private final ClerkJwtAuthenticationFilter clerkJwtAuthenticationFilter;
    private final TenantContextFilter tenantContextFilter;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    public SecurityConfiguration(
        ClerkJwtAuthenticationFilter clerkJwtAuthenticationFilter,
        TenantContextFilter tenantContextFilter,
        JwtAuthenticationConverter jwtAuthenticationConverter
    ) {
        this.clerkJwtAuthenticationFilter = clerkJwtAuthenticationFilter;
        this.tenantContextFilter = tenantContextFilter;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authz ->
                authz
                    // Public endpoints for authentication
                    .requestMatchers(mvc.pattern("/api/authenticate"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/auth/sign-up"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/auth/sign-in"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/auth/sign-in/social"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/auth/sign-out"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/auth/verify-token"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/auth/refresh-token"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/webhooks/clerk"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/webhooks/givebutter"))
                    .permitAll()
                    // Cron endpoints - authentication handled in controller (cron secret or JWT)
                    .requestMatchers(mvc.pattern("/api/cron/**"))
                    .permitAll()
                    // All other /api/** endpoints require authentication
                    .requestMatchers(mvc.pattern("/api/**"))
                    .authenticated()
                    .anyRequest()
                    .permitAll()
            )
            .addFilterBefore(tenantContextFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(clerkJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Handle wildcard or specific origins
        if ("*".equals(corsAllowedOrigins)) {
            // Allow all origins using patterns (supports wildcards properly)
            configuration.setAllowedOriginPatterns(Arrays.asList("*"));
            configuration.setAllowCredentials(false); // Must be false when using "*" origin pattern
        } else {
            // Split comma-separated origins and trim whitespace
            String[] origins = corsAllowedOrigins.split(",");
            for (int i = 0; i < origins.length; i++) {
                origins[i] = origins[i].trim();
            }
            configuration.setAllowedOrigins(Arrays.asList(origins));
            configuration.setAllowCredentials(true);
        }

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(
            Arrays.asList(
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
        ); // Added for multi-tenant support
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Link", "X-Total-Count", "X-XSRF-TOKEN"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        // Enhanced logging for debugging
        System.out.println("=== JWT API AUTH CREDENTIALS DEBUG ===");
        System.out.println("JWT_API_AUTH_USERNAME loaded: " + jwtApiAuthUsername);
        System.out.println("JWT_API_AUTH_USERNAME length: " + (jwtApiAuthUsername != null ? jwtApiAuthUsername.length() : "null"));
        System.out.println("JWT_API_AUTH_PASSWORD loaded: " + jwtApiAuthPassword);
        System.out.println("JWT_API_AUTH_PASSWORD length: " + (jwtApiAuthPassword != null ? jwtApiAuthPassword.length() : "null"));
        System.out.println("JWT_API_AUTH_PASSWORD is null: " + (jwtApiAuthPassword == null));
        System.out.println("JWT_API_AUTH_PASSWORD is empty: " + (jwtApiAuthPassword != null && jwtApiAuthPassword.isEmpty()));

        // Mask password for security (show first 2 chars only)
        if (jwtApiAuthPassword != null && jwtApiAuthPassword.length() > 2) {
            System.out.println("JWT_API_AUTH_PASSWORD prefix: " + jwtApiAuthPassword.substring(0, 2) + "***");
        }

        String encodedPassword = encoder.encode(jwtApiAuthPassword);
        System.out.println("Encoded password length: " + encodedPassword.length());
        System.out.println("Encoded password prefix: " + encodedPassword.substring(0, 8) + "...");
        System.out.println("=== END DEBUG ===");

        return new InMemoryUserDetailsManager(
            User.withUsername(jwtApiAuthUsername).password(encodedPassword).roles("ADMIN", "USER").build()
        );
    }
}
