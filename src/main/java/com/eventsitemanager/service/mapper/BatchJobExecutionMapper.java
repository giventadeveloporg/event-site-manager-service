package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.BatchJobExecution;
import com.eventsitemanager.service.dto.BatchJobExecutionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BatchJobExecution} and its DTO {@link BatchJobExecutionDTO}.
 */
@Mapper(componentModel = "spring")
public interface BatchJobExecutionMapper extends EntityMapper<BatchJobExecutionDTO, BatchJobExecution> {}
