package com.nextjstemplate.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for email already exists (AUTH_005).
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class EmailAlreadyExistsException extends ClerkAuthenticationException {

    private static final long serialVersionUID = 1L;

    public EmailAlreadyExistsException(String message) {
        super(ClerkErrorConstants.AUTH_005, message != null ? message : "Email already exists", HttpStatus.CONFLICT);
    }

    public EmailAlreadyExistsException(String message, Throwable cause) {
        super(ClerkErrorConstants.AUTH_005, message != null ? message : "Email already exists", HttpStatus.CONFLICT, cause);
    }
}
