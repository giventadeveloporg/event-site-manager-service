package com.eventsitemanager.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SequenceSynchronizationServiceTest {

    private SequenceSynchronizationService service;

    @BeforeEach
    void setUp() {
        service = new SequenceSynchronizationService();
    }

    @Test
    void shouldSkipSpringBatchFrameworkSequences() {
        assertThat(service.shouldSkipSequenceSync("batch_job_seq", null)).isTrue();
        assertThat(service.shouldSkipSequenceSync("batch_job_execution_seq", null)).isTrue();
        assertThat(service.shouldSkipSequenceSync("batch_step_execution_seq", null)).isTrue();
        assertThat(service.shouldSkipSequenceSync("batch_job_execution_log_id_seq", null)).isTrue();
    }

    @Test
    void shouldSkipJoinTableSequencesAndTables() {
        assertThat(service.shouldSkipSequenceSync("rel_event_details__discount_codes_id_seq", "rel_event_details__discount_codes"))
            .isTrue();
    }

    @Test
    void shouldSkipBatchPrefixedSequencesExceptAuditLogTableSyncPath() {
        assertThat(service.shouldSkipSequenceSync("batch_foo_id_seq", "batch_foo")).isTrue();
        assertThat(service.shouldSkipSequenceSync("user_profile_id_seq", "user_profile")).isFalse();
    }

    @Test
    void shouldSkipIntegrationAndLiquibaseSequences() {
        assertThat(service.shouldSkipSequenceSync("int_message_id_seq", "int_message")).isTrue();
        assertThat(service.shouldSkipSequenceSync("databasechangeloglock_id_seq", "databasechangeloglock")).isTrue();
    }
}
