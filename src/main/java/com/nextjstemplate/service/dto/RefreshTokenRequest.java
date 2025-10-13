package com.nextjstemplate.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * DTO for token refresh request.
 */
@Schema(description = "Request body for refreshing access token")
public class RefreshTokenRequest implements Serializable {

    @NotBlank(message = "Refresh token is required")
    @Schema(description = "Current JWT token or refresh token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", required = true)
    private String refreshToken;

    // Getters and Setters
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "RefreshTokenRequest{refreshToken='[REDACTED]'}";
    }
}
