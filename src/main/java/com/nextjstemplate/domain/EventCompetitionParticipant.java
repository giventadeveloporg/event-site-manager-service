package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.LocalDate;
import com.nextjstemplate.domain.enumeration.*;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.domain.UserProfile;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventCompetitionParticipant.
 */
@Entity
@Table(name = "event_competition_participant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCompetitionParticipant implements Serializable {

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
    @Column(name = "participant_type", length = 20)
    private CompetitionParticipantType participantType;

    @NotNull
    @Size(max = 255)
    @Column(name = "clerk_user_id")
    private String clerkUserId;

    @NotNull
    @Size(max = 100)
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Size(max = 100)
    @Column(name = "last_name")
    private String lastName;

    @Size(max = 200)
    @Column(name = "display_name")
    private String displayName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "current_grade")
    private Integer currentGrade;

    @Size(max = 255)
    @Column(name = "school_name")
    private String schoolName;

    @Size(max = 50)
    @Column(name = "phone")
    private String phone;

    @Size(max = 255)
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "is_active")
    private Boolean isActive;

    @NotNull
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reviewedByAdmin", "userSubscription" }, allowSetters = true)
    private UserProfile userProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reviewedByAdmin", "userSubscription" }, allowSetters = true)
    private UserProfile guardianUserProfile;

    public String getTenantId() {
        return this.tenantId;
    }

    public EventCompetitionParticipant tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public CompetitionParticipantType getParticipantType() {
        return this.participantType;
    }

    public EventCompetitionParticipant participantType(CompetitionParticipantType participantType) {
        this.setParticipantType(participantType);
        return this;
    }

    public void setParticipantType(CompetitionParticipantType participantType) {
        this.participantType = participantType;
    }

    public String getClerkUserId() {
        return this.clerkUserId;
    }

    public EventCompetitionParticipant clerkUserId(String clerkUserId) {
        this.setClerkUserId(clerkUserId);
        return this;
    }

    public void setClerkUserId(String clerkUserId) {
        this.clerkUserId = clerkUserId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public EventCompetitionParticipant firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public EventCompetitionParticipant lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public EventCompetitionParticipant displayName(String displayName) {
        this.setDisplayName(displayName);
        return this;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public EventCompetitionParticipant dateOfBirth(LocalDate dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getCurrentGrade() {
        return this.currentGrade;
    }

    public EventCompetitionParticipant currentGrade(Integer currentGrade) {
        this.setCurrentGrade(currentGrade);
        return this;
    }

    public void setCurrentGrade(Integer currentGrade) {
        this.currentGrade = currentGrade;
    }

    public String getSchoolName() {
        return this.schoolName;
    }

    public EventCompetitionParticipant schoolName(String schoolName) {
        this.setSchoolName(schoolName);
        return this;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getPhone() {
        return this.phone;
    }

    public EventCompetitionParticipant phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public EventCompetitionParticipant email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public EventCompetitionParticipant isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventCompetitionParticipant createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public EventCompetitionParticipant updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public EventCompetitionParticipant userProfile(UserProfile userProfile) {
        this.setUserProfile(userProfile);
        return this;
    }

    public UserProfile getGuardianUserProfile() {
        return this.guardianUserProfile;
    }

    public void setGuardianUserProfile(UserProfile guardianUserProfile) {
        this.guardianUserProfile = guardianUserProfile;
    }

    public EventCompetitionParticipant guardianUserProfile(UserProfile guardianUserProfile) {
        this.setGuardianUserProfile(guardianUserProfile);
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventCompetitionParticipant id(Long id) {
        this.setId(id);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventCompetitionParticipant)) {
            return false;
        }
        return getId() != null && getId().equals(((EventCompetitionParticipant) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
