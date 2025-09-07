package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventAttendee;
import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.service.dto.EventAttendeeDTO;
import com.nextjstemplate.service.dto.EventDetailsDTO;
import com.nextjstemplate.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventAttendee} and its DTO {@link EventAttendeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventAttendeeMapper extends EntityMapper<EventAttendeeDTO, EventAttendee> {
    /* @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
    @Mapping(target = "attendee", source = "attendee", qualifiedByName = "userProfileId")
*/
    //   EventAttendeeDTO toDto(EventAttendee s);

    /* @Override
    @Mappings({
        @Mapping(target = "firstName", source = "firstName"),
        @Mapping(target = "lastName", source = "lastName"),
        @Mapping(target = "email", source = "email"),
        @Mapping(target = "phone", source = "phone"),
        @Mapping(target = "isMember", source = "isMember"),
        @Mapping(target = "tenantId", source = "tenantId"),
        @Mapping(target = "registrationStatus", source = "registrationStatus"),
        @Mapping(target = "registrationDate", source = "registrationDate"),
        @Mapping(target = "confirmationDate", source = "confirmationDate"),
        @Mapping(target = "cancellationDate", source = "cancellationDate"),
        @Mapping(target = "cancellationReason", source = "cancellationReason"),
        @Mapping(target = "attendeeType", source = "attendeeType"),
        @Mapping(target = "specialRequirements", source = "specialRequirements"),
        @Mapping(target = "emergencyContactName", source = "emergencyContactName"),
        @Mapping(target = "emergencyContactPhone", source = "emergencyContactPhone"),
        @Mapping(target = "checkInStatus", source = "checkInStatus"),
        @Mapping(target = "checkInTime", source = "checkInTime"),
        @Mapping(target = "notes", source = "notes"),
        @Mapping(target = "qrCodeData", source = "qrCodeData"),
        @Mapping(target = "qrCodeGenerated", source = "qrCodeGenerated"),
        @Mapping(target = "qrCodeGeneratedAt", source = "qrCodeGeneratedAt"),
        @Mapping(target = "createdAt", source = "createdAt"),
        @Mapping(target = "updatedAt", source = "updatedAt"),
        @Mapping(target = "eventId", source = "eventId"),
        @Mapping(target = "attendeeId", source = "attendeeId")
        // ...add all other fields as needed
    })
    EventAttendee toEntity(EventAttendeeDTO dto);*/

    @Named("eventDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDetailsDTO toDtoEventDetailsId(EventDetails eventDetails);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
