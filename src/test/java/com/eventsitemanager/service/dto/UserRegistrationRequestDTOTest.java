package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserRegistrationRequestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserRegistrationRequestDTO.class);
        UserRegistrationRequestDTO userRegistrationRequestDTO1 = new UserRegistrationRequestDTO();
        userRegistrationRequestDTO1.setId(1L);
        UserRegistrationRequestDTO userRegistrationRequestDTO2 = new UserRegistrationRequestDTO();
        assertThat(userRegistrationRequestDTO1).isNotEqualTo(userRegistrationRequestDTO2);
        userRegistrationRequestDTO2.setId(userRegistrationRequestDTO1.getId());
        assertThat(userRegistrationRequestDTO1).isEqualTo(userRegistrationRequestDTO2);
        userRegistrationRequestDTO2.setId(2L);
        assertThat(userRegistrationRequestDTO1).isNotEqualTo(userRegistrationRequestDTO2);
        userRegistrationRequestDTO1.setId(null);
        assertThat(userRegistrationRequestDTO1).isNotEqualTo(userRegistrationRequestDTO2);
    }
}
