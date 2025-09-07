package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.UserTask;
import com.nextjstemplate.repository.UserTaskRepository;
import com.nextjstemplate.service.criteria.UserTaskCriteria;
import com.nextjstemplate.service.dto.UserTaskDTO;
import com.nextjstemplate.service.mapper.UserTaskMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link UserTask} entities in the database.
 * The main input is a {@link UserTaskCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link UserTaskDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserTaskQueryService extends QueryService<UserTask> {

    private static final Logger LOG = LoggerFactory.getLogger(UserTaskQueryService.class);

    private final UserTaskRepository userTaskRepository;

    private final UserTaskMapper userTaskMapper;

    public UserTaskQueryService(UserTaskRepository userTaskRepository, UserTaskMapper userTaskMapper) {
        this.userTaskRepository = userTaskRepository;
        this.userTaskMapper = userTaskMapper;
    }

    /**
     * Return a {@link Page} of {@link UserTaskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserTaskDTO> findByCriteria(UserTaskCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserTask> specification = createSpecification(criteria);
        return userTaskRepository.findAll(specification, page).map(userTaskMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserTaskCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<UserTask> specification = createSpecification(criteria);
        return userTaskRepository.count(specification);
    }

    /**
     * Function to convert {@link UserTaskCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserTask> createSpecification(UserTaskCriteria criteria) {
        Specification<UserTask> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification =
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                    buildRangeSpecification(criteria.getId(), UserTask_.id),
                    buildStringSpecification(criteria.getTenantId(), UserTask_.tenantId),
                    buildStringSpecification(criteria.getTitle(), UserTask_.title),
                    buildStringSpecification(criteria.getDescription(), UserTask_.description),
                    buildStringSpecification(criteria.getStatus(), UserTask_.status),
                    buildStringSpecification(criteria.getPriority(), UserTask_.priority),
                    buildRangeSpecification(criteria.getDueDate(), UserTask_.dueDate),
                    buildSpecification(criteria.getCompleted(), UserTask_.completed),
                    buildStringSpecification(criteria.getAssigneeName(), UserTask_.assigneeName),
                    buildStringSpecification(criteria.getAssigneeContactPhone(), UserTask_.assigneeContactPhone),
                    buildStringSpecification(criteria.getAssigneeContactEmail(), UserTask_.assigneeContactEmail),
                    buildRangeSpecification(criteria.getCreatedAt(), UserTask_.createdAt),
                    buildRangeSpecification(criteria.getUpdatedAt(), UserTask_.updatedAt),
                    buildSpecification(criteria.getUserId(), root -> root.join(UserTask_.user, JoinType.LEFT).get(UserProfile_.id)),
                    buildSpecification(criteria.getEventId(), root -> root.join(UserTask_.event, JoinType.LEFT).get(EventDetails_.id))
                );
        }
        return specification;
    }
}
