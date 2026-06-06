package com.nextjstemplate.service;

import com.nextjstemplate.domain.*;
import com.nextjstemplate.repository.EventCompetitionGroupMemberRepository;
import com.nextjstemplate.service.criteria.EventCompetitionGroupMemberCriteria;
import com.nextjstemplate.service.dto.EventCompetitionGroupMemberDTO;
import com.nextjstemplate.service.mapper.EventCompetitionGroupMemberMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

@Service
@Transactional(readOnly = true)
public class EventCompetitionGroupMemberQueryService extends QueryService<EventCompetitionGroupMember> {

    private final Logger log = LoggerFactory.getLogger(EventCompetitionGroupMemberQueryService.class);

    private final EventCompetitionGroupMemberRepository eventCompetitionGroupMemberRepository;

    private final EventCompetitionGroupMemberMapper eventCompetitionGroupMemberMapper;

    public EventCompetitionGroupMemberQueryService(
        EventCompetitionGroupMemberRepository eventCompetitionGroupMemberRepository,
        EventCompetitionGroupMemberMapper eventCompetitionGroupMemberMapper
    ) {
        this.eventCompetitionGroupMemberRepository = eventCompetitionGroupMemberRepository;
        this.eventCompetitionGroupMemberMapper = eventCompetitionGroupMemberMapper;
    }

    public List<EventCompetitionGroupMemberDTO> findByCriteria(EventCompetitionGroupMemberCriteria criteria) {
        final Specification<EventCompetitionGroupMember> specification = createSpecification(criteria);
        return eventCompetitionGroupMemberMapper.toDto(eventCompetitionGroupMemberRepository.findAll(specification));
    }

    public Page<EventCompetitionGroupMemberDTO> findByCriteria(EventCompetitionGroupMemberCriteria criteria, Pageable page) {
        final Specification<EventCompetitionGroupMember> specification = createSpecification(criteria);
        return eventCompetitionGroupMemberRepository.findAll(specification, page).map(eventCompetitionGroupMemberMapper::toDto);
    }

    public long countByCriteria(EventCompetitionGroupMemberCriteria criteria) {
        return eventCompetitionGroupMemberRepository.count(createSpecification(criteria));
    }

    protected Specification<EventCompetitionGroupMember> createSpecification(EventCompetitionGroupMemberCriteria criteria) {
        Specification<EventCompetitionGroupMember> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventCompetitionGroupMember_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventCompetitionGroupMember_.tenantId));
            }
            if (criteria.getMemberRole() != null) {
                specification = specification.and(buildSpecification(criteria.getMemberRole(), EventCompetitionGroupMember_.memberRole));
            }
            if (criteria.getSortOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSortOrder(), EventCompetitionGroupMember_.sortOrder));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventCompetitionGroupMember_.createdAt));
            }
            if (criteria.getRegistrationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRegistrationId(),
                            root ->
                                root.join(EventCompetitionGroupMember_.registration, JoinType.LEFT).get(EventCompetitionRegistration_.id)
                        )
                    );
            }
            if (criteria.getParticipantProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getParticipantProfileId(),
                            root ->
                                root
                                    .join(EventCompetitionGroupMember_.participantProfile, JoinType.LEFT)
                                    .get(EventCompetitionParticipant_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
