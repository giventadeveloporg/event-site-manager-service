package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.EventAttendeeTestSamples.*;
import static com.nextjstemplate.domain.EventDetailsTestSamples.*;
import static com.nextjstemplate.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventAttendeeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventAttendee.class);
        EventAttendee eventAttendee1 = getEventAttendeeSample1();
        EventAttendee eventAttendee2 = new EventAttendee();
        assertThat(eventAttendee1).isNotEqualTo(eventAttendee2);

        eventAttendee2.setId(eventAttendee1.getId());
        assertThat(eventAttendee1).isEqualTo(eventAttendee2);

        eventAttendee2 = getEventAttendeeSample2();
        assertThat(eventAttendee1).isNotEqualTo(eventAttendee2);
    }
    /* @Test
    void eventTest() throws Exception {
        EventAttendee eventAttendee = getEventAttendeeRandomSampleGenerator();
        EventDetails eventDetailsBack = getEventDetailsRandomSampleGenerator();

        eventAttendee.setEvent(eventDetailsBack);
        assertThat(eventAttendee.getEvent()).isEqualTo(eventDetailsBack);

        eventAttendee.event(null);
        assertThat(eventAttendee.getEvent()).isNull();
    }

    @Test
    void attendeeTest() throws Exception {
        EventAttendee eventAttendee = getEventAttendeeRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        eventAttendee.setAttendee(userProfileBack);
        assertThat(eventAttendee.getAttendee()).isEqualTo(userProfileBack);

        eventAttendee.attendee(null);
        assertThat(eventAttendee.getAttendee()).isNull();
    }*/
}
