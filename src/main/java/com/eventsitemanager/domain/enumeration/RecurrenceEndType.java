package com.eventsitemanager.domain.enumeration;

/**
 * The RecurrenceEndType enumeration.
 * Defines how a recurring event series ends.
 *
 * <p>Valid values:</p>
 * <ul>
 *   <li>{@code END_DATE} - Series ends on a specific date</li>
 *   <li>{@code OCCURRENCES} - Series ends after a specific number of occurrences</li>
 * </ul>
 */
public enum RecurrenceEndType {
    /** Series ends on a specific date */
    END_DATE,

    /** Series ends after a specific number of occurrences */
    OCCURRENCES,
}
