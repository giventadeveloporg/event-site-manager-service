package com.nextjstemplate.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

/**
 * DTO for user sign-out response.
 */
@Schema(description = "Response after user sign-out")
public class SignOutResponse implements Serializable {

    @Schema(description = "Sign-out success status", example = "true")
    private boolean success;

    @Schema(description = "Success message", example = "User signed out successfully")
    private String message;

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

    @Override
    public String toString() {
        return "SignOutResponse{" + "success=" + success + ", message='" + message + "'}";
    }
}
