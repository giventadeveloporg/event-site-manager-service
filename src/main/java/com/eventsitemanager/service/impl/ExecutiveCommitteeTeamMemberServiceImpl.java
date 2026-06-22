package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.ExecutiveCommitteeTeamMember;
import com.eventsitemanager.repository.ExecutiveCommitteeTeamMemberRepository;
import com.eventsitemanager.service.ExecutiveCommitteeTeamMemberService;
import com.eventsitemanager.service.dto.ExecutiveCommitteeTeamMemberDTO;
import com.eventsitemanager.service.mapper.ExecutiveCommitteeTeamMemberMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eventsitemanager.domain.ExecutiveCommitteeTeamMember}.
 */
@Service
@Transactional
public class ExecutiveCommitteeTeamMemberServiceImpl implements ExecutiveCommitteeTeamMemberService {

    private static final Logger LOG = LoggerFactory.getLogger(ExecutiveCommitteeTeamMemberServiceImpl.class);

    private final ExecutiveCommitteeTeamMemberRepository executiveCommitteeTeamMemberRepository;

    private final ExecutiveCommitteeTeamMemberMapper executiveCommitteeTeamMemberMapper;

    public ExecutiveCommitteeTeamMemberServiceImpl(
        ExecutiveCommitteeTeamMemberRepository executiveCommitteeTeamMemberRepository,
        ExecutiveCommitteeTeamMemberMapper executiveCommitteeTeamMemberMapper
    ) {
        this.executiveCommitteeTeamMemberRepository = executiveCommitteeTeamMemberRepository;
        this.executiveCommitteeTeamMemberMapper = executiveCommitteeTeamMemberMapper;
    }

    @Override
    public ExecutiveCommitteeTeamMemberDTO save(ExecutiveCommitteeTeamMemberDTO executiveCommitteeTeamMemberDTO) {
        LOG.debug("Request to save ExecutiveCommitteeTeamMember : {}", executiveCommitteeTeamMemberDTO);
        ExecutiveCommitteeTeamMember executiveCommitteeTeamMember = executiveCommitteeTeamMemberMapper.toEntity(
            executiveCommitteeTeamMemberDTO
        );
        executiveCommitteeTeamMember = executiveCommitteeTeamMemberRepository.save(executiveCommitteeTeamMember);
        return executiveCommitteeTeamMemberMapper.toDto(executiveCommitteeTeamMember);
    }

    @Override
    public ExecutiveCommitteeTeamMemberDTO update(ExecutiveCommitteeTeamMemberDTO executiveCommitteeTeamMemberDTO) {
        LOG.debug("Request to update ExecutiveCommitteeTeamMember : {}", executiveCommitteeTeamMemberDTO);
        ExecutiveCommitteeTeamMember executiveCommitteeTeamMember = executiveCommitteeTeamMemberMapper.toEntity(
            executiveCommitteeTeamMemberDTO
        );
        executiveCommitteeTeamMember = executiveCommitteeTeamMemberRepository.save(executiveCommitteeTeamMember);
        return executiveCommitteeTeamMemberMapper.toDto(executiveCommitteeTeamMember);
    }

    @Override
    public Optional<ExecutiveCommitteeTeamMemberDTO> partialUpdate(ExecutiveCommitteeTeamMemberDTO executiveCommitteeTeamMemberDTO) {
        LOG.debug("Request to partially update ExecutiveCommitteeTeamMember : {}", executiveCommitteeTeamMemberDTO);

        return executiveCommitteeTeamMemberRepository
            .findById(executiveCommitteeTeamMemberDTO.getId())
            .map(existingExecutiveCommitteeTeamMember -> {
                executiveCommitteeTeamMemberMapper.partialUpdate(existingExecutiveCommitteeTeamMember, executiveCommitteeTeamMemberDTO);

                return existingExecutiveCommitteeTeamMember;
            })
            .map(executiveCommitteeTeamMemberRepository::save)
            .map(executiveCommitteeTeamMemberMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExecutiveCommitteeTeamMemberDTO> findOne(Long id) {
        LOG.debug("Request to get ExecutiveCommitteeTeamMember : {}", id);
        return executiveCommitteeTeamMemberRepository.findById(id).map(executiveCommitteeTeamMemberMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ExecutiveCommitteeTeamMember : {}", id);
        executiveCommitteeTeamMemberRepository.deleteById(id);
    }
}
