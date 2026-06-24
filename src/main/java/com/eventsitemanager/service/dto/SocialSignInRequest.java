package com.eventsitemanager.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for social sign-in request.
 */
@Schema(description = "Request body for social provider authentication")
public class SocialSignInRequest implements Serializable {

    @NotBlank(message = "Clerk session token is required")
    @Schema(description = "Clerk session token from social login", example = "sess_2abc123def456", required = true)
    private String clerkSessionToken;

    @NotBlank(message = "Tenant ID is required")
    @Size(max = 255, message = "Tenant ID must not exceed 255 characters")
    @Schema(description = "Tenant/organization identifier", example = "tenant_demo_001", required = true)
    private String tenantId;

    @Schema(
        description = "Social provider name",
        example = "google",
        allowableValues = { "google", "github", "facebook", "apple", "microsoft" }
    )
    private String provider;

    // Getters and Setters
    public String getClerkSessionToken() {
        return clerkSessionToken;
    }

    public void setClerkSessionToken(String clerkSessionToken) {
        this.clerkSessionToken = clerkSessionToken;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Override
    public String toString() {
        return "SocialSignInRequest{" + "clerkSessionToken='[REDACTED]', tenantId='" + tenantId + "', provider='" + provider + "'}";
    }
}
