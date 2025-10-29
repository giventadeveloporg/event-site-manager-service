package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.FocusGroup;
import com.nextjstemplate.service.dto.FocusGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FocusGroup} and its DTO {@link FocusGroupDTO}.
 */
@Mapper(componentModel = "spring")
public interface FocusGroupMapper extends EntityMapper<FocusGroupDTO, FocusGroup> {}
