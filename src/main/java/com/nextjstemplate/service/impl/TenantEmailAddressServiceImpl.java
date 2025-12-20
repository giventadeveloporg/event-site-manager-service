package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.TenantEmailAddress;
import com.nextjstemplate.repository.TenantEmailAddressRepository;
import com.nextjstemplate.service.TenantEmailAddressService;
import com.nextjstemplate.service.dto.TenantEmailAddressDTO;
import com.nextjstemplate.service.mapper.TenantEmailAddressMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.TenantEmailAddress}.
 */
@Service
@Transactional
public class TenantEmailAddressServiceImpl implements TenantEmailAddressService {

    private final Logger log = LoggerFactory.getLogger(TenantEmailAddressServiceImpl.class);

    private final TenantEmailAddressRepository tenantEmailAddressRepository;

    private final TenantEmailAddressMapper tenantEmailAddressMapper;

    public TenantEmailAddressServiceImpl(
        TenantEmailAddressRepository tenantEmailAddressRepository,
        TenantEmailAddressMapper tenantEmailAddressMapper
    ) {
        this.tenantEmailAddressRepository = tenantEmailAddressRepository;
        this.tenantEmailAddressMapper = tenantEmailAddressMapper;
    }

    @Override
    @CacheEvict(value = "tenantEmailAddresses", allEntries = true)
    public TenantEmailAddressDTO save(TenantEmailAddressDTO tenantEmailAddressDTO) {
        log.debug("Request to save TenantEmailAddress : {}", tenantEmailAddressDTO);

        // Set default isActive if null
        if (tenantEmailAddressDTO.getIsActive() == null) {
            tenantEmailAddressDTO.setIsActive(true);
        }

        // Set default isDefault if null
        if (tenantEmailAddressDTO.getIsDefault() == null) {
            tenantEmailAddressDTO.setIsDefault(false);
        }

        // If setting as default, unset other defaults for the same tenant
        if (Boolean.TRUE.equals(tenantEmailAddressDTO.getIsDefault())) {
            tenantEmailAddressRepository
                .findByTenantIdAndIsDefaultTrue(tenantEmailAddressDTO.getTenantId())
                .ifPresent(existingDefault -> {
                    existingDefault.setIsDefault(false);
                    tenantEmailAddressRepository.save(existingDefault);
                });
        }

        TenantEmailAddress tenantEmailAddress = tenantEmailAddressMapper.toEntity(tenantEmailAddressDTO);

        try {
            tenantEmailAddress = tenantEmailAddressRepository.save(tenantEmailAddress);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to save TenantEmailAddress due to constraint violation: {}", e.getMessage());
            throw new IllegalArgumentException(
                "Email address with this type already exists for this tenant. Email: " +
                tenantEmailAddressDTO.getEmailAddress() +
                ", Type: " +
                tenantEmailAddressDTO.getEmailType()
            );
        }

        return tenantEmailAddressMapper.toDto(tenantEmailAddress);
    }

    @Override
    @CacheEvict(value = "tenantEmailAddresses", allEntries = true)
    public TenantEmailAddressDTO update(TenantEmailAddressDTO tenantEmailAddressDTO) {
        log.debug("Request to update TenantEmailAddress : {}", tenantEmailAddressDTO);

        // If setting as default, unset other defaults for the same tenant
        if (Boolean.TRUE.equals(tenantEmailAddressDTO.getIsDefault())) {
            tenantEmailAddressRepository
                .findByTenantIdAndIsDefaultTrue(tenantEmailAddressDTO.getTenantId())
                .filter(existing -> !existing.getId().equals(tenantEmailAddressDTO.getId()))
                .ifPresent(existingDefault -> {
                    existingDefault.setIsDefault(false);
                    tenantEmailAddressRepository.save(existingDefault);
                });
        }

        TenantEmailAddress tenantEmailAddress = tenantEmailAddressMapper.toEntity(tenantEmailAddressDTO);

        try {
            tenantEmailAddress = tenantEmailAddressRepository.save(tenantEmailAddress);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to update TenantEmailAddress due to constraint violation: {}", e.getMessage());
            throw new IllegalArgumentException(
                "Email address with this type already exists for this tenant. Email: " +
                tenantEmailAddressDTO.getEmailAddress() +
                ", Type: " +
                tenantEmailAddressDTO.getEmailType()
            );
        }

        return tenantEmailAddressMapper.toDto(tenantEmailAddress);
    }

    @Override
    @CacheEvict(value = "tenantEmailAddresses", allEntries = true)
    public Optional<TenantEmailAddressDTO> partialUpdate(TenantEmailAddressDTO tenantEmailAddressDTO) {
        log.debug("Request to partially update TenantEmailAddress : {}", tenantEmailAddressDTO);

        // If setting as default, unset other defaults for the same tenant
        if (Boolean.TRUE.equals(tenantEmailAddressDTO.getIsDefault())) {
            tenantEmailAddressRepository
                .findByTenantIdAndIsDefaultTrue(tenantEmailAddressDTO.getTenantId())
                .filter(existing -> !existing.getId().equals(tenantEmailAddressDTO.getId()))
                .ifPresent(existingDefault -> {
                    existingDefault.setIsDefault(false);
                    tenantEmailAddressRepository.save(existingDefault);
                });
        }

        return tenantEmailAddressRepository
            .findById(tenantEmailAddressDTO.getId())
            .map(existingTenantEmailAddress -> {
                tenantEmailAddressMapper.partialUpdate(existingTenantEmailAddress, tenantEmailAddressDTO);
                return existingTenantEmailAddress;
            })
            .map(tenantEmailAddress -> {
                try {
                    return tenantEmailAddressRepository.save(tenantEmailAddress);
                } catch (DataIntegrityViolationException e) {
                    log.error("Failed to update TenantEmailAddress due to constraint violation: {}", e.getMessage());
                    throw new IllegalArgumentException("Email address with this type already exists for this tenant.");
                }
            })
            .map(tenantEmailAddressMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TenantEmailAddressDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TenantEmailAddresses");
        return tenantEmailAddressRepository.findAll(pageable).map(tenantEmailAddressMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TenantEmailAddressDTO> findOne(Long id) {
        log.debug("Request to get TenantEmailAddress : {}", id);
        return tenantEmailAddressRepository.findById(id).map(tenantEmailAddressMapper::toDto);
    }

    @Override
    @CacheEvict(value = "tenantEmailAddresses", allEntries = true)
    public void delete(Long id) {
        log.debug("Request to delete TenantEmailAddress : {}", id);
        tenantEmailAddressRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "tenantEmailAddresses", key = "'tenantId:' + #tenantId", unless = "#result == null || #result.isEmpty()")
    public List<TenantEmailAddressDTO> findByTenantId(String tenantId) {
        log.debug("Request to get TenantEmailAddresses by tenantId : {}", tenantId);
        return tenantEmailAddressRepository
            .findByTenantId(tenantId)
            .stream()
            .map(tenantEmailAddressMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(
        value = "tenantEmailAddresses",
        key = "'tenantId:' + #tenantId + ':isActive:' + #isActive",
        unless = "#result == null || #result.isEmpty()"
    )
    public List<TenantEmailAddressDTO> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        log.debug("Request to get TenantEmailAddresses by tenantId {} and isActive : {}", tenantId, isActive);
        return tenantEmailAddressRepository
            .findByTenantIdAndIsActive(tenantId, isActive)
            .stream()
            .map(tenantEmailAddressMapper::toDto)
            .collect(Collectors.toList());
    }
}
