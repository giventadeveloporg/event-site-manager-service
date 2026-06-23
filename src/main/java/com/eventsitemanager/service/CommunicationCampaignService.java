package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.CommunicationCampaignDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.CommunicationCampaign}.
 */
public interface CommunicationCampaignService {
    /**
     * Save a communicationCampaign.
     *
     * @param communicationCampaignDTO the entity to save.
     * @return the persisted entity.
     */
    CommunicationCampaignDTO save(CommunicationCampaignDTO communicationCampaignDTO);

    /**
     * Updates a communicationCampaign.
     *
     * @param communicationCampaignDTO the entity to update.
     * @return the persisted entity.
     */
    CommunicationCampaignDTO update(CommunicationCampaignDTO communicationCampaignDTO);

    /**
     * Partially updates a communicationCampaign.
     *
     * @param communicationCampaignDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CommunicationCampaignDTO> partialUpdate(CommunicationCampaignDTO communicationCampaignDTO);

    /**
     * Get all the communicationCampaigns.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CommunicationCampaignDTO> findAll(Pageable pageable);

    /**
     * Get the "id" communicationCampaign.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CommunicationCampaignDTO> findOne(Long id);

    /**
     * Delete the "id" communicationCampaign.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
