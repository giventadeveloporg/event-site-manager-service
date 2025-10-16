package com.nextjstemplate.service.dto;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for storing OAuth state data for CSRF protection.
 */
public class OAuthStateData implements Serializable {

    private String stateToken;
    private String provider;
    private String tenantId;
    private String redirectUrl;
    private Instant createdAt;
    private Instant expiresAt;

    // Constructors
    public OAuthStateData() {
        this.createdAt = Instant.now();
        this.expiresAt = this.createdAt.plusSeconds(600); // 10 minutes expiry
    }

    public OAuthStateData(String stateToken, String provider, String tenantId, String redirectUrl) {
        this();
        this.stateToken = stateToken;
        this.provider = provider;
        this.tenantId = tenantId;
        this.redirectUrl = redirectUrl;
    }

    // Getters and Setters
    public String getStateToken() {
        return stateToken;
    }

    public void setStateToken(String stateToken) {
        this.stateToken = stateToken;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    @Override
    public String toString() {
        return (
            "OAuthStateData{" +
            "stateToken='" +
            stateToken +
            "', provider='" +
            provider +
            "', tenantId='" +
            tenantId +
            "', redirectUrl='" +
            redirectUrl +
            "', createdAt=" +
            createdAt +
            ", expiresAt=" +
            expiresAt +
            '}'
        );
    }
}
