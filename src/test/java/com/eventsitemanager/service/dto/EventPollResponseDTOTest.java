package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventPollResponseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventPollResponseDTO.class);
        EventPollResponseDTO eventPollResponseDTO1 = new EventPollResponseDTO();
        eventPollResponseDTO1.setId(1L);
        EventPollResponseDTO eventPollResponseDTO2 = new EventPollResponseDTO();
        assertThat(eventPollResponseDTO1).isNotEqualTo(eventPollResponseDTO2);
        eventPollResponseDTO2.setId(eventPollResponseDTO1.getId());
        assertThat(eventPollResponseDTO1).isEqualTo(eventPollResponseDTO2);
        eventPollResponseDTO2.setId(2L);
        assertThat(eventPollResponseDTO1).isNotEqualTo(eventPollResponseDTO2);
        eventPollResponseDTO1.setId(null);
        assertThat(eventPollResponseDTO1).isNotEqualTo(eventPollResponseDTO2);
    }
}
