package com.eventsitemanager.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Map;

/**
 * DTO for user sign-up request with Clerk integration.
 */
@Schema(description = "Request body for user registration")
public class SignUpRequest implements Serializable {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    @Schema(description = "User's email address", example = "user@example.com", required = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Schema(description = "User's password (minimum 8 characters)", example = "SecurePass123!", required = true)
    private String password;

    @NotBlank(message = "First name is required")
    @Size(max = 255, message = "First name must not exceed 255 characters")
    @Schema(description = "User's first name", example = "John", required = true)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 255, message = "Last name must not exceed 255 characters")
    @Schema(description = "User's last name", example = "Doe", required = true)
    private String lastName;

    @NotBlank(message = "Tenant ID is required")
    @Size(max = 255, message = "Tenant ID must not exceed 255 characters")
    @Schema(description = "Tenant/organization identifier", example = "tenant_demo_001", required = true)
    private String tenantId;

    @Size(max = 255, message = "Phone must not exceed 255 characters")
    @Schema(description = "User's phone number", example = "+1234567890")
    private String phone;

    @Schema(description = "Additional metadata for the user")
    private Map<String, Object> metadata;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return (
            "SignUpRequest{" +
            "email='" +
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
            ", phone='" +
            phone +
            '\'' +
            '}'
        );
    }
}
