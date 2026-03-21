package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.SatelliteDomain;
import com.nextjstemplate.repository.SatelliteDomainRepository;
import com.nextjstemplate.service.SatelliteDomainService;
import com.nextjstemplate.service.dto.SatelliteDomainDTO;
import com.nextjstemplate.service.mapper.SatelliteDomainMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.SatelliteDomain}.
 */
@Service
@Transactional
public class SatelliteDomainServiceImpl implements SatelliteDomainService {

    private static final Logger LOG = LoggerFactory.getLogger(SatelliteDomainServiceImpl.class);

    private final SatelliteDomainRepository satelliteDomainRepository;

    private final SatelliteDomainMapper satelliteDomainMapper;

    public SatelliteDomainServiceImpl(SatelliteDomainRepository satelliteDomainRepository, SatelliteDomainMapper satelliteDomainMapper) {
        this.satelliteDomainRepository = satelliteDomainRepository;
        this.satelliteDomainMapper = satelliteDomainMapper;
    }

    @Override
    @CacheEvict(value = "satelliteDomains", allEntries = true)
    public SatelliteDomainDTO save(SatelliteDomainDTO satelliteDomainDTO) {
        LOG.debug("Request to save SatelliteDomain : {}", satelliteDomainDTO);
        SatelliteDomain satelliteDomain = satelliteDomainMapper.toEntity(satelliteDomainDTO);
        satelliteDomain = satelliteDomainRepository.save(satelliteDomain);
        return satelliteDomainMapper.toDto(satelliteDomain);
    }

    @Override
    @CacheEvict(value = "satelliteDomains", allEntries = true)
    public SatelliteDomainDTO update(SatelliteDomainDTO satelliteDomainDTO) {
        LOG.debug("Request to update SatelliteDomain : {}", satelliteDomainDTO);
        SatelliteDomain satelliteDomain = satelliteDomainMapper.toEntity(satelliteDomainDTO);
        satelliteDomain = satelliteDomainRepository.save(satelliteDomain);
        return satelliteDomainMapper.toDto(satelliteDomain);
    }

    @Override
    public Optional<SatelliteDomainDTO> partialUpdate(SatelliteDomainDTO satelliteDomainDTO) {
        LOG.debug("Request to partially update SatelliteDomain : {}", satelliteDomainDTO);

        return satelliteDomainRepository
            .findById(satelliteDomainDTO.getId())
            .map(existingSatelliteDomain -> {
                satelliteDomainMapper.partialUpdate(existingSatelliteDomain, satelliteDomainDTO);

                return existingSatelliteDomain;
            })
            .map(satelliteDomainRepository::save)
            .map(satelliteDomainMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "satelliteDomains", key = "#id", unless = "#result == null")
    public Optional<SatelliteDomainDTO> findOne(Long id) {
        LOG.debug("Request to get SatelliteDomain : {}", id);
        return satelliteDomainRepository.findById(id).map(satelliteDomainMapper::toDto);
    }

    @Override
    @CacheEvict(value = "satelliteDomains", allEntries = true)
    public void delete(Long id) {
        LOG.debug("Request to delete SatelliteDomain : {}", id);
        satelliteDomainRepository.deleteById(id);
    }
}
