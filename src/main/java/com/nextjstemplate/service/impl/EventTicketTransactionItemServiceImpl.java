package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventTicketTransactionItem;
import com.nextjstemplate.repository.EventTicketTransactionItemRepository;
import com.nextjstemplate.service.EventTicketTransactionItemService;
import com.nextjstemplate.service.dto.EventTicketTransactionItemDTO;
import com.nextjstemplate.service.mapper.EventTicketTransactionItemMapper;
import com.nextjstemplate.service.QRCodeService;
import com.nextjstemplate.service.EventTicketTransactionService;
import com.nextjstemplate.service.dto.EventTicketTransactionDTO;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing
 * {@link com.nextjstemplate.domain.EventTicketTransactionItem}.
 */
@Service
@Transactional
public class EventTicketTransactionItemServiceImpl implements EventTicketTransactionItemService {

    @Value("${qrcode.scan-host.url-prefix}")
    private String qrScanUrlPrefix;

    private final Logger log = LoggerFactory.getLogger(EventTicketTransactionItemServiceImpl.class);

    private final EventTicketTransactionItemRepository eventTicketTransactionItemRepository;

    private final EventTicketTransactionItemMapper eventTicketTransactionItemMapper;

    private final QRCodeService qrCodeService;

    private final EventTicketTransactionService eventTicketTransactionService;


    public EventTicketTransactionItemServiceImpl(
            EventTicketTransactionItemRepository eventTicketTransactionItemRepository,
            EventTicketTransactionItemMapper eventTicketTransactionItemMapper,
            QRCodeService qrCodeService,
            EventTicketTransactionService eventTicketTransactionService) {
        this.eventTicketTransactionItemRepository = eventTicketTransactionItemRepository;
        this.eventTicketTransactionItemMapper = eventTicketTransactionItemMapper;
        this.qrCodeService = qrCodeService;
        this.eventTicketTransactionService = eventTicketTransactionService;
    }

    @Override
    public EventTicketTransactionItemDTO save(EventTicketTransactionItemDTO eventTicketTransactionItemDTO) {
        log.debug("Request to save EventTicketTransactionItem : {}", eventTicketTransactionItemDTO);
        EventTicketTransactionItem eventTicketTransactionItem = eventTicketTransactionItemMapper
                .toEntity(eventTicketTransactionItemDTO);
        eventTicketTransactionItem = eventTicketTransactionItemRepository.save(eventTicketTransactionItem);
        return eventTicketTransactionItemMapper.toDto(eventTicketTransactionItem);
    }

    @Override
    public EventTicketTransactionItemDTO update(EventTicketTransactionItemDTO eventTicketTransactionItemDTO) {
        log.debug("Request to update EventTicketTransactionItem : {}", eventTicketTransactionItemDTO);
        EventTicketTransactionItem eventTicketTransactionItem = eventTicketTransactionItemMapper
                .toEntity(eventTicketTransactionItemDTO);
        eventTicketTransactionItem = eventTicketTransactionItemRepository.save(eventTicketTransactionItem);
        return eventTicketTransactionItemMapper.toDto(eventTicketTransactionItem);
    }

    @Override
    public Optional<EventTicketTransactionItemDTO> partialUpdate(
            EventTicketTransactionItemDTO eventTicketTransactionItemDTO) {
        log.debug("Request to partially update EventTicketTransactionItem : {}", eventTicketTransactionItemDTO);

        return eventTicketTransactionItemRepository
                .findById(eventTicketTransactionItemDTO.getId())
                .map(existingEventTicketTransactionItem -> {
                    eventTicketTransactionItemMapper.partialUpdate(existingEventTicketTransactionItem,
                            eventTicketTransactionItemDTO);

                    return existingEventTicketTransactionItem;
                })
                .map(eventTicketTransactionItemRepository::save)
                .map(eventTicketTransactionItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventTicketTransactionItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventTicketTransactionItems");
        return eventTicketTransactionItemRepository.findAll(pageable).map(eventTicketTransactionItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventTicketTransactionItemDTO> findOne(Long id) {
        log.debug("Request to get EventTicketTransactionItem : {}", id);
        return eventTicketTransactionItemRepository.findById(id).map(eventTicketTransactionItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventTicketTransactionItem : {}", id);
        eventTicketTransactionItemRepository.deleteById(id);
    }

    @Override
    public List<EventTicketTransactionItemDTO> saveAll(
            List<EventTicketTransactionItemDTO> eventTicketTransactionItemDTOs) {
        log.debug("Request to save bulk EventTicketTransactionItems : {}", eventTicketTransactionItemDTOs);
        List<EventTicketTransactionItem> entities = eventTicketTransactionItemDTOs.stream()
                .map(eventTicketTransactionItemMapper::toEntity)
                .collect(Collectors.toList());
        List<EventTicketTransactionItem> savedEntities = eventTicketTransactionItemRepository.saveAll(entities);
        // Generate QR code for each unique transaction
        savedEntities.stream()
                .map(EventTicketTransactionItem::getTransactionId)
                .distinct()
                .forEach(transactionId -> {
                    try {
                        Optional<EventTicketTransactionDTO> transactionOpt = eventTicketTransactionService
                                .findOne(transactionId);
                        if (transactionOpt.isPresent()) {
                            EventTicketTransactionDTO transaction = transactionOpt.orElseThrow();
                            String qrScanUrlContent = qrScanUrlPrefix +
                                "/qrcode-scan/tickets"+"/events/"+transaction.getEventId()+
                                "/transactions/"+transaction.getId(); // or any QR
                                                                                                         // content
                                                                                                         // logic
                            String qrCodeImageUrl = qrCodeService.generateAndUploadQRCode(
                                    qrScanUrlContent,
                                    transaction.getEventId(),
                                    String.valueOf(transaction.getId()),
                                    transaction.getTenantId());
                            transaction.setQrCodeImageUrl(qrCodeImageUrl);
                            eventTicketTransactionService.update(transaction);
                        }
                    } catch (Exception e) {
                        log.error("Failed to generate/upload QR code for transactionId {}: {}", transactionId,
                                e.getMessage(), e);
                    }
                });
        return savedEntities.stream().map(eventTicketTransactionItemMapper::toDto).collect(Collectors.toList());
    }
}
