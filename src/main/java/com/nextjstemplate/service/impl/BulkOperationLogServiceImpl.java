package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.BulkOperationLog;
import com.nextjstemplate.repository.BulkOperationLogRepository;
import com.nextjstemplate.service.BulkOperationLogService;
import com.nextjstemplate.service.dto.BulkOperationLogDTO;
import com.nextjstemplate.service.mapper.BulkOperationLogMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.BulkOperationLog}.
 */
@Service
@Transactional
public class BulkOperationLogServiceImpl implements BulkOperationLogService {

    private static final Logger LOG = LoggerFactory.getLogger(BulkOperationLogServiceImpl.class);

    private final BulkOperationLogRepository bulkOperationLogRepository;

    private final BulkOperationLogMapper bulkOperationLogMapper;

    public BulkOperationLogServiceImpl(
        BulkOperationLogRepository bulkOperationLogRepository,
        BulkOperationLogMapper bulkOperationLogMapper
    ) {
        this.bulkOperationLogRepository = bulkOperationLogRepository;
        this.bulkOperationLogMapper = bulkOperationLogMapper;
    }

    @Override
    public BulkOperationLogDTO save(BulkOperationLogDTO bulkOperationLogDTO) {
        LOG.debug("Request to save BulkOperationLog : {}", bulkOperationLogDTO);
        BulkOperationLog bulkOperationLog = bulkOperationLogMapper.toEntity(bulkOperationLogDTO);
        bulkOperationLog = bulkOperationLogRepository.save(bulkOperationLog);
        return bulkOperationLogMapper.toDto(bulkOperationLog);
    }

    @Override
    public BulkOperationLogDTO update(BulkOperationLogDTO bulkOperationLogDTO) {
        LOG.debug("Request to update BulkOperationLog : {}", bulkOperationLogDTO);
        BulkOperationLog bulkOperationLog = bulkOperationLogMapper.toEntity(bulkOperationLogDTO);
        bulkOperationLog = bulkOperationLogRepository.save(bulkOperationLog);
        return bulkOperationLogMapper.toDto(bulkOperationLog);
    }

    @Override
    public Optional<BulkOperationLogDTO> partialUpdate(BulkOperationLogDTO bulkOperationLogDTO) {
        LOG.debug("Request to partially update BulkOperationLog : {}", bulkOperationLogDTO);

        return bulkOperationLogRepository
            .findById(bulkOperationLogDTO.getId())
            .map(existingBulkOperationLog -> {
                bulkOperationLogMapper.partialUpdate(existingBulkOperationLog, bulkOperationLogDTO);

                return existingBulkOperationLog;
            })
            .map(bulkOperationLogRepository::save)
            .map(bulkOperationLogMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BulkOperationLogDTO> findOne(Long id) {
        LOG.debug("Request to get BulkOperationLog : {}", id);
        return bulkOperationLogRepository.findById(id).map(bulkOperationLogMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete BulkOperationLog : {}", id);
        bulkOperationLogRepository.deleteById(id);
    }
}
