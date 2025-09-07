package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.CommunicationCampaignTestSamples.*;
import static com.nextjstemplate.domain.WhatsAppLogTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WhatsAppLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WhatsAppLog.class);
        WhatsAppLog whatsAppLog1 = getWhatsAppLogSample1();
        WhatsAppLog whatsAppLog2 = new WhatsAppLog();
        assertThat(whatsAppLog1).isNotEqualTo(whatsAppLog2);

        whatsAppLog2.setId(whatsAppLog1.getId());
        assertThat(whatsAppLog1).isEqualTo(whatsAppLog2);

        whatsAppLog2 = getWhatsAppLogSample2();
        assertThat(whatsAppLog1).isNotEqualTo(whatsAppLog2);
    }
    /* @Test
    void campaignTest() throws Exception {
        WhatsAppLog whatsAppLog = getWhatsAppLogRandomSampleGenerator();
        CommunicationCampaign communicationCampaignBack = getCommunicationCampaignRandomSampleGenerator();

        whatsAppLog.setCampaign(communicationCampaignBack);
        assertThat(whatsAppLog.getCampaign()).isEqualTo(communicationCampaignBack);

        whatsAppLog.campaign(null);
        assertThat(whatsAppLog.getCampaign()).isNull();
    }*/
}
