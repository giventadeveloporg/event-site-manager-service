package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventCompetition;
import com.nextjstemplate.domain.EventCompetitionParticipant;
import com.nextjstemplate.domain.EventCompetitionRegistration;
import com.nextjstemplate.domain.EventCompetitionSettings;
import com.nextjstemplate.domain.enumeration.CompetitionAudienceMode;
import com.nextjstemplate.domain.enumeration.CompetitionEligibleAudience;
import com.nextjstemplate.domain.enumeration.CompetitionParticipantType;
import com.nextjstemplate.domain.enumeration.CompetitionRegistrationStatus;
import com.nextjstemplate.domain.enumeration.CompetitionType;
import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.errors.ConflictException;
import com.nextjstemplate.repository.EventCompetitionParticipantRepository;
import com.nextjstemplate.repository.EventCompetitionRegistrationRepository;
import com.nextjstemplate.repository.EventCompetitionRepository;
import com.nextjstemplate.repository.EventCompetitionSettingsRepository;
import com.nextjstemplate.service.EventCompetitionRegistrationService;
import com.nextjstemplate.service.dto.EventCompetitionRegistrationDTO;
import com.nextjstemplate.service.mapper.EventCompetitionRegistrationMapper;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Optional;
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

    public EventCompetitionRegistrationServiceImpl(
        EventCompetitionRegistrationRepository eventCompetitionRegistrationRepository,
        EventCompetitionRegistrationMapper eventCompetitionRegistrationMapper,
        EventCompetitionSettingsRepository eventCompetitionSettingsRepository,
        EventCompetitionRepository eventCompetitionRepository,
        EventCompetitionParticipantRepository eventCompetitionParticipantRepository
    ) {
        this.eventCompetitionRegistrationRepository = eventCompetitionRegistrationRepository;
        this.eventCompetitionRegistrationMapper = eventCompetitionRegistrationMapper;
        this.eventCompetitionSettingsRepository = eventCompetitionSettingsRepository;
        this.eventCompetitionRepository = eventCompetitionRepository;
        this.eventCompetitionParticipantRepository = eventCompetitionParticipantRepository;
    }

    @Override
    public EventCompetitionRegistrationDTO save(EventCompetitionRegistrationDTO eventCompetitionRegistrationDTO) {
        log.debug("Request to save EventCompetitionRegistration : {}", eventCompetitionRegistrationDTO);
        EventCompetitionRegistration entity = eventCompetitionRegistrationMapper.toEntity(eventCompetitionRegistrationDTO);
        if (entity.getId() != null) {
            log.warn("EventCompetitionRegistration has ID {} set during create. Clearing ID.", entity.getId());
            entity.setId(null);
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

    private void validateRegistration(EventCompetitionRegistration registration, Long excludeId) {
        EventCompetition competition = registration.getCompetition();
        if (competition == null || competition.getId() == null) {
            throw new BadRequestAlertException("Competition is required", ENTITY_NAME, "competitionrequired");
        }
        competition = eventCompetitionRepository
            .findById(competition.getId())
            .orElseThrow(() -> new BadRequestAlertException("Competition not found", ENTITY_NAME, "competitionnotfound"));

        if (competition.getEvent() == null || competition.getEvent().getId() == null) {
            throw new BadRequestAlertException("Event is required", ENTITY_NAME, "eventrequired");
        }

        EventCompetitionSettings settings = eventCompetitionSettingsRepository
            .findOneByEventId(competition.getEvent().getId())
            .orElseThrow(() -> new BadRequestAlertException("Competition settings not found", ENTITY_NAME, "settingsnotfound"));

        ZonedDateTime now = ZonedDateTime.now();
        if ((settings.getRegistrationDeadline() != null && settings.getRegistrationDeadline().isBefore(now)) || Boolean.FALSE.equals(settings.getRegistrationOpen())) {
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

        if (competition.getCompetitionType() == CompetitionType.GROUP && registration.getGroupLeaderRegistration() != null && registration.getGroupLeaderRegistration().getId() != null) {
            long groupSize =
                eventCompetitionRegistrationRepository.countByGroupLeaderRegistrationId(registration.getGroupLeaderRegistration().getId()) + 1;
            if (competition.getMaxGroupSize() != null && groupSize > competition.getMaxGroupSize()) {
                throw new BadRequestAlertException("Group size exceeds maximum", ENTITY_NAME, "groupsizeexceeded");
            }
        }
    }
}
