package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.PromotionEmailSentLog;
import com.nextjstemplate.domain.enumeration.EmailStatus;
import com.nextjstemplate.repository.PromotionEmailSentLogRepository;
import com.nextjstemplate.service.PromotionEmailSentLogService;
import com.nextjstemplate.service.dto.PromotionEmailSentLogDTO;
import com.nextjstemplate.service.mapper.PromotionEmailSentLogMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for querying promotion email sent logs.
 */
@Service
@Transactional(readOnly = true)
public class PromotionEmailSentLogServiceImpl implements PromotionEmailSentLogService {

    private final Logger log = LoggerFactory.getLogger(PromotionEmailSentLogServiceImpl.class);

    private final PromotionEmailSentLogRepository promotionEmailSentLogRepository;

    private final PromotionEmailSentLogMapper promotionEmailSentLogMapper;

    public PromotionEmailSentLogServiceImpl(
        PromotionEmailSentLogRepository promotionEmailSentLogRepository,
        PromotionEmailSentLogMapper promotionEmailSentLogMapper
    ) {
        this.promotionEmailSentLogRepository = promotionEmailSentLogRepository;
        this.promotionEmailSentLogMapper = promotionEmailSentLogMapper;
    }

    @Override
    public Page<PromotionEmailSentLogDTO> findAll(Pageable pageable, String tenantId) {
        log.debug("Request to get all PromotionEmailSentLogs for tenant: {}", tenantId);
        // Note: This method should use PromotionEmailSentLogQueryService for proper filtering
        // For now, getting all logs and filtering by tenant (pagination handled manually)
        List<PromotionEmailSentLogDTO> allLogs = promotionEmailSentLogRepository
            .findAll()
            .stream()
            .filter(log -> log.getTenantId() != null && log.getTenantId().equals(tenantId))
            .map(promotionEmailSentLogMapper::toDto)
            .collect(java.util.stream.Collectors.toList());

        // Manual pagination
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allLogs.size());
        List<PromotionEmailSentLogDTO> pageContent = start < allLogs.size()
            ? allLogs.subList(start, end)
            : java.util.Collections.emptyList();

        return new org.springframework.data.domain.PageImpl<>(pageContent, pageable, allLogs.size());
    }

    @Override
    public Optional<PromotionEmailSentLogDTO> findOne(Long id, String tenantId) {
        log.debug("Request to get PromotionEmailSentLog: id={}, tenantId={}", id, tenantId);
        return promotionEmailSentLogRepository
            .findById(id)
            .filter(log -> log.getTenantId().equals(tenantId))
            .map(promotionEmailSentLogMapper::toDto);
    }

    @Override
    public Map<String, Object> getEmailStatistics(Long eventId, String tenantId) {
        log.debug("Request to get email statistics: eventId={}, tenantId={}", eventId, tenantId);

        List<PromotionEmailSentLog> logs = promotionEmailSentLogRepository.findByTenantIdAndEventId(tenantId, eventId);

        long totalSent = logs.stream().filter(log -> log.getEmailStatus() == EmailStatus.SENT).count();
        long totalFailed = logs.stream().filter(log -> log.getEmailStatus() == EmailStatus.FAILED).count();
        long totalBounced = logs.stream().filter(log -> log.getEmailStatus() == EmailStatus.BOUNCED).count();
        long totalTestEmails = logs.stream().filter(log -> Boolean.TRUE.equals(log.getIsTestEmail())).count();
        long totalBulkEmails = logs.stream().filter(log -> Boolean.FALSE.equals(log.getIsTestEmail())).count();

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalSent", totalSent);
        statistics.put("totalFailed", totalFailed);
        statistics.put("totalBounced", totalBounced);
        statistics.put("totalTestEmails", totalTestEmails);
        statistics.put("totalBulkEmails", totalBulkEmails);
        statistics.put("totalEmails", logs.size());
        statistics.put("successRate", logs.isEmpty() ? 0.0 : ((double) totalSent / logs.size()) * 100);

        return statistics;
    }
}
