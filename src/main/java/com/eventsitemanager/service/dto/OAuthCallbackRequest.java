package com.eventsitemanager.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * DTO for OAuth callback request parameters.
 */
@Schema(description = "Request parameters for OAuth callback")
public class OAuthCallbackRequest implements Serializable {

    @NotBlank(message = "Provider is required")
    @Schema(description = "OAuth provider name", example = "google", required = true)
    private String provider;

    @NotBlank(message = "Authorization code is required")
    @Schema(description = "OAuth authorization code from provider", example = "4/0AX4XfWh...", required = true)
    private String code;

    @NotBlank(message = "State parameter is required")
    @Schema(description = "State parameter for CSRF protection", example = "abc123xyz", required = true)
    private String state;

    @Schema(description = "Error code if OAuth failed", example = "access_denied")
    private String error;

    @Schema(description = "Error description if OAuth failed", example = "User denied access")
    private String errorDescription;

    // Constructors
    public OAuthCallbackRequest() {}

    public OAuthCallbackRequest(String provider, String code, String state) {
        this.provider = provider;
        this.code = code;
        this.state = state;
    }

    // Getters and Setters
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    @Override
    public String toString() {
        return (
            "OAuthCallbackRequest{" +
            "provider='" +
            provider +
            "', code='[REDACTED]', state='" +
            state +
            "', error='" +
            error +
            "', errorDescription='" +
            errorDescription +
            "'}"
        );
    }
}
