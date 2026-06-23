package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventOrganizerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventOrganizerDTO.class);
        EventOrganizerDTO eventOrganizerDTO1 = new EventOrganizerDTO();
        eventOrganizerDTO1.setId(1L);
        EventOrganizerDTO eventOrganizerDTO2 = new EventOrganizerDTO();
        assertThat(eventOrganizerDTO1).isNotEqualTo(eventOrganizerDTO2);
        eventOrganizerDTO2.setId(eventOrganizerDTO1.getId());
        assertThat(eventOrganizerDTO1).isEqualTo(eventOrganizerDTO2);
        eventOrganizerDTO2.setId(2L);
        assertThat(eventOrganizerDTO1).isNotEqualTo(eventOrganizerDTO2);
        eventOrganizerDTO1.setId(null);
        assertThat(eventOrganizerDTO1).isNotEqualTo(eventOrganizerDTO2);
    }
}
