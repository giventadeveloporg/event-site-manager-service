package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the
 * {@link com.nextjstemplate.domain.EventFeaturedPerformers} entity. This class
 * is used
 * in {@link com.nextjstemplate.web.rest.EventFeaturedPerformersResource} to
 * receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-featured-performers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific
 * {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventFeaturedPerformersCriteria implements Serializable, Criteria {

  private static final long serialVersionUID = 1L;

  private LongFilter id;

  private StringFilter name;

  private StringFilter stageName;

  private StringFilter role;

  private StringFilter bio;

  private StringFilter nationality;

  private LocalDateFilter dateOfBirth;

  private StringFilter email;

  private StringFilter phone;

  private StringFilter websiteUrl;

  private StringFilter portraitImageUrl;

  private StringFilter performanceImageUrl;

  private StringFilter galleryImageUrls;

  private IntegerFilter performanceDurationMinutes;

  private IntegerFilter performanceOrder;

  private BooleanFilter isHeadliner;

  private StringFilter facebookUrl;

  private StringFilter twitterUrl;

  private StringFilter instagramUrl;

  private StringFilter youtubeUrl;

  private StringFilter linkedinUrl;

  private StringFilter tiktokUrl;

  private BooleanFilter isActive;

  private IntegerFilter priorityRanking;

  private ZonedDateTimeFilter createdAt;

  private ZonedDateTimeFilter updatedAt;

  private LongFilter eventId;

  private Boolean distinct;

  public EventFeaturedPerformersCriteria() {
  }

  public EventFeaturedPerformersCriteria(EventFeaturedPerformersCriteria other) {
    this.id = other.id == null ? null : other.id.copy();
    this.name = other.name == null ? null : other.name.copy();
    this.stageName = other.stageName == null ? null : other.stageName.copy();
    this.role = other.role == null ? null : other.role.copy();
    this.bio = other.bio == null ? null : other.bio.copy();
    this.nationality = other.nationality == null ? null : other.nationality.copy();
    this.dateOfBirth = other.dateOfBirth == null ? null : other.dateOfBirth.copy();
    this.email = other.email == null ? null : other.email.copy();
    this.phone = other.phone == null ? null : other.phone.copy();
    this.websiteUrl = other.websiteUrl == null ? null : other.websiteUrl.copy();
    this.portraitImageUrl = other.portraitImageUrl == null ? null : other.portraitImageUrl.copy();
    this.performanceImageUrl = other.performanceImageUrl == null ? null : other.performanceImageUrl.copy();
    this.galleryImageUrls = other.galleryImageUrls == null ? null : other.galleryImageUrls.copy();
    this.performanceDurationMinutes = other.performanceDurationMinutes == null ? null
        : other.performanceDurationMinutes.copy();
    this.performanceOrder = other.performanceOrder == null ? null : other.performanceOrder.copy();
    this.isHeadliner = other.isHeadliner == null ? null : other.isHeadliner.copy();
    this.facebookUrl = other.facebookUrl == null ? null : other.facebookUrl.copy();
    this.twitterUrl = other.twitterUrl == null ? null : other.twitterUrl.copy();
    this.instagramUrl = other.instagramUrl == null ? null : other.instagramUrl.copy();
    this.youtubeUrl = other.youtubeUrl == null ? null : other.youtubeUrl.copy();
    this.linkedinUrl = other.linkedinUrl == null ? null : other.linkedinUrl.copy();
    this.tiktokUrl = other.tiktokUrl == null ? null : other.tiktokUrl.copy();
    this.isActive = other.isActive == null ? null : other.isActive.copy();
    this.priorityRanking = other.priorityRanking == null ? null : other.priorityRanking.copy();
    this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
    this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
    this.eventId = other.eventId == null ? null : other.eventId.copy();
    this.distinct = other.distinct;
  }

  @Override
  public EventFeaturedPerformersCriteria copy() {
    return new EventFeaturedPerformersCriteria(this);
  }

  public LongFilter getId() {
    return id;
  }

  public LongFilter id() {
    if (id == null) {
      id = new LongFilter();
    }
    return id;
  }

  public void setId(LongFilter id) {
    this.id = id;
  }

  public StringFilter getName() {
    return name;
  }

  public StringFilter name() {
    if (name == null) {
      name = new StringFilter();
    }
    return name;
  }

  public void setName(StringFilter name) {
    this.name = name;
  }

  public StringFilter getStageName() {
    return stageName;
  }

  public StringFilter stageName() {
    if (stageName == null) {
      stageName = new StringFilter();
    }
    return stageName;
  }

  public void setStageName(StringFilter stageName) {
    this.stageName = stageName;
  }

  public StringFilter getRole() {
    return role;
  }

  public StringFilter role() {
    if (role == null) {
      role = new StringFilter();
    }
    return role;
  }

  public void setRole(StringFilter role) {
    this.role = role;
  }

  public StringFilter getBio() {
    return bio;
  }

  public StringFilter bio() {
    if (bio == null) {
      bio = new StringFilter();
    }
    return bio;
  }

  public void setBio(StringFilter bio) {
    this.bio = bio;
  }

  public StringFilter getNationality() {
    return nationality;
  }

  public StringFilter nationality() {
    if (nationality == null) {
      nationality = new StringFilter();
    }
    return nationality;
  }

  public void setNationality(StringFilter nationality) {
    this.nationality = nationality;
  }

  public LocalDateFilter getDateOfBirth() {
    return dateOfBirth;
  }

  public LocalDateFilter dateOfBirth() {
    if (dateOfBirth == null) {
      dateOfBirth = new LocalDateFilter();
    }
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDateFilter dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public StringFilter getEmail() {
    return email;
  }

  public StringFilter email() {
    if (email == null) {
      email = new StringFilter();
    }
    return email;
  }

  public void setEmail(StringFilter email) {
    this.email = email;
  }

  public StringFilter getPhone() {
    return phone;
  }

  public StringFilter phone() {
    if (phone == null) {
      phone = new StringFilter();
    }
    return phone;
  }

  public void setPhone(StringFilter phone) {
    this.phone = phone;
  }

  public StringFilter getWebsiteUrl() {
    return websiteUrl;
  }

  public StringFilter websiteUrl() {
    if (websiteUrl == null) {
      websiteUrl = new StringFilter();
    }
    return websiteUrl;
  }

  public void setWebsiteUrl(StringFilter websiteUrl) {
    this.websiteUrl = websiteUrl;
  }

  public StringFilter getPortraitImageUrl() {
    return portraitImageUrl;
  }

  public StringFilter portraitImageUrl() {
    if (portraitImageUrl == null) {
      portraitImageUrl = new StringFilter();
    }
    return portraitImageUrl;
  }

  public void setPortraitImageUrl(StringFilter portraitImageUrl) {
    this.portraitImageUrl = portraitImageUrl;
  }

  public StringFilter getPerformanceImageUrl() {
    return performanceImageUrl;
  }

  public StringFilter performanceImageUrl() {
    if (performanceImageUrl == null) {
      performanceImageUrl = new StringFilter();
    }
    return performanceImageUrl;
  }

  public void setPerformanceImageUrl(StringFilter performanceImageUrl) {
    this.performanceImageUrl = performanceImageUrl;
  }

  public StringFilter getGalleryImageUrls() {
    return galleryImageUrls;
  }

  public StringFilter galleryImageUrls() {
    if (galleryImageUrls == null) {
      galleryImageUrls = new StringFilter();
    }
    return galleryImageUrls;
  }

  public void setGalleryImageUrls(StringFilter galleryImageUrls) {
    this.galleryImageUrls = galleryImageUrls;
  }

  public IntegerFilter getPerformanceDurationMinutes() {
    return performanceDurationMinutes;
  }

  public IntegerFilter performanceDurationMinutes() {
    if (performanceDurationMinutes == null) {
      performanceDurationMinutes = new IntegerFilter();
    }
    return performanceDurationMinutes;
  }

  public void setPerformanceDurationMinutes(IntegerFilter performanceDurationMinutes) {
    this.performanceDurationMinutes = performanceDurationMinutes;
  }

  public IntegerFilter getPerformanceOrder() {
    return performanceOrder;
  }

  public IntegerFilter performanceOrder() {
    if (performanceOrder == null) {
      performanceOrder = new IntegerFilter();
    }
    return performanceOrder;
  }

  public void setPerformanceOrder(IntegerFilter performanceOrder) {
    this.performanceOrder = performanceOrder;
  }

  public BooleanFilter getIsHeadliner() {
    return isHeadliner;
  }

  public BooleanFilter isHeadliner() {
    if (isHeadliner == null) {
      isHeadliner = new BooleanFilter();
    }
    return isHeadliner;
  }

  public void setIsHeadliner(BooleanFilter isHeadliner) {
    this.isHeadliner = isHeadliner;
  }

  public StringFilter getFacebookUrl() {
    return facebookUrl;
  }

  public StringFilter facebookUrl() {
    if (facebookUrl == null) {
      facebookUrl = new StringFilter();
    }
    return facebookUrl;
  }

  public void setFacebookUrl(StringFilter facebookUrl) {
    this.facebookUrl = facebookUrl;
  }

  public StringFilter getTwitterUrl() {
    return twitterUrl;
  }

  public StringFilter twitterUrl() {
    if (twitterUrl == null) {
      twitterUrl = new StringFilter();
    }
    return twitterUrl;
  }

  public void setTwitterUrl(StringFilter twitterUrl) {
    this.twitterUrl = twitterUrl;
  }

  public StringFilter getInstagramUrl() {
    return instagramUrl;
  }

  public StringFilter instagramUrl() {
    if (instagramUrl == null) {
      instagramUrl = new StringFilter();
    }
    return instagramUrl;
  }

  public void setInstagramUrl(StringFilter instagramUrl) {
    this.instagramUrl = instagramUrl;
  }

  public StringFilter getYoutubeUrl() {
    return youtubeUrl;
  }

  public StringFilter youtubeUrl() {
    if (youtubeUrl == null) {
      youtubeUrl = new StringFilter();
    }
    return youtubeUrl;
  }

  public void setYoutubeUrl(StringFilter youtubeUrl) {
    this.youtubeUrl = youtubeUrl;
  }

  public StringFilter getLinkedinUrl() {
    return linkedinUrl;
  }

  public StringFilter linkedinUrl() {
    if (linkedinUrl == null) {
      linkedinUrl = new StringFilter();
    }
    return linkedinUrl;
  }

  public void setLinkedinUrl(StringFilter linkedinUrl) {
    this.linkedinUrl = linkedinUrl;
  }

  public StringFilter getTiktokUrl() {
    return tiktokUrl;
  }

  public StringFilter tiktokUrl() {
    if (tiktokUrl == null) {
      tiktokUrl = new StringFilter();
    }
    return tiktokUrl;
  }

  public void setTiktokUrl(StringFilter tiktokUrl) {
    this.tiktokUrl = tiktokUrl;
  }

  public BooleanFilter getIsActive() {
    return isActive;
  }

  public BooleanFilter isActive() {
    if (isActive == null) {
      isActive = new BooleanFilter();
    }
    return isActive;
  }

  public void setIsActive(BooleanFilter isActive) {
    this.isActive = isActive;
  }

  public IntegerFilter getPriorityRanking() {
    return priorityRanking;
  }

  public IntegerFilter priorityRanking() {
    if (priorityRanking == null) {
      priorityRanking = new IntegerFilter();
    }
    return priorityRanking;
  }

  public void setPriorityRanking(IntegerFilter priorityRanking) {
    this.priorityRanking = priorityRanking;
  }

  public ZonedDateTimeFilter getCreatedAt() {
    return createdAt;
  }

  public ZonedDateTimeFilter createdAt() {
    if (createdAt == null) {
      createdAt = new ZonedDateTimeFilter();
    }
    return createdAt;
  }

  public void setCreatedAt(ZonedDateTimeFilter createdAt) {
    this.createdAt = createdAt;
  }

  public ZonedDateTimeFilter getUpdatedAt() {
    return updatedAt;
  }

  public ZonedDateTimeFilter updatedAt() {
    if (updatedAt == null) {
      updatedAt = new ZonedDateTimeFilter();
    }
    return updatedAt;
  }

  public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
    this.updatedAt = updatedAt;
  }

  public LongFilter getEventId() {
    return eventId;
  }

  public LongFilter eventId() {
    if (eventId == null) {
      eventId = new LongFilter();
    }
    return eventId;
  }

  public void setEventId(LongFilter eventId) {
    this.eventId = eventId;
  }

  public Boolean getDistinct() {
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
    final EventFeaturedPerformersCriteria that = (EventFeaturedPerformersCriteria) o;
    return (Objects.equals(id, that.id) &&
        Objects.equals(name, that.name) &&
        Objects.equals(stageName, that.stageName) &&
        Objects.equals(role, that.role) &&
        Objects.equals(bio, that.bio) &&
        Objects.equals(nationality, that.nationality) &&
        Objects.equals(dateOfBirth, that.dateOfBirth) &&
        Objects.equals(email, that.email) &&
        Objects.equals(phone, that.phone) &&
        Objects.equals(websiteUrl, that.websiteUrl) &&
        Objects.equals(portraitImageUrl, that.portraitImageUrl) &&
        Objects.equals(performanceImageUrl, that.performanceImageUrl) &&
        Objects.equals(galleryImageUrls, that.galleryImageUrls) &&
        Objects.equals(performanceDurationMinutes, that.performanceDurationMinutes) &&
        Objects.equals(performanceOrder, that.performanceOrder) &&
        Objects.equals(isHeadliner, that.isHeadliner) &&
        Objects.equals(facebookUrl, that.facebookUrl) &&
        Objects.equals(twitterUrl, that.twitterUrl) &&
        Objects.equals(instagramUrl, that.instagramUrl) &&
        Objects.equals(youtubeUrl, that.youtubeUrl) &&
        Objects.equals(linkedinUrl, that.linkedinUrl) &&
        Objects.equals(tiktokUrl, that.tiktokUrl) &&
        Objects.equals(isActive, that.isActive) &&
        Objects.equals(priorityRanking, that.priorityRanking) &&
        Objects.equals(createdAt, that.createdAt) &&
        Objects.equals(updatedAt, that.updatedAt) &&
        Objects.equals(eventId, that.eventId) &&
        Objects.equals(distinct, that.distinct));
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        id,
        name,
        stageName,
        role,
        bio,
        nationality,
        dateOfBirth,
        email,
        phone,
        websiteUrl,
        portraitImageUrl,
        performanceImageUrl,
        galleryImageUrls,
        performanceDurationMinutes,
        performanceOrder,
        isHeadliner,
        facebookUrl,
        twitterUrl,
        instagramUrl,
        youtubeUrl,
        linkedinUrl,
        tiktokUrl,
        isActive,
        priorityRanking,
        createdAt,
        updatedAt,
        eventId,
        distinct);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "EventFeaturedPerformersCriteria{" +
        (id != null ? "id=" + id + ", " : "") +
        (name != null ? "name=" + name + ", " : "") +
        (stageName != null ? "stageName=" + stageName + ", " : "") +
        (role != null ? "role=" + role + ", " : "") +
        (bio != null ? "bio=" + bio + ", " : "") +
        (nationality != null ? "nationality=" + nationality + ", " : "") +
        (dateOfBirth != null ? "dateOfBirth=" + dateOfBirth + ", " : "") +
        (email != null ? "email=" + email + ", " : "") +
        (phone != null ? "phone=" + phone + ", " : "") +
        (websiteUrl != null ? "websiteUrl=" + websiteUrl + ", " : "") +
        (portraitImageUrl != null ? "portraitImageUrl=" + portraitImageUrl + ", " : "") +
        (performanceImageUrl != null ? "performanceImageUrl=" + performanceImageUrl + ", " : "") +
        (galleryImageUrls != null ? "galleryImageUrls=" + galleryImageUrls + ", " : "") +
        (performanceDurationMinutes != null ? "performanceDurationMinutes=" + performanceDurationMinutes + ", " : "") +
        (performanceOrder != null ? "performanceOrder=" + performanceOrder + ", " : "") +
        (isHeadliner != null ? "isHeadliner=" + isHeadliner + ", " : "") +
        (facebookUrl != null ? "facebookUrl=" + facebookUrl + ", " : "") +
        (twitterUrl != null ? "twitterUrl=" + twitterUrl + ", " : "") +
        (instagramUrl != null ? "instagramUrl=" + instagramUrl + ", " : "") +
        (youtubeUrl != null ? "youtubeUrl=" + youtubeUrl + ", " : "") +
        (linkedinUrl != null ? "linkedinUrl=" + linkedinUrl + ", " : "") +
        (tiktokUrl != null ? "tiktokUrl=" + tiktokUrl + ", " : "") +
        (isActive != null ? "isActive=" + isActive + ", " : "") +
        (priorityRanking != null ? "priorityRanking=" + priorityRanking + ", " : "") +
        (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
        (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
        (eventId != null ? "eventId=" + eventId + ", " : "") +
        (distinct != null ? "distinct=" + distinct + ", " : "") +
        "}";
  }
}
