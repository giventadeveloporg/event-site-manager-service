package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommunicationCampaignDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommunicationCampaignDTO.class);
        CommunicationCampaignDTO communicationCampaignDTO1 = new CommunicationCampaignDTO();
        communicationCampaignDTO1.setId(1L);
        CommunicationCampaignDTO communicationCampaignDTO2 = new CommunicationCampaignDTO();
        assertThat(communicationCampaignDTO1).isNotEqualTo(communicationCampaignDTO2);
        communicationCampaignDTO2.setId(communicationCampaignDTO1.getId());
        assertThat(communicationCampaignDTO1).isEqualTo(communicationCampaignDTO2);
        communicationCampaignDTO2.setId(2L);
        assertThat(communicationCampaignDTO1).isNotEqualTo(communicationCampaignDTO2);
        communicationCampaignDTO1.setId(null);
        assertThat(communicationCampaignDTO1).isNotEqualTo(communicationCampaignDTO2);
    }
}
