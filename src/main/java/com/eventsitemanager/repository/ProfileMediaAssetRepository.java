package com.eventsitemanager.repository;

import com.eventsitemanager.domain.ProfileMediaAsset;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileMediaAssetRepository extends JpaRepository<ProfileMediaAsset, Long>, JpaSpecificationExecutor<ProfileMediaAsset> {}
