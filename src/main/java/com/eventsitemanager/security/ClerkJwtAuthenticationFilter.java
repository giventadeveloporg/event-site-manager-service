package com.eventsitemanager.security;

import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.repository.UserProfileRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT Authentication Filter for Clerk integration.
 * Extracts and validates JWT tokens from Authorization headers.
 * <p>
 * Note: OAuth2 {@link org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken}
 * is built by the resource server using {@link JwtClaimAuthoritiesMapper}; this filter supplements
 * tenant-based {@link UserProfile} resolution when claims do not carry authorities.
 */
@Component
public class ClerkJwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(ClerkJwtAuthenticationFilter.class);
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtDecoder jwtDecoder;
    private final UserProfileRepository userProfileRepository;

    public ClerkJwtAuthenticationFilter(JwtDecoder jwtDecoder, UserProfileRepository userProfileRepository) {
        this.jwtDecoder = jwtDecoder;
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        try {
            String jwt = extractJwtFromRequest(request);

            if (StringUtils.hasText(jwt)) {
                try {
                    Jwt decodedJwt = jwtDecoder.decode(jwt);

                    String clerkUserId = decodedJwt.getSubject();
                    String tenantId = decodedJwt.getClaimAsString("tenant_id");

                    log.debug("JWT validated for user: {} (tenant: {})", clerkUserId, tenantId);

                    List<GrantedAuthority> authorities = new ArrayList<>(JwtClaimAuthoritiesMapper.mapAuthorities(decodedJwt));

                    if (!authorities.isEmpty()) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            clerkUserId,
                            null,
                            authorities
                        );
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.info(
                            "Security context set for system user: {} with authorities: {}",
                            clerkUserId,
                            authorities.stream().map(GrantedAuthority::getAuthority).toList()
                        );
                    } else {
                        String tenantForProfile = tenantId;
                        if (!StringUtils.hasText(tenantForProfile)) {
                            tenantForProfile = TenantContext.getCurrentTenant();
                        }
                        UserProfile userProfile = null;
                        if (StringUtils.hasText(tenantForProfile)) {
                            userProfile = userProfileRepository.findByUserIdAndTenantId(clerkUserId, tenantForProfile).orElse(null);
                        } else {
                            log.warn(
                                "Cannot load UserProfile for JWT user {}: no tenant_id claim and no TenantContext (X-Tenant-ID / tenant param)",
                                clerkUserId
                            );
                        }

                        if (userProfile != null) {
                            if (userProfile.getUserRole() != null) {
                                authorities.add(new SimpleGrantedAuthority("ROLE_" + userProfile.getUserRole()));
                            }

                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                clerkUserId,
                                null,
                                authorities
                            );
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            log.debug(
                                "Security context set for application user: {} with authorities: {}",
                                clerkUserId,
                                authorities.stream().map(GrantedAuthority::getAuthority).toList()
                            );
                        } else {
                            log.warn("Application user not found in database for user ID: {}", clerkUserId);
                        }
                    }
                } catch (JwtException e) {
                    log.warn("Invalid JWT token: {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Error processing JWT authentication", e);
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }
}
