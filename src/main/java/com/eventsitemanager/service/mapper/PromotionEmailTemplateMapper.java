package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.PromotionEmailTemplate;
import com.eventsitemanager.service.dto.PromotionEmailTemplateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PromotionEmailTemplate} and its DTO {@link PromotionEmailTemplateDTO}.
 */
@Mapper(componentModel = "spring", uses = { EventDetailsMapper.class, DiscountCodeMapper.class, UserProfileMapper.class })
public interface PromotionEmailTemplateMapper extends EntityMapper<PromotionEmailTemplateDTO, PromotionEmailTemplate> {
    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "discountCodeId", source = "discountCode.id")
    @Mapping(target = "createdById", source = "createdBy.id")
    PromotionEmailTemplateDTO toDto(PromotionEmailTemplate entity);

    @Mapping(target = "event", ignore = true)
    @Mapping(target = "discountCode", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "eventId", source = "eventId")
    @Mapping(target = "discountCodeId", source = "discountCodeId")
    @Mapping(target = "createdById", source = "createdById")
    PromotionEmailTemplate toEntity(PromotionEmailTemplateDTO dto);
}
