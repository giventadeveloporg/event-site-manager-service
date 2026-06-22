package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventTicketTransactionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventTicketTransactionDTO.class);
        EventTicketTransactionDTO eventTicketTransactionDTO1 = new EventTicketTransactionDTO();
        eventTicketTransactionDTO1.setId(1L);
        EventTicketTransactionDTO eventTicketTransactionDTO2 = new EventTicketTransactionDTO();
        assertThat(eventTicketTransactionDTO1).isNotEqualTo(eventTicketTransactionDTO2);
        eventTicketTransactionDTO2.setId(eventTicketTransactionDTO1.getId());
        assertThat(eventTicketTransactionDTO1).isEqualTo(eventTicketTransactionDTO2);
        eventTicketTransactionDTO2.setId(2L);
        assertThat(eventTicketTransactionDTO1).isNotEqualTo(eventTicketTransactionDTO2);
        eventTicketTransactionDTO1.setId(null);
        assertThat(eventTicketTransactionDTO1).isNotEqualTo(eventTicketTransactionDTO2);
    }
}
