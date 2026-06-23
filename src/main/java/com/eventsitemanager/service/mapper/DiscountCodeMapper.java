package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.DiscountCode;
import com.eventsitemanager.service.dto.DiscountCodeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DiscountCode} and its DTO {@link DiscountCodeDTO}.
 */
@Mapper(componentModel = "spring")
public interface DiscountCodeMapper extends EntityMapper<DiscountCodeDTO, DiscountCode> {}
