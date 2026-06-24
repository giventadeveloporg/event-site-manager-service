package com.eventsitemanager.domain;

import static com.eventsitemanager.domain.EventAdminTestSamples.*;
import static com.eventsitemanager.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventAdminTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventAdmin.class);
        EventAdmin eventAdmin1 = getEventAdminSample1();
        EventAdmin eventAdmin2 = new EventAdmin();
        assertThat(eventAdmin1).isNotEqualTo(eventAdmin2);

        eventAdmin2.setId(eventAdmin1.getId());
        assertThat(eventAdmin1).isEqualTo(eventAdmin2);

        eventAdmin2 = getEventAdminSample2();
        assertThat(eventAdmin1).isNotEqualTo(eventAdmin2);
    }

    @Test
    void userTest() throws Exception {
        EventAdmin eventAdmin = getEventAdminRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        eventAdmin.setUser(userProfileBack);
        assertThat(eventAdmin.getUser()).isEqualTo(userProfileBack);

        eventAdmin.user(null);
        assertThat(eventAdmin.getUser()).isNull();
    }

    @Test
    void createdByTest() throws Exception {
        EventAdmin eventAdmin = getEventAdminRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        eventAdmin.setCreatedBy(userProfileBack);
        assertThat(eventAdmin.getCreatedBy()).isEqualTo(userProfileBack);

        eventAdmin.createdBy(null);
        assertThat(eventAdmin.getCreatedBy()).isNull();
    }
}
