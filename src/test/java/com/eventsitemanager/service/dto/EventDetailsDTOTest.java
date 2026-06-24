package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventDetailsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventDetailsDTO.class);
        EventDetailsDTO eventDetailsDTO1 = new EventDetailsDTO();
        eventDetailsDTO1.setId(1L);
        EventDetailsDTO eventDetailsDTO2 = new EventDetailsDTO();
        assertThat(eventDetailsDTO1).isNotEqualTo(eventDetailsDTO2);
        eventDetailsDTO2.setId(eventDetailsDTO1.getId());
        assertThat(eventDetailsDTO1).isEqualTo(eventDetailsDTO2);
        eventDetailsDTO2.setId(2L);
        assertThat(eventDetailsDTO1).isNotEqualTo(eventDetailsDTO2);
        eventDetailsDTO1.setId(null);
        assertThat(eventDetailsDTO1).isNotEqualTo(eventDetailsDTO2);
    }
}
