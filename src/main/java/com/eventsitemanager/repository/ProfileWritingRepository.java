package com.eventsitemanager.repository;

import com.eventsitemanager.domain.ProfileWriting;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileWritingRepository extends JpaRepository<ProfileWriting, Long>, JpaSpecificationExecutor<ProfileWriting> {}
