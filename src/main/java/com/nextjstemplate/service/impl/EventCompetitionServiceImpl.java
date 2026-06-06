package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventCompetition;
import com.nextjstemplate.domain.EventCompetitionParticipant;
import com.nextjstemplate.domain.EventCompetitionSettings;
import com.nextjstemplate.domain.enumeration.CompetitionAudienceMode;
import com.nextjstemplate.domain.enumeration.CompetitionEligibleAudience;
import com.nextjstemplate.domain.enumeration.CompetitionParticipantType;
import com.nextjstemplate.domain.enumeration.CompetitionRegistrationStatus;
import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.repository.EventCompetitionParticipantRepository;
import com.nextjstemplate.repository.EventCompetitionRegistrationRepository;
import com.nextjstemplate.repository.EventCompetitionRepository;
import com.nextjstemplate.repository.EventCompetitionSettingsRepository;
import com.nextjstemplate.service.EventCompetitionService;
import com.nextjstemplate.service.dto.CompetitionEligibilityCheckDTO;
import com.nextjstemplate.service.dto.EventCompetitionDTO;
import com.nextjstemplate.service.mapper.EventCompetitionMapper;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EventCompetitionServiceImpl implements EventCompetitionService {

    private static final String ENTITY_NAME = "eventCompetition";

    private final Logger log = LoggerFactory.getLogger(EventCompetitionServiceImpl.class);

    private final EventCompetitionRepository eventCompetitionRepository;

    private final EventCompetitionMapper eventCompetitionMapper;

    private final EventCompetitionSettingsRepository eventCompetitionSettingsRepository;

    private final EventCompetitionParticipantRepository eventCompetitionParticipantRepository;

    private final EventCompetitionRegistrationRepository eventCompetitionRegistrationRepository;

    public EventCompetitionServiceImpl(
        EventCompetitionRepository eventCompetitionRepository,
        EventCompetitionMapper eventCompetitionMapper,
        EventCompetitionSettingsRepository eventCompetitionSettingsRepository,
        EventCompetitionParticipantRepository eventCompetitionParticipantRepository,
        EventCompetitionRegistrationRepository eventCompetitionRegistrationRepository
    ) {
        this.eventCompetitionRepository = eventCompetitionRepository;
        this.eventCompetitionMapper = eventCompetitionMapper;
        this.eventCompetitionSettingsRepository = eventCompetitionSettingsRepository;
        this.eventCompetitionParticipantRepository = eventCompetitionParticipantRepository;
        this.eventCompetitionRegistrationRepository = eventCompetitionRegistrationRepository;
    }

    @Override
    public EventCompetitionDTO save(EventCompetitionDTO eventCompetitionDTO) {
        log.debug("Request to save EventCompetition : {}", eventCompetitionDTO);
        EventCompetition entity = eventCompetitionMapper.toEntity(eventCompetitionDTO);
        if (entity.getId() != null) {
            log.warn("EventCompetition has ID {} set during create operation. Clearing ID to force sequence generation.", entity.getId());
            entity.setId(null);
        }
        entity = eventCompetitionRepository.save(entity);
        return eventCompetitionMapper.toDto(entity);
    }

    @Override
    public EventCompetitionDTO update(EventCompetitionDTO eventCompetitionDTO) {
        log.debug("Request to update EventCompetition : {}", eventCompetitionDTO);
        EventCompetition entity = eventCompetitionMapper.toEntity(eventCompetitionDTO);
        entity = eventCompetitionRepository.save(entity);
        return eventCompetitionMapper.toDto(entity);
    }

    @Override
    public Optional<EventCompetitionDTO> partialUpdate(EventCompetitionDTO eventCompetitionDTO) {
        log.debug("Request to partially update EventCompetition : {}", eventCompetitionDTO);
        return eventCompetitionRepository
            .findById(eventCompetitionDTO.getId())
            .map(existing -> {
                eventCompetitionMapper.partialUpdate(existing, eventCompetitionDTO);
                return existing;
            })
            .map(eventCompetitionRepository::save)
            .map(eventCompetitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventCompetitionDTO> findAll(Pageable pageable) {
        return eventCompetitionRepository.findAll(pageable).map(eventCompetitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventCompetitionDTO> findOne(Long id) {
        return eventCompetitionRepository.findById(id).map(eventCompetitionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        eventCompetitionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CompetitionEligibilityCheckDTO checkEligibility(Long competitionId, Long participantProfileId) {
        CompetitionEligibilityCheckDTO result = new CompetitionEligibilityCheckDTO();
        result.setEligible(true);

        EventCompetition competition = eventCompetitionRepository
            .findById(competitionId)
            .orElseThrow(() -> new BadRequestAlertException("Competition not found", ENTITY_NAME, "competitionnotfound"));

        if (competition.getEvent() == null || competition.getEvent().getId() == null) {
            result.setEligible(false);
            result.addReason("Event is not configured for this competition");
            return result;
        }

        EventCompetitionSettings settings = eventCompetitionSettingsRepository
            .findOneByEventId(competition.getEvent().getId())
            .orElse(null);
        if (settings == null) {
            result.setEligible(false);
            result.addReason("Competition settings not found");
            return result;
        }

        ZonedDateTime now = ZonedDateTime.now();
        if (competition.getRegistrationDeadline() != null && competition.getRegistrationDeadline().isBefore(now)) {
            result.setEligible(false);
            result.addReason("Registration deadline has passed for this competition");
        }
        if (
            (settings.getRegistrationDeadline() != null && settings.getRegistrationDeadline().isBefore(now)) ||
            Boolean.FALSE.equals(settings.getRegistrationOpen())
        ) {
            result.setEligible(false);
            result.addReason("Registration is closed");
        }

        if (competition.getMaxParticipants() != null) {
            long activeCount = eventCompetitionRegistrationRepository.countByCompetitionIdAndRegistrationStatusNotIn(
                competition.getId(),
                Arrays.asList(CompetitionRegistrationStatus.CANCELLED, CompetitionRegistrationStatus.REFUNDED)
            );
            if (activeCount >= competition.getMaxParticipants()) {
                result.setEligible(false);
                result.addReason("Competition is at capacity");
            }
        }

        EventCompetitionParticipant participant = eventCompetitionParticipantRepository
            .findById(participantProfileId)
            .orElseThrow(() -> new BadRequestAlertException("Participant not found", ENTITY_NAME, "participantnotfound"));

        if (
            eventCompetitionRegistrationRepository.existsByCompetitionIdAndParticipantProfileIdAndIdNot(
                competition.getId(),
                participantProfileId,
                -1L
            )
        ) {
            result.setEligible(false);
            result.addReason("Participant is already registered for this competition");
        }

        CompetitionEligibleAudience eligible = competition.getEligibleAudience();
        CompetitionParticipantType participantType = participant.getParticipantType();
        if (eligible == CompetitionEligibleAudience.YOUTH_ONLY && participantType != CompetitionParticipantType.CHILD) {
            result.setEligible(false);
            result.addReason("Only child participants are eligible");
        }
        if (eligible == CompetitionEligibleAudience.ADULT_ONLY && participantType != CompetitionParticipantType.ADULT) {
            result.setEligible(false);
            result.addReason("Only adult participants are eligible");
        }

        if (settings.getAudienceMode() == CompetitionAudienceMode.YOUTH && participantType == CompetitionParticipantType.CHILD) {
            if (participant.getGuardianUserProfile() == null || participant.getGuardianUserProfile().getId() == null) {
                result.setEligible(false);
                result.addReason("Guardian is required for youth participants");
            }
        }
        if (settings.getAudienceMode() == CompetitionAudienceMode.ADULT && participantType != CompetitionParticipantType.ADULT) {
            result.setEligible(false);
            result.addReason("Only adult participants are allowed");
        }

        if (participant.getDateOfBirth() != null) {
            int age = Period.between(participant.getDateOfBirth(), LocalDate.now()).getYears();
            if (competition.getMinAge() != null && age < competition.getMinAge()) {
                result.setEligible(false);
                result.addReason("Participant age is below the minimum of " + competition.getMinAge());
            }
            if (competition.getMaxAge() != null && age > competition.getMaxAge()) {
                result.setEligible(false);
                result.addReason("Participant age is above the maximum of " + competition.getMaxAge());
            }
        }

        if (participant.getCurrentGrade() != null) {
            if (competition.getMinGrade() != null && participant.getCurrentGrade() < competition.getMinGrade()) {
                result.setEligible(false);
                result.addReason("Participant grade is below the minimum of " + competition.getMinGrade());
            }
            if (competition.getMaxGrade() != null && participant.getCurrentGrade() > competition.getMaxGrade()) {
                result.setEligible(false);
                result.addReason("Participant grade is above the maximum of " + competition.getMaxGrade());
            }
        }

        return result;
    }
}
