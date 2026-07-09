package com.eventsitemanager.service.dto;

import com.eventsitemanager.domain.enumeration.ProfileAudienceContactOptInStatus;
import com.eventsitemanager.domain.enumeration.ProfileAudienceContactSource;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfileAudienceContactDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
    private Long publicProfileId;

    @NotNull
    @Email
    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String firstName;

    @Size(max = 255)
    private String lastName;

    @NotNull
    private ProfileAudienceContactSource source;

    @NotNull
    private ProfileAudienceContactOptInStatus optInStatus;

    @Size(max = 64)
    private String unsubscribeToken;

    @Size(max = 500)
    private String notes;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
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
}
