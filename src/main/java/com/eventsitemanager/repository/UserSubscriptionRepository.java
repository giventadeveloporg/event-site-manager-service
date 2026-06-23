package com.eventsitemanager.repository;

import com.eventsitemanager.domain.UserSubscription;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long>, JpaSpecificationExecutor<UserSubscription> {
    List<UserSubscription> findByUserProfileId(Long userProfileId);
}
