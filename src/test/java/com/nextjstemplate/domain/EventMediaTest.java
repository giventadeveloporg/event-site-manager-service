package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.EventDetailsTestSamples.*;
import static com.nextjstemplate.domain.EventMediaTestSamples.*;
import static com.nextjstemplate.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventMediaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventMedia.class);
        EventMedia eventMedia1 = getEventMediaSample1();
        EventMedia eventMedia2 = new EventMedia();
        assertThat(eventMedia1).isNotEqualTo(eventMedia2);

        eventMedia2.setId(eventMedia1.getId());
        assertThat(eventMedia1).isEqualTo(eventMedia2);

        eventMedia2 = getEventMediaSample2();
        assertThat(eventMedia1).isNotEqualTo(eventMedia2);
    }
    /* @Test
    void eventTest() throws Exception {
        EventMedia eventMedia = getEventMediaRandomSampleGenerator();
        EventDetails eventDetailsBack = getEventDetailsRandomSampleGenerator();

        eventMedia.setEvent(eventDetailsBack);
        assertThat(eventMedia.getEvent()).isEqualTo(eventDetailsBack);

        eventMedia.event(null);
        assertThat(eventMedia.getEvent()).isNull();
    }

    @Test
    void uploadedByTest() throws Exception {
        EventMedia eventMedia = getEventMediaRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        eventMedia.setUploadedBy(userProfileBack);
        assertThat(eventMedia.getUploadedBy()).isEqualTo(userProfileBack);

        eventMedia.uploadedBy(null);
        assertThat(eventMedia.getUploadedBy()).isNull();
    }*/
}
