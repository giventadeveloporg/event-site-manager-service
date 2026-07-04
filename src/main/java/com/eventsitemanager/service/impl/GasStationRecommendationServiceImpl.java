package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.GasStationRecommendation;
import com.eventsitemanager.repository.GasStationRecommendationRepository;
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

    public GasStationRecommendationServiceImpl(
        GasStationRecommendationRepository gasStationRecommendationRepository,
        GasStationRecommendationMapper gasStationRecommendationMapper
    ) {
        this.gasStationRecommendationRepository = gasStationRecommendationRepository;
        this.gasStationRecommendationMapper = gasStationRecommendationMapper;
    }

    @Override
    public GasStationRecommendationDTO save(GasStationRecommendationDTO gasStationRecommendationDTO) {
        LOG.debug("Request to save GasStationRecommendation : {}", gasStationRecommendationDTO);
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
        return gasStationRecommendationRepository.findById(id).map(gasStationRecommendationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete GasStationRecommendation : {}", id);
        gasStationRecommendationRepository.deleteById(id);
    }
}
