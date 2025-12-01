package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.MembershipPlan;
import com.nextjstemplate.service.dto.MembershipPlanDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MembershipPlan} and its DTO {@link MembershipPlanDTO}.
 */
@Mapper(componentModel = "spring")
public interface MembershipPlanMapper extends EntityMapper<MembershipPlanDTO, MembershipPlan> {
    @Override
    MembershipPlanDTO toDto(MembershipPlan entity);

    @Override
    MembershipPlan toEntity(MembershipPlanDTO dto);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MembershipPlanDTO toDtoId(MembershipPlan membershipPlan);

    @Named("planName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "planName", source = "planName")
    @Mapping(target = "planCode", source = "planCode")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "billingInterval", source = "billingInterval")
    MembershipPlanDTO toDtoPlanName(MembershipPlan membershipPlan);
}
