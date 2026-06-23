package com.eventsitemanager.domain;

import static com.eventsitemanager.domain.BulkOperationLogTestSamples.*;
import static com.eventsitemanager.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BulkOperationLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BulkOperationLog.class);
        BulkOperationLog bulkOperationLog1 = getBulkOperationLogSample1();
        BulkOperationLog bulkOperationLog2 = new BulkOperationLog();
        assertThat(bulkOperationLog1).isNotEqualTo(bulkOperationLog2);

        bulkOperationLog2.setId(bulkOperationLog1.getId());
        assertThat(bulkOperationLog1).isEqualTo(bulkOperationLog2);

        bulkOperationLog2 = getBulkOperationLogSample2();
        assertThat(bulkOperationLog1).isNotEqualTo(bulkOperationLog2);
    }

    @Test
    void performedByTest() throws Exception {
        BulkOperationLog bulkOperationLog = getBulkOperationLogRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        bulkOperationLog.setPerformedBy(userProfileBack);
        assertThat(bulkOperationLog.getPerformedBy()).isEqualTo(userProfileBack);

        bulkOperationLog.performedBy(null);
        assertThat(bulkOperationLog.getPerformedBy()).isNull();
    }
}
