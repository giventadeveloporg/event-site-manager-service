package com.nextjstemplate.security;

import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.repository.UserProfileRepository;
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
                    // Decode and validate the JWT
                    Jwt decodedJwt = jwtDecoder.decode(jwt);

                    // Extract user information from JWT claims
                    String clerkUserId = decodedJwt.getSubject();
                    String tenantId = decodedJwt.getClaimAsString("tenant_id");

                    log.debug("JWT validated for user: {} (tenant: {})", clerkUserId, tenantId);

                    // Extract authorities from JWT claims for system users (like jwtadmin)
                    String authClaim = decodedJwt.getClaimAsString("auth");
                    List<GrantedAuthority> authorities = new ArrayList<>();

                    if (authClaim != null && !authClaim.isEmpty()) {
                        // System user with roles in JWT (e.g., "ROLE_ADMIN ROLE_USER")
                        log.debug("Using roles from JWT auth claim for system user: {}", clerkUserId);
                        String[] roles = authClaim.split(" ");
                        for (String role : roles) {
                            authorities.add(new SimpleGrantedAuthority(role));
                        }

                        // Create authentication token for system user
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            clerkUserId,
                            null,
                            authorities
                        );
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.debug("Security context set for system user: {}", clerkUserId);
                    } else {
                        // Regular application user - load from database
                        UserProfile userProfile = userProfileRepository.findByUserId(clerkUserId).orElse(null);

                        if (userProfile != null) {
                            // Create authorities from user roles
                            if (userProfile.getUserRole() != null) {
                                authorities.add(new SimpleGrantedAuthority("ROLE_" + userProfile.getUserRole()));
                            }

                            // Create authentication token
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                clerkUserId,
                                null,
                                authorities
                            );
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                            // Set authentication in security context
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            log.debug("Security context set for application user: {}", clerkUserId);
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

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Extract JWT token from the Authorization header.
     *
     * @param request the HTTP request
     * @return the JWT token or null if not present
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }
}
