package com.nextjstemplate.repository;

import com.nextjstemplate.domain.FocusGroupMember;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FocusGroupMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FocusGroupMemberRepository extends JpaRepository<FocusGroupMember, Long>, JpaSpecificationExecutor<FocusGroupMember> {}
