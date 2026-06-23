package com.eventsitemanager.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * DTO for user sign-up response.
 */
@Schema(description = "Response after successful user registration")
public class SignUpResponse implements Serializable {

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

    @Schema(description = "JWT access token for authentication")
    private String accessToken;

    @Schema(description = "Token type", example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(description = "Token expiration time in seconds", example = "86400")
    private Long expiresIn;

    @Schema(description = "User creation timestamp")
    private ZonedDateTime createdAt;

    @Schema(description = "Success message", example = "User registered successfully")
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

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
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
            "SignUpResponse{" +
            "id=" +
            id +
            ", clerkUserId='" +
            clerkUserId +
            '\'' +
            ", email='" +
            email +
            '\'' +
            ", firstName='" +
            firstName +
            '\'' +
            ", lastName='" +
            lastName +
            '\'' +
            ", tenantId='" +
            tenantId +
            '\'' +
            ", tokenType='" +
            tokenType +
            '\'' +
            ", expiresIn=" +
            expiresIn +
            ", createdAt=" +
            createdAt +
            ", message='" +
            message +
            '\'' +
            '}'
        );
    }
}
