package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventTypeDetailsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventTypeDetailsDTO.class);
        EventTypeDetailsDTO eventTypeDetailsDTO1 = new EventTypeDetailsDTO();
        eventTypeDetailsDTO1.setId(1L);
        EventTypeDetailsDTO eventTypeDetailsDTO2 = new EventTypeDetailsDTO();
        assertThat(eventTypeDetailsDTO1).isNotEqualTo(eventTypeDetailsDTO2);
        eventTypeDetailsDTO2.setId(eventTypeDetailsDTO1.getId());
        assertThat(eventTypeDetailsDTO1).isEqualTo(eventTypeDetailsDTO2);
        eventTypeDetailsDTO2.setId(2L);
        assertThat(eventTypeDetailsDTO1).isNotEqualTo(eventTypeDetailsDTO2);
        eventTypeDetailsDTO1.setId(null);
        assertThat(eventTypeDetailsDTO1).isNotEqualTo(eventTypeDetailsDTO2);
    }
}
