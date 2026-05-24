"""Event Competition stack generator - appended module."""
from __future__ import annotations

import re
from datetime import date
from pathlib import Path
from typing import Any

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java/com/nextjstemplate"
TEST = ROOT / "src/test/java/com/nextjstemplate"

WRITTEN: list[str] = []


def write(path: Path, content: str) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(content, encoding="utf-8")
    rel = str(path.relative_to(ROOT)).replace("\\", "/")
    WRITTEN.append(rel)
    print(f"Wrote {rel}")


def camel(s: str) -> str:
    return s[0].lower() + s[1:] if s else s


def kebab(name: str) -> str:
    out = []
    for i, ch in enumerate(name):
        if ch.isupper() and i > 0:
            out.append("-")
        out.append(ch.lower())
    return "".join(out)


def java_type(f: dict) -> str:
    t = f["type"]
    if t == "enum":
        return f["enum"]
    if t == "relation":
        return f["relation_type"]
    return {"String": "String", "Boolean": "Boolean", "Integer": "Integer", "ZonedDateTime": "ZonedDateTime", "LocalDate": "LocalDate", "BigDecimal": "BigDecimal"}[t]


def dto_type(f: dict) -> str:
    t = f["type"]
    if t == "enum":
        return f["enum"]
    if t == "relation":
        return f["dto_type"]
    return java_type(f)


def column_ann(f: dict) -> str:
    parts = [f'@Column(name = "{f["column"]}"']
    if f.get("length"):
        parts[0] += f', length = {f["length"]}'
    if f.get("precision"):
        parts[0] += f', precision = {f["precision"]}, scale = {f.get("scale", 2)}'
    if f.get("nullable") is False:
        parts.append("nullable = false")
    parts[0] += ")"
    ann = []
    if f.get("lob"):
        ann.append("@Lob")
    if f["type"] == "enum":
        ann.append("@Enumerated(EnumType.STRING)")
    if f.get("not_null"):
        ann.append("@NotNull")
    if f.get("size"):
        ann.append(f'@Size(max = {f["size"]})')
    ann.append(parts[0])
    return "\n    ".join(ann)



STACK: list[dict[str, Any]] = [
    {
        "class": "EventCompetitionSettings",
        "table": "event_competition_settings",
        "api": "event-competition-settings",
        "entity_name": "eventCompetitionSettings",
        "fields": [
            {"name": "tenantId", "column": "tenant_id", "type": "String", "size": 255, "not_null": True},
            {"name": "audienceMode", "column": "audience_mode", "type": "enum", "enum": "CompetitionAudienceMode", "length": 20, "not_null": True},
            {"name": "registrationMode", "column": "registration_mode", "type": "enum", "enum": "CompetitionRegistrationMode", "length": 32, "not_null": True},
            {"name": "registrationDeadline", "column": "registration_deadline", "type": "ZonedDateTime"},
            {"name": "registrationOpen", "column": "registration_open", "type": "Boolean", "not_null": True},
            {"name": "allowTicketSales", "column": "allow_ticket_sales", "type": "Boolean", "not_null": True},
            {"name": "pointsFirst", "column": "points_first", "type": "Integer", "not_null": True},
            {"name": "pointsSecond", "column": "points_second", "type": "Integer", "not_null": True},
            {"name": "pointsThird", "column": "points_third", "type": "Integer", "not_null": True},
            {"name": "championEnabled", "column": "champion_enabled", "type": "Boolean", "not_null": True},
            {"name": "championExcludeGroupPoints", "column": "champion_exclude_group_points", "type": "Boolean", "not_null": True},
            {"name": "championMaxCategory", "column": "champion_max_category", "type": "Integer"},
            {"name": "resultsDisplayMode", "column": "results_display_mode", "type": "enum", "enum": "CompetitionResultsDisplayMode", "length": 32},
            {"name": "eligibilityText", "column": "eligibility_text", "type": "String", "lob": True},
            {"name": "createdAt", "column": "created_at", "type": "ZonedDateTime", "not_null": True},
            {"name": "updatedAt", "column": "updated_at", "type": "ZonedDateTime", "not_null": True},
        ],
        "relations": [
            {"field": "event", "entity": "EventDetails", "dto": "EventDetailsDTO", "named": "eventDetailsId", "ignore": ["createdBy", "eventType"]},
        ],
        "criteria_joins": [("eventId", "event", "EventDetails_.id")],
        "custom_repo": ["Optional<EventCompetitionSettings> findOneByEventId(Long eventId);"],
    },
    {
        "class": "EventCompetitionDay",
        "table": "event_competition_day",
        "api": "event-competition-days",
        "entity_name": "eventCompetitionDay",
        "fields": [
            {"name": "tenantId", "column": "tenant_id", "type": "String", "size": 255, "not_null": True},
            {"name": "dayLabel", "column": "day_label", "type": "String", "size": 100, "not_null": True},
            {"name": "eventDate", "column": "event_date", "type": "LocalDate", "not_null": True},
            {"name": "venueName", "column": "venue_name", "type": "String", "size": 255, "not_null": True},
            {"name": "venueAddress", "column": "venue_address", "type": "String", "size": 500},
            {"name": "sortOrder", "column": "sort_order", "type": "Integer", "not_null": True},
            {"name": "notes", "column": "notes", "type": "String", "lob": True},
            {"name": "createdAt", "column": "created_at", "type": "ZonedDateTime", "not_null": True},
            {"name": "updatedAt", "column": "updated_at", "type": "ZonedDateTime", "not_null": True},
        ],
        "relations": [{"field": "event", "entity": "EventDetails", "dto": "EventDetailsDTO", "named": "eventDetailsId", "ignore": ["createdBy", "eventType"]}],
        "criteria_joins": [("eventId", "event", "EventDetails_.id")],
    },
    {
        "class": "EventCompetition",
        "table": "event_competition",
        "api": "event-competitions",
        "entity_name": "eventCompetition",
        "fields": [
            {"name": "tenantId", "column": "tenant_id", "type": "String", "size": 255, "not_null": True},
            {"name": "name", "column": "name", "type": "String", "size": 255, "not_null": True},
            {"name": "description", "column": "description", "type": "String", "lob": True},
            {"name": "competitionType", "column": "competition_type", "type": "enum", "enum": "CompetitionType", "length": 20, "not_null": True},
            {"name": "eligibleAudience", "column": "eligible_audience", "type": "enum", "enum": "CompetitionEligibleAudience", "length": 20, "not_null": True},
            {"name": "categoryCode", "column": "category_code", "type": "String", "size": 20},
            {"name": "divisionLabel", "column": "division_label", "type": "String", "size": 100},
            {"name": "track", "column": "track", "type": "String", "size": 50},
            {"name": "feeAmount", "column": "fee_amount", "type": "BigDecimal", "precision": 10, "scale": 2, "not_null": True},
            {"name": "maxParticipants", "column": "max_participants", "type": "Integer"},
            {"name": "minGroupSize", "column": "min_group_size", "type": "Integer"},
            {"name": "maxGroupSize", "column": "max_group_size", "type": "Integer"},
            {"name": "timeLimitMinutes", "column": "time_limit_minutes", "type": "Integer"},
            {"name": "requiresSoundtrack", "column": "requires_soundtrack", "type": "Boolean", "not_null": True},
            {"name": "judgmentCriteriaJson", "column": "judgment_criteria_json", "type": "String", "lob": True},
            {"name": "displayOrder", "column": "display_order", "type": "Integer", "not_null": True},
            {"name": "isActive", "column": "is_active", "type": "Boolean", "not_null": True},
            {"name": "createdAt", "column": "created_at", "type": "ZonedDateTime", "not_null": True},
            {"name": "updatedAt", "column": "updated_at", "type": "ZonedDateTime", "not_null": True},
        ],
        "relations": [
            {"field": "event", "entity": "EventDetails", "dto": "EventDetailsDTO", "named": "eventDetailsId", "ignore": ["createdBy", "eventType"]},
            {"field": "competitionDay", "entity": "EventCompetitionDay", "dto": "EventCompetitionDayDTO", "named": "eventCompetitionDayId", "ignore": ["event"]},
        ],
        "criteria_joins": [
            ("eventId", "event", "EventDetails_.id"),
            ("competitionDayId", "competitionDay", "EventCompetitionDay_.id"),
        ],
    },
]


STACK += [
    {
        "class": "EventCompetitionParticipant",
        "table": "event_competition_participant",
        "api": "event-competition-participants",
        "entity_name": "eventCompetitionParticipant",
        "fields": [
            {"name": "tenantId", "column": "tenant_id", "type": "String", "size": 255, "not_null": True},
            {"name": "participantType", "column": "participant_type", "type": "enum", "enum": "CompetitionParticipantType", "length": 20, "not_null": True},
            {"name": "clerkUserId", "column": "clerk_user_id", "type": "String", "size": 255, "not_null": True},
            {"name": "firstName", "column": "first_name", "type": "String", "size": 100, "not_null": True},
            {"name": "lastName", "column": "last_name", "type": "String", "size": 100, "not_null": True},
            {"name": "displayName", "column": "display_name", "type": "String", "size": 200},
            {"name": "dateOfBirth", "column": "date_of_birth", "type": "LocalDate"},
            {"name": "currentGrade", "column": "current_grade", "type": "Integer"},
            {"name": "schoolName", "column": "school_name", "type": "String", "size": 255},
            {"name": "phone", "column": "phone", "type": "String", "size": 50},
            {"name": "email", "column": "email", "type": "String", "size": 255},
            {"name": "isActive", "column": "is_active", "type": "Boolean", "not_null": True},
            {"name": "createdAt", "column": "created_at", "type": "ZonedDateTime", "not_null": True},
            {"name": "updatedAt", "column": "updated_at", "type": "ZonedDateTime", "not_null": True},
        ],
        "relations": [
            {"field": "userProfile", "entity": "UserProfile", "dto": "UserProfileDTO", "named": "userProfileId", "ignore": ["reviewedByAdmin", "userSubscription"]},
            {"field": "guardianUserProfile", "entity": "UserProfile", "dto": "UserProfileDTO", "named": "guardianUserProfileId", "ignore": ["reviewedByAdmin", "userSubscription"]},
        ],
        "criteria_joins": [
            ("userProfileId", "userProfile", "UserProfile_.id"),
            ("guardianUserProfileId", "guardianUserProfile", "UserProfile_.id"),
        ],
    },
    {
        "class": "EventCompetitionRegistration",
        "table": "event_competition_registration",
        "api": "event-competition-registrations",
        "entity_name": "eventCompetitionRegistration",
        "fields": [
            {"name": "tenantId", "column": "tenant_id", "type": "String", "size": 255, "not_null": True},
            {"name": "registrationStatus", "column": "registration_status", "type": "enum", "enum": "CompetitionRegistrationStatus", "length": 32, "not_null": True},
            {"name": "feeAmount", "column": "fee_amount", "type": "BigDecimal", "precision": 10, "scale": 2, "not_null": True},
            {"name": "effectiveCategory", "column": "effective_category", "type": "String", "size": 20},
            {"name": "stripePaymentIntentId", "column": "stripe_payment_intent_id", "type": "String", "size": 255},
            {"name": "createdAt", "column": "created_at", "type": "ZonedDateTime", "not_null": True},
            {"name": "updatedAt", "column": "updated_at", "type": "ZonedDateTime", "not_null": True},
        ],
        "relations": [
            {"field": "event", "entity": "EventDetails", "dto": "EventDetailsDTO", "named": "eventDetailsId", "ignore": ["createdBy", "eventType"]},
            {"field": "competition", "entity": "EventCompetition", "dto": "EventCompetitionDTO", "named": "eventCompetitionId", "ignore": ["event", "competitionDay"]},
            {"field": "participantProfile", "entity": "EventCompetitionParticipant", "dto": "EventCompetitionParticipantDTO", "named": "eventCompetitionParticipantId", "ignore": ["userProfile", "guardianUserProfile"]},
            {"field": "groupLeaderRegistration", "entity": "EventCompetitionRegistration", "dto": "EventCompetitionRegistrationDTO", "named": "eventCompetitionRegistrationId", "ignore": ["event", "competition", "participantProfile", "registeredByUserProfile", "groupLeaderRegistration"]},
            {"field": "registeredByUserProfile", "entity": "UserProfile", "dto": "UserProfileDTO", "named": "registeredByUserProfileId", "ignore": ["reviewedByAdmin", "userSubscription"]},
        ],
        "criteria_joins": [
            ("eventId", "event", "EventDetails_.id"),
            ("competitionId", "competition", "EventCompetition_.id"),
            ("participantProfileId", "participantProfile", "EventCompetitionParticipant_.id"),
            ("registeredByUserProfileId", "registeredByUserProfile", "UserProfile_.id"),
            ("groupLeaderRegistrationId", "groupLeaderRegistration", "EventCompetitionRegistration_.id"),
        ],
        "custom_repo": [
            "boolean existsByCompetitionIdAndParticipantProfileIdAndIdNot(Long competitionId, Long participantProfileId, Long id);",
            "long countByCompetitionIdAndRegistrationStatusNotIn(Long competitionId, java.util.Collection<com.nextjstemplate.domain.enumeration.CompetitionRegistrationStatus> statuses);",
            "long countByGroupLeaderRegistrationId(Long groupLeaderRegistrationId);",
        ],
        "special_service_impl": "registration",
    },
    {
        "class": "EventCompetitionResult",
        "table": "event_competition_result",
        "api": "event-competition-results",
        "entity_name": "eventCompetitionResult",
        "fields": [
            {"name": "tenantId", "column": "tenant_id", "type": "String", "size": 255, "not_null": True},
            {"name": "displayName", "column": "display_name", "type": "String", "size": 200, "not_null": True},
            {"name": "placement", "column": "placement", "type": "Integer"},
            {"name": "placementLabel", "column": "placement_label", "type": "String", "size": 50},
            {"name": "prizeTitle", "column": "prize_title", "type": "String", "size": 255},
            {"name": "prizeDetails", "column": "prize_details", "type": "String", "lob": True},
            {"name": "pointsAwarded", "column": "points_awarded", "type": "Integer", "not_null": True},
            {"name": "winnerPhotoUrl", "column": "winner_photo_url", "type": "String", "size": 1024},
            {"name": "notes", "column": "notes", "type": "String", "lob": True},
            {"name": "isPublished", "column": "is_published", "type": "Boolean", "not_null": True},
            {"name": "publishedAt", "column": "published_at", "type": "ZonedDateTime"},
            {"name": "createdAt", "column": "created_at", "type": "ZonedDateTime", "not_null": True},
            {"name": "updatedAt", "column": "updated_at", "type": "ZonedDateTime", "not_null": True},
        ],
        "relations": [
            {"field": "event", "entity": "EventDetails", "dto": "EventDetailsDTO", "named": "eventDetailsId", "ignore": ["createdBy", "eventType"]},
            {"field": "competition", "entity": "EventCompetition", "dto": "EventCompetitionDTO", "named": "eventCompetitionId", "ignore": ["event", "competitionDay"]},
            {"field": "participantProfile", "entity": "EventCompetitionParticipant", "dto": "EventCompetitionParticipantDTO", "named": "eventCompetitionParticipantId", "ignore": ["userProfile", "guardianUserProfile"]},
            {"field": "registration", "entity": "EventCompetitionRegistration", "dto": "EventCompetitionRegistrationDTO", "named": "eventCompetitionRegistrationId", "ignore": ["event", "competition", "participantProfile", "registeredByUserProfile", "groupLeaderRegistration"]},
            {"field": "winnerMedia", "entity": "EventMedia", "dto": "EventMediaDTO", "named": "eventMediaId", "ignore": ["event"]},
        ],
        "criteria_joins": [
            ("eventId", "event", "EventDetails_.id"),
            ("competitionId", "competition", "EventCompetition_.id"),
            ("participantProfileId", "participantProfile", "EventCompetitionParticipant_.id"),
            ("registrationId", "registration", "EventCompetitionRegistration_.id"),
            ("winnerMediaId", "winnerMedia", "EventMedia_.id"),
        ],
        "special_service_impl": "result",
    },
    {
        "class": "EventCompetitionContentBlock",
        "table": "event_competition_content_block",
        "api": "event-competition-content-blocks",
        "entity_name": "eventCompetitionContentBlock",
        "fields": [
            {"name": "tenantId", "column": "tenant_id", "type": "String", "size": 255, "not_null": True},
            {"name": "blockType", "column": "block_type", "type": "String", "size": 32, "not_null": True},
            {"name": "title", "column": "title", "type": "String", "size": 255},
            {"name": "bodyMarkdown", "column": "body_markdown", "type": "String", "lob": True, "not_null": True},
            {"name": "sortOrder", "column": "sort_order", "type": "Integer", "not_null": True},
            {"name": "createdAt", "column": "created_at", "type": "ZonedDateTime", "not_null": True},
            {"name": "updatedAt", "column": "updated_at", "type": "ZonedDateTime", "not_null": True},
        ],
        "relations": [{"field": "event", "entity": "EventDetails", "dto": "EventDetailsDTO", "named": "eventDetailsId", "ignore": ["createdBy", "eventType"]}],
        "criteria_joins": [("eventId", "event", "EventDetails_.id")],
    },
]



def gen_entity(cfg: dict) -> None:
    cls = cfg["class"]
    imports = [
        "import com.fasterxml.jackson.annotation.JsonIgnoreProperties;",
        "import jakarta.persistence.*;",
        "import jakarta.validation.constraints.*;",
        "import java.io.Serializable;",
    ]
    if any(f["type"] == "ZonedDateTime" for f in cfg["fields"]):
        imports.append("import java.time.ZonedDateTime;")
    if any(f["type"] == "LocalDate" for f in cfg["fields"]):
        imports.append("import java.time.LocalDate;")
    if any(f["type"] == "BigDecimal" for f in cfg["fields"]):
        imports.append("import java.math.BigDecimal;")
    if any(f["type"] == "enum" for f in cfg["fields"]):
        imports.append("import com.nextjstemplate.domain.enumeration.*;")
    for rel in cfg.get("relations", []):
        imports.append(f"import com.nextjstemplate.domain.{rel['entity']};")
    imports += [
        "import org.hibernate.annotations.Cache;",
        "import org.hibernate.annotations.CacheConcurrencyStrategy;",
        "",
        "/**",
        f" * A {cls}.",
        " */",
        "@Entity",
        f'@Table(name = "{cfg["table"]}")',
        "@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)",
        '@SuppressWarnings("common-java:DuplicatedBlocks")',
        f"public class {cls} implements Serializable {{",
        "",
        "    private static final long serialVersionUID = 1L;",
        "",
        "    @Id",
        '    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")',
        '    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "public.sequence_generator")',
        '    @Column(name = "id")',
        "    private Long id;",
        "",
    ]
    body = []
    for f in cfg["fields"]:
        body.append(f"    {column_ann(f)}")
        body.append(f"    private {java_type(f)} {f['name']};")
        body.append("")
    for rel in cfg.get("relations", []):
        ign = ", ".join(f'"{x}"' for x in rel.get("ignore", []))
        body += [
            "    @ManyToOne(fetch = FetchType.LAZY)",
            f"    @JsonIgnoreProperties(value = {{ {ign} }}, allowSetters = true)",
            f"    private {rel['entity']} {rel['field']};",
            "",
        ]
    getters = []
    for f in cfg["fields"]:
        n, jt = f["name"], java_type(f)
        getters += [
            f"    public {jt} get{n[0].upper()}{n[1:]}() {{",
            f"        return this.{n};",
            "    }",
            "",
            f"    public {cls} {n}({jt} {n}) {{",
            f"        this.set{n[0].upper()}{n[1:]}({n});",
            f"        return this;",
            "    }",
            "",
            f"    public void set{n[0].upper()}{n[1:]}({jt} {n}) {{",
            f"        this.{n} = {n};",
            "    }",
            "",
        ]
    for rel in cfg.get("relations", []):
        n, et = rel["field"], rel["entity"]
        getters += [
            f"    public {et} get{n[0].upper()}{n[1:]}() {{",
            f"        return this.{n};",
            "    }",
            "",
            f"    public void set{n[0].upper()}{n[1:]}({et} {n}) {{",
            f"        this.{n} = {n};",
            "    }",
            "",
            f"    public {cls} {n}({et} {n}) {{",
            f"        this.set{n[0].upper()}{n[1:]}({n});",
            f"        return this;",
            "    }",
            "",
        ]
    tail = [
        "    public Long getId() {",
        "        return this.id;",
        "    }",
        "",
        "    public void setId(Long id) {",
        "        this.id = id;",
        "    }",
        "",
        "    public " + cls + " id(Long id) {",
        "        this.setId(id);",
        "        return this;",
        "    }",
        "",
        "    @Override",
        "    public boolean equals(Object o) {",
        "        if (this == o) {",
        "            return true;",
        "        }",
        f"        if (!(o instanceof {cls})) {{",
        "            return false;",
        "        }",
        f"        return getId() != null && getId().equals((({cls}) o).getId());",
        "    }",
        "",
        "    @Override",
        "    public int hashCode() {",
        "        return getClass().hashCode();",
        "    }",
        "}",
        "",
    ]
    write(JAVA / "domain" / f"{cls}.java", "\n".join(imports + body + getters + tail))


def gen_dto(cfg: dict) -> None:
    cls, dto = cfg["class"], cfg["class"] + "DTO"
    imports = [
        "import jakarta.validation.constraints.*;",
        "import java.io.Serializable;",
        "import java.util.Objects;",
    ]
    if any(f["type"] == "ZonedDateTime" for f in cfg["fields"]):
        imports.append("import java.time.ZonedDateTime;")
    if any(f["type"] == "LocalDate" for f in cfg["fields"]):
        imports.append("import java.time.LocalDate;")
    if any(f["type"] == "BigDecimal" for f in cfg["fields"]):
        imports.append("import java.math.BigDecimal;")
    if any(f["type"] == "enum" for f in cfg["fields"]):
        imports.append("import com.nextjstemplate.domain.enumeration.*;")
    for rel in cfg.get("relations", []):
        imports.append(f"import com.nextjstemplate.service.dto.{rel['dto']};")
    lines = imports + [
        "",
        "/**",
        f" * A DTO for the {{@link com.nextjstemplate.domain.{cls}}} entity.",
        " */",
        '@SuppressWarnings("common-java:DuplicatedBlocks")',
        f"public class {dto} implements Serializable {{",
        "",
        "    private Long id;",
        "",
    ]
    for f in cfg["fields"]:
        if f.get("not_null"):
            lines.append("    @NotNull")
        if f.get("size"):
            lines.append(f'    @Size(max = {f["size"]})')
        lines.append(f"    private {dto_type(f)} {f['name']};")
        lines.append("")
    for rel in cfg.get("relations", []):
        lines.append(f"    private {rel['dto']} {rel['field']};")
        lines.append("")
    for f in [{"name": "id", "type": "Long"}] + cfg["fields"]:
        n = f["name"]
        typ = "Long" if n == "id" else dto_type(f)
        cap = n[0].upper() + n[1:]
        lines += [
            f"    public {typ} get{cap}() {{",
            f"        return {n if n != 'id' else 'id'};",
            "    }",
            "",
            f"    public void set{cap}({typ} {n}) {{",
            f"        this.{n} = {n};",
            "    }",
            "",
        ]
    for rel in cfg.get("relations", []):
        n, dt = rel["field"], rel["dto"]
        cap = n[0].upper() + n[1:]
        lines += [
            f"    public {dt} get{cap}() {{",
            f"        return {n};",
            "    }",
            "",
            f"    public void set{cap}({dt} {n}) {{",
            f"        this.{n} = {n};",
            "    }",
            "",
        ]
    lines += [
        "    @Override",
        "    public boolean equals(Object o) {",
        "        if (this == o) {",
        "            return true;",
        "        }",
        f"        if (!(o instanceof {dto})) {{",
        "            return false;",
        "        }",
        f"        {dto} other = ({dto}) o;",
        "        if (this.id == null) {",
        "            return false;",
        "        }",
        "        return Objects.equals(this.id, other.id);",
        "    }",
        "",
        "    @Override",
        "    public int hashCode() {",
        "        return Objects.hash(this.id);",
        "    }",
        "}",
        "",
    ]
    write(JAVA / "service/dto" / f"{dto}.java", "\n".join(lines))



def gen_repository(cfg: dict) -> None:
    cls = cfg["class"]
    extra = ""
    if cfg.get("custom_repo"):
        extra = "\n".join("    " + m for m in cfg["custom_repo"]) + "\n"
    if "findOneByEventId" in extra:
        imports_opt = "import java.util.Optional;\n"
    elif "Collection" in extra:
        imports_opt = "import java.util.Collection;\n"
    else:
        imports_opt = ""
    content = f"""package com.nextjstemplate.repository;

import com.nextjstemplate.domain.{cls};
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

{imports_opt}/**
 * Spring Data JPA repository for the {cls} entity.
 */
@Repository
public interface {cls}Repository extends JpaRepository<{cls}, Long>, JpaSpecificationExecutor<{cls}> {{
{extra}}}
"""
    write(JAVA / "repository" / f"{cls}Repository.java", content)


def gen_mapper(cfg: dict) -> None:
    cls, dto = cfg["class"], cfg["class"] + "DTO"
    maps = [f'    @Mapping(target = "{r["field"]}", source = "{r["field"]}", qualifiedByName = "{r["named"]}")' for r in cfg.get("relations", [])]
    named = []
    for r in cfg.get("relations", []):
        named += [
            f'    @Named("{r["named"]}")',
            "    @BeanMapping(ignoreByDefault = true)",
            '    @Mapping(target = "id", source = "id")',
            f"    {r['dto']} toDto{r['named'].replace('Id','').replace('eventDetails','EventDetails').replace('userProfile','UserProfile')}(com.nextjstemplate.domain.{r['entity']} entity);",
        ]
    # fix named method names - use simple pattern
    named = []
    for r in cfg.get("relations", []):
        method = "toDto" + r["named"][0].upper() + r["named"][1:]
        named += [
            f'    @Named("{r["named"]}")',
            "    @BeanMapping(ignoreByDefault = true)",
            '    @Mapping(target = "id", source = "id")',
            f"    {r['dto']} {method}(com.nextjstemplate.domain.{r['entity']} entity);",
        ]
    imports = ["import com.nextjstemplate.domain." + cls + ";", "import com.nextjstemplate.service.dto." + dto + ";"]
    for r in cfg.get("relations", []):
        imports.append(f"import com.nextjstemplate.domain.{r['entity']};")
        imports.append(f"import com.nextjstemplate.service.dto.{r['dto']};")
    content = (
        "package com.nextjstemplate.service.mapper;\n\n"
        + "\n".join(imports)
        + "\nimport org.mapstruct.*;\n\n"
        + "/**\n * Mapper for the entity {@link "
        + cls
        + "} and its DTO {@link "
        + dto
        + "}.\n */\n@Mapper(componentModel = \"spring\")\npublic interface "
        + cls
        + "Mapper extends EntityMapper<"
        + dto
        + ", "
        + cls
        + "> {\n"
        + "\n".join(maps)
        + "\n    "
        + dto
        + " toDto("
        + cls
        + " s);\n\n"
        + "\n".join(named)
        + "\n}\n"
    )
    write(JAVA / "service/mapper" / f"{cls}Mapper.java", content)


def gen_service_interface(cfg: dict) -> None:
    cls, dto = cfg["class"], cfg["class"] + "DTO"
    content = f"""package com.nextjstemplate.service;

import {JAVA.as_posix() if False else 'com.nextjstemplate.service.dto.' + dto};
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {{@link com.nextjstemplate.domain.{cls}}}.
 */
public interface {cls}Service {{
    {dto} save({dto} {camel(cls)}DTO);

    {dto} update({dto} {camel(cls)}DTO);

    Optional<{dto}> partialUpdate({dto} {camel(cls)}DTO);

    Page<{dto}> findAll(Pageable pageable);

    Optional<{dto}> findOne(Long id);

    void delete(Long id);
}}
"""
    # fix import path
    content = content.replace(f"{JAVA.as_posix() if False else 'com.nextjstemplate.service.dto.' + dto}", f"com.nextjstemplate.service.dto.{dto}")
    write(JAVA / "service" / f"{cls}Service.java", content)



def gen_service_impl_default(cfg: dict) -> None:
    cls, dto, var = cfg["class"], cfg["class"] + "DTO", camel(cfg["class"])
    content = f"""package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.{cls};
import com.nextjstemplate.repository.{cls}Repository;
import com.nextjstemplate.service.{cls}Service;
import com.nextjstemplate.service.dto.{dto};
import com.nextjstemplate.service.mapper.{cls}Mapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class {cls}ServiceImpl implements {cls}Service {{

    private final Logger log = LoggerFactory.getLogger({cls}ServiceImpl.class);

    private final {cls}Repository {var}Repository;

    private final {cls}Mapper {var}Mapper;

    public {cls}ServiceImpl({cls}Repository {var}Repository, {cls}Mapper {var}Mapper) {{
        this.{var}Repository = {var}Repository;
        this.{var}Mapper = {var}Mapper;
    }}

    @Override
    public {dto} save({dto} {var}DTO) {{
        log.debug("Request to save {cls} : {{}}", {var}DTO);
        {cls} entity = {var}Mapper.toEntity({var}DTO);
        if (entity.getId() != null) {{
            log.warn("{cls} has ID {{}} set during create operation. Clearing ID to force sequence generation.", entity.getId());
            entity.setId(null);
        }}
        entity = {var}Repository.save(entity);
        return {var}Mapper.toDto(entity);
    }}

    @Override
    public {dto} update({dto} {var}DTO) {{
        log.debug("Request to update {cls} : {{}}", {var}DTO);
        {cls} entity = {var}Mapper.toEntity({var}DTO);
        entity = {var}Repository.save(entity);
        return {var}Mapper.toDto(entity);
    }}

    @Override
    public Optional<{dto}> partialUpdate({dto} {var}DTO) {{
        log.debug("Request to partially update {cls} : {{}}", {var}DTO);
        return {var}Repository
            .findById({var}DTO.getId())
            .map(existing -> {{
                {var}Mapper.partialUpdate(existing, {var}DTO);
                return existing;
            }})
            .map({var}Repository::save)
            .map({var}Mapper::toDto);
    }}

    @Override
    @Transactional(readOnly = true)
    public Page<{dto}> findAll(Pageable pageable) {{
        return {var}Repository.findAll(pageable).map({var}Mapper::toDto);
    }}

    @Override
    @Transactional(readOnly = true)
    public Optional<{dto}> findOne(Long id) {{
        return {var}Repository.findById(id).map({var}Mapper::toDto);
    }}

    @Override
    public void delete(Long id) {{
        {var}Repository.deleteById(id);
    }}
}}
"""
    write(JAVA / "service/impl" / f"{cls}ServiceImpl.java", content)


def criteria_filter_type(f: dict) -> str:
    if f["type"] == "enum":
        return f"Filter<{f['enum']}>"
    if f["type"] == "ZonedDateTime":
        return "ZonedDateTimeFilter"
    if f["type"] == "LocalDate":
        return "LocalDateFilter"
    if f["type"] == "Boolean":
        return "BooleanFilter"
    if f["type"] == "Integer":
        return "IntegerFilter"
    if f["type"] == "BigDecimal":
        return "RangeFilter<java.math.BigDecimal>"
    return "StringFilter"


def gen_criteria(cfg: dict) -> None:
    cls = cfg["class"]
    fields = [{"name": "id", "type": "Long"}] + cfg["fields"]
    lines = [
        "package com.nextjstemplate.service.criteria;",
        "",
    ]
    if any(f["type"] == "enum" for f in cfg["fields"]):
        lines.append("import com.nextjstemplate.domain.enumeration.*;")
    lines += [
        "import java.io.Serializable;",
        "import java.util.Objects;",
        "import org.springdoc.core.annotations.ParameterObject;",
        "import tech.jhipster.service.Criteria;",
        "import tech.jhipster.service.filter.*;",
        "",
        "/**",
        f" * Criteria class for the {{@link com.nextjstemplate.domain.{cls}}} entity.",
        " */",
        "@ParameterObject",
        '@SuppressWarnings("common-java:DuplicatedBlocks")',
        f"public class {cls}Criteria implements Serializable, Criteria {{",
        "",
        "    private static final long serialVersionUID = 1L;",
        "",
    ]
    for f in fields:
        ft = "LongFilter" if f["name"] == "id" else criteria_filter_type(f)
        lines.append(f"    private {ft} {f['name']};")
        lines.append("")
    for j in cfg.get("criteria_joins", []):
        lines.append(f"    private LongFilter {j[0]};")
        lines.append("")
    lines.append("    private Boolean distinct;")
    lines.append("")
    lines += [
        f"    public {cls}Criteria() {{}}",
        "",
        f"    public {cls}Criteria({cls}Criteria other) {{",
    ]
    for f in fields:
        n = f["name"]
        lines.append(f"        this.{n} = other.{n} == null ? null : other.{n}.copy();")
    for j in cfg.get("criteria_joins", []):
        lines.append(f"        this.{j[0]} = other.{j[0]} == null ? null : other.{j[0]}.copy();")
    lines += [
        "        this.distinct = other.distinct;",
        "    }",
        "",
        "    @Override",
        f"    public {cls}Criteria copy() {{",
        f"        return new {cls}Criteria(this);",
        "    }",
        "",
    ]
    # getters/setters abbreviated - need id() fluent and get/set for each
    for f in fields + [{"name": j[0], "ftype": "LongFilter"} for j in cfg.get("criteria_joins", [])]:
        if "ftype" in f:
            ft, n = f["ftype"], f["name"]
        else:
            ft, n = ("LongFilter" if f["name"] == "id" else criteria_filter_type(f)), f["name"]
        cap = n[0].upper() + n[1:]
        lines += [
            f"    public {ft} get{cap}() {{",
            f"        return {n};",
            "    }",
            "",
            f"    public {ft} {n}() {{",
            f"        if ({n} == null) {{",
            f"            {n} = new {ft}();",
            "        }",
            f"        return {n};",
            "    }",
            "",
            f"    public void set{cap}({ft} {n}) {{",
            f"        this.{n} = {n};",
            "    }",
            "",
        ]
    lines += [
        "    public Boolean getDistinct() {",
        "        return distinct;",
        "    }",
        "",
        "    public void setDistinct(Boolean distinct) {",
        "        this.distinct = distinct;",
        "    }",
        "",
        "    @Override",
        "    public boolean equals(Object o) {",
        "        if (this == o) {",
        "            return true;",
        "        }",
        f"        if (o == null || getClass() != o.getClass()) {{",
        "            return false;",
        "        }",
        f"        final {cls}Criteria that = ({cls}Criteria) o;",
        "        return Objects.equals(distinct, that.distinct);",
        "    }",
        "",
        "    @Override",
        "    public int hashCode() {",
        "        return Objects.hash(distinct);",
        "    }",
        "}",
        "",
    ]
    write(JAVA / "service/criteria" / f"{cls}Criteria.java", "\n".join(lines))



def gen_query_service(cfg: dict) -> None:
    cls = cfg["class"]
    metamodel = cls + "_"
    lines = [
        "package com.nextjstemplate.service;",
        "",
        "import com.nextjstemplate.domain.*;",
        f"import com.nextjstemplate.domain.{cls};",
        f"import com.nextjstemplate.repository.{cls}Repository;",
        f"import com.nextjstemplate.service.criteria.{cls}Criteria;",
        f"import com.nextjstemplate.service.dto.{cls}DTO;",
        f"import com.nextjstemplate.service.mapper.{cls}Mapper;",
        "import jakarta.persistence.criteria.JoinType;",
        "import java.util.List;",
        "import org.slf4j.Logger;",
        "import org.slf4j.LoggerFactory;",
        "import org.springframework.data.domain.Page;",
        "import org.springframework.data.domain.Pageable;",
        "import org.springframework.data.jpa.domain.Specification;",
        "import org.springframework.stereotype.Service;",
        "import org.springframework.transaction.annotation.Transactional;",
        "import tech.jhipster.service.QueryService;",
        "",
        "@Service",
        "@Transactional(readOnly = true)",
        f"public class {cls}QueryService extends QueryService<{cls}> {{",
        "",
        f"    private final Logger log = LoggerFactory.getLogger({cls}QueryService.class);",
        "",
        f"    private final {cls}Repository {camel(cls)}Repository;",
        "",
        f"    private final {cls}Mapper {camel(cls)}Mapper;",
        "",
        f"    public {cls}QueryService({cls}Repository {camel(cls)}Repository, {cls}Mapper {camel(cls)}Mapper) {{",
        f"        this.{camel(cls)}Repository = {camel(cls)}Repository;",
        f"        this.{camel(cls)}Mapper = {camel(cls)}Mapper;",
        "    }",
        "",
        "    public List<" + cls + "DTO> findByCriteria(" + cls + "Criteria criteria) {",
        "        final Specification<" + cls + "> specification = createSpecification(criteria);",
        "        return " + camel(cls) + "Mapper.toDto(" + camel(cls) + "Repository.findAll(specification));",
        "    }",
        "",
        "    public Page<" + cls + "DTO> findByCriteria(" + cls + "Criteria criteria, Pageable page) {",
        "        final Specification<" + cls + "> specification = createSpecification(criteria);",
        "        return " + camel(cls) + "Repository.findAll(specification, page).map(" + camel(cls) + "Mapper::toDto);",
        "    }",
        "",
        "    public long countByCriteria(" + cls + "Criteria criteria) {",
        "        return " + camel(cls) + "Repository.count(createSpecification(criteria));",
        "    }",
        "",
        "    protected Specification<" + cls + "> createSpecification(" + cls + "Criteria criteria) {",
        "        Specification<" + cls + "> specification = Specification.where(null);",
        "        if (criteria != null) {",
        "            if (criteria.getDistinct() != null) {",
        "                specification = specification.and(distinct(criteria.getDistinct()));",
        "            }",
    ]
    for f in [{"name": "id"}] + cfg["fields"]:
        n = f["name"]
        if n == "id":
            spec = f"buildRangeSpecification(criteria.getId(), {metamodel}id)"
        elif f["type"] == "enum":
            spec = f"buildSpecification(criteria.get{n[0].upper()}{n[1:]}(), {metamodel}{n})"
        elif f["type"] == "ZonedDateTime":
            spec = f"buildRangeSpecification(criteria.get{n[0].upper()}{n[1:]}(), {metamodel}{n})"
        elif f["type"] == "LocalDate":
            spec = f"buildRangeSpecification(criteria.get{n[0].upper()}{n[1:]}(), {metamodel}{n})"
        elif f["type"] in ("Boolean", "Integer"):
            spec = f"buildSpecification(criteria.get{n[0].upper()}{n[1:]}(), {metamodel}{n})"
        elif f["type"] == "BigDecimal":
            spec = f"buildRangeSpecification(criteria.get{n[0].upper()}{n[1:]}(), {metamodel}{n})"
        else:
            spec = f"buildStringSpecification(criteria.get{n[0].upper()}{n[1:]}(), {metamodel}{n})"
        cap = n[0].upper() + n[1:]
        lines += [
            f"            if (criteria.get{cap}() != null) {{",
            f"                specification = specification.and({spec});",
            "            }",
        ]
    for j in cfg.get("criteria_joins", []):
        jid, jfield, jpath = j
        root_field = cls + "_." + jfield
        lines += [
            f"            if (criteria.get{jid[0].upper()}{jid[1:]}() != null) {{",
            "                specification =",
            "                    specification.and(",
            f"                        buildSpecification(criteria.get{jid[0].upper()}{jid[1:]}(), root -> root.join({root_field}, JoinType.LEFT).get({jpath}))",
            "                    );",
            "            }",
        ]
    lines += [
        "        }",
        "        return specification;",
        "    }",
        "}",
        "",
    ]
    write(JAVA / "service" / f"{cls}QueryService.java", "\n".join(lines))


def gen_resource(cfg: dict) -> None:
    cls, dto, en = cfg["class"], cfg["class"] + "DTO", cfg["entity_name"]
    api = cfg["api"]
    var = camel(cls)
    content = f"""package com.nextjstemplate.web.rest;

import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.repository.{cls}Repository;
import com.nextjstemplate.service.{cls}QueryService;
import com.nextjstemplate.service.{cls}Service;
import com.nextjstemplate.service.criteria.{cls}Criteria;
import com.nextjstemplate.service.dto.{dto};
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
public class {cls}Resource {{

    private final Logger log = LoggerFactory.getLogger({cls}Resource.class);

    private static final String ENTITY_NAME = "{en}";

    @Value("${{jhipster.clientApp.name}}")
    private String applicationName;

    private final {cls}Service {var}Service;

    private final {cls}Repository {var}Repository;

    private final {cls}QueryService {var}QueryService;

    public {cls}Resource({cls}Service {var}Service, {cls}Repository {var}Repository, {cls}QueryService {var}QueryService) {{
        this.{var}Service = {var}Service;
        this.{var}Repository = {var}Repository;
        this.{var}QueryService = {var}QueryService;
    }}

    @PostMapping("")
    public ResponseEntity<{dto}> create(@Valid @RequestBody {dto} dto) throws URISyntaxException {{
        if (dto.getId() != null) {{
            throw new BadRequestAlertException("A new {en} cannot already have an ID", ENTITY_NAME, "idexists");
        }}
        {dto} result = {var}Service.save(dto);
        return ResponseEntity
            .created(new URI("/api/{api}/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }}

    @PutMapping("/{{id}}")
    public ResponseEntity<{dto}> update(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody {dto} dto) throws URISyntaxException {{
        if (dto.getId() == null) {{
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }}
        if (!Objects.equals(id, dto.getId())) {{
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }}
        if (!{var}Repository.existsById(id)) {{
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }}
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString())).body({var}Service.update(dto));
    }}

    @PatchMapping(value = "/{{id}}", consumes = {{ "application/json", "application/merge-patch+json" }})
    public ResponseEntity<{dto}> partialUpdate(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody {dto} dto) throws URISyntaxException {{
        if (dto.getId() == null) {{
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }}
        if (!Objects.equals(id, dto.getId())) {{
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }}
        if (!{var}Repository.existsById(id)) {{
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }}
        Optional<{dto}> result = {var}Service.partialUpdate(dto);
        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString()));
    }}

    @GetMapping("")
    public ResponseEntity<List<{dto}>> getAll({cls}Criteria criteria, @org.springdoc.core.annotations.ParameterObject Pageable pageable) {{
        Page<{dto}> page = {var}QueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }}

    @GetMapping("/count")
    public ResponseEntity<Long> count({cls}Criteria criteria) {{
        return ResponseEntity.ok().body({var}QueryService.countByCriteria(criteria));
    }}

    @GetMapping("/{{id}}")
    public ResponseEntity<{dto}> getOne(@PathVariable Long id) {{
        Optional<{dto}> dto = {var}Service.findOne(id);
        return ResponseUtil.wrapOrNotFound(dto);
    }}

    @DeleteMapping("/{{id}}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {{
        {var}Service.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }}
}}
"""
    write(JAVA / "web/rest" / f"{cls}Resource.java", content)



REGISTRATION_SERVICE_IMPL = r'''package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventCompetition;
import com.nextjstemplate.domain.EventCompetitionParticipant;
import com.nextjstemplate.domain.EventCompetitionRegistration;
import com.nextjstemplate.domain.EventCompetitionSettings;
import com.nextjstemplate.domain.enumeration.CompetitionAudienceMode;
import com.nextjstemplate.domain.enumeration.CompetitionEligibleAudience;
import com.nextjstemplate.domain.enumeration.CompetitionParticipantType;
import com.nextjstemplate.domain.enumeration.CompetitionRegistrationStatus;
import com.nextjstemplate.domain.enumeration.CompetitionType;
import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.errors.ConflictException;
import com.nextjstemplate.repository.EventCompetitionRegistrationRepository;
import com.nextjstemplate.repository.EventCompetitionRepository;
import com.nextjstemplate.repository.EventCompetitionSettingsRepository;
import com.nextjstemplate.service.EventCompetitionRegistrationService;
import com.nextjstemplate.service.dto.EventCompetitionRegistrationDTO;
import com.nextjstemplate.service.mapper.EventCompetitionRegistrationMapper;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EventCompetitionRegistrationServiceImpl implements EventCompetitionRegistrationService {

    private static final String ENTITY_NAME = "eventCompetitionRegistration";

    private final Logger log = LoggerFactory.getLogger(EventCompetitionRegistrationServiceImpl.class);

    private final EventCompetitionRegistrationRepository eventCompetitionRegistrationRepository;

    private final EventCompetitionRegistrationMapper eventCompetitionRegistrationMapper;

    private final EventCompetitionSettingsRepository eventCompetitionSettingsRepository;

    private final EventCompetitionRepository eventCompetitionRepository;

    public EventCompetitionRegistrationServiceImpl(
        EventCompetitionRegistrationRepository eventCompetitionRegistrationRepository,
        EventCompetitionRegistrationMapper eventCompetitionRegistrationMapper,
        EventCompetitionSettingsRepository eventCompetitionSettingsRepository,
        EventCompetitionRepository eventCompetitionRepository
    ) {
        this.eventCompetitionRegistrationRepository = eventCompetitionRegistrationRepository;
        this.eventCompetitionRegistrationMapper = eventCompetitionRegistrationMapper;
        this.eventCompetitionSettingsRepository = eventCompetitionSettingsRepository;
        this.eventCompetitionRepository = eventCompetitionRepository;
    }

    @Override
    public EventCompetitionRegistrationDTO save(EventCompetitionRegistrationDTO eventCompetitionRegistrationDTO) {
        log.debug("Request to save EventCompetitionRegistration : {}", eventCompetitionRegistrationDTO);
        EventCompetitionRegistration entity = eventCompetitionRegistrationMapper.toEntity(eventCompetitionRegistrationDTO);
        if (entity.getId() != null) {
            log.warn("EventCompetitionRegistration has ID {} set during create. Clearing ID.", entity.getId());
            entity.setId(null);
        }
        validateRegistration(entity, null);
        entity = eventCompetitionRegistrationRepository.save(entity);
        return eventCompetitionRegistrationMapper.toDto(entity);
    }

    @Override
    public EventCompetitionRegistrationDTO update(EventCompetitionRegistrationDTO eventCompetitionRegistrationDTO) {
        log.debug("Request to update EventCompetitionRegistration : {}", eventCompetitionRegistrationDTO);
        EventCompetitionRegistration entity = eventCompetitionRegistrationMapper.toEntity(eventCompetitionRegistrationDTO);
        validateRegistration(entity, entity.getId());
        entity = eventCompetitionRegistrationRepository.save(entity);
        return eventCompetitionRegistrationMapper.toDto(entity);
    }

    @Override
    public Optional<EventCompetitionRegistrationDTO> partialUpdate(EventCompetitionRegistrationDTO eventCompetitionRegistrationDTO) {
        log.debug("Request to partially update EventCompetitionRegistration : {}", eventCompetitionRegistrationDTO);
        return eventCompetitionRegistrationRepository
            .findById(eventCompetitionRegistrationDTO.getId())
            .map(existing -> {
                eventCompetitionRegistrationMapper.partialUpdate(existing, eventCompetitionRegistrationDTO);
                validateRegistration(existing, existing.getId());
                return existing;
            })
            .map(eventCompetitionRegistrationRepository::save)
            .map(eventCompetitionRegistrationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventCompetitionRegistrationDTO> findAll(Pageable pageable) {
        return eventCompetitionRegistrationRepository.findAll(pageable).map(eventCompetitionRegistrationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventCompetitionRegistrationDTO> findOne(Long id) {
        return eventCompetitionRegistrationRepository.findById(id).map(eventCompetitionRegistrationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        eventCompetitionRegistrationRepository.deleteById(id);
    }

    private void validateRegistration(EventCompetitionRegistration registration, Long excludeId) {
        EventCompetition competition = registration.getCompetition();
        if (competition == null || competition.getId() == null) {
            throw new BadRequestAlertException("Competition is required", ENTITY_NAME, "competitionrequired");
        }
        competition = eventCompetitionRepository
            .findById(competition.getId())
            .orElseThrow(() -> new BadRequestAlertException("Competition not found", ENTITY_NAME, "competitionnotfound"));

        if (competition.getEvent() == null || competition.getEvent().getId() == null) {
            throw new BadRequestAlertException("Event is required", ENTITY_NAME, "eventrequired");
        }

        EventCompetitionSettings settings = eventCompetitionSettingsRepository
            .findOneByEventId(competition.getEvent().getId())
            .orElseThrow(() -> new BadRequestAlertException("Competition settings not found", ENTITY_NAME, "settingsnotfound"));

        ZonedDateTime now = ZonedDateTime.now();
        if ((settings.getRegistrationDeadline() != null && settings.getRegistrationDeadline().isBefore(now)) || Boolean.FALSE.equals(settings.getRegistrationOpen())) {
            throw new BadRequestAlertException("registration closed", ENTITY_NAME, "registrationclosed");
        }

        if (competition.getMaxParticipants() != null) {
            long activeCount = eventCompetitionRegistrationRepository.countByCompetitionIdAndRegistrationStatusNotIn(
                competition.getId(),
                Arrays.asList(CompetitionRegistrationStatus.CANCELLED, CompetitionRegistrationStatus.REFUNDED)
            );
            if (activeCount >= competition.getMaxParticipants()) {
                throw new BadRequestAlertException("Competition is at capacity", ENTITY_NAME, "capacityexceeded");
            }
        }

        EventCompetitionParticipant participant = registration.getParticipantProfile();
        if (participant == null || participant.getId() == null) {
            throw new BadRequestAlertException("Participant is required", ENTITY_NAME, "participantrequired");
        }

        Long participantId = participant.getId();
        if (
            eventCompetitionRegistrationRepository.existsByCompetitionIdAndParticipantProfileIdAndIdNot(
                competition.getId(),
                participantId,
                excludeId == null ? -1L : excludeId
            )
        ) {
            throw new ConflictException("Registration already exists for this participant", ENTITY_NAME, "duplicate");
        }

        CompetitionEligibleAudience eligible = competition.getEligibleAudience();
        CompetitionParticipantType participantType = participant.getParticipantType();
        if (eligible == CompetitionEligibleAudience.YOUTH_ONLY && participantType != CompetitionParticipantType.CHILD) {
            throw new BadRequestAlertException("Only child participants are eligible", ENTITY_NAME, "ineligibleparticipant");
        }
        if (eligible == CompetitionEligibleAudience.ADULT_ONLY && participantType != CompetitionParticipantType.ADULT) {
            throw new BadRequestAlertException("Only adult participants are eligible", ENTITY_NAME, "ineligibleparticipant");
        }

        if (settings.getAudienceMode() == CompetitionAudienceMode.YOUTH && participantType == CompetitionParticipantType.CHILD) {
            if (participant.getGuardianUserProfile() == null || participant.getGuardianUserProfile().getId() == null) {
                throw new BadRequestAlertException("Guardian is required for youth participants", ENTITY_NAME, "guardianrequired");
            }
        }
        if (settings.getAudienceMode() == CompetitionAudienceMode.ADULT && participantType != CompetitionParticipantType.ADULT) {
            throw new BadRequestAlertException("Only adult participants are allowed", ENTITY_NAME, "ineligibleparticipant");
        }

        if (competition.getCompetitionType() == CompetitionType.GROUP && registration.getGroupLeaderRegistration() != null && registration.getGroupLeaderRegistration().getId() != null) {
            long groupSize = eventCompetitionRegistrationRepository.countByGroupLeaderRegistrationId(registration.getGroupLeaderRegistration().getId());
            if (competition.getMaxGroupSize() != null && groupSize > competition.getMaxGroupSize()) {
                throw new BadRequestAlertException("Group size exceeds maximum", ENTITY_NAME, "groupsizeexceeded");
            }
        }
    }
}
'''


RESULT_SERVICE_IMPL = r'''package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventCompetitionResult;
import com.nextjstemplate.domain.EventMedia;
import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.repository.EventCompetitionResultRepository;
import com.nextjstemplate.repository.EventMediaRepository;
import com.nextjstemplate.service.EventCompetitionResultService;
import com.nextjstemplate.service.dto.EventCompetitionResultDTO;
import com.nextjstemplate.service.mapper.EventCompetitionResultMapper;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EventCompetitionResultServiceImpl implements EventCompetitionResultService {

    private static final String ENTITY_NAME = "eventCompetitionResult";

    private final Logger log = LoggerFactory.getLogger(EventCompetitionResultServiceImpl.class);

    private final EventCompetitionResultRepository eventCompetitionResultRepository;

    private final EventCompetitionResultMapper eventCompetitionResultMapper;

    private final EventMediaRepository eventMediaRepository;

    public EventCompetitionResultServiceImpl(
        EventCompetitionResultRepository eventCompetitionResultRepository,
        EventCompetitionResultMapper eventCompetitionResultMapper,
        EventMediaRepository eventMediaRepository
    ) {
        this.eventCompetitionResultRepository = eventCompetitionResultRepository;
        this.eventCompetitionResultMapper = eventCompetitionResultMapper;
        this.eventMediaRepository = eventMediaRepository;
    }

    @Override
    public EventCompetitionResultDTO save(EventCompetitionResultDTO dto) {
        log.debug("Request to save EventCompetitionResult : {}", dto);
        EventCompetitionResult entity = eventCompetitionResultMapper.toEntity(dto);
        if (entity.getId() != null) {
            log.warn("EventCompetitionResult has ID {} set during create. Clearing ID.", entity.getId());
            entity.setId(null);
        }
        validateResult(entity);
        entity = eventCompetitionResultRepository.save(entity);
        return eventCompetitionResultMapper.toDto(entity);
    }

    @Override
    public EventCompetitionResultDTO update(EventCompetitionResultDTO dto) {
        log.debug("Request to update EventCompetitionResult : {}", dto);
        EventCompetitionResult entity = eventCompetitionResultMapper.toEntity(dto);
        validateResult(entity);
        entity = eventCompetitionResultRepository.save(entity);
        return eventCompetitionResultMapper.toDto(entity);
    }

    @Override
    public Optional<EventCompetitionResultDTO> partialUpdate(EventCompetitionResultDTO dto) {
        log.debug("Request to partially update EventCompetitionResult : {}", dto);
        return eventCompetitionResultRepository
            .findById(dto.getId())
            .map(existing -> {
                eventCompetitionResultMapper.partialUpdate(existing, dto);
                validateResult(existing);
                return existing;
            })
            .map(eventCompetitionResultRepository::save)
            .map(eventCompetitionResultMapper.toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventCompetitionResultDTO> findAll(Pageable pageable) {
        return eventCompetitionResultRepository.findAll(pageable).map(eventCompetitionResultMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventCompetitionResultDTO> findOne(Long id) {
        return eventCompetitionResultRepository.findById(id).map(eventCompetitionResultMapper.toDto);
    }

    @Override
    public void delete(Long id) {
        eventCompetitionResultRepository.deleteById(id);
    }

    private void validateResult(EventCompetitionResult result) {
        if (StringUtils.isBlank(result.getDisplayName())) {
            throw new BadRequestAlertException("displayName is required", ENTITY_NAME, "displaynamerequired");
        }
        if (result.getWinnerMedia() != null && result.getWinnerMedia().getId() != null) {
            EventMedia media = eventMediaRepository
                .findById(result.getWinnerMedia().getId())
                .orElseThrow(() -> new BadRequestAlertException("Winner media not found", ENTITY_NAME, "winnermedianotfound"));
            if (result.getEvent() == null || result.getEvent().getId() == null || media.getEvent() == null || media.getEvent().getId() == null) {
                throw new BadRequestAlertException("Event is required for winner media", ENTITY_NAME, "eventrequired");
            }
            if (!result.getEvent().getId().equals(media.getEvent().getId())) {
                throw new BadRequestAlertException("Winner media must belong to the same event", ENTITY_NAME, "winnermediaeventmismatch");
            }
        }
    }
}
'''


def patch_event_details() -> None:
    patches = [
        (
            JAVA / "domain/EventDetails.java",
            (
                '    @Column(name = "is_sports_event")\n    private Boolean isSportsEvent;\n',
                '    @Column(name = "is_sports_event")\n    private Boolean isSportsEvent;\n\n    @Column(name = "is_competition_event")\n    private Boolean isCompetitionEvent;\n',
            ),
            (
                "    public void setIsSportsEvent(Boolean isSportsEvent) {\n        this.isSportsEvent = isSportsEvent;\n    }\n",
                "    public void setIsSportsEvent(Boolean isSportsEvent) {\n        this.isSportsEvent = isSportsEvent;\n    }\n\n    public Boolean getIsCompetitionEvent() {\n        return this.isCompetitionEvent;\n    }\n\n    public EventDetails isCompetitionEvent(Boolean isCompetitionEvent) {\n        this.setIsCompetitionEvent(isCompetitionEvent);\n        return this;\n    }\n\n    public void setIsCompetitionEvent(Boolean isCompetitionEvent) {\n        this.isCompetitionEvent = isCompetitionEvent;\n    }\n",
            ),
            (
                ", isSportsEvent='" + getIsSportsEvent() + "'",
                ", isSportsEvent='" + getIsSportsEvent() + "'" + "\n                + \", isCompetitionEvent='\" + getIsCompetitionEvent() + \"'\"",
            ),
        ),
        (
            JAVA / "service/dto/EventDetailsDTO.java",
            (
                "    private Boolean isSportsEvent;\n",
                "    private Boolean isSportsEvent;\n\n    private Boolean isCompetitionEvent;\n",
            ),
            (
                "    public void setIsSportsEvent(Boolean isSportsEvent) {\n        this.isSportsEvent = isSportsEvent;\n    }\n",
                "    public void setIsSportsEvent(Boolean isSportsEvent) {\n        this.isSportsEvent = isSportsEvent;\n    }\n\n    public Boolean getIsCompetitionEvent() {\n        return isCompetitionEvent;\n    }\n\n    public void setIsCompetitionEvent(Boolean isCompetitionEvent) {\n        this.isCompetitionEvent = isCompetitionEvent;\n    }\n",
            ),
            (
                ", isSportsEvent='" + getIsSportsEvent() + "'",
                ", isSportsEvent='" + getIsSportsEvent() + "'" + "\n            + \", isCompetitionEvent='\" + getIsCompetitionEvent() + \"'\"",
            ),
        ),
        (
            JAVA / "service/criteria/EventDetailsCriteria.java",
            (
                "    private BooleanFilter isSportsEvent;\n",
                "    private BooleanFilter isSportsEvent;\n\n    private BooleanFilter isCompetitionEvent;\n",
            ),
            (
                "        this.isSportsEvent = other.isSportsEvent == null ? null : other.isSportsEvent.copy();\n",
                "        this.isSportsEvent = other.isSportsEvent == null ? null : other.isSportsEvent.copy();\n        this.isCompetitionEvent = other.isCompetitionEvent == null ? null : other.isCompetitionEvent.copy();\n",
            ),
            (
                "    public void setIsSportsEvent(BooleanFilter isSportsEvent) {\n        this.isSportsEvent = isSportsEvent;\n    }\n",
                "    public void setIsSportsEvent(BooleanFilter isSportsEvent) {\n        this.isSportsEvent = isSportsEvent;\n    }\n\n    public BooleanFilter getIsCompetitionEvent() {\n        return isCompetitionEvent;\n    }\n\n    public BooleanFilter isCompetitionEvent() {\n        if (isCompetitionEvent == null) {\n            isCompetitionEvent = new BooleanFilter();\n        }\n        return isCompetitionEvent;\n    }\n\n    public void setIsCompetitionEvent(BooleanFilter isCompetitionEvent) {\n        this.isCompetitionEvent = isCompetitionEvent;\n    }\n",
            ),
            None,
        ),
        (
            JAVA / "service/EventDetailsQueryService.java",
            (
                "            if (criteria.getIsSportsEvent() != null) {\n                specification = specification.and(buildSpecification(criteria.getIsSportsEvent(), EventDetails_.isSportsEvent));\n            }\n",
                "            if (criteria.getIsSportsEvent() != null) {\n                specification = specification.and(buildSpecification(criteria.getIsSportsEvent(), EventDetails_.isSportsEvent));\n            }\n            if (criteria.getIsCompetitionEvent() != null) {\n                specification = specification.and(buildSpecification(criteria.getIsCompetitionEvent(), EventDetails_.isCompetitionEvent));\n            }\n",
            ),
            None,
            None,
        ),
    ]
    for path, *ops in patches:
        text = path.read_text(encoding="utf-8")
        if "isCompetitionEvent" in text:
            continue
        for op in ops:
            if op is None:
                continue
            old, new = op
            if old not in text:
                raise RuntimeError(f"Patch failed in {path}: missing snippet")
            text = text.replace(old, new, 1)
        write(path, text)


def gen_registration_tests() -> None:
    write(
        TEST / "domain/EventCompetitionRegistrationTestSamples.java",
        """package com.nextjstemplate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EventCompetitionRegistrationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EventCompetitionRegistration getEventCompetitionRegistrationSample1() {
        return new EventCompetitionRegistration().id(1L).tenantId("tenantId1");
    }

    public static EventCompetitionRegistration getEventCompetitionRegistrationSample2() {
        return new EventCompetitionRegistration().id(2L).tenantId("tenantId2");
    }

    public static EventCompetitionRegistration getEventCompetitionRegistrationRandomSampleGenerator() {
        return new EventCompetitionRegistration().id(longCount.incrementAndGet()).tenantId(UUID.randomUUID().toString());
    }
}
""",
    )
    # Resource IT - simplified create test
    write(
        TEST / "web/rest/EventCompetitionRegistrationResourceIT.java",
        """package com.nextjstemplate.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.EventCompetitionRegistration;
import com.nextjstemplate.repository.EventCompetitionRegistrationRepository;
import com.nextjstemplate.service.mapper.EventCompetitionRegistrationMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventCompetitionRegistrationResourceIT {

    private static final String ENTITY_API_URL = "/api/event-competition-registrations";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventCompetitionRegistrationRepository eventCompetitionRegistrationRepository;

    @Autowired
    private EventCompetitionRegistrationMapper eventCompetitionRegistrationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventCompetitionRegistrationMockMvc;

    private EventCompetitionRegistration eventCompetitionRegistration;

    public static EventCompetitionRegistration createEntity(EntityManager em) {
        return new EventCompetitionRegistration().tenantId("AAAAAAAAAA");
    }

    @BeforeEach
    void initTest() {
        eventCompetitionRegistration = createEntity(em);
    }

    @Test
    @Transactional
    void checkRepositoryIsPresent() throws Exception {
        assertThat(eventCompetitionRegistrationRepository).isNotNull();
    }
}
""",
    )


def run_stack() -> list[str]:
    WRITTEN.clear()
    for cfg in STACK:
        gen_entity(cfg)
        gen_dto(cfg)
        gen_repository(cfg)
        gen_mapper(cfg)
        gen_service_interface(cfg)
        if cfg.get("special_service_impl") == "registration":
            write(JAVA / "service/impl/EventCompetitionRegistrationServiceImpl.java", REGISTRATION_SERVICE_IMPL)
        elif cfg.get("special_service_impl") == "result":
            write(JAVA / "service/impl/EventCompetitionResultServiceImpl.java", RESULT_SERVICE_IMPL)
        else:
            gen_service_impl_default(cfg)
        gen_criteria(cfg)
        gen_query_service(cfg)
        gen_resource(cfg)
    patch_event_details()
    gen_registration_tests()
    return list(WRITTEN)


if __name__ == "__main__":
    paths = run_stack()
    print("Generated", len(paths), "files")

