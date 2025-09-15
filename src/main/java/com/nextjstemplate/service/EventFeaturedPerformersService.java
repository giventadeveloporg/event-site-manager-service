package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.EventFeaturedPerformersDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing
 * {@link com.nextjstemplate.domain.EventFeaturedPerformers}.
 */
public interface EventFeaturedPerformersService {
  /**
   * Save a eventFeaturedPerformers.
   *
   * @param eventFeaturedPerformersDTO the entity to save.
   * @return the persisted entity.
   */
  EventFeaturedPerformersDTO save(EventFeaturedPerformersDTO eventFeaturedPerformersDTO);

  /**
   * Updates a eventFeaturedPerformers.
   *
   * @param eventFeaturedPerformersDTO the entity to update.
   * @return the persisted entity.
   */
  EventFeaturedPerformersDTO update(EventFeaturedPerformersDTO eventFeaturedPerformersDTO);

  /**
   * Partially updates a eventFeaturedPerformers.
   *
   * @param eventFeaturedPerformersDTO the entity to update partially.
   * @return the persisted entity.
   */
  Optional<EventFeaturedPerformersDTO> partialUpdate(EventFeaturedPerformersDTO eventFeaturedPerformersDTO);

  /**
   * Get all the eventFeaturedPerformers.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  Page<EventFeaturedPerformersDTO> findAll(Pageable pageable);

  /**
   * Get the "id" eventFeaturedPerformers.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Optional<EventFeaturedPerformersDTO> findOne(Long id);

  /**
   * Delete the "id" eventFeaturedPerformers.
   *
   * @param id the id of the entity.
   */
  void delete(Long id);

  /**
   * Get all eventFeaturedPerformers for a specific event.
   *
   * @param eventId the id of the event.
   * @return the list of entities.
   */
  List<EventFeaturedPerformersDTO> findByEventId(Long eventId);

  /**
   * Get all eventFeaturedPerformers for a specific event with pagination.
   *
   * @param eventId  the id of the event.
   * @param pageable the pagination information.
   * @return the page of entities.
   */
  Page<EventFeaturedPerformersDTO> findByEventId(Long eventId, Pageable pageable);

  /**
   * Get all active eventFeaturedPerformers for a specific event.
   *
   * @param eventId  the id of the event.
   * @param isActive active status.
   * @return the list of entities.
   */
  List<EventFeaturedPerformersDTO> findByEventIdAndIsActive(Long eventId, Boolean isActive);

  /**
   * Get all eventFeaturedPerformers by name containing.
   *
   * @param name the name to search for.
   * @return the list of entities.
   */
  List<EventFeaturedPerformersDTO> findByNameContaining(String name);

  /**
   * Get all eventFeaturedPerformers by role.
   *
   * @param role the role to search for.
   * @return the list of entities.
   */
  List<EventFeaturedPerformersDTO> findByRole(String role);

  /**
   * Get all eventFeaturedPerformers by nationality.
   *
   * @param nationality the nationality to search for.
   * @return the list of entities.
   */
  List<EventFeaturedPerformersDTO> findByNationality(String nationality);

  /**
   * Get all headliner performers for a specific event.
   *
   * @param eventId     the id of the event.
   * @param isHeadliner headliner status.
   * @return the list of entities.
   */
  List<EventFeaturedPerformersDTO> findByEventIdAndIsHeadliner(Long eventId, Boolean isHeadliner);

  /**
   * Get all eventFeaturedPerformers ordered by priority ranking.
   *
   * @param eventId the id of the event.
   * @return the list of entities ordered by priority.
   */
  List<EventFeaturedPerformersDTO> findByEventIdOrderByPriorityRanking(Long eventId);

  /**
   * Count eventFeaturedPerformers for a specific event.
   *
   * @param eventId the id of the event.
   * @return the count of entities.
   */
  long countByEventId(Long eventId);

  /**
   * Count active eventFeaturedPerformers for a specific event.
   *
   * @param eventId  the id of the event.
   * @param isActive active status.
   * @return the count of entities.
   */
  long countByEventIdAndIsActive(Long eventId, Boolean isActive);
}
