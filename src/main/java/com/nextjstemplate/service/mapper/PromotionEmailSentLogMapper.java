package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.PromotionEmailSentLog;
import com.nextjstemplate.service.dto.PromotionEmailSentLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PromotionEmailSentLog} and its DTO {@link PromotionEmailSentLogDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { PromotionEmailTemplateMapper.class, EventDetailsMapper.class, DiscountCodeMapper.class, UserProfileMapper.class }
)
public interface PromotionEmailSentLogMapper extends EntityMapper<PromotionEmailSentLogDTO, PromotionEmailSentLog> {
    @Mapping(target = "templateId", source = "template.id")
    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "discountCodeId", source = "discountCode.id")
    @Mapping(target = "sentById", source = "sentBy.id")
    PromotionEmailSentLogDTO toDto(PromotionEmailSentLog entity);

    @Mapping(target = "template", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "discountCode", ignore = true)
    @Mapping(target = "sentBy", ignore = true)
    @Mapping(target = "templateId", source = "templateId")
    @Mapping(target = "eventId", source = "eventId")
    @Mapping(target = "discountCodeId", source = "discountCodeId")
    @Mapping(target = "sentById", source = "sentById")
    PromotionEmailSentLog toEntity(PromotionEmailSentLogDTO dto);
}

