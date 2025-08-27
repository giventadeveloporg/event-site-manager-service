package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.ExecutiveCommitteeTeamMember} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExecutiveCommitteeTeamMemberDTO implements Serializable {

    private Long id;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExecutiveCommitteeTeamMemberDTO)) {
            return false;
        }

        ExecutiveCommitteeTeamMemberDTO executiveCommitteeTeamMemberDTO = (ExecutiveCommitteeTeamMemberDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, executiveCommitteeTeamMemberDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExecutiveCommitteeTeamMemberDTO{" +
            "id=" + getId() +
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
            "}";
    }
}
