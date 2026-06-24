package com.eventsitemanager.domain;

import static com.eventsitemanager.domain.DiscountCodeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DiscountCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DiscountCode.class);
        DiscountCode discountCode1 = getDiscountCodeSample1();
        DiscountCode discountCode2 = new DiscountCode();
        assertThat(discountCode1).isNotEqualTo(discountCode2);

        discountCode2.setId(discountCode1.getId());
        assertThat(discountCode1).isEqualTo(discountCode2);

        discountCode2 = getDiscountCodeSample2();
        assertThat(discountCode1).isNotEqualTo(discountCode2);
    }
}
