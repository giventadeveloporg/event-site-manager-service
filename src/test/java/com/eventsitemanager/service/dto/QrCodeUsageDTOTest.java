package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QrCodeUsageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QrCodeUsageDTO.class);
        QrCodeUsageDTO qrCodeUsageDTO1 = new QrCodeUsageDTO();
        qrCodeUsageDTO1.setId(1L);
        QrCodeUsageDTO qrCodeUsageDTO2 = new QrCodeUsageDTO();
        assertThat(qrCodeUsageDTO1).isNotEqualTo(qrCodeUsageDTO2);
        qrCodeUsageDTO2.setId(qrCodeUsageDTO1.getId());
        assertThat(qrCodeUsageDTO1).isEqualTo(qrCodeUsageDTO2);
        qrCodeUsageDTO2.setId(2L);
        assertThat(qrCodeUsageDTO1).isNotEqualTo(qrCodeUsageDTO2);
        qrCodeUsageDTO1.setId(null);
        assertThat(qrCodeUsageDTO1).isNotEqualTo(qrCodeUsageDTO2);
    }
}
