package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BulkOperationLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BulkOperationLogDTO.class);
        BulkOperationLogDTO bulkOperationLogDTO1 = new BulkOperationLogDTO();
        bulkOperationLogDTO1.setId(1L);
        BulkOperationLogDTO bulkOperationLogDTO2 = new BulkOperationLogDTO();
        assertThat(bulkOperationLogDTO1).isNotEqualTo(bulkOperationLogDTO2);
        bulkOperationLogDTO2.setId(bulkOperationLogDTO1.getId());
        assertThat(bulkOperationLogDTO1).isEqualTo(bulkOperationLogDTO2);
        bulkOperationLogDTO2.setId(2L);
        assertThat(bulkOperationLogDTO1).isNotEqualTo(bulkOperationLogDTO2);
        bulkOperationLogDTO1.setId(null);
        assertThat(bulkOperationLogDTO1).isNotEqualTo(bulkOperationLogDTO2);
    }
}
