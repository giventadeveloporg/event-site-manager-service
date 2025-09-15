package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventSponsors;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventSponsors entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventSponsorsRepository
    extends JpaRepository<EventSponsors, Long>, JpaSpecificationExecutor<EventSponsors> {

  /**
   * Find sponsors by name containing (case insensitive)
   * 
   * @param name the name to search for
   * @return list of sponsors
   */
  List<EventSponsors> findByNameContainingIgnoreCase(String name);

  /**
   * Find sponsors by company name containing (case insensitive)
   * 
   * @param companyName the company name to search for
   * @return list of sponsors
   */
  List<EventSponsors> findByCompanyNameContainingIgnoreCase(String companyName);

  /**
   * Find sponsors by type
   * 
   * @param type the sponsor type
   * @return list of sponsors
   */
  List<EventSponsors> findByType(String type);

  /**
   * Find active sponsors
   * 
   * @param isActive active status
   * @return list of active sponsors
   */
  List<EventSponsors> findByIsActive(Boolean isActive);

  /**
   * Find sponsors by type and active status
   * 
   * @param type     the sponsor type
   * @param isActive active status
   * @return list of sponsors
   */
  List<EventSponsors> findByTypeAndIsActive(String type, Boolean isActive);

  /**
   * Find sponsors by name and type
   * 
   * @param name the sponsor name
   * @param type the sponsor type
   * @return list of sponsors
   */
  List<EventSponsors> findByNameAndType(String name, String type);

  /**
   * Find sponsors by contact email
   * 
   * @param contactEmail the contact email
   * @return list of sponsors
   */
  List<EventSponsors> findByContactEmail(String contactEmail);

  /**
   * Find sponsors by contact phone
   * 
   * @param contactPhone the contact phone
   * @return list of sponsors
   */
  List<EventSponsors> findByContactPhone(String contactPhone);

  /**
   * Find sponsors ordered by priority ranking
   * 
   * @return list of sponsors ordered by priority
   */
  @Query("SELECT es FROM EventSponsors es ORDER BY es.priorityRanking ASC")
  List<EventSponsors> findAllOrderByPriorityRanking();

  /**
   * Find active sponsors ordered by priority ranking
   * 
   * @param isActive active status
   * @return list of active sponsors ordered by priority
   */
  @Query("SELECT es FROM EventSponsors es WHERE es.isActive = :isActive ORDER BY es.priorityRanking ASC")
  List<EventSponsors> findByIsActiveOrderByPriorityRanking(@Param("isActive") Boolean isActive);

  /**
   * Find sponsors by type ordered by priority ranking
   * 
   * @param type the sponsor type
   * @return list of sponsors ordered by priority
   */
  @Query("SELECT es FROM EventSponsors es WHERE es.type = :type ORDER BY es.priorityRanking ASC")
  List<EventSponsors> findByTypeOrderByPriorityRanking(@Param("type") String type);

  /**
   * Count sponsors by type
   * 
   * @param type the sponsor type
   * @return count of sponsors
   */
  long countByType(String type);

  /**
   * Count active sponsors
   * 
   * @param isActive active status
   * @return count of active sponsors
   */
  long countByIsActive(Boolean isActive);

  /**
   * Count sponsors by type and active status
   * 
   * @param type     the sponsor type
   * @param isActive active status
   * @return count of sponsors
   */
  long countByTypeAndIsActive(String type, Boolean isActive);
}
