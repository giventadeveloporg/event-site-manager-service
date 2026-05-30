package com.nextjstemplate.web.rest;

import com.nextjstemplate.service.BatchJobService;
import com.nextjstemplate.service.dto.BatchJobExecutionDTO;
import com.nextjstemplate.service.dto.BatchJobExecutionPageResponseDTO;
import com.nextjstemplate.service.dto.BatchJobSummaryResponseDTO;
import com.nextjstemplate.service.dto.ConfiguredBatchJobResponseDTO;
import java.time.ZonedDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/batch-jobs")
public class BatchJobAdminResource {

    private static final Logger log = LoggerFactory.getLogger(BatchJobAdminResource.class);

    private final BatchJobService batchJobService;

    public BatchJobAdminResource(BatchJobService batchJobService) {
        this.batchJobService = batchJobService;
    }

    @GetMapping("/executions")
    public ResponseEntity<BatchJobExecutionPageResponseDTO> getExecutions(
        @RequestHeader(value = "Authorization", required = false) String authHeader,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String jobName,
        @RequestParam(required = false) String tenantId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startedAfter,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startedBefore,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size,
        @RequestParam(required = false) String sort
    ) {
        log.debug("REST request to get batch job executions");
        return ResponseEntity.ok(
            batchJobService.getExecutions(status, jobName, tenantId, startedAfter, startedBefore, page, size, sort, authHeader)
        );
    }

    @GetMapping("/executions/{id}")
    public ResponseEntity<BatchJobExecutionDTO> getExecution(
        @PathVariable Long id,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        return ResponseEntity.ok(batchJobService.getExecution(id, authHeader));
    }

    @GetMapping("/executions/failed")
    public ResponseEntity<List<BatchJobExecutionDTO>> getFailedExecutions(
        @RequestHeader(value = "Authorization", required = false) String authHeader,
        @RequestParam(defaultValue = "50") Integer limit
    ) {
        return ResponseEntity.ok(batchJobService.getFailedExecutions(limit, authHeader));
    }

    @GetMapping("/executions/running")
    public ResponseEntity<List<BatchJobExecutionDTO>> getRunningExecutions(
        @RequestHeader(value = "Authorization", required = false) String authHeader,
        @RequestParam(defaultValue = "50") Integer limit
    ) {
        return ResponseEntity.ok(batchJobService.getRunningExecutions(limit, authHeader));
    }

    @GetMapping("/summary")
    public ResponseEntity<BatchJobSummaryResponseDTO> getSummary(
        @RequestHeader(value = "Authorization", required = false) String authHeader,
        @RequestParam(required = false) String tenantId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startedAfter,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startedBefore
    ) {
        return ResponseEntity.ok(batchJobService.getSummary(tenantId, startedAfter, startedBefore, authHeader));
    }

    @GetMapping("/configured-jobs")
    public ResponseEntity<List<ConfiguredBatchJobResponseDTO>> getConfiguredJobs(
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        return ResponseEntity.ok(batchJobService.getConfiguredJobs(authHeader));
    }
}
