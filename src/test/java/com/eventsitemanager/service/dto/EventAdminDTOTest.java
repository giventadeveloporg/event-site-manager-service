package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventAdminDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventAdminDTO.class);
        EventAdminDTO eventAdminDTO1 = new EventAdminDTO();
        eventAdminDTO1.setId(1L);
        EventAdminDTO eventAdminDTO2 = new EventAdminDTO();
        assertThat(eventAdminDTO1).isNotEqualTo(eventAdminDTO2);
        eventAdminDTO2.setId(eventAdminDTO1.getId());
        assertThat(eventAdminDTO1).isEqualTo(eventAdminDTO2);
        eventAdminDTO2.setId(2L);
        assertThat(eventAdminDTO1).isNotEqualTo(eventAdminDTO2);
        eventAdminDTO1.setId(null);
        assertThat(eventAdminDTO1).isNotEqualTo(eventAdminDTO2);
    }
}
