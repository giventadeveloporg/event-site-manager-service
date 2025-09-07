package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.CommunicationCampaignTestSamples.*;
import static com.nextjstemplate.domain.EmailLogTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmailLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailLog.class);
        EmailLog emailLog1 = getEmailLogSample1();
        EmailLog emailLog2 = new EmailLog();
        assertThat(emailLog1).isNotEqualTo(emailLog2);

        emailLog2.setId(emailLog1.getId());
        assertThat(emailLog1).isEqualTo(emailLog2);

        emailLog2 = getEmailLogSample2();
        assertThat(emailLog1).isNotEqualTo(emailLog2);
    }
    /* @Test
    void campaignTest() throws Exception {
        EmailLog emailLog = getEmailLogRandomSampleGenerator();
        CommunicationCampaign communicationCampaignBack = getCommunicationCampaignRandomSampleGenerator();

        emailLog.setCampaign(communicationCampaignBack);
        assertThat(emailLog.getCampaign()).isEqualTo(communicationCampaignBack);

        emailLog.campaign(null);
        assertThat(emailLog.getCampaign()).isNull();
    }*/
}
