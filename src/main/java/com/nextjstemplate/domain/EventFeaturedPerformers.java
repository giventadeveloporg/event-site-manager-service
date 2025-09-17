package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventFeaturedPerformers.
 */
@Entity
@Table(name = "event_featured_performers")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventFeaturedPerformers implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  @Column(name = "id")
  private Long id;

  @Size(max = 255)
  @Column(name = "tenant_id", length = 255)
  private String tenantId;

  @NotNull
  @Size(max = 255)
  @Column(name = "name", length = 255, nullable = false)
  private String name;

  @Size(max = 255)
  @Column(name = "stage_name", length = 255)
  private String stageName;

  @Size(max = 100)
  @Column(name = "role", length = 100)
  private String role;

  @Column(name = "bio", columnDefinition = "text")
  private String bio;

  @Size(max = 100)
  @Column(name = "nationality", length = 100)
  private String nationality;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  @Size(max = 255)
  @Column(name = "email", length = 255)
  private String email;

  @Size(max = 50)
  @Column(name = "phone", length = 50)
  private String phone;

  @Size(max = 1024)
  @Column(name = "website_url", length = 1024)
  private String websiteUrl;

  @Size(max = 1024)
  @Column(name = "portrait_image_url", length = 1024)
  private String portraitImageUrl;

  @Size(max = 1024)
  @Column(name = "performance_image_url", length = 1024)
  private String performanceImageUrl;

  @Column(name = "gallery_image_urls", columnDefinition = "text")
  private String galleryImageUrls;

  @Min(value = 1)
  @Column(name = "performance_duration_minutes")
  private Integer performanceDurationMinutes;

  @Min(value = 0)
  @Column(name = "performance_order", nullable = false)
  private Integer performanceOrder = 0;

  @Column(name = "is_headliner", nullable = false)
  private Boolean isHeadliner = false;

  @Size(max = 1024)
  @Column(name = "facebook_url", length = 1024)
  private String facebookUrl;

  @Size(max = 1024)
  @Column(name = "twitter_url", length = 1024)
  private String twitterUrl;

  @Size(max = 1024)
  @Column(name = "instagram_url", length = 1024)
  private String instagramUrl;

  @Size(max = 1024)
  @Column(name = "youtube_url", length = 1024)
  private String youtubeUrl;

  @Size(max = 1024)
  @Column(name = "linkedin_url", length = 1024)
  private String linkedinUrl;

  @Size(max = 1024)
  @Column(name = "tiktok_url", length = 1024)
  private String tiktokUrl;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive = true;

  @Min(value = 0)
  @Column(name = "priority_ranking", nullable = false)
  private Integer priorityRanking = 0;

  @NotNull
  @Column(name = "created_at", nullable = false)
  private ZonedDateTime createdAt;

  @NotNull
  @Column(name = "updated_at", nullable = false)
  private ZonedDateTime updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnoreProperties(value = { "eventFeaturedPerformers", "eventContacts", "eventEmails",
      "eventProgramDirectors" }, allowSetters = true)
  private EventDetails event;

  // jhipster-needle-entity-add-field - JHipster will add fields here

  public Long getId() {
    return this.id;
  }

  public EventFeaturedPerformers id(Long id) {
    this.setId(id);
    return this;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTenantId() {
    return this.tenantId;
  }

  public EventFeaturedPerformers tenantId(String tenantId) {
    this.setTenantId(tenantId);
    return this;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public String getName() {
    return this.name;
  }

  public EventFeaturedPerformers name(String name) {
    this.setName(name);
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStageName() {
    return this.stageName;
  }

  public EventFeaturedPerformers stageName(String stageName) {
    this.setStageName(stageName);
    return this;
  }

  public void setStageName(String stageName) {
    this.stageName = stageName;
  }

  public String getRole() {
    return this.role;
  }

  public EventFeaturedPerformers role(String role) {
    this.setRole(role);
    return this;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getBio() {
    return this.bio;
  }

  public EventFeaturedPerformers bio(String bio) {
    this.setBio(bio);
    return this;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public String getNationality() {
    return this.nationality;
  }

  public EventFeaturedPerformers nationality(String nationality) {
    this.setNationality(nationality);
    return this;
  }

  public void setNationality(String nationality) {
    this.nationality = nationality;
  }

  public LocalDate getDateOfBirth() {
    return this.dateOfBirth;
  }

  public EventFeaturedPerformers dateOfBirth(LocalDate dateOfBirth) {
    this.setDateOfBirth(dateOfBirth);
    return this;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getEmail() {
    return this.email;
  }

  public EventFeaturedPerformers email(String email) {
    this.setEmail(email);
    return this;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return this.phone;
  }

  public EventFeaturedPerformers phone(String phone) {
    this.setPhone(phone);
    return this;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getWebsiteUrl() {
    return this.websiteUrl;
  }

  public EventFeaturedPerformers websiteUrl(String websiteUrl) {
    this.setWebsiteUrl(websiteUrl);
    return this;
  }

  public void setWebsiteUrl(String websiteUrl) {
    this.websiteUrl = websiteUrl;
  }

  public String getPortraitImageUrl() {
    return this.portraitImageUrl;
  }

  public EventFeaturedPerformers portraitImageUrl(String portraitImageUrl) {
    this.setPortraitImageUrl(portraitImageUrl);
    return this;
  }

  public void setPortraitImageUrl(String portraitImageUrl) {
    this.portraitImageUrl = portraitImageUrl;
  }

  public String getPerformanceImageUrl() {
    return this.performanceImageUrl;
  }

  public EventFeaturedPerformers performanceImageUrl(String performanceImageUrl) {
    this.setPerformanceImageUrl(performanceImageUrl);
    return this;
  }

  public void setPerformanceImageUrl(String performanceImageUrl) {
    this.performanceImageUrl = performanceImageUrl;
  }

  public String getGalleryImageUrls() {
    return this.galleryImageUrls;
  }

  public EventFeaturedPerformers galleryImageUrls(String galleryImageUrls) {
    this.setGalleryImageUrls(galleryImageUrls);
    return this;
  }

  public void setGalleryImageUrls(String galleryImageUrls) {
    this.galleryImageUrls = galleryImageUrls;
  }

  public Integer getPerformanceDurationMinutes() {
    return this.performanceDurationMinutes;
  }

  public EventFeaturedPerformers performanceDurationMinutes(Integer performanceDurationMinutes) {
    this.setPerformanceDurationMinutes(performanceDurationMinutes);
    return this;
  }

  public void setPerformanceDurationMinutes(Integer performanceDurationMinutes) {
    this.performanceDurationMinutes = performanceDurationMinutes;
  }

  public Integer getPerformanceOrder() {
    return this.performanceOrder;
  }

  public EventFeaturedPerformers performanceOrder(Integer performanceOrder) {
    this.setPerformanceOrder(performanceOrder);
    return this;
  }

  public void setPerformanceOrder(Integer performanceOrder) {
    this.performanceOrder = performanceOrder;
  }

  public Boolean getIsHeadliner() {
    return this.isHeadliner;
  }

  public EventFeaturedPerformers isHeadliner(Boolean isHeadliner) {
    this.setIsHeadliner(isHeadliner);
    return this;
  }

  public void setIsHeadliner(Boolean isHeadliner) {
    this.isHeadliner = isHeadliner;
  }

  public String getFacebookUrl() {
    return this.facebookUrl;
  }

  public EventFeaturedPerformers facebookUrl(String facebookUrl) {
    this.setFacebookUrl(facebookUrl);
    return this;
  }

  public void setFacebookUrl(String facebookUrl) {
    this.facebookUrl = facebookUrl;
  }

  public String getTwitterUrl() {
    return this.twitterUrl;
  }

  public EventFeaturedPerformers twitterUrl(String twitterUrl) {
    this.setTwitterUrl(twitterUrl);
    return this;
  }

  public void setTwitterUrl(String twitterUrl) {
    this.twitterUrl = twitterUrl;
  }

  public String getInstagramUrl() {
    return this.instagramUrl;
  }

  public EventFeaturedPerformers instagramUrl(String instagramUrl) {
    this.setInstagramUrl(instagramUrl);
    return this;
  }

  public void setInstagramUrl(String instagramUrl) {
    this.instagramUrl = instagramUrl;
  }

  public String getYoutubeUrl() {
    return this.youtubeUrl;
  }

  public EventFeaturedPerformers youtubeUrl(String youtubeUrl) {
    this.setYoutubeUrl(youtubeUrl);
    return this;
  }

  public void setYoutubeUrl(String youtubeUrl) {
    this.youtubeUrl = youtubeUrl;
  }

  public String getLinkedinUrl() {
    return this.linkedinUrl;
  }

  public EventFeaturedPerformers linkedinUrl(String linkedinUrl) {
    this.setLinkedinUrl(linkedinUrl);
    return this;
  }

  public void setLinkedinUrl(String linkedinUrl) {
    this.linkedinUrl = linkedinUrl;
  }

  public String getTiktokUrl() {
    return this.tiktokUrl;
  }

  public EventFeaturedPerformers tiktokUrl(String tiktokUrl) {
    this.setTiktokUrl(tiktokUrl);
    return this;
  }

  public void setTiktokUrl(String tiktokUrl) {
    this.tiktokUrl = tiktokUrl;
  }

  public Boolean getIsActive() {
    return this.isActive;
  }

  public EventFeaturedPerformers isActive(Boolean isActive) {
    this.setIsActive(isActive);
    return this;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public Integer getPriorityRanking() {
    return this.priorityRanking;
  }

  public EventFeaturedPerformers priorityRanking(Integer priorityRanking) {
    this.setPriorityRanking(priorityRanking);
    return this;
  }

  public void setPriorityRanking(Integer priorityRanking) {
    this.priorityRanking = priorityRanking;
  }

  public ZonedDateTime getCreatedAt() {
    return this.createdAt;
  }

  public EventFeaturedPerformers createdAt(ZonedDateTime createdAt) {
    this.setCreatedAt(createdAt);
    return this;
  }

  public void setCreatedAt(ZonedDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public ZonedDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public EventFeaturedPerformers updatedAt(ZonedDateTime updatedAt) {
    this.setUpdatedAt(updatedAt);
    return this;
  }

  public void setUpdatedAt(ZonedDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public EventDetails getEvent() {
    return this.event;
  }

  public void setEvent(EventDetails event) {
    this.event = event;
  }

  public EventFeaturedPerformers event(EventDetails event) {
    this.setEvent(event);
    return this;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
  // setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EventFeaturedPerformers)) {
      return false;
    }
    return getId() != null && getId().equals(((EventFeaturedPerformers) o).getId());
  }

  @Override
  public int hashCode() {
    // see
    // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "EventFeaturedPerformers{" +
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
        "}";
  }
}
