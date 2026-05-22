package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventCompetitionContentBlock;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventCompetitionContentBlock entity.
 */
@Repository
public interface EventCompetitionContentBlockRepository extends JpaRepository<EventCompetitionContentBlock, Long>, JpaSpecificationExecutor<EventCompetitionContentBlock> {
}
