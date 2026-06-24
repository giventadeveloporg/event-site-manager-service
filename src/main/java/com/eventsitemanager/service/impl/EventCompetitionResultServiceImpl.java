package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.EventCompetition;
import com.eventsitemanager.domain.EventCompetitionResult;
import com.eventsitemanager.domain.EventCompetitionSettings;
import com.eventsitemanager.domain.EventMedia;
import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.EventCompetitionRepository;
import com.eventsitemanager.repository.EventCompetitionResultRepository;
import com.eventsitemanager.repository.EventCompetitionSettingsRepository;
import com.eventsitemanager.repository.EventMediaRepository;
import com.eventsitemanager.service.EventCompetitionResultService;
import com.eventsitemanager.service.dto.EventCompetitionResultDTO;
import com.eventsitemanager.service.mapper.EventCompetitionResultMapper;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EventCompetitionResultServiceImpl implements EventCompetitionResultService {

    private static final String ENTITY_NAME = "eventCompetitionResult";

    private final Logger log = LoggerFactory.getLogger(EventCompetitionResultServiceImpl.class);

    private final EventCompetitionResultRepository eventCompetitionResultRepository;

    private final EventCompetitionResultMapper eventCompetitionResultMapper;

    private final EventMediaRepository eventMediaRepository;

    private final EventCompetitionRepository eventCompetitionRepository;

    private final EventCompetitionSettingsRepository eventCompetitionSettingsRepository;

    public EventCompetitionResultServiceImpl(
        EventCompetitionResultRepository eventCompetitionResultRepository,
        EventCompetitionResultMapper eventCompetitionResultMapper,
        EventMediaRepository eventMediaRepository,
        EventCompetitionRepository eventCompetitionRepository,
        EventCompetitionSettingsRepository eventCompetitionSettingsRepository
    ) {
        this.eventCompetitionResultRepository = eventCompetitionResultRepository;
        this.eventCompetitionResultMapper = eventCompetitionResultMapper;
        this.eventMediaRepository = eventMediaRepository;
        this.eventCompetitionRepository = eventCompetitionRepository;
        this.eventCompetitionSettingsRepository = eventCompetitionSettingsRepository;
    }

    @Override
    public EventCompetitionResultDTO save(EventCompetitionResultDTO dto) {
        log.debug("Request to save EventCompetitionResult : {}", dto);
        EventCompetitionResult entity = eventCompetitionResultMapper.toEntity(dto);
        if (entity.getId() != null) {
            log.warn("EventCompetitionResult has ID {} set during create. Clearing ID.", entity.getId());
            entity.setId(null);
        }
        validateResult(entity);
        entity = eventCompetitionResultRepository.save(entity);
        return eventCompetitionResultMapper.toDto(entity);
    }

    @Override
    public EventCompetitionResultDTO update(EventCompetitionResultDTO dto) {
        log.debug("Request to update EventCompetitionResult : {}", dto);
        EventCompetitionResult entity = eventCompetitionResultMapper.toEntity(dto);
        validateResult(entity);
        entity = eventCompetitionResultRepository.save(entity);
        return eventCompetitionResultMapper.toDto(entity);
    }

    @Override
    public Optional<EventCompetitionResultDTO> partialUpdate(EventCompetitionResultDTO dto) {
        log.debug("Request to partially update EventCompetitionResult : {}", dto);
        return eventCompetitionResultRepository
            .findById(dto.getId())
            .map(existing -> {
                eventCompetitionResultMapper.partialUpdate(existing, dto);
                validateResult(existing);
                return existing;
            })
            .map(eventCompetitionResultRepository::save)
            .map(eventCompetitionResultMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventCompetitionResultDTO> findAll(Pageable pageable) {
        return eventCompetitionResultRepository.findAll(pageable).map(eventCompetitionResultMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventCompetitionResultDTO> findOne(Long id) {
        return eventCompetitionResultRepository.findById(id).map(eventCompetitionResultMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        eventCompetitionResultRepository.deleteById(id);
    }

    private void validateResult(EventCompetitionResult result) {
        if (StringUtils.isBlank(result.getDisplayName())) {
            throw new BadRequestAlertException("displayName is required", ENTITY_NAME, "displaynamerequired");
        }
        if (result.getWinnerMedia() != null && result.getWinnerMedia().getId() != null) {
            EventMedia media = eventMediaRepository
                .findById(result.getWinnerMedia().getId())
                .orElseThrow(() -> new BadRequestAlertException("Winner media not found", ENTITY_NAME, "winnermedianotfound"));
            if (result.getEvent() == null || result.getEvent().getId() == null || media.getEventId() == null) {
                throw new BadRequestAlertException("Event is required for winner media", ENTITY_NAME, "eventrequired");
            }
            if (!result.getEvent().getId().equals(media.getEventId())) {
                throw new BadRequestAlertException("Winner media must belong to the same event", ENTITY_NAME, "winnermediaeventmismatch");
            }
        }

        if (result.getCompetition() == null || result.getCompetition().getId() == null) {
            throw new BadRequestAlertException("Competition is required", ENTITY_NAME, "competitionrequired");
        }

        EventCompetition competition = eventCompetitionRepository
            .findById(result.getCompetition().getId())
            .orElseThrow(() -> new BadRequestAlertException("Competition not found", ENTITY_NAME, "competitionnotfound"));

        Integer maxPlacements = competition.getMaxPlacements();
        if (maxPlacements == null && competition.getEvent() != null && competition.getEvent().getId() != null) {
            maxPlacements =
                eventCompetitionSettingsRepository
                    .findOneByEventId(competition.getEvent().getId())
                    .map(EventCompetitionSettings::getDefaultMaxPlacements)
                    .orElse(3);
        }
        if (maxPlacements == null) {
            maxPlacements = 3;
        }

        if (result.getPlacement() != null) {
            if (result.getPlacement() < 1 || result.getPlacement() > maxPlacements) {
                throw new BadRequestAlertException("Placement must be between 1 and " + maxPlacements, ENTITY_NAME, "placementoutofrange");
            }

            if (Boolean.TRUE.equals(result.getIsPublished())) {
                boolean duplicatePlacement = eventCompetitionResultRepository.existsByCompetitionIdAndPlacementAndIsPublishedTrueAndIdNot(
                    competition.getId(),
                    result.getPlacement(),
                    result.getId() == null ? -1L : result.getId()
                );
                if (duplicatePlacement) {
                    throw new BadRequestAlertException(
                        "Placement already assigned for this competition",
                        ENTITY_NAME,
                        "duplicateplacement"
                    );
                }
            }
        }

        if (
            result.getPointsAwarded() != null &&
            result.getPointsAwarded() == 0 &&
            result.getPlacement() != null &&
            competition.getEvent() != null &&
            competition.getEvent().getId() != null
        ) {
            eventCompetitionSettingsRepository
                .findOneByEventId(competition.getEvent().getId())
                .ifPresent(settings -> result.setPointsAwarded(resolvePointsForPlacement(settings, result.getPlacement())));
        }
    }

    private int resolvePointsForPlacement(EventCompetitionSettings settings, int placement) {
        return switch (placement) {
            case 1 -> settings.getPointsFirst() != null ? settings.getPointsFirst() : 0;
            case 2 -> settings.getPointsSecond() != null ? settings.getPointsSecond() : 0;
            case 3 -> settings.getPointsThird() != null ? settings.getPointsThird() : 0;
            case 4 -> settings.getPointsFourth() != null ? settings.getPointsFourth() : 0;
            default -> 0;
        };
    }
}
