package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.CommunicationCampaign;
import com.eventsitemanager.domain.EmailLog;
import com.eventsitemanager.service.dto.CommunicationCampaignDTO;
import com.eventsitemanager.service.dto.EmailLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmailLog} and its DTO {@link EmailLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmailLogMapper extends EntityMapper<EmailLogDTO, EmailLog> {
    @Mapping(target = "campaign", source = "campaign", qualifiedByName = "communicationCampaignId")
    EmailLogDTO toDto(EmailLog s);

    @Named("communicationCampaignId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CommunicationCampaignDTO toDtoCommunicationCampaignId(CommunicationCampaign communicationCampaign);
}
