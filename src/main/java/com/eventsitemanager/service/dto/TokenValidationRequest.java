package com.eventsitemanager.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * DTO for token validation request.
 */
@Schema(description = "Request body for token validation")
public class TokenValidationRequest implements Serializable {

    @NotBlank(message = "Token is required")
    @Schema(description = "JWT token to validate", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", required = true)
    private String token;

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "TokenValidationRequest{token='[REDACTED]'}";
    }
}
