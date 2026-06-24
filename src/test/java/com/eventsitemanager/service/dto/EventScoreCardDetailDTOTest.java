package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventScoreCardDetailDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventScoreCardDetailDTO.class);
        EventScoreCardDetailDTO eventScoreCardDetailDTO1 = new EventScoreCardDetailDTO();
        eventScoreCardDetailDTO1.setId(1L);
        EventScoreCardDetailDTO eventScoreCardDetailDTO2 = new EventScoreCardDetailDTO();
        assertThat(eventScoreCardDetailDTO1).isNotEqualTo(eventScoreCardDetailDTO2);
        eventScoreCardDetailDTO2.setId(eventScoreCardDetailDTO1.getId());
        assertThat(eventScoreCardDetailDTO1).isEqualTo(eventScoreCardDetailDTO2);
        eventScoreCardDetailDTO2.setId(2L);
        assertThat(eventScoreCardDetailDTO1).isNotEqualTo(eventScoreCardDetailDTO2);
        eventScoreCardDetailDTO1.setId(null);
        assertThat(eventScoreCardDetailDTO1).isNotEqualTo(eventScoreCardDetailDTO2);
    }
}
