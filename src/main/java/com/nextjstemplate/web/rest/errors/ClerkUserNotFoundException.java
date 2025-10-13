package com.nextjstemplate.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for user not found (AUTH_004).
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClerkUserNotFoundException extends ClerkAuthenticationException {

    private static final long serialVersionUID = 1L;

    public ClerkUserNotFoundException(String message) {
        super(ClerkErrorConstants.AUTH_004, message != null ? message : "User not found", HttpStatus.NOT_FOUND);
    }

    public ClerkUserNotFoundException(String message, Throwable cause) {
        super(ClerkErrorConstants.AUTH_004, message != null ? message : "User not found", HttpStatus.NOT_FOUND, cause);
    }
}
