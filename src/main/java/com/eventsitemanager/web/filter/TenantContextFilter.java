package com.eventsitemanager.web.filter;

import com.eventsitemanager.security.TenantContext;
import com.eventsitemanager.service.TenantService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter to extract and validate tenant context from HTTP requests.
 * Tenant can be provided via:
 * 1. X-Tenant-ID header
 * 2. tenant query parameter
 * 3. JWT token claim (tenant_id)
 */
@Component
public class TenantContextFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(TenantContextFilter.class);
    private static final String TENANT_HEADER = "X-Tenant-ID";
    private static final String TENANT_PARAM = "tenant";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final TenantService tenantService;
    private final JwtDecoder jwtDecoder;

    public TenantContextFilter(TenantService tenantService, JwtDecoder jwtDecoder) {
        this.tenantService = tenantService;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        try {
            String tenantId = extractTenantId(request);

            if (tenantId != null) {
                // Validate tenant
                if (tenantService.isValidTenant(tenantId)) {
                    TenantContext.setCurrentTenant(tenantId);
                    log.debug("Tenant context set to: {}", tenantId);
                } else {
                    log.warn("Invalid tenant ID: {}", tenantId);
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid tenant ID");
                    return;
                }
            } else {
                // No tenant in request: leave TenantContext null so list/pagination endpoints
                // can treat it as wildcard (global search across all tenants) for admin dashboard.
                // Endpoints that require tenant will call requireTenantId() and return 400.
                log.debug("No tenant in request; leaving context null for optional wildcard behavior");
            }

            // Continue the filter chain
            filterChain.doFilter(request, response);
        } finally {
            // Always clear the context after request processing
            TenantContext.clear();
            log.trace("Tenant context cleared");
        }
    }

    /**
     * Extract tenant ID from request.
     * Priority: 1. Header, 2. Query param, 3. JWT claim
     */
    private String extractTenantId(HttpServletRequest request) {
        // 1. Check X-Tenant-ID header
        String tenantId = request.getHeader(TENANT_HEADER);
        if (StringUtils.hasText(tenantId)) {
            log.debug("Tenant ID extracted from header: {}", tenantId);
            return tenantId;
        }

        // 2. Check query parameter
        tenantId = request.getParameter(TENANT_PARAM);
        if (StringUtils.hasText(tenantId)) {
            log.debug("Tenant ID extracted from query parameter: {}", tenantId);
            return tenantId;
        }

        // 3. Check JWT token
        tenantId = extractTenantFromJwt(request);
        if (StringUtils.hasText(tenantId)) {
            log.debug("Tenant ID extracted from JWT: {}", tenantId);
            return tenantId;
        }

        log.debug("No tenant ID found in request");
        return null;
    }

    /**
     * Extract tenant ID from JWT token.
     */
    private String extractTenantFromJwt(HttpServletRequest request) {
        try {
            String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
                String token = bearerToken.substring(BEARER_PREFIX.length());

                try {
                    Jwt jwt = jwtDecoder.decode(token);
                    String tenantId = jwt.getClaimAsString("tenant_id");

                    if (StringUtils.hasText(tenantId)) {
                        return tenantId;
                    }
                } catch (JwtException e) {
                    log.debug("Could not decode JWT for tenant extraction: {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            log.debug("Error extracting tenant from JWT", e);
        }

        return null;
    }
}
