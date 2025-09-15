package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventSponsors}
 * entity.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventSponsorsCriteria implements Serializable, Criteria {

  private static final long serialVersionUID = 1L;

  private LongFilter id;
  private StringFilter name;
  private StringFilter type;
  private StringFilter companyName;
  private StringFilter tagline;
  private StringFilter description;
  private StringFilter websiteUrl;
  private StringFilter contactEmail;
  private StringFilter contactPhone;
  private StringFilter logoUrl;
  private StringFilter heroImageUrl;
  private StringFilter bannerImageUrl;
  private BooleanFilter isActive;
  private IntegerFilter priorityRanking;
  private StringFilter facebookUrl;
  private StringFilter twitterUrl;
  private StringFilter linkedinUrl;
  private StringFilter instagramUrl;
  private ZonedDateTimeFilter createdAt;
  private ZonedDateTimeFilter updatedAt;
  private Boolean distinct;

  public EventSponsorsCriteria() {
  }

  public EventSponsorsCriteria(EventSponsorsCriteria other) {
    this.id = other.id == null ? null : other.id.copy();
    this.name = other.name == null ? null : other.name.copy();
    this.type = other.type == null ? null : other.type.copy();
    this.companyName = other.companyName == null ? null : other.companyName.copy();
    this.tagline = other.tagline == null ? null : other.tagline.copy();
    this.description = other.description == null ? null : other.description.copy();
    this.websiteUrl = other.websiteUrl == null ? null : other.websiteUrl.copy();
    this.contactEmail = other.contactEmail == null ? null : other.contactEmail.copy();
    this.contactPhone = other.contactPhone == null ? null : other.contactPhone.copy();
    this.logoUrl = other.logoUrl == null ? null : other.logoUrl.copy();
    this.heroImageUrl = other.heroImageUrl == null ? null : other.heroImageUrl.copy();
    this.bannerImageUrl = other.bannerImageUrl == null ? null : other.bannerImageUrl.copy();
    this.isActive = other.isActive == null ? null : other.isActive.copy();
    this.priorityRanking = other.priorityRanking == null ? null : other.priorityRanking.copy();
    this.facebookUrl = other.facebookUrl == null ? null : other.facebookUrl.copy();
    this.twitterUrl = other.twitterUrl == null ? null : other.twitterUrl.copy();
    this.linkedinUrl = other.linkedinUrl == null ? null : other.linkedinUrl.copy();
    this.instagramUrl = other.instagramUrl == null ? null : other.instagramUrl.copy();
    this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
    this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
    this.distinct = other.distinct;
  }

  @Override
  public EventSponsorsCriteria copy() {
    return new EventSponsorsCriteria(this);
  }

  public LongFilter getId() {
    return id;
  }

  public void setId(LongFilter id) {
    this.id = id;
  }

  public StringFilter getName() {
    return name;
  }

  public void setName(StringFilter name) {
    this.name = name;
  }

  public StringFilter getType() {
    return type;
  }

  public void setType(StringFilter type) {
    this.type = type;
  }

  public StringFilter getCompanyName() {
    return companyName;
  }

  public void setCompanyName(StringFilter companyName) {
    this.companyName = companyName;
  }

  public StringFilter getTagline() {
    return tagline;
  }

  public void setTagline(StringFilter tagline) {
    this.tagline = tagline;
  }

  public StringFilter getDescription() {
    return description;
  }

  public void setDescription(StringFilter description) {
    this.description = description;
  }

  public StringFilter getWebsiteUrl() {
    return websiteUrl;
  }

  public void setWebsiteUrl(StringFilter websiteUrl) {
    this.websiteUrl = websiteUrl;
  }

  public StringFilter getContactEmail() {
    return contactEmail;
  }

  public void setContactEmail(StringFilter contactEmail) {
    this.contactEmail = contactEmail;
  }

  public StringFilter getContactPhone() {
    return contactPhone;
  }

  public void setContactPhone(StringFilter contactPhone) {
    this.contactPhone = contactPhone;
  }

  public StringFilter getLogoUrl() {
    return logoUrl;
  }

  public void setLogoUrl(StringFilter logoUrl) {
    this.logoUrl = logoUrl;
  }

  public StringFilter getHeroImageUrl() {
    return heroImageUrl;
  }

  public void setHeroImageUrl(StringFilter heroImageUrl) {
    this.heroImageUrl = heroImageUrl;
  }

  public StringFilter getBannerImageUrl() {
    return bannerImageUrl;
  }

  public void setBannerImageUrl(StringFilter bannerImageUrl) {
    this.bannerImageUrl = bannerImageUrl;
  }

  public BooleanFilter getIsActive() {
    return isActive;
  }

  public void setIsActive(BooleanFilter isActive) {
    this.isActive = isActive;
  }

  public IntegerFilter getPriorityRanking() {
    return priorityRanking;
  }

  public void setPriorityRanking(IntegerFilter priorityRanking) {
    this.priorityRanking = priorityRanking;
  }

  public StringFilter getFacebookUrl() {
    return facebookUrl;
  }

  public void setFacebookUrl(StringFilter facebookUrl) {
    this.facebookUrl = facebookUrl;
  }

  public StringFilter getTwitterUrl() {
    return twitterUrl;
  }

  public void setTwitterUrl(StringFilter twitterUrl) {
    this.twitterUrl = twitterUrl;
  }

  public StringFilter getLinkedinUrl() {
    return linkedinUrl;
  }

  public void setLinkedinUrl(StringFilter linkedinUrl) {
    this.linkedinUrl = linkedinUrl;
  }

  public StringFilter getInstagramUrl() {
    return instagramUrl;
  }

  public void setInstagramUrl(StringFilter instagramUrl) {
    this.instagramUrl = instagramUrl;
  }

  public ZonedDateTimeFilter getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(ZonedDateTimeFilter createdAt) {
    this.createdAt = createdAt;
  }

  public ZonedDateTimeFilter getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Boolean getDistinct() {
    return distinct;
  }

  public void setDistinct(Boolean distinct) {
    this.distinct = distinct;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    EventSponsorsCriteria that = (EventSponsorsCriteria) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(name, that.name) &&
        Objects.equals(type, that.type) &&
        Objects.equals(companyName, that.companyName) &&
        Objects.equals(tagline, that.tagline) &&
        Objects.equals(description, that.description) &&
        Objects.equals(websiteUrl, that.websiteUrl) &&
        Objects.equals(contactEmail, that.contactEmail) &&
        Objects.equals(contactPhone, that.contactPhone) &&
        Objects.equals(logoUrl, that.logoUrl) &&
        Objects.equals(heroImageUrl, that.heroImageUrl) &&
        Objects.equals(bannerImageUrl, that.bannerImageUrl) &&
        Objects.equals(isActive, that.isActive) &&
        Objects.equals(priorityRanking, that.priorityRanking) &&
        Objects.equals(facebookUrl, that.facebookUrl) &&
        Objects.equals(twitterUrl, that.twitterUrl) &&
        Objects.equals(linkedinUrl, that.linkedinUrl) &&
        Objects.equals(instagramUrl, that.instagramUrl) &&
        Objects.equals(createdAt, that.createdAt) &&
        Objects.equals(updatedAt, that.updatedAt) &&
        Objects.equals(distinct, that.distinct);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, type, companyName, tagline, description, websiteUrl, contactEmail, contactPhone,
        logoUrl, heroImageUrl, bannerImageUrl, isActive, priorityRanking, facebookUrl, twitterUrl, linkedinUrl,
        instagramUrl, createdAt, updatedAt, distinct);
  }

  @Override
  public String toString() {
    return "EventSponsorsCriteria{" +
        (id != null ? "id=" + id + ", " : "") +
        (name != null ? "name=" + name + ", " : "") +
        (type != null ? "type=" + type + ", " : "") +
        (companyName != null ? "companyName=" + companyName + ", " : "") +
        (tagline != null ? "tagline=" + tagline + ", " : "") +
        (description != null ? "description=" + description + ", " : "") +
        (websiteUrl != null ? "websiteUrl=" + websiteUrl + ", " : "") +
        (contactEmail != null ? "contactEmail=" + contactEmail + ", " : "") +
        (contactPhone != null ? "contactPhone=" + contactPhone + ", " : "") +
        (logoUrl != null ? "logoUrl=" + logoUrl + ", " : "") +
        (heroImageUrl != null ? "heroImageUrl=" + heroImageUrl + ", " : "") +
        (bannerImageUrl != null ? "bannerImageUrl=" + bannerImageUrl + ", " : "") +
        (isActive != null ? "isActive=" + isActive + ", " : "") +
        (priorityRanking != null ? "priorityRanking=" + priorityRanking + ", " : "") +
        (facebookUrl != null ? "facebookUrl=" + facebookUrl + ", " : "") +
        (twitterUrl != null ? "twitterUrl=" + twitterUrl + ", " : "") +
        (linkedinUrl != null ? "linkedinUrl=" + linkedinUrl + ", " : "") +
        (instagramUrl != null ? "instagramUrl=" + instagramUrl + ", " : "") +
        (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
        (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
        (distinct != null ? "distinct=" + distinct + ", " : "") +
        "}";
  }
}
