package com.eventsitemanager.service.dto;

/**
 * Result of create-or-update registration: one attendee per event+email+tenant.
 * Used by the event registration flow to return 201 Created vs 200 OK.
 */
public record EventAttendeeSaveResult(EventAttendeeDTO dto, boolean created) {}
