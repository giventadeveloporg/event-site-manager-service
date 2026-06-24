package com.eventsitemanager.repository;

import com.eventsitemanager.domain.EventCompetitionGroupMember;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventCompetitionGroupMember entity.
 */
@Repository
public interface EventCompetitionGroupMemberRepository
    extends JpaRepository<EventCompetitionGroupMember, Long>, JpaSpecificationExecutor<EventCompetitionGroupMember> {}
