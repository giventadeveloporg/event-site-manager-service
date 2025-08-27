package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.ExecutiveCommitteeTeamMember;
import com.nextjstemplate.repository.ExecutiveCommitteeTeamMemberRepository;
import com.nextjstemplate.service.ExecutiveCommitteeTeamMemberService;
import com.nextjstemplate.service.dto.ExecutiveCommitteeTeamMemberDTO;
import com.nextjstemplate.service.mapper.ExecutiveCommitteeTeamMemberMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.ExecutiveCommitteeTeamMember}.
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
