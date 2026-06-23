package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventPollOptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventPollOptionDTO.class);
        EventPollOptionDTO eventPollOptionDTO1 = new EventPollOptionDTO();
        eventPollOptionDTO1.setId(1L);
        EventPollOptionDTO eventPollOptionDTO2 = new EventPollOptionDTO();
        assertThat(eventPollOptionDTO1).isNotEqualTo(eventPollOptionDTO2);
        eventPollOptionDTO2.setId(eventPollOptionDTO1.getId());
        assertThat(eventPollOptionDTO1).isEqualTo(eventPollOptionDTO2);
        eventPollOptionDTO2.setId(2L);
        assertThat(eventPollOptionDTO1).isNotEqualTo(eventPollOptionDTO2);
        eventPollOptionDTO1.setId(null);
        assertThat(eventPollOptionDTO1).isNotEqualTo(eventPollOptionDTO2);
    }
}
