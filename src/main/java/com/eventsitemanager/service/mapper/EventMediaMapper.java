package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.EventMedia;
import com.eventsitemanager.domain.GalleryAlbum;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.service.dto.EventDetailsDTO;
import com.eventsitemanager.service.dto.EventMediaDTO;
import com.eventsitemanager.service.dto.GalleryAlbumDTO;
import com.eventsitemanager.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventMedia} and its DTO {@link EventMediaDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventMediaMapper extends EntityMapper<EventMediaDTO, EventMedia> {
    @Mapping(target = "albumId", source = "album.id")
    /*@Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
    @Mapping(target = "uploadedBy", source = "uploadedBy", qualifiedByName = "userProfileId")*/
    EventMediaDTO toDto(EventMedia s);

    @Mapping(target = "album", ignore = true)
    EventMedia toEntity(EventMediaDTO dto);

    default EventMedia fromId(Long id) {
        if (id == null) {
            return null;
        }
        EventMedia eventMedia = new EventMedia();
        eventMedia.setId(id);
        return eventMedia;
    }

    @Named("eventDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDetailsDTO toDtoEventDetailsId(EventDetails eventDetails);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
