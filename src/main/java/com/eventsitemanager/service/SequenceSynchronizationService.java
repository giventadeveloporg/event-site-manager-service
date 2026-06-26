package com.eventsitemanager.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Synchronizes per-table PostgreSQL id sequences ({table}_id_seq) with MAX(id) after manual imports.
 * Spring Batch framework sequences are handled separately (sync_spring_batch_sequences.sql).
 */
@Service
public class SequenceSynchronizationService {

    private static final Logger log = LoggerFactory.getLogger(SequenceSynchronizationService.class);

    /** Spring Batch + Liquibase meta — not application {table}_id_seq tables. */
    private static final List<String> EXCLUDED_SEQUENCES = List.of(
        "batch_job_seq",
        "batch_job_execution_seq",
        "batch_step_execution_seq",
        "batch_job_execution_log_id_seq"
    );

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Sync all application per-table id sequences discovered in pg_sequences.
     *
     * @return map of sequence name to new last_value
     */
    @Transactional
    public Map<String, Long> synchronizeAllTableSequences() {
        log.info("Synchronizing per-table id sequences...");
        Map<String, Long> results = new LinkedHashMap<>();

        @SuppressWarnings("unchecked")
        List<String> sequences = entityManager
            .createNativeQuery(
                """
                SELECT sequencename
                FROM pg_sequences
                WHERE schemaname = 'public'
                  AND sequencename LIKE '%\\_id_seq' ESCAPE '\\'
                ORDER BY sequencename
                """
            )
            .getResultList();

        for (String seqName : sequences) {
            if (EXCLUDED_SEQUENCES.contains(seqName)) {
                continue;
            }
            String tableName = resolveTableName(seqName);
            if (tableName == null) {
                log.debug("Skipping sequence {} — no matching public table", seqName);
                continue;
            }
            Long newValue = synchronizeTableSequence(tableName, seqName);
            if (newValue != null) {
                results.put(seqName, newValue);
            }
        }

        log.info("Synchronized {} per-table sequences", results.size());
        return results;
    }

    /**
     * @deprecated Use {@link #synchronizeAllTableSequences()}. Kept for admin API compatibility.
     */
    @Transactional
    public Long synchronizeSequence() {
        Map<String, Long> results = synchronizeAllTableSequences();
        return results.isEmpty() ? null : results.values().stream().max(Long::compareTo).orElse(null);
    }

    /**
     * Sync a single table's sequence (used after duplicate-key recovery).
     */
    @Transactional
    public Long synchronizeTableSequence(String tableName) {
        return synchronizeTableSequence(tableName, tableName + "_id_seq");
    }

    @Transactional
    public Long synchronizeTableSequence(String tableName, String sequenceName) {
        String qualifiedSeq = "public." + sequenceName;

        if (!tableExists(tableName)) {
            log.warn("Table public.{} not found — skipping sequence sync", tableName);
            return null;
        }

        if (!sequenceExists(sequenceName)) {
            log.warn("Sequence {} not found — skipping", qualifiedSeq);
            return null;
        }

        String sql = String.format(
            "SELECT pg_catalog.setval('%s', GREATEST(COALESCE((SELECT MAX(id) FROM public.%s), 0), 1), true)",
            qualifiedSeq.replace("'", "''"),
            tableName.replace("\"", "\"\"")
        );

        try {
            Object result = entityManager.createNativeQuery(sql).getSingleResult();
            Long newValue = result != null ? ((Number) result).longValue() : null;
            log.info("Synced {} -> {} (table {})", qualifiedSeq, newValue, tableName);
            return newValue;
        } catch (Exception e) {
            log.error("Failed to sync sequence {} for table {}", qualifiedSeq, tableName, e);
            throw new RuntimeException("Failed to sync sequence " + qualifiedSeq + ": " + e.getMessage(), e);
        }
    }

    private String resolveTableName(String sequenceName) {
        if (!sequenceName.endsWith("_id_seq")) {
            return null;
        }
        String base = sequenceName.substring(0, sequenceName.length() - "_id_seq".length());
        if (tableExists(base)) {
            return base;
        }
        return null;
    }

    private boolean tableExists(String tableName) {
        Number count = (Number) entityManager
            .createNativeQuery(
                """
                SELECT COUNT(*) FROM information_schema.tables
                WHERE table_schema = 'public' AND table_name = :tableName
                """
            )
            .setParameter("tableName", tableName)
            .getSingleResult();
        return count != null && count.longValue() > 0;
    }

    private boolean sequenceExists(String sequenceName) {
        Number count = (Number) entityManager
            .createNativeQuery(
                """
                SELECT COUNT(*) FROM pg_sequences
                WHERE schemaname = 'public' AND sequencename = :seqName
                """
            )
            .setParameter("seqName", sequenceName)
            .getSingleResult();
        return count != null && count.longValue() > 0;
    }
}
