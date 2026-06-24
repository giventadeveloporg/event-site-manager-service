package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.TeamMember;
import com.eventsitemanager.repository.TeamMemberRepository;
import com.eventsitemanager.service.TeamMemberService;
import com.eventsitemanager.service.dto.TeamMemberDTO;
import com.eventsitemanager.service.mapper.TeamMemberMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eventsitemanager.domain.TeamMember}.
 */
@Service
@Transactional
public class TeamMemberServiceImpl implements TeamMemberService {

    private static final Logger LOG = LoggerFactory.getLogger(TeamMemberServiceImpl.class);

    private final TeamMemberRepository teamMemberRepository;

    private final TeamMemberMapper teamMemberMapper;

    public TeamMemberServiceImpl(TeamMemberRepository teamMemberRepository, TeamMemberMapper teamMemberMapper) {
        this.teamMemberRepository = teamMemberRepository;
        this.teamMemberMapper = teamMemberMapper;
    }

    @Override
    public TeamMemberDTO save(TeamMemberDTO teamMemberDTO) {
        LOG.debug("Request to save TeamMember : {}", teamMemberDTO);
        TeamMember teamMember = teamMemberMapper.toEntity(teamMemberDTO);

        if (teamMember.getId() != null) {
            LOG.warn("TeamMember has ID {} set during create operation. Clearing ID to force sequence generation.", teamMember.getId());
            teamMember.setId(null);
        }

        teamMember = teamMemberRepository.save(teamMember);
        return teamMemberMapper.toDto(teamMember);
    }

    @Override
    public TeamMemberDTO update(TeamMemberDTO teamMemberDTO) {
        LOG.debug("Request to update TeamMember : {}", teamMemberDTO);
        TeamMember teamMember = teamMemberMapper.toEntity(teamMemberDTO);
        teamMember = teamMemberRepository.save(teamMember);
        return teamMemberMapper.toDto(teamMember);
    }

    @Override
    public Optional<TeamMemberDTO> partialUpdate(TeamMemberDTO teamMemberDTO) {
        LOG.debug("Request to partially update TeamMember : {}", teamMemberDTO);

        return teamMemberRepository
            .findById(teamMemberDTO.getId())
            .map(existingTeamMember -> {
                teamMemberMapper.partialUpdate(existingTeamMember, teamMemberDTO);
                return existingTeamMember;
            })
            .map(teamMemberRepository::save)
            .map(teamMemberMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TeamMemberDTO> findOne(Long id) {
        LOG.debug("Request to get TeamMember : {}", id);
        return teamMemberRepository.findById(id).map(teamMemberMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete TeamMember : {}", id);
        teamMemberRepository.deleteById(id);
    }
}
