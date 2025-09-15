package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.EventProgramDirectorsDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing
 * {@link com.nextjstemplate.domain.EventProgramDirectors}.
 */
public interface EventProgramDirectorsService {
  /**
   * Save a eventProgramDirectors.
   *
   * @param eventProgramDirectorsDTO the entity to save.
   * @return the persisted entity.
   */
  EventProgramDirectorsDTO save(EventProgramDirectorsDTO eventProgramDirectorsDTO);

  /**
   * Updates a eventProgramDirectors.
   *
   * @param eventProgramDirectorsDTO the entity to update.
   * @return the persisted entity.
   */
  EventProgramDirectorsDTO update(EventProgramDirectorsDTO eventProgramDirectorsDTO);

  /**
   * Partially updates a eventProgramDirectors.
   *
   * @param eventProgramDirectorsDTO the entity to update partially.
   * @return the persisted entity.
   */
  Optional<EventProgramDirectorsDTO> partialUpdate(EventProgramDirectorsDTO eventProgramDirectorsDTO);

  /**
   * Get all the eventProgramDirectors.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  Page<EventProgramDirectorsDTO> findAll(Pageable pageable);

  /**
   * Get the "id" eventProgramDirectors.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Optional<EventProgramDirectorsDTO> findOne(Long id);

  /**
   * Delete the "id" eventProgramDirectors.
   *
   * @param id the id of the entity.
   */
  void delete(Long id);

  /**
   * Get all eventProgramDirectors for a specific event.
   *
   * @param eventId the id of the event.
   * @return the list of entities.
   */
  List<EventProgramDirectorsDTO> findByEventId(Long eventId);

  /**
   * Get all eventProgramDirectors for a specific event with pagination.
   *
   * @param eventId  the id of the event.
   * @param pageable the pagination information.
   * @return the page of entities.
   */
  Page<EventProgramDirectorsDTO> findByEventId(Long eventId, Pageable pageable);

  /**
   * Get all eventProgramDirectors by name containing.
   *
   * @param name the name to search for.
   * @return the list of entities.
   */
  List<EventProgramDirectorsDTO> findByNameContaining(String name);

  /**
   * Get all eventProgramDirectors by event ID and name.
   *
   * @param eventId the id of the event.
   * @param name    the name.
   * @return the list of entities.
   */
  List<EventProgramDirectorsDTO> findByEventIdAndName(Long eventId, String name);

  /**
   * Get all eventProgramDirectors by event ID and name containing.
   *
   * @param eventId the id of the event.
   * @param name    the name to search for.
   * @return the list of entities.
   */
  List<EventProgramDirectorsDTO> findByEventIdAndNameContaining(Long eventId, String name);

  /**
   * Get all eventProgramDirectors by bio containing.
   *
   * @param bio the bio to search for.
   * @return the list of entities.
   */
  List<EventProgramDirectorsDTO> findByBioContaining(String bio);

  /**
   * Get all eventProgramDirectors by event ID and bio containing.
   *
   * @param eventId the id of the event.
   * @param bio     the bio to search for.
   * @return the list of entities.
   */
  List<EventProgramDirectorsDTO> findByEventIdAndBioContaining(Long eventId, String bio);

  /**
   * Count eventProgramDirectors for a specific event.
   *
   * @param eventId the id of the event.
   * @return the count of entities.
   */
  long countByEventId(Long eventId);

  /**
   * Get all eventProgramDirectors ordered by creation date.
   *
   * @param eventId the id of the event.
   * @return the list of entities ordered by creation date.
   */
  List<EventProgramDirectorsDTO> findByEventIdOrderByCreatedAt(Long eventId);

  /**
   * Get all eventProgramDirectors ordered by name.
   *
   * @param eventId the id of the event.
   * @return the list of entities ordered by name.
   */
  List<EventProgramDirectorsDTO> findByEventIdOrderByName(Long eventId);

  /**
   * Get all eventProgramDirectors with photo URLs.
   *
   * @param eventId the id of the event.
   * @return the list of entities with photos.
   */
  List<EventProgramDirectorsDTO> findByEventIdWithPhoto(Long eventId);

  /**
   * Get all eventProgramDirectors without photo URLs.
   *
   * @param eventId the id of the event.
   * @return the list of entities without photos.
   */
  List<EventProgramDirectorsDTO> findByEventIdWithoutPhoto(Long eventId);

  /**
   * Get all eventProgramDirectors with bio.
   *
   * @param eventId the id of the event.
   * @return the list of entities with bio.
   */
  List<EventProgramDirectorsDTO> findByEventIdWithBio(Long eventId);

  /**
   * Get all eventProgramDirectors without bio.
   *
   * @param eventId the id of the event.
   * @return the list of entities without bio.
   */
  List<EventProgramDirectorsDTO> findByEventIdWithoutBio(Long eventId);
}
