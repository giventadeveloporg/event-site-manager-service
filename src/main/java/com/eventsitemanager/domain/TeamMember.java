package com.eventsitemanager.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TeamMember.
 */
@Entity
@Table(name = "team_members")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TeamMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "public.sequence_generator", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @NotNull
    @Column(name = "team_group_id", nullable = false)
    private Long teamGroupId;

    @Column(name = "user_profile_id")
    private Long userProfileId;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "designation")
    private String designation;

    @Size(max = 2048)
    @Column(name = "bio", length = 2048)
    private String bio;

    @Column(name = "email")
    private String email;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Size(max = 500)
    @Column(name = "expertise", length = 500)
    private String expertise;

    @Column(name = "image_background")
    private String imageBackground;

    @Column(name = "image_style")
    private String imageStyle;

    @Column(name = "department")
    private String department;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @Column(name = "twitter_url")
    private String twitterUrl;

    @Column(name = "priority_order")
    private Integer priorityOrder;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "jersey_number")
    private Integer jerseyNumber;

    @Size(max = 128)
    @Column(name = "position", length = 128)
    private String position;

    @Size(max = 128)
    @Column(name = "lineup_subtitle", length = 128)
    private String lineupSubtitle;

    @Size(max = 128)
    @Column(name = "instrument", length = 128)
    private String instrument;

    @Size(max = 128)
    @Column(name = "vocal_role", length = 128)
    private String vocalRole;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TeamMember id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public TeamMember tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getTeamGroupId() {
        return this.teamGroupId;
    }

    public TeamMember teamGroupId(Long teamGroupId) {
        this.setTeamGroupId(teamGroupId);
        return this;
    }

    public void setTeamGroupId(Long teamGroupId) {
        this.teamGroupId = teamGroupId;
    }

    public Long getUserProfileId() {
        return this.userProfileId;
    }

    public TeamMember userProfileId(Long userProfileId) {
        this.setUserProfileId(userProfileId);
        return this;
    }

    public void setUserProfileId(Long userProfileId) {
        this.userProfileId = userProfileId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public TeamMember firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public TeamMember lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return this.title;
    }

    public TeamMember title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesignation() {
        return this.designation;
    }

    public TeamMember designation(String designation) {
        this.setDesignation(designation);
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getBio() {
        return this.bio;
    }

    public TeamMember bio(String bio) {
        this.setBio(bio);
        return this;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return this.email;
    }

    public TeamMember email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImageUrl() {
        return this.profileImageUrl;
    }

    public TeamMember profileImageUrl(String profileImageUrl) {
        this.setProfileImageUrl(profileImageUrl);
        return this;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getExpertise() {
        return this.expertise;
    }

    public TeamMember expertise(String expertise) {
        this.setExpertise(expertise);
        return this;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getImageBackground() {
        return this.imageBackground;
    }

    public TeamMember imageBackground(String imageBackground) {
        this.setImageBackground(imageBackground);
        return this;
    }

    public void setImageBackground(String imageBackground) {
        this.imageBackground = imageBackground;
    }

    public String getImageStyle() {
        return this.imageStyle;
    }

    public TeamMember imageStyle(String imageStyle) {
        this.setImageStyle(imageStyle);
        return this;
    }

    public void setImageStyle(String imageStyle) {
        this.imageStyle = imageStyle;
    }

    public String getDepartment() {
        return this.department;
    }

    public TeamMember department(String department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDate getJoinDate() {
        return this.joinDate;
    }

    public TeamMember joinDate(LocalDate joinDate) {
        this.setJoinDate(joinDate);
        return this;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public TeamMember isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getLinkedinUrl() {
        return this.linkedinUrl;
    }

    public TeamMember linkedinUrl(String linkedinUrl) {
        this.setLinkedinUrl(linkedinUrl);
        return this;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getTwitterUrl() {
        return this.twitterUrl;
    }

    public TeamMember twitterUrl(String twitterUrl) {
        this.setTwitterUrl(twitterUrl);
        return this;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public Integer getPriorityOrder() {
        return this.priorityOrder;
    }

    public TeamMember priorityOrder(Integer priorityOrder) {
        this.setPriorityOrder(priorityOrder);
        return this;
    }

    public void setPriorityOrder(Integer priorityOrder) {
        this.priorityOrder = priorityOrder;
    }

    public String getWebsiteUrl() {
        return this.websiteUrl;
    }

    public TeamMember websiteUrl(String websiteUrl) {
        this.setWebsiteUrl(websiteUrl);
        return this;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public Integer getJerseyNumber() {
        return this.jerseyNumber;
    }

    public TeamMember jerseyNumber(Integer jerseyNumber) {
        this.setJerseyNumber(jerseyNumber);
        return this;
    }

    public void setJerseyNumber(Integer jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    public String getPosition() {
        return this.position;
    }

    public TeamMember position(String position) {
        this.setPosition(position);
        return this;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLineupSubtitle() {
        return this.lineupSubtitle;
    }

    public TeamMember lineupSubtitle(String lineupSubtitle) {
        this.setLineupSubtitle(lineupSubtitle);
        return this;
    }

    public void setLineupSubtitle(String lineupSubtitle) {
        this.lineupSubtitle = lineupSubtitle;
    }

    public String getInstrument() {
        return this.instrument;
    }

    public TeamMember instrument(String instrument) {
        this.setInstrument(instrument);
        return this;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getVocalRole() {
        return this.vocalRole;
    }

    public TeamMember vocalRole(String vocalRole) {
        this.setVocalRole(vocalRole);
        return this;
    }

    public void setVocalRole(String vocalRole) {
        this.vocalRole = vocalRole;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeamMember)) {
            return false;
        }
        return getId() != null && getId().equals(((TeamMember) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeamMember{" +
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
