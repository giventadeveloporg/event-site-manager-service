package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.EventSponsorsDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing
 * {@link com.nextjstemplate.domain.EventSponsors}.
 */
public interface EventSponsorsService {
  /**
   * Save a eventSponsors.
   *
   * @param eventSponsorsDTO the entity to save.
   * @return the persisted entity.
   */
  EventSponsorsDTO save(EventSponsorsDTO eventSponsorsDTO);

  /**
   * Updates a eventSponsors.
   *
   * @param eventSponsorsDTO the entity to update.
   * @return the persisted entity.
   */
  EventSponsorsDTO update(EventSponsorsDTO eventSponsorsDTO);

  /**
   * Partially updates a eventSponsors.
   *
   * @param eventSponsorsDTO the entity to update partially.
   * @return the persisted entity.
   */
  Optional<EventSponsorsDTO> partialUpdate(EventSponsorsDTO eventSponsorsDTO);

  /**
   * Get all the eventSponsors.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  Page<EventSponsorsDTO> findAll(Pageable pageable);

  /**
   * Get the "id" eventSponsors.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Optional<EventSponsorsDTO> findOne(Long id);

  /**
   * Delete the "id" eventSponsors.
   *
   * @param id the id of the entity.
   */
  void delete(Long id);

  /**
   * Get all eventSponsors by name containing.
   *
   * @param name the name to search for.
   * @return the list of entities.
   */
  List<EventSponsorsDTO> findByNameContaining(String name);

  /**
   * Get all eventSponsors by company name containing.
   *
   * @param companyName the company name to search for.
   * @return the list of entities.
   */
  List<EventSponsorsDTO> findByCompanyNameContaining(String companyName);

  /**
   * Get all eventSponsors by type.
   *
   * @param type the sponsor type.
   * @return the list of entities.
   */
  List<EventSponsorsDTO> findByType(String type);

  /**
   * Get all active eventSponsors.
   *
   * @param isActive active status.
   * @return the list of entities.
   */
  List<EventSponsorsDTO> findByIsActive(Boolean isActive);

  /**
   * Get all eventSponsors by type and active status.
   *
   * @param type     the sponsor type.
   * @param isActive active status.
   * @return the list of entities.
   */
  List<EventSponsorsDTO> findByTypeAndIsActive(String type, Boolean isActive);

  /**
   * Get all eventSponsors by name and type.
   *
   * @param name the sponsor name.
   * @param type the sponsor type.
   * @return the list of entities.
   */
  List<EventSponsorsDTO> findByNameAndType(String name, String type);

  /**
   * Get all eventSponsors by contact email.
   *
   * @param contactEmail the contact email.
   * @return the list of entities.
   */
  List<EventSponsorsDTO> findByContactEmail(String contactEmail);

  /**
   * Get all eventSponsors by contact phone.
   *
   * @param contactPhone the contact phone.
   * @return the list of entities.
   */
  List<EventSponsorsDTO> findByContactPhone(String contactPhone);

  /**
   * Get all eventSponsors ordered by priority ranking.
   *
   * @return the list of entities ordered by priority.
   */
  List<EventSponsorsDTO> findAllOrderByPriorityRanking();

  /**
   * Get all active eventSponsors ordered by priority ranking.
   *
   * @param isActive active status.
   * @return the list of entities ordered by priority.
   */
  List<EventSponsorsDTO> findByIsActiveOrderByPriorityRanking(Boolean isActive);

  /**
   * Get all eventSponsors by type ordered by priority ranking.
   *
   * @param type the sponsor type.
   * @return the list of entities ordered by priority.
   */
  List<EventSponsorsDTO> findByTypeOrderByPriorityRanking(String type);

  /**
   * Count eventSponsors by type.
   *
   * @param type the sponsor type.
   * @return the count of entities.
   */
  long countByType(String type);

  /**
   * Count active eventSponsors.
   *
   * @param isActive active status.
   * @return the count of entities.
   */
  long countByIsActive(Boolean isActive);

  /**
   * Count eventSponsors by type and active status.
   *
   * @param type     the sponsor type.
   * @param isActive active status.
   * @return the count of entities.
   */
  long countByTypeAndIsActive(String type, Boolean isActive);
}
