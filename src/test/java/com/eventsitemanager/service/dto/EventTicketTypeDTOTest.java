package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventTicketTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventTicketTypeDTO.class);
        EventTicketTypeDTO eventTicketTypeDTO1 = new EventTicketTypeDTO();
        eventTicketTypeDTO1.setId(1L);
        EventTicketTypeDTO eventTicketTypeDTO2 = new EventTicketTypeDTO();
        assertThat(eventTicketTypeDTO1).isNotEqualTo(eventTicketTypeDTO2);
        eventTicketTypeDTO2.setId(eventTicketTypeDTO1.getId());
        assertThat(eventTicketTypeDTO1).isEqualTo(eventTicketTypeDTO2);
        eventTicketTypeDTO2.setId(2L);
        assertThat(eventTicketTypeDTO1).isNotEqualTo(eventTicketTypeDTO2);
        eventTicketTypeDTO1.setId(null);
        assertThat(eventTicketTypeDTO1).isNotEqualTo(eventTicketTypeDTO2);
    }
}
