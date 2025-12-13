package com.nextjstemplate.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.EventRecurrenceSeries;
import com.nextjstemplate.domain.enumeration.RecurrenceEndType;
import com.nextjstemplate.domain.enumeration.RecurrencePattern;
import com.nextjstemplate.repository.EventDetailsRepository;
import com.nextjstemplate.repository.EventRecurrenceSeriesRepository;
import com.nextjstemplate.service.EventDetailsService;
import com.nextjstemplate.service.RecurringEventGenerationService;
import com.nextjstemplate.service.dto.EventDetailsDTO;
import com.nextjstemplate.service.mapper.EventDetailsMapper;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.EventDetails}.
 */
@Service
@Transactional
public class EventDetailsServiceImpl implements EventDetailsService {

    private final Logger log = LoggerFactory.getLogger(EventDetailsServiceImpl.class);

    private final EventDetailsRepository eventDetailsRepository;

    private final EventDetailsMapper eventDetailsMapper;

    private final EventRecurrenceSeriesRepository recurrenceSeriesRepository;

    private final ObjectMapper objectMapper;

    private final RecurringEventGenerationService recurringEventGenerationService;

    public EventDetailsServiceImpl(
        EventDetailsRepository eventDetailsRepository,
        EventDetailsMapper eventDetailsMapper,
        EventRecurrenceSeriesRepository recurrenceSeriesRepository,
        ObjectMapper objectMapper,
        RecurringEventGenerationService recurringEventGenerationService
    ) {
        this.eventDetailsRepository = eventDetailsRepository;
        this.eventDetailsMapper = eventDetailsMapper;
        this.recurrenceSeriesRepository = recurrenceSeriesRepository;
        this.objectMapper = objectMapper;
        this.recurringEventGenerationService = recurringEventGenerationService;
    }

    @Override
    @CacheEvict(value = "eventDetails", allEntries = true)
    public EventDetailsDTO save(EventDetailsDTO eventDetailsDTO) {
        log.debug("Request to save EventDetails : {}", eventDetailsDTO);
        EventDetails eventDetails = eventDetailsMapper.toEntity(eventDetailsDTO);

        // Handle parentEventId mapping
        if (eventDetailsDTO.getParentEventId() != null) {
            eventDetailsRepository.findById(eventDetailsDTO.getParentEventId()).ifPresent(eventDetails::setParentEvent);
        }

        // Handle donation metadata (fundraiser/charity)
        handleDonationMetadata(eventDetails, eventDetailsDTO);

        // Handle recurrence metadata
        handleRecurrenceMetadata(eventDetails, eventDetailsDTO);

        // Process recurrence: Read from DTO fields first (primary source), then fallback to metadata
        processRecurrenceConfiguration(eventDetails, eventDetailsDTO);

        // CRITICAL FIX: Build eventRecurrenceMetadata JSON from individual recurrence fields
        if (Boolean.TRUE.equals(eventDetails.getIsRecurring())) {
            buildAndSetRecurrenceMetadata(eventDetails);
        }

        eventDetails = eventDetailsRepository.save(eventDetails);

        // CRITICAL FIX: Set recurrenceSeriesId to parent event ID (for parent events)
        // This must be set after save when we have the ID
        if (Boolean.TRUE.equals(eventDetails.getIsRecurring()) && eventDetails.getParentEvent() == null) {
            if (eventDetails.getRecurrenceSeriesId() == null) {
                eventDetails.setRecurrenceSeriesId(eventDetails.getId());
                eventDetails = eventDetailsRepository.save(eventDetails);
            }
            // Create or update recurrence series record
            createOrUpdateRecurrenceSeries(eventDetails, eventDetailsDTO);

            // CRITICAL: Generate child event occurrences
            try {
                recurringEventGenerationService.generateRecurringEvents(eventDetails.getId());
                log.info("Generated child events for recurring parent event: {}", eventDetails.getId());
            } catch (Exception e) {
                log.error("Failed to generate child events for parent event: {}", eventDetails.getId(), e);
                // Don't throw exception - allow parent event to be saved even if child generation fails
            }
        }

        return eventDetailsMapper.toDto(eventDetails);
    }

    @Override
    @CacheEvict(value = "eventDetails", allEntries = true)
    public EventDetailsDTO update(EventDetailsDTO eventDetailsDTO) {
        log.info(
            "UPDATE REQUEST - Event ID: {}, isActive: {} -> {}, isRecurring: {}, parentEventId: {}",
            eventDetailsDTO.getId(),
            eventDetailsDTO.getIsActive() != null ? eventDetailsDTO.getIsActive() : "null",
            eventDetailsDTO.getIsActive() != null ? eventDetailsDTO.getIsActive() : "null",
            eventDetailsDTO.getIsRecurring() != null ? eventDetailsDTO.getIsRecurring() : "null",
            eventDetailsDTO.getParentEventId() != null ? eventDetailsDTO.getParentEventId() : "null"
        );
        log.debug("Request to update EventDetails : {}", eventDetailsDTO);

        // CRITICAL FIX: Load existing entity first to properly compare and update
        EventDetails eventDetails = eventDetailsRepository
            .findById(eventDetailsDTO.getId())
            .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("EventDetails not found with id: " + eventDetailsDTO.getId())
            );

        // CRITICAL FIX: Store original isActive value BEFORE any updates
        Boolean originalIsActive = eventDetails.getIsActive();
        log.info(
            "EXISTING EVENT STATE - Event ID: {}, current isActive: {}, current isRecurring: {}, current parentEventId: {}",
            eventDetails.getId(),
            originalIsActive != null ? originalIsActive : "null",
            eventDetails.getIsRecurring() != null ? eventDetails.getIsRecurring() : "null",
            eventDetails.getParentEvent() != null ? eventDetails.getParentEvent().getId() : "null"
        );

        // CRITICAL FIX: Handle recurrence metadata and detect changes BEFORE updating entity
        String newRecurrenceMetadata = getRecurrenceMetadataFromDto(eventDetailsDTO);
        String existingRecurrenceMetadata = eventDetails.getEventRecurrenceMetadata();
        boolean wasRecurring = Boolean.TRUE.equals(eventDetails.getIsRecurring());
        boolean willBeRecurring = Boolean.TRUE.equals(eventDetailsDTO.getIsRecurring());
        boolean isParentEvent = eventDetails.getParentEvent() == null;

        // Check if this is just a deactivation (isActive changing to false) vs actual recurrence config change
        boolean isDeactivating = Boolean.FALSE.equals(eventDetailsDTO.getIsActive()) && Boolean.TRUE.equals(originalIsActive);

        // Check if recurrence config has changed - compare metadata JSON AND individual fields
        boolean metadataChanged = !java.util.Objects.equals(newRecurrenceMetadata, existingRecurrenceMetadata);
        boolean recurringStatusChanged = !java.util.Objects.equals(wasRecurring, willBeRecurring);
        boolean recurrenceFieldsChanged =
            !java.util.Objects.equals(eventDetails.getRecurrencePattern(), eventDetailsDTO.getRecurrencePattern()) ||
            !java.util.Objects.equals(eventDetails.getRecurrenceInterval(), eventDetailsDTO.getRecurrenceInterval()) ||
            !java.util.Objects.equals(eventDetails.getRecurrenceEndType(), eventDetailsDTO.getRecurrenceEndType()) ||
            !java.util.Objects.equals(eventDetails.getRecurrenceOccurrences(), eventDetailsDTO.getRecurrenceOccurrences()) ||
            !java.util.Objects.equals(eventDetails.getRecurrenceEndDate(), eventDetailsDTO.getRecurrenceEndDate());

        // CRITICAL FIX: Only delete child events and recurrence series if:
        // 1. Recurrence configuration fields are actually changing (not just isRecurring flag)
        // 2. AND it's not just a deactivation (isActive changing to false)
        // When deactivating, we should keep child events and recurrence series, just mark parent as inactive
        boolean shouldDeleteRecurrenceData =
            (metadataChanged || recurrenceFieldsChanged) && !isDeactivating && (wasRecurring || willBeRecurring) && isParentEvent;

        if (shouldDeleteRecurrenceData) {
            // CRITICAL: Delete existing child events and recurrence series only when recurrence config actually changes
            log.debug("Deleting recurrence series and children due to recurrence configuration change for event: {}", eventDetails.getId());
            deleteRecurringSeriesAndChildren(eventDetails.getId());
        } else if (recurringStatusChanged && isDeactivating) {
            // When deactivating, just log - don't delete child events or recurrence series
            log.debug("Deactivating event {} - keeping child events and recurrence series", eventDetails.getId());
        }

        // Map DTO fields to existing entity (this updates the entity with DTO values)
        eventDetailsMapper.partialUpdate(eventDetails, eventDetailsDTO);

        // CRITICAL FIX: Log DTO recurrence values to debug
        log.debug(
            "DTO recurrence values - isRecurring: {}, pattern: {}, occurrences: {}, interval: {}, endType: {}",
            eventDetailsDTO.getIsRecurring(),
            eventDetailsDTO.getRecurrencePattern(),
            eventDetailsDTO.getRecurrenceOccurrences(),
            eventDetailsDTO.getRecurrenceInterval(),
            eventDetailsDTO.getRecurrenceEndType()
        );

        // Handle parentEventId mapping
        if (eventDetailsDTO.getParentEventId() != null) {
            eventDetailsRepository.findById(eventDetailsDTO.getParentEventId()).ifPresent(eventDetails::setParentEvent);
        } else {
            // Clear parent event if parentEventId is null
            eventDetails.setParentEvent(null);
        }

        // Handle donation metadata (fundraiser/charity)
        handleDonationMetadata(eventDetails, eventDetailsDTO);

        // Process recurrence: Read from DTO fields first (primary source), then fallback to metadata
        processRecurrenceConfiguration(eventDetails, eventDetailsDTO);

        // CRITICAL FIX: Build eventRecurrenceMetadata JSON from individual recurrence fields
        // This ensures metadata JSON stays in sync with individual columns
        if (Boolean.TRUE.equals(eventDetails.getIsRecurring())) {
            buildAndSetRecurrenceMetadata(eventDetails);
        }

        eventDetails = eventDetailsRepository.save(eventDetails);

        // CRITICAL FIX: Flush and refresh entity to ensure we have the latest persisted values
        // This is important because Hibernate might return a cached entity
        eventDetailsRepository.flush();
        final Long eventId = eventDetails.getId();
        eventDetails =
            eventDetailsRepository
                .findById(eventId)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("EventDetails not found after save: " + eventId));

        log.debug(
            "After save and refresh - recurrenceOccurrences: {}, recurrencePattern: {}, isRecurring: {}",
            eventDetails.getRecurrenceOccurrences(),
            eventDetails.getRecurrencePattern(),
            eventDetails.getIsRecurring()
        );

        // CRITICAL FIX: Only process recurrence if isRecurring is true (check refreshed entity value)
        // This prevents processing recurrence for events that have been deactivated or are not recurring
        if (Boolean.TRUE.equals(eventDetails.getIsRecurring()) && eventDetails.getParentEvent() == null) {
            if (eventDetails.getRecurrenceSeriesId() == null) {
                eventDetails.setRecurrenceSeriesId(eventDetails.getId());
                eventDetails = eventDetailsRepository.save(eventDetails);
                eventDetailsRepository.flush();
            }
            // Create or update recurrence series record
            createOrUpdateRecurrenceSeries(eventDetails, eventDetailsDTO);

            // CRITICAL: Generate child event occurrences (regenerate on update)
            // Refresh entity one more time before generation to ensure latest values
            final Long eventIdForGeneration = eventDetails.getId();
            eventDetails =
                eventDetailsRepository
                    .findById(eventIdForGeneration)
                    .orElseThrow(() ->
                        new jakarta.persistence.EntityNotFoundException("EventDetails not found before generation: " + eventIdForGeneration)
                    );
            log.debug(
                "Before generateRecurringEvents - recurrenceOccurrences: {}, isRecurring: {}",
                eventDetails.getRecurrenceOccurrences(),
                eventDetails.getIsRecurring()
            );

            // CRITICAL FIX: Only generate recurring events if isRecurring is still true after refresh
            if (eventDetails.getIsRecurring() != null && eventDetails.getIsRecurring()) {
                try {
                    recurringEventGenerationService.generateRecurringEvents(eventDetails.getId());
                    log.info("Generated child events for recurring parent event: {}", eventDetails.getId());
                } catch (Exception e) {
                    log.error("Failed to generate child events for parent event: {}", eventDetails.getId(), e);
                    // Don't throw exception - allow parent event to be saved even if child generation fails
                }
            } else {
                log.debug("Skipping generateRecurringEvents - event is not recurring (isRecurring: {})", eventDetails.getIsRecurring());
            }
        } else {
            log.debug(
                "Skipping recurrence processing - isRecurring: {}, parentEvent: {}",
                eventDetails.getIsRecurring(),
                eventDetails.getParentEvent() != null ? eventDetails.getParentEvent().getId() : null
            );
        }

        // CRITICAL FIX: Sync child events' isActive status with parent when parent isActive changes
        // Only update children of THIS specific parent event, not other parents
        // Reuse existing isParentEvent variable (already defined at line 127)
        // Reuse existing eventId variable (already defined at line 199)
        boolean isActiveChanged = !java.util.Objects.equals(originalIsActive, eventDetails.getIsActive());

        // Log detailed information about the event's parent status for debugging
        Long parentEventIdFromEntity = eventDetails.getParentEvent() != null ? eventDetails.getParentEvent().getId() : null;
        log.debug(
            "Event {} - isParentEvent: {}, parentEvent from entity: {}, isActiveChanged: {}, new isActive: {}",
            eventId,
            isParentEvent,
            parentEventIdFromEntity,
            isActiveChanged,
            eventDetails.getIsActive()
        );

        if (isParentEvent && isActiveChanged && eventDetails.getIsActive() != null) {
            log.info(
                "Parent event {} isActive changed from {} to {} - syncing ONLY direct child events",
                eventId,
                originalIsActive,
                eventDetails.getIsActive()
            );
            syncChildEventsActiveStatus(eventId, eventDetails.getIsActive());
        } else if (!isParentEvent) {
            log.debug("Event {} is not a parent event (has parent: {}) - skipping child sync", eventId, parentEventIdFromEntity);
        } else {
            log.debug(
                "Event {} - skipping child sync (isParentEvent: {}, isActiveChanged: {}, isActive: {})",
                eventId,
                isParentEvent,
                isActiveChanged,
                eventDetails.getIsActive()
            );
        }

        return eventDetailsMapper.toDto(eventDetails);
    }

    @Override
    public Optional<EventDetailsDTO> partialUpdate(EventDetailsDTO eventDetailsDTO) {
        log.debug("Request to partially update EventDetails : {}", eventDetailsDTO);

        // CRITICAL FIX: Store original isActive value BEFORE the map chain so it's accessible in all lambdas
        final Boolean[] originalIsActiveHolder = new Boolean[1];

        return eventDetailsRepository
            .findById(eventDetailsDTO.getId())
            .map(existingEventDetails -> {
                // CRITICAL FIX: Store original isActive value BEFORE partial update
                originalIsActiveHolder[0] = existingEventDetails.getIsActive();

                eventDetailsMapper.partialUpdate(existingEventDetails, eventDetailsDTO);

                // Handle parentEventId mapping if provided
                if (eventDetailsDTO.getParentEventId() != null) {
                    eventDetailsRepository
                        .findById(eventDetailsDTO.getParentEventId())
                        .ifPresentOrElse(existingEventDetails::setParentEvent, () -> existingEventDetails.setParentEvent(null));
                }

                // Handle donation metadata (fundraiser/charity)
                handleDonationMetadata(existingEventDetails, eventDetailsDTO);

                // CRITICAL FIX: Handle recurrence metadata and detect changes BEFORE updating entity

                String newRecurrenceMetadata = getRecurrenceMetadataFromDto(eventDetailsDTO);
                String existingRecurrenceMetadata = existingEventDetails.getEventRecurrenceMetadata();
                boolean wasRecurring = Boolean.TRUE.equals(existingEventDetails.getIsRecurring());
                boolean willBeRecurring = Boolean.TRUE.equals(eventDetailsDTO.getIsRecurring());
                boolean isParentEvent = existingEventDetails.getParentEvent() == null;

                // Check if this is just a deactivation (isActive changing to false) vs actual recurrence config change
                boolean isDeactivating =
                    Boolean.FALSE.equals(eventDetailsDTO.getIsActive()) && Boolean.TRUE.equals(originalIsActiveHolder[0]);

                // Check if recurrence config has changed - compare metadata JSON AND individual fields
                boolean metadataChanged = !java.util.Objects.equals(newRecurrenceMetadata, existingRecurrenceMetadata);
                boolean recurringStatusChanged = !java.util.Objects.equals(wasRecurring, willBeRecurring);
                boolean recurrenceFieldsChanged =
                    !java.util.Objects.equals(existingEventDetails.getRecurrencePattern(), eventDetailsDTO.getRecurrencePattern()) ||
                    !java.util.Objects.equals(existingEventDetails.getRecurrenceInterval(), eventDetailsDTO.getRecurrenceInterval()) ||
                    !java.util.Objects.equals(existingEventDetails.getRecurrenceEndType(), eventDetailsDTO.getRecurrenceEndType()) ||
                    !java.util.Objects.equals(
                        existingEventDetails.getRecurrenceOccurrences(),
                        eventDetailsDTO.getRecurrenceOccurrences()
                    ) ||
                    !java.util.Objects.equals(existingEventDetails.getRecurrenceEndDate(), eventDetailsDTO.getRecurrenceEndDate());

                // CRITICAL FIX: Only delete child events and recurrence series if:
                // 1. Recurrence configuration fields are actually changing (not just isRecurring flag)
                // 2. AND it's not just a deactivation (isActive changing to false)
                // When deactivating, we should keep child events and recurrence series, just mark parent as inactive
                boolean shouldDeleteRecurrenceData =
                    (metadataChanged || recurrenceFieldsChanged) && !isDeactivating && (wasRecurring || willBeRecurring) && isParentEvent;

                if (shouldDeleteRecurrenceData) {
                    // CRITICAL: Delete existing child events and recurrence series only when recurrence config actually changes
                    log.debug(
                        "Deleting recurrence series and children due to recurrence configuration change for event: {}",
                        existingEventDetails.getId()
                    );
                    deleteRecurringSeriesAndChildren(existingEventDetails.getId());
                } else if (recurringStatusChanged && isDeactivating) {
                    // When deactivating, just log - don't delete child events or recurrence series
                    log.debug("Deactivating event {} - keeping child events and recurrence series", existingEventDetails.getId());
                }

                // Handle recurrence metadata
                handleRecurrenceMetadata(existingEventDetails, eventDetailsDTO);

                // Process recurrence: Read from DTO fields first (primary source), then fallback to metadata
                processRecurrenceConfiguration(existingEventDetails, eventDetailsDTO);

                // CRITICAL FIX: Build eventRecurrenceMetadata JSON from individual recurrence fields
                if (Boolean.TRUE.equals(existingEventDetails.getIsRecurring())) {
                    buildAndSetRecurrenceMetadata(existingEventDetails);
                }

                return existingEventDetails;
            })
            .map(eventDetailsRepository::save)
            .map(eventDetails -> {
                // CRITICAL FIX: Set recurrenceSeriesId to parent event ID (for parent events)
                if (Boolean.TRUE.equals(eventDetails.getIsRecurring()) && eventDetails.getParentEvent() == null) {
                    if (eventDetails.getRecurrenceSeriesId() == null) {
                        eventDetails.setRecurrenceSeriesId(eventDetails.getId());
                        eventDetails = eventDetailsRepository.save(eventDetails);
                    }
                    // Create or update recurrence series record
                    createOrUpdateRecurrenceSeries(eventDetails, eventDetailsDTO);

                    // CRITICAL FIX: Only generate recurring events if isRecurring is true
                    if (eventDetails.getIsRecurring() != null && eventDetails.getIsRecurring()) {
                        try {
                            recurringEventGenerationService.generateRecurringEvents(eventDetails.getId());
                            log.info("Generated child events for recurring parent event: {}", eventDetails.getId());
                        } catch (Exception e) {
                            log.error("Failed to generate child events for parent event: {}", eventDetails.getId(), e);
                            // Don't throw exception - allow parent event to be saved even if child generation fails
                        }
                    }
                }

                // CRITICAL FIX: Sync child events' isActive status with parent when parent isActive changes
                // Only update children of THIS specific parent event, not other parents
                boolean isParentEventForSync = eventDetails.getParentEvent() == null;
                boolean isActiveChanged =
                    originalIsActiveHolder[0] != null && !java.util.Objects.equals(originalIsActiveHolder[0], eventDetails.getIsActive());

                // Log detailed information about the event's parent status for debugging
                Long eventId = eventDetails.getId();
                Long parentEventIdFromEntity = eventDetails.getParentEvent() != null ? eventDetails.getParentEvent().getId() : null;
                log.debug(
                    "Event {} - isParentEventForSync: {}, parentEvent from entity: {}, isActiveChanged: {}, new isActive: {}",
                    eventId,
                    isParentEventForSync,
                    parentEventIdFromEntity,
                    isActiveChanged,
                    eventDetails.getIsActive()
                );

                if (isParentEventForSync && isActiveChanged && eventDetails.getIsActive() != null) {
                    log.info(
                        "Parent event {} isActive changed from {} to {} - syncing ONLY direct child events",
                        eventId,
                        originalIsActiveHolder[0],
                        eventDetails.getIsActive()
                    );
                    syncChildEventsActiveStatus(eventId, eventDetails.getIsActive());
                } else if (!isParentEventForSync) {
                    log.debug("Event {} is not a parent event (has parent: {}) - skipping child sync", eventId, parentEventIdFromEntity);
                } else {
                    log.debug(
                        "Event {} - skipping child sync (isParentEventForSync: {}, isActiveChanged: {}, isActive: {})",
                        eventId,
                        isParentEventForSync,
                        isActiveChanged,
                        eventDetails.getIsActive()
                    );
                }

                return eventDetails;
            })
            .map(eventDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventDetails");
        return eventDetailsRepository.findAll(pageable).map(eventDetailsMapper::toDto);
    }

    public Page<EventDetailsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return eventDetailsRepository.findAllWithEagerRelationships(pageable).map(eventDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "eventDetails", key = "#id", unless = "#result == null")
    public Optional<EventDetailsDTO> findOne(Long id) {
        log.debug("Request to get EventDetails : {}", id);
        return eventDetailsRepository.findOneWithEagerRelationships(id).map(eventDetailsMapper::toDto);
    }

    @Override
    @CacheEvict(value = "eventDetails", allEntries = true)
    public void delete(Long id) {
        log.debug("Request to delete EventDetails : {}", id);
        eventDetailsRepository.deleteById(id);
    }

    /**
     * Handle donation metadata (fundraiser/charity configuration).
     *
     * @param eventDetails the event details entity
     * @param eventDetailsDTO the DTO containing metadata fields
     */
    private void handleDonationMetadata(EventDetails eventDetails, EventDetailsDTO eventDetailsDTO) {
        // Priority 1: Use new donationMetadata field
        if (eventDetailsDTO.getDonationMetadata() != null && !eventDetailsDTO.getDonationMetadata().isEmpty()) {
            eventDetails.setDonationMetadata(eventDetailsDTO.getDonationMetadata());
        } else if (eventDetailsDTO.getMetadata() != null && !eventDetailsDTO.getMetadata().isEmpty()) {
            // Priority 2: Fallback - extract from old metadata field
            String donationMetadata = extractDonationMetadata(eventDetailsDTO.getMetadata());
            if (donationMetadata != null) {
                eventDetails.setDonationMetadata(donationMetadata);
            }
        }
    }

    /**
     * Handle recurrence metadata.
     *
     * @param eventDetails the event details entity
     * @param eventDetailsDTO the DTO containing metadata fields
     */
    private void handleRecurrenceMetadata(EventDetails eventDetails, EventDetailsDTO eventDetailsDTO) {
        // Priority 1: Use new eventRecurrenceMetadata field
        if (eventDetailsDTO.getEventRecurrenceMetadata() != null && !eventDetailsDTO.getEventRecurrenceMetadata().isEmpty()) {
            eventDetails.setEventRecurrenceMetadata(eventDetailsDTO.getEventRecurrenceMetadata());
        } else if (eventDetailsDTO.getMetadata() != null && !eventDetailsDTO.getMetadata().isEmpty()) {
            // Priority 2: Fallback - extract from old metadata field
            String recurrenceMetadata = extractRecurrenceMetadata(eventDetailsDTO.getMetadata());
            if (recurrenceMetadata != null) {
                eventDetails.setEventRecurrenceMetadata(recurrenceMetadata);
            }
        }
    }

    /**
     * Get recurrence metadata from DTO (for comparison purposes).
     *
     * @param eventDetailsDTO the DTO
     * @return recurrence metadata string or null
     */
    private String getRecurrenceMetadataFromDto(EventDetailsDTO eventDetailsDTO) {
        if (eventDetailsDTO.getEventRecurrenceMetadata() != null && !eventDetailsDTO.getEventRecurrenceMetadata().isEmpty()) {
            return eventDetailsDTO.getEventRecurrenceMetadata();
        } else if (eventDetailsDTO.getMetadata() != null && !eventDetailsDTO.getMetadata().isEmpty()) {
            return extractRecurrenceMetadata(eventDetailsDTO.getMetadata());
        }
        return null;
    }

    /**
     * Extract donation metadata from old metadata JSON string.
     *
     * @param metadataJson the old metadata JSON string
     * @return donation metadata JSON string or null
     */
    private String extractDonationMetadata(String metadataJson) {
        if (metadataJson == null || metadataJson.isEmpty()) {
            return null;
        }
        try {
            Map<String, Object> metadataMap = objectMapper.readValue(metadataJson, Map.class);
            Map<String, Object> donationMetadata = new java.util.HashMap<>();

            if (metadataMap.containsKey("isFundraiserEvent")) {
                donationMetadata.put("isFundraiserEvent", metadataMap.get("isFundraiserEvent"));
            }
            if (metadataMap.containsKey("isCharityEvent")) {
                donationMetadata.put("isCharityEvent", metadataMap.get("isCharityEvent"));
            }
            if (metadataMap.containsKey("zeroFeeProvider")) {
                donationMetadata.put("zeroFeeProvider", metadataMap.get("zeroFeeProvider"));
            }
            if (metadataMap.containsKey("givebutterCampaignId")) {
                donationMetadata.put("givebutterCampaignId", metadataMap.get("givebutterCampaignId"));
            }

            if (donationMetadata.isEmpty()) {
                return null;
            }

            return objectMapper.writeValueAsString(donationMetadata);
        } catch (Exception e) {
            log.warn("Failed to extract donation metadata from old metadata field", e);
            return null;
        }
    }

    /**
     * Build recurrence metadata JSON string from individual recurrence fields in entity.
     * This ensures eventRecurrenceMetadata stays in sync with individual columns.
     *
     * @param eventDetails the event details entity
     */
    private void buildAndSetRecurrenceMetadata(EventDetails eventDetails) {
        try {
            Map<String, Object> recurrenceConfig = new java.util.HashMap<>();

            if (eventDetails.getRecurrencePattern() != null) {
                recurrenceConfig.put("pattern", eventDetails.getRecurrencePattern().name());
            }
            if (eventDetails.getRecurrenceInterval() != null) {
                recurrenceConfig.put("interval", eventDetails.getRecurrenceInterval());
            }
            if (eventDetails.getRecurrenceEndType() != null) {
                recurrenceConfig.put("endType", eventDetails.getRecurrenceEndType().name());
            }
            if (eventDetails.getRecurrenceEndDate() != null) {
                recurrenceConfig.put("endDate", eventDetails.getRecurrenceEndDate().toString());
            }
            if (eventDetails.getRecurrenceOccurrences() != null) {
                recurrenceConfig.put("occurrences", eventDetails.getRecurrenceOccurrences());
            }
            if (eventDetails.getRecurrenceWeeklyDays() != null && eventDetails.getRecurrenceWeeklyDays().length > 0) {
                recurrenceConfig.put("weeklyDays", java.util.Arrays.asList(eventDetails.getRecurrenceWeeklyDays()));
            }
            if (eventDetails.getRecurrenceMonthlyDay() != null) {
                recurrenceConfig.put("monthlyDay", eventDetails.getRecurrenceMonthlyDay());
            }

            if (!recurrenceConfig.isEmpty()) {
                String recurrenceMetadataJson = objectMapper.writeValueAsString(recurrenceConfig);
                eventDetails.setEventRecurrenceMetadata(recurrenceMetadataJson);
            }
        } catch (Exception e) {
            log.error("Failed to build recurrence metadata JSON for event: {}", eventDetails.getId(), e);
            // Don't throw exception - allow event to be saved without metadata JSON
        }
    }

    /**
     * Extract recurrence metadata from old metadata JSON string.
     *
     * @param metadataJson the old metadata JSON string
     * @return recurrence metadata JSON string or null
     */
    private String extractRecurrenceMetadata(String metadataJson) {
        if (metadataJson == null || metadataJson.isEmpty()) {
            return null;
        }
        try {
            Map<String, Object> metadataMap = objectMapper.readValue(metadataJson, Map.class);
            Boolean isRecurring = (Boolean) metadataMap.get("isRecurring");
            if (Boolean.TRUE.equals(isRecurring)) {
                @SuppressWarnings("unchecked")
                Map<String, Object> recurrenceConfig = (Map<String, Object>) metadataMap.get("recurrenceConfig");
                if (recurrenceConfig != null) {
                    return objectMapper.writeValueAsString(recurrenceConfig);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to extract recurrence metadata from old metadata field", e);
        }
        return null;
    }

    /**
     * Delete existing recurrence series and child events.
     *
     * @param parentEventId the parent event ID
     */
    private void deleteRecurringSeriesAndChildren(Long parentEventId) {
        try {
            // Delete child events
            List<EventDetails> childEvents = eventDetailsRepository.findByParentEventId(parentEventId);
            if (!childEvents.isEmpty()) {
                log.debug("Deleting {} existing child events for parent event: {}", childEvents.size(), parentEventId);
                eventDetailsRepository.deleteAll(childEvents);
            }

            // Delete recurrence series record
            recurrenceSeriesRepository
                .findByParentEventId(parentEventId)
                .ifPresent(series -> {
                    log.debug("Deleting existing recurrence series for parent event: {}", parentEventId);
                    recurrenceSeriesRepository.delete(series);
                });
        } catch (Exception e) {
            log.error("Failed to delete recurring series and children for parent event: {}", parentEventId, e);
            // Don't throw exception - allow update to continue
        }
    }

    /**
     * Sync child events' isActive status with parent event.
     * Only updates child events that belong to the specific parent event ID.
     *
     * @param parentEventId the parent event ID
     * @param isActive the isActive status to set on child events
     */
    private void syncChildEventsActiveStatus(Long parentEventId, Boolean isActive) {
        try {
            // CRITICAL: Only get child events for THIS specific parent event ID
            // The native query ensures we query the parent_event_id column directly, avoiding JPA lazy loading issues
            List<EventDetails> childEvents = eventDetailsRepository.findByParentEventId(parentEventId);

            if (!childEvents.isEmpty()) {
                log.info(
                    "Syncing isActive status for {} child events of parent event: {} to {}",
                    childEvents.size(),
                    parentEventId,
                    isActive
                );

                int updatedCount = 0;
                int skippedCount = 0;
                int invalidCount = 0;

                for (EventDetails childEvent : childEvents) {
                    Long childId = childEvent.getId();
                    Boolean currentIsActive = childEvent.getIsActive();

                    // CRITICAL: Double-check that this child event actually belongs to the specified parent
                    // Query the parent_event_id column directly from the database to avoid lazy loading issues
                    Long actualParentId = eventDetailsRepository.getParentEventIdByEventId(childId);

                    // This defensive check prevents updating unrelated events due to any query issues
                    if (actualParentId == null || !actualParentId.equals(parentEventId)) {
                        log.warn(
                            "SKIPPING child event {} - parent_event_id mismatch! Expected parent: {}, Actual parent from DB: {}. " +
                            "This should not happen with correct query filtering. Event will NOT be updated.",
                            childId,
                            parentEventId,
                            actualParentId
                        );
                        invalidCount++;
                        continue;
                    }

                    // Only update if the status is different
                    if (!java.util.Objects.equals(currentIsActive, isActive)) {
                        childEvent.setIsActive(isActive);
                        eventDetailsRepository.save(childEvent);
                        updatedCount++;
                        log.info(
                            "Updated child event {} isActive from {} to {} for parent event {}",
                            childId,
                            currentIsActive,
                            isActive,
                            parentEventId
                        );
                    } else {
                        skippedCount++;
                        log.debug(
                            "Child event {} already has isActive={} for parent event {} - skipping update",
                            childId,
                            isActive,
                            parentEventId
                        );
                    }
                }

                log.info(
                    "Completed sync for parent event {}: {} updated, {} skipped (already correct), {} invalid (parent mismatch)",
                    parentEventId,
                    updatedCount,
                    skippedCount,
                    invalidCount
                );

                if (invalidCount > 0) {
                    log.error(
                        "WARNING: {} child events were skipped due to parent_event_id mismatch for parent event {}. " +
                        "This indicates a potential data integrity issue or query problem.",
                        invalidCount,
                        parentEventId
                    );
                }
            } else {
                log.debug("No child events found for parent event: {} (this is expected if event has no children)", parentEventId);
            }
        } catch (Exception e) {
            log.error("Failed to sync child events active status for parent event: {}", parentEventId, e);
            // Don't throw exception - allow parent update to continue
        }
    }

    /**
     * Process recurrence configuration: Read from DTO fields first (primary source),
     * then fallback to eventRecurrenceMetadata parsing if DTO fields are not set.
     *
     * @param eventDetails the event details entity to process
     * @param eventDetailsDTO the DTO containing recurrence fields
     */
    private void processRecurrenceConfiguration(EventDetails eventDetails, EventDetailsDTO eventDetailsDTO) {
        // Priority 1: Read from DTO fields (primary source)
        Boolean isRecurringFromDto = eventDetailsDTO.getIsRecurring();

        // CRITICAL FIX: If isRecurring is explicitly false, clear recurrence columns immediately
        if (Boolean.FALSE.equals(isRecurringFromDto)) {
            clearRecurrenceColumns(eventDetails);
            log.debug("Cleared recurrence columns for event: {} (isRecurring=false from DTO)", eventDetails.getId());
            return;
        }

        // CRITICAL FIX: Also check if DTO has any recurrence fields set (even if isRecurring is null)
        // This handles cases where frontend sends recurrence fields but isRecurring is not explicitly set
        boolean hasRecurrenceFieldsInDto =
            isRecurringFromDto != null ||
            eventDetailsDTO.getRecurrencePattern() != null ||
            eventDetailsDTO.getRecurrenceOccurrences() != null ||
            eventDetailsDTO.getRecurrenceEndType() != null ||
            eventDetailsDTO.getRecurrenceEndDate() != null ||
            (eventDetailsDTO.getRecurrenceWeeklyDays() != null && !eventDetailsDTO.getRecurrenceWeeklyDays().isEmpty()) ||
            eventDetailsDTO.getRecurrenceMonthlyDay() != null;

        if (Boolean.TRUE.equals(isRecurringFromDto) || hasRecurrenceFieldsInDto) {
            // Populate from DTO fields
            populateRecurrenceColumnsFromDto(eventDetails, eventDetailsDTO);
            log.debug(
                "Populated recurrence columns from DTO fields for event: {} (isRecurring={}, hasFields={})",
                eventDetails.getId(),
                isRecurringFromDto,
                hasRecurrenceFieldsInDto
            );
        } else {
            // Priority 2: Fallback to eventRecurrenceMetadata parsing
            processRecurrenceFromEventRecurrenceMetadata(eventDetails);
        }

        // If still not recurring after both attempts, clear recurrence columns
        if (!Boolean.TRUE.equals(eventDetails.getIsRecurring())) {
            clearRecurrenceColumns(eventDetails);
        }
    }

    /**
     * Populate event_details recurrence columns from EventDetailsDTO fields (PRIMARY SOURCE).
     *
     * @param eventDetails the event details entity
     * @param dto the DTO containing recurrence fields
     */
    private void populateRecurrenceColumnsFromDto(EventDetails eventDetails, EventDetailsDTO dto) {
        // Validate recurrence configuration
        validateRecurrenceConfiguration(dto);

        // CRITICAL FIX: Set is_recurring flag from DTO (default to true if not explicitly set)
        eventDetails.setIsRecurring(dto.getIsRecurring() != null ? dto.getIsRecurring() : true);

        // Set pattern from DTO
        if (dto.getRecurrencePattern() != null) {
            eventDetails.setRecurrencePattern(dto.getRecurrencePattern());
        }

        // Set interval from DTO (default to 1 if not provided)
        eventDetails.setRecurrenceInterval(dto.getRecurrenceInterval() != null ? dto.getRecurrenceInterval() : 1);

        // Set end type from DTO
        if (dto.getRecurrenceEndType() != null) {
            eventDetails.setRecurrenceEndType(dto.getRecurrenceEndType());
        }

        // Handle endDate from DTO - Always set from DTO (even if null, to clear value)
        eventDetails.setRecurrenceEndDate(dto.getRecurrenceEndDate());

        // Handle occurrences from DTO - Always set from DTO (even if null, to clear value)
        Integer occurrencesFromDto = dto.getRecurrenceOccurrences();
        eventDetails.setRecurrenceOccurrences(occurrencesFromDto);
        log.debug("Setting recurrenceOccurrences from DTO: {} (was: {})", occurrencesFromDto, eventDetails.getRecurrenceOccurrences());

        // Handle weeklyDays from DTO - Convert List<Integer> to Integer[] for PostgreSQL integer[] column
        // Always set from DTO (even if null/empty, to clear value)
        if (dto.getRecurrenceWeeklyDays() != null && !dto.getRecurrenceWeeklyDays().isEmpty()) {
            eventDetails.setRecurrenceWeeklyDays(dto.getRecurrenceWeeklyDays().toArray(new Integer[0]));
        } else {
            eventDetails.setRecurrenceWeeklyDays(null);
        }

        // Handle monthlyDay from DTO - Always set from DTO (even if null, to clear value)
        eventDetails.setRecurrenceMonthlyDay(dto.getRecurrenceMonthlyDay());

        // CRITICAL FIX: Set recurrenceSeriesId to parent event ID (will be set after save when we have the ID)
        // Set parent event ID to null for parent events (handled in save/update methods)
        if (eventDetails.getParentEvent() == null) {
            eventDetails.setParentEvent(null);
        }
    }

    /**
     * Validate recurrence configuration.
     *
     * @param dto the DTO containing recurrence fields
     * @throws IllegalArgumentException if validation fails
     */
    private void validateRecurrenceConfiguration(EventDetailsDTO dto) {
        if (!Boolean.TRUE.equals(dto.getIsRecurring())) {
            return; // Not recurring, no validation needed
        }

        // Pattern required
        if (dto.getRecurrencePattern() == null) {
            throw new IllegalArgumentException("Recurrence pattern is required for recurring events");
        }

        // Interval must be > 0
        if (dto.getRecurrenceInterval() != null && dto.getRecurrenceInterval() <= 0) {
            throw new IllegalArgumentException("Recurrence interval must be greater than 0");
        }

        // End type validation
        if (dto.getRecurrenceEndType() == null) {
            throw new IllegalArgumentException("Recurrence end type is required for recurring events");
        }

        if (dto.getRecurrenceEndType() == RecurrenceEndType.END_DATE && dto.getRecurrenceEndDate() == null) {
            throw new IllegalArgumentException("End date is required for END_DATE end type");
        }

        if (dto.getRecurrenceEndType() == RecurrenceEndType.OCCURRENCES && dto.getRecurrenceOccurrences() == null) {
            throw new IllegalArgumentException("Occurrences count is required for OCCURRENCES end type");
        }

        // End date validation (if provided)
        if (dto.getRecurrenceEndDate() != null && dto.getStartDate() != null) {
            LocalDate maxEndDate = dto.getStartDate().plusYears(5);
            if (dto.getRecurrenceEndDate().isAfter(maxEndDate)) {
                throw new IllegalArgumentException("Recurrence end date cannot be more than 5 years from start date");
            }
        }

        // Occurrences limit
        if (dto.getRecurrenceOccurrences() != null && dto.getRecurrenceOccurrences() > 1000) {
            throw new IllegalArgumentException("Maximum 1000 occurrences allowed");
        }

        // Weekly days required for WEEKLY/BIWEEKLY
        if (
            (dto.getRecurrencePattern() == RecurrencePattern.WEEKLY || dto.getRecurrencePattern() == RecurrencePattern.BIWEEKLY) &&
            (dto.getRecurrenceWeeklyDays() == null || dto.getRecurrenceWeeklyDays().isEmpty())
        ) {
            throw new IllegalArgumentException("At least one weekday must be selected for weekly recurrence");
        }
    }

    /**
     * Process recurrence configuration from eventRecurrenceMetadata field.
     * This method parses the eventRecurrenceMetadata JSON and extracts
     * recurrence configuration to populate the dedicated event_details recurrence columns.
     *
     * @param eventDetails the event details entity to process
     */
    private void processRecurrenceFromEventRecurrenceMetadata(EventDetails eventDetails) {
        String recurrenceMetadata = eventDetails.getEventRecurrenceMetadata();
        if (recurrenceMetadata == null || recurrenceMetadata.isEmpty()) {
            // Fallback to old metadata field for backward compatibility
            processRecurrenceFromOldMetadata(eventDetails);
            return;
        }

        try {
            Map<String, Object> recurrenceConfig = objectMapper.readValue(recurrenceMetadata, Map.class);
            if (recurrenceConfig != null) {
                processRecurrenceConfigFromMap(eventDetails, recurrenceConfig);
                log.debug("Populated recurrence columns from eventRecurrenceMetadata for event: {}", eventDetails.getId());
            }
        } catch (Exception e) {
            log.error("Failed to parse eventRecurrenceMetadata for event: {}", eventDetails.getId(), e);
            // Don't throw exception - allow event to be saved without recurrence
        }
    }

    /**
     * Process recurrence configuration from old metadata field (backward compatibility).
     *
     * @param eventDetails the event details entity to process
     */
    private void processRecurrenceFromOldMetadata(EventDetails eventDetails) {
        String metadata = eventDetails.getMetadata();
        if (metadata == null || metadata.isEmpty()) {
            return;
        }

        try {
            Map<String, Object> metadataMap = eventDetails.getMetadataAsMap();
            if (metadataMap == null || metadataMap.isEmpty()) {
                return;
            }

            Boolean isRecurring = (Boolean) metadataMap.get("isRecurring");
            if (Boolean.TRUE.equals(isRecurring)) {
                @SuppressWarnings("unchecked")
                Map<String, Object> recurrenceConfig = (Map<String, Object>) metadataMap.get("recurrenceConfig");
                if (recurrenceConfig != null) {
                    processRecurrenceConfigFromMap(eventDetails, recurrenceConfig);
                    log.debug(
                        "Populated recurrence columns from old metadata (backward compatibility) for event: {}",
                        eventDetails.getId()
                    );
                }
            }
        } catch (Exception e) {
            log.error("Failed to parse recurrence from old metadata for event: {}", eventDetails.getId(), e);
            // Don't throw exception - allow event to be saved without recurrence
        }
    }

    /**
     * Process recurrence configuration from a map and populate recurrence columns.
     * CRITICAL FIX: Properly extract and update all recurrence fields including occurrences.
     *
     * @param eventDetails the event details entity
     * @param recurrenceConfig the recurrence configuration map
     */
    private void processRecurrenceConfigFromMap(EventDetails eventDetails, Map<String, Object> recurrenceConfig) {
        // Set is_recurring flag
        eventDetails.setIsRecurring(true);

        // Set pattern
        String patternStr = (String) recurrenceConfig.get("pattern");
        if (patternStr != null) {
            try {
                eventDetails.setRecurrencePattern(RecurrencePattern.valueOf(patternStr));
            } catch (IllegalArgumentException e) {
                log.warn("Invalid recurrence pattern: {}", patternStr);
            }
        }

        // CRITICAL FIX: Ensure interval is correctly extracted
        Object intervalObj = recurrenceConfig.get("interval");
        if (intervalObj != null) {
            if (intervalObj instanceof Number) {
                eventDetails.setRecurrenceInterval(((Number) intervalObj).intValue());
            } else {
                try {
                    eventDetails.setRecurrenceInterval(Integer.parseInt(intervalObj.toString()));
                } catch (NumberFormatException e) {
                    log.warn("Failed to parse interval: {}", intervalObj);
                }
            }
        } else {
            eventDetails.setRecurrenceInterval(1); // Default to 1 if not provided
        }

        // Set end type
        String endTypeStr = (String) recurrenceConfig.get("endType");
        if (endTypeStr != null) {
            try {
                eventDetails.setRecurrenceEndType(RecurrenceEndType.valueOf(endTypeStr));
            } catch (IllegalArgumentException e) {
                log.warn("Invalid recurrence end type: {}", endTypeStr);
            }
        }

        // Handle endDate
        if (recurrenceConfig.containsKey("endDate")) {
            Object endDateObj = recurrenceConfig.get("endDate");
            if (endDateObj instanceof String) {
                try {
                    eventDetails.setRecurrenceEndDate(LocalDate.parse((String) endDateObj));
                } catch (Exception e) {
                    log.warn("Failed to parse endDate: {}", endDateObj, e);
                }
            }
        }

        // CRITICAL FIX: Ensure occurrences is correctly extracted and updated
        Object occurrencesObj = recurrenceConfig.get("occurrences");
        if (occurrencesObj != null) {
            if (occurrencesObj instanceof Number) {
                eventDetails.setRecurrenceOccurrences(((Number) occurrencesObj).intValue());
            } else {
                try {
                    eventDetails.setRecurrenceOccurrences(Integer.parseInt(occurrencesObj.toString()));
                } catch (NumberFormatException e) {
                    log.warn("Failed to parse occurrences: {}", occurrencesObj);
                }
            }
        }

        // Handle weeklyDays - CRITICAL: recurrence_weekly_days is integer[], convert List<Integer> to Integer[]
        if (recurrenceConfig.containsKey("weeklyDays")) {
            @SuppressWarnings("unchecked")
            List<Integer> weeklyDaysList = (List<Integer>) recurrenceConfig.get("weeklyDays");
            if (weeklyDaysList != null && !weeklyDaysList.isEmpty()) {
                // Convert List<Integer> to Integer[] for PostgreSQL integer[] column
                eventDetails.setRecurrenceWeeklyDays(weeklyDaysList.toArray(new Integer[0]));
            }
        }

        // Handle monthlyDay
        if (recurrenceConfig.containsKey("monthlyDay")) {
            Object monthlyDayObj = recurrenceConfig.get("monthlyDay");
            if (monthlyDayObj instanceof Number) {
                eventDetails.setRecurrenceMonthlyDay(((Number) monthlyDayObj).intValue());
            } else if ("LAST".equals(monthlyDayObj)) {
                eventDetails.setRecurrenceMonthlyDay(null); // NULL means last day of month
            }
        }
    }

    /**
     * Clear all recurrence columns when event is not recurring.
     *
     * @param eventDetails the event details entity
     */
    private void clearRecurrenceColumns(EventDetails eventDetails) {
        eventDetails.setIsRecurring(false);
        eventDetails.setRecurrencePattern(null);
        eventDetails.setRecurrenceInterval(null);
        eventDetails.setRecurrenceEndType(null);
        eventDetails.setRecurrenceEndDate(null);
        eventDetails.setRecurrenceOccurrences(null);
        eventDetails.setRecurrenceWeeklyDays(null);
        eventDetails.setRecurrenceMonthlyDay(null);
        // Clear recurrenceSeriesId only if this is a parent event (not a child)
        if (eventDetails.getParentEvent() == null) {
            eventDetails.setRecurrenceSeriesId(null);
        }
        // Clear eventRecurrenceMetadata
        eventDetails.setEventRecurrenceMetadata(null);
        // Don't clear parentEventId - it may be needed for existing child events
    }

    /**
     * Create or update event_recurrence_series record for a recurring event.
     *
     * @param eventDetails the parent event details entity
     * @param eventDetailsDTO the DTO containing recurrence fields (for additional context if needed)
     */
    private void createOrUpdateRecurrenceSeries(EventDetails eventDetails, EventDetailsDTO eventDetailsDTO) {
        if (!Boolean.TRUE.equals(eventDetails.getIsRecurring()) || eventDetails.getParentEvent() != null) {
            return; // Only create series for parent recurring events
        }

        try {
            EventRecurrenceSeries series = recurrenceSeriesRepository
                .findByParentEventId(eventDetails.getId())
                .orElse(new EventRecurrenceSeries());

            // Set parent event
            series.setParentEvent(eventDetails);

            // Set tenant ID
            series.setTenantId(eventDetails.getTenantId());

            // Set pattern
            if (eventDetails.getRecurrencePattern() != null) {
                series.setPattern(eventDetails.getRecurrencePattern().name());
            }

            // Set interval
            series.setInterval(eventDetails.getRecurrenceInterval() != null ? eventDetails.getRecurrenceInterval() : 1);

            // Set end type
            if (eventDetails.getRecurrenceEndType() != null) {
                series.setEndType(eventDetails.getRecurrenceEndType().name());
            }

            // Set end date
            series.setEndDate(eventDetails.getRecurrenceEndDate());

            // Set occurrences
            series.setOccurrences(eventDetails.getRecurrenceOccurrences());

            // Handle weeklyDays - event_recurrence_series.weekly_days is INTEGER[] (PostgreSQL array)
            if (eventDetails.getRecurrenceWeeklyDays() != null && eventDetails.getRecurrenceWeeklyDays().length > 0) {
                series.setWeeklyDays(eventDetails.getRecurrenceWeeklyDays());
            } else {
                series.setWeeklyDays(null);
            }

            // Set monthly day
            series.setMonthlyDay(eventDetails.getRecurrenceMonthlyDay());

            // Set timestamps
            ZonedDateTime now = ZonedDateTime.now();
            if (series.getId() == null) {
                series.setCreatedAt(now);
            }
            series.setUpdatedAt(now);

            recurrenceSeriesRepository.save(series);
            log.debug("Created/updated recurrence series for parent event: {}", eventDetails.getId());
        } catch (Exception e) {
            log.error("Failed to create/update recurrence series for event: {}", eventDetails.getId(), e);
            // Don't throw exception - allow event to be saved without series record
        }
    }
}
