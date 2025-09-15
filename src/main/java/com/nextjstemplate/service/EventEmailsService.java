package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.EventEmailsDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.EventEmails}.
 */
public interface EventEmailsService {
  /**
   * Save a eventEmails.
   *
   * @param eventEmailsDTO the entity to save.
   * @return the persisted entity.
   */
  EventEmailsDTO save(EventEmailsDTO eventEmailsDTO);

  /**
   * Updates a eventEmails.
   *
   * @param eventEmailsDTO the entity to update.
   * @return the persisted entity.
   */
  EventEmailsDTO update(EventEmailsDTO eventEmailsDTO);

  /**
   * Partially updates a eventEmails.
   *
   * @param eventEmailsDTO the entity to update partially.
   * @return the persisted entity.
   */
  Optional<EventEmailsDTO> partialUpdate(EventEmailsDTO eventEmailsDTO);

  /**
   * Get all the eventEmails.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  Page<EventEmailsDTO> findAll(Pageable pageable);

  /**
   * Get the "id" eventEmails.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Optional<EventEmailsDTO> findOne(Long id);

  /**
   * Delete the "id" eventEmails.
   *
   * @param id the id of the entity.
   */
  void delete(Long id);

  /**
   * Get all eventEmails for a specific event.
   *
   * @param eventId the id of the event.
   * @return the list of entities.
   */
  List<EventEmailsDTO> findByEventId(Long eventId);

  /**
   * Get all eventEmails for a specific event with pagination.
   *
   * @param eventId  the id of the event.
   * @param pageable the pagination information.
   * @return the page of entities.
   */
  Page<EventEmailsDTO> findByEventId(Long eventId, Pageable pageable);

  /**
   * Get all eventEmails by email address.
   *
   * @param email the email address.
   * @return the list of entities.
   */
  List<EventEmailsDTO> findByEmail(String email);

  /**
   * Get all eventEmails by email address containing.
   *
   * @param email the email address to search for.
   * @return the list of entities.
   */
  List<EventEmailsDTO> findByEmailContaining(String email);

  /**
   * Get all eventEmails by event ID and email address.
   *
   * @param eventId the id of the event.
   * @param email   the email address.
   * @return the list of entities.
   */
  List<EventEmailsDTO> findByEventIdAndEmail(Long eventId, String email);

  /**
   * Get all eventEmails by event ID and email address containing.
   *
   * @param eventId the id of the event.
   * @param email   the email address to search for.
   * @return the list of entities.
   */
  List<EventEmailsDTO> findByEventIdAndEmailContaining(Long eventId, String email);

  /**
   * Count eventEmails for a specific event.
   *
   * @param eventId the id of the event.
   * @return the count of entities.
   */
  long countByEventId(Long eventId);

  /**
   * Count eventEmails by email address.
   *
   * @param email the email address.
   * @return the count of entities.
   */
  long countByEmail(String email);

  /**
   * Get all eventEmails ordered by creation date.
   *
   * @param eventId the id of the event.
   * @return the list of entities ordered by creation date.
   */
  List<EventEmailsDTO> findByEventIdOrderByCreatedAt(Long eventId);

  /**
   * Get unique email addresses for a specific event.
   *
   * @param eventId the id of the event.
   * @return the list of unique email addresses.
   */
  List<String> findDistinctEmailsByEventId(Long eventId);

  /**
   * Get eventEmails by event ID and email address (exact match).
   *
   * @param eventId the id of the event.
   * @param email   the email address.
   * @return the entity.
   */
  Optional<EventEmailsDTO> findByEventIdAndEmailExact(Long eventId, String email);

  /**
   * Check if email exists for a specific event.
   *
   * @param eventId the id of the event.
   * @param email   the email address.
   * @return true if email exists.
   */
  boolean existsByEventIdAndEmail(Long eventId, String email);
}
