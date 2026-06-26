package com.eventsitemanager.domain;

import com.eventsitemanager.domain.enumeration.CompetitionGroupMemberRole;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventCompetitionGroupMember.
 */
@Entity
@Table(name = "event_competition_group_member")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCompetitionGroupMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eventCompetitionGroupMemberSeq")
    @SequenceGenerator(
        name = "eventCompetitionGroupMemberSeq",
        sequenceName = "public.event_competition_group_member_id_seq",
        allocationSize = 1
    )
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id")
    private String tenantId;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", length = 20)
    private CompetitionGroupMemberRole memberRole;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @NotNull
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "event", "competition", "participantProfile", "registeredByUserProfile", "groupLeaderRegistration" },
        allowSetters = true
    )
    private EventCompetitionRegistration registration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userProfile", "guardianUserProfile" }, allowSetters = true)
    private EventCompetitionParticipant participantProfile;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventCompetitionGroupMember id(Long id) {
        this.setId(id);
        return this;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public EventCompetitionGroupMember tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public CompetitionGroupMemberRole getMemberRole() {
        return this.memberRole;
    }

    public EventCompetitionGroupMember memberRole(CompetitionGroupMemberRole memberRole) {
        this.setMemberRole(memberRole);
        return this;
    }

    public void setMemberRole(CompetitionGroupMemberRole memberRole) {
        this.memberRole = memberRole;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public EventCompetitionGroupMember sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventCompetitionGroupMember createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public EventCompetitionRegistration getRegistration() {
        return this.registration;
    }

    public void setRegistration(EventCompetitionRegistration registration) {
        this.registration = registration;
    }

    public EventCompetitionGroupMember registration(EventCompetitionRegistration registration) {
        this.setRegistration(registration);
        return this;
    }

    public EventCompetitionParticipant getParticipantProfile() {
        return this.participantProfile;
    }

    public void setParticipantProfile(EventCompetitionParticipant participantProfile) {
        this.participantProfile = participantProfile;
    }

    public EventCompetitionGroupMember participantProfile(EventCompetitionParticipant participantProfile) {
        this.setParticipantProfile(participantProfile);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventCompetitionGroupMember)) {
            return false;
        }
        return getId() != null && getId().equals(((EventCompetitionGroupMember) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
