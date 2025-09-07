package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventTicketTransaction;
import com.nextjstemplate.repository.EventTicketTransactionRepository;
import com.nextjstemplate.service.EventTicketTransactionService;
import com.nextjstemplate.service.dto.EventTicketTransactionDTO;
import com.nextjstemplate.service.dto.EventTicketTransactionStatisticsDTO;
import com.nextjstemplate.service.mapper.EventTicketTransactionMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing
 * {@link com.nextjstemplate.domain.EventTicketTransaction}.
 */
@Service
@Transactional
public class EventTicketTransactionServiceImpl implements EventTicketTransactionService {

    private final Logger log = LoggerFactory.getLogger(EventTicketTransactionServiceImpl.class);

    private final EventTicketTransactionRepository eventTicketTransactionRepository;

    private final EventTicketTransactionMapper eventTicketTransactionMapper;

    public EventTicketTransactionServiceImpl(
        EventTicketTransactionRepository eventTicketTransactionRepository,
        EventTicketTransactionMapper eventTicketTransactionMapper
    ) {
        this.eventTicketTransactionRepository = eventTicketTransactionRepository;
        this.eventTicketTransactionMapper = eventTicketTransactionMapper;
    }

    @Override
    public EventTicketTransactionDTO save(EventTicketTransactionDTO eventTicketTransactionDTO) {
        log.debug("Request to save EventTicketTransaction : {}", eventTicketTransactionDTO);
        EventTicketTransaction eventTicketTransaction = eventTicketTransactionMapper.toEntity(eventTicketTransactionDTO);
        eventTicketTransaction = eventTicketTransactionRepository.save(eventTicketTransaction);
        return eventTicketTransactionMapper.toDto(eventTicketTransaction);
    }

    @Override
    public EventTicketTransactionDTO update(EventTicketTransactionDTO eventTicketTransactionDTO) {
        log.debug("Request to update EventTicketTransaction : {}", eventTicketTransactionDTO);
        EventTicketTransaction eventTicketTransaction = eventTicketTransactionMapper.toEntity(eventTicketTransactionDTO);
        eventTicketTransaction = eventTicketTransactionRepository.save(eventTicketTransaction);
        return eventTicketTransactionMapper.toDto(eventTicketTransaction);
    }

    @Override
    public Optional<EventTicketTransactionDTO> partialUpdate(EventTicketTransactionDTO eventTicketTransactionDTO) {
        log.debug("Request to partially update EventTicketTransaction : {}", eventTicketTransactionDTO);

        return eventTicketTransactionRepository
            .findById(eventTicketTransactionDTO.getId())
            .map(existingEventTicketTransaction -> {
                eventTicketTransactionMapper.partialUpdate(existingEventTicketTransaction, eventTicketTransactionDTO);

                return existingEventTicketTransaction;
            })
            .map(eventTicketTransactionRepository::save)
            .map(eventTicketTransactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventTicketTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventTicketTransactions");
        return eventTicketTransactionRepository.findAll(pageable).map(eventTicketTransactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventTicketTransactionDTO> findOne(Long id) {
        log.debug("Request to get EventTicketTransaction : {}", id);
        return eventTicketTransactionRepository.findById(id).map(eventTicketTransactionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventTicketTransaction : {}", id);
        eventTicketTransactionRepository.deleteById(id);
    }

    @Override
    public EventTicketTransactionStatisticsDTO getEventStatistics(Long eventId) {
        List<EventTicketTransaction> transactions = eventTicketTransactionRepository
            .findAll()
            .stream()
            .filter(t -> t.getEventId() != null && t.getEventId().equals(eventId))
            .collect(Collectors.toList());
        int totalTicketsSold = transactions.stream().mapToInt(t -> t.getQuantity() != null ? t.getQuantity() : 0).sum();
        BigDecimal totalAmount = transactions
            .stream()
            .map(t -> t.getFinalAmount() != null ? t.getFinalAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalFees = transactions
            .stream()
            .map(t -> {
                BigDecimal platformFee = t.getPlatformFeeAmount() != null ? t.getPlatformFeeAmount() : BigDecimal.ZERO;
                BigDecimal stripeFee = t.getStripeFeeAmount() != null ? t.getStripeFeeAmount() : BigDecimal.ZERO;
                return platformFee.add(stripeFee);
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netAmount = totalAmount.subtract(totalFees);
        Map<String, Integer> ticketsByStatus = transactions
            .stream()
            .collect(
                Collectors.groupingBy(
                    t -> t.getStatus() != null ? t.getStatus() : "UNKNOWN",
                    Collectors.summingInt(t -> t.getQuantity() != null ? t.getQuantity() : 0)
                )
            );
        Map<String, BigDecimal> amountByStatus = transactions
            .stream()
            .collect(
                Collectors.groupingBy(
                    t -> t.getStatus() != null ? t.getStatus() : "UNKNOWN",
                    Collectors.reducing(
                        BigDecimal.ZERO,
                        t -> t.getFinalAmount() != null ? t.getFinalAmount() : BigDecimal.ZERO,
                        BigDecimal::add
                    )
                )
            );
        EventTicketTransactionStatisticsDTO stats = new EventTicketTransactionStatisticsDTO();
        stats.setEventId(eventId);
        stats.setTotalTicketsSold(totalTicketsSold);
        stats.setTotalAmount(totalAmount);
        stats.setNetAmount(netAmount);
        stats.setTicketsByStatus(ticketsByStatus);
        stats.setAmountByStatus(amountByStatus);
        return stats;
    }
}
