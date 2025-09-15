package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.EventContactsDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing
 * {@link com.nextjstemplate.domain.EventContacts}.
 */
public interface EventContactsService {
  /**
   * Save a eventContacts.
   *
   * @param eventContactsDTO the entity to save.
   * @return the persisted entity.
   */
  EventContactsDTO save(EventContactsDTO eventContactsDTO);

  /**
   * Updates a eventContacts.
   *
   * @param eventContactsDTO the entity to update.
   * @return the persisted entity.
   */
  EventContactsDTO update(EventContactsDTO eventContactsDTO);

  /**
   * Partially updates a eventContacts.
   *
   * @param eventContactsDTO the entity to update partially.
   * @return the persisted entity.
   */
  Optional<EventContactsDTO> partialUpdate(EventContactsDTO eventContactsDTO);

  /**
   * Get all the eventContacts.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  Page<EventContactsDTO> findAll(Pageable pageable);

  /**
   * Get the "id" eventContacts.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Optional<EventContactsDTO> findOne(Long id);

  /**
   * Delete the "id" eventContacts.
   *
   * @param id the id of the entity.
   */
  void delete(Long id);

  /**
   * Get all eventContacts for a specific event.
   *
   * @param eventId the id of the event.
   * @return the list of entities.
   */
  List<EventContactsDTO> findByEventId(Long eventId);

  /**
   * Get all eventContacts for a specific event with pagination.
   *
   * @param eventId  the id of the event.
   * @param pageable the pagination information.
   * @return the page of entities.
   */
  Page<EventContactsDTO> findByEventId(Long eventId, Pageable pageable);

  /**
   * Get all eventContacts by name containing.
   *
   * @param name the name to search for.
   * @return the list of entities.
   */
  List<EventContactsDTO> findByNameContaining(String name);

  /**
   * Get all eventContacts by phone number.
   *
   * @param phone the phone number.
   * @return the list of entities.
   */
  List<EventContactsDTO> findByPhone(String phone);

  /**
   * Get all eventContacts by email.
   *
   * @param email the email address.
   * @return the list of entities.
   */
  List<EventContactsDTO> findByEmail(String email);

  /**
   * Get all eventContacts by event ID and name.
   *
   * @param eventId the id of the event.
   * @param name    the name.
   * @return the list of entities.
   */
  List<EventContactsDTO> findByEventIdAndName(Long eventId, String name);

  /**
   * Get all eventContacts by event ID and phone.
   *
   * @param eventId the id of the event.
   * @param phone   the phone number.
   * @return the list of entities.
   */
  List<EventContactsDTO> findByEventIdAndPhone(Long eventId, String phone);

  /**
   * Get all eventContacts by event ID and email.
   *
   * @param eventId the id of the event.
   * @param email   the email address.
   * @return the list of entities.
   */
  List<EventContactsDTO> findByEventIdAndEmail(Long eventId, String email);

  /**
   * Count eventContacts for a specific event.
   *
   * @param eventId the id of the event.
   * @return the count of entities.
   */
  long countByEventId(Long eventId);
}
