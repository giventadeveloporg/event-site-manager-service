package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.enumeration.RecurrenceEndType;
import com.nextjstemplate.domain.enumeration.RecurrencePattern;
import com.nextjstemplate.service.RecurrenceCalculationService;
import com.nextjstemplate.service.dto.RecurrenceConfig;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service implementation for calculating recurrence occurrence dates.
 */
@Service
public class RecurrenceCalculationServiceImpl implements RecurrenceCalculationService {

    private static final Logger log = LoggerFactory.getLogger(RecurrenceCalculationServiceImpl.class);

    private static final int MAX_YEARS_FROM_START = 5;
    private static final int MAX_OCCURRENCES = 1000;

    @Override
    public List<LocalDate> calculateOccurrences(LocalDate startDate, RecurrenceConfig recurrenceConfig) {
        log.debug("Calculating occurrences for startDate: {}, config: {}", startDate, recurrenceConfig);

        if (recurrenceConfig == null || recurrenceConfig.getPattern() == null) {
            log.warn("Invalid recurrence config provided");
            return new ArrayList<>();
        }

        List<LocalDate> occurrences = new ArrayList<>();
        occurrences.add(startDate); // Include the start date as first occurrence

        LocalDate currentDate = startDate;
        int occurrenceCount = 1;
        LocalDate maxEndDate = startDate.plusYears(MAX_YEARS_FROM_START);

        // Determine end condition
        LocalDate endDate = null;
        Integer maxOccurrences = null;

        if (recurrenceConfig.getEndType() == RecurrenceEndType.END_DATE) {
            endDate = recurrenceConfig.getEndDate();
            if (endDate != null && !validateRecurrenceEndDate(startDate, endDate)) {
                log.warn("End date {} exceeds maximum allowed duration from start date {}", endDate, startDate);
                endDate = maxEndDate;
            }
        } else if (recurrenceConfig.getEndType() == RecurrenceEndType.OCCURRENCES) {
            maxOccurrences = recurrenceConfig.getOccurrences();
            if (maxOccurrences != null && maxOccurrences > MAX_OCCURRENCES) {
                log.warn("Occurrences {} exceeds maximum allowed {}, capping to {}", maxOccurrences, MAX_OCCURRENCES, MAX_OCCURRENCES);
                maxOccurrences = MAX_OCCURRENCES;
            }
        }

        // Calculate occurrences based on pattern
        while (occurrenceCount < (maxOccurrences != null ? maxOccurrences : MAX_OCCURRENCES)) {
            LocalDate nextDate = calculateNextOccurrence(startDate, recurrenceConfig, currentDate);

            if (nextDate == null) {
                break;
            }

            // Check end date constraint
            if (endDate != null && nextDate.isAfter(endDate)) {
                break;
            }

            // Check max date constraint (5 years from start)
            if (nextDate.isAfter(maxEndDate)) {
                break;
            }

            occurrences.add(nextDate);
            currentDate = nextDate;
            occurrenceCount++;
        }

        log.debug("Calculated {} occurrences", occurrences.size());
        return occurrences;
    }

    @Override
    public LocalDate calculateNextOccurrence(LocalDate startDate, RecurrenceConfig recurrenceConfig, LocalDate currentDate) {
        if (recurrenceConfig == null || recurrenceConfig.getPattern() == null) {
            return null;
        }

        int interval = recurrenceConfig.getInterval() != null ? recurrenceConfig.getInterval() : 1;
        RecurrencePattern pattern = recurrenceConfig.getPattern();

        switch (pattern) {
            case DAILY:
                return currentDate.plusDays(interval);
            case WEEKLY:
                return calculateNextWeeklyOccurrence(currentDate, interval, recurrenceConfig.getWeeklyDays());
            case BIWEEKLY:
                return calculateNextWeeklyOccurrence(currentDate, 2, recurrenceConfig.getWeeklyDays());
            case MONTHLY:
                return calculateNextMonthlyOccurrence(currentDate, interval, recurrenceConfig.getMonthlyDay());
            default:
                log.warn("Unknown recurrence pattern: {}", pattern);
                return null;
        }
    }

    @Override
    public boolean validateRecurrenceEndDate(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return false;
        }
        LocalDate maxEndDate = startDate.plusYears(MAX_YEARS_FROM_START);
        return !endDate.isAfter(maxEndDate);
    }

    /**
     * Calculate next weekly occurrence.
     * Handles WEEKLY and BIWEEKLY patterns.
     */
    private LocalDate calculateNextWeeklyOccurrence(LocalDate currentDate, int weeksInterval, List<Integer> weeklyDays) {
        if (weeklyDays == null || weeklyDays.isEmpty()) {
            // If no days specified, default to same day of week
            return currentDate.plusWeeks(weeksInterval);
        }

        // Convert to DayOfWeek enum (1=Monday, 7=Sunday)
        List<DayOfWeek> targetDays = weeklyDays
            .stream()
            .map(day -> {
                // Convert: 1=Monday -> DayOfWeek.MONDAY, 7=Sunday -> DayOfWeek.SUNDAY
                if (day == 7) {
                    return DayOfWeek.SUNDAY;
                }
                return DayOfWeek.of(day);
            })
            .collect(Collectors.toList());

        LocalDate nextDate = currentDate.plusWeeks(weeksInterval);
        DayOfWeek nextDayOfWeek = nextDate.getDayOfWeek();

        // If the next date falls on one of the target days, return it
        if (targetDays.contains(nextDayOfWeek)) {
            return nextDate;
        }

        // Otherwise, find the next target day within the same week
        for (int i = 1; i <= 7; i++) {
            LocalDate candidate = nextDate.plusDays(i);
            if (targetDays.contains(candidate.getDayOfWeek())) {
                return candidate;
            }
        }

        // Fallback: return next week's first target day
        return findNextTargetDay(nextDate, targetDays);
    }

    /**
     * Find the next occurrence of any target day.
     */
    private LocalDate findNextTargetDay(LocalDate startDate, List<DayOfWeek> targetDays) {
        for (int i = 0; i < 7; i++) {
            LocalDate candidate = startDate.plusDays(i);
            if (targetDays.contains(candidate.getDayOfWeek())) {
                return candidate;
            }
        }
        // Should never reach here, but return startDate + 7 days as fallback
        return startDate.plusDays(7);
    }

    /**
     * Calculate next monthly occurrence.
     * Handles edge cases like month-end dates (Jan 31 -> Feb 28/29).
     */
    private LocalDate calculateNextMonthlyOccurrence(LocalDate currentDate, int monthsInterval, Integer monthlyDay) {
        LocalDate nextDate = currentDate.plusMonths(monthsInterval);

        if (monthlyDay == null) {
            // If no day specified, use same day of month
            return adjustToValidMonthDay(nextDate, currentDate.getDayOfMonth());
        }

        return adjustToValidMonthDay(nextDate, monthlyDay);
    }

    /**
     * Adjust date to valid day of month.
     * If the target day doesn't exist in the month (e.g., Feb 31), use the last day of the month.
     */
    private LocalDate adjustToValidMonthDay(LocalDate date, int targetDay) {
        int lastDayOfMonth = date.lengthOfMonth();

        if (targetDay > lastDayOfMonth) {
            // Use last day of month
            return date.withDayOfMonth(lastDayOfMonth);
        }

        return date.withDayOfMonth(targetDay);
    }
}
