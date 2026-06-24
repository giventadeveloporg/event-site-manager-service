package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.UserRegistrationRequest;
import com.eventsitemanager.repository.UserRegistrationRequestRepository;
import com.eventsitemanager.service.UserRegistrationRequestService;
import com.eventsitemanager.service.dto.UserRegistrationRequestDTO;
import com.eventsitemanager.service.mapper.UserRegistrationRequestMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eventsitemanager.domain.UserRegistrationRequest}.
 */
@Service
@Transactional
public class UserRegistrationRequestServiceImpl implements UserRegistrationRequestService {

    private final Logger log = LoggerFactory.getLogger(UserRegistrationRequestServiceImpl.class);

    private final UserRegistrationRequestRepository userRegistrationRequestRepository;

    private final UserRegistrationRequestMapper userRegistrationRequestMapper;

    public UserRegistrationRequestServiceImpl(
        UserRegistrationRequestRepository userRegistrationRequestRepository,
        UserRegistrationRequestMapper userRegistrationRequestMapper
    ) {
        this.userRegistrationRequestRepository = userRegistrationRequestRepository;
        this.userRegistrationRequestMapper = userRegistrationRequestMapper;
    }

    @Override
    public UserRegistrationRequestDTO save(UserRegistrationRequestDTO userRegistrationRequestDTO) {
        log.debug("Request to save UserRegistrationRequest : {}", userRegistrationRequestDTO);
        UserRegistrationRequest userRegistrationRequest = userRegistrationRequestMapper.toEntity(userRegistrationRequestDTO);
        userRegistrationRequest = userRegistrationRequestRepository.save(userRegistrationRequest);
        return userRegistrationRequestMapper.toDto(userRegistrationRequest);
    }

    @Override
    public UserRegistrationRequestDTO update(UserRegistrationRequestDTO userRegistrationRequestDTO) {
        log.debug("Request to update UserRegistrationRequest : {}", userRegistrationRequestDTO);
        UserRegistrationRequest userRegistrationRequest = userRegistrationRequestMapper.toEntity(userRegistrationRequestDTO);
        userRegistrationRequest = userRegistrationRequestRepository.save(userRegistrationRequest);
        return userRegistrationRequestMapper.toDto(userRegistrationRequest);
    }

    @Override
    public Optional<UserRegistrationRequestDTO> partialUpdate(UserRegistrationRequestDTO userRegistrationRequestDTO) {
        log.debug("Request to partially update UserRegistrationRequest : {}", userRegistrationRequestDTO);

        return userRegistrationRequestRepository
            .findById(userRegistrationRequestDTO.getId())
            .map(existingUserRegistrationRequest -> {
                userRegistrationRequestMapper.partialUpdate(existingUserRegistrationRequest, userRegistrationRequestDTO);

                return existingUserRegistrationRequest;
            })
            .map(userRegistrationRequestRepository::save)
            .map(userRegistrationRequestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserRegistrationRequestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserRegistrationRequests");
        return userRegistrationRequestRepository.findAll(pageable).map(userRegistrationRequestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserRegistrationRequestDTO> findOne(Long id) {
        log.debug("Request to get UserRegistrationRequest : {}", id);
        return userRegistrationRequestRepository.findById(id).map(userRegistrationRequestMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserRegistrationRequest : {}", id);
        userRegistrationRequestRepository.deleteById(id);
    }
}
