package com.nextjstemplate.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * DTO for admin user operations response.
 */
@Schema(description = "Response after admin user operation")
public class AdminUserResponse implements Serializable {

    @Schema(description = "Operation success status", example = "true")
    private boolean success;

    @Schema(description = "Response message", example = "User role updated successfully")
    private String message;

    @Schema(description = "User profile ID", example = "123")
    private Long userId;

    @Schema(description = "Updated field", example = "userRole")
    private String updatedField;

    @Schema(description = "New value", example = "ADMIN")
    private String newValue;

    @Schema(description = "Update timestamp")
    private ZonedDateTime updatedAt;

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUpdatedField() {
        return updatedField;
    }

    public void setUpdatedField(String updatedField) {
        this.updatedField = updatedField;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return (
            "AdminUserResponse{" +
            "success=" +
            success +
            ", message='" +
            message +
            "', userId=" +
            userId +
            ", updatedField='" +
            updatedField +
            "', newValue='" +
            newValue +
            "', updatedAt=" +
            updatedAt +
            '}'
        );
    }
}
