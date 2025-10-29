package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventFocusGroup;
import com.nextjstemplate.domain.FocusGroup;
import com.nextjstemplate.service.dto.EventFocusGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventFocusGroup} and its DTO
 * {@link EventFocusGroupDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventFocusGroupMapper extends EntityMapper<EventFocusGroupDTO, EventFocusGroup> {
    @Mapping(target = "focusGroupId", source = "focusGroup.id")
    EventFocusGroupDTO toDto(EventFocusGroup s);

    @Mapping(target = "focusGroup", source = "focusGroupId", qualifiedByName = "focusGroupId")
    EventFocusGroup toEntity(EventFocusGroupDTO dto);

    @Named("focusGroupId")
    default FocusGroup focusGroupFromId(Long id) {
        if (id == null) {
            return null;
        }
        FocusGroup focusGroup = new FocusGroup();
        focusGroup.setId(id);
        return focusGroup;
    }
}
