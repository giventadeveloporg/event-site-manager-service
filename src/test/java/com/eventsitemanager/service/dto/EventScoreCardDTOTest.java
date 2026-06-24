package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventScoreCardDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventScoreCardDTO.class);
        EventScoreCardDTO eventScoreCardDTO1 = new EventScoreCardDTO();
        eventScoreCardDTO1.setId(1L);
        EventScoreCardDTO eventScoreCardDTO2 = new EventScoreCardDTO();
        assertThat(eventScoreCardDTO1).isNotEqualTo(eventScoreCardDTO2);
        eventScoreCardDTO2.setId(eventScoreCardDTO1.getId());
        assertThat(eventScoreCardDTO1).isEqualTo(eventScoreCardDTO2);
        eventScoreCardDTO2.setId(2L);
        assertThat(eventScoreCardDTO1).isNotEqualTo(eventScoreCardDTO2);
        eventScoreCardDTO1.setId(null);
        assertThat(eventScoreCardDTO1).isNotEqualTo(eventScoreCardDTO2);
    }
}
