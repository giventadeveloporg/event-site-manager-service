package com.nextjstemplate.service;

import com.nextjstemplate.domain.enumeration.RecurrenceEndType;
import com.nextjstemplate.domain.enumeration.RecurrencePattern;
import com.nextjstemplate.service.dto.RecurrenceConfig;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for calculating recurrence occurrence dates.
 */
public interface RecurrenceCalculationService {
    /**
     * Calculate all occurrence dates for a recurring event.
     *
     * @param startDate the start date of the recurring event
     * @param recurrenceConfig the recurrence configuration
     * @return list of occurrence dates
     */
    List<LocalDate> calculateOccurrences(LocalDate startDate, RecurrenceConfig recurrenceConfig);

    /**
     * Calculate the next occurrence date after a given date.
     *
     * @param startDate the start date of the recurring event
     * @param recurrenceConfig the recurrence configuration
     * @param currentDate the current date to find the next occurrence after
     * @return the next occurrence date, or null if no more occurrences
     */
    LocalDate calculateNextOccurrence(LocalDate startDate, RecurrenceConfig recurrenceConfig, LocalDate currentDate);

    /**
     * Validate that the recurrence end date is within acceptable limits.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return true if valid, false otherwise
     */
    boolean validateRecurrenceEndDate(LocalDate startDate, LocalDate endDate);
}
