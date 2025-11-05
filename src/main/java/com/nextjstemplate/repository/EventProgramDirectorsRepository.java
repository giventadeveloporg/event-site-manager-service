package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventProgramDirectors;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventProgramDirectors entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventProgramDirectorsRepository
    extends JpaRepository<EventProgramDirectors, Long>, JpaSpecificationExecutor<EventProgramDirectors> {
    /**
     * Find all program directors for a specific event
     *
     * @param eventId the event ID
     * @return list of program directors
     */
    List<EventProgramDirectors> findByEventId(Long eventId);

    /**
     * Find all program directors for a specific event with pagination
     *
     * @param eventId  the event ID
     * @param pageable pagination information
     * @return page of program directors
     */
    Page<EventProgramDirectors> findByEventId(Long eventId, Pageable pageable);

    /**
     * Find program directors by name containing (case insensitive)
     *
     * @param name the name to search for
     * @return list of program directors
     */
    List<EventProgramDirectors> findByNameContainingIgnoreCase(String name);

    /**
     * Find program directors by event ID and name
     *
     * @param eventId the event ID
     * @param name    the name
     * @return list of program directors
     */
    List<EventProgramDirectors> findByEventIdAndName(Long eventId, String name);

    /**
     * Find program directors by event ID and name containing
     *
     * @param eventId the event ID
     * @param name    the name to search for
     * @return list of program directors
     */
    List<EventProgramDirectors> findByEventIdAndNameContainingIgnoreCase(Long eventId, String name);

    /**
     * Find program directors by bio containing (case insensitive)
     *
     * @param bio the bio to search for
     * @return list of program directors
     */
    List<EventProgramDirectors> findByBioContainingIgnoreCase(String bio);

    /**
     * Find program directors by event ID and bio containing
     *
     * @param eventId the event ID
     * @param bio     the bio to search for
     * @return list of program directors
     */
    List<EventProgramDirectors> findByEventIdAndBioContainingIgnoreCase(Long eventId, String bio);

    /**
     * Count program directors for a specific event
     *
     * @param eventId the event ID
     * @return count of program directors
     */
    long countByEventId(Long eventId);

    /**
     * Find program directors by event ID ordered by creation date
     *
     * @param eventId the event ID
     * @return list of program directors ordered by creation date
     */
    @Query("SELECT epd FROM EventProgramDirectors epd WHERE epd.event.id = :eventId ORDER BY epd.createdAt ASC")
    List<EventProgramDirectors> findByEventIdOrderByCreatedAt(@Param("eventId") Long eventId);

    /**
     * Find program directors by event ID ordered by name
     *
     * @param eventId the event ID
     * @return list of program directors ordered by name
     */
    @Query("SELECT epd FROM EventProgramDirectors epd WHERE epd.event.id = :eventId ORDER BY epd.name ASC")
    List<EventProgramDirectors> findByEventIdOrderByName(@Param("eventId") Long eventId);

    /**
     * Find program directors with photo URLs
     *
     * @param eventId the event ID
     * @return list of program directors with photos
     */
    @Query("SELECT epd FROM EventProgramDirectors epd WHERE epd.event.id = :eventId AND epd.photoUrl IS NOT NULL")
    List<EventProgramDirectors> findByEventIdWithPhoto(@Param("eventId") Long eventId);

    /**
     * Find program directors without photo URLs
     *
     * @param eventId the event ID
     * @return list of program directors without photos
     */
    @Query("SELECT epd FROM EventProgramDirectors epd WHERE epd.event.id = :eventId AND epd.photoUrl IS NULL")
    List<EventProgramDirectors> findByEventIdWithoutPhoto(@Param("eventId") Long eventId);

    /**
     * Find program directors with bio
     *
     * @param eventId the event ID
     * @return list of program directors with bio
     */
    @Query("SELECT epd FROM EventProgramDirectors epd WHERE epd.event.id = :eventId AND epd.bio IS NOT NULL AND epd.bio != ''")
    List<EventProgramDirectors> findByEventIdWithBio(@Param("eventId") Long eventId);

    /**
     * Find program directors without bio
     *
     * @param eventId the event ID
     * @return list of program directors without bio
     */
    @Query("SELECT epd FROM EventProgramDirectors epd WHERE epd.event.id = :eventId AND (epd.bio IS NULL OR epd.bio = '')")
    List<EventProgramDirectors> findByEventIdWithoutBio(@Param("eventId") Long eventId);

    // ===================================================================
    // Multi-tenant and nullable event_id support methods
    // ===================================================================

    /**
     * Find all program directors for a tenant that are NOT associated with any event
     *
     * @param tenantId the tenant ID
     * @return list of tenant-level program directors
     */
    @Query("SELECT epd FROM EventProgramDirectors epd WHERE epd.tenantId = :tenantId AND epd.event IS NULL")
    List<EventProgramDirectors> findByTenantIdAndEventIsNull(@Param("tenantId") String tenantId);

    /**
     * Find all program directors for a tenant that are NOT associated with a specific event
     * (includes tenant-level directors and directors from other events)
     *
     * @param tenantId the tenant ID
     * @param eventId  the event ID to exclude
     * @return list of available program directors
     */
    @Query("SELECT epd FROM EventProgramDirectors epd WHERE epd.tenantId = :tenantId AND (epd.event IS NULL OR epd.event.id != :eventId)")
    List<EventProgramDirectors> findByTenantIdAndEventIsNullOrNotEqualToEventId(
        @Param("tenantId") String tenantId,
        @Param("eventId") Long eventId
    );

    /**
     * Find program directors by tenant and event (for specific event)
     *
     * @param tenantId the tenant ID
     * @param eventId  the event ID
     * @return list of program directors
     */
    List<EventProgramDirectors> findByTenantIdAndEventId(String tenantId, Long eventId);

    /**
     * Find all program directors for a tenant (regardless of event association)
     *
     * @param tenantId the tenant ID
     * @return list of all program directors for the tenant
     */
    List<EventProgramDirectors> findByTenantId(String tenantId);

    /**
     * Count tenant-level program directors (where event is null)
     *
     * @param tenantId the tenant ID
     * @return count of tenant-level program directors
     */
    long countByTenantIdAndEventIsNull(String tenantId);
}
