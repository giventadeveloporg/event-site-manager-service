package com.eventsitemanager.domain;

import static com.eventsitemanager.domain.EventLiveUpdateAttachmentTestSamples.*;
import static com.eventsitemanager.domain.EventLiveUpdateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventLiveUpdateAttachmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventLiveUpdateAttachment.class);
        EventLiveUpdateAttachment eventLiveUpdateAttachment1 = getEventLiveUpdateAttachmentSample1();
        EventLiveUpdateAttachment eventLiveUpdateAttachment2 = new EventLiveUpdateAttachment();
        assertThat(eventLiveUpdateAttachment1).isNotEqualTo(eventLiveUpdateAttachment2);

        eventLiveUpdateAttachment2.setId(eventLiveUpdateAttachment1.getId());
        assertThat(eventLiveUpdateAttachment1).isEqualTo(eventLiveUpdateAttachment2);

        eventLiveUpdateAttachment2 = getEventLiveUpdateAttachmentSample2();
        assertThat(eventLiveUpdateAttachment1).isNotEqualTo(eventLiveUpdateAttachment2);
    }

    @Test
    void liveUpdateTest() throws Exception {
        EventLiveUpdateAttachment eventLiveUpdateAttachment = getEventLiveUpdateAttachmentRandomSampleGenerator();
        EventLiveUpdate eventLiveUpdateBack = getEventLiveUpdateRandomSampleGenerator();

        eventLiveUpdateAttachment.setLiveUpdate(eventLiveUpdateBack);
        assertThat(eventLiveUpdateAttachment.getLiveUpdate()).isEqualTo(eventLiveUpdateBack);

        eventLiveUpdateAttachment.liveUpdate(null);
        assertThat(eventLiveUpdateAttachment.getLiveUpdate()).isNull();
    }
}
