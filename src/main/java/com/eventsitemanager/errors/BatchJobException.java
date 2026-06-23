package com.eventsitemanager.errors;

import com.eventsitemanager.errors.ErrorConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import tech.jhipster.web.rest.errors.ProblemDetailWithCause;
import tech.jhipster.web.rest.errors.ProblemDetailWithCause.ProblemDetailWithCauseBuilder;

/**
 * Exception thrown when batch job submission fails.
 */
@SuppressWarnings("java:S110") // Inheritance tree of classes should not be too deep
public class BatchJobException extends ErrorResponseException {

    private static final long serialVersionUID = 1L;

    private final String entityName = "batchJob";

    private final String errorKey;

    public BatchJobException(String defaultMessage, String errorKey) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, errorKey);
    }

    public BatchJobException(org.springframework.http.HttpStatus status, String defaultMessage, String errorKey) {
        this(ErrorConstants.DEFAULT_TYPE, status, defaultMessage, errorKey);
    }

    public BatchJobException(java.net.URI type, String defaultMessage, String errorKey) {
        super(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ProblemDetailWithCauseBuilder
                .instance()
                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .withType(type)
                .withTitle(defaultMessage)
                .withProperty("message", "error." + errorKey)
                .withProperty("params", "batchJob")
                .build(),
            null
        );
        this.errorKey = errorKey;
    }

    public BatchJobException(java.net.URI type, org.springframework.http.HttpStatus status, String defaultMessage, String errorKey) {
        super(
            status,
            ProblemDetailWithCauseBuilder
                .instance()
                .withStatus(status.value())
                .withType(type)
                .withTitle(defaultMessage)
                .withProperty("message", "error." + errorKey)
                .withProperty("params", "batchJob")
                .build(),
            null
        );
        this.errorKey = errorKey;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }
}
