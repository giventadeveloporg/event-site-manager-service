package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WhatsAppLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WhatsAppLogDTO.class);
        WhatsAppLogDTO whatsAppLogDTO1 = new WhatsAppLogDTO();
        whatsAppLogDTO1.setId(1L);
        WhatsAppLogDTO whatsAppLogDTO2 = new WhatsAppLogDTO();
        assertThat(whatsAppLogDTO1).isNotEqualTo(whatsAppLogDTO2);
        whatsAppLogDTO2.setId(whatsAppLogDTO1.getId());
        assertThat(whatsAppLogDTO1).isEqualTo(whatsAppLogDTO2);
        whatsAppLogDTO2.setId(2L);
        assertThat(whatsAppLogDTO1).isNotEqualTo(whatsAppLogDTO2);
        whatsAppLogDTO1.setId(null);
        assertThat(whatsAppLogDTO1).isNotEqualTo(whatsAppLogDTO2);
    }
}
