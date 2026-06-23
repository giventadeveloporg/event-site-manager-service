package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.UserPaymentTransaction;
import com.eventsitemanager.repository.UserPaymentTransactionRepository;
import com.eventsitemanager.service.UserPaymentTransactionService;
import com.eventsitemanager.service.dto.UserPaymentTransactionDTO;
import com.eventsitemanager.service.mapper.UserPaymentTransactionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eventsitemanager.domain.UserPaymentTransaction}.
 */
@Service
@Transactional
public class UserPaymentTransactionServiceImpl implements UserPaymentTransactionService {

    private final Logger log = LoggerFactory.getLogger(UserPaymentTransactionServiceImpl.class);

    private final UserPaymentTransactionRepository userPaymentTransactionRepository;

    private final UserPaymentTransactionMapper userPaymentTransactionMapper;

    public UserPaymentTransactionServiceImpl(
        UserPaymentTransactionRepository userPaymentTransactionRepository,
        UserPaymentTransactionMapper userPaymentTransactionMapper
    ) {
        this.userPaymentTransactionRepository = userPaymentTransactionRepository;
        this.userPaymentTransactionMapper = userPaymentTransactionMapper;
    }

    @Override
    public UserPaymentTransactionDTO save(UserPaymentTransactionDTO userPaymentTransactionDTO) {
        log.debug("Request to save UserPaymentTransaction : {}", userPaymentTransactionDTO);
        UserPaymentTransaction userPaymentTransaction = userPaymentTransactionMapper.toEntity(userPaymentTransactionDTO);
        userPaymentTransaction = userPaymentTransactionRepository.save(userPaymentTransaction);
        return userPaymentTransactionMapper.toDto(userPaymentTransaction);
    }

    @Override
    public UserPaymentTransactionDTO update(UserPaymentTransactionDTO userPaymentTransactionDTO) {
        log.debug("Request to update UserPaymentTransaction : {}", userPaymentTransactionDTO);
        UserPaymentTransaction userPaymentTransaction = userPaymentTransactionMapper.toEntity(userPaymentTransactionDTO);
        userPaymentTransaction = userPaymentTransactionRepository.save(userPaymentTransaction);
        return userPaymentTransactionMapper.toDto(userPaymentTransaction);
    }

    @Override
    public Optional<UserPaymentTransactionDTO> partialUpdate(UserPaymentTransactionDTO userPaymentTransactionDTO) {
        log.debug("Request to partially update UserPaymentTransaction : {}", userPaymentTransactionDTO);

        return userPaymentTransactionRepository
            .findById(userPaymentTransactionDTO.getId())
            .map(existingUserPaymentTransaction -> {
                userPaymentTransactionMapper.partialUpdate(existingUserPaymentTransaction, userPaymentTransactionDTO);

                return existingUserPaymentTransaction;
            })
            .map(userPaymentTransactionRepository::save)
            .map(userPaymentTransactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserPaymentTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserPaymentTransactions");
        return userPaymentTransactionRepository.findAll(pageable).map(userPaymentTransactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserPaymentTransactionDTO> findOne(Long id) {
        log.debug("Request to get UserPaymentTransaction : {}", id);
        return userPaymentTransactionRepository.findById(id).map(userPaymentTransactionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserPaymentTransaction : {}", id);
        userPaymentTransactionRepository.deleteById(id);
    }
}
