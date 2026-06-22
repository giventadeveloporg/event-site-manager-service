package com.eventsitemanager.domain;

import static com.eventsitemanager.domain.EventDetailsTestSamples.*;
import static com.eventsitemanager.domain.EventScoreCardTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventScoreCardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventScoreCard.class);
        EventScoreCard eventScoreCard1 = getEventScoreCardSample1();
        EventScoreCard eventScoreCard2 = new EventScoreCard();
        assertThat(eventScoreCard1).isNotEqualTo(eventScoreCard2);

        eventScoreCard2.setId(eventScoreCard1.getId());
        assertThat(eventScoreCard1).isEqualTo(eventScoreCard2);

        eventScoreCard2 = getEventScoreCardSample2();
        assertThat(eventScoreCard1).isNotEqualTo(eventScoreCard2);
    }

    @Test
    void eventTest() throws Exception {
        EventScoreCard eventScoreCard = getEventScoreCardRandomSampleGenerator();
        EventDetails eventDetailsBack = getEventDetailsRandomSampleGenerator();

        eventScoreCard.setEvent(eventDetailsBack);
        assertThat(eventScoreCard.getEvent()).isEqualTo(eventDetailsBack);

        eventScoreCard.event(null);
        assertThat(eventScoreCard.getEvent()).isNull();
    }
}
