package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventContacts;
import com.nextjstemplate.repository.EventContactsRepository;
import com.nextjstemplate.service.EventContactsService;
import com.nextjstemplate.service.dto.EventContactsDTO;
import com.nextjstemplate.service.mapper.EventContactsMapper;
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
 * Service Implementation for managing {@link EventContacts}.
 */
@Service
@Transactional
public class EventContactsServiceImpl implements EventContactsService {

  private final Logger log = LoggerFactory.getLogger(EventContactsServiceImpl.class);

  private final EventContactsRepository eventContactsRepository;

  private final EventContactsMapper eventContactsMapper;

  public EventContactsServiceImpl(EventContactsRepository eventContactsRepository,
      EventContactsMapper eventContactsMapper) {
    this.eventContactsRepository = eventContactsRepository;
    this.eventContactsMapper = eventContactsMapper;
  }

  @Override
  public EventContactsDTO save(EventContactsDTO eventContactsDTO) {
    log.debug("Request to save EventContacts : {}", eventContactsDTO);
    EventContacts eventContacts = eventContactsMapper.toEntity(eventContactsDTO);

    // Set timestamps for new entities
    ZonedDateTime now = ZonedDateTime.now();
    if (eventContacts.getId() == null) {
      eventContacts.setCreatedAt(now);
    }
    eventContacts.setUpdatedAt(now);

    eventContacts = eventContactsRepository.save(eventContacts);
    return eventContactsMapper.toDto(eventContacts);
  }

  @Override
  public EventContactsDTO update(EventContactsDTO eventContactsDTO) {
    log.debug("Request to update EventContacts : {}", eventContactsDTO);
    EventContacts eventContacts = eventContactsMapper.toEntity(eventContactsDTO);

    // Set updatedAt timestamp
    eventContacts.setUpdatedAt(ZonedDateTime.now());

    eventContacts = eventContactsRepository.save(eventContacts);
    return eventContactsMapper.toDto(eventContacts);
  }

  @Override
  public Optional<EventContactsDTO> partialUpdate(EventContactsDTO eventContactsDTO) {
    log.debug("Request to partially update EventContacts : {}", eventContactsDTO);

    return eventContactsRepository
        .findById(eventContactsDTO.getId())
        .map(existingEventContacts -> {
          eventContactsMapper.partialUpdate(existingEventContacts, eventContactsDTO);
          // Set updatedAt timestamp
          existingEventContacts.setUpdatedAt(ZonedDateTime.now());
          return existingEventContacts;
        })
        .map(eventContactsRepository::save)
        .map(eventContactsMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<EventContactsDTO> findAll(Pageable pageable) {
    log.debug("Request to get all EventContacts");
    return eventContactsRepository.findAll(pageable).map(eventContactsMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<EventContactsDTO> findOne(Long id) {
    log.debug("Request to get EventContacts : {}", id);
    return eventContactsRepository.findById(id).map(eventContactsMapper::toDto);
  }

  @Override
  public void delete(Long id) {
    log.debug("Request to delete EventContacts : {}", id);
    eventContactsRepository.deleteById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventContactsDTO> findByEventId(Long eventId) {
    log.debug("Request to get all EventContacts for event : {}", eventId);
    return eventContactsRepository
        .findByEventId(eventId)
        .stream()
        .map(eventContactsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<EventContactsDTO> findByEventId(Long eventId, Pageable pageable) {
    log.debug("Request to get all EventContacts for event : {} with pagination", eventId);
    return eventContactsRepository.findByEventId(eventId, pageable).map(eventContactsMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventContactsDTO> findByNameContaining(String name) {
    log.debug("Request to get all EventContacts by name containing : {}", name);
    return eventContactsRepository
        .findByNameContainingIgnoreCase(name)
        .stream()
        .map(eventContactsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventContactsDTO> findByPhone(String phone) {
    log.debug("Request to get all EventContacts by phone : {}", phone);
    return eventContactsRepository
        .findByPhone(phone)
        .stream()
        .map(eventContactsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventContactsDTO> findByEmail(String email) {
    log.debug("Request to get all EventContacts by email : {}", email);
    return eventContactsRepository
        .findByEmail(email)
        .stream()
        .map(eventContactsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventContactsDTO> findByEventIdAndName(Long eventId, String name) {
    log.debug("Request to get all EventContacts for event : {} and name : {}", eventId, name);
    return eventContactsRepository
        .findByEventIdAndName(eventId, name)
        .stream()
        .map(eventContactsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventContactsDTO> findByEventIdAndPhone(Long eventId, String phone) {
    log.debug("Request to get all EventContacts for event : {} and phone : {}", eventId, phone);
    return eventContactsRepository
        .findByEventIdAndPhone(eventId, phone)
        .stream()
        .map(eventContactsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventContactsDTO> findByEventIdAndEmail(Long eventId, String email) {
    log.debug("Request to get all EventContacts for event : {} and email : {}", eventId, email);
    return eventContactsRepository
        .findByEventIdAndEmail(eventId, email)
        .stream()
        .map(eventContactsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public long countByEventId(Long eventId) {
    log.debug("Request to count EventContacts for event : {}", eventId);
    return eventContactsRepository.countByEventId(eventId);
  }
}
