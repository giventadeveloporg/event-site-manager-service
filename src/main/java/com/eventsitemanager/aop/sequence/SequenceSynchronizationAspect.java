package com.eventsitemanager.aop.sequence;

import com.eventsitemanager.service.SequenceSynchronizationService;
import jakarta.persistence.Table;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
 * On duplicate primary-key violations, sync the affected table's per-table id sequence and retry once.
 */
@Aspect
@Component
@Order(1)
public class SequenceSynchronizationAspect {

    private static final Logger log = LoggerFactory.getLogger(SequenceSynchronizationAspect.class);
    private static final Pattern TABLE_FROM_PKEY = Pattern.compile("(?:public\\.)?([a-z0-9_]+)_pkey", Pattern.CASE_INSENSITIVE);

    private final SequenceSynchronizationService sequenceSynchronizationService;

    public SequenceSynchronizationAspect(SequenceSynchronizationService sequenceSynchronizationService) {
        this.sequenceSynchronizationService = sequenceSynchronizationService;
    }

    @Pointcut(
        "execution(* org.springframework.data.repository.CrudRepository.save(..)) || " +
        "execution(* org.springframework.data.jpa.repository.JpaRepository.save(..)) || " +
        "execution(* org.springframework.data.jpa.repository.JpaRepository.saveAndFlush(..)) || " +
        "execution(* org.springframework.data.repository.CrudRepository.saveAll(..))"
    )
    public void repositorySavePointcut() {}

    @Pointcut("within(com.eventsitemanager.repository..*)")
    public void repositoryPointcut() {}

    @Around("repositorySavePointcut() && repositoryPointcut()")
    public Object handleDuplicateKeyViolation(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (DataIntegrityViolationException e) {
            if (isDuplicateKeyViolation(e)) {
                return handleDuplicateKeyAndRetry(joinPoint, e);
            }
            throw e;
        } catch (ConstraintViolationException e) {
            if (isDuplicateKeyViolation(e)) {
                return handleDuplicateKeyAndRetry(joinPoint, e);
            }
            throw e;
        }
    }

    private Object handleDuplicateKeyAndRetry(ProceedingJoinPoint joinPoint, Exception e) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String tableName = resolveTableName(joinPoint, args, e);

        log.warn(
            "Duplicate key violation in {}.{}() (table={}). Syncing per-table sequence and retrying...",
            joinPoint.getSignature().getDeclaringTypeName(),
            methodName,
            tableName,
            e
        );

        try {
            if (tableName != null) {
                sequenceSynchronizationService.synchronizeTableSequence(tableName);
            } else {
                sequenceSynchronizationService.synchronizeAllTableSequences();
            }

            if (args != null && args.length > 0) {
                clearEntityIdIfNeeded(args[0]);
            }

            Object result = joinPoint.proceed();
            log.info(
                "Successfully completed {}.{}() after per-table sequence sync",
                joinPoint.getSignature().getDeclaringTypeName(),
                methodName
            );
            return result;
        } catch (Exception retryException) {
            log.error(
                "Failed {}.{}() after sequence synchronization",
                joinPoint.getSignature().getDeclaringTypeName(),
                methodName,
                retryException
            );
            throw new RuntimeException(
                "Failed to save entity after per-table sequence sync: " + retryException.getMessage(),
                retryException
            );
        }
    }

    private String resolveTableName(ProceedingJoinPoint joinPoint, Object[] args, Exception e) {
        String fromConstraint = extractTableFromConstraintMessage(e);
        if (fromConstraint != null) {
            return fromConstraint;
        }
        if (args != null && args.length > 0 && args[0] != null) {
            Table table = args[0].getClass().getAnnotation(Table.class);
            if (table != null && table.name() != null && !table.name().isBlank()) {
                return table.name();
            }
        }
        return null;
    }

    private String extractTableFromConstraintMessage(Exception e) {
        String combined = buildMessageChain(e);
        Matcher m = TABLE_FROM_PKEY.matcher(combined);
        if (m.find()) {
            return m.group(1).toLowerCase();
        }
        return null;
    }

    private String buildMessageChain(Exception e) {
        StringBuilder sb = new StringBuilder();
        Throwable t = e;
        while (t != null) {
            if (t.getMessage() != null) {
                sb.append(t.getMessage()).append(' ');
            }
            t = t.getCause();
        }
        return sb.toString().toLowerCase();
    }

    private boolean isDuplicateKeyViolation(Exception e) {
        if (e == null) {
            return false;
        }
        String message = buildMessageChain(e);
        boolean hasDuplicateKey = message.contains("duplicate key value violates unique constraint");
        boolean hasPrimaryKey = message.contains("pkey") || message.contains("primary key");
        return hasDuplicateKey && hasPrimaryKey;
    }

    private void clearEntityIdIfNeeded(Object entity) {
        if (entity == null) {
            return;
        }
        try {
            java.lang.reflect.Method getIdMethod = entity.getClass().getMethod("getId");
            Object id = getIdMethod.invoke(entity);
            if (id != null) {
                java.lang.reflect.Method setIdMethod = entity.getClass().getMethod("setId", Long.class);
                setIdMethod.invoke(entity, (Long) null);
                log.debug("Cleared entity ID before retry to force sequence generation");
            }
        } catch (Exception ex) {
            log.debug("Could not clear entity ID via reflection: {}", ex.getMessage());
        }
    }
}
