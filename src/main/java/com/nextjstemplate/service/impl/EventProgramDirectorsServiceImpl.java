package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventProgramDirectors;
import com.nextjstemplate.repository.EventProgramDirectorsRepository;
import com.nextjstemplate.service.EventProgramDirectorsService;
import com.nextjstemplate.service.dto.EventProgramDirectorsDTO;
import com.nextjstemplate.service.mapper.EventProgramDirectorsMapper;
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
 * Service Implementation for managing {@link EventProgramDirectors}.
 */
@Service
@Transactional
public class EventProgramDirectorsServiceImpl implements EventProgramDirectorsService {

  private final Logger log = LoggerFactory.getLogger(EventProgramDirectorsServiceImpl.class);

  private final EventProgramDirectorsRepository eventProgramDirectorsRepository;

  private final EventProgramDirectorsMapper eventProgramDirectorsMapper;

  public EventProgramDirectorsServiceImpl(
      EventProgramDirectorsRepository eventProgramDirectorsRepository,
      EventProgramDirectorsMapper eventProgramDirectorsMapper) {
    this.eventProgramDirectorsRepository = eventProgramDirectorsRepository;
    this.eventProgramDirectorsMapper = eventProgramDirectorsMapper;
  }

  @Override
  public EventProgramDirectorsDTO save(EventProgramDirectorsDTO eventProgramDirectorsDTO) {
    log.debug("Request to save EventProgramDirectors : {}", eventProgramDirectorsDTO);
    EventProgramDirectors eventProgramDirectors = eventProgramDirectorsMapper.toEntity(eventProgramDirectorsDTO);

    // Set timestamps for new entities
    ZonedDateTime now = ZonedDateTime.now();
    if (eventProgramDirectors.getId() == null) {
      eventProgramDirectors.setCreatedAt(now);
    }
    eventProgramDirectors.setUpdatedAt(now);

    eventProgramDirectors = eventProgramDirectorsRepository.save(eventProgramDirectors);
    return eventProgramDirectorsMapper.toDto(eventProgramDirectors);
  }

  @Override
  public EventProgramDirectorsDTO update(EventProgramDirectorsDTO eventProgramDirectorsDTO) {
    log.debug("Request to update EventProgramDirectors : {}", eventProgramDirectorsDTO);
    EventProgramDirectors eventProgramDirectors = eventProgramDirectorsMapper.toEntity(eventProgramDirectorsDTO);

    // Set updatedAt timestamp
    eventProgramDirectors.setUpdatedAt(ZonedDateTime.now());

    eventProgramDirectors = eventProgramDirectorsRepository.save(eventProgramDirectors);
    return eventProgramDirectorsMapper.toDto(eventProgramDirectors);
  }

  @Override
  public Optional<EventProgramDirectorsDTO> partialUpdate(EventProgramDirectorsDTO eventProgramDirectorsDTO) {
    log.debug("Request to partially update EventProgramDirectors : {}", eventProgramDirectorsDTO);

    return eventProgramDirectorsRepository
        .findById(eventProgramDirectorsDTO.getId())
        .map(existingEventProgramDirectors -> {
          eventProgramDirectorsMapper.partialUpdate(existingEventProgramDirectors, eventProgramDirectorsDTO);
          return existingEventProgramDirectors;
        })
        .map(eventProgramDirectorsRepository::save)
        .map(eventProgramDirectorsMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<EventProgramDirectorsDTO> findAll(Pageable pageable) {
    log.debug("Request to get all EventProgramDirectors");
    return eventProgramDirectorsRepository.findAll(pageable).map(eventProgramDirectorsMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<EventProgramDirectorsDTO> findOne(Long id) {
    log.debug("Request to get EventProgramDirectors : {}", id);
    return eventProgramDirectorsRepository.findById(id).map(eventProgramDirectorsMapper::toDto);
  }

  @Override
  public void delete(Long id) {
    log.debug("Request to delete EventProgramDirectors : {}", id);
    eventProgramDirectorsRepository.deleteById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventProgramDirectorsDTO> findByEventId(Long eventId) {
    log.debug("Request to get all EventProgramDirectors for event : {}", eventId);
    return eventProgramDirectorsRepository
        .findByEventId(eventId)
        .stream()
        .map(eventProgramDirectorsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<EventProgramDirectorsDTO> findByEventId(Long eventId, Pageable pageable) {
    log.debug("Request to get all EventProgramDirectors for event : {} with pagination", eventId);
    return eventProgramDirectorsRepository.findByEventId(eventId, pageable).map(eventProgramDirectorsMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventProgramDirectorsDTO> findByNameContaining(String name) {
    log.debug("Request to get all EventProgramDirectors by name containing : {}", name);
    return eventProgramDirectorsRepository
        .findByNameContainingIgnoreCase(name)
        .stream()
        .map(eventProgramDirectorsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventProgramDirectorsDTO> findByEventIdAndName(Long eventId, String name) {
    log.debug("Request to get all EventProgramDirectors for event : {} and name : {}", eventId, name);
    return eventProgramDirectorsRepository
        .findByEventIdAndName(eventId, name)
        .stream()
        .map(eventProgramDirectorsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventProgramDirectorsDTO> findByEventIdAndNameContaining(Long eventId, String name) {
    log.debug("Request to get all EventProgramDirectors for event : {} and name containing : {}", eventId, name);
    return eventProgramDirectorsRepository
        .findByEventIdAndNameContainingIgnoreCase(eventId, name)
        .stream()
        .map(eventProgramDirectorsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventProgramDirectorsDTO> findByBioContaining(String bio) {
    log.debug("Request to get all EventProgramDirectors by bio containing : {}", bio);
    return eventProgramDirectorsRepository
        .findByBioContainingIgnoreCase(bio)
        .stream()
        .map(eventProgramDirectorsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventProgramDirectorsDTO> findByEventIdAndBioContaining(Long eventId, String bio) {
    log.debug("Request to get all EventProgramDirectors for event : {} and bio containing : {}", eventId, bio);
    return eventProgramDirectorsRepository
        .findByEventIdAndBioContainingIgnoreCase(eventId, bio)
        .stream()
        .map(eventProgramDirectorsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public long countByEventId(Long eventId) {
    log.debug("Request to count EventProgramDirectors for event : {}", eventId);
    return eventProgramDirectorsRepository.countByEventId(eventId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventProgramDirectorsDTO> findByEventIdOrderByCreatedAt(Long eventId) {
    log.debug("Request to get all EventProgramDirectors for event : {} ordered by creation date", eventId);
    return eventProgramDirectorsRepository
        .findByEventIdOrderByCreatedAt(eventId)
        .stream()
        .map(eventProgramDirectorsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventProgramDirectorsDTO> findByEventIdOrderByName(Long eventId) {
    log.debug("Request to get all EventProgramDirectors for event : {} ordered by name", eventId);
    return eventProgramDirectorsRepository
        .findByEventIdOrderByName(eventId)
        .stream()
        .map(eventProgramDirectorsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventProgramDirectorsDTO> findByEventIdWithPhoto(Long eventId) {
    log.debug("Request to get all EventProgramDirectors with photo for event : {}", eventId);
    return eventProgramDirectorsRepository
        .findByEventIdWithPhoto(eventId)
        .stream()
        .map(eventProgramDirectorsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventProgramDirectorsDTO> findByEventIdWithoutPhoto(Long eventId) {
    log.debug("Request to get all EventProgramDirectors without photo for event : {}", eventId);
    return eventProgramDirectorsRepository
        .findByEventIdWithoutPhoto(eventId)
        .stream()
        .map(eventProgramDirectorsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventProgramDirectorsDTO> findByEventIdWithBio(Long eventId) {
    log.debug("Request to get all EventProgramDirectors with bio for event : {}", eventId);
    return eventProgramDirectorsRepository
        .findByEventIdWithBio(eventId)
        .stream()
        .map(eventProgramDirectorsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventProgramDirectorsDTO> findByEventIdWithoutBio(Long eventId) {
    log.debug("Request to get all EventProgramDirectors without bio for event : {}", eventId);
    return eventProgramDirectorsRepository
        .findByEventIdWithoutBio(eventId)
        .stream()
        .map(eventProgramDirectorsMapper::toDto)
        .collect(Collectors.toList());
  }
}
