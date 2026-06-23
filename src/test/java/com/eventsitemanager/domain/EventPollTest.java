package com.eventsitemanager.domain;

import static com.eventsitemanager.domain.EventDetailsTestSamples.*;
import static com.eventsitemanager.domain.EventPollTestSamples.*;
import static com.eventsitemanager.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventPollTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventPoll.class);
        EventPoll eventPoll1 = getEventPollSample1();
        EventPoll eventPoll2 = new EventPoll();
        assertThat(eventPoll1).isNotEqualTo(eventPoll2);

        eventPoll2.setId(eventPoll1.getId());
        assertThat(eventPoll1).isEqualTo(eventPoll2);

        eventPoll2 = getEventPollSample2();
        assertThat(eventPoll1).isNotEqualTo(eventPoll2);
    }

    @Test
    void eventTest() throws Exception {
        EventPoll eventPoll = getEventPollRandomSampleGenerator();
        EventDetails eventDetailsBack = getEventDetailsRandomSampleGenerator();

        eventPoll.setEvent(eventDetailsBack);
        assertThat(eventPoll.getEvent()).isEqualTo(eventDetailsBack);

        eventPoll.event(null);
        assertThat(eventPoll.getEvent()).isNull();
    }

    @Test
    void createdByTest() throws Exception {
        EventPoll eventPoll = getEventPollRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        eventPoll.setCreatedBy(userProfileBack);
        assertThat(eventPoll.getCreatedBy()).isEqualTo(userProfileBack);

        eventPoll.createdBy(null);
        assertThat(eventPoll.getCreatedBy()).isNull();
    }
}
