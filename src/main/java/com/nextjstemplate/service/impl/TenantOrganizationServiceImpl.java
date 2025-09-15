package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.TenantOrganization;
import com.nextjstemplate.repository.TenantOrganizationRepository;
import com.nextjstemplate.service.TenantOrganizationService;
import com.nextjstemplate.service.dto.TenantOrganizationDTO;
import com.nextjstemplate.service.mapper.TenantOrganizationMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.TenantOrganization}.
 */
@Service
@Transactional
public class TenantOrganizationServiceImpl implements TenantOrganizationService {

    private final Logger log = LoggerFactory.getLogger(TenantOrganizationServiceImpl.class);

    private final TenantOrganizationRepository tenantOrganizationRepository;

    private final TenantOrganizationMapper tenantOrganizationMapper;

    public TenantOrganizationServiceImpl(
        TenantOrganizationRepository tenantOrganizationRepository,
        TenantOrganizationMapper tenantOrganizationMapper
    ) {
        this.tenantOrganizationRepository = tenantOrganizationRepository;
        this.tenantOrganizationMapper = tenantOrganizationMapper;
    }

    @Override
    public TenantOrganizationDTO save(TenantOrganizationDTO tenantOrganizationDTO) {
        log.debug("Request to save TenantOrganization : {}", tenantOrganizationDTO);
        TenantOrganization tenantOrganization = tenantOrganizationMapper.toEntity(tenantOrganizationDTO);
        tenantOrganization = tenantOrganizationRepository.save(tenantOrganization);
        return tenantOrganizationMapper.toDto(tenantOrganization);
    }

    @Override
    public TenantOrganizationDTO update(TenantOrganizationDTO tenantOrganizationDTO) {
        log.debug("Request to update TenantOrganization : {}", tenantOrganizationDTO);
        TenantOrganization tenantOrganization = tenantOrganizationMapper.toEntity(tenantOrganizationDTO);
        tenantOrganization = tenantOrganizationRepository.save(tenantOrganization);
        return tenantOrganizationMapper.toDto(tenantOrganization);
    }

    @Override
    public Optional<TenantOrganizationDTO> partialUpdate(TenantOrganizationDTO tenantOrganizationDTO) {
        log.debug("Request to partially update TenantOrganization : {}", tenantOrganizationDTO);

        return tenantOrganizationRepository
            .findById(tenantOrganizationDTO.getId())
            .map(existingTenantOrganization -> {
                tenantOrganizationMapper.partialUpdate(existingTenantOrganization, tenantOrganizationDTO);

                return existingTenantOrganization;
            })
            .map(tenantOrganizationRepository::save)
            .map(tenantOrganizationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TenantOrganizationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TenantOrganizations");
        return tenantOrganizationRepository.findAll(pageable).map(tenantOrganizationMapper::toDto);
    }

    /**
     *  Get all the tenantOrganizations where TenantSettings is {@code null}.
     *  @return the list of entities.
     */
    /* @Transactional(readOnly = true)
    public List<TenantOrganizationDTO> findAllWhereTenantSettingsIsNull() {
        log.debug("Request to get all tenantOrganizations where TenantSettings is null");
        return StreamSupport
            .stream(tenantOrganizationRepository.findAll().spliterator(), false)
            .filter(tenantOrganization -> tenantOrganization.getTenantSettings() == null)
            .map(tenantOrganizationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }*/

    @Override
    @Transactional(readOnly = true)
    public Optional<TenantOrganizationDTO> findOne(Long id) {
        log.debug("Request to get TenantOrganization : {}", id);
        return tenantOrganizationRepository.findById(id).map(tenantOrganizationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TenantOrganization : {}", id);
        tenantOrganizationRepository.deleteById(id);
    }
}
