package com.eventsitemanager.domain;

import static com.eventsitemanager.domain.EventPollOptionTestSamples.*;
import static com.eventsitemanager.domain.EventPollResponseTestSamples.*;
import static com.eventsitemanager.domain.EventPollTestSamples.*;
import static com.eventsitemanager.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventPollResponseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventPollResponse.class);
        EventPollResponse eventPollResponse1 = getEventPollResponseSample1();
        EventPollResponse eventPollResponse2 = new EventPollResponse();
        assertThat(eventPollResponse1).isNotEqualTo(eventPollResponse2);

        eventPollResponse2.setId(eventPollResponse1.getId());
        assertThat(eventPollResponse1).isEqualTo(eventPollResponse2);

        eventPollResponse2 = getEventPollResponseSample2();
        assertThat(eventPollResponse1).isNotEqualTo(eventPollResponse2);
    }

    @Test
    void pollTest() throws Exception {
        EventPollResponse eventPollResponse = getEventPollResponseRandomSampleGenerator();
        EventPoll eventPollBack = getEventPollRandomSampleGenerator();

        eventPollResponse.setPoll(eventPollBack);
        assertThat(eventPollResponse.getPoll()).isEqualTo(eventPollBack);

        eventPollResponse.poll(null);
        assertThat(eventPollResponse.getPoll()).isNull();
    }

    @Test
    void pollOptionTest() throws Exception {
        EventPollResponse eventPollResponse = getEventPollResponseRandomSampleGenerator();
        EventPollOption eventPollOptionBack = getEventPollOptionRandomSampleGenerator();

        eventPollResponse.setPollOption(eventPollOptionBack);
        assertThat(eventPollResponse.getPollOption()).isEqualTo(eventPollOptionBack);

        eventPollResponse.pollOption(null);
        assertThat(eventPollResponse.getPollOption()).isNull();
    }

    @Test
    void userTest() throws Exception {
        EventPollResponse eventPollResponse = getEventPollResponseRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        eventPollResponse.setUser(userProfileBack);
        assertThat(eventPollResponse.getUser()).isEqualTo(userProfileBack);

        eventPollResponse.user(null);
        assertThat(eventPollResponse.getUser()).isNull();
    }
}
