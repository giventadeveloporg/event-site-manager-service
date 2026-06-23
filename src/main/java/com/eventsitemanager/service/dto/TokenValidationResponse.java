package com.eventsitemanager.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * DTO for token validation response.
 */
@Schema(description = "Response after token validation")
public class TokenValidationResponse implements Serializable {

    @Schema(description = "Validation status", example = "true")
    private boolean valid;

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

    @Schema(description = "User's roles")
    private List<String> roles;

    @Schema(description = "User's organizations with roles")
    private List<Map<String, Object>> organizations;

    @Schema(description = "Error message if validation failed")
    private String error;

    @Schema(description = "Additional token claims")
    private Map<String, Object> claims;

    // Getters and Setters
    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<Map<String, Object>> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Map<String, Object>> organizations) {
        this.organizations = organizations;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Map<String, Object> getClaims() {
        return claims;
    }

    public void setClaims(Map<String, Object> claims) {
        this.claims = claims;
    }

    @Override
    public String toString() {
        return (
            "TokenValidationResponse{" +
            "valid=" +
            valid +
            ", id=" +
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
            ", roles=" +
            roles +
            ", error='" +
            error +
            '\'' +
            '}'
        );
    }
}
