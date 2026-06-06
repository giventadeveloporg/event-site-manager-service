package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventCompetitionGroupMember;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventCompetitionGroupMember entity.
 */
@Repository
public interface EventCompetitionGroupMemberRepository
    extends JpaRepository<EventCompetitionGroupMember, Long>, JpaSpecificationExecutor<EventCompetitionGroupMember> {}
