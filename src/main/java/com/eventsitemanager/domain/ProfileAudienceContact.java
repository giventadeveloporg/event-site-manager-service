package com.eventsitemanager.domain;

import com.eventsitemanager.domain.enumeration.ProfileAudienceContactOptInStatus;
import com.eventsitemanager.domain.enumeration.ProfileAudienceContactSource;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "profile_audience_contact")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfileAudienceContact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profileAudienceContactSeq")
    @SequenceGenerator(name = "profileAudienceContactSeq", sequenceName = "public.profile_audience_contact_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @NotNull
    @Column(name = "public_profile_id", nullable = false)
    private Long publicProfileId;

    @NotNull
    @Size(max = 255)
    @Column(name = "email", length = 255, nullable = false)
    private String email;

    @Size(max = 255)
    @Column(name = "first_name", length = 255)
    private String firstName;

    @Size(max = 255)
    @Column(name = "last_name", length = 255)
    private String lastName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "source", length = 32, nullable = false)
    private ProfileAudienceContactSource source = ProfileAudienceContactSource.ADMIN_MANUAL;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "opt_in_status", length = 32, nullable = false)
    private ProfileAudienceContactOptInStatus optInStatus = ProfileAudienceContactOptInStatus.OPTED_IN;

    @Size(max = 64)
    @Column(name = "unsubscribe_token", length = 64)
    private String unsubscribeToken;

    @Size(max = 500)
    @Column(name = "notes", length = 500)
    private String notes;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getPublicProfileId() {
        return publicProfileId;
    }

    public void setPublicProfileId(Long publicProfileId) {
        this.publicProfileId = publicProfileId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ProfileAudienceContactSource getSource() {
        return source;
    }

    public void setSource(ProfileAudienceContactSource source) {
        this.source = source;
    }

    public ProfileAudienceContactOptInStatus getOptInStatus() {
        return optInStatus;
    }

    public void setOptInStatus(ProfileAudienceContactOptInStatus optInStatus) {
        this.optInStatus = optInStatus;
    }

    public String getUnsubscribeToken() {
        return unsubscribeToken;
    }

    public void setUnsubscribeToken(String unsubscribeToken) {
        this.unsubscribeToken = unsubscribeToken;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProfileAudienceContact)) return false;
        return id != null && id.equals(((ProfileAudienceContact) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
