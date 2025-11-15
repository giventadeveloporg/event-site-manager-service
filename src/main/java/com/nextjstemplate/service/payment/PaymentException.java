package com.nextjstemplate.service.payment;

import java.util.Map;

/**
 * Exception thrown by payment service operations.
 */
public class PaymentException extends Exception {

    private static final long serialVersionUID = 1L;

    private final String errorCode;
    private final Map<String, Object> errorDetails;

    public PaymentException(String message) {
        super(message);
        this.errorCode = "PAYMENT_ERROR";
        this.errorDetails = null;
    }

    public PaymentException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "PAYMENT_ERROR";
        this.errorDetails = null;
    }

    public PaymentException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorDetails = null;
    }

    public PaymentException(String errorCode, String message, Map<String, Object> errorDetails) {
        super(message);
        this.errorCode = errorCode;
        this.errorDetails = errorDetails;
    }

    public PaymentException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorDetails = null;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Map<String, Object> getErrorDetails() {
        return errorDetails;
    }
}
