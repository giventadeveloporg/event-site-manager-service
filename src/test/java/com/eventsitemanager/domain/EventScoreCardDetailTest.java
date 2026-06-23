package com.eventsitemanager.domain;

import static com.eventsitemanager.domain.EventScoreCardDetailTestSamples.*;
import static com.eventsitemanager.domain.EventScoreCardTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventScoreCardDetailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventScoreCardDetail.class);
        EventScoreCardDetail eventScoreCardDetail1 = getEventScoreCardDetailSample1();
        EventScoreCardDetail eventScoreCardDetail2 = new EventScoreCardDetail();
        assertThat(eventScoreCardDetail1).isNotEqualTo(eventScoreCardDetail2);

        eventScoreCardDetail2.setId(eventScoreCardDetail1.getId());
        assertThat(eventScoreCardDetail1).isEqualTo(eventScoreCardDetail2);

        eventScoreCardDetail2 = getEventScoreCardDetailSample2();
        assertThat(eventScoreCardDetail1).isNotEqualTo(eventScoreCardDetail2);
    }

    @Test
    void scoreCardTest() throws Exception {
        EventScoreCardDetail eventScoreCardDetail = getEventScoreCardDetailRandomSampleGenerator();
        EventScoreCard eventScoreCardBack = getEventScoreCardRandomSampleGenerator();

        eventScoreCardDetail.setScoreCard(eventScoreCardBack);
        assertThat(eventScoreCardDetail.getScoreCard()).isEqualTo(eventScoreCardBack);

        eventScoreCardDetail.scoreCard(null);
        assertThat(eventScoreCardDetail.getScoreCard()).isNull();
    }
}
