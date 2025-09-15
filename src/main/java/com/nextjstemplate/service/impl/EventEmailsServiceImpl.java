package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventEmails;
import com.nextjstemplate.repository.EventEmailsRepository;
import com.nextjstemplate.service.EventEmailsService;
import com.nextjstemplate.service.dto.EventEmailsDTO;
import com.nextjstemplate.service.mapper.EventEmailsMapper;
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
 * Service Implementation for managing {@link EventEmails}.
 */
@Service
@Transactional
public class EventEmailsServiceImpl implements EventEmailsService {

  private final Logger log = LoggerFactory.getLogger(EventEmailsServiceImpl.class);

  private final EventEmailsRepository eventEmailsRepository;

  private final EventEmailsMapper eventEmailsMapper;

  public EventEmailsServiceImpl(EventEmailsRepository eventEmailsRepository, EventEmailsMapper eventEmailsMapper) {
    this.eventEmailsRepository = eventEmailsRepository;
    this.eventEmailsMapper = eventEmailsMapper;
  }

  @Override
  public EventEmailsDTO save(EventEmailsDTO eventEmailsDTO) {
    log.debug("Request to save EventEmails : {}", eventEmailsDTO);
    EventEmails eventEmails = eventEmailsMapper.toEntity(eventEmailsDTO);

    // Set timestamps for new entities
    ZonedDateTime now = ZonedDateTime.now();
    if (eventEmails.getId() == null) {
      eventEmails.setCreatedAt(now);
    }
    eventEmails.setUpdatedAt(now);

    eventEmails = eventEmailsRepository.save(eventEmails);
    return eventEmailsMapper.toDto(eventEmails);
  }

  @Override
  public EventEmailsDTO update(EventEmailsDTO eventEmailsDTO) {
    log.debug("Request to update EventEmails : {}", eventEmailsDTO);
    EventEmails eventEmails = eventEmailsMapper.toEntity(eventEmailsDTO);

    // Set updatedAt timestamp
    eventEmails.setUpdatedAt(ZonedDateTime.now());

    eventEmails = eventEmailsRepository.save(eventEmails);
    return eventEmailsMapper.toDto(eventEmails);
  }

  @Override
  public Optional<EventEmailsDTO> partialUpdate(EventEmailsDTO eventEmailsDTO) {
    log.debug("Request to partially update EventEmails : {}", eventEmailsDTO);

    return eventEmailsRepository
        .findById(eventEmailsDTO.getId())
        .map(existingEventEmails -> {
          eventEmailsMapper.partialUpdate(existingEventEmails, eventEmailsDTO);
          return existingEventEmails;
        })
        .map(eventEmailsRepository::save)
        .map(eventEmailsMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<EventEmailsDTO> findAll(Pageable pageable) {
    log.debug("Request to get all EventEmails");
    return eventEmailsRepository.findAll(pageable).map(eventEmailsMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<EventEmailsDTO> findOne(Long id) {
    log.debug("Request to get EventEmails : {}", id);
    return eventEmailsRepository.findById(id).map(eventEmailsMapper::toDto);
  }

  @Override
  public void delete(Long id) {
    log.debug("Request to delete EventEmails : {}", id);
    eventEmailsRepository.deleteById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventEmailsDTO> findByEventId(Long eventId) {
    log.debug("Request to get all EventEmails for event : {}", eventId);
    return eventEmailsRepository
        .findByEventId(eventId)
        .stream()
        .map(eventEmailsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<EventEmailsDTO> findByEventId(Long eventId, Pageable pageable) {
    log.debug("Request to get all EventEmails for event : {} with pagination", eventId);
    return eventEmailsRepository.findByEventId(eventId, pageable).map(eventEmailsMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventEmailsDTO> findByEmail(String email) {
    log.debug("Request to get all EventEmails by email : {}", email);
    return eventEmailsRepository
        .findByEmail(email)
        .stream()
        .map(eventEmailsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventEmailsDTO> findByEmailContaining(String email) {
    log.debug("Request to get all EventEmails by email containing : {}", email);
    return eventEmailsRepository
        .findByEmailContainingIgnoreCase(email)
        .stream()
        .map(eventEmailsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventEmailsDTO> findByEventIdAndEmail(Long eventId, String email) {
    log.debug("Request to get all EventEmails for event : {} and email : {}", eventId, email);
    return eventEmailsRepository
        .findByEventIdAndEmail(eventId, email)
        .stream()
        .map(eventEmailsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventEmailsDTO> findByEventIdAndEmailContaining(Long eventId, String email) {
    log.debug("Request to get all EventEmails for event : {} and email containing : {}", eventId, email);
    return eventEmailsRepository
        .findByEventIdAndEmailContainingIgnoreCase(eventId, email)
        .stream()
        .map(eventEmailsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public long countByEventId(Long eventId) {
    log.debug("Request to count EventEmails for event : {}", eventId);
    return eventEmailsRepository.countByEventId(eventId);
  }

  @Override
  @Transactional(readOnly = true)
  public long countByEmail(String email) {
    log.debug("Request to count EventEmails by email : {}", email);
    return eventEmailsRepository.countByEmail(email);
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventEmailsDTO> findByEventIdOrderByCreatedAt(Long eventId) {
    log.debug("Request to get all EventEmails for event : {} ordered by creation date", eventId);
    return eventEmailsRepository
        .findByEventIdOrderByCreatedAt(eventId)
        .stream()
        .map(eventEmailsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<String> findDistinctEmailsByEventId(Long eventId) {
    log.debug("Request to get distinct emails for event : {}", eventId);
    return eventEmailsRepository.findDistinctEmailsByEventId(eventId);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<EventEmailsDTO> findByEventIdAndEmailExact(Long eventId, String email) {
    log.debug("Request to get EventEmails for event : {} and email exact : {}", eventId, email);
    return eventEmailsRepository
        .findOneByEventIdAndEmail(eventId, email)
        .map(eventEmailsMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsByEventIdAndEmail(Long eventId, String email) {
    log.debug("Request to check if EventEmails exists for event : {} and email : {}", eventId, email);
    return eventEmailsRepository.existsByEventIdAndEmail(eventId, email);
  }
}
