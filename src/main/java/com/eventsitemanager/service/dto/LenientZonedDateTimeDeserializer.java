package com.eventsitemanager.service.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Custom deserializer for ZonedDateTime that handles multiple ISO 8601 formats,
 * including formats with 'Z' timezone indicator and formats with timezone offsets.
 */
public class LenientZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {

    private static final DateTimeFormatter[] FORMATTERS = {
        DateTimeFormatter.ISO_ZONED_DATE_TIME, // Handles most ISO formats including Z
        DateTimeFormatter.ISO_OFFSET_DATE_TIME, // Handles offset formats
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"), // With milliseconds and offset
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"), // Without milliseconds, with offset
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"), // With milliseconds and Z
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"), // Without milliseconds, with Z
    };

    @Override
    public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateString = p.getText();
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        // Try each formatter until one works
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return ZonedDateTime.parse(dateString, formatter);
            } catch (DateTimeParseException e) {
                // Try next formatter
            }
        }

        // If all formatters fail, try parsing as Instant and converting to ZonedDateTime
        try {
            return ZonedDateTime.parse(dateString);
        } catch (DateTimeParseException e) {
            throw new IOException("Unable to parse date: " + dateString, e);
        }
    }
}
