package com.eventsitemanager.repository;

import com.eventsitemanager.domain.ProfileFamilyMember;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileFamilyMemberRepository
    extends JpaRepository<ProfileFamilyMember, Long>, JpaSpecificationExecutor<ProfileFamilyMember> {}
