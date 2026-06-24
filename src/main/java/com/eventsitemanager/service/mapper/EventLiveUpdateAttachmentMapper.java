package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventLiveUpdate;
import com.eventsitemanager.domain.EventLiveUpdateAttachment;
import com.eventsitemanager.service.dto.EventLiveUpdateAttachmentDTO;
import com.eventsitemanager.service.dto.EventLiveUpdateDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper for the entity {@link EventLiveUpdateAttachment} and its DTO {@link EventLiveUpdateAttachmentDTO}.
 * <p>
 * DTO→entity must map {@code liveUpdate} by reference (id only). Deep mapping into {@code EventLiveUpdate#event}
 * ({@code EventDetails} collections, etc.) triggers a MapStruct 1.5.x code generation bug (invalid if/else for
 * {@code discountCodes}) and is unnecessary for REST persistence (Hibernate resolves {@code @ManyToOne} by id).
 */
@Mapper(componentModel = "spring")
public interface EventLiveUpdateAttachmentMapper extends EntityMapper<EventLiveUpdateAttachmentDTO, EventLiveUpdateAttachment> {
    @Mapping(target = "liveUpdate", source = "liveUpdate", qualifiedByName = "eventLiveUpdateId")
    EventLiveUpdateAttachmentDTO toDto(EventLiveUpdateAttachment s);

    @Named("eventLiveUpdateId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventLiveUpdateDTO toDtoEventLiveUpdateId(EventLiveUpdate eventLiveUpdate);

    @Override
    @Mapping(target = "liveUpdate", source = "liveUpdate", qualifiedByName = "eventLiveUpdateEntityRef")
    EventLiveUpdateAttachment toEntity(EventLiveUpdateAttachmentDTO dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "liveUpdate", source = "liveUpdate", qualifiedByName = "eventLiveUpdateEntityRef")
    void partialUpdate(@MappingTarget EventLiveUpdateAttachment entity, EventLiveUpdateAttachmentDTO dto);

    /**
     * Id-only {@link EventLiveUpdate} for {@code @ManyToOne} — avoids deep {@link com.eventsitemanager.domain.EventDetails} mapping.
     */
    @Named("eventLiveUpdateEntityRef")
    default EventLiveUpdate toEventLiveUpdateEntityRef(EventLiveUpdateDTO dto) {
        if (dto == null) {
            return null;
        }
        EventLiveUpdate ref = new EventLiveUpdate();
        if (dto.getId() != null) {
            ref.setId(dto.getId());
        }
        return ref;
    }
}
