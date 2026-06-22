package com.eventsitemanager.repository;

import com.eventsitemanager.domain.EventDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class EventDetailsRepositoryWithBagRelationshipsImpl implements EventDetailsRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<EventDetails> fetchBagRelationships(Optional<EventDetails> eventDetails) {
        return eventDetails.map(this::fetchDiscountCodes);
    }

    @Override
    public Page<EventDetails> fetchBagRelationships(Page<EventDetails> eventDetails) {
        return new PageImpl<>(
            fetchBagRelationships(eventDetails.getContent()),
            eventDetails.getPageable(),
            eventDetails.getTotalElements()
        );
    }

    @Override
    public List<EventDetails> fetchBagRelationships(List<EventDetails> eventDetails) {
        return Optional.of(eventDetails).map(this::fetchDiscountCodes).orElse(Collections.emptyList());
    }

    EventDetails fetchDiscountCodes(EventDetails result) {
        return entityManager
            .createQuery(
                "select eventDetails from EventDetails eventDetails left join fetch eventDetails.discountCodes where eventDetails.id = :id",
                EventDetails.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<EventDetails> fetchDiscountCodes(List<EventDetails> eventDetails) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, eventDetails.size()).forEach(index -> order.put(eventDetails.get(index).getId(), index));
        List<EventDetails> result = entityManager
            .createQuery(
                "select eventDetails from EventDetails eventDetails left join fetch eventDetails.discountCodes where eventDetails in :eventDetails",
                EventDetails.class
            )
            .setParameter("eventDetails", eventDetails)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
