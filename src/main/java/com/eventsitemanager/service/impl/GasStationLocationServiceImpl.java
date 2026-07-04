package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.GasStationLocation;
import com.eventsitemanager.repository.GasStationLocationRepository;
import com.eventsitemanager.service.GasStationLocationService;
import com.eventsitemanager.service.dto.GasStationLocationDTO;
import com.eventsitemanager.service.mapper.GasStationLocationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GasStationLocationServiceImpl implements GasStationLocationService {

    private static final Logger LOG = LoggerFactory.getLogger(GasStationLocationServiceImpl.class);

    private final GasStationLocationRepository gasStationLocationRepository;
    private final GasStationLocationMapper gasStationLocationMapper;

    public GasStationLocationServiceImpl(
        GasStationLocationRepository gasStationLocationRepository,
        GasStationLocationMapper gasStationLocationMapper
    ) {
        this.gasStationLocationRepository = gasStationLocationRepository;
        this.gasStationLocationMapper = gasStationLocationMapper;
    }

    @Override
    public GasStationLocationDTO save(GasStationLocationDTO gasStationLocationDTO) {
        LOG.debug("Request to save GasStationLocation : {}", gasStationLocationDTO);
        GasStationLocation gasStationLocation = gasStationLocationMapper.toEntity(gasStationLocationDTO);
        if (gasStationLocation.getId() != null) {
            LOG.warn(
                "GasStationLocation has ID {} set during create operation. Clearing ID to force sequence generation.",
                gasStationLocation.getId()
            );
            gasStationLocation.setId(null);
        }

        gasStationLocation = gasStationLocationRepository.save(gasStationLocation);
        return gasStationLocationMapper.toDto(gasStationLocation);
    }

    @Override
    public GasStationLocationDTO update(GasStationLocationDTO gasStationLocationDTO) {
        LOG.debug("Request to update GasStationLocation : {}", gasStationLocationDTO);
        GasStationLocation gasStationLocation = gasStationLocationMapper.toEntity(gasStationLocationDTO);

        gasStationLocation = gasStationLocationRepository.save(gasStationLocation);
        return gasStationLocationMapper.toDto(gasStationLocation);
    }

    @Override
    public Optional<GasStationLocationDTO> partialUpdate(GasStationLocationDTO gasStationLocationDTO) {
        LOG.debug("Request to partially update GasStationLocation : {}", gasStationLocationDTO);

        return gasStationLocationRepository
            .findById(gasStationLocationDTO.getId())
            .map(existing -> {
                gasStationLocationMapper.partialUpdate(existing, gasStationLocationDTO);

                return existing;
            })
            .map(gasStationLocationRepository::save)
            .map(gasStationLocationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GasStationLocationDTO> findOne(Long id) {
        LOG.debug("Request to get GasStationLocation : {}", id);
        return gasStationLocationRepository.findById(id).map(gasStationLocationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete GasStationLocation : {}", id);
        gasStationLocationRepository.deleteById(id);
    }
}
