package com.eventsitemanager.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for invalid tokens (AUTH_003).
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenInvalidException extends ClerkAuthenticationException {

    private static final long serialVersionUID = 1L;

    public TokenInvalidException(String message) {
        super(ClerkErrorConstants.AUTH_003, message != null ? message : "Token invalid", HttpStatus.UNAUTHORIZED);
    }

    public TokenInvalidException(String message, Throwable cause) {
        super(ClerkErrorConstants.AUTH_003, message != null ? message : "Token invalid", HttpStatus.UNAUTHORIZED, cause);
    }
}
