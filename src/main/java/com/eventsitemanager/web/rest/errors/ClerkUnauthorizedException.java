package com.eventsitemanager.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for unauthorized access (AUTH_007).
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ClerkUnauthorizedException extends ClerkAuthenticationException {

    private static final long serialVersionUID = 1L;

    public ClerkUnauthorizedException(String message) {
        super(ClerkErrorConstants.AUTH_007, message != null ? message : "Unauthorized access", HttpStatus.FORBIDDEN);
    }

    public ClerkUnauthorizedException(String message, Throwable cause) {
        super(ClerkErrorConstants.AUTH_007, message != null ? message : "Unauthorized access", HttpStatus.FORBIDDEN, cause);
    }
}
