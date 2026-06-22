package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventPollDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventPollDTO.class);
        EventPollDTO eventPollDTO1 = new EventPollDTO();
        eventPollDTO1.setId(1L);
        EventPollDTO eventPollDTO2 = new EventPollDTO();
        assertThat(eventPollDTO1).isNotEqualTo(eventPollDTO2);
        eventPollDTO2.setId(eventPollDTO1.getId());
        assertThat(eventPollDTO1).isEqualTo(eventPollDTO2);
        eventPollDTO2.setId(2L);
        assertThat(eventPollDTO1).isNotEqualTo(eventPollDTO2);
        eventPollDTO1.setId(null);
        assertThat(eventPollDTO1).isNotEqualTo(eventPollDTO2);
    }
}
