package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventFeaturedPerformers}
 * entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventFeaturedPerformersDTO implements Serializable {

  private Long id;

  @Size(max = 255)
  private String tenantId;

  @NotNull
  @Size(max = 255)
  private String name;

  @Size(max = 255)
  private String stageName;

  @Size(max = 100)
  private String role;

  private String bio;

  @Size(max = 100)
  private String nationality;

  private LocalDate dateOfBirth;

  @Size(max = 255)
  private String email;

  @Size(max = 50)
  private String phone;

  @Size(max = 1024)
  private String websiteUrl;

  @Size(max = 1024)
  private String portraitImageUrl;

  @Size(max = 1024)
  private String performanceImageUrl;

  private String galleryImageUrls;

  @Min(value = 1)
  private Integer performanceDurationMinutes;

  @Min(value = 0)
  private Integer performanceOrder = 0;

  @NotNull
  private Boolean isHeadliner = false;

  @Size(max = 1024)
  private String facebookUrl;

  @Size(max = 1024)
  private String twitterUrl;

  @Size(max = 1024)
  private String instagramUrl;

  @Size(max = 1024)
  private String youtubeUrl;

  @Size(max = 1024)
  private String linkedinUrl;

  @Size(max = 1024)
  private String tiktokUrl;

  @NotNull
  private Boolean isActive = true;

  @Min(value = 0)
  private Integer priorityRanking = 0;

  @NotNull
  private ZonedDateTime createdAt;

  @NotNull
  private ZonedDateTime updatedAt;

  private EventDetailsDTO event;

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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStageName() {
    return stageName;
  }

  public void setStageName(String stageName) {
    this.stageName = stageName;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public String getNationality() {
    return nationality;
  }

  public void setNationality(String nationality) {
    this.nationality = nationality;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getWebsiteUrl() {
    return websiteUrl;
  }

  public void setWebsiteUrl(String websiteUrl) {
    this.websiteUrl = websiteUrl;
  }

  public String getPortraitImageUrl() {
    return portraitImageUrl;
  }

  public void setPortraitImageUrl(String portraitImageUrl) {
    this.portraitImageUrl = portraitImageUrl;
  }

  public String getPerformanceImageUrl() {
    return performanceImageUrl;
  }

  public void setPerformanceImageUrl(String performanceImageUrl) {
    this.performanceImageUrl = performanceImageUrl;
  }

  public String getGalleryImageUrls() {
    return galleryImageUrls;
  }

  public void setGalleryImageUrls(String galleryImageUrls) {
    this.galleryImageUrls = galleryImageUrls;
  }

  public Integer getPerformanceDurationMinutes() {
    return performanceDurationMinutes;
  }

  public void setPerformanceDurationMinutes(Integer performanceDurationMinutes) {
    this.performanceDurationMinutes = performanceDurationMinutes;
  }

  public Integer getPerformanceOrder() {
    return performanceOrder;
  }

  public void setPerformanceOrder(Integer performanceOrder) {
    this.performanceOrder = performanceOrder;
  }

  public Boolean getIsHeadliner() {
    return isHeadliner;
  }

  public void setIsHeadliner(Boolean isHeadliner) {
    this.isHeadliner = isHeadliner;
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

  public String getInstagramUrl() {
    return instagramUrl;
  }

  public void setInstagramUrl(String instagramUrl) {
    this.instagramUrl = instagramUrl;
  }

  public String getYoutubeUrl() {
    return youtubeUrl;
  }

  public void setYoutubeUrl(String youtubeUrl) {
    this.youtubeUrl = youtubeUrl;
  }

  public String getLinkedinUrl() {
    return linkedinUrl;
  }

  public void setLinkedinUrl(String linkedinUrl) {
    this.linkedinUrl = linkedinUrl;
  }

  public String getTiktokUrl() {
    return tiktokUrl;
  }

  public void setTiktokUrl(String tiktokUrl) {
    this.tiktokUrl = tiktokUrl;
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

  public EventDetailsDTO getEvent() {
    return event;
  }

  public void setEvent(EventDetailsDTO event) {
    this.event = event;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EventFeaturedPerformersDTO)) {
      return false;
    }

    EventFeaturedPerformersDTO eventFeaturedPerformersDTO = (EventFeaturedPerformersDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, eventFeaturedPerformersDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "EventFeaturedPerformersDTO{" +
        "id=" + getId() +
        ", name='" + getName() + "'" +
        ", stageName='" + getStageName() + "'" +
        ", role='" + getRole() + "'" +
        ", bio='" + getBio() + "'" +
        ", nationality='" + getNationality() + "'" +
        ", dateOfBirth='" + getDateOfBirth() + "'" +
        ", email='" + getEmail() + "'" +
        ", phone='" + getPhone() + "'" +
        ", websiteUrl='" + getWebsiteUrl() + "'" +
        ", portraitImageUrl='" + getPortraitImageUrl() + "'" +
        ", performanceImageUrl='" + getPerformanceImageUrl() + "'" +
        ", galleryImageUrls='" + getGalleryImageUrls() + "'" +
        ", performanceDurationMinutes=" + getPerformanceDurationMinutes() +
        ", performanceOrder=" + getPerformanceOrder() +
        ", isHeadliner='" + getIsHeadliner() + "'" +
        ", facebookUrl='" + getFacebookUrl() + "'" +
        ", twitterUrl='" + getTwitterUrl() + "'" +
        ", instagramUrl='" + getInstagramUrl() + "'" +
        ", youtubeUrl='" + getYoutubeUrl() + "'" +
        ", linkedinUrl='" + getLinkedinUrl() + "'" +
        ", tiktokUrl='" + getTiktokUrl() + "'" +
        ", isActive='" + getIsActive() + "'" +
        ", priorityRanking=" + getPriorityRanking() +
        ", createdAt='" + getCreatedAt() + "'" +
        ", updatedAt='" + getUpdatedAt() + "'" +
        ", event=" + getEvent() +
        "}";
  }
}
