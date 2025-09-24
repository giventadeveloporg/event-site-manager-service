package com.nextjstemplate.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ExecutiveCommitteeTeamMember.
 */
@Entity
@Table(name = "executive_committee_team_members")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExecutiveCommitteeTeamMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExecutiveCommitteeTeamMember id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public ExecutiveCommitteeTeamMember firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public ExecutiveCommitteeTeamMember lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return this.title;
    }

    public ExecutiveCommitteeTeamMember title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesignation() {
        return this.designation;
    }

    public ExecutiveCommitteeTeamMember designation(String designation) {
        this.setDesignation(designation);
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getBio() {
        return this.bio;
    }

    public ExecutiveCommitteeTeamMember bio(String bio) {
        this.setBio(bio);
        return this;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return this.email;
    }

    public ExecutiveCommitteeTeamMember email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImageUrl() {
        return this.profileImageUrl;
    }

    public ExecutiveCommitteeTeamMember profileImageUrl(String profileImageUrl) {
        this.setProfileImageUrl(profileImageUrl);
        return this;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getExpertise() {
        return this.expertise;
    }

    public ExecutiveCommitteeTeamMember expertise(String expertise) {
        this.setExpertise(expertise);
        return this;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getImageBackground() {
        return this.imageBackground;
    }

    public ExecutiveCommitteeTeamMember imageBackground(String imageBackground) {
        this.setImageBackground(imageBackground);
        return this;
    }

    public void setImageBackground(String imageBackground) {
        this.imageBackground = imageBackground;
    }

    public String getImageStyle() {
        return this.imageStyle;
    }

    public ExecutiveCommitteeTeamMember imageStyle(String imageStyle) {
        this.setImageStyle(imageStyle);
        return this;
    }

    public void setImageStyle(String imageStyle) {
        this.imageStyle = imageStyle;
    }

    public String getDepartment() {
        return this.department;
    }

    public ExecutiveCommitteeTeamMember department(String department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDate getJoinDate() {
        return this.joinDate;
    }

    public ExecutiveCommitteeTeamMember joinDate(LocalDate joinDate) {
        this.setJoinDate(joinDate);
        return this;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public ExecutiveCommitteeTeamMember isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getLinkedinUrl() {
        return this.linkedinUrl;
    }

    public ExecutiveCommitteeTeamMember linkedinUrl(String linkedinUrl) {
        this.setLinkedinUrl(linkedinUrl);
        return this;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getTwitterUrl() {
        return this.twitterUrl;
    }

    public ExecutiveCommitteeTeamMember twitterUrl(String twitterUrl) {
        this.setTwitterUrl(twitterUrl);
        return this;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public Integer getPriorityOrder() {
        return this.priorityOrder;
    }

    public ExecutiveCommitteeTeamMember priorityOrder(Integer priorityOrder) {
        this.setPriorityOrder(priorityOrder);
        return this;
    }

    public void setPriorityOrder(Integer priorityOrder) {
        this.priorityOrder = priorityOrder;
    }

    public String getWebsiteUrl() {
        return this.websiteUrl;
    }

    public ExecutiveCommitteeTeamMember websiteUrl(String websiteUrl) {
        this.setWebsiteUrl(websiteUrl);
        return this;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExecutiveCommitteeTeamMember)) {
            return false;
        }
        return getId() != null && getId().equals(((ExecutiveCommitteeTeamMember) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExecutiveCommitteeTeamMember{" +
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
