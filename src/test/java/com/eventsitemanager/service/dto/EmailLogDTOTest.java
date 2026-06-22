package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmailLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailLogDTO.class);
        EmailLogDTO emailLogDTO1 = new EmailLogDTO();
        emailLogDTO1.setId(1L);
        EmailLogDTO emailLogDTO2 = new EmailLogDTO();
        assertThat(emailLogDTO1).isNotEqualTo(emailLogDTO2);
        emailLogDTO2.setId(emailLogDTO1.getId());
        assertThat(emailLogDTO1).isEqualTo(emailLogDTO2);
        emailLogDTO2.setId(2L);
        assertThat(emailLogDTO1).isNotEqualTo(emailLogDTO2);
        emailLogDTO1.setId(null);
        assertThat(emailLogDTO1).isNotEqualTo(emailLogDTO2);
    }
}
