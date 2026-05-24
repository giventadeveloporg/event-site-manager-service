package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.TeamMember} entity.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TeamMemberCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private LongFilter teamGroupId;

    private LongFilter userProfileId;

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

    private IntegerFilter jerseyNumber;

    private StringFilter position;

    private StringFilter lineupSubtitle;

    private StringFilter instrument;

    private StringFilter vocalRole;

    private Boolean distinct;

    public TeamMemberCriteria() {}

    public TeamMemberCriteria(TeamMemberCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(StringFilter::copy).orElse(null);
        this.teamGroupId = other.optionalTeamGroupId().map(LongFilter::copy).orElse(null);
        this.userProfileId = other.optionalUserProfileId().map(LongFilter::copy).orElse(null);
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
        this.jerseyNumber = other.optionalJerseyNumber().map(IntegerFilter::copy).orElse(null);
        this.position = other.optionalPosition().map(StringFilter::copy).orElse(null);
        this.lineupSubtitle = other.optionalLineupSubtitle().map(StringFilter::copy).orElse(null);
        this.instrument = other.optionalInstrument().map(StringFilter::copy).orElse(null);
        this.vocalRole = other.optionalVocalRole().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TeamMemberCriteria copy() {
        return new TeamMemberCriteria(this);
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

    public StringFilter getTenantId() {
        return tenantId;
    }

    public Optional<StringFilter> optionalTenantId() {
        return Optional.ofNullable(tenantId);
    }

    public StringFilter tenantId() {
        if (tenantId == null) {
            setTenantId(new StringFilter());
        }
        return tenantId;
    }

    public void setTenantId(StringFilter tenantId) {
        this.tenantId = tenantId;
    }

    public LongFilter getTeamGroupId() {
        return teamGroupId;
    }

    public Optional<LongFilter> optionalTeamGroupId() {
        return Optional.ofNullable(teamGroupId);
    }

    public LongFilter teamGroupId() {
        if (teamGroupId == null) {
            setTeamGroupId(new LongFilter());
        }
        return teamGroupId;
    }

    public void setTeamGroupId(LongFilter teamGroupId) {
        this.teamGroupId = teamGroupId;
    }

    public LongFilter getUserProfileId() {
        return userProfileId;
    }

    public Optional<LongFilter> optionalUserProfileId() {
        return Optional.ofNullable(userProfileId);
    }

    public LongFilter userProfileId() {
        if (userProfileId == null) {
            setUserProfileId(new LongFilter());
        }
        return userProfileId;
    }

    public void setUserProfileId(LongFilter userProfileId) {
        this.userProfileId = userProfileId;
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

    public IntegerFilter getJerseyNumber() {
        return jerseyNumber;
    }

    public Optional<IntegerFilter> optionalJerseyNumber() {
        return Optional.ofNullable(jerseyNumber);
    }

    public IntegerFilter jerseyNumber() {
        if (jerseyNumber == null) {
            setJerseyNumber(new IntegerFilter());
        }
        return jerseyNumber;
    }

    public void setJerseyNumber(IntegerFilter jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    public StringFilter getPosition() {
        return position;
    }

    public Optional<StringFilter> optionalPosition() {
        return Optional.ofNullable(position);
    }

    public StringFilter position() {
        if (position == null) {
            setPosition(new StringFilter());
        }
        return position;
    }

    public void setPosition(StringFilter position) {
        this.position = position;
    }

    public StringFilter getLineupSubtitle() {
        return lineupSubtitle;
    }

    public Optional<StringFilter> optionalLineupSubtitle() {
        return Optional.ofNullable(lineupSubtitle);
    }

    public StringFilter lineupSubtitle() {
        if (lineupSubtitle == null) {
            setLineupSubtitle(new StringFilter());
        }
        return lineupSubtitle;
    }

    public void setLineupSubtitle(StringFilter lineupSubtitle) {
        this.lineupSubtitle = lineupSubtitle;
    }

    public StringFilter getInstrument() {
        return instrument;
    }

    public Optional<StringFilter> optionalInstrument() {
        return Optional.ofNullable(instrument);
    }

    public StringFilter instrument() {
        if (instrument == null) {
            setInstrument(new StringFilter());
        }
        return instrument;
    }

    public void setInstrument(StringFilter instrument) {
        this.instrument = instrument;
    }

    public StringFilter getVocalRole() {
        return vocalRole;
    }

    public Optional<StringFilter> optionalVocalRole() {
        return Optional.ofNullable(vocalRole);
    }

    public StringFilter vocalRole() {
        if (vocalRole == null) {
            setVocalRole(new StringFilter());
        }
        return vocalRole;
    }

    public void setVocalRole(StringFilter vocalRole) {
        this.vocalRole = vocalRole;
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
        final TeamMemberCriteria that = (TeamMemberCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(teamGroupId, that.teamGroupId) &&
            Objects.equals(userProfileId, that.userProfileId) &&
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
            Objects.equals(jerseyNumber, that.jerseyNumber) &&
            Objects.equals(position, that.position) &&
            Objects.equals(lineupSubtitle, that.lineupSubtitle) &&
            Objects.equals(instrument, that.instrument) &&
            Objects.equals(vocalRole, that.vocalRole) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            teamGroupId,
            userProfileId,
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
            jerseyNumber,
            position,
            lineupSubtitle,
            instrument,
            vocalRole,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeamMemberCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalTeamGroupId().map(f -> "teamGroupId=" + f + ", ").orElse("") +
            optionalUserProfileId().map(f -> "userProfileId=" + f + ", ").orElse("") +
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
            optionalJerseyNumber().map(f -> "jerseyNumber=" + f + ", ").orElse("") +
            optionalPosition().map(f -> "position=" + f + ", ").orElse("") +
            optionalLineupSubtitle().map(f -> "lineupSubtitle=" + f + ", ").orElse("") +
            optionalInstrument().map(f -> "instrument=" + f + ", ").orElse("") +
            optionalVocalRole().map(f -> "vocalRole=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
