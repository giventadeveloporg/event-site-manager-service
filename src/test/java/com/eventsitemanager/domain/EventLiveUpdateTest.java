package com.eventsitemanager.domain;

import static com.eventsitemanager.domain.EventDetailsTestSamples.*;
import static com.eventsitemanager.domain.EventLiveUpdateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventLiveUpdateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventLiveUpdate.class);
        EventLiveUpdate eventLiveUpdate1 = getEventLiveUpdateSample1();
        EventLiveUpdate eventLiveUpdate2 = new EventLiveUpdate();
        assertThat(eventLiveUpdate1).isNotEqualTo(eventLiveUpdate2);

        eventLiveUpdate2.setId(eventLiveUpdate1.getId());
        assertThat(eventLiveUpdate1).isEqualTo(eventLiveUpdate2);

        eventLiveUpdate2 = getEventLiveUpdateSample2();
        assertThat(eventLiveUpdate1).isNotEqualTo(eventLiveUpdate2);
    }

    @Test
    void eventTest() throws Exception {
        EventLiveUpdate eventLiveUpdate = getEventLiveUpdateRandomSampleGenerator();
        EventDetails eventDetailsBack = getEventDetailsRandomSampleGenerator();

        eventLiveUpdate.setEvent(eventDetailsBack);
        assertThat(eventLiveUpdate.getEvent()).isEqualTo(eventDetailsBack);

        eventLiveUpdate.event(null);
        assertThat(eventLiveUpdate.getEvent()).isNull();
    }
}
