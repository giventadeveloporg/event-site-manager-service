package com.nextjstemplate.service.dto;

/**
 * Constants for recipient types in batch email jobs.
 */
public final class RecipientType {

    /**
     * Recipient type for event attendees/registrants.
     * Source: EventAttendee table with registrationStatus = 'CONFIRMED'
     */
    public static final String EVENT_ATTENDEES = "EVENT_ATTENDEES";

    /**
     * Recipient type for subscribed members.
     * Source: UserProfile table with isEmailSubscribed = true
     */
    public static final String SUBSCRIBED_MEMBERS = "SUBSCRIBED_MEMBERS";

    private RecipientType() {
        // Utility class - prevent instantiation
    }
}
