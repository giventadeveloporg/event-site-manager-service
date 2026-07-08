package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.GasStationIntegration;
import com.eventsitemanager.repository.GasStationIntegrationRepository;
import com.eventsitemanager.service.GasStationAccessService;
import com.eventsitemanager.service.GasStationIntegrationService;
import com.eventsitemanager.service.dto.GasStationIntegrationDTO;
import com.eventsitemanager.service.mapper.GasStationIntegrationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GasStationIntegrationServiceImpl implements GasStationIntegrationService {

    private static final Logger LOG = LoggerFactory.getLogger(GasStationIntegrationServiceImpl.class);

    private final GasStationIntegrationRepository gasStationIntegrationRepository;
    private final GasStationIntegrationMapper gasStationIntegrationMapper;
    private final GasStationAccessService gasStationAccessService;

    public GasStationIntegrationServiceImpl(
        GasStationIntegrationRepository gasStationIntegrationRepository,
        GasStationIntegrationMapper gasStationIntegrationMapper,
        GasStationAccessService gasStationAccessService
    ) {
        this.gasStationIntegrationRepository = gasStationIntegrationRepository;
        this.gasStationIntegrationMapper = gasStationIntegrationMapper;
        this.gasStationAccessService = gasStationAccessService;
    }

    @Override
    public GasStationIntegrationDTO save(GasStationIntegrationDTO gasStationIntegrationDTO) {
        LOG.debug("Request to save GasStationIntegration : {}", gasStationIntegrationDTO);
        gasStationAccessService.assertAllLocationsScope();
        GasStationIntegration gasStationIntegration = gasStationIntegrationMapper.toEntity(gasStationIntegrationDTO);
        if (gasStationIntegration.getId() != null) {
            LOG.warn(
                "GasStationIntegration has ID {} set during create operation. Clearing ID to force sequence generation.",
                gasStationIntegration.getId()
            );
            gasStationIntegration.setId(null);
        }

        gasStationIntegration = gasStationIntegrationRepository.save(gasStationIntegration);
        return gasStationIntegrationMapper.toDto(gasStationIntegration);
    }

    @Override
    public GasStationIntegrationDTO update(GasStationIntegrationDTO gasStationIntegrationDTO) {
        LOG.debug("Request to update GasStationIntegration : {}", gasStationIntegrationDTO);
        gasStationAccessService.assertAllLocationsScope();
        GasStationIntegration gasStationIntegration = gasStationIntegrationMapper.toEntity(gasStationIntegrationDTO);

        gasStationIntegration = gasStationIntegrationRepository.save(gasStationIntegration);
        return gasStationIntegrationMapper.toDto(gasStationIntegration);
    }

    @Override
    public Optional<GasStationIntegrationDTO> partialUpdate(GasStationIntegrationDTO gasStationIntegrationDTO) {
        LOG.debug("Request to partially update GasStationIntegration : {}", gasStationIntegrationDTO);
        gasStationAccessService.assertAllLocationsScope();

        return gasStationIntegrationRepository
            .findById(gasStationIntegrationDTO.getId())
            .map(existing -> {
                gasStationIntegrationMapper.partialUpdate(existing, gasStationIntegrationDTO);

                return existing;
            })
            .map(gasStationIntegrationRepository::save)
            .map(gasStationIntegrationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GasStationIntegrationDTO> findOne(Long id) {
        LOG.debug("Request to get GasStationIntegration : {}", id);
        gasStationAccessService.assertGasModuleAccess();
        return gasStationIntegrationRepository
            .findById(id)
            .map(gasStationIntegrationMapper::toDto)
            .map(dto -> {
                gasStationAccessService.assertStationIdAllowed(dto.getStationId());
                return dto;
            });
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete GasStationIntegration : {}", id);
        gasStationAccessService.assertAllLocationsScope();
        gasStationIntegrationRepository.deleteById(id);
    }
}
