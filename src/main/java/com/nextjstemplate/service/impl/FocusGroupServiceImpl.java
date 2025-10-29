package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.FocusGroup;
import com.nextjstemplate.repository.FocusGroupRepository;
import com.nextjstemplate.service.FocusGroupService;
import com.nextjstemplate.service.dto.FocusGroupDTO;
import com.nextjstemplate.service.mapper.FocusGroupMapper;
import java.util.Optional;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing
 * {@link com.nextjstemplate.domain.FocusGroup}.
 */
@Service
@Transactional
public class FocusGroupServiceImpl implements FocusGroupService {

    private final Logger log = LoggerFactory.getLogger(FocusGroupServiceImpl.class);

    private static final Pattern SLUG_PATTERN = Pattern.compile("^[a-z0-9-]+$");

    private final FocusGroupRepository focusGroupRepository;

    private final FocusGroupMapper focusGroupMapper;

    public FocusGroupServiceImpl(FocusGroupRepository focusGroupRepository, FocusGroupMapper focusGroupMapper) {
        this.focusGroupRepository = focusGroupRepository;
        this.focusGroupMapper = focusGroupMapper;
    }

    @Override
    public FocusGroupDTO save(FocusGroupDTO focusGroupDTO) {
        log.debug("Request to save FocusGroup : {}", focusGroupDTO);

        // Validate slug pattern
        validateSlug(focusGroupDTO.getSlug());

        // Set default isActive if null
        if (focusGroupDTO.getIsActive() == null) {
            focusGroupDTO.setIsActive(true);
        }

        FocusGroup focusGroup = focusGroupMapper.toEntity(focusGroupDTO);

        try {
            focusGroup = focusGroupRepository.save(focusGroup);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to save FocusGroup due to constraint violation: {}", e.getMessage());
            throw new IllegalArgumentException(
                "Focus group with this slug or name already exists for this tenant. Slug: " +
                focusGroupDTO.getSlug() +
                ", Name: " +
                focusGroupDTO.getName()
            );
        }

        return focusGroupMapper.toDto(focusGroup);
    }

    @Override
    public FocusGroupDTO update(FocusGroupDTO focusGroupDTO) {
        log.debug("Request to update FocusGroup : {}", focusGroupDTO);

        // Validate slug pattern
        validateSlug(focusGroupDTO.getSlug());

        FocusGroup focusGroup = focusGroupMapper.toEntity(focusGroupDTO);

        try {
            focusGroup = focusGroupRepository.save(focusGroup);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to update FocusGroup due to constraint violation: {}", e.getMessage());
            throw new IllegalArgumentException(
                "Focus group with this slug or name already exists for this tenant. Slug: " +
                focusGroupDTO.getSlug() +
                ", Name: " +
                focusGroupDTO.getName()
            );
        }

        return focusGroupMapper.toDto(focusGroup);
    }

    @Override
    public Optional<FocusGroupDTO> partialUpdate(FocusGroupDTO focusGroupDTO) {
        log.debug("Request to partially update FocusGroup : {}", focusGroupDTO);

        // Validate slug pattern if slug is being updated
        if (focusGroupDTO.getSlug() != null) {
            validateSlug(focusGroupDTO.getSlug());
        }

        return focusGroupRepository
            .findById(focusGroupDTO.getId())
            .map(existingFocusGroup -> {
                focusGroupMapper.partialUpdate(existingFocusGroup, focusGroupDTO);
                return existingFocusGroup;
            })
            .map(focusGroup -> {
                try {
                    return focusGroupRepository.save(focusGroup);
                } catch (DataIntegrityViolationException e) {
                    log.error("Failed to update FocusGroup due to constraint violation: {}", e.getMessage());
                    throw new IllegalArgumentException("Focus group with this slug or name already exists for this tenant.");
                }
            })
            .map(focusGroupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FocusGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FocusGroups");
        return focusGroupRepository.findAll(pageable).map(focusGroupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FocusGroupDTO> findOne(Long id) {
        log.debug("Request to get FocusGroup : {}", id);
        return focusGroupRepository.findById(id).map(focusGroupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FocusGroupDTO> findByTenantIdAndSlug(String tenantId, String slug) {
        log.debug("Request to get FocusGroup by tenantId {} and slug : {}", tenantId, slug);
        return focusGroupRepository.findByTenantIdAndSlug(tenantId, slug).map(focusGroupMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FocusGroup : {}", id);
        focusGroupRepository.deleteById(id);
    }

    /**
     * Validates that the slug matches the required pattern [a-z0-9-]+
     *
     * @param slug the slug to validate
     * @throws IllegalArgumentException if the slug is invalid
     */
    private void validateSlug(String slug) {
        if (slug == null || slug.trim().isEmpty()) {
            throw new IllegalArgumentException("Slug cannot be empty");
        }
        if (!SLUG_PATTERN.matcher(slug).matches()) {
            throw new IllegalArgumentException("Slug must contain only lowercase letters, numbers, and hyphens. Invalid slug: " + slug);
        }
    }
}
