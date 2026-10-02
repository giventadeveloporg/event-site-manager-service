package com.eventsitemanager.repository;

import com.eventsitemanager.domain.HomepageYoutubeOverride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HomepageYoutubeOverride entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HomepageYoutubeOverrideRepository
    extends JpaRepository<HomepageYoutubeOverride, Long>, JpaSpecificationExecutor<HomepageYoutubeOverride> {}
