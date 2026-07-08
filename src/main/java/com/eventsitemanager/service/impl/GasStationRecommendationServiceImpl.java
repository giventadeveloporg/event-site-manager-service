package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.GasStationRecommendation;
import com.eventsitemanager.repository.GasStationRecommendationRepository;
import com.eventsitemanager.service.GasStationAccessService;
import com.eventsitemanager.service.GasStationRecommendationService;
import com.eventsitemanager.service.dto.GasStationRecommendationDTO;
import com.eventsitemanager.service.mapper.GasStationRecommendationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GasStationRecommendationServiceImpl implements GasStationRecommendationService {

    private static final Logger LOG = LoggerFactory.getLogger(GasStationRecommendationServiceImpl.class);

    private final GasStationRecommendationRepository gasStationRecommendationRepository;
    private final GasStationRecommendationMapper gasStationRecommendationMapper;
    private final GasStationAccessService gasStationAccessService;

    public GasStationRecommendationServiceImpl(
        GasStationRecommendationRepository gasStationRecommendationRepository,
        GasStationRecommendationMapper gasStationRecommendationMapper,
        GasStationAccessService gasStationAccessService
    ) {
        this.gasStationRecommendationRepository = gasStationRecommendationRepository;
        this.gasStationRecommendationMapper = gasStationRecommendationMapper;
        this.gasStationAccessService = gasStationAccessService;
    }

    @Override
    public GasStationRecommendationDTO save(GasStationRecommendationDTO gasStationRecommendationDTO) {
        LOG.debug("Request to save GasStationRecommendation : {}", gasStationRecommendationDTO);
        gasStationAccessService.assertAllLocationsScope();
        GasStationRecommendation gasStationRecommendation = gasStationRecommendationMapper.toEntity(gasStationRecommendationDTO);
        if (gasStationRecommendation.getId() != null) {
            LOG.warn(
                "GasStationRecommendation has ID {} set during create operation. Clearing ID to force sequence generation.",
                gasStationRecommendation.getId()
            );
            gasStationRecommendation.setId(null);
        }

        gasStationRecommendation = gasStationRecommendationRepository.save(gasStationRecommendation);
        return gasStationRecommendationMapper.toDto(gasStationRecommendation);
    }

    @Override
    public GasStationRecommendationDTO update(GasStationRecommendationDTO gasStationRecommendationDTO) {
        LOG.debug("Request to update GasStationRecommendation : {}", gasStationRecommendationDTO);
        gasStationAccessService.assertAllLocationsScope();
        GasStationRecommendation gasStationRecommendation = gasStationRecommendationMapper.toEntity(gasStationRecommendationDTO);

        gasStationRecommendation = gasStationRecommendationRepository.save(gasStationRecommendation);
        return gasStationRecommendationMapper.toDto(gasStationRecommendation);
    }

    @Override
    public Optional<GasStationRecommendationDTO> partialUpdate(GasStationRecommendationDTO gasStationRecommendationDTO) {
        LOG.debug("Request to partially update GasStationRecommendation : {}", gasStationRecommendationDTO);

        return gasStationRecommendationRepository
            .findById(gasStationRecommendationDTO.getId())
            .map(existing -> {
                gasStationAccessService.assertRecommendationAccess(existing.getStationId());
                gasStationRecommendationMapper.partialUpdate(existing, gasStationRecommendationDTO);
                return existing;
            })
            .map(gasStationRecommendationRepository::save)
            .map(gasStationRecommendationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GasStationRecommendationDTO> findOne(Long id) {
        LOG.debug("Request to get GasStationRecommendation : {}", id);
        gasStationAccessService.assertGasModuleAccess();
        return gasStationRecommendationRepository
            .findById(id)
            .map(gasStationRecommendationMapper::toDto)
            .map(dto -> {
                gasStationAccessService.assertRecommendationAccess(dto.getStationId());
                return dto;
            });
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete GasStationRecommendation : {}", id);
        gasStationAccessService.assertAllLocationsScope();
        gasStationRecommendationRepository.deleteById(id);
    }
}
