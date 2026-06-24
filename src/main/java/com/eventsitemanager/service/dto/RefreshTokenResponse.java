package com.eventsitemanager.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

/**
 * DTO for token refresh response.
 */
@Schema(description = "Response after token refresh")
public class RefreshTokenResponse implements Serializable {

    @Schema(description = "New JWT access token")
    private String accessToken;

    @Schema(description = "Token type", example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(description = "Token expiration time in seconds", example = "86400")
    private Long expiresIn;

    @Schema(description = "New refresh token (optional)")
    private String refreshToken;

    @Schema(description = "Success message", example = "Token refreshed successfully")
    private String message;

    // Getters and Setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return (
            "RefreshTokenResponse{" + "tokenType='" + tokenType + '\'' + ", expiresIn=" + expiresIn + ", message='" + message + '\'' + '}'
        );
    }
}
