#!/usr/bin/env python3
"""Generate Event Competition JHipster Java stack."""
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java/com/eventsitemanager"
TEST = ROOT / "src/test/java/com/eventsitemanager"

ENTITIES = [
    "EventCompetitionSettings",
    "EventCompetitionDay",
    "EventCompetition",
    "EventCompetitionParticipant",
    "EventCompetitionRegistration",
    "EventCompetitionResult",
    "EventCompetitionContentBlock",
]

API_PATHS = {
    "EventCompetitionSettings": "event-competition-settings",
    "EventCompetitionDay": "event-competition-days",
    "EventCompetition": "event-competitions",
    "EventCompetitionParticipant": "event-competition-participants",
    "EventCompetitionRegistration": "event-competition-registrations",
    "EventCompetitionResult": "event-competition-results",
    "EventCompetitionContentBlock": "event-competition-content-blocks",
}

ENTITY_NAMES = {
    "EventCompetitionSettings": "eventCompetitionSettings",
    "EventCompetitionDay": "eventCompetitionDay",
    "EventCompetition": "eventCompetition",
    "EventCompetitionParticipant": "eventCompetitionParticipant",
    "EventCompetitionRegistration": "eventCompetitionRegistration",
    "EventCompetitionResult": "eventCompetitionResult",
    "EventCompetitionContentBlock": "eventCompetitionContentBlock",
}


def write(path: Path, content: str):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(content.strip() + "\n", encoding="utf-8")
    print(path.relative_to(ROOT))


def camel_entity(name: str) -> str:
    return name[0].lower() + name[1:]


def gen_repository(name: str):
    extra = ""
    if name == "EventCompetitionSettings":
        extra = """
    Optional<EventCompetitionSettings> findOneByEventId(Long eventId);
"""
    elif name == "EventCompetitionRegistration":
        extra = """
    long countByCompetitionIdAndRegistrationStatusNotIn(Long competitionId, java.util.Collection<com.eventsitemanager.domain.enumeration.CompetitionRegistrationStatus> statuses);

    boolean existsByCompetitionIdAndParticipantProfileIdAndIdNot(Long competitionId, Long participantProfileId, Long excludeId);

    long countByGroupLeaderRegistrationId(Long groupLeaderRegistrationId);
"""
    write(
        JAVA / "repository" / f"{name}Repository.java",
        f"""package com.eventsitemanager.repository;

import com.eventsitemanager.domain.{name};
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface {name}Repository extends JpaRepository<{name}, Long>, JpaSpecificationExecutor<{name}> {{{extra}
}}
""",
    )


def gen_service_interface(name: str):
    write(
        JAVA / "service" / f"{name}Service.java",
        f"""package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.{name}DTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface {name}Service {{
    {name}DTO save({name}DTO dto);
    {name}DTO update({name}DTO dto);
    Optional<{name}DTO> partialUpdate({name}DTO dto);
    Page<{name}DTO> findAll(Pageable pageable);
    Optional<{name}DTO> findOne(Long id);
    void delete(Long id);
}}
""",
    )


def gen_service_impl(name: str):
    ce = camel_entity(name)
    write(
        JAVA / "service/impl" / f"{name}ServiceImpl.java",
        f"""package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.{name};
import com.eventsitemanager.repository.{name}Repository;
import com.eventsitemanager.service.{name}Service;
import com.eventsitemanager.service.dto.{name}DTO;
import com.eventsitemanager.service.mapper.{name}Mapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class {name}ServiceImpl implements {name}Service {{

    private final Logger log = LoggerFactory.getLogger({name}ServiceImpl.class);
    private final {name}Repository repository;
    private final {name}Mapper mapper;

    public {name}ServiceImpl({name}Repository repository, {name}Mapper mapper) {{
        this.repository = repository;
        this.mapper = mapper;
    }}

    @Override
    public {name}DTO save({name}DTO dto) {{
        log.debug("Request to save {name} : {{}}", dto);
        {name} entity = mapper.toEntity(dto);
        if (entity.getId() != null) {{
            log.warn("{name} has ID {{}} set during create. Clearing ID.", entity.getId());
            entity.setId(null);
        }}
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }}

    @Override
    public {name}DTO update({name}DTO dto) {{
        log.debug("Request to update {name} : {{}}", dto);
        {name} entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }}

    @Override
    public Optional<{name}DTO> partialUpdate({name}DTO dto) {{
        log.debug("Request to partially update {name} : {{}}", dto);
        return repository
            .findById(dto.getId())
            .map(existing -> {{
                mapper.partialUpdate(existing, dto);
                return existing;
            }})
            .map(repository::save)
            .map(mapper::toDto);
    }}

    @Override
    @Transactional(readOnly = true)
    public Page<{name}DTO> findAll(Pageable pageable) {{
        return repository.findAll(pageable).map(mapper::toDto);
    }}

    @Override
    @Transactional(readOnly = true)
    public Optional<{name}DTO> findOne(Long id) {{
        return repository.findById(id).map(mapper::toDto);
    }}

    @Override
    public void delete(Long id) {{
        repository.deleteById(id);
    }}
}}
""",
    )


def gen_mapper(name: str, relations: list[tuple[str, str]]):
    mappings = "\n".join(
        f'    @Mapping(target = "{dto_field}", source = "{entity_field}", qualifiedByName = "{qualified}")'
        for dto_field, entity_field, qualified in [(r[0], r[1], r[2]) for r in relations]
    )
    named_methods = ""
    for _, entity_type, qualified in relations:
        if entity_type == "EventDetails":
            named_methods += """
    @Named("eventDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDetailsDTO toDtoEventDetailsId(EventDetails event);

"""
        elif entity_type == "EventCompetitionDay":
            named_methods += """
    @Named("eventCompetitionDayId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventCompetitionDayDTO toDtoEventCompetitionDayId(EventCompetitionDay day);

"""
        elif entity_type == "EventCompetition":
            named_methods += """
    @Named("eventCompetitionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventCompetitionDTO toDtoEventCompetitionId(EventCompetition competition);

"""
        elif entity_type == "EventCompetitionParticipant":
            named_methods += """
    @Named("eventCompetitionParticipantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventCompetitionParticipantDTO toDtoEventCompetitionParticipantId(EventCompetitionParticipant participant);

"""
        elif entity_type == "EventCompetitionRegistration":
            named_methods += """
    @Named("eventCompetitionRegistrationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventCompetitionRegistrationDTO toDtoEventCompetitionRegistrationId(EventCompetitionRegistration registration);

"""
        elif entity_type == "UserProfile":
            named_methods += """
    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

"""
        elif entity_type == "EventMedia":
            named_methods += """
    @Named("eventMediaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventMediaDTO toDtoEventMediaId(EventMedia eventMedia);

"""
    imports = """import com.eventsitemanager.domain.*;
import com.eventsitemanager.service.dto.*;
import org.mapstruct.*;"""
    write(
        JAVA / "service/mapper" / f"{name}Mapper.java",
        f"""package com.eventsitemanager.service.mapper;

{imports}

@Mapper(componentModel = "spring")
public interface {name}Mapper extends EntityMapper<{name}DTO, {name}> {{
{mappings}
    {name}DTO toDto({name} s);
{named_methods}}}
""",
    )


def gen_resource(name: str):
    api = API_PATHS[name]
    en = ENTITY_NAMES[name]
    write(
        JAVA / "web/rest" / f"{name}Resource.java",
        f"""package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.{name}Repository;
import com.eventsitemanager.service.{name}QueryService;
import com.eventsitemanager.service.{name}Service;
import com.eventsitemanager.service.criteria.{name}Criteria;
import com.eventsitemanager.service.dto.{name}DTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/{api}")
public class {name}Resource {{

    private final Logger log = LoggerFactory.getLogger({name}Resource.class);
    private static final String ENTITY_NAME = "{en}";
    @Value("${{jhipster.clientApp.name}}")
    private String applicationName;
    private final {name}Service service;
    private final {name}Repository repository;
    private final {name}QueryService queryService;

    public {name}Resource({name}Service service, {name}Repository repository, {name}QueryService queryService) {{
        this.service = service;
        this.repository = repository;
        this.queryService = queryService;
    }}

    @PostMapping("")
    public ResponseEntity<{name}DTO> create(@Valid @RequestBody {name}DTO dto) throws URISyntaxException {{
        if (dto.getId() != null) throw new BadRequestAlertException("A new entity cannot already have an ID", ENTITY_NAME, "idexists");
        {name}DTO result = service.save(dto);
        return ResponseEntity.created(new URI("/api/{api}/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }}

    @PutMapping("/{{id}}")
    public ResponseEntity<{name}DTO> update(@PathVariable Long id, @Valid @RequestBody {name}DTO dto) throws URISyntaxException {{
        if (dto.getId() == null) throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        if (!Objects.equals(id, dto.getId())) throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        if (!repository.existsById(id)) throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString())).body(service.update(dto));
    }}

    @PatchMapping(value = "/{{id}}", consumes = {{ "application/json", "application/merge-patch+json" }})
    public ResponseEntity<{name}DTO> partialUpdate(@PathVariable Long id, @NotNull @RequestBody {name}DTO dto) throws URISyntaxException {{
        if (dto.getId() == null) throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        if (!Objects.equals(id, dto.getId())) throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        if (!repository.existsById(id)) throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        Optional<{name}DTO> result = service.partialUpdate(dto);
        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString()));
    }}

    @GetMapping("")
    public ResponseEntity<List<{name}DTO>> getAll({name}Criteria criteria, Pageable pageable) {{
        Page<{name}DTO> page = queryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }}

    @GetMapping("/count")
    public ResponseEntity<Long> count({name}Criteria criteria) {{
        return ResponseEntity.ok().body(queryService.countByCriteria(criteria));
    }}

    @GetMapping("/{{id}}")
    public ResponseEntity<{name}DTO> getOne(@PathVariable Long id) {{
        Optional<{name}DTO> dto = service.findOne(id);
        return ResponseUtil.wrapOrNotFound(dto);
    }}

    @DeleteMapping("/{{id}}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {{
        service.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }}
}}
""",
    )


def main():
    for name in ENTITIES:
        gen_repository(name)
        gen_service_interface(name)
        if name not in ("EventCompetitionRegistration", "EventCompetitionResult"):
            gen_service_impl(name)
        gen_resource(name)
    print("Boilerplate services/repos/resources done")


if __name__ == "__main__":
    main()
