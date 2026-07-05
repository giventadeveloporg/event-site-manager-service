package com.eventsitemanager.service.validation;

import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.service.dto.EventDetailsDTO;

/**
 * Truncates and strips control characters from event text fields before persist and on read
 * (legacy rows that bypassed API validation via direct DB import).
 */
public final class EventDetailsTextSanitizer {

    public static final int DESCRIPTION_MAX = 900;
    public static final int CAPTION_MAX = 255;
    public static final int DIRECTIONS_MAX = 600;

    private EventDetailsTextSanitizer() {}

    public static void applyToEntity(EventDetails entity) {
        if (entity == null) {
            return;
        }
        entity.setDescription(sanitizeDescription(entity.getDescription()));
        entity.setCaption(sanitizeCaption(entity.getCaption()));
        entity.setDirectionsToVenue(sanitizeDirectionsToVenue(entity.getDirectionsToVenue()));
    }

    public static void applyToDto(EventDetailsDTO dto) {
        if (dto == null) {
            return;
        }
        dto.setDescription(sanitizeDescription(dto.getDescription()));
        dto.setCaption(sanitizeCaption(dto.getCaption()));
        dto.setDirectionsToVenue(sanitizeDirectionsToVenue(dto.getDirectionsToVenue()));
    }

    public static String sanitizeDescription(String description) {
        return sanitizeText(description, DESCRIPTION_MAX);
    }

    public static String sanitizeCaption(String caption) {
        return sanitizeText(caption, CAPTION_MAX);
    }

    public static String sanitizeDirectionsToVenue(String directions) {
        return sanitizeText(directions, DIRECTIONS_MAX);
    }

    static String sanitizeText(String value, int maxLength) {
        if (value == null || value.isBlank()) {
            return value;
        }
        if (value.length() <= maxLength && !hasDisallowedControlChar(value, maxLength)) {
            return value;
        }
        int cut = maxLength;
        for (int i = 0; i < Math.min(value.length(), maxLength); i++) {
            if (isDisallowedControlChar(value.charAt(i))) {
                cut = Math.min(cut, i);
                break;
            }
        }
        return value.substring(0, cut).stripTrailing();
    }

    private static boolean hasDisallowedControlChar(String value, int maxLength) {
        for (int i = 0; i < Math.min(value.length(), maxLength); i++) {
            if (isDisallowedControlChar(value.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isDisallowedControlChar(char c) {
        return c < 9 || (c > 13 && c < 32) || c > 126;
    }
}
