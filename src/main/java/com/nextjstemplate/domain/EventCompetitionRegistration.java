package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.math.BigDecimal;
import com.nextjstemplate.domain.enumeration.*;
import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.EventCompetition;
import com.nextjstemplate.domain.EventCompetitionParticipant;
import com.nextjstemplate.domain.EventCompetitionRegistration;
import com.nextjstemplate.domain.UserProfile;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventCompetitionRegistration.
 */
@Entity
@Table(name = "event_competition_registration")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCompetitionRegistration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "public.sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id")
    private String tenantId;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "registration_status", length = 32)
    private CompetitionRegistrationStatus registrationStatus;

    @NotNull
    @Column(name = "fee_amount", precision = 10, scale = 2)
    private BigDecimal feeAmount;

    @Size(max = 20)
    @Column(name = "effective_category")
    private String effectiveCategory;

    @Size(max = 255)
    @Column(name = "stripe_payment_intent_id")
    private String stripePaymentIntentId;

    @NotNull
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "createdBy", "eventType" }, allowSetters = true)
    private EventDetails event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "event", "competitionDay" }, allowSetters = true)
    private EventCompetition competition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userProfile", "guardianUserProfile" }, allowSetters = true)
    private EventCompetitionParticipant participantProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "event", "competition", "participantProfile", "registeredByUserProfile", "groupLeaderRegistration" }, allowSetters = true)
    private EventCompetitionRegistration groupLeaderRegistration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reviewedByAdmin", "userSubscription" }, allowSetters = true)
    private UserProfile registeredByUserProfile;

    public String getTenantId() {
        return this.tenantId;
    }

    public EventCompetitionRegistration tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public CompetitionRegistrationStatus getRegistrationStatus() {
        return this.registrationStatus;
    }

    public EventCompetitionRegistration registrationStatus(CompetitionRegistrationStatus registrationStatus) {
        this.setRegistrationStatus(registrationStatus);
        return this;
    }

    public void setRegistrationStatus(CompetitionRegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public BigDecimal getFeeAmount() {
        return this.feeAmount;
    }

    public EventCompetitionRegistration feeAmount(BigDecimal feeAmount) {
        this.setFeeAmount(feeAmount);
        return this;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getEffectiveCategory() {
        return this.effectiveCategory;
    }

    public EventCompetitionRegistration effectiveCategory(String effectiveCategory) {
        this.setEffectiveCategory(effectiveCategory);
        return this;
    }

    public void setEffectiveCategory(String effectiveCategory) {
        this.effectiveCategory = effectiveCategory;
    }

    public String getStripePaymentIntentId() {
        return this.stripePaymentIntentId;
    }

    public EventCompetitionRegistration stripePaymentIntentId(String stripePaymentIntentId) {
        this.setStripePaymentIntentId(stripePaymentIntentId);
        return this;
    }

    public void setStripePaymentIntentId(String stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventCompetitionRegistration createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public EventCompetitionRegistration updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public EventDetails getEvent() {
        return this.event;
    }

    public void setEvent(EventDetails event) {
        this.event = event;
    }

    public EventCompetitionRegistration event(EventDetails event) {
        this.setEvent(event);
        return this;
    }

    public EventCompetition getCompetition() {
        return this.competition;
    }

    public void setCompetition(EventCompetition competition) {
        this.competition = competition;
    }

    public EventCompetitionRegistration competition(EventCompetition competition) {
        this.setCompetition(competition);
        return this;
    }

    public EventCompetitionParticipant getParticipantProfile() {
        return this.participantProfile;
    }

    public void setParticipantProfile(EventCompetitionParticipant participantProfile) {
        this.participantProfile = participantProfile;
    }

    public EventCompetitionRegistration participantProfile(EventCompetitionParticipant participantProfile) {
        this.setParticipantProfile(participantProfile);
        return this;
    }

    public EventCompetitionRegistration getGroupLeaderRegistration() {
        return this.groupLeaderRegistration;
    }

    public void setGroupLeaderRegistration(EventCompetitionRegistration groupLeaderRegistration) {
        this.groupLeaderRegistration = groupLeaderRegistration;
    }

    public EventCompetitionRegistration groupLeaderRegistration(EventCompetitionRegistration groupLeaderRegistration) {
        this.setGroupLeaderRegistration(groupLeaderRegistration);
        return this;
    }

    public UserProfile getRegisteredByUserProfile() {
        return this.registeredByUserProfile;
    }

    public void setRegisteredByUserProfile(UserProfile registeredByUserProfile) {
        this.registeredByUserProfile = registeredByUserProfile;
    }

    public EventCompetitionRegistration registeredByUserProfile(UserProfile registeredByUserProfile) {
        this.setRegisteredByUserProfile(registeredByUserProfile);
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventCompetitionRegistration id(Long id) {
        this.setId(id);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventCompetitionRegistration)) {
            return false;
        }
        return getId() != null && getId().equals(((EventCompetitionRegistration) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
