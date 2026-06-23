package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.QrCodeUsage;
import com.eventsitemanager.repository.QrCodeUsageRepository;
import com.eventsitemanager.service.QrCodeUsageService;
import com.eventsitemanager.service.dto.QrCodeUsageDTO;
import com.eventsitemanager.service.mapper.QrCodeUsageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eventsitemanager.domain.QrCodeUsage}.
 */
@Service
@Transactional
public class QrCodeUsageServiceImpl implements QrCodeUsageService {

    private final Logger log = LoggerFactory.getLogger(QrCodeUsageServiceImpl.class);

    private final QrCodeUsageRepository qrCodeUsageRepository;

    private final QrCodeUsageMapper qrCodeUsageMapper;

    public QrCodeUsageServiceImpl(QrCodeUsageRepository qrCodeUsageRepository, QrCodeUsageMapper qrCodeUsageMapper) {
        this.qrCodeUsageRepository = qrCodeUsageRepository;
        this.qrCodeUsageMapper = qrCodeUsageMapper;
    }

    @Override
    public QrCodeUsageDTO save(QrCodeUsageDTO qrCodeUsageDTO) {
        log.debug("Request to save QrCodeUsage : {}", qrCodeUsageDTO);
        QrCodeUsage qrCodeUsage = qrCodeUsageMapper.toEntity(qrCodeUsageDTO);
        qrCodeUsage = qrCodeUsageRepository.save(qrCodeUsage);
        return qrCodeUsageMapper.toDto(qrCodeUsage);
    }

    @Override
    public QrCodeUsageDTO update(QrCodeUsageDTO qrCodeUsageDTO) {
        log.debug("Request to update QrCodeUsage : {}", qrCodeUsageDTO);
        QrCodeUsage qrCodeUsage = qrCodeUsageMapper.toEntity(qrCodeUsageDTO);
        qrCodeUsage = qrCodeUsageRepository.save(qrCodeUsage);
        return qrCodeUsageMapper.toDto(qrCodeUsage);
    }

    @Override
    public Optional<QrCodeUsageDTO> partialUpdate(QrCodeUsageDTO qrCodeUsageDTO) {
        log.debug("Request to partially update QrCodeUsage : {}", qrCodeUsageDTO);

        return qrCodeUsageRepository
            .findById(qrCodeUsageDTO.getId())
            .map(existingQrCodeUsage -> {
                qrCodeUsageMapper.partialUpdate(existingQrCodeUsage, qrCodeUsageDTO);

                return existingQrCodeUsage;
            })
            .map(qrCodeUsageRepository::save)
            .map(qrCodeUsageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QrCodeUsageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QrCodeUsages");
        return qrCodeUsageRepository.findAll(pageable).map(qrCodeUsageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QrCodeUsageDTO> findOne(Long id) {
        log.debug("Request to get QrCodeUsage : {}", id);
        return qrCodeUsageRepository.findById(id).map(qrCodeUsageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete QrCodeUsage : {}", id);
        qrCodeUsageRepository.deleteById(id);
    }
}
