package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventTicketTransactionItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventTicketTransactionItemDTO.class);
        EventTicketTransactionItemDTO eventTicketTransactionItemDTO1 = new EventTicketTransactionItemDTO();
        eventTicketTransactionItemDTO1.setId(1L);
        EventTicketTransactionItemDTO eventTicketTransactionItemDTO2 = new EventTicketTransactionItemDTO();
        assertThat(eventTicketTransactionItemDTO1).isNotEqualTo(eventTicketTransactionItemDTO2);
        eventTicketTransactionItemDTO2.setId(eventTicketTransactionItemDTO1.getId());
        assertThat(eventTicketTransactionItemDTO1).isEqualTo(eventTicketTransactionItemDTO2);
        eventTicketTransactionItemDTO2.setId(2L);
        assertThat(eventTicketTransactionItemDTO1).isNotEqualTo(eventTicketTransactionItemDTO2);
        eventTicketTransactionItemDTO1.setId(null);
        assertThat(eventTicketTransactionItemDTO1).isNotEqualTo(eventTicketTransactionItemDTO2);
    }
}
