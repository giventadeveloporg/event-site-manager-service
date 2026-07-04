package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.GasStationDailyMetrics;
import com.eventsitemanager.repository.GasStationDailyMetricsRepository;
import com.eventsitemanager.service.GasStationDailyMetricsService;
import com.eventsitemanager.service.dto.GasStationDailyMetricsDTO;
import com.eventsitemanager.service.mapper.GasStationDailyMetricsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GasStationDailyMetricsServiceImpl implements GasStationDailyMetricsService {

    private static final Logger LOG = LoggerFactory.getLogger(GasStationDailyMetricsServiceImpl.class);

    private final GasStationDailyMetricsRepository gasStationDailyMetricsRepository;
    private final GasStationDailyMetricsMapper gasStationDailyMetricsMapper;

    public GasStationDailyMetricsServiceImpl(
        GasStationDailyMetricsRepository gasStationDailyMetricsRepository,
        GasStationDailyMetricsMapper gasStationDailyMetricsMapper
    ) {
        this.gasStationDailyMetricsRepository = gasStationDailyMetricsRepository;
        this.gasStationDailyMetricsMapper = gasStationDailyMetricsMapper;
    }

    @Override
    public GasStationDailyMetricsDTO save(GasStationDailyMetricsDTO gasStationDailyMetricsDTO) {
        LOG.debug("Request to save GasStationDailyMetrics : {}", gasStationDailyMetricsDTO);
        GasStationDailyMetrics gasStationDailyMetrics = gasStationDailyMetricsMapper.toEntity(gasStationDailyMetricsDTO);
        if (gasStationDailyMetrics.getId() != null) {
            LOG.warn(
                "GasStationDailyMetrics has ID {} set during create operation. Clearing ID to force sequence generation.",
                gasStationDailyMetrics.getId()
            );
            gasStationDailyMetrics.setId(null);
        }

        gasStationDailyMetrics = gasStationDailyMetricsRepository.save(gasStationDailyMetrics);
        return gasStationDailyMetricsMapper.toDto(gasStationDailyMetrics);
    }

    @Override
    public GasStationDailyMetricsDTO update(GasStationDailyMetricsDTO gasStationDailyMetricsDTO) {
        LOG.debug("Request to update GasStationDailyMetrics : {}", gasStationDailyMetricsDTO);
        GasStationDailyMetrics gasStationDailyMetrics = gasStationDailyMetricsMapper.toEntity(gasStationDailyMetricsDTO);

        gasStationDailyMetrics = gasStationDailyMetricsRepository.save(gasStationDailyMetrics);
        return gasStationDailyMetricsMapper.toDto(gasStationDailyMetrics);
    }

    @Override
    public Optional<GasStationDailyMetricsDTO> partialUpdate(GasStationDailyMetricsDTO gasStationDailyMetricsDTO) {
        LOG.debug("Request to partially update GasStationDailyMetrics : {}", gasStationDailyMetricsDTO);

        return gasStationDailyMetricsRepository
            .findById(gasStationDailyMetricsDTO.getId())
            .map(existing -> {
                gasStationDailyMetricsMapper.partialUpdate(existing, gasStationDailyMetricsDTO);

                return existing;
            })
            .map(gasStationDailyMetricsRepository::save)
            .map(gasStationDailyMetricsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GasStationDailyMetricsDTO> findOne(Long id) {
        LOG.debug("Request to get GasStationDailyMetrics : {}", id);
        return gasStationDailyMetricsRepository.findById(id).map(gasStationDailyMetricsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete GasStationDailyMetrics : {}", id);
        gasStationDailyMetricsRepository.deleteById(id);
    }
}
