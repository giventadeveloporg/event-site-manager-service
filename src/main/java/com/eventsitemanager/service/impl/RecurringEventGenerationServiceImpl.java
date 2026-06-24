package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.enumeration.RecurrenceEndType;
import com.eventsitemanager.domain.enumeration.RecurrencePattern;
import com.eventsitemanager.repository.EventDetailsRepository;
import com.eventsitemanager.service.RecurrenceCalculationService;
import com.eventsitemanager.service.RecurringEventGenerationService;
import com.eventsitemanager.service.dto.EventDetailsDTO;
import com.eventsitemanager.service.dto.RecurrenceConfig;
import com.eventsitemanager.service.mapper.EventDetailsMapper;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for generating and managing recurring events.
 */
@Service
@Transactional
public class RecurringEventGenerationServiceImpl implements RecurringEventGenerationService {

    private static final Logger log = LoggerFactory.getLogger(RecurringEventGenerationServiceImpl.class);

    private final EventDetailsRepository eventDetailsRepository;
    private final EventDetailsMapper eventDetailsMapper;
    private final RecurrenceCalculationService recurrenceCalculationService;

    public RecurringEventGenerationServiceImpl(
        EventDetailsRepository eventDetailsRepository,
        EventDetailsMapper eventDetailsMapper,
        RecurrenceCalculationService recurrenceCalculationService
    ) {
        this.eventDetailsRepository = eventDetailsRepository;
        this.eventDetailsMapper = eventDetailsMapper;
        this.recurrenceCalculationService = recurrenceCalculationService;
    }

    @Override
    public List<EventDetailsDTO> generateRecurringEvents(Long parentEventId) {
        log.debug("Generating recurring events for parent event: {}", parentEventId);

        EventDetails parentEvent = eventDetailsRepository
            .findById(parentEventId)
            .orElseThrow(() -> new EntityNotFoundException("Parent event not found: " + parentEventId));

        if (!Boolean.TRUE.equals(parentEvent.getIsRecurring())) {
            throw new IllegalArgumentException("Event is not a recurring event: " + parentEventId);
        }

        // Build recurrence config from parent event
        RecurrenceConfig recurrenceConfig = buildRecurrenceConfigFromEvent(parentEvent);
        if (recurrenceConfig == null) {
            throw new IllegalStateException("Recurrence configuration is missing for event: " + parentEventId);
        }

        // Calculate occurrence dates
        List<LocalDate> occurrenceDates = recurrenceCalculationService.calculateOccurrences(parentEvent.getStartDate(), recurrenceConfig);

        // Remove the first occurrence (it's the parent event itself)
        if (occurrenceDates.size() > 1) {
            occurrenceDates = occurrenceDates.subList(1, occurrenceDates.size());
        } else {
            log.warn("No additional occurrences to generate for event: {}", parentEventId);
            return new ArrayList<>();
        }

        // Delete existing child events
        List<EventDetails> existingChildren = eventDetailsRepository.findByParentEventId(parentEventId);
        if (!existingChildren.isEmpty()) {
            log.debug("Deleting {} existing child events", existingChildren.size());
            eventDetailsRepository.deleteAll(existingChildren);
        }

        // Generate new child events
        List<EventDetailsDTO> generatedEvents = new ArrayList<>();
        Long seriesId = parentEvent.getRecurrenceSeriesId() != null ? parentEvent.getRecurrenceSeriesId() : parentEventId;

        // Ensure parent has series ID set
        if (parentEvent.getRecurrenceSeriesId() == null) {
            parentEvent.setRecurrenceSeriesId(seriesId);
            eventDetailsRepository.save(parentEvent);
        }

        for (LocalDate occurrenceDate : occurrenceDates) {
            EventDetails childEvent = createChildEvent(parentEvent, occurrenceDate, seriesId);
            childEvent = eventDetailsRepository.save(childEvent);
            generatedEvents.add(eventDetailsMapper.toDto(childEvent));
        }

        log.info("Generated {} recurring events for parent event: {}", generatedEvents.size(), parentEventId);
        return generatedEvents;
    }

    @Override
    public void updateRecurringSeries(Long parentEventId, RecurrenceConfig newConfig, boolean regenerateEvents) {
        log.debug("Updating recurring series for parent event: {}, regenerateEvents: {}", parentEventId, regenerateEvents);

        EventDetails parentEvent = eventDetailsRepository
            .findById(parentEventId)
            .orElseThrow(() -> new EntityNotFoundException("Parent event not found: " + parentEventId));

        // Update parent event recurrence fields
        parentEvent.setIsRecurring(true);
        parentEvent.setRecurrencePattern(newConfig.getPattern());
        parentEvent.setRecurrenceInterval(newConfig.getInterval() != null ? newConfig.getInterval() : 1);
        parentEvent.setRecurrenceEndType(newConfig.getEndType());
        parentEvent.setRecurrenceEndDate(newConfig.getEndDate());
        parentEvent.setRecurrenceOccurrences(newConfig.getOccurrences());
        // Convert List<Integer> to Integer[] for PostgreSQL integer[] column
        if (newConfig.getWeeklyDays() != null && !newConfig.getWeeklyDays().isEmpty()) {
            parentEvent.setRecurrenceWeeklyDays(newConfig.getWeeklyDays().toArray(new Integer[0]));
        } else {
            parentEvent.setRecurrenceWeeklyDays(null);
        }
        parentEvent.setRecurrenceMonthlyDay(newConfig.getMonthlyDay());

        eventDetailsRepository.save(parentEvent);

        if (regenerateEvents) {
            generateRecurringEvents(parentEventId);
        }

        log.info("Updated recurring series for parent event: {}", parentEventId);
    }

    @Override
    public void deleteRecurringSeries(Long parentEventId) {
        log.debug("Deleting recurring series for parent event: {}", parentEventId);

        EventDetails parentEvent = eventDetailsRepository
            .findById(parentEventId)
            .orElseThrow(() -> new EntityNotFoundException("Parent event not found: " + parentEventId));

        Long seriesId = parentEvent.getRecurrenceSeriesId() != null ? parentEvent.getRecurrenceSeriesId() : parentEventId;

        // Delete all events in the series
        List<EventDetails> seriesEvents = eventDetailsRepository.findByRecurrenceSeriesId(seriesId);
        eventDetailsRepository.deleteAll(seriesEvents);

        log.info("Deleted recurring series with {} events (seriesId: {})", seriesEvents.size(), seriesId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDetailsDTO> getRecurringSeries(Long parentEventId) {
        log.debug("Getting recurring series for parent event: {}", parentEventId);

        EventDetails parentEvent = eventDetailsRepository
            .findById(parentEventId)
            .orElseThrow(() -> new EntityNotFoundException("Parent event not found: " + parentEventId));

        Long seriesId = parentEvent.getRecurrenceSeriesId() != null ? parentEvent.getRecurrenceSeriesId() : parentEventId;

        List<EventDetails> seriesEvents = eventDetailsRepository.findByRecurrenceSeriesId(seriesId);

        return seriesEvents.stream().map(eventDetailsMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Build RecurrenceConfig from EventDetails entity.
     */
    private RecurrenceConfig buildRecurrenceConfigFromEvent(EventDetails event) {
        if (event.getRecurrencePattern() == null) {
            return null;
        }

        RecurrenceConfig config = new RecurrenceConfig();
        config.setPattern(event.getRecurrencePattern());
        config.setInterval(event.getRecurrenceInterval() != null ? event.getRecurrenceInterval() : 1);
        config.setEndType(event.getRecurrenceEndType());
        config.setEndDate(event.getRecurrenceEndDate());
        config.setOccurrences(event.getRecurrenceOccurrences());
        // Convert Integer[] to List<Integer> for RecurrenceConfig DTO
        if (event.getRecurrenceWeeklyDays() != null && event.getRecurrenceWeeklyDays().length > 0) {
            config.setWeeklyDays(java.util.Arrays.asList(event.getRecurrenceWeeklyDays()));
        } else {
            config.setWeeklyDays(null);
        }
        config.setMonthlyDay(event.getRecurrenceMonthlyDay());

        return config;
    }

    /**
     * Create a child event from parent event with new dates.
     */
    private EventDetails createChildEvent(EventDetails parentEvent, LocalDate occurrenceDate, Long seriesId) {
        EventDetails childEvent = new EventDetails();

        // Copy all properties from parent
        childEvent.setTenantId(parentEvent.getTenantId());
        childEvent.setTitle(parentEvent.getTitle());
        childEvent.setCaption(parentEvent.getCaption());
        childEvent.setDescription(parentEvent.getDescription());
        childEvent.setStartDate(occurrenceDate);
        // Calculate end date based on parent event duration
        if (parentEvent.getStartDate() != null && parentEvent.getEndDate() != null) {
            long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(parentEvent.getStartDate(), parentEvent.getEndDate());
            childEvent.setEndDate(occurrenceDate.plusDays(daysBetween));
        } else {
            childEvent.setEndDate(occurrenceDate); // Same day if no duration
        }
        childEvent.setPromotionStartDate(parentEvent.getPromotionStartDate());
        childEvent.setStartTime(parentEvent.getStartTime());
        childEvent.setEndTime(parentEvent.getEndTime());
        childEvent.setTimezone(parentEvent.getTimezone());
        childEvent.setLocation(parentEvent.getLocation());
        childEvent.setDirectionsToVenue(parentEvent.getDirectionsToVenue());
        childEvent.setCapacity(parentEvent.getCapacity());
        childEvent.setAdmissionType(parentEvent.getAdmissionType());
        childEvent.setIsActive(parentEvent.getIsActive());
        childEvent.setMaxGuestsPerAttendee(parentEvent.getMaxGuestsPerAttendee());
        childEvent.setAllowGuests(parentEvent.getAllowGuests());
        childEvent.setRequireGuestApproval(parentEvent.getRequireGuestApproval());
        childEvent.setEnableGuestPricing(parentEvent.getEnableGuestPricing());
        childEvent.setEnableQrCode(parentEvent.getEnableQrCode());
        childEvent.setIsRegistrationRequired(parentEvent.getIsRegistrationRequired());
        childEvent.setIsSportsEvent(parentEvent.getIsSportsEvent());
        childEvent.setIsLive(parentEvent.getIsLive());
        childEvent.setIsFeaturedEvent(parentEvent.getIsFeaturedEvent());
        childEvent.setFeaturedEventPriorityRanking(parentEvent.getFeaturedEventPriorityRanking());
        childEvent.setLiveEventPriorityRanking(parentEvent.getLiveEventPriorityRanking());
        childEvent.setMetadata(parentEvent.getMetadata());
        childEvent.setEmailHeaderImageUrl(parentEvent.getEmailHeaderImageUrl());
        childEvent.setFromEmail(parentEvent.getFromEmail());
        childEvent.setCreatedBy(parentEvent.getCreatedBy());
        childEvent.setEventType(parentEvent.getEventType());

        // Set recurrence relationship fields
        childEvent.setIsRecurring(false); // Child events are not recurring themselves
        childEvent.setParentEvent(parentEvent);
        childEvent.setRecurrenceSeriesId(seriesId);

        // Set timestamps
        childEvent.setCreatedAt(ZonedDateTime.now());
        childEvent.setUpdatedAt(ZonedDateTime.now());

        return childEvent;
    }
}
