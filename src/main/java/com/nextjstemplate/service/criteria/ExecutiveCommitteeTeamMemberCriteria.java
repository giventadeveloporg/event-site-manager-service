package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.ExecutiveCommitteeTeamMember} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.ExecutiveCommitteeTeamMemberResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /executive-committee-team-members?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExecutiveCommitteeTeamMemberCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter title;

    private StringFilter designation;

    private StringFilter bio;

    private StringFilter email;

    private StringFilter profileImageUrl;

    private StringFilter expertise;

    private StringFilter imageBackground;

    private StringFilter imageStyle;

    private StringFilter department;

    private LocalDateFilter joinDate;

    private BooleanFilter isActive;

    private StringFilter linkedinUrl;

    private StringFilter twitterUrl;

    private IntegerFilter priorityOrder;

    private StringFilter websiteUrl;

    private Boolean distinct;

    public ExecutiveCommitteeTeamMemberCriteria() {}

    public ExecutiveCommitteeTeamMemberCriteria(ExecutiveCommitteeTeamMemberCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.firstName = other.optionalFirstName().map(StringFilter::copy).orElse(null);
        this.lastName = other.optionalLastName().map(StringFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.designation = other.optionalDesignation().map(StringFilter::copy).orElse(null);
        this.bio = other.optionalBio().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.profileImageUrl = other.optionalProfileImageUrl().map(StringFilter::copy).orElse(null);
        this.expertise = other.optionalExpertise().map(StringFilter::copy).orElse(null);
        this.imageBackground = other.optionalImageBackground().map(StringFilter::copy).orElse(null);
        this.imageStyle = other.optionalImageStyle().map(StringFilter::copy).orElse(null);
        this.department = other.optionalDepartment().map(StringFilter::copy).orElse(null);
        this.joinDate = other.optionalJoinDate().map(LocalDateFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.linkedinUrl = other.optionalLinkedinUrl().map(StringFilter::copy).orElse(null);
        this.twitterUrl = other.optionalTwitterUrl().map(StringFilter::copy).orElse(null);
        this.priorityOrder = other.optionalPriorityOrder().map(IntegerFilter::copy).orElse(null);
        this.websiteUrl = other.optionalWebsiteUrl().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ExecutiveCommitteeTeamMemberCriteria copy() {
        return new ExecutiveCommitteeTeamMemberCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public Optional<StringFilter> optionalFirstName() {
        return Optional.ofNullable(firstName);
    }

    public StringFilter firstName() {
        if (firstName == null) {
            setFirstName(new StringFilter());
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public Optional<StringFilter> optionalLastName() {
        return Optional.ofNullable(lastName);
    }

    public StringFilter lastName() {
        if (lastName == null) {
            setLastName(new StringFilter());
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDesignation() {
        return designation;
    }

    public Optional<StringFilter> optionalDesignation() {
        return Optional.ofNullable(designation);
    }

    public StringFilter designation() {
        if (designation == null) {
            setDesignation(new StringFilter());
        }
        return designation;
    }

    public void setDesignation(StringFilter designation) {
        this.designation = designation;
    }

    public StringFilter getBio() {
        return bio;
    }

    public Optional<StringFilter> optionalBio() {
        return Optional.ofNullable(bio);
    }

    public StringFilter bio() {
        if (bio == null) {
            setBio(new StringFilter());
        }
        return bio;
    }

    public void setBio(StringFilter bio) {
        this.bio = bio;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getProfileImageUrl() {
        return profileImageUrl;
    }

    public Optional<StringFilter> optionalProfileImageUrl() {
        return Optional.ofNullable(profileImageUrl);
    }

    public StringFilter profileImageUrl() {
        if (profileImageUrl == null) {
            setProfileImageUrl(new StringFilter());
        }
        return profileImageUrl;
    }

    public void setProfileImageUrl(StringFilter profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public StringFilter getExpertise() {
        return expertise;
    }

    public Optional<StringFilter> optionalExpertise() {
        return Optional.ofNullable(expertise);
    }

    public StringFilter expertise() {
        if (expertise == null) {
            setExpertise(new StringFilter());
        }
        return expertise;
    }

    public void setExpertise(StringFilter expertise) {
        this.expertise = expertise;
    }

    public StringFilter getImageBackground() {
        return imageBackground;
    }

    public Optional<StringFilter> optionalImageBackground() {
        return Optional.ofNullable(imageBackground);
    }

    public StringFilter imageBackground() {
        if (imageBackground == null) {
            setImageBackground(new StringFilter());
        }
        return imageBackground;
    }

    public void setImageBackground(StringFilter imageBackground) {
        this.imageBackground = imageBackground;
    }

    public StringFilter getImageStyle() {
        return imageStyle;
    }

    public Optional<StringFilter> optionalImageStyle() {
        return Optional.ofNullable(imageStyle);
    }

    public StringFilter imageStyle() {
        if (imageStyle == null) {
            setImageStyle(new StringFilter());
        }
        return imageStyle;
    }

    public void setImageStyle(StringFilter imageStyle) {
        this.imageStyle = imageStyle;
    }

    public StringFilter getDepartment() {
        return department;
    }

    public Optional<StringFilter> optionalDepartment() {
        return Optional.ofNullable(department);
    }

    public StringFilter department() {
        if (department == null) {
            setDepartment(new StringFilter());
        }
        return department;
    }

    public void setDepartment(StringFilter department) {
        this.department = department;
    }

    public LocalDateFilter getJoinDate() {
        return joinDate;
    }

    public Optional<LocalDateFilter> optionalJoinDate() {
        return Optional.ofNullable(joinDate);
    }

    public LocalDateFilter joinDate() {
        if (joinDate == null) {
            setJoinDate(new LocalDateFilter());
        }
        return joinDate;
    }

    public void setJoinDate(LocalDateFilter joinDate) {
        this.joinDate = joinDate;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public Optional<BooleanFilter> optionalIsActive() {
        return Optional.ofNullable(isActive);
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            setIsActive(new BooleanFilter());
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public StringFilter getLinkedinUrl() {
        return linkedinUrl;
    }

    public Optional<StringFilter> optionalLinkedinUrl() {
        return Optional.ofNullable(linkedinUrl);
    }

    public StringFilter linkedinUrl() {
        if (linkedinUrl == null) {
            setLinkedinUrl(new StringFilter());
        }
        return linkedinUrl;
    }

    public void setLinkedinUrl(StringFilter linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public StringFilter getTwitterUrl() {
        return twitterUrl;
    }

    public Optional<StringFilter> optionalTwitterUrl() {
        return Optional.ofNullable(twitterUrl);
    }

    public StringFilter twitterUrl() {
        if (twitterUrl == null) {
            setTwitterUrl(new StringFilter());
        }
        return twitterUrl;
    }

    public void setTwitterUrl(StringFilter twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public IntegerFilter getPriorityOrder() {
        return priorityOrder;
    }

    public Optional<IntegerFilter> optionalPriorityOrder() {
        return Optional.ofNullable(priorityOrder);
    }

    public IntegerFilter priorityOrder() {
        if (priorityOrder == null) {
            setPriorityOrder(new IntegerFilter());
        }
        return priorityOrder;
    }

    public void setPriorityOrder(IntegerFilter priorityOrder) {
        this.priorityOrder = priorityOrder;
    }

    public StringFilter getWebsiteUrl() {
        return websiteUrl;
    }

    public Optional<StringFilter> optionalWebsiteUrl() {
        return Optional.ofNullable(websiteUrl);
    }

    public StringFilter websiteUrl() {
        if (websiteUrl == null) {
            setWebsiteUrl(new StringFilter());
        }
        return websiteUrl;
    }

    public void setWebsiteUrl(StringFilter websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ExecutiveCommitteeTeamMemberCriteria that = (ExecutiveCommitteeTeamMemberCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(title, that.title) &&
            Objects.equals(designation, that.designation) &&
            Objects.equals(bio, that.bio) &&
            Objects.equals(email, that.email) &&
            Objects.equals(profileImageUrl, that.profileImageUrl) &&
            Objects.equals(expertise, that.expertise) &&
            Objects.equals(imageBackground, that.imageBackground) &&
            Objects.equals(imageStyle, that.imageStyle) &&
            Objects.equals(department, that.department) &&
            Objects.equals(joinDate, that.joinDate) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(linkedinUrl, that.linkedinUrl) &&
            Objects.equals(twitterUrl, that.twitterUrl) &&
            Objects.equals(priorityOrder, that.priorityOrder) &&
            Objects.equals(websiteUrl, that.websiteUrl) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            firstName,
            lastName,
            title,
            designation,
            bio,
            email,
            profileImageUrl,
            expertise,
            imageBackground,
            imageStyle,
            department,
            joinDate,
            isActive,
            linkedinUrl,
            twitterUrl,
            priorityOrder,
            websiteUrl,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExecutiveCommitteeTeamMemberCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFirstName().map(f -> "firstName=" + f + ", ").orElse("") +
            optionalLastName().map(f -> "lastName=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalDesignation().map(f -> "designation=" + f + ", ").orElse("") +
            optionalBio().map(f -> "bio=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalProfileImageUrl().map(f -> "profileImageUrl=" + f + ", ").orElse("") +
            optionalExpertise().map(f -> "expertise=" + f + ", ").orElse("") +
            optionalImageBackground().map(f -> "imageBackground=" + f + ", ").orElse("") +
            optionalImageStyle().map(f -> "imageStyle=" + f + ", ").orElse("") +
            optionalDepartment().map(f -> "department=" + f + ", ").orElse("") +
            optionalJoinDate().map(f -> "joinDate=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalLinkedinUrl().map(f -> "linkedinUrl=" + f + ", ").orElse("") +
            optionalTwitterUrl().map(f -> "twitterUrl=" + f + ", ").orElse("") +
            optionalPriorityOrder().map(f -> "priorityOrder=" + f + ", ").orElse("") +
            optionalWebsiteUrl().map(f -> "websiteUrl=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
