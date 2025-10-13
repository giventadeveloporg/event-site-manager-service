package com.nextjstemplate.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for invalid credentials (AUTH_001).
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidCredentialsException extends ClerkAuthenticationException {

    private static final long serialVersionUID = 1L;

    public InvalidCredentialsException(String message) {
        super(ClerkErrorConstants.AUTH_001, message != null ? message : "Invalid credentials", HttpStatus.UNAUTHORIZED);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(ClerkErrorConstants.AUTH_001, message != null ? message : "Invalid credentials", HttpStatus.UNAUTHORIZED, cause);
    }
}
