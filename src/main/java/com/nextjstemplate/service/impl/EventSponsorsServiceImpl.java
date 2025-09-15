package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventSponsors;
import com.nextjstemplate.repository.EventSponsorsRepository;
import com.nextjstemplate.service.EventSponsorsService;
import com.nextjstemplate.service.dto.EventSponsorsDTO;
import com.nextjstemplate.service.mapper.EventSponsorsMapper;
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
 * Service Implementation for managing {@link EventSponsors}.
 */
@Service
@Transactional
public class EventSponsorsServiceImpl implements EventSponsorsService {

  private final Logger log = LoggerFactory.getLogger(EventSponsorsServiceImpl.class);

  private final EventSponsorsRepository eventSponsorsRepository;

  private final EventSponsorsMapper eventSponsorsMapper;

  public EventSponsorsServiceImpl(EventSponsorsRepository eventSponsorsRepository,
      EventSponsorsMapper eventSponsorsMapper) {
    this.eventSponsorsRepository = eventSponsorsRepository;
    this.eventSponsorsMapper = eventSponsorsMapper;
  }

  @Override
  public EventSponsorsDTO save(EventSponsorsDTO eventSponsorsDTO) {
    log.debug("Request to save EventSponsors : {}", eventSponsorsDTO);
    EventSponsors eventSponsors = eventSponsorsMapper.toEntity(eventSponsorsDTO);

    // Set timestamps for new entities
    ZonedDateTime now = ZonedDateTime.now();
    if (eventSponsors.getId() == null) {
      eventSponsors.setCreatedAt(now);
    }
    eventSponsors.setUpdatedAt(now);

    eventSponsors = eventSponsorsRepository.save(eventSponsors);
    return eventSponsorsMapper.toDto(eventSponsors);
  }

  @Override
  public EventSponsorsDTO update(EventSponsorsDTO eventSponsorsDTO) {
    log.debug("Request to update EventSponsors : {}", eventSponsorsDTO);
    EventSponsors eventSponsors = eventSponsorsMapper.toEntity(eventSponsorsDTO);

    // Set updatedAt timestamp
    eventSponsors.setUpdatedAt(ZonedDateTime.now());

    eventSponsors = eventSponsorsRepository.save(eventSponsors);
    return eventSponsorsMapper.toDto(eventSponsors);
  }

  @Override
  public Optional<EventSponsorsDTO> partialUpdate(EventSponsorsDTO eventSponsorsDTO) {
    log.debug("Request to partially update EventSponsors : {}", eventSponsorsDTO);

    return eventSponsorsRepository
        .findById(eventSponsorsDTO.getId())
        .map(existingEventSponsors -> {
          eventSponsorsMapper.partialUpdate(existingEventSponsors, eventSponsorsDTO);
          return existingEventSponsors;
        })
        .map(eventSponsorsRepository::save)
        .map(eventSponsorsMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<EventSponsorsDTO> findAll(Pageable pageable) {
    log.debug("Request to get all EventSponsors");
    return eventSponsorsRepository.findAll(pageable).map(eventSponsorsMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<EventSponsorsDTO> findOne(Long id) {
    log.debug("Request to get EventSponsors : {}", id);
    return eventSponsorsRepository.findById(id).map(eventSponsorsMapper::toDto);
  }

  @Override
  public void delete(Long id) {
    log.debug("Request to delete EventSponsors : {}", id);
    eventSponsorsRepository.deleteById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventSponsorsDTO> findByNameContaining(String name) {
    log.debug("Request to get all EventSponsors by name containing : {}", name);
    return eventSponsorsRepository
        .findByNameContainingIgnoreCase(name)
        .stream()
        .map(eventSponsorsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventSponsorsDTO> findByCompanyNameContaining(String companyName) {
    log.debug("Request to get all EventSponsors by company name containing : {}", companyName);
    return eventSponsorsRepository
        .findByCompanyNameContainingIgnoreCase(companyName)
        .stream()
        .map(eventSponsorsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventSponsorsDTO> findByType(String type) {
    log.debug("Request to get all EventSponsors by type : {}", type);
    return eventSponsorsRepository
        .findByType(type)
        .stream()
        .map(eventSponsorsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventSponsorsDTO> findByIsActive(Boolean isActive) {
    log.debug("Request to get all EventSponsors by active status : {}", isActive);
    return eventSponsorsRepository
        .findByIsActive(isActive)
        .stream()
        .map(eventSponsorsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventSponsorsDTO> findByTypeAndIsActive(String type, Boolean isActive) {
    log.debug("Request to get all EventSponsors by type : {} and active status : {}", type, isActive);
    return eventSponsorsRepository
        .findByTypeAndIsActive(type, isActive)
        .stream()
        .map(eventSponsorsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventSponsorsDTO> findByNameAndType(String name, String type) {
    log.debug("Request to get all EventSponsors by name : {} and type : {}", name, type);
    return eventSponsorsRepository
        .findByNameAndType(name, type)
        .stream()
        .map(eventSponsorsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventSponsorsDTO> findByContactEmail(String contactEmail) {
    log.debug("Request to get all EventSponsors by contact email : {}", contactEmail);
    return eventSponsorsRepository
        .findByContactEmail(contactEmail)
        .stream()
        .map(eventSponsorsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventSponsorsDTO> findByContactPhone(String contactPhone) {
    log.debug("Request to get all EventSponsors by contact phone : {}", contactPhone);
    return eventSponsorsRepository
        .findByContactPhone(contactPhone)
        .stream()
        .map(eventSponsorsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventSponsorsDTO> findAllOrderByPriorityRanking() {
    log.debug("Request to get all EventSponsors ordered by priority ranking");
    return eventSponsorsRepository
        .findAllOrderByPriorityRanking()
        .stream()
        .map(eventSponsorsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventSponsorsDTO> findByIsActiveOrderByPriorityRanking(Boolean isActive) {
    log.debug("Request to get all EventSponsors by active status : {} ordered by priority ranking", isActive);
    return eventSponsorsRepository
        .findByIsActiveOrderByPriorityRanking(isActive)
        .stream()
        .map(eventSponsorsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventSponsorsDTO> findByTypeOrderByPriorityRanking(String type) {
    log.debug("Request to get all EventSponsors by type : {} ordered by priority ranking", type);
    return eventSponsorsRepository
        .findByTypeOrderByPriorityRanking(type)
        .stream()
        .map(eventSponsorsMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public long countByType(String type) {
    log.debug("Request to count EventSponsors by type : {}", type);
    return eventSponsorsRepository.countByType(type);
  }

  @Override
  @Transactional(readOnly = true)
  public long countByIsActive(Boolean isActive) {
    log.debug("Request to count EventSponsors by active status : {}", isActive);
    return eventSponsorsRepository.countByIsActive(isActive);
  }

  @Override
  @Transactional(readOnly = true)
  public long countByTypeAndIsActive(String type, Boolean isActive) {
    log.debug("Request to count EventSponsors by type : {} and active status : {}", type, isActive);
    return eventSponsorsRepository.countByTypeAndIsActive(type, isActive);
  }
}
