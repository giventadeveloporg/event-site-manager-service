package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventTicketTransactionItem;
import com.nextjstemplate.domain.EventTicketType;
import com.nextjstemplate.repository.EventTicketTransactionItemRepository;
import com.nextjstemplate.repository.EventTicketTypeRepository;
import com.nextjstemplate.service.EventTicketTransactionItemService;
import com.nextjstemplate.service.dto.EventTicketTransactionItemDTO;
import com.nextjstemplate.service.mapper.EventTicketTransactionItemMapper;
import com.nextjstemplate.service.QRCodeService;
import com.nextjstemplate.service.EventTicketTransactionService;
import com.nextjstemplate.service.dto.EventTicketTransactionDTO;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
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

    private final EventTicketTypeRepository eventTicketTypeRepository;

    public EventTicketTransactionItemServiceImpl(
            EventTicketTransactionItemRepository eventTicketTransactionItemRepository,
            EventTicketTransactionItemMapper eventTicketTransactionItemMapper,
            QRCodeService qrCodeService,
            EventTicketTransactionService eventTicketTransactionService,
            EventTicketTypeRepository eventTicketTypeRepository) {
        this.eventTicketTransactionItemRepository = eventTicketTransactionItemRepository;
        this.eventTicketTransactionItemMapper = eventTicketTransactionItemMapper;
        this.qrCodeService = qrCodeService;
        this.eventTicketTransactionService = eventTicketTransactionService;
        this.eventTicketTypeRepository = eventTicketTypeRepository;
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
        
        // Update ticket type quantities for COMPLETED transactions only
        updateTicketTypeQuantities(savedEntities);
        
        // Generate QR code for each unique transaction
        savedEntities.stream()
                .map(EventTicketTransactionItem::getTransactionId)
                .distinct()
                .forEach(transactionId -> {
                    try {
                        Optional<EventTicketTransactionDTO> transactionOpt = eventTicketTransactionService
                                .findOne(transactionId);
                        transactionOpt.ifPresent(transaction -> {
                            try {
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
                            } catch (Exception e) {
                                log.error("Failed to generate/upload QR code for transaction {}: {}", transaction.getId(),
                                        e.getMessage(), e);
                            }
                        });
                    } catch (Exception e) {
                        log.error("Failed to generate/upload QR code for transactionId {}: {}", transactionId,
                                e.getMessage(), e);
                    }
                });
        return savedEntities.stream().map(eventTicketTransactionItemMapper::toDto).collect(Collectors.toList());
    }

    private void updateTicketTypeQuantities(List<EventTicketTransactionItem> savedEntities) {
        try {
            // Get unique ticket type IDs and their associated event IDs from the saved entities
            Map<Long, Long> ticketTypeToEventMap = savedEntities.stream()
                .collect(Collectors.groupingBy(EventTicketTransactionItem::getTicketTypeId))
                .entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> {
                        // Get event ID from the first transaction in the group
                        Long transactionId = entry.getValue().get(0).getTransactionId();
                        Optional<EventTicketTransactionDTO> transactionOpt = eventTicketTransactionService.findOne(transactionId);
                        return transactionOpt.map(EventTicketTransactionDTO::getEventId).orElse(null);
                    }
                ));

            // Update quantities for each ticket type
            ticketTypeToEventMap.forEach((ticketTypeId, eventId) -> {
                if (eventId != null) {
                    updateTicketTypeQuantityForEvent(ticketTypeId, eventId);
                }
            });

        } catch (Exception e) {
            log.error("Failed to update ticket type quantities: {}", e.getMessage(), e);
        }
    }

    private void updateTicketTypeQuantityForEvent(Long ticketTypeId, Long eventId) {
        try {
            eventTicketTypeRepository.findById(ticketTypeId).ifPresent(ticketType -> {
                // Calculate sold quantity by summing quantities from COMPLETED transactions
                Integer soldQuantity = eventTicketTransactionItemRepository.findAll().stream()
                    .filter(item -> item.getTicketTypeId().equals(ticketTypeId))
                    .filter(item -> {
                        // Check if the transaction is COMPLETED
                        Optional<EventTicketTransactionDTO> transactionOpt = eventTicketTransactionService.findOne(item.getTransactionId());
                        return transactionOpt.map(transaction -> 
                            "COMPLETED".equals(transaction.getStatus()) && eventId.equals(transaction.getEventId())
                        ).orElse(false);
                    })
                    .mapToInt(EventTicketTransactionItem::getQuantity)
                    .sum();

                // Calculate remaining quantity
                Integer availableQuantity = ticketType.getAvailableQuantity();
                Integer remainingQuantity = availableQuantity != null ? 
                    Math.max(0, availableQuantity - soldQuantity) : null;

                // Update the ticket type
                ticketType.setSoldQuantity(soldQuantity);
                ticketType.setRemainingQuantity(remainingQuantity);
                ticketType.setUpdatedAt(ZonedDateTime.now());

                eventTicketTypeRepository.save(ticketType);
                
                log.debug("Updated ticket type {} quantities: sold={}, remaining={}", 
                    ticketTypeId, soldQuantity, remainingQuantity);
            });
        } catch (Exception e) {
            log.error("Failed to update quantities for ticket type {}: {}", ticketTypeId, e.getMessage(), e);
        }
    }
}
