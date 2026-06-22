package com.eventsitemanager.domain;

import static com.eventsitemanager.domain.EventDetailsTestSamples.*;
import static com.eventsitemanager.domain.EventTypeDetailsTestSamples.*;
import static com.eventsitemanager.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventDetails.class);
        EventDetails eventDetails1 = getEventDetailsSample1();
        EventDetails eventDetails2 = new EventDetails();
        assertThat(eventDetails1).isNotEqualTo(eventDetails2);

        eventDetails2.setId(eventDetails1.getId());
        assertThat(eventDetails1).isEqualTo(eventDetails2);

        eventDetails2 = getEventDetailsSample2();
        assertThat(eventDetails1).isNotEqualTo(eventDetails2);
    }

    @Test
    void createdByTest() throws Exception {
        EventDetails eventDetails = getEventDetailsRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        eventDetails.setCreatedBy(userProfileBack);
        assertThat(eventDetails.getCreatedBy()).isEqualTo(userProfileBack);

        eventDetails.createdBy(null);
        assertThat(eventDetails.getCreatedBy()).isNull();
    }

    @Test
    void eventTypeTest() throws Exception {
        EventDetails eventDetails = getEventDetailsRandomSampleGenerator();
        EventTypeDetails eventTypeDetailsBack = getEventTypeDetailsRandomSampleGenerator();

        eventDetails.setEventType(eventTypeDetailsBack);
        assertThat(eventDetails.getEventType()).isEqualTo(eventTypeDetailsBack);

        eventDetails.eventType(null);
        assertThat(eventDetails.getEventType()).isNull();
    }
}
