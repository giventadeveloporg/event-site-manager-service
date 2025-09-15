package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.EventSponsorsJoinDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing
 * {@link com.nextjstemplate.domain.EventSponsorsJoin}.
 */
public interface EventSponsorsJoinService {
  /**
   * Save a eventSponsorsJoin.
   *
   * @param eventSponsorsJoinDTO the entity to save.
   * @return the persisted entity.
   */
  EventSponsorsJoinDTO save(EventSponsorsJoinDTO eventSponsorsJoinDTO);

  /**
   * Updates a eventSponsorsJoin.
   *
   * @param eventSponsorsJoinDTO the entity to update.
   * @return the persisted entity.
   */
  EventSponsorsJoinDTO update(EventSponsorsJoinDTO eventSponsorsJoinDTO);

  /**
   * Partially updates a eventSponsorsJoin.
   *
   * @param eventSponsorsJoinDTO the entity to update partially.
   * @return the persisted entity.
   */
  Optional<EventSponsorsJoinDTO> partialUpdate(EventSponsorsJoinDTO eventSponsorsJoinDTO);

  /**
   * Get all the eventSponsorsJoin.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  Page<EventSponsorsJoinDTO> findAll(Pageable pageable);

  /**
   * Get the "id" eventSponsorsJoin.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Optional<EventSponsorsJoinDTO> findOne(Long id);

  /**
   * Delete the "id" eventSponsorsJoin.
   *
   * @param id the id of the entity.
   */
  void delete(Long id);

  /**
   * Get all eventSponsorsJoin for a specific event.
   *
   * @param eventId the id of the event.
   * @return the list of entities.
   */
  List<EventSponsorsJoinDTO> findByEventId(Long eventId);

  /**
   * Get all eventSponsorsJoin for a specific event with pagination.
   *
   * @param eventId  the id of the event.
   * @param pageable the pagination information.
   * @return the page of entities.
   */
  Page<EventSponsorsJoinDTO> findByEventId(Long eventId, Pageable pageable);

  /**
   * Get all eventSponsorsJoin for a specific sponsor.
   *
   * @param sponsorId the id of the sponsor.
   * @return the list of entities.
   */
  List<EventSponsorsJoinDTO> findBySponsorId(Long sponsorId);

  /**
   * Get all eventSponsorsJoin for a specific sponsor with pagination.
   *
   * @param sponsorId the id of the sponsor.
   * @param pageable  the pagination information.
   * @return the page of entities.
   */
  Page<EventSponsorsJoinDTO> findBySponsorId(Long sponsorId, Pageable pageable);

  /**
   * Get specific event-sponsor association.
   *
   * @param eventId   the id of the event.
   * @param sponsorId the id of the sponsor.
   * @return the entity.
   */
  Optional<EventSponsorsJoinDTO> findByEventIdAndSponsorId(Long eventId, Long sponsorId);

  /**
   * Check if event-sponsor association exists.
   *
   * @param eventId   the id of the event.
   * @param sponsorId the id of the sponsor.
   * @return true if association exists.
   */
  boolean existsByEventIdAndSponsorId(Long eventId, Long sponsorId);

  /**
   * Count eventSponsorsJoin for a specific event.
   *
   * @param eventId the id of the event.
   * @return the count of entities.
   */
  long countByEventId(Long eventId);

  /**
   * Count eventSponsorsJoin for a specific sponsor.
   *
   * @param sponsorId the id of the sponsor.
   * @return the count of entities.
   */
  long countBySponsorId(Long sponsorId);

  /**
   * Delete all associations for a specific event.
   *
   * @param eventId the id of the event.
   */
  void deleteByEventId(Long eventId);

  /**
   * Delete all associations for a specific sponsor.
   *
   * @param sponsorId the id of the sponsor.
   */
  void deleteBySponsorId(Long sponsorId);

  /**
   * Delete specific event-sponsor association.
   *
   * @param eventId   the id of the event.
   * @param sponsorId the id of the sponsor.
   */
  void deleteByEventIdAndSponsorId(Long eventId, Long sponsorId);

  /**
   * Get all eventSponsorsJoin ordered by creation date.
   *
   * @param eventId the id of the event.
   * @return the list of entities ordered by creation date.
   */
  List<EventSponsorsJoinDTO> findByEventIdOrderByCreatedAt(Long eventId);

  /**
   * Get all eventSponsorsJoin ordered by creation date.
   *
   * @param sponsorId the id of the sponsor.
   * @return the list of entities ordered by creation date.
   */
  List<EventSponsorsJoinDTO> findBySponsorIdOrderByCreatedAt(Long sponsorId);
}
