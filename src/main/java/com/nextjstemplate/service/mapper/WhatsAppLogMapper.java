package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.CommunicationCampaign;
import com.nextjstemplate.domain.WhatsAppLog;
import com.nextjstemplate.service.dto.CommunicationCampaignDTO;
import com.nextjstemplate.service.dto.WhatsAppLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WhatsAppLog} and its DTO {@link WhatsAppLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface WhatsAppLogMapper extends EntityMapper<WhatsAppLogDTO, WhatsAppLog> {
    @Mapping(target = "campaign", source = "campaign", qualifiedByName = "communicationCampaignId")
    WhatsAppLogDTO toDto(WhatsAppLog s);

    @Named("communicationCampaignId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CommunicationCampaignDTO toDtoCommunicationCampaignId(CommunicationCampaign communicationCampaign);
}
