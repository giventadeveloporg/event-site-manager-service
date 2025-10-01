package com.nextjstemplate.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(mvc.pattern("/api/authenticate"))
                        .permitAll()
                        .requestMatchers(mvc.pattern("/api/**"))
                        .authenticated()
                        .anyRequest()
                        .permitAll())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()));
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
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
                        "X-XSRF-TOKEN"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Link", "X-Total-Count", "X-XSRF-TOKEN"));
        configuration.setAllowCredentials(false);
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
        String jwtApiAuthUsername = System.getenv("JWT_API_AUTH_USERNAME");
        String jwtApiAuthPassword = System.getenv("JWT_API_AUTH_PASSWORD");

        // Use environment variables with fallback to defaults for development
        if (jwtApiAuthUsername == null || jwtApiAuthUsername.isEmpty()) {
            jwtApiAuthUsername = "admin";
        }
        if (jwtApiAuthPassword == null || jwtApiAuthPassword.isEmpty()) {
            jwtApiAuthPassword = "admin";
        }

        return new InMemoryUserDetailsManager(
                User.withUsername(jwtApiAuthUsername).password(encoder.encode(jwtApiAuthPassword))
                        .roles("ADMIN", "USER").build());
    }
}
