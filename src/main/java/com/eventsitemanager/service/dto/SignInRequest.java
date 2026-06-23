package com.eventsitemanager.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for user sign-in request.
 */
@Schema(description = "Request body for user authentication")
public class SignInRequest implements Serializable {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    @Schema(description = "User's email address", example = "user@example.com", required = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "User's password", example = "SecurePass123!", required = true)
    private String password;

    @NotBlank(message = "Tenant ID is required")
    @Size(max = 255, message = "Tenant ID must not exceed 255 characters")
    @Schema(description = "Tenant/organization identifier", example = "tenant_demo_001", required = true)
    private String tenantId;

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "SignInRequest{" + "email='" + email + '\'' + ", tenantId='" + tenantId + '\'' + '}';
    }
}
