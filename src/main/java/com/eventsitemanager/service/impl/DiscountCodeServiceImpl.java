package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.DiscountCode;
import com.eventsitemanager.repository.DiscountCodeRepository;
import com.eventsitemanager.service.DiscountCodeService;
import com.eventsitemanager.service.dto.DiscountCodeDTO;
import com.eventsitemanager.service.mapper.DiscountCodeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eventsitemanager.domain.DiscountCode}.
 */
@Service
@Transactional
public class DiscountCodeServiceImpl implements DiscountCodeService {

    private final Logger log = LoggerFactory.getLogger(DiscountCodeServiceImpl.class);

    private final DiscountCodeRepository discountCodeRepository;

    private final DiscountCodeMapper discountCodeMapper;

    public DiscountCodeServiceImpl(DiscountCodeRepository discountCodeRepository, DiscountCodeMapper discountCodeMapper) {
        this.discountCodeRepository = discountCodeRepository;
        this.discountCodeMapper = discountCodeMapper;
    }

    @Override
    public DiscountCodeDTO save(DiscountCodeDTO discountCodeDTO) {
        log.debug("Request to save DiscountCode : {}", discountCodeDTO);
        DiscountCode discountCode = discountCodeMapper.toEntity(discountCodeDTO);
        discountCode = discountCodeRepository.save(discountCode);
        return discountCodeMapper.toDto(discountCode);
    }

    @Override
    public DiscountCodeDTO update(DiscountCodeDTO discountCodeDTO) {
        log.debug("Request to update DiscountCode : {}", discountCodeDTO);
        DiscountCode discountCode = discountCodeMapper.toEntity(discountCodeDTO);
        discountCode = discountCodeRepository.save(discountCode);
        return discountCodeMapper.toDto(discountCode);
    }

    @Override
    public Optional<DiscountCodeDTO> partialUpdate(DiscountCodeDTO discountCodeDTO) {
        log.debug("Request to partially update DiscountCode : {}", discountCodeDTO);

        return discountCodeRepository
            .findById(discountCodeDTO.getId())
            .map(existingDiscountCode -> {
                discountCodeMapper.partialUpdate(existingDiscountCode, discountCodeDTO);

                return existingDiscountCode;
            })
            .map(discountCodeRepository::save)
            .map(discountCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DiscountCodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DiscountCodes");
        return discountCodeRepository.findAll(pageable).map(discountCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DiscountCodeDTO> findOne(Long id) {
        log.debug("Request to get DiscountCode : {}", id);
        return discountCodeRepository.findById(id).map(discountCodeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DiscountCode : {}", id);
        discountCodeRepository.deleteById(id);
    }
}
