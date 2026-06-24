package com.eventsitemanager.service.dto;

import com.eventsitemanager.domain.enumeration.RecurrenceEndType;
import com.eventsitemanager.domain.enumeration.RecurrencePattern;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for recurrence configuration.
 * Used to configure recurring event patterns and end conditions.
 */
public class RecurrenceConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private RecurrencePattern pattern;

    @Min(value = 1)
    private Integer interval = 1;

    @NotNull
    private RecurrenceEndType endType;

    private LocalDate endDate;

    @Min(value = 1)
    @Max(value = 1000)
    private Integer occurrences;

    /**
     * Days of week for WEEKLY/BIWEEKLY patterns.
     * Values: 1 = Monday, 2 = Tuesday, ..., 7 = Sunday
     */
    private List<Integer> weeklyDays;

    /**
     * Day of month for MONTHLY pattern (1-31).
     * For months with fewer days, uses the last valid day.
     */
    @Min(value = 1)
    @Max(value = 31)
    private Integer monthlyDay;

    public RecurrencePattern getPattern() {
        return pattern;
    }

    public void setPattern(RecurrencePattern pattern) {
        this.pattern = pattern;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public RecurrenceEndType getEndType() {
        return endType;
    }

    public void setEndType(RecurrenceEndType endType) {
        this.endType = endType;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(Integer occurrences) {
        this.occurrences = occurrences;
    }

    public List<Integer> getWeeklyDays() {
        return weeklyDays;
    }

    public void setWeeklyDays(List<Integer> weeklyDays) {
        this.weeklyDays = weeklyDays;
    }

    public Integer getMonthlyDay() {
        return monthlyDay;
    }

    public void setMonthlyDay(Integer monthlyDay) {
        this.monthlyDay = monthlyDay;
    }

    @Override
    public String toString() {
        return (
            "RecurrenceConfig{" +
            "pattern=" +
            pattern +
            ", interval=" +
            interval +
            ", endType=" +
            endType +
            ", endDate=" +
            endDate +
            ", occurrences=" +
            occurrences +
            ", weeklyDays=" +
            weeklyDays +
            ", monthlyDay=" +
            monthlyDay +
            "}"
        );
    }
}
