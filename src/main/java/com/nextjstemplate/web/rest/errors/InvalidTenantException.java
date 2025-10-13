package com.nextjstemplate.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for invalid tenant (AUTH_006).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTenantException extends ClerkAuthenticationException {

    private static final long serialVersionUID = 1L;

    public InvalidTenantException(String message) {
        super(ClerkErrorConstants.AUTH_006, message != null ? message : "Invalid tenant", HttpStatus.BAD_REQUEST);
    }

    public InvalidTenantException(String message, Throwable cause) {
        super(ClerkErrorConstants.AUTH_006, message != null ? message : "Invalid tenant", HttpStatus.BAD_REQUEST, cause);
    }
}
