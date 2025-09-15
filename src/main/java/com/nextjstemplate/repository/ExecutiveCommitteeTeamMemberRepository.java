package com.nextjstemplate.repository;

import com.nextjstemplate.domain.ExecutiveCommitteeTeamMember;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ExecutiveCommitteeTeamMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExecutiveCommitteeTeamMemberRepository
    extends JpaRepository<ExecutiveCommitteeTeamMember, Long>, JpaSpecificationExecutor<ExecutiveCommitteeTeamMember> {}
