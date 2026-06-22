package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventAdminAuditLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventAdminAuditLogDTO.class);
        EventAdminAuditLogDTO eventAdminAuditLogDTO1 = new EventAdminAuditLogDTO();
        eventAdminAuditLogDTO1.setId(1L);
        EventAdminAuditLogDTO eventAdminAuditLogDTO2 = new EventAdminAuditLogDTO();
        assertThat(eventAdminAuditLogDTO1).isNotEqualTo(eventAdminAuditLogDTO2);
        eventAdminAuditLogDTO2.setId(eventAdminAuditLogDTO1.getId());
        assertThat(eventAdminAuditLogDTO1).isEqualTo(eventAdminAuditLogDTO2);
        eventAdminAuditLogDTO2.setId(2L);
        assertThat(eventAdminAuditLogDTO1).isNotEqualTo(eventAdminAuditLogDTO2);
        eventAdminAuditLogDTO1.setId(null);
        assertThat(eventAdminAuditLogDTO1).isNotEqualTo(eventAdminAuditLogDTO2);
    }
}
