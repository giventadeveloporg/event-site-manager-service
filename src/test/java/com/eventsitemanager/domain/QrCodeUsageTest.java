package com.eventsitemanager.domain;

import static com.eventsitemanager.domain.EventAttendeeTestSamples.*;
import static com.eventsitemanager.domain.QrCodeUsageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QrCodeUsageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QrCodeUsage.class);
        QrCodeUsage qrCodeUsage1 = getQrCodeUsageSample1();
        QrCodeUsage qrCodeUsage2 = new QrCodeUsage();
        assertThat(qrCodeUsage1).isNotEqualTo(qrCodeUsage2);

        qrCodeUsage2.setId(qrCodeUsage1.getId());
        assertThat(qrCodeUsage1).isEqualTo(qrCodeUsage2);

        qrCodeUsage2 = getQrCodeUsageSample2();
        assertThat(qrCodeUsage1).isNotEqualTo(qrCodeUsage2);
    }

    @Test
    void attendeeTest() throws Exception {
        QrCodeUsage qrCodeUsage = getQrCodeUsageRandomSampleGenerator();
        EventAttendee eventAttendeeBack = getEventAttendeeRandomSampleGenerator();

        qrCodeUsage.setAttendee(eventAttendeeBack);
        assertThat(qrCodeUsage.getAttendee()).isEqualTo(eventAttendeeBack);

        qrCodeUsage.attendee(null);
        assertThat(qrCodeUsage.getAttendee()).isNull();
    }
}
