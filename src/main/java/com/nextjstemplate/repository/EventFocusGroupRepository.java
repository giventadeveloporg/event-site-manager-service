package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventFocusGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventFocusGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventFocusGroupRepository extends JpaRepository<EventFocusGroup, Long>, JpaSpecificationExecutor<EventFocusGroup> {}
