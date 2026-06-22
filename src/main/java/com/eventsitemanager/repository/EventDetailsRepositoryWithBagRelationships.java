package com.eventsitemanager.repository;

import com.eventsitemanager.domain.EventDetails;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface EventDetailsRepositoryWithBagRelationships {
    Optional<EventDetails> fetchBagRelationships(Optional<EventDetails> eventDetails);

    List<EventDetails> fetchBagRelationships(List<EventDetails> eventDetails);

    Page<EventDetails> fetchBagRelationships(Page<EventDetails> eventDetails);
}
