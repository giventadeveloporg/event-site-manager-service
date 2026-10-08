package com.eventsitemanager.repository;

import com.eventsitemanager.domain.ProfileService;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileServiceRepository extends JpaRepository<ProfileService, Long>, JpaSpecificationExecutor<ProfileService> {}
