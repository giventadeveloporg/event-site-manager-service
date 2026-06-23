package com.eventsitemanager.aop.sequence;

import com.eventsitemanager.service.SequenceSynchronizationService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.exception.ConstraintViolationException;
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
    @Pointcut("within(com.eventsitemanager.repository..*)")
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
                return handleDuplicateKeyAndRetry(joinPoint, e);
            } else {
                // Not a duplicate key violation we can handle, re-throw as-is
                throw e;
            }
        } catch (ConstraintViolationException e) {
            // Also catch Hibernate's ConstraintViolationException directly
            // This can happen when the exception hasn't been wrapped by Spring yet
            if (isDuplicateKeyViolation(e)) {
                return handleDuplicateKeyAndRetry(joinPoint, e);
            } else {
                // Not a duplicate key violation we can handle, re-throw as-is
                throw e;
            }
        }
    }

    /**
     * Handles duplicate key violation by synchronizing sequence and retrying.
     */
    private Object handleDuplicateKeyAndRetry(ProceedingJoinPoint joinPoint, Exception e) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
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

            // Clear entity ID if it's set, to force Hibernate to use sequence
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                Object entity = args[0];
                clearEntityIdIfNeeded(entity);
            }

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
    }

    /**
     * Check if the exception is a duplicate key violation on a primary key constraint.
     *
     * @param e the exception (DataIntegrityViolationException or ConstraintViolationException)
     * @return true if it's a duplicate key violation on a primary key
     */
    private boolean isDuplicateKeyViolation(Exception e) {
        if (e == null) {
            return false;
        }

        // Get all exception messages from the chain
        String message = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
        Throwable cause = e.getCause();
        String causeMessage = "";

        // Traverse the exception chain to find the root cause message
        while (cause != null) {
            if (cause.getMessage() != null) {
                causeMessage = cause.getMessage().toLowerCase();
                if (causeMessage.contains("duplicate key")) {
                    break;
                }
            }
            cause = cause.getCause();
        }

        // Check for duplicate key violations on primary key constraints
        // PostgreSQL error: "duplicate key value violates unique constraint" + "pkey" or "_pkey"
        boolean hasDuplicateKeyMessage =
            message.contains("duplicate key value violates unique constraint") ||
            causeMessage.contains("duplicate key value violates unique constraint");

        boolean hasPrimaryKeyIndicator =
            message.contains("pkey") ||
            causeMessage.contains("pkey") ||
            message.contains("primary key") ||
            causeMessage.contains("primary key") ||
            message.contains("_pkey") ||
            causeMessage.contains("_pkey");

        return hasDuplicateKeyMessage && hasPrimaryKeyIndicator;
    }

    /**
     * Clear entity ID if it's set, to force Hibernate to use the sequence generator.
     * This helps when an entity has an ID set that conflicts with existing data.
     */
    private void clearEntityIdIfNeeded(Object entity) {
        if (entity == null) {
            return;
        }

        try {
            // Use reflection to check if entity has an ID set
            java.lang.reflect.Method getIdMethod = entity.getClass().getMethod("getId");
            Object id = getIdMethod.invoke(entity);

            if (id != null) {
                // Clear the ID to force Hibernate to use sequence generator
                java.lang.reflect.Method setIdMethod = entity.getClass().getMethod("setId", Long.class);
                setIdMethod.invoke(entity, (Long) null);
                log.debug("Cleared entity ID before retry to force sequence generation");
            }
        } catch (Exception ex) {
            // If reflection fails, just log and continue - not critical
            log.debug("Could not clear entity ID via reflection: {}", ex.getMessage());
        }
    }
}
