package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventCompetition;
import com.nextjstemplate.domain.EventCompetitionParticipant;
import com.nextjstemplate.domain.EventCompetitionRegistration;
import com.nextjstemplate.domain.EventCompetitionSettings;
import com.nextjstemplate.domain.enumeration.CompetitionAudienceMode;
import com.nextjstemplate.domain.enumeration.CompetitionEligibleAudience;
import com.nextjstemplate.domain.enumeration.CompetitionGroupMemberRole;
import com.nextjstemplate.domain.enumeration.CompetitionParticipantType;
import com.nextjstemplate.domain.enumeration.CompetitionRegistrationStatus;
import com.nextjstemplate.domain.enumeration.CompetitionType;
import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.errors.ConflictException;
import com.nextjstemplate.repository.EventCompetitionParticipantRepository;
import com.nextjstemplate.repository.EventCompetitionRegistrationRepository;
import com.nextjstemplate.repository.EventCompetitionRepository;
import com.nextjstemplate.repository.EventCompetitionSettingsRepository;
import com.nextjstemplate.service.EventCompetitionGroupMemberService;
import com.nextjstemplate.service.EventCompetitionRegistrationService;
import com.nextjstemplate.service.dto.EventCompetitionGroupMemberDTO;
import com.nextjstemplate.service.dto.EventCompetitionParticipantDTO;
import com.nextjstemplate.service.dto.EventCompetitionRegistrationDTO;
import com.nextjstemplate.service.dto.TeamRegistrationRequestDTO;
import com.nextjstemplate.service.mapper.EventCompetitionRegistrationMapper;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EventCompetitionRegistrationServiceImpl implements EventCompetitionRegistrationService {

    private static final String ENTITY_NAME = "eventCompetitionRegistration";

    private final Logger log = LoggerFactory.getLogger(EventCompetitionRegistrationServiceImpl.class);

    private final EventCompetitionRegistrationRepository eventCompetitionRegistrationRepository;

    private final EventCompetitionRegistrationMapper eventCompetitionRegistrationMapper;

    private final EventCompetitionSettingsRepository eventCompetitionSettingsRepository;

    private final EventCompetitionRepository eventCompetitionRepository;

    private final EventCompetitionParticipantRepository eventCompetitionParticipantRepository;

    private final EventCompetitionGroupMemberService eventCompetitionGroupMemberService;

    public EventCompetitionRegistrationServiceImpl(
        EventCompetitionRegistrationRepository eventCompetitionRegistrationRepository,
        EventCompetitionRegistrationMapper eventCompetitionRegistrationMapper,
        EventCompetitionSettingsRepository eventCompetitionSettingsRepository,
        EventCompetitionRepository eventCompetitionRepository,
        EventCompetitionParticipantRepository eventCompetitionParticipantRepository,
        EventCompetitionGroupMemberService eventCompetitionGroupMemberService
    ) {
        this.eventCompetitionRegistrationRepository = eventCompetitionRegistrationRepository;
        this.eventCompetitionRegistrationMapper = eventCompetitionRegistrationMapper;
        this.eventCompetitionSettingsRepository = eventCompetitionSettingsRepository;
        this.eventCompetitionRepository = eventCompetitionRepository;
        this.eventCompetitionParticipantRepository = eventCompetitionParticipantRepository;
        this.eventCompetitionGroupMemberService = eventCompetitionGroupMemberService;
    }

    @Override
    public EventCompetitionRegistrationDTO save(EventCompetitionRegistrationDTO eventCompetitionRegistrationDTO) {
        log.debug("Request to save EventCompetitionRegistration : {}", eventCompetitionRegistrationDTO);
        EventCompetitionRegistration entity = eventCompetitionRegistrationMapper.toEntity(eventCompetitionRegistrationDTO);
        if (entity.getId() != null) {
            log.warn("EventCompetitionRegistration has ID {} set during create. Clearing ID.", entity.getId());
            entity.setId(null);
        }
        if (entity.getConfirmationEmailSent() == null) {
            entity.setConfirmationEmailSent(false);
        }
        validateRegistration(entity, null);
        try {
            entity = eventCompetitionRegistrationRepository.save(entity);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Registration already exists for this participant", ENTITY_NAME, "duplicate");
        }
        return eventCompetitionRegistrationMapper.toDto(entity);
    }

    @Override
    public EventCompetitionRegistrationDTO update(EventCompetitionRegistrationDTO eventCompetitionRegistrationDTO) {
        log.debug("Request to update EventCompetitionRegistration : {}", eventCompetitionRegistrationDTO);
        EventCompetitionRegistration entity = eventCompetitionRegistrationMapper.toEntity(eventCompetitionRegistrationDTO);
        validateRegistration(entity, entity.getId());
        entity = eventCompetitionRegistrationRepository.save(entity);
        return eventCompetitionRegistrationMapper.toDto(entity);
    }

    @Override
    public Optional<EventCompetitionRegistrationDTO> partialUpdate(EventCompetitionRegistrationDTO eventCompetitionRegistrationDTO) {
        log.debug("Request to partially update EventCompetitionRegistration : {}", eventCompetitionRegistrationDTO);
        return eventCompetitionRegistrationRepository
            .findById(eventCompetitionRegistrationDTO.getId())
            .map(existing -> {
                eventCompetitionRegistrationMapper.partialUpdate(existing, eventCompetitionRegistrationDTO);
                validateRegistration(existing, existing.getId());
                return existing;
            })
            .map(eventCompetitionRegistrationRepository::save)
            .map(eventCompetitionRegistrationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventCompetitionRegistrationDTO> findAll(Pageable pageable) {
        return eventCompetitionRegistrationRepository.findAll(pageable).map(eventCompetitionRegistrationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventCompetitionRegistrationDTO> findOne(Long id) {
        return eventCompetitionRegistrationRepository.findById(id).map(eventCompetitionRegistrationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        eventCompetitionRegistrationRepository.deleteById(id);
    }

    @Override
    public EventCompetitionRegistrationDTO saveTeamRegistration(TeamRegistrationRequestDTO request) {
        if (request == null || request.getLeaderRegistration() == null) {
            throw new BadRequestAlertException("Leader registration is required", ENTITY_NAME, "leaderrequired");
        }

        EventCompetitionRegistrationDTO leaderDto = request.getLeaderRegistration();
        if (request.getTeamName() != null) {
            leaderDto.setTeamName(request.getTeamName());
        }
        if (request.getTeamDisplayName() != null) {
            leaderDto.setTeamDisplayName(request.getTeamDisplayName());
        }

        List<Long> memberIds = request.getMemberParticipantIds() != null ? request.getMemberParticipantIds() : List.of();
        EventCompetition competition = resolveCompetition(leaderDto);
        if (competition.getCompetitionType() == CompetitionType.GROUP && competition.getMinGroupSize() != null) {
            int totalSize = 1 + memberIds.size();
            if (totalSize < competition.getMinGroupSize()) {
                throw new BadRequestAlertException("Group size below minimum", ENTITY_NAME, "groupsizebelowmin");
            }
        }

        if (Boolean.TRUE.equals(competition.getRequiresTeamName()) && StringUtils.isBlank(leaderDto.getTeamName())) {
            throw new BadRequestAlertException("Team name is required", ENTITY_NAME, "teamnamerequired");
        }

        EventCompetitionRegistrationDTO savedLeader = save(leaderDto);

        for (Long memberParticipantId : memberIds) {
            EventCompetitionRegistrationDTO memberDto = buildMemberRegistration(leaderDto, savedLeader, memberParticipantId);
            save(memberDto);
        }

        ZonedDateTime now = ZonedDateTime.now();
        if (request.getGroupMembers() != null && !request.getGroupMembers().isEmpty()) {
            for (EventCompetitionGroupMemberDTO groupMember : request.getGroupMembers()) {
                groupMember.setRegistration(savedLeader);
                if (groupMember.getCreatedAt() == null) {
                    groupMember.setCreatedAt(now);
                }
                if (groupMember.getMemberRole() == null) {
                    groupMember.setMemberRole(CompetitionGroupMemberRole.MEMBER);
                }
                if (groupMember.getSortOrder() == null) {
                    groupMember.setSortOrder(0);
                }
                eventCompetitionGroupMemberService.save(groupMember);
            }
        } else {
            int sortOrder = 0;
            EventCompetitionParticipantDTO leaderParticipant = leaderDto.getParticipantProfile();
            if (leaderParticipant != null && leaderParticipant.getId() != null) {
                EventCompetitionGroupMemberDTO captain = new EventCompetitionGroupMemberDTO();
                captain.setTenantId(leaderDto.getTenantId());
                captain.setRegistration(savedLeader);
                captain.setParticipantProfile(leaderParticipant);
                captain.setMemberRole(CompetitionGroupMemberRole.CAPTAIN);
                captain.setSortOrder(sortOrder++);
                captain.setCreatedAt(now);
                eventCompetitionGroupMemberService.save(captain);
            }
            for (Long memberParticipantId : memberIds) {
                EventCompetitionGroupMemberDTO member = new EventCompetitionGroupMemberDTO();
                member.setTenantId(leaderDto.getTenantId());
                member.setRegistration(savedLeader);
                EventCompetitionParticipantDTO participantDto = new EventCompetitionParticipantDTO();
                participantDto.setId(memberParticipantId);
                member.setParticipantProfile(participantDto);
                member.setMemberRole(CompetitionGroupMemberRole.MEMBER);
                member.setSortOrder(sortOrder++);
                member.setCreatedAt(now);
                eventCompetitionGroupMemberService.save(member);
            }
        }

        return savedLeader;
    }

    @Override
    public List<EventCompetitionRegistrationDTO> saveBulkRegistrations(List<EventCompetitionRegistrationDTO> registrations) {
        if (registrations == null || registrations.isEmpty()) {
            throw new BadRequestAlertException("At least one registration is required", ENTITY_NAME, "bulkempty");
        }
        List<EventCompetitionRegistrationDTO> saved = new ArrayList<>();
        for (EventCompetitionRegistrationDTO dto : registrations) {
            saved.add(save(dto));
        }
        return saved;
    }

    private EventCompetitionRegistrationDTO buildMemberRegistration(
        EventCompetitionRegistrationDTO leaderDto,
        EventCompetitionRegistrationDTO savedLeader,
        Long memberParticipantId
    ) {
        EventCompetitionRegistrationDTO memberDto = new EventCompetitionRegistrationDTO();
        memberDto.setTenantId(leaderDto.getTenantId());
        memberDto.setEvent(leaderDto.getEvent());
        memberDto.setCompetition(leaderDto.getCompetition());
        memberDto.setRegistrationStatus(leaderDto.getRegistrationStatus());
        memberDto.setFeeAmount(leaderDto.getFeeAmount());
        memberDto.setEffectiveCategory(leaderDto.getEffectiveCategory());
        memberDto.setRegisteredByUserProfile(leaderDto.getRegisteredByUserProfile());
        memberDto.setTeamName(leaderDto.getTeamName());
        memberDto.setTeamDisplayName(leaderDto.getTeamDisplayName());
        memberDto.setCreatedAt(leaderDto.getCreatedAt() != null ? leaderDto.getCreatedAt() : ZonedDateTime.now());
        memberDto.setUpdatedAt(leaderDto.getUpdatedAt() != null ? leaderDto.getUpdatedAt() : ZonedDateTime.now());

        EventCompetitionRegistrationDTO leaderRef = new EventCompetitionRegistrationDTO();
        leaderRef.setId(savedLeader.getId());
        memberDto.setGroupLeaderRegistration(leaderRef);

        EventCompetitionParticipantDTO participantDto = new EventCompetitionParticipantDTO();
        participantDto.setId(memberParticipantId);
        memberDto.setParticipantProfile(participantDto);
        return memberDto;
    }

    private EventCompetition resolveCompetition(EventCompetitionRegistrationDTO dto) {
        if (dto.getCompetition() == null || dto.getCompetition().getId() == null) {
            throw new BadRequestAlertException("Competition is required", ENTITY_NAME, "competitionrequired");
        }
        return eventCompetitionRepository
            .findById(dto.getCompetition().getId())
            .orElseThrow(() -> new BadRequestAlertException("Competition not found", ENTITY_NAME, "competitionnotfound"));
    }

    private void validateRegistration(EventCompetitionRegistration registration, Long excludeId) {
        EventCompetition competition = registration.getCompetition();
        if (competition == null || competition.getId() == null) {
            throw new BadRequestAlertException("Competition is required", ENTITY_NAME, "competitionrequired");
        }
        competition =
            eventCompetitionRepository
                .findById(competition.getId())
                .orElseThrow(() -> new BadRequestAlertException("Competition not found", ENTITY_NAME, "competitionnotfound"));

        if (competition.getEvent() == null || competition.getEvent().getId() == null) {
            throw new BadRequestAlertException("Event is required", ENTITY_NAME, "eventrequired");
        }

        EventCompetitionSettings settings = eventCompetitionSettingsRepository
            .findOneByEventId(competition.getEvent().getId())
            .orElseThrow(() -> new BadRequestAlertException("Competition settings not found", ENTITY_NAME, "settingsnotfound"));

        ZonedDateTime now = ZonedDateTime.now();
        if (competition.getRegistrationDeadline() != null && competition.getRegistrationDeadline().isBefore(now)) {
            throw new BadRequestAlertException("Registration closed for this competition", ENTITY_NAME, "competitiondeadlinepassed");
        }
        if (
            (settings.getRegistrationDeadline() != null && settings.getRegistrationDeadline().isBefore(now)) ||
            Boolean.FALSE.equals(settings.getRegistrationOpen())
        ) {
            throw new BadRequestAlertException("registration closed", ENTITY_NAME, "registrationclosed");
        }

        if (competition.getMaxParticipants() != null) {
            long activeCount = eventCompetitionRegistrationRepository.countByCompetitionIdAndRegistrationStatusNotIn(
                competition.getId(),
                Arrays.asList(CompetitionRegistrationStatus.CANCELLED, CompetitionRegistrationStatus.REFUNDED)
            );
            if (activeCount >= competition.getMaxParticipants()) {
                throw new BadRequestAlertException("Competition is at capacity", ENTITY_NAME, "capacityexceeded");
            }
        }

        EventCompetitionParticipant participantRef = registration.getParticipantProfile();
        if (participantRef == null || participantRef.getId() == null) {
            throw new BadRequestAlertException("Participant is required", ENTITY_NAME, "participantrequired");
        }

        EventCompetitionParticipant participant = eventCompetitionParticipantRepository
            .findById(participantRef.getId())
            .orElseThrow(() -> new BadRequestAlertException("Participant not found", ENTITY_NAME, "participantnotfound"));

        Long participantId = participant.getId();
        if (
            eventCompetitionRegistrationRepository.existsByCompetitionIdAndParticipantProfileIdAndIdNot(
                competition.getId(),
                participantId,
                excludeId == null ? -1L : excludeId
            )
        ) {
            throw new ConflictException("Registration already exists for this participant", ENTITY_NAME, "duplicate");
        }

        CompetitionEligibleAudience eligible = competition.getEligibleAudience();
        CompetitionParticipantType participantType = participant.getParticipantType();
        if (eligible == CompetitionEligibleAudience.YOUTH_ONLY && participantType != CompetitionParticipantType.CHILD) {
            throw new BadRequestAlertException("Only child participants are eligible", ENTITY_NAME, "ineligibleparticipant");
        }
        if (eligible == CompetitionEligibleAudience.ADULT_ONLY && participantType != CompetitionParticipantType.ADULT) {
            throw new BadRequestAlertException("Only adult participants are eligible", ENTITY_NAME, "ineligibleparticipant");
        }

        if (settings.getAudienceMode() == CompetitionAudienceMode.YOUTH && participantType == CompetitionParticipantType.CHILD) {
            if (participant.getGuardianUserProfile() == null || participant.getGuardianUserProfile().getId() == null) {
                throw new BadRequestAlertException("Guardian is required for youth participants", ENTITY_NAME, "guardianrequired");
            }
        }
        if (settings.getAudienceMode() == CompetitionAudienceMode.ADULT && participantType != CompetitionParticipantType.ADULT) {
            throw new BadRequestAlertException("Only adult participants are allowed", ENTITY_NAME, "ineligibleparticipant");
        }

        if (participant.getDateOfBirth() != null) {
            int age = Period.between(participant.getDateOfBirth(), LocalDate.now()).getYears();
            if (competition.getMinAge() != null && age < competition.getMinAge()) {
                throw new BadRequestAlertException("Participant age below minimum", ENTITY_NAME, "agebelowmin");
            }
            if (competition.getMaxAge() != null && age > competition.getMaxAge()) {
                throw new BadRequestAlertException("Participant age above maximum", ENTITY_NAME, "ageabovemax");
            }
        }

        if (participant.getCurrentGrade() != null) {
            if (competition.getMinGrade() != null && participant.getCurrentGrade() < competition.getMinGrade()) {
                throw new BadRequestAlertException("Participant grade below minimum", ENTITY_NAME, "gradebelowmin");
            }
            if (competition.getMaxGrade() != null && participant.getCurrentGrade() > competition.getMaxGrade()) {
                throw new BadRequestAlertException("Participant grade above maximum", ENTITY_NAME, "gradeabovemax");
            }
        }

        if (competition.getCompetitionType() == CompetitionType.GROUP) {
            if (registration.getGroupLeaderRegistration() != null && registration.getGroupLeaderRegistration().getId() != null) {
                long groupSize =
                    eventCompetitionRegistrationRepository.countByGroupLeaderRegistrationId(
                        registration.getGroupLeaderRegistration().getId()
                    ) +
                    1;
                if (competition.getMaxGroupSize() != null && groupSize > competition.getMaxGroupSize()) {
                    throw new BadRequestAlertException("Group size exceeds maximum", ENTITY_NAME, "groupsizeexceeded");
                }
            } else if (registration.getId() != null && competition.getMinGroupSize() != null) {
                long groupSize = eventCompetitionRegistrationRepository.countByGroupLeaderRegistrationId(registration.getId()) + 1;
                if (groupSize < competition.getMinGroupSize()) {
                    throw new BadRequestAlertException("Group size below minimum", ENTITY_NAME, "groupsizebelowmin");
                }
            }
        }

        if (
            Boolean.TRUE.equals(competition.getRequiresTeamName()) &&
            registration.getGroupLeaderRegistration() == null &&
            StringUtils.isBlank(registration.getTeamName())
        ) {
            throw new BadRequestAlertException("Team name is required", ENTITY_NAME, "teamnamerequired");
        }
    }
}
