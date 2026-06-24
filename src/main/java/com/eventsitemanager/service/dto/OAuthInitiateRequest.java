package com.eventsitemanager.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for OAuth initiate request parameters.
 */
@Schema(description = "Request parameters for initiating OAuth flow")
public class OAuthInitiateRequest implements Serializable {

    @NotBlank(message = "Provider is required")
    @Pattern(regexp = "^(google|facebook|github|apple|microsoft)$", message = "Invalid OAuth provider")
    @Schema(
        description = "OAuth provider name",
        example = "google",
        allowableValues = { "google", "facebook", "github", "apple", "microsoft" },
        required = true
    )
    private String provider;

    @NotBlank(message = "Tenant ID is required")
    @Size(max = 255, message = "Tenant ID must not exceed 255 characters")
    @Schema(description = "Tenant/organization identifier", example = "tenant_demo_001", required = true)
    private String tenantId;

    @Schema(description = "URL to redirect to after successful authentication", example = "/dashboard")
    private String redirectUrl;

    // Constructors
    public OAuthInitiateRequest() {}

    public OAuthInitiateRequest(String provider, String tenantId, String redirectUrl) {
        this.provider = provider;
        this.tenantId = tenantId;
        this.redirectUrl = redirectUrl;
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return ("OAuthInitiateRequest{" + "provider='" + provider + "', tenantId='" + tenantId + "', redirectUrl='" + redirectUrl + "'}");
    }
}
