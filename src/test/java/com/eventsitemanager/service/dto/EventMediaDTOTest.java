package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventMediaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventMediaDTO.class);
        EventMediaDTO eventMediaDTO1 = new EventMediaDTO();
        eventMediaDTO1.setId(1L);
        EventMediaDTO eventMediaDTO2 = new EventMediaDTO();
        assertThat(eventMediaDTO1).isNotEqualTo(eventMediaDTO2);
        eventMediaDTO2.setId(eventMediaDTO1.getId());
        assertThat(eventMediaDTO1).isEqualTo(eventMediaDTO2);
        eventMediaDTO2.setId(2L);
        assertThat(eventMediaDTO1).isNotEqualTo(eventMediaDTO2);
        eventMediaDTO1.setId(null);
        assertThat(eventMediaDTO1).isNotEqualTo(eventMediaDTO2);
    }
}
