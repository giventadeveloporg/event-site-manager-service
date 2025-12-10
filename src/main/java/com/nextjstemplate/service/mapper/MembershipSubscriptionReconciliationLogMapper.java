package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.MembershipSubscription;
import com.nextjstemplate.domain.MembershipSubscriptionReconciliationLog;
import com.nextjstemplate.service.dto.MembershipSubscriptionDTO;
import com.nextjstemplate.service.dto.MembershipSubscriptionReconciliationLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MembershipSubscriptionReconciliationLog} and its DTO {@link MembershipSubscriptionReconciliationLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface MembershipSubscriptionReconciliationLogMapper
    extends EntityMapper<MembershipSubscriptionReconciliationLogDTO, MembershipSubscriptionReconciliationLog> {
    @Mapping(target = "subscriptionId", source = "subscription.id")
    @Mapping(target = "subscription", ignore = true)
    MembershipSubscriptionReconciliationLogDTO toDto(MembershipSubscriptionReconciliationLog entity);

    @Mapping(target = "subscription", source = "subscriptionId", qualifiedByName = "membershipSubscriptionFromId")
    MembershipSubscriptionReconciliationLog toEntity(MembershipSubscriptionReconciliationLogDTO dto);

    @Named("membershipSubscriptionFromId")
    default MembershipSubscription membershipSubscriptionFromId(Long id) {
        if (id == null) {
            return null;
        }
        MembershipSubscription membershipSubscription = new MembershipSubscription();
        membershipSubscription.setId(id);
        return membershipSubscription;
    }
}
