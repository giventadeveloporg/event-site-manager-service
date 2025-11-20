package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.EventDetailsDTO;
import com.nextjstemplate.service.dto.RecurrenceConfig;
import java.util.List;

/**
 * Service for generating and managing recurring events.
 */
public interface RecurringEventGenerationService {
    /**
     * Generate child events for a recurring event series.
     *
     * @param parentEventId the parent event ID
     * @return list of generated child event DTOs
     */
    List<EventDetailsDTO> generateRecurringEvents(Long parentEventId);

    /**
     * Update recurrence configuration and optionally regenerate events.
     *
     * @param parentEventId the parent event ID
     * @param newConfig the new recurrence configuration
     * @param regenerateEvents whether to regenerate all child events
     */
    void updateRecurringSeries(Long parentEventId, RecurrenceConfig newConfig, boolean regenerateEvents);

    /**
     * Delete a recurring event series (parent and all child events).
     *
     * @param parentEventId the parent event ID
     */
    void deleteRecurringSeries(Long parentEventId);

    /**
     * Get all events in a recurrence series.
     *
     * @param parentEventId the parent event ID
     * @return list of events in the series (parent + children)
     */
    List<EventDetailsDTO> getRecurringSeries(Long parentEventId);
}
