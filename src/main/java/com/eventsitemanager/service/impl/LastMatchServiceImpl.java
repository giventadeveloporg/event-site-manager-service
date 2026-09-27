package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.LastMatch;
import com.eventsitemanager.repository.LastMatchRepository;
import com.eventsitemanager.service.LastMatchService;
import com.eventsitemanager.service.dto.LastMatchDTO;
import com.eventsitemanager.service.mapper.LastMatchMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eventsitemanager.domain.LastMatch}.
 */
@Service
@Transactional
public class LastMatchServiceImpl implements LastMatchService {

    private static final Logger LOG = LoggerFactory.getLogger(LastMatchServiceImpl.class);

    private final LastMatchRepository lastMatchRepository;

    private final LastMatchMapper lastMatchMapper;

    public LastMatchServiceImpl(LastMatchRepository lastMatchRepository, LastMatchMapper lastMatchMapper) {
        this.lastMatchRepository = lastMatchRepository;
        this.lastMatchMapper = lastMatchMapper;
    }

    @Override
    public LastMatchDTO save(LastMatchDTO lastMatchDTO) {
        LOG.debug("Request to save LastMatch : {}", lastMatchDTO);
        LastMatch lastMatch = lastMatchMapper.toEntity(lastMatchDTO);

        if (lastMatch.getId() != null) {
            LOG.warn("LastMatch has ID {} set during create operation. Clearing ID to force sequence generation.", lastMatch.getId());
            lastMatch.setId(null);
        }

        lastMatch = lastMatchRepository.save(lastMatch);
        return lastMatchMapper.toDto(lastMatch);
    }

    @Override
    public LastMatchDTO update(LastMatchDTO lastMatchDTO) {
        LOG.debug("Request to update LastMatch : {}", lastMatchDTO);
        LastMatch lastMatch = lastMatchMapper.toEntity(lastMatchDTO);
        lastMatch = lastMatchRepository.save(lastMatch);
        return lastMatchMapper.toDto(lastMatch);
    }

    @Override
    public Optional<LastMatchDTO> partialUpdate(LastMatchDTO lastMatchDTO) {
        LOG.debug("Request to partially update LastMatch : {}", lastMatchDTO);

        return lastMatchRepository
            .findById(lastMatchDTO.getId())
            .map(existingLastMatch -> {
                lastMatchMapper.partialUpdate(existingLastMatch, lastMatchDTO);
                return existingLastMatch;
            })
            .map(lastMatchRepository::save)
            .map(lastMatchMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LastMatchDTO> findOne(Long id) {
        LOG.debug("Request to get LastMatch : {}", id);
        return lastMatchRepository.findById(id).map(lastMatchMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete LastMatch : {}", id);
        lastMatchRepository.deleteById(id);
    }
}
