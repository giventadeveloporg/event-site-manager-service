package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventLiveUpdateAttachmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventLiveUpdateAttachmentDTO.class);
        EventLiveUpdateAttachmentDTO eventLiveUpdateAttachmentDTO1 = new EventLiveUpdateAttachmentDTO();
        eventLiveUpdateAttachmentDTO1.setId(1L);
        EventLiveUpdateAttachmentDTO eventLiveUpdateAttachmentDTO2 = new EventLiveUpdateAttachmentDTO();
        assertThat(eventLiveUpdateAttachmentDTO1).isNotEqualTo(eventLiveUpdateAttachmentDTO2);
        eventLiveUpdateAttachmentDTO2.setId(eventLiveUpdateAttachmentDTO1.getId());
        assertThat(eventLiveUpdateAttachmentDTO1).isEqualTo(eventLiveUpdateAttachmentDTO2);
        eventLiveUpdateAttachmentDTO2.setId(2L);
        assertThat(eventLiveUpdateAttachmentDTO1).isNotEqualTo(eventLiveUpdateAttachmentDTO2);
        eventLiveUpdateAttachmentDTO1.setId(null);
        assertThat(eventLiveUpdateAttachmentDTO1).isNotEqualTo(eventLiveUpdateAttachmentDTO2);
    }
}
