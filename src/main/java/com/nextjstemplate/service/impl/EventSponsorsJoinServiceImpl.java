package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventSponsorsJoin;
import com.nextjstemplate.repository.EventSponsorsJoinRepository;
import com.nextjstemplate.service.EventSponsorsJoinService;
import com.nextjstemplate.service.dto.EventSponsorsJoinDTO;
import com.nextjstemplate.service.mapper.EventSponsorsJoinMapper;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EventSponsorsJoin}.
 */
@Service
@Transactional
public class EventSponsorsJoinServiceImpl implements EventSponsorsJoinService {

  private final Logger log = LoggerFactory.getLogger(EventSponsorsJoinServiceImpl.class);

  private final EventSponsorsJoinRepository eventSponsorsJoinRepository;

  private final EventSponsorsJoinMapper eventSponsorsJoinMapper;

  public EventSponsorsJoinServiceImpl(
      EventSponsorsJoinRepository eventSponsorsJoinRepository,
      EventSponsorsJoinMapper eventSponsorsJoinMapper) {
    this.eventSponsorsJoinRepository = eventSponsorsJoinRepository;
    this.eventSponsorsJoinMapper = eventSponsorsJoinMapper;
  }

  @Override
  public EventSponsorsJoinDTO save(EventSponsorsJoinDTO eventSponsorsJoinDTO) {
    log.debug("Request to save EventSponsorsJoin : {}", eventSponsorsJoinDTO);
    EventSponsorsJoin eventSponsorsJoin = eventSponsorsJoinMapper.toEntity(eventSponsorsJoinDTO);

    // Set timestamp for new entities (EventSponsorsJoin only has createdAt)
    ZonedDateTime now = ZonedDateTime.now();
    if (eventSponsorsJoin.getId() == null) {
      eventSponsorsJoin.setCreatedAt(now);
    }

    eventSponsorsJoin = eventSponsorsJoinRepository.save(eventSponsorsJoin);
    return eventSponsorsJoinMapper.toDto(eventSponsorsJoin);
  }

  @Override
  public EventSponsorsJoinDTO update(EventSponsorsJoinDTO eventSponsorsJoinDTO) {
    log.debug("Request to update EventSponsorsJoin : {}", eventSponsorsJoinDTO);
    EventSponsorsJoin eventSponsorsJoin = eventSponsorsJoinMapper.toEntity(eventSponsorsJoinDTO);

    // EventSponsorsJoin only has createdAt, no updatedAt field
    eventSponsorsJoin = eventSponsorsJoinRepository.save(eventSponsorsJoin);
    return eventSponsorsJoinMapper.toDto(eventSponsorsJoin);
  }

  @Override
  public Optional<EventSponsorsJoinDTO> partialUpdate(EventSponsorsJoinDTO eventSponsorsJoinDTO) {
    log.debug("Request to partially update EventSponsorsJoin : {}", eventSponsorsJoinDTO);

    return eventSponsorsJoinRepository
        .findById(eventSponsorsJoinDTO.getId())
        .map(existingEventSponsorsJoin -> {
          eventSponsorsJoinMapper.partialUpdate(existingEventSponsorsJoin, eventSponsorsJoinDTO);
          return existingEventSponsorsJoin;
        })
        .map(eventSponsorsJoinRepository::save)
        .map(eventSponsorsJoinMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<EventSponsorsJoinDTO> findAll(Pageable pageable) {
    log.debug("Request to get all EventSponsorsJoin");
    return eventSponsorsJoinRepository.findAll(pageable).map(eventSponsorsJoinMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<EventSponsorsJoinDTO> findOne(Long id) {
    log.debug("Request to get EventSponsorsJoin : {}", id);
    return eventSponsorsJoinRepository.findById(id).map(eventSponsorsJoinMapper::toDto);
  }

  @Override
  public void delete(Long id) {
    log.debug("Request to delete EventSponsorsJoin : {}", id);
    eventSponsorsJoinRepository.deleteById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventSponsorsJoinDTO> findByEventId(Long eventId) {
    log.debug("Request to get all EventSponsorsJoin for event : {}", eventId);
    return eventSponsorsJoinRepository
        .findByEventId(eventId)
        .stream()
        .map(eventSponsorsJoinMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<EventSponsorsJoinDTO> findByEventId(Long eventId, Pageable pageable) {
    log.debug("Request to get all EventSponsorsJoin for event : {} with pagination", eventId);
    return eventSponsorsJoinRepository.findByEventId(eventId, pageable).map(eventSponsorsJoinMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventSponsorsJoinDTO> findBySponsorId(Long sponsorId) {
    log.debug("Request to get all EventSponsorsJoin for sponsor : {}", sponsorId);
    return eventSponsorsJoinRepository
        .findBySponsorId(sponsorId)
        .stream()
        .map(eventSponsorsJoinMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<EventSponsorsJoinDTO> findBySponsorId(Long sponsorId, Pageable pageable) {
    log.debug("Request to get all EventSponsorsJoin for sponsor : {} with pagination", sponsorId);
    return eventSponsorsJoinRepository.findBySponsorId(sponsorId, pageable).map(eventSponsorsJoinMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<EventSponsorsJoinDTO> findByEventIdAndSponsorId(Long eventId, Long sponsorId) {
    log.debug("Request to get EventSponsorsJoin for event : {} and sponsor : {}", eventId, sponsorId);
    return eventSponsorsJoinRepository
        .findByEventIdAndSponsorId(eventId, sponsorId)
        .map(eventSponsorsJoinMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsByEventIdAndSponsorId(Long eventId, Long sponsorId) {
    log.debug("Request to check if EventSponsorsJoin exists for event : {} and sponsor : {}", eventId, sponsorId);
    return eventSponsorsJoinRepository.existsByEventIdAndSponsorId(eventId, sponsorId);
  }

  @Override
  @Transactional(readOnly = true)
  public long countByEventId(Long eventId) {
    log.debug("Request to count EventSponsorsJoin for event : {}", eventId);
    return eventSponsorsJoinRepository.countByEventId(eventId);
  }

  @Override
  @Transactional(readOnly = true)
  public long countBySponsorId(Long sponsorId) {
    log.debug("Request to count EventSponsorsJoin for sponsor : {}", sponsorId);
    return eventSponsorsJoinRepository.countBySponsorId(sponsorId);
  }

  @Override
  public void deleteByEventId(Long eventId) {
    log.debug("Request to delete all EventSponsorsJoin for event : {}", eventId);
    eventSponsorsJoinRepository.deleteByEventId(eventId);
  }

  @Override
  public void deleteBySponsorId(Long sponsorId) {
    log.debug("Request to delete all EventSponsorsJoin for sponsor : {}", sponsorId);
    eventSponsorsJoinRepository.deleteBySponsorId(sponsorId);
  }

  @Override
  public void deleteByEventIdAndSponsorId(Long eventId, Long sponsorId) {
    log.debug("Request to delete EventSponsorsJoin for event : {} and sponsor : {}", eventId, sponsorId);
    eventSponsorsJoinRepository.deleteByEventIdAndSponsorId(eventId, sponsorId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventSponsorsJoinDTO> findByEventIdOrderByCreatedAt(Long eventId) {
    log.debug("Request to get all EventSponsorsJoin for event : {} ordered by creation date", eventId);
    return eventSponsorsJoinRepository
        .findByEventIdOrderByCreatedAt(eventId)
        .stream()
        .map(eventSponsorsJoinMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventSponsorsJoinDTO> findBySponsorIdOrderByCreatedAt(Long sponsorId) {
    log.debug("Request to get all EventSponsorsJoin for sponsor : {} ordered by creation date", sponsorId);
    return eventSponsorsJoinRepository
        .findBySponsorIdOrderByCreatedAt(sponsorId)
        .stream()
        .map(eventSponsorsJoinMapper::toDto)
        .collect(Collectors.toList());
  }
}
