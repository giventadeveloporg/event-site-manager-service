package com.eventsitemanager.repository;

import com.eventsitemanager.domain.ProfileProject;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileProjectRepository extends JpaRepository<ProfileProject, Long>, JpaSpecificationExecutor<ProfileProject> {}
