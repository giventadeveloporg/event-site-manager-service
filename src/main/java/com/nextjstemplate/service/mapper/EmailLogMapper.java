package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.CommunicationCampaign;
import com.nextjstemplate.domain.EmailLog;
import com.nextjstemplate.service.dto.CommunicationCampaignDTO;
import com.nextjstemplate.service.dto.EmailLogDTO;
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
