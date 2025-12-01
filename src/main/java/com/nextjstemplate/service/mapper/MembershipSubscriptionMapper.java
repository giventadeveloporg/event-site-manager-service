package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.MembershipPlan;
import com.nextjstemplate.domain.MembershipSubscription;
import com.nextjstemplate.domain.PaymentProviderConfig;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.service.dto.MembershipPlanDTO;
import com.nextjstemplate.service.dto.MembershipSubscriptionDTO;
import com.nextjstemplate.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MembershipSubscription} and its DTO {@link MembershipSubscriptionDTO}.
 */
@Mapper(componentModel = "spring", uses = { MembershipPlanMapper.class, UserProfileMapper.class })
public interface MembershipSubscriptionMapper extends EntityMapper<MembershipSubscriptionDTO, MembershipSubscription> {
    @Mapping(target = "userProfileId", source = "userProfile.id")
    @Mapping(target = "membershipPlanId", source = "membershipPlan.id")
    @Mapping(target = "paymentProviderConfigId", source = "paymentProviderConfig.id")
    @Mapping(target = "membershipPlan", source = "membershipPlan", qualifiedByName = "planName")
    @Mapping(target = "userProfile", source = "userProfile", qualifiedByName = "id")
    MembershipSubscriptionDTO toDto(MembershipSubscription entity);

    @Mapping(target = "userProfile", source = "userProfileId", qualifiedByName = "userProfileFromId")
    @Mapping(target = "membershipPlan", source = "membershipPlanId", qualifiedByName = "membershipPlanFromId")
    @Mapping(target = "paymentProviderConfig", source = "paymentProviderConfigId", qualifiedByName = "paymentProviderConfigFromId")
    MembershipSubscription toEntity(MembershipSubscriptionDTO dto);

    @Named("userProfileFromId")
    default UserProfile userProfileFromId(Long id) {
        if (id == null) {
            return null;
        }
        UserProfile userProfile = new UserProfile();
        userProfile.setId(id);
        return userProfile;
    }

    @Named("membershipPlanFromId")
    default MembershipPlan membershipPlanFromId(Long id) {
        if (id == null) {
            return null;
        }
        MembershipPlan membershipPlan = new MembershipPlan();
        membershipPlan.setId(id);
        return membershipPlan;
    }

    @Named("paymentProviderConfigFromId")
    default PaymentProviderConfig paymentProviderConfigFromId(Long id) {
        if (id == null) {
            return null;
        }
        PaymentProviderConfig paymentProviderConfig = new PaymentProviderConfig();
        paymentProviderConfig.setId(id);
        return paymentProviderConfig;
    }

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
