#!/usr/bin/env python3
"""Generate Event Competitions JHipster stack files."""
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java/com/eventsitemanager"
TEST = ROOT / "src/test/java/com/eventsitemanager"
LIQ = ROOT / "src/main/resources/config/liquibase/changelog"

ENUMS = {
    "CompetitionAudienceMode": ["YOUTH", "ADULT", "MIXED"],
    "CompetitionRegistrationMode": ["PARENT_CHILD", "SELF", "TEAM_CAPTAIN", "MIXED"],
    "CompetitionType": ["INDIVIDUAL", "GROUP"],
    "CompetitionEligibleAudience": ["YOUTH_ONLY", "ADULT_ONLY", "ALL"],
    "CompetitionParticipantType": ["CHILD", "ADULT", "TEAM_MEMBER"],
    "CompetitionRegistrationStatus": ["PENDING_PAYMENT", "CONFIRMED", "CANCELLED", "REFUNDED"],
    "CompetitionResultsDisplayMode": ["FULL_NAME", "INITIALS", "ANONYMOUS"],
}


def write(path: Path, content: str):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(content, encoding="utf-8")
    print(f"Wrote {path.relative_to(ROOT)}")


def gen_enums():
    for name, values in ENUMS.items():
        body = ",\n    ".join(values)
        write(
            JAVA / "domain/enumeration" / f"{name}.java",
            f"""package com.eventsitemanager.domain.enumeration;

/**
 * The {name} enumeration.
 */
public enum {name} {{
    {body},
}}
""",
        )


def gen_liquibase_is_competition():
    write(
        LIQ / "20260522000000_add_is_competition_event_event_details.xml",
        """<?xml version="1.0" encoding="utf-8"?>
<databaseChangeLog
    xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-latest.xsd">

    <changeSet id="20260522000000-1" author="system">
        <preConditions onFail="MARK_RAN">
            <not>
                <columnExists tableName="event_details" columnName="is_competition_event"/>
            </not>
        </preConditions>
        <addColumn tableName="event_details">
            <column name="is_competition_event" type="boolean" defaultValueBoolean="false">
                <constraints nullable="false"/>
            </column>
        </addColumn>
    </changeSet>
</databaseChangeLog>
""",
    )


def gen_liquibase_tables():
    write(
        LIQ / "20260522000001_added_entity_event_competition_tables.xml",
        open(ROOT / "scripts/event_competition_liquibase.xml", encoding="utf-8").read()
        if (ROOT / "scripts/event_competition_liquibase.xml").exists()
        else _liquibase_tables_content(),
    )


def _liquibase_tables_content():
    return r'''<?xml version="1.0" encoding="utf-8"?>
<databaseChangeLog
    xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-latest.xsd">

    <changeSet id="20260522000001-settings" author="system">
        <preConditions onFail="MARK_RAN"><not><tableExists tableName="event_competition_settings"/></not></preConditions>
        <createTable tableName="event_competition_settings">
            <column name="id" type="bigint"><constraints primaryKey="true" nullable="false"/></column>
            <column name="tenant_id" type="varchar(255)"><constraints nullable="false"/></column>
            <column name="event_id" type="bigint"><constraints nullable="false" unique="true" uniqueConstraintName="ux_event_competition_settings__event"/></column>
            <column name="audience_mode" type="varchar(20)" defaultValue="YOUTH"><constraints nullable="false"/></column>
            <column name="registration_mode" type="varchar(32)" defaultValue="PARENT_CHILD"><constraints nullable="false"/></column>
            <column name="registration_deadline" type="${datetimeType}"/>
            <column name="registration_open" type="boolean" defaultValueBoolean="true"><constraints nullable="false"/></column>
            <column name="allow_ticket_sales" type="boolean" defaultValueBoolean="false"><constraints nullable="false"/></column>
            <column name="points_first" type="integer" defaultValueNumeric="5"><constraints nullable="false"/></column>
            <column name="points_second" type="integer" defaultValueNumeric="3"><constraints nullable="false"/></column>
            <column name="points_third" type="integer" defaultValueNumeric="1"><constraints nullable="false"/></column>
            <column name="champion_enabled" type="boolean" defaultValueBoolean="false"><constraints nullable="false"/></column>
            <column name="champion_exclude_group_points" type="boolean" defaultValueBoolean="true"><constraints nullable="false"/></column>
            <column name="champion_max_category" type="integer"/>
            <column name="results_display_mode" type="varchar(32)" defaultValue="FULL_NAME"/>
            <column name="eligibility_text" type="clob"/>
            <column name="created_at" type="${datetimeType}"><constraints nullable="false"/></column>
            <column name="updated_at" type="${datetimeType}"><constraints nullable="false"/></column>
        </createTable>
        <addForeignKeyConstraint baseTableName="event_competition_settings" baseColumnNames="event_id" constraintName="fk_event_competition_settings__event" referencedTableName="event_details" referencedColumnNames="id"/>
    </changeSet>

    <changeSet id="20260522000001-day" author="system">
        <preConditions onFail="MARK_RAN"><not><tableExists tableName="event_competition_day"/></not></preConditions>
        <createTable tableName="event_competition_day">
            <column name="id" type="bigint"><constraints primaryKey="true" nullable="false"/></column>
            <column name="tenant_id" type="varchar(255)"><constraints nullable="false"/></column>
            <column name="event_id" type="bigint"><constraints nullable="false"/></column>
            <column name="day_label" type="varchar(100)"><constraints nullable="false"/></column>
            <column name="event_date" type="date"><constraints nullable="false"/></column>
            <column name="venue_name" type="varchar(255)"><constraints nullable="false"/></column>
            <column name="venue_address" type="varchar(500)"/>
            <column name="sort_order" type="integer" defaultValueNumeric="0"><constraints nullable="false"/></column>
            <column name="notes" type="clob"/>
            <column name="created_at" type="${datetimeType}"><constraints nullable="false"/></column>
            <column name="updated_at" type="${datetimeType}"><constraints nullable="false"/></column>
        </createTable>
        <addForeignKeyConstraint baseTableName="event_competition_day" baseColumnNames="event_id" constraintName="fk_event_competition_day__event" referencedTableName="event_details" referencedColumnNames="id"/>
        <createIndex indexName="idx_event_competition_day__event_id" tableName="event_competition_day"><column name="event_id"/></createIndex>
    </changeSet>

    <changeSet id="20260522000001-competition" author="system">
        <preConditions onFail="MARK_RAN"><not><tableExists tableName="event_competition"/></not></preConditions>
        <createTable tableName="event_competition">
            <column name="id" type="bigint"><constraints primaryKey="true" nullable="false"/></column>
            <column name="tenant_id" type="varchar(255)"><constraints nullable="false"/></column>
            <column name="event_id" type="bigint"><constraints nullable="false"/></column>
            <column name="competition_day_id" type="bigint"/>
            <column name="name" type="varchar(255)"><constraints nullable="false"/></column>
            <column name="description" type="clob"/>
            <column name="competition_type" type="varchar(20)"><constraints nullable="false"/></column>
            <column name="eligible_audience" type="varchar(20)" defaultValue="ALL"><constraints nullable="false"/></column>
            <column name="category_code" type="varchar(20)"/>
            <column name="division_label" type="varchar(100)"/>
            <column name="track" type="varchar(50)"/>
            <column name="fee_amount" type="decimal(10,2)" defaultValueNumeric="0"><constraints nullable="false"/></column>
            <column name="max_participants" type="integer"/>
            <column name="min_group_size" type="integer" defaultValueNumeric="3"/>
            <column name="max_group_size" type="integer" defaultValueNumeric="10"/>
            <column name="time_limit_minutes" type="integer"/>
            <column name="requires_soundtrack" type="boolean" defaultValueBoolean="false"><constraints nullable="false"/></column>
            <column name="judgment_criteria_json" type="clob"/>
            <column name="display_order" type="integer" defaultValueNumeric="0"><constraints nullable="false"/></column>
            <column name="is_active" type="boolean" defaultValueBoolean="true"><constraints nullable="false"/></column>
            <column name="created_at" type="${datetimeType}"><constraints nullable="false"/></column>
            <column name="updated_at" type="${datetimeType}"><constraints nullable="false"/></column>
        </createTable>
        <addForeignKeyConstraint baseTableName="event_competition" baseColumnNames="event_id" constraintName="fk_event_competition__event" referencedTableName="event_details" referencedColumnNames="id"/>
        <addForeignKeyConstraint baseTableName="event_competition" baseColumnNames="competition_day_id" constraintName="fk_event_competition__day" referencedTableName="event_competition_day" referencedColumnNames="id"/>
        <createIndex indexName="idx_event_competition__event_id" tableName="event_competition"><column name="event_id"/></createIndex>
    </changeSet>

    <changeSet id="20260522000001-participant" author="system">
        <preConditions onFail="MARK_RAN"><not><tableExists tableName="event_competition_participant"/></not></preConditions>
        <createTable tableName="event_competition_participant">
            <column name="id" type="bigint"><constraints primaryKey="true" nullable="false"/></column>
            <column name="tenant_id" type="varchar(255)"><constraints nullable="false"/></column>
            <column name="participant_type" type="varchar(20)"><constraints nullable="false"/></column>
            <column name="user_profile_id" type="bigint"><constraints nullable="false"/></column>
            <column name="clerk_user_id" type="varchar(255)"><constraints nullable="false"/></column>
            <column name="guardian_user_profile_id" type="bigint"/>
            <column name="first_name" type="varchar(100)"><constraints nullable="false"/></column>
            <column name="last_name" type="varchar(100)"><constraints nullable="false"/></column>
            <column name="display_name" type="varchar(200)"/>
            <column name="date_of_birth" type="date"/>
            <column name="current_grade" type="integer"/>
            <column name="school_name" type="varchar(255)"/>
            <column name="phone" type="varchar(50)"/>
            <column name="email" type="varchar(255)"/>
            <column name="is_active" type="boolean" defaultValueBoolean="true"><constraints nullable="false"/></column>
            <column name="created_at" type="${datetimeType}"><constraints nullable="false"/></column>
            <column name="updated_at" type="${datetimeType}"><constraints nullable="false"/></column>
        </createTable>
        <addForeignKeyConstraint baseTableName="event_competition_participant" baseColumnNames="user_profile_id" constraintName="fk_event_competition_participant__user_profile" referencedTableName="user_profile" referencedColumnNames="id"/>
        <addForeignKeyConstraint baseTableName="event_competition_participant" baseColumnNames="guardian_user_profile_id" constraintName="fk_event_competition_participant__guardian" referencedTableName="user_profile" referencedColumnNames="id"/>
    </changeSet>

    <changeSet id="20260522000001-registration" author="system">
        <preConditions onFail="MARK_RAN"><not><tableExists tableName="event_competition_registration"/></not></preConditions>
        <createTable tableName="event_competition_registration">
            <column name="id" type="bigint"><constraints primaryKey="true" nullable="false"/></column>
            <column name="tenant_id" type="varchar(255)"><constraints nullable="false"/></column>
            <column name="event_id" type="bigint"><constraints nullable="false"/></column>
            <column name="competition_id" type="bigint"><constraints nullable="false"/></column>
            <column name="participant_profile_id" type="bigint"><constraints nullable="false"/></column>
            <column name="registration_status" type="varchar(32)" defaultValue="PENDING_PAYMENT"><constraints nullable="false"/></column>
            <column name="fee_amount" type="decimal(10,2)"><constraints nullable="false"/></column>
            <column name="effective_category" type="varchar(20)"/>
            <column name="stripe_payment_intent_id" type="varchar(255)"/>
            <column name="group_leader_registration_id" type="bigint"/>
            <column name="registered_by_user_profile_id" type="bigint"><constraints nullable="false"/></column>
            <column name="created_at" type="${datetimeType}"><constraints nullable="false"/></column>
            <column name="updated_at" type="${datetimeType}"><constraints nullable="false"/></column>
        </createTable>
        <addUniqueConstraint tableName="event_competition_registration" columnNames="competition_id, participant_profile_id" constraintName="ux_event_comp_reg__participant_competition"/>
        <addForeignKeyConstraint baseTableName="event_competition_registration" baseColumnNames="competition_id" constraintName="fk_event_comp_reg__competition" referencedTableName="event_competition" referencedColumnNames="id"/>
        <addForeignKeyConstraint baseTableName="event_competition_registration" baseColumnNames="participant_profile_id" constraintName="fk_event_comp_reg__participant" referencedTableName="event_competition_participant" referencedColumnNames="id"/>
        <addForeignKeyConstraint baseTableName="event_competition_registration" baseColumnNames="event_id" constraintName="fk_event_comp_reg__event" referencedTableName="event_details" referencedColumnNames="id"/>
        <addForeignKeyConstraint baseTableName="event_competition_registration" baseColumnNames="registered_by_user_profile_id" constraintName="fk_event_comp_reg__registered_by" referencedTableName="user_profile" referencedColumnNames="id"/>
        <addForeignKeyConstraint baseTableName="event_competition_registration" baseColumnNames="group_leader_registration_id" constraintName="fk_event_comp_reg__group_leader" referencedTableName="event_competition_registration" referencedColumnNames="id"/>
        <createIndex indexName="idx_event_comp_reg__event_id" tableName="event_competition_registration"><column name="event_id"/></createIndex>
    </changeSet>

    <changeSet id="20260522000001-result" author="system">
        <preConditions onFail="MARK_RAN"><not><tableExists tableName="event_competition_result"/></not></preConditions>
        <createTable tableName="event_competition_result">
            <column name="id" type="bigint"><constraints primaryKey="true" nullable="false"/></column>
            <column name="tenant_id" type="varchar(255)"><constraints nullable="false"/></column>
            <column name="event_id" type="bigint"><constraints nullable="false"/></column>
            <column name="competition_id" type="bigint"><constraints nullable="false"/></column>
            <column name="participant_profile_id" type="bigint"/>
            <column name="registration_id" type="bigint"/>
            <column name="display_name" type="varchar(200)"><constraints nullable="false"/></column>
            <column name="placement" type="integer"/>
            <column name="placement_label" type="varchar(50)"/>
            <column name="prize_title" type="varchar(255)"/>
            <column name="prize_details" type="clob"/>
            <column name="points_awarded" type="integer" defaultValueNumeric="0"><constraints nullable="false"/></column>
            <column name="winner_photo_url" type="varchar(1024)"/>
            <column name="winner_media_id" type="bigint"/>
            <column name="notes" type="clob"/>
            <column name="is_published" type="boolean" defaultValueBoolean="false"><constraints nullable="false"/></column>
            <column name="published_at" type="${datetimeType}"/>
            <column name="created_at" type="${datetimeType}"><constraints nullable="false"/></column>
            <column name="updated_at" type="${datetimeType}"><constraints nullable="false"/></column>
        </createTable>
        <addForeignKeyConstraint baseTableName="event_competition_result" baseColumnNames="competition_id" constraintName="fk_event_comp_result__competition" referencedTableName="event_competition" referencedColumnNames="id"/>
        <addForeignKeyConstraint baseTableName="event_competition_result" baseColumnNames="participant_profile_id" constraintName="fk_event_comp_result__participant" referencedTableName="event_competition_participant" referencedColumnNames="id"/>
        <addForeignKeyConstraint baseTableName="event_competition_result" baseColumnNames="registration_id" constraintName="fk_event_comp_result__registration" referencedTableName="event_competition_registration" referencedColumnNames="id"/>
        <addForeignKeyConstraint baseTableName="event_competition_result" baseColumnNames="winner_media_id" constraintName="fk_event_comp_result__winner_media" referencedTableName="event_media" referencedColumnNames="id"/>
        <createIndex indexName="idx_event_comp_result__event_published" tableName="event_competition_result"><column name="event_id"/><column name="is_published"/></createIndex>
    </changeSet>

    <changeSet id="20260522000001-content-block" author="system">
        <preConditions onFail="MARK_RAN"><not><tableExists tableName="event_competition_content_block"/></not></preConditions>
        <createTable tableName="event_competition_content_block">
            <column name="id" type="bigint"><constraints primaryKey="true" nullable="false"/></column>
            <column name="tenant_id" type="varchar(255)"><constraints nullable="false"/></column>
            <column name="event_id" type="bigint"><constraints nullable="false"/></column>
            <column name="block_type" type="varchar(32)"><constraints nullable="false"/></column>
            <column name="title" type="varchar(255)"/>
            <column name="body_markdown" type="clob"><constraints nullable="false"/></column>
            <column name="sort_order" type="integer" defaultValueNumeric="0"><constraints nullable="false"/></column>
            <column name="created_at" type="${datetimeType}"><constraints nullable="false"/></column>
            <column name="updated_at" type="${datetimeType}"><constraints nullable="false"/></column>
        </createTable>
        <addUniqueConstraint tableName="event_competition_content_block" columnNames="event_id, block_type" constraintName="ux_event_comp_content__event_type"/>
        <addForeignKeyConstraint baseTableName="event_competition_content_block" baseColumnNames="event_id" constraintName="fk_event_comp_content__event" referencedTableName="event_details" referencedColumnNames="id"/>
    </changeSet>

    <changeSet id="20260522000001-group-member" author="system">
        <preConditions onFail="MARK_RAN"><not><tableExists tableName="event_competition_group_member"/></not></preConditions>
        <createTable tableName="event_competition_group_member">
            <column name="id" type="bigint"><constraints primaryKey="true" nullable="false"/></column>
            <column name="tenant_id" type="varchar(255)"><constraints nullable="false"/></column>
            <column name="registration_id" type="bigint"><constraints nullable="false"/></column>
            <column name="participant_profile_id" type="bigint"><constraints nullable="false"/></column>
            <column name="created_at" type="${datetimeType}"><constraints nullable="false"/></column>
        </createTable>
        <addForeignKeyConstraint baseTableName="event_competition_group_member" baseColumnNames="registration_id" constraintName="fk_event_comp_group_member__registration" referencedTableName="event_competition_registration" referencedColumnNames="id"/>
        <addForeignKeyConstraint baseTableName="event_competition_group_member" baseColumnNames="participant_profile_id" constraintName="fk_event_comp_group_member__participant" referencedTableName="event_competition_participant" referencedColumnNames="id"/>
    </changeSet>
</databaseChangeLog>
'''


if __name__ == "__main__":
    gen_enums()
    gen_liquibase_is_competition()
    gen_liquibase_tables()
    print("Enums and Liquibase done. Run entity generator separately.")
