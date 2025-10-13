package com.nextjstemplate.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * DTO for social sign-in response.
 */
@Schema(description = "Response after successful social provider authentication")
public class SocialSignInResponse implements Serializable {

    @Schema(description = "User profile ID in database", example = "123")
    private Long id;

    @Schema(description = "Clerk user ID", example = "user_2abc123def456")
    private String clerkUserId;

    @Schema(description = "User's email address", example = "user@example.com")
    private String email;

    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Schema(description = "Tenant ID", example = "tenant_demo_001")
    private String tenantId;

    @Schema(description = "Authentication provider", example = "google")
    private String authProvider;

    @Schema(description = "Whether this is a new user", example = "false")
    private boolean newUser;

    @Schema(description = "JWT access token for authentication")
    private String accessToken;

    @Schema(description = "Token type", example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(description = "Token expiration time in seconds", example = "86400")
    private Long expiresIn;

    @Schema(description = "Last sign-in timestamp")
    private ZonedDateTime lastSignInAt;

    @Schema(description = "Success message", example = "User authenticated successfully with Google")
    private String message;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClerkUserId() {
        return clerkUserId;
    }

    public void setClerkUserId(String clerkUserId) {
        this.clerkUserId = clerkUserId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(String authProvider) {
        this.authProvider = authProvider;
    }

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

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

    public ZonedDateTime getLastSignInAt() {
        return lastSignInAt;
    }

    public void setLastSignInAt(ZonedDateTime lastSignInAt) {
        this.lastSignInAt = lastSignInAt;
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
            "SocialSignInResponse{" +
            "id=" +
            id +
            ", clerkUserId='" +
            clerkUserId +
            "', email='" +
            email +
            "', firstName='" +
            firstName +
            "', lastName='" +
            lastName +
            "', tenantId='" +
            tenantId +
            "', authProvider='" +
            authProvider +
            "', newUser=" +
            newUser +
            ", tokenType='" +
            tokenType +
            "', expiresIn=" +
            expiresIn +
            ", lastSignInAt=" +
            lastSignInAt +
            ", message='" +
            message +
            "'}"
        );
    }
}
