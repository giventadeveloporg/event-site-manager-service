package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.GasStationUserStationAssignment;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.domain.enumeration.UserRoleType;
import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.GasStationLocationRepository;
import com.eventsitemanager.repository.GasStationUserStationAssignmentRepository;
import com.eventsitemanager.repository.UserProfileRepository;
import com.eventsitemanager.service.GasStationAccessService;
import com.eventsitemanager.service.GasStationUserStationAssignmentService;
import com.eventsitemanager.service.dto.GasStationUserStationAssignmentDTO;
import com.eventsitemanager.service.mapper.GasStationUserStationAssignmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GasStationUserStationAssignmentServiceImpl implements GasStationUserStationAssignmentService {

    private static final Logger LOG = LoggerFactory.getLogger(GasStationUserStationAssignmentServiceImpl.class);
    private static final String ENTITY_NAME = "gasStationUserStationAssignment";

    private final GasStationUserStationAssignmentRepository assignmentRepository;
    private final GasStationUserStationAssignmentMapper assignmentMapper;
    private final GasStationAccessService gasStationAccessService;
    private final UserProfileRepository userProfileRepository;
    private final GasStationLocationRepository gasStationLocationRepository;

    public GasStationUserStationAssignmentServiceImpl(
        GasStationUserStationAssignmentRepository assignmentRepository,
        GasStationUserStationAssignmentMapper assignmentMapper,
        GasStationAccessService gasStationAccessService,
        UserProfileRepository userProfileRepository,
        GasStationLocationRepository gasStationLocationRepository
    ) {
        this.assignmentRepository = assignmentRepository;
        this.assignmentMapper = assignmentMapper;
        this.gasStationAccessService = gasStationAccessService;
        this.userProfileRepository = userProfileRepository;
        this.gasStationLocationRepository = gasStationLocationRepository;
    }

    @Override
    public GasStationUserStationAssignmentDTO save(GasStationUserStationAssignmentDTO dto) {
        LOG.debug("Request to save GasStationUserStationAssignment : {}", dto);
        gasStationAccessService.assertAllLocationsScope();
        validateAssignmentPayload(dto, null);

        GasStationUserStationAssignment entity = assignmentMapper.toEntity(dto);
        if (entity.getId() != null) {
            entity.setId(null);
        }
        entity = assignmentRepository.save(entity);
        return assignmentMapper.toDto(entity);
    }

    @Override
    public GasStationUserStationAssignmentDTO update(GasStationUserStationAssignmentDTO dto) {
        LOG.debug("Request to update GasStationUserStationAssignment : {}", dto);
        gasStationAccessService.assertAllLocationsScope();
        validateAssignmentPayload(dto, dto.getId());

        GasStationUserStationAssignment entity = assignmentMapper.toEntity(dto);
        entity = assignmentRepository.save(entity);
        return assignmentMapper.toDto(entity);
    }

    @Override
    public Optional<GasStationUserStationAssignmentDTO> partialUpdate(GasStationUserStationAssignmentDTO dto) {
        LOG.debug("Request to partially update GasStationUserStationAssignment : {}", dto);
        gasStationAccessService.assertAllLocationsScope();

        return assignmentRepository
            .findById(dto.getId())
            .map(existing -> {
                assignmentMapper.partialUpdate(existing, dto);
                validateAssignmentPayload(assignmentMapper.toDto(existing), existing.getId());
                return existing;
            })
            .map(assignmentRepository::save)
            .map(assignmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GasStationUserStationAssignmentDTO> findOne(Long id) {
        LOG.debug("Request to get GasStationUserStationAssignment : {}", id);
        gasStationAccessService.assertGasModuleAccess();
        return assignmentRepository
            .findById(id)
            .map(assignmentMapper::toDto)
            .map(dto -> {
                gasStationAccessService.assertStationIdAllowed(dto.getStationId());
                Long currentProfileId = gasStationAccessService.getCurrentUserProfileIdOrNull();
                if (currentProfileId != null && gasStationAccessService.getAllowedStationIdsOrNull() != null) {
                    if (!currentProfileId.equals(dto.getUserProfileId())) {
                        throw new org.springframework.security.access.AccessDeniedException(
                            "Managers may only read their own station assignments."
                        );
                    }
                }
                return dto;
            });
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete GasStationUserStationAssignment : {}", id);
        gasStationAccessService.assertAllLocationsScope();
        assignmentRepository.deleteById(id);
    }

    private void validateAssignmentPayload(GasStationUserStationAssignmentDTO dto, Long existingId) {
        if (dto.getTenantId() == null || dto.getTenantId().isBlank()) {
            throw new BadRequestAlertException("tenantId is required", ENTITY_NAME, "tenantidrequired");
        }
        if (dto.getUserProfileId() == null || dto.getStationId() == null) {
            throw new BadRequestAlertException("userProfileId and stationId are required", ENTITY_NAME, "fieldsrequired");
        }

        UserProfile targetUser = userProfileRepository
            .findById(dto.getUserProfileId())
            .orElseThrow(() -> new BadRequestAlertException("User profile not found", ENTITY_NAME, "usernotfound"));
        if (!dto.getTenantId().equals(targetUser.getTenantId())) {
            throw new BadRequestAlertException("user_profile_id must belong to tenant", ENTITY_NAME, "tenantmismatch");
        }
        if (!UserRoleType.GAS_STATION_MANAGER.name().equalsIgnoreCase(String.valueOf(targetUser.getUserRole()))) {
            throw new BadRequestAlertException(
                "Assignments are only allowed for GAS_STATION_MANAGER users",
                ENTITY_NAME,
                "invalidtargetrole"
            );
        }

        gasStationLocationRepository
            .findById(dto.getStationId())
            .filter(loc -> dto.getTenantId().equals(loc.getTenantId()))
            .orElseThrow(() -> new BadRequestAlertException("station_id must belong to tenant", ENTITY_NAME, "stationnotfound"));

        if (
            existingId == null &&
            assignmentRepository.existsByTenantIdAndUserProfileIdAndStationId(dto.getTenantId(), dto.getUserProfileId(), dto.getStationId())
        ) {
            throw new BadRequestAlertException("Duplicate assignment", ENTITY_NAME, "duplicateassignment");
        }
    }
}
