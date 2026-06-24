package com.eventsitemanager.domain.enumeration;

/**
 * The RecurrencePattern enumeration.
 * Defines the recurrence patterns for recurring events.
 *
 * <p>Valid values:</p>
 * <ul>
 *   <li>{@code DAILY} - Event occurs every day</li>
 *   <li>{@code WEEKLY} - Event occurs weekly on specified days</li>
 *   <li>{@code BIWEEKLY} - Event occurs every two weeks</li>
 *   <li>{@code MONTHLY} - Event occurs monthly on specified day</li>
 * </ul>
 */
public enum RecurrencePattern {
    /** Event occurs every day */
    DAILY,

    /** Event occurs weekly on specified days */
    WEEKLY,

    /** Event occurs every two weeks */
    BIWEEKLY,

    /** Event occurs monthly on specified day */
    MONTHLY,
}
