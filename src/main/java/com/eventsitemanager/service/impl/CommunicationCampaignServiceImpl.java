package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.CommunicationCampaign;
import com.eventsitemanager.repository.CommunicationCampaignRepository;
import com.eventsitemanager.service.CommunicationCampaignService;
import com.eventsitemanager.service.dto.CommunicationCampaignDTO;
import com.eventsitemanager.service.mapper.CommunicationCampaignMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eventsitemanager.domain.CommunicationCampaign}.
 */
@Service
@Transactional
public class CommunicationCampaignServiceImpl implements CommunicationCampaignService {

    private final Logger log = LoggerFactory.getLogger(CommunicationCampaignServiceImpl.class);

    private final CommunicationCampaignRepository communicationCampaignRepository;

    private final CommunicationCampaignMapper communicationCampaignMapper;

    public CommunicationCampaignServiceImpl(
        CommunicationCampaignRepository communicationCampaignRepository,
        CommunicationCampaignMapper communicationCampaignMapper
    ) {
        this.communicationCampaignRepository = communicationCampaignRepository;
        this.communicationCampaignMapper = communicationCampaignMapper;
    }

    @Override
    public CommunicationCampaignDTO save(CommunicationCampaignDTO communicationCampaignDTO) {
        log.debug("Request to save CommunicationCampaign : {}", communicationCampaignDTO);
        CommunicationCampaign communicationCampaign = communicationCampaignMapper.toEntity(communicationCampaignDTO);
        communicationCampaign = communicationCampaignRepository.save(communicationCampaign);
        return communicationCampaignMapper.toDto(communicationCampaign);
    }

    @Override
    public CommunicationCampaignDTO update(CommunicationCampaignDTO communicationCampaignDTO) {
        log.debug("Request to update CommunicationCampaign : {}", communicationCampaignDTO);
        CommunicationCampaign communicationCampaign = communicationCampaignMapper.toEntity(communicationCampaignDTO);
        communicationCampaign = communicationCampaignRepository.save(communicationCampaign);
        return communicationCampaignMapper.toDto(communicationCampaign);
    }

    @Override
    public Optional<CommunicationCampaignDTO> partialUpdate(CommunicationCampaignDTO communicationCampaignDTO) {
        log.debug("Request to partially update CommunicationCampaign : {}", communicationCampaignDTO);

        return communicationCampaignRepository
            .findById(communicationCampaignDTO.getId())
            .map(existingCommunicationCampaign -> {
                communicationCampaignMapper.partialUpdate(existingCommunicationCampaign, communicationCampaignDTO);

                return existingCommunicationCampaign;
            })
            .map(communicationCampaignRepository::save)
            .map(communicationCampaignMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommunicationCampaignDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CommunicationCampaigns");
        return communicationCampaignRepository.findAll(pageable).map(communicationCampaignMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommunicationCampaignDTO> findOne(Long id) {
        log.debug("Request to get CommunicationCampaign : {}", id);
        return communicationCampaignRepository.findById(id).map(communicationCampaignMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CommunicationCampaign : {}", id);
        communicationCampaignRepository.deleteById(id);
    }
}
