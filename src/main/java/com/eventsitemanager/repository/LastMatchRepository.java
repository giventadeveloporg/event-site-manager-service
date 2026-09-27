package com.eventsitemanager.repository;

import com.eventsitemanager.domain.LastMatch;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LastMatch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LastMatchRepository extends JpaRepository<LastMatch, Long>, JpaSpecificationExecutor<LastMatch> {}
