package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventLiveUpdateDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventLiveUpdateDTO.class);
        EventLiveUpdateDTO eventLiveUpdateDTO1 = new EventLiveUpdateDTO();
        eventLiveUpdateDTO1.setId(1L);
        EventLiveUpdateDTO eventLiveUpdateDTO2 = new EventLiveUpdateDTO();
        assertThat(eventLiveUpdateDTO1).isNotEqualTo(eventLiveUpdateDTO2);
        eventLiveUpdateDTO2.setId(eventLiveUpdateDTO1.getId());
        assertThat(eventLiveUpdateDTO1).isEqualTo(eventLiveUpdateDTO2);
        eventLiveUpdateDTO2.setId(2L);
        assertThat(eventLiveUpdateDTO1).isNotEqualTo(eventLiveUpdateDTO2);
        eventLiveUpdateDTO1.setId(null);
        assertThat(eventLiveUpdateDTO1).isNotEqualTo(eventLiveUpdateDTO2);
    }
}
