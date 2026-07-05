package com.eventsitemanager.service.validation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EventDetailsTextSanitizerTest {

    @Test
    void sanitizeDescription_truncatesAt900() {
        String input = "a".repeat(1000);
        assertThat(EventDetailsTextSanitizer.sanitizeDescription(input)).hasSize(900);
    }

    @Test
    void sanitizeDescription_stripsAtFirstControlChar() {
        String input = "Hello" + (char) 1 + "world".repeat(200);
        String result = EventDetailsTextSanitizer.sanitizeDescription(input);
        assertThat(result).isEqualTo("Hello");
    }

    @Test
    void sanitizeDirectionsToVenue_truncatesAt600() {
        assertThat(EventDetailsTextSanitizer.sanitizeDirectionsToVenue("x".repeat(700))).hasSize(600);
    }

    @Test
    void sanitizeCaption_truncatesAt255() {
        assertThat(EventDetailsTextSanitizer.sanitizeCaption("y".repeat(300))).hasSize(255);
    }
}
