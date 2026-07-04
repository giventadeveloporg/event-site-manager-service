package com.eventsitemanager.repository;

import com.eventsitemanager.domain.ProfileAffiliation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileAffiliationRepository
    extends JpaRepository<ProfileAffiliation, Long>, JpaSpecificationExecutor<ProfileAffiliation> {}
