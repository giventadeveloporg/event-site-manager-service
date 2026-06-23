package com.eventsitemanager.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.eventsitemanager.domain.TeamMember} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TeamMemberDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
    private Long teamGroupId;

    private Long userProfileId;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String title;

    private String designation;

    @Size(max = 2048)
    private String bio;

    private String email;

    private String profileImageUrl;

    @Size(max = 4096)
    private String expertise;

    private String imageBackground;

    private String imageStyle;

    private String department;

    private LocalDate joinDate;

    private Boolean isActive;

    private String linkedinUrl;

    private String twitterUrl;

    private Integer priorityOrder;

    private String websiteUrl;

    private Integer jerseyNumber;

    @Size(max = 128)
    private String position;

    @Size(max = 128)
    private String lineupSubtitle;

    @Size(max = 128)
    private String instrument;

    @Size(max = 128)
    private String vocalRole;

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

    public Long getTeamGroupId() {
        return teamGroupId;
    }

    public void setTeamGroupId(Long teamGroupId) {
        this.teamGroupId = teamGroupId;
    }

    public Long getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(Long userProfileId) {
        this.userProfileId = userProfileId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getImageBackground() {
        return imageBackground;
    }

    public void setImageBackground(String imageBackground) {
        this.imageBackground = imageBackground;
    }

    public String getImageStyle() {
        return imageStyle;
    }

    public void setImageStyle(String imageStyle) {
        this.imageStyle = imageStyle;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public Integer getPriorityOrder() {
        return priorityOrder;
    }

    public void setPriorityOrder(Integer priorityOrder) {
        this.priorityOrder = priorityOrder;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public Integer getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(Integer jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLineupSubtitle() {
        return lineupSubtitle;
    }

    public void setLineupSubtitle(String lineupSubtitle) {
        this.lineupSubtitle = lineupSubtitle;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getVocalRole() {
        return vocalRole;
    }

    public void setVocalRole(String vocalRole) {
        this.vocalRole = vocalRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeamMemberDTO)) {
            return false;
        }

        TeamMemberDTO teamMemberDTO = (TeamMemberDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, teamMemberDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeamMemberDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", teamGroupId=" + getTeamGroupId() +
            ", userProfileId=" + getUserProfileId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", title='" + getTitle() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", bio='" + getBio() + "'" +
            ", email='" + getEmail() + "'" +
            ", profileImageUrl='" + getProfileImageUrl() + "'" +
            ", expertise='" + getExpertise() + "'" +
            ", imageBackground='" + getImageBackground() + "'" +
            ", imageStyle='" + getImageStyle() + "'" +
            ", department='" + getDepartment() + "'" +
            ", joinDate='" + getJoinDate() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", linkedinUrl='" + getLinkedinUrl() + "'" +
            ", twitterUrl='" + getTwitterUrl() + "'" +
            ", priorityOrder=" + getPriorityOrder() +
            ", websiteUrl='" + getWebsiteUrl() + "'" +
            ", jerseyNumber=" + getJerseyNumber() +
            ", position='" + getPosition() + "'" +
            ", lineupSubtitle='" + getLineupSubtitle() + "'" +
            ", instrument='" + getInstrument() + "'" +
            ", vocalRole='" + getVocalRole() + "'" +
            "}";
    }
}
