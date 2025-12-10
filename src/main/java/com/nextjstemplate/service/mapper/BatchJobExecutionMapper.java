package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.BatchJobExecution;
import com.nextjstemplate.service.dto.BatchJobExecutionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BatchJobExecution} and its DTO {@link BatchJobExecutionDTO}.
 */
@Mapper(componentModel = "spring")
public interface BatchJobExecutionMapper extends EntityMapper<BatchJobExecutionDTO, BatchJobExecution> {}
