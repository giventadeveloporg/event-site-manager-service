package com.nextjstemplate.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;

/**
 * Maps API / service JWT claims ({@code auth}, {@code authorities}, {@code roles}) to Spring
 * {@link GrantedAuthority} instances. Used by {@link org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter}
 * and {@link ClerkJwtAuthenticationFilter} so Bearer {@code JwtAuthenticationToken} and custom filter
 * behavior stay aligned.
 */
public final class JwtClaimAuthoritiesMapper {

    private static final String JWTADMIN_SUBJECT = "jwtadmin";

    private JwtClaimAuthoritiesMapper() {}

    /**
     * Returns granted authorities from JWT claims, including {@code jwtadmin} → {@code ROLE_ADMIN}
     * when no admin authority is present (service token fallback).
     */
    public static List<GrantedAuthority> mapAuthorities(Jwt jwt) {
        List<GrantedAuthority> authorities = extractAuthoritiesFromClaims(jwt);
        if (
            JWTADMIN_SUBJECT.equals(jwt.getSubject()) &&
            authorities.stream().noneMatch(a -> AuthoritiesConstants.ADMIN.equals(a.getAuthority()))
        ) {
            authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ADMIN));
        }
        return authorities;
    }

    private static List<GrantedAuthority> extractAuthoritiesFromClaims(Jwt jwt) {
        List<String> rawAuthorities = new ArrayList<>();

        String authClaim = jwt.getClaimAsString("auth");
        if (StringUtils.hasText(authClaim)) {
            for (String role : authClaim.split(" ")) {
                if (StringUtils.hasText(role)) {
                    rawAuthorities.add(role.trim());
                }
            }
        }

        rawAuthorities.addAll(extractStringClaimValues(jwt.getClaim("authorities")));
        rawAuthorities.addAll(extractStringClaimValues(jwt.getClaim("roles")));

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String candidate : rawAuthorities) {
            if (!StringUtils.hasText(candidate)) {
                continue;
            }
            String normalized = candidate.trim();
            if (!normalized.startsWith("ROLE_")) {
                normalized = "ROLE_" + normalized.toUpperCase(Locale.ROOT);
            }
            final String authorityName = normalized;
            if (authorities.stream().noneMatch(a -> a.getAuthority().equals(authorityName))) {
                authorities.add(new SimpleGrantedAuthority(authorityName));
            }
        }
        return authorities;
    }

    private static List<String> extractStringClaimValues(Object claimValue) {
        List<String> values = new ArrayList<>();
        if (claimValue == null) {
            return values;
        }
        if (claimValue instanceof String s) {
            if (StringUtils.hasText(s)) {
                for (String part : s.split("[,\\s]+")) {
                    if (StringUtils.hasText(part)) {
                        values.add(part.trim());
                    }
                }
            }
            return values;
        }
        if (claimValue instanceof Collection<?> collection) {
            collection
                .stream()
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .forEach(entry -> {
                    for (String part : entry.split("[,\\s]+")) {
                        if (StringUtils.hasText(part)) {
                            values.add(part.trim());
                        }
                    }
                });
        }
        return values;
    }
}
