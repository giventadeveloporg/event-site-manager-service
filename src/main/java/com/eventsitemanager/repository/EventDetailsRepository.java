package com.eventsitemanager.repository;

import com.eventsitemanager.domain.EventDetails;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventDetails entity.
 *
 * When extending this class, extend EventDetailsRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface EventDetailsRepository
    extends EventDetailsRepositoryWithBagRelationships, JpaRepository<EventDetails, Long>, JpaSpecificationExecutor<EventDetails> {
    default Optional<EventDetails> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<EventDetails> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<EventDetails> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
