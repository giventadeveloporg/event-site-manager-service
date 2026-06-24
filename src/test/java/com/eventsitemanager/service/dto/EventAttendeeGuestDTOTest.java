package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventAttendeeGuestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventAttendeeGuestDTO.class);
        EventAttendeeGuestDTO eventAttendeeGuestDTO1 = new EventAttendeeGuestDTO();
        eventAttendeeGuestDTO1.setId(1L);
        EventAttendeeGuestDTO eventAttendeeGuestDTO2 = new EventAttendeeGuestDTO();
        assertThat(eventAttendeeGuestDTO1).isNotEqualTo(eventAttendeeGuestDTO2);
        eventAttendeeGuestDTO2.setId(eventAttendeeGuestDTO1.getId());
        assertThat(eventAttendeeGuestDTO1).isEqualTo(eventAttendeeGuestDTO2);
        eventAttendeeGuestDTO2.setId(2L);
        assertThat(eventAttendeeGuestDTO1).isNotEqualTo(eventAttendeeGuestDTO2);
        eventAttendeeGuestDTO1.setId(null);
        assertThat(eventAttendeeGuestDTO1).isNotEqualTo(eventAttendeeGuestDTO2);
    }
}
