package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventSponsors} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventSponsorsDTO implements Serializable {

  private Long id;

  @NotNull
  @Size(max = 255)
  private String name;

  @NotNull
  @Size(max = 100)
  private String type;

  @Size(max = 255)
  private String companyName;

  @Size(max = 500)
  private String tagline;

  private String description;

  @Size(max = 1024)
  private String websiteUrl;

  @Size(max = 255)
  private String contactEmail;

  @Size(max = 50)
  private String contactPhone;

  @Size(max = 1024)
  private String logoUrl;

  @Size(max = 1024)
  private String heroImageUrl;

  @Size(max = 1024)
  private String bannerImageUrl;

  @NotNull
  private Boolean isActive = true;

  @Min(value = 0)
  private Integer priorityRanking = 0;

  @Size(max = 1024)
  private String facebookUrl;

  @Size(max = 1024)
  private String twitterUrl;

  @Size(max = 1024)
  private String linkedinUrl;

  @Size(max = 1024)
  private String instagramUrl;

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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getTagline() {
    return tagline;
  }

  public void setTagline(String tagline) {
    this.tagline = tagline;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getWebsiteUrl() {
    return websiteUrl;
  }

  public void setWebsiteUrl(String websiteUrl) {
    this.websiteUrl = websiteUrl;
  }

  public String getContactEmail() {
    return contactEmail;
  }

  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }

  public String getContactPhone() {
    return contactPhone;
  }

  public void setContactPhone(String contactPhone) {
    this.contactPhone = contactPhone;
  }

  public String getLogoUrl() {
    return logoUrl;
  }

  public void setLogoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
  }

  public String getHeroImageUrl() {
    return heroImageUrl;
  }

  public void setHeroImageUrl(String heroImageUrl) {
    this.heroImageUrl = heroImageUrl;
  }

  public String getBannerImageUrl() {
    return bannerImageUrl;
  }

  public void setBannerImageUrl(String bannerImageUrl) {
    this.bannerImageUrl = bannerImageUrl;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public Integer getPriorityRanking() {
    return priorityRanking;
  }

  public void setPriorityRanking(Integer priorityRanking) {
    this.priorityRanking = priorityRanking;
  }

  public String getFacebookUrl() {
    return facebookUrl;
  }

  public void setFacebookUrl(String facebookUrl) {
    this.facebookUrl = facebookUrl;
  }

  public String getTwitterUrl() {
    return twitterUrl;
  }

  public void setTwitterUrl(String twitterUrl) {
    this.twitterUrl = twitterUrl;
  }

  public String getLinkedinUrl() {
    return linkedinUrl;
  }

  public void setLinkedinUrl(String linkedinUrl) {
    this.linkedinUrl = linkedinUrl;
  }

  public String getInstagramUrl() {
    return instagramUrl;
  }

  public void setInstagramUrl(String instagramUrl) {
    this.instagramUrl = instagramUrl;
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
    if (this == o) {
      return true;
    }
    if (!(o instanceof EventSponsorsDTO)) {
      return false;
    }

    EventSponsorsDTO eventSponsorsDTO = (EventSponsorsDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, eventSponsorsDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "EventSponsorsDTO{" +
        "id=" + getId() +
        ", name='" + getName() + "'" +
        ", type='" + getType() + "'" +
        ", companyName='" + getCompanyName() + "'" +
        ", tagline='" + getTagline() + "'" +
        ", description='" + getDescription() + "'" +
        ", websiteUrl='" + getWebsiteUrl() + "'" +
        ", contactEmail='" + getContactEmail() + "'" +
        ", contactPhone='" + getContactPhone() + "'" +
        ", logoUrl='" + getLogoUrl() + "'" +
        ", heroImageUrl='" + getHeroImageUrl() + "'" +
        ", bannerImageUrl='" + getBannerImageUrl() + "'" +
        ", isActive='" + getIsActive() + "'" +
        ", priorityRanking=" + getPriorityRanking() +
        ", facebookUrl='" + getFacebookUrl() + "'" +
        ", twitterUrl='" + getTwitterUrl() + "'" +
        ", linkedinUrl='" + getLinkedinUrl() + "'" +
        ", instagramUrl='" + getInstagramUrl() + "'" +
        ", createdAt='" + getCreatedAt() + "'" +
        ", updatedAt='" + getUpdatedAt() + "'" +
        "}";
  }
}
