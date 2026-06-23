package com.eventsitemanager.repository;

import com.eventsitemanager.domain.CommunicationCampaign;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CommunicationCampaign entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommunicationCampaignRepository
    extends JpaRepository<CommunicationCampaign, Long>, JpaSpecificationExecutor<CommunicationCampaign> {}
