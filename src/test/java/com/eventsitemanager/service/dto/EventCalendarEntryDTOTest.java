package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventCalendarEntryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventCalendarEntryDTO.class);
        EventCalendarEntryDTO eventCalendarEntryDTO1 = new EventCalendarEntryDTO();
        eventCalendarEntryDTO1.setId(1L);
        EventCalendarEntryDTO eventCalendarEntryDTO2 = new EventCalendarEntryDTO();
        assertThat(eventCalendarEntryDTO1).isNotEqualTo(eventCalendarEntryDTO2);
        eventCalendarEntryDTO2.setId(eventCalendarEntryDTO1.getId());
        assertThat(eventCalendarEntryDTO1).isEqualTo(eventCalendarEntryDTO2);
        eventCalendarEntryDTO2.setId(2L);
        assertThat(eventCalendarEntryDTO1).isNotEqualTo(eventCalendarEntryDTO2);
        eventCalendarEntryDTO1.setId(null);
        assertThat(eventCalendarEntryDTO1).isNotEqualTo(eventCalendarEntryDTO2);
    }
}
