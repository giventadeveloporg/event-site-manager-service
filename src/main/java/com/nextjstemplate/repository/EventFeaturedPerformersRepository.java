package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventFeaturedPerformers;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventFeaturedPerformers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventFeaturedPerformersRepository
    extends JpaRepository<EventFeaturedPerformers, Long>, JpaSpecificationExecutor<EventFeaturedPerformers> {

  /**
   * Find all featured performers for a specific event
   * 
   * @param eventId the event ID
   * @return list of featured performers
   */
  List<EventFeaturedPerformers> findByEventId(Long eventId);

  /**
   * Find all featured performers for a specific event with pagination
   * 
   * @param eventId  the event ID
   * @param pageable pagination information
   * @return page of featured performers
   */
  Page<EventFeaturedPerformers> findByEventId(Long eventId, Pageable pageable);

  /**
   * Find all active featured performers for a specific event
   * 
   * @param eventId  the event ID
   * @param isActive active status
   * @return list of active featured performers
   */
  List<EventFeaturedPerformers> findByEventIdAndIsActive(Long eventId, Boolean isActive);

  /**
   * Find featured performers by name containing (case insensitive)
   * 
   * @param name the name to search for
   * @return list of featured performers
   */
  List<EventFeaturedPerformers> findByNameContainingIgnoreCase(String name);

  /**
   * Find featured performers by role
   * 
   * @param role the role to search for
   * @return list of featured performers
   */
  List<EventFeaturedPerformers> findByRole(String role);

  /**
   * Find featured performers by nationality
   * 
   * @param nationality the nationality to search for
   * @return list of featured performers
   */
  List<EventFeaturedPerformers> findByNationality(String nationality);

  /**
   * Find featured performers by event ID and role
   * 
   * @param eventId the event ID
   * @param role    the role
   * @return list of featured performers
   */
  List<EventFeaturedPerformers> findByEventIdAndRole(Long eventId, String role);

  /**
   * Find featured performers by event ID and nationality
   * 
   * @param eventId     the event ID
   * @param nationality the nationality
   * @return list of featured performers
   */
  List<EventFeaturedPerformers> findByEventIdAndNationality(Long eventId, String nationality);

  /**
   * Find headliner performers for a specific event
   * 
   * @param eventId     the event ID
   * @param isHeadliner headliner status
   * @return list of headliner performers
   */
  List<EventFeaturedPerformers> findByEventIdAndIsHeadliner(Long eventId, Boolean isHeadliner);

  /**
   * Find featured performers ordered by priority ranking
   * 
   * @param eventId the event ID
   * @return list of featured performers ordered by priority
   */
  @Query("SELECT efp FROM EventFeaturedPerformers efp WHERE efp.event.id = :eventId ORDER BY efp.priorityRanking ASC")
  List<EventFeaturedPerformers> findByEventIdOrderByPriorityRanking(@Param("eventId") Long eventId);

  /**
   * Find featured performers by performance order
   * 
   * @param eventId          the event ID
   * @param performanceOrder the performance order
   * @return list of featured performers
   */
  List<EventFeaturedPerformers> findByEventIdAndPerformanceOrder(Long eventId, Integer performanceOrder);

  /**
   * Count featured performers for a specific event
   * 
   * @param eventId the event ID
   * @return count of featured performers
   */
  long countByEventId(Long eventId);

  /**
   * Count active featured performers for a specific event
   * 
   * @param eventId  the event ID
   * @param isActive active status
   * @return count of active featured performers
   */
  long countByEventIdAndIsActive(Long eventId, Boolean isActive);
}
