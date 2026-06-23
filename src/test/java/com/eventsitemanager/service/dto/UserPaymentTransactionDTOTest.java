package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserPaymentTransactionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserPaymentTransactionDTO.class);
        UserPaymentTransactionDTO userPaymentTransactionDTO1 = new UserPaymentTransactionDTO();
        userPaymentTransactionDTO1.setId(1L);
        UserPaymentTransactionDTO userPaymentTransactionDTO2 = new UserPaymentTransactionDTO();
        assertThat(userPaymentTransactionDTO1).isNotEqualTo(userPaymentTransactionDTO2);
        userPaymentTransactionDTO2.setId(userPaymentTransactionDTO1.getId());
        assertThat(userPaymentTransactionDTO1).isEqualTo(userPaymentTransactionDTO2);
        userPaymentTransactionDTO2.setId(2L);
        assertThat(userPaymentTransactionDTO1).isNotEqualTo(userPaymentTransactionDTO2);
        userPaymentTransactionDTO1.setId(null);
        assertThat(userPaymentTransactionDTO1).isNotEqualTo(userPaymentTransactionDTO2);
    }
}
