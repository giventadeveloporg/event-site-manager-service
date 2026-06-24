package com.eventsitemanager.repository;

import com.eventsitemanager.domain.DiscountCode;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DiscountCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiscountCodeRepository extends JpaRepository<DiscountCode, Long>, JpaSpecificationExecutor<DiscountCode> {}
