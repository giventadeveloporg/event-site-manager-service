package com.eventsitemanager.repository;

import com.eventsitemanager.domain.EventAgendaItem;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventAgendaItem entity.
 */
@Repository
public interface EventAgendaItemRepository extends JpaRepository<EventAgendaItem, Long>, JpaSpecificationExecutor<EventAgendaItem> {
    List<EventAgendaItem> findByEventIdOrderBySortOrderAsc(Long eventId);

    List<EventAgendaItem> findByEventIdAndIsPublishedTrueOrderBySortOrderAsc(Long eventId);
}
