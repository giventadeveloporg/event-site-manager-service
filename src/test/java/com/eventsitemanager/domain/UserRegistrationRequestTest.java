package com.eventsitemanager.domain;

import static com.eventsitemanager.domain.UserProfileTestSamples.*;
import static com.eventsitemanager.domain.UserRegistrationRequestTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserRegistrationRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserRegistrationRequest.class);
        UserRegistrationRequest userRegistrationRequest1 = getUserRegistrationRequestSample1();
        UserRegistrationRequest userRegistrationRequest2 = new UserRegistrationRequest();
        assertThat(userRegistrationRequest1).isNotEqualTo(userRegistrationRequest2);

        userRegistrationRequest2.setId(userRegistrationRequest1.getId());
        assertThat(userRegistrationRequest1).isEqualTo(userRegistrationRequest2);

        userRegistrationRequest2 = getUserRegistrationRequestSample2();
        assertThat(userRegistrationRequest1).isNotEqualTo(userRegistrationRequest2);
    }

    @Test
    void reviewedByTest() throws Exception {
        UserRegistrationRequest userRegistrationRequest = getUserRegistrationRequestRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userRegistrationRequest.setReviewedBy(userProfileBack);
        assertThat(userRegistrationRequest.getReviewedBy()).isEqualTo(userProfileBack);

        userRegistrationRequest.reviewedBy(null);
        assertThat(userRegistrationRequest.getReviewedBy()).isNull();
    }
}
