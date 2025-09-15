package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventContacts;
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
public interface EventContactsRepository
    extends JpaRepository<EventContacts, Long>, JpaSpecificationExecutor<EventContacts> {

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
}
