package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.CommunicationCampaignTestSamples.*;
import static com.nextjstemplate.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommunicationCampaignTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommunicationCampaign.class);
        CommunicationCampaign communicationCampaign1 = getCommunicationCampaignSample1();
        CommunicationCampaign communicationCampaign2 = new CommunicationCampaign();
        assertThat(communicationCampaign1).isNotEqualTo(communicationCampaign2);

        communicationCampaign2.setId(communicationCampaign1.getId());
        assertThat(communicationCampaign1).isEqualTo(communicationCampaign2);

        communicationCampaign2 = getCommunicationCampaignSample2();
        assertThat(communicationCampaign1).isNotEqualTo(communicationCampaign2);
    }
    /*@Test
    void createdByTest() throws Exception {
        CommunicationCampaign communicationCampaign = getCommunicationCampaignRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        communicationCampaign.setCreatedBy(userProfileBack);
        assertThat(communicationCampaign.getCreatedBy()).isEqualTo(userProfileBack);

        communicationCampaign.createdBy(null);
        assertThat(communicationCampaign.getCreatedBy()).isNull();
    }*/
}
