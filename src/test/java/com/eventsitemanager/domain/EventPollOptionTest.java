package com.eventsitemanager.domain;

import static com.eventsitemanager.domain.EventPollOptionTestSamples.*;
import static com.eventsitemanager.domain.EventPollTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventPollOptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventPollOption.class);
        EventPollOption eventPollOption1 = getEventPollOptionSample1();
        EventPollOption eventPollOption2 = new EventPollOption();
        assertThat(eventPollOption1).isNotEqualTo(eventPollOption2);

        eventPollOption2.setId(eventPollOption1.getId());
        assertThat(eventPollOption1).isEqualTo(eventPollOption2);

        eventPollOption2 = getEventPollOptionSample2();
        assertThat(eventPollOption1).isNotEqualTo(eventPollOption2);
    }

    @Test
    void pollTest() throws Exception {
        EventPollOption eventPollOption = getEventPollOptionRandomSampleGenerator();
        EventPoll eventPollBack = getEventPollRandomSampleGenerator();

        eventPollOption.setPoll(eventPollBack);
        assertThat(eventPollOption.getPoll()).isEqualTo(eventPollBack);

        eventPollOption.poll(null);
        assertThat(eventPollOption.getPoll()).isNull();
    }
}
