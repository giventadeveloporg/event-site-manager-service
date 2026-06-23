package com.eventsitemanager.repository;

import com.eventsitemanager.domain.EventEmails;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventEmails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventEmailsRepository extends JpaRepository<EventEmails, Long>, JpaSpecificationExecutor<EventEmails> {
    /**
     * Find all emails for a specific event
     *
     * @param eventId the event ID
     * @return list of emails
     */
    List<EventEmails> findByEventId(Long eventId);

    /**
     * Find all emails for a specific event with pagination
     *
     * @param eventId  the event ID
     * @param pageable pagination information
     * @return page of emails
     */
    Page<EventEmails> findByEventId(Long eventId, Pageable pageable);

    /**
     * Find emails by email address
     *
     * @param email the email address
     * @return list of emails
     */
    List<EventEmails> findByEmail(String email);

    /**
     * Find emails by email address containing (case insensitive)
     *
     * @param email the email address to search for
     * @return list of emails
     */
    List<EventEmails> findByEmailContainingIgnoreCase(String email);

    /**
     * Find emails by event ID and email address
     *
     * @param eventId the event ID
     * @param email   the email address
     * @return list of emails
     */
    List<EventEmails> findByEventIdAndEmail(Long eventId, String email);

    /**
     * Find emails by event ID and email address containing
     *
     * @param eventId the event ID
     * @param email   the email address to search for
     * @return list of emails
     */
    List<EventEmails> findByEventIdAndEmailContainingIgnoreCase(Long eventId, String email);

    /**
     * Count emails for a specific event
     *
     * @param eventId the event ID
     * @return count of emails
     */
    long countByEventId(Long eventId);

    /**
     * Count emails by email address
     *
     * @param email the email address
     * @return count of emails
     */
    long countByEmail(String email);

    /**
     * Find emails by event ID ordered by creation date
     *
     * @param eventId the event ID
     * @return list of emails ordered by creation date
     */
    @Query("SELECT ee FROM EventEmails ee WHERE ee.event.id = :eventId ORDER BY ee.createdAt ASC")
    List<EventEmails> findByEventIdOrderByCreatedAt(@Param("eventId") Long eventId);

    /**
     * Find unique email addresses for a specific event
     *
     * @param eventId the event ID
     * @return list of unique email addresses
     */
    @Query("SELECT DISTINCT ee.email FROM EventEmails ee WHERE ee.event.id = :eventId")
    List<String> findDistinctEmailsByEventId(@Param("eventId") Long eventId);

    /**
     * Find a single email by event ID and email address (exact match)
     *
     * @param eventId the event ID
     * @param email   the email address
     * @return optional email
     */
    @Query("SELECT ee FROM EventEmails ee WHERE ee.event.id = :eventId AND ee.email = :email")
    Optional<EventEmails> findOneByEventIdAndEmail(@Param("eventId") Long eventId, @Param("email") String email);

    /**
     * Check if email exists for a specific event
     *
     * @param eventId the event ID
     * @param email   the email address
     * @return true if email exists
     */
    boolean existsByEventIdAndEmail(Long eventId, String email);

    // ===================================================================
    // Multi-tenant and nullable event_id support methods
    // ===================================================================

    /**
     * Find all emails for a tenant that are NOT associated with any event
     *
     * @param tenantId the tenant ID
     * @return list of tenant-level emails
     */
    @Query("SELECT ee FROM EventEmails ee WHERE ee.tenantId = :tenantId AND ee.event IS NULL")
    List<EventEmails> findByTenantIdAndEventIsNull(@Param("tenantId") String tenantId);

    /**
     * Find all emails for a tenant that are NOT associated with a specific event
     * (includes tenant-level emails and emails from other events)
     *
     * @param tenantId the tenant ID
     * @param eventId  the event ID to exclude
     * @return list of available emails
     */
    @Query("SELECT ee FROM EventEmails ee WHERE ee.tenantId = :tenantId AND (ee.event IS NULL OR ee.event.id != :eventId)")
    List<EventEmails> findByTenantIdAndEventIsNullOrNotEqualToEventId(@Param("tenantId") String tenantId, @Param("eventId") Long eventId);

    /**
     * Find emails by tenant and event (for specific event)
     *
     * @param tenantId the tenant ID
     * @param eventId  the event ID
     * @return list of emails
     */
    List<EventEmails> findByTenantIdAndEventId(String tenantId, Long eventId);

    /**
     * Find all emails for a tenant (regardless of event association)
     *
     * @param tenantId the tenant ID
     * @return list of all emails for the tenant
     */
    List<EventEmails> findByTenantId(String tenantId);

    /**
     * Count tenant-level emails (where event is null)
     *
     * @param tenantId the tenant ID
     * @return count of tenant-level emails
     */
    long countByTenantIdAndEventIsNull(String tenantId);
}
