package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.TenantSettings;
import com.eventsitemanager.repository.TenantSettingsRepository;
import com.eventsitemanager.service.TenantSettingsService;
import com.eventsitemanager.service.dto.TenantSettingsDTO;
import com.eventsitemanager.service.mapper.TenantSettingsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eventsitemanager.domain.TenantSettings}.
 */
@Service
@Transactional
public class TenantSettingsServiceImpl implements TenantSettingsService {

    private static final Logger LOG = LoggerFactory.getLogger(TenantSettingsServiceImpl.class);

    private final TenantSettingsRepository tenantSettingsRepository;

    private final TenantSettingsMapper tenantSettingsMapper;

    public TenantSettingsServiceImpl(TenantSettingsRepository tenantSettingsRepository, TenantSettingsMapper tenantSettingsMapper) {
        this.tenantSettingsRepository = tenantSettingsRepository;
        this.tenantSettingsMapper = tenantSettingsMapper;
    }

    @Override
    public TenantSettingsDTO save(TenantSettingsDTO tenantSettingsDTO) {
        LOG.debug("Request to save TenantSettings : {}", tenantSettingsDTO);
        TenantSettings tenantSettings = tenantSettingsMapper.toEntity(tenantSettingsDTO);
        tenantSettings = tenantSettingsRepository.save(tenantSettings);
        return tenantSettingsMapper.toDto(tenantSettings);
    }

    @Override
    public TenantSettingsDTO update(TenantSettingsDTO tenantSettingsDTO) {
        LOG.debug("Request to update TenantSettings : {}", tenantSettingsDTO);
        TenantSettings tenantSettings = tenantSettingsMapper.toEntity(tenantSettingsDTO);
        tenantSettings = tenantSettingsRepository.save(tenantSettings);
        return tenantSettingsMapper.toDto(tenantSettings);
    }

    @Override
    public Optional<TenantSettingsDTO> partialUpdate(TenantSettingsDTO tenantSettingsDTO) {
        LOG.debug("Request to partially update TenantSettings : {}", tenantSettingsDTO);

        return tenantSettingsRepository
            .findById(tenantSettingsDTO.getId())
            .map(existingTenantSettings -> {
                tenantSettingsMapper.partialUpdate(existingTenantSettings, tenantSettingsDTO);

                return existingTenantSettings;
            })
            .map(tenantSettingsRepository::save)
            .map(tenantSettingsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TenantSettingsDTO> findOne(Long id) {
        LOG.debug("Request to get TenantSettings : {}", id);
        return tenantSettingsRepository.findById(id).map(tenantSettingsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete TenantSettings : {}", id);
        tenantSettingsRepository.deleteById(id);
    }
}
