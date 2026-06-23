package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.UserPaymentTransactionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.UserPaymentTransaction}.
 */
public interface UserPaymentTransactionService {
    /**
     * Save a userPaymentTransaction.
     *
     * @param userPaymentTransactionDTO the entity to save.
     * @return the persisted entity.
     */
    UserPaymentTransactionDTO save(UserPaymentTransactionDTO userPaymentTransactionDTO);

    /**
     * Updates a userPaymentTransaction.
     *
     * @param userPaymentTransactionDTO the entity to update.
     * @return the persisted entity.
     */
    UserPaymentTransactionDTO update(UserPaymentTransactionDTO userPaymentTransactionDTO);

    /**
     * Partially updates a userPaymentTransaction.
     *
     * @param userPaymentTransactionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserPaymentTransactionDTO> partialUpdate(UserPaymentTransactionDTO userPaymentTransactionDTO);

    /**
     * Get all the userPaymentTransactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserPaymentTransactionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" userPaymentTransaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserPaymentTransactionDTO> findOne(Long id);

    /**
     * Delete the "id" userPaymentTransaction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
