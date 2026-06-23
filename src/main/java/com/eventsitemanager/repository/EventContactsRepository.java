package com.eventsitemanager.repository;

import com.eventsitemanager.domain.EventContacts;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventContacts entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventContactsRepository extends JpaRepository<EventContacts, Long>, JpaSpecificationExecutor<EventContacts> {
    /**
     * Find all contacts for a specific event
     *
     * @param eventId the event ID
     * @return list of contacts
     */
    List<EventContacts> findByEventId(Long eventId);

    /**
     * Find all contacts for a specific event with pagination
     *
     * @param eventId  the event ID
     * @param pageable pagination information
     * @return page of contacts
     */
    Page<EventContacts> findByEventId(Long eventId, Pageable pageable);

    /**
     * Find contacts by name containing (case insensitive)
     *
     * @param name the name to search for
     * @return list of contacts
     */
    List<EventContacts> findByNameContainingIgnoreCase(String name);

    /**
     * Find contacts by phone number
     *
     * @param phone the phone number
     * @return list of contacts
     */
    List<EventContacts> findByPhone(String phone);

    /**
     * Find contacts by email
     *
     * @param email the email address
     * @return list of contacts
     */
    List<EventContacts> findByEmail(String email);

    /**
     * Find contacts by event ID and name
     *
     * @param eventId the event ID
     * @param name    the name
     * @return list of contacts
     */
    List<EventContacts> findByEventIdAndName(Long eventId, String name);

    /**
     * Find contacts by event ID and phone
     *
     * @param eventId the event ID
     * @param phone   the phone number
     * @return list of contacts
     */
    List<EventContacts> findByEventIdAndPhone(Long eventId, String phone);

    /**
     * Find contacts by event ID and email
     *
     * @param eventId the event ID
     * @param email   the email address
     * @return list of contacts
     */
    List<EventContacts> findByEventIdAndEmail(Long eventId, String email);

    /**
     * Count contacts for a specific event
     *
     * @param eventId the event ID
     * @return count of contacts
     */
    long countByEventId(Long eventId);

    /**
     * Find contacts by name containing and event ID
     *
     * @param eventId the event ID
     * @param name    the name to search for
     * @return list of contacts
     */
    List<EventContacts> findByEventIdAndNameContainingIgnoreCase(Long eventId, String name);

    // ===================================================================
    // Multi-tenant and nullable event_id support methods
    // ===================================================================

    /**
     * Find all contacts for a tenant that are NOT associated with any event
     *
     * @param tenantId the tenant ID
     * @return list of tenant-level contacts
     */
    @Query("SELECT ec FROM EventContacts ec WHERE ec.tenantId = :tenantId AND ec.event IS NULL")
    List<EventContacts> findByTenantIdAndEventIsNull(@Param("tenantId") String tenantId);

    /**
     * Find all contacts for a tenant that are NOT associated with a specific event
     * (includes tenant-level contacts and contacts from other events)
     *
     * @param tenantId the tenant ID
     * @param eventId  the event ID to exclude
     * @return list of available contacts
     */
    @Query("SELECT ec FROM EventContacts ec WHERE ec.tenantId = :tenantId AND (ec.event IS NULL OR ec.event.id != :eventId)")
    List<EventContacts> findByTenantIdAndEventIsNullOrNotEqualToEventId(@Param("tenantId") String tenantId, @Param("eventId") Long eventId);

    /**
     * Find contacts by tenant and event (for specific event)
     *
     * @param tenantId the tenant ID
     * @param eventId  the event ID
     * @return list of contacts
     */
    List<EventContacts> findByTenantIdAndEventId(String tenantId, Long eventId);

    /**
     * Find all contacts for a tenant (regardless of event association)
     *
     * @param tenantId the tenant ID
     * @return list of all contacts for the tenant
     */
    List<EventContacts> findByTenantId(String tenantId);

    /**
     * Count tenant-level contacts (where event is null)
     *
     * @param tenantId the tenant ID
     * @return count of tenant-level contacts
     */
    long countByTenantIdAndEventIsNull(String tenantId);
}
