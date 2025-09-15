package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventSponsorsJoin;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventSponsorsJoin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventSponsorsJoinRepository
    extends JpaRepository<EventSponsorsJoin, Long>, JpaSpecificationExecutor<EventSponsorsJoin> {

  /**
   * Find all sponsor associations for a specific event
   * 
   * @param eventId the event ID
   * @return list of sponsor associations
   */
  List<EventSponsorsJoin> findByEventId(Long eventId);

  /**
   * Find all sponsor associations for a specific event with pagination
   * 
   * @param eventId  the event ID
   * @param pageable pagination information
   * @return page of sponsor associations
   */
  Page<EventSponsorsJoin> findByEventId(Long eventId, Pageable pageable);

  /**
   * Find all event associations for a specific sponsor
   * 
   * @param sponsorId the sponsor ID
   * @return list of event associations
   */
  List<EventSponsorsJoin> findBySponsorId(Long sponsorId);

  /**
   * Find all event associations for a specific sponsor with pagination
   * 
   * @param sponsorId the sponsor ID
   * @param pageable  pagination information
   * @return page of event associations
   */
  Page<EventSponsorsJoin> findBySponsorId(Long sponsorId, Pageable pageable);

  /**
   * Find specific event-sponsor association
   * 
   * @param eventId   the event ID
   * @param sponsorId the sponsor ID
   * @return optional event-sponsor association
   */
  Optional<EventSponsorsJoin> findByEventIdAndSponsorId(Long eventId, Long sponsorId);

  /**
   * Check if event-sponsor association exists
   * 
   * @param eventId   the event ID
   * @param sponsorId the sponsor ID
   * @return true if association exists
   */
  boolean existsByEventIdAndSponsorId(Long eventId, Long sponsorId);

  /**
   * Count sponsor associations for a specific event
   * 
   * @param eventId the event ID
   * @return count of sponsor associations
   */
  long countByEventId(Long eventId);

  /**
   * Count event associations for a specific sponsor
   * 
   * @param sponsorId the sponsor ID
   * @return count of event associations
   */
  long countBySponsorId(Long sponsorId);

  /**
   * Delete all associations for a specific event
   * 
   * @param eventId the event ID
   */
  void deleteByEventId(Long eventId);

  /**
   * Delete all associations for a specific sponsor
   * 
   * @param sponsorId the sponsor ID
   */
  void deleteBySponsorId(Long sponsorId);

  /**
   * Delete specific event-sponsor association
   * 
   * @param eventId   the event ID
   * @param sponsorId the sponsor ID
   */
  void deleteByEventIdAndSponsorId(Long eventId, Long sponsorId);

  /**
   * Find associations by event ID ordered by creation date
   * 
   * @param eventId the event ID
   * @return list of associations ordered by creation date
   */
  @Query("SELECT esj FROM EventSponsorsJoin esj WHERE esj.event.id = :eventId ORDER BY esj.createdAt ASC")
  List<EventSponsorsJoin> findByEventIdOrderByCreatedAt(@Param("eventId") Long eventId);

  /**
   * Find associations by sponsor ID ordered by creation date
   * 
   * @param sponsorId the sponsor ID
   * @return list of associations ordered by creation date
   */
  @Query("SELECT esj FROM EventSponsorsJoin esj WHERE esj.sponsor.id = :sponsorId ORDER BY esj.createdAt ASC")
  List<EventSponsorsJoin> findBySponsorIdOrderByCreatedAt(@Param("sponsorId") Long sponsorId);
}
