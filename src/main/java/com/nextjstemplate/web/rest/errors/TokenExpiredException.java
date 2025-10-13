package com.nextjstemplate.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for expired tokens (AUTH_002).
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenExpiredException extends ClerkAuthenticationException {

    private static final long serialVersionUID = 1L;

    public TokenExpiredException(String message) {
        super(ClerkErrorConstants.AUTH_002, message != null ? message : "Token expired", HttpStatus.UNAUTHORIZED);
    }

    public TokenExpiredException(String message, Throwable cause) {
        super(ClerkErrorConstants.AUTH_002, message != null ? message : "Token expired", HttpStatus.UNAUTHORIZED, cause);
    }
}
