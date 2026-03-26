package com.nextjstemplate.config;

import com.nextjstemplate.security.JwtClaimAuthoritiesMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

/**
 * OAuth2 resource-server JWT processing: map {@code auth} / {@code authorities} / {@code roles}
 * claims to {@link org.springframework.security.core.GrantedAuthority} on {@link org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken}.
 */
@Configuration
public class JwtSecurityResourceServerConfiguration {

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(JwtClaimAuthoritiesMapper::mapAuthorities);
        return converter;
    }
}
