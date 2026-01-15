package com.nextjstemplate.aop.sequence;

import com.nextjstemplate.service.SequenceSynchronizationService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

/**
 * Aspect for handling duplicate key constraint violations by synchronizing
 * the sequence_generator sequence and retrying the operation.
 *
 * This aspect intercepts all JPA repository save operations and automatically
 * handles sequence synchronization issues that can occur when the sequence
 * gets out of sync with manually inserted data.
 */
@Aspect
@Component
@Order(1) // Execute before logging aspect (which has default order)
public class SequenceSynchronizationAspect {

    private static final Logger log = LoggerFactory.getLogger(SequenceSynchronizationAspect.class);

    private final SequenceSynchronizationService sequenceSynchronizationService;

    public SequenceSynchronizationAspect(SequenceSynchronizationService sequenceSynchronizationService) {
        this.sequenceSynchronizationService = sequenceSynchronizationService;
    }

    /**
     * Pointcut that matches all save methods in Spring Data JPA repositories.
     * This includes:
     * - save() methods
     * - saveAll() methods
     * - saveAndFlush() methods
     */
    @Pointcut(
        "execution(* org.springframework.data.repository.CrudRepository.save(..)) || " +
        "execution(* org.springframework.data.jpa.repository.JpaRepository.save(..)) || " +
        "execution(* org.springframework.data.jpa.repository.JpaRepository.saveAndFlush(..)) || " +
        "execution(* org.springframework.data.repository.CrudRepository.saveAll(..))"
    )
    public void repositorySavePointcut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Pointcut that matches all repositories in the application.
     */
    @Pointcut("within(com.nextjstemplate.repository..*)")
    public void repositoryPointcut() {
        // Method is empty as this is just a Pointcut
    }

    /**
     * Around advice that intercepts repository save operations and handles
     * duplicate key constraint violations by synchronizing the sequence
     * and retrying the operation.
     *
     * @param joinPoint the join point (the save method being called)
     * @return the result of the save operation
     * @throws Throwable if the operation fails after retry
     */
    @Around("repositorySavePointcut() && repositoryPointcut()")
    public Object handleDuplicateKeyViolation(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        try {
            // Try the save operation
            return joinPoint.proceed();
        } catch (DataIntegrityViolationException e) {
            // Check if this is a duplicate key violation on a primary key constraint
            if (isDuplicateKeyViolation(e)) {
                log.warn(
                    "Duplicate key violation detected in {}.{}(). Synchronizing sequence_generator and retrying...",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    methodName,
                    e
                );

                try {
                    // Synchronize the sequence to fix the issue
                    Long newSequenceValue = sequenceSynchronizationService.synchronizeSequence();
                    log.info(
                        "Sequence synchronized to value: {}. Retrying {}.{}() operation...",
                        newSequenceValue,
                        joinPoint.getSignature().getDeclaringTypeName(),
                        methodName
                    );

                    // Retry the save operation once
                    Object result = joinPoint.proceed();
                    log.info(
                        "Successfully completed {}.{}() after sequence synchronization",
                        joinPoint.getSignature().getDeclaringTypeName(),
                        methodName
                    );
                    return result;
                } catch (Exception retryException) {
                    log.error(
                        "Failed to execute {}.{}() even after sequence synchronization",
                        joinPoint.getSignature().getDeclaringTypeName(),
                        methodName,
                        retryException
                    );
                    // Re-throw the original exception wrapped with context
                    throw new RuntimeException(
                        String.format(
                            "Failed to save entity due to duplicate key constraint. Sequence synchronization attempted but failed: %s",
                            retryException.getMessage()
                        ),
                        retryException
                    );
                }
            } else {
                // Not a duplicate key violation we can handle, re-throw as-is
                throw e;
            }
        }
    }

    /**
     * Check if the exception is a duplicate key violation on a primary key constraint.
     *
     * @param e the DataIntegrityViolationException
     * @return true if it's a duplicate key violation on a primary key
     */
    private boolean isDuplicateKeyViolation(DataIntegrityViolationException e) {
        if (e == null || e.getMessage() == null) {
            return false;
        }

        String message = e.getMessage().toLowerCase();
        String causeMessage = e.getCause() != null && e.getCause().getMessage() != null ? e.getCause().getMessage().toLowerCase() : "";

        // Check for duplicate key violations on primary key constraints
        // PostgreSQL error: "duplicate key value violates unique constraint" + "pkey" or "_pkey"
        return (
            (message.contains("duplicate key value violates unique constraint") ||
                causeMessage.contains("duplicate key value violates unique constraint")) &&
            (message.contains("pkey") ||
                causeMessage.contains("pkey") ||
                message.contains("primary key") ||
                causeMessage.contains("primary key"))
        );
    }
}
