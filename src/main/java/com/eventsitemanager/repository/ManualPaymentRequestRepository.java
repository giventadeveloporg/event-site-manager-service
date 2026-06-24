package com.eventsitemanager.repository;

import com.eventsitemanager.domain.ManualPaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ManualPaymentRequest entity.
 */
@Repository
public interface ManualPaymentRequestRepository
    extends JpaRepository<ManualPaymentRequest, Long>, JpaSpecificationExecutor<ManualPaymentRequest> {}
