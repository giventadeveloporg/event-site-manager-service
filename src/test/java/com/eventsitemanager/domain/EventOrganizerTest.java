package com.eventsitemanager.domain;

import static com.eventsitemanager.domain.EventDetailsTestSamples.*;
import static com.eventsitemanager.domain.EventOrganizerTestSamples.*;
import static com.eventsitemanager.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventOrganizerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventOrganizer.class);
        EventOrganizer eventOrganizer1 = getEventOrganizerSample1();
        EventOrganizer eventOrganizer2 = new EventOrganizer();
        assertThat(eventOrganizer1).isNotEqualTo(eventOrganizer2);

        eventOrganizer2.setId(eventOrganizer1.getId());
        assertThat(eventOrganizer1).isEqualTo(eventOrganizer2);

        eventOrganizer2 = getEventOrganizerSample2();
        assertThat(eventOrganizer1).isNotEqualTo(eventOrganizer2);
    }

    @Test
    void eventTest() throws Exception {
        EventOrganizer eventOrganizer = getEventOrganizerRandomSampleGenerator();
        EventDetails eventDetailsBack = getEventDetailsRandomSampleGenerator();

        eventOrganizer.setEvent(eventDetailsBack);
        assertThat(eventOrganizer.getEvent()).isEqualTo(eventDetailsBack);

        eventOrganizer.event(null);
        assertThat(eventOrganizer.getEvent()).isNull();
    }

    @Test
    void organizerTest() throws Exception {
        EventOrganizer eventOrganizer = getEventOrganizerRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        eventOrganizer.setOrganizer(userProfileBack);
        assertThat(eventOrganizer.getOrganizer()).isEqualTo(userProfileBack);

        eventOrganizer.organizer(null);
        assertThat(eventOrganizer.getOrganizer()).isNull();
    }
}
