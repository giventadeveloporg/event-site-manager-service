package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.EventCompetitionGroupMember;
import com.eventsitemanager.repository.EventCompetitionGroupMemberRepository;
import com.eventsitemanager.service.EventCompetitionGroupMemberService;
import com.eventsitemanager.service.dto.EventCompetitionGroupMemberDTO;
import com.eventsitemanager.service.mapper.EventCompetitionGroupMemberMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EventCompetitionGroupMemberServiceImpl implements EventCompetitionGroupMemberService {

    private final Logger log = LoggerFactory.getLogger(EventCompetitionGroupMemberServiceImpl.class);

    private final EventCompetitionGroupMemberRepository eventCompetitionGroupMemberRepository;

    private final EventCompetitionGroupMemberMapper eventCompetitionGroupMemberMapper;

    public EventCompetitionGroupMemberServiceImpl(
        EventCompetitionGroupMemberRepository eventCompetitionGroupMemberRepository,
        EventCompetitionGroupMemberMapper eventCompetitionGroupMemberMapper
    ) {
        this.eventCompetitionGroupMemberRepository = eventCompetitionGroupMemberRepository;
        this.eventCompetitionGroupMemberMapper = eventCompetitionGroupMemberMapper;
    }

    @Override
    public EventCompetitionGroupMemberDTO save(EventCompetitionGroupMemberDTO eventCompetitionGroupMemberDTO) {
        log.debug("Request to save EventCompetitionGroupMember : {}", eventCompetitionGroupMemberDTO);
        EventCompetitionGroupMember entity = eventCompetitionGroupMemberMapper.toEntity(eventCompetitionGroupMemberDTO);
        if (entity.getId() != null) {
            log.warn(
                "EventCompetitionGroupMember has ID {} set during create operation. Clearing ID to force sequence generation.",
                entity.getId()
            );
            entity.setId(null);
        }
        entity = eventCompetitionGroupMemberRepository.save(entity);
        return eventCompetitionGroupMemberMapper.toDto(entity);
    }

    @Override
    public EventCompetitionGroupMemberDTO update(EventCompetitionGroupMemberDTO eventCompetitionGroupMemberDTO) {
        log.debug("Request to update EventCompetitionGroupMember : {}", eventCompetitionGroupMemberDTO);
        EventCompetitionGroupMember entity = eventCompetitionGroupMemberMapper.toEntity(eventCompetitionGroupMemberDTO);
        entity = eventCompetitionGroupMemberRepository.save(entity);
        return eventCompetitionGroupMemberMapper.toDto(entity);
    }

    @Override
    public Optional<EventCompetitionGroupMemberDTO> partialUpdate(EventCompetitionGroupMemberDTO eventCompetitionGroupMemberDTO) {
        log.debug("Request to partially update EventCompetitionGroupMember : {}", eventCompetitionGroupMemberDTO);
        return eventCompetitionGroupMemberRepository
            .findById(eventCompetitionGroupMemberDTO.getId())
            .map(existing -> {
                eventCompetitionGroupMemberMapper.partialUpdate(existing, eventCompetitionGroupMemberDTO);
                return existing;
            })
            .map(eventCompetitionGroupMemberRepository::save)
            .map(eventCompetitionGroupMemberMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventCompetitionGroupMemberDTO> findAll(Pageable pageable) {
        return eventCompetitionGroupMemberRepository.findAll(pageable).map(eventCompetitionGroupMemberMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventCompetitionGroupMemberDTO> findOne(Long id) {
        return eventCompetitionGroupMemberRepository.findById(id).map(eventCompetitionGroupMemberMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        eventCompetitionGroupMemberRepository.deleteById(id);
    }
}
