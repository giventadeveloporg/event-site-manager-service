package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.FocusGroupMember;
import com.nextjstemplate.repository.FocusGroupMemberRepository;
import com.nextjstemplate.service.FocusGroupMemberService;
import com.nextjstemplate.service.dto.FocusGroupMemberDTO;
import com.nextjstemplate.service.mapper.FocusGroupMemberMapper;
import java.util.Locale;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing
 * {@link com.nextjstemplate.domain.FocusGroupMember}.
 */
@Service
@Transactional
public class FocusGroupMemberServiceImpl implements FocusGroupMemberService {

    private final Logger log = LoggerFactory.getLogger(FocusGroupMemberServiceImpl.class);

    private final FocusGroupMemberRepository focusGroupMemberRepository;

    private final FocusGroupMemberMapper focusGroupMemberMapper;

    public FocusGroupMemberServiceImpl(
        FocusGroupMemberRepository focusGroupMemberRepository,
        FocusGroupMemberMapper focusGroupMemberMapper
    ) {
        this.focusGroupMemberRepository = focusGroupMemberRepository;
        this.focusGroupMemberMapper = focusGroupMemberMapper;
    }

    @Override
    public FocusGroupMemberDTO save(FocusGroupMemberDTO focusGroupMemberDTO) {
        log.debug("Request to save FocusGroupMember : {}", focusGroupMemberDTO);

        // Normalize role and status to uppercase
        normalizeRoleAndStatus(focusGroupMemberDTO);

        FocusGroupMember focusGroupMember = focusGroupMemberMapper.toEntity(focusGroupMemberDTO);

        try {
            focusGroupMember = focusGroupMemberRepository.save(focusGroupMember);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to save FocusGroupMember due to constraint violation: {}", e.getMessage());
            throw new IllegalArgumentException(
                "This user is already a member of this focus group. FocusGroupId: " +
                focusGroupMemberDTO.getFocusGroupId() +
                ", UserProfileId: " +
                focusGroupMemberDTO.getUserProfileId()
            );
        }

        return focusGroupMemberMapper.toDto(focusGroupMember);
    }

    @Override
    public FocusGroupMemberDTO update(FocusGroupMemberDTO focusGroupMemberDTO) {
        log.debug("Request to update FocusGroupMember : {}", focusGroupMemberDTO);

        // Normalize role and status to uppercase
        normalizeRoleAndStatus(focusGroupMemberDTO);

        FocusGroupMember focusGroupMember = focusGroupMemberMapper.toEntity(focusGroupMemberDTO);

        try {
            focusGroupMember = focusGroupMemberRepository.save(focusGroupMember);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to update FocusGroupMember due to constraint violation: {}", e.getMessage());
            throw new IllegalArgumentException("This user is already a member of this focus group with a different membership record.");
        }

        return focusGroupMemberMapper.toDto(focusGroupMember);
    }

    @Override
    public Optional<FocusGroupMemberDTO> partialUpdate(FocusGroupMemberDTO focusGroupMemberDTO) {
        log.debug("Request to partially update FocusGroupMember : {}", focusGroupMemberDTO);

        // Normalize role and status to uppercase if they're being updated
        normalizeRoleAndStatus(focusGroupMemberDTO);

        return focusGroupMemberRepository
            .findById(focusGroupMemberDTO.getId())
            .map(existingFocusGroupMember -> {
                focusGroupMemberMapper.partialUpdate(existingFocusGroupMember, focusGroupMemberDTO);
                return existingFocusGroupMember;
            })
            .map(focusGroupMember -> {
                try {
                    return focusGroupMemberRepository.save(focusGroupMember);
                } catch (DataIntegrityViolationException e) {
                    log.error("Failed to update FocusGroupMember due to constraint violation: {}", e.getMessage());
                    throw new IllegalArgumentException("This user is already a member of this focus group.");
                }
            })
            .map(focusGroupMemberMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FocusGroupMemberDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FocusGroupMembers");
        return focusGroupMemberRepository.findAll(pageable).map(focusGroupMemberMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FocusGroupMemberDTO> findOne(Long id) {
        log.debug("Request to get FocusGroupMember : {}", id);
        return focusGroupMemberRepository.findById(id).map(focusGroupMemberMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FocusGroupMember : {}", id);
        focusGroupMemberRepository.deleteById(id);
    }

    /**
     * Normalizes role and status to uppercase
     *
     * @param dto the DTO to normalize
     */
    private void normalizeRoleAndStatus(FocusGroupMemberDTO dto) {
        if (dto.getRole() != null) {
            dto.setRole(dto.getRole().toUpperCase(Locale.ROOT));
        }
        if (dto.getStatus() != null) {
            dto.setStatus(dto.getStatus().toUpperCase(Locale.ROOT));
        }
    }
}
