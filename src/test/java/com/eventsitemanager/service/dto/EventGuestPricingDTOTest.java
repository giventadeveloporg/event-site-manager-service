package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventGuestPricingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventGuestPricingDTO.class);
        EventGuestPricingDTO eventGuestPricingDTO1 = new EventGuestPricingDTO();
        eventGuestPricingDTO1.setId(1L);
        EventGuestPricingDTO eventGuestPricingDTO2 = new EventGuestPricingDTO();
        assertThat(eventGuestPricingDTO1).isNotEqualTo(eventGuestPricingDTO2);
        eventGuestPricingDTO2.setId(eventGuestPricingDTO1.getId());
        assertThat(eventGuestPricingDTO1).isEqualTo(eventGuestPricingDTO2);
        eventGuestPricingDTO2.setId(2L);
        assertThat(eventGuestPricingDTO1).isNotEqualTo(eventGuestPricingDTO2);
        eventGuestPricingDTO1.setId(null);
        assertThat(eventGuestPricingDTO1).isNotEqualTo(eventGuestPricingDTO2);
    }
}
