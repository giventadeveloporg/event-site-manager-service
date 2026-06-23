package com.eventsitemanager.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

/**
 * Base exception for Clerk authentication errors.
 */
public class ClerkAuthenticationException extends ErrorResponseException {

    private static final long serialVersionUID = 1L;

    private final String errorCode;

    public ClerkAuthenticationException(String errorCode, String message, HttpStatus status) {
        super(status, createProblemDetail(errorCode, message, status), null);
        this.errorCode = errorCode;
    }

    public ClerkAuthenticationException(String errorCode, String message, HttpStatus status, Throwable cause) {
        super(status, createProblemDetail(errorCode, message, status), cause);
        this.errorCode = errorCode;
    }

    private static ProblemDetail createProblemDetail(String errorCode, String message, HttpStatus status) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, message);
        problemDetail.setType(ClerkErrorConstants.CLERK_ERROR_TYPE);
        problemDetail.setTitle("Clerk Authentication Error");
        problemDetail.setProperty("errorCode", errorCode);
        problemDetail.setProperty("message", message);
        return problemDetail;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
