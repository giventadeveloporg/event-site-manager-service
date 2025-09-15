package com.nextjstemplate.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventSponsors.
 */
@Entity
@Table(name = "event_sponsors")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventSponsors implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  @Column(name = "id")
  private Long id;

  @NotNull
  @Size(max = 255)
  @Column(name = "name", length = 255, nullable = false)
  private String name;

  @NotNull
  @Size(max = 100)
  @Column(name = "type", length = 100, nullable = false)
  private String type;

  @Size(max = 255)
  @Column(name = "company_name", length = 255)
  private String companyName;

  @Size(max = 500)
  @Column(name = "tagline", length = 500)
  private String tagline;

  @Column(name = "description", columnDefinition = "text")
  private String description;

  @Size(max = 1024)
  @Column(name = "website_url", length = 1024)
  private String websiteUrl;

  @Size(max = 255)
  @Column(name = "contact_email", length = 255)
  private String contactEmail;

  @Size(max = 50)
  @Column(name = "contact_phone", length = 50)
  private String contactPhone;

  @Size(max = 1024)
  @Column(name = "logo_url", length = 1024)
  private String logoUrl;

  @Size(max = 1024)
  @Column(name = "hero_image_url", length = 1024)
  private String heroImageUrl;

  @Size(max = 1024)
  @Column(name = "banner_image_url", length = 1024)
  private String bannerImageUrl;

  @NotNull
  @Column(name = "is_active", nullable = false)
  private Boolean isActive = true;

  @Min(value = 0)
  @Column(name = "priority_ranking", nullable = false)
  private Integer priorityRanking = 0;

  @Size(max = 1024)
  @Column(name = "facebook_url", length = 1024)
  private String facebookUrl;

  @Size(max = 1024)
  @Column(name = "twitter_url", length = 1024)
  private String twitterUrl;

  @Size(max = 1024)
  @Column(name = "linkedin_url", length = 1024)
  private String linkedinUrl;

  @Size(max = 1024)
  @Column(name = "instagram_url", length = 1024)
  private String instagramUrl;

  @NotNull
  @Column(name = "created_at", nullable = false)
  private ZonedDateTime createdAt;

  @NotNull
  @Column(name = "updated_at", nullable = false)
  private ZonedDateTime updatedAt;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "sponsor")
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private Set<EventSponsorsJoin> eventSponsorsJoins = new HashSet<>();

  // jhipster-needle-entity-add-field - JHipster will add fields here

  public Long getId() {
    return this.id;
  }

  public EventSponsors id(Long id) {
    this.setId(id);
    return this;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public EventSponsors name(String name) {
    this.setName(name);
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return this.type;
  }

  public EventSponsors type(String type) {
    this.setType(type);
    return this;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getCompanyName() {
    return this.companyName;
  }

  public EventSponsors companyName(String companyName) {
    this.setCompanyName(companyName);
    return this;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getTagline() {
    return this.tagline;
  }

  public EventSponsors tagline(String tagline) {
    this.setTagline(tagline);
    return this;
  }

  public void setTagline(String tagline) {
    this.tagline = tagline;
  }

  public String getDescription() {
    return this.description;
  }

  public EventSponsors description(String description) {
    this.setDescription(description);
    return this;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getWebsiteUrl() {
    return this.websiteUrl;
  }

  public EventSponsors websiteUrl(String websiteUrl) {
    this.setWebsiteUrl(websiteUrl);
    return this;
  }

  public void setWebsiteUrl(String websiteUrl) {
    this.websiteUrl = websiteUrl;
  }

  public String getContactEmail() {
    return this.contactEmail;
  }

  public EventSponsors contactEmail(String contactEmail) {
    this.setContactEmail(contactEmail);
    return this;
  }

  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }

  public String getContactPhone() {
    return this.contactPhone;
  }

  public EventSponsors contactPhone(String contactPhone) {
    this.setContactPhone(contactPhone);
    return this;
  }

  public void setContactPhone(String contactPhone) {
    this.contactPhone = contactPhone;
  }

  public String getLogoUrl() {
    return this.logoUrl;
  }

  public EventSponsors logoUrl(String logoUrl) {
    this.setLogoUrl(logoUrl);
    return this;
  }

  public void setLogoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
  }

  public String getHeroImageUrl() {
    return this.heroImageUrl;
  }

  public EventSponsors heroImageUrl(String heroImageUrl) {
    this.setHeroImageUrl(heroImageUrl);
    return this;
  }

  public void setHeroImageUrl(String heroImageUrl) {
    this.heroImageUrl = heroImageUrl;
  }

  public String getBannerImageUrl() {
    return this.bannerImageUrl;
  }

  public EventSponsors bannerImageUrl(String bannerImageUrl) {
    this.setBannerImageUrl(bannerImageUrl);
    return this;
  }

  public void setBannerImageUrl(String bannerImageUrl) {
    this.bannerImageUrl = bannerImageUrl;
  }

  public Boolean getIsActive() {
    return this.isActive;
  }

  public EventSponsors isActive(Boolean isActive) {
    this.setIsActive(isActive);
    return this;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public Integer getPriorityRanking() {
    return this.priorityRanking;
  }

  public EventSponsors priorityRanking(Integer priorityRanking) {
    this.setPriorityRanking(priorityRanking);
    return this;
  }

  public void setPriorityRanking(Integer priorityRanking) {
    this.priorityRanking = priorityRanking;
  }

  public String getFacebookUrl() {
    return this.facebookUrl;
  }

  public EventSponsors facebookUrl(String facebookUrl) {
    this.setFacebookUrl(facebookUrl);
    return this;
  }

  public void setFacebookUrl(String facebookUrl) {
    this.facebookUrl = facebookUrl;
  }

  public String getTwitterUrl() {
    return this.twitterUrl;
  }

  public EventSponsors twitterUrl(String twitterUrl) {
    this.setTwitterUrl(twitterUrl);
    return this;
  }

  public void setTwitterUrl(String twitterUrl) {
    this.twitterUrl = twitterUrl;
  }

  public String getLinkedinUrl() {
    return this.linkedinUrl;
  }

  public EventSponsors linkedinUrl(String linkedinUrl) {
    this.setLinkedinUrl(linkedinUrl);
    return this;
  }

  public void setLinkedinUrl(String linkedinUrl) {
    this.linkedinUrl = linkedinUrl;
  }

  public String getInstagramUrl() {
    return this.instagramUrl;
  }

  public EventSponsors instagramUrl(String instagramUrl) {
    this.setInstagramUrl(instagramUrl);
    return this;
  }

  public void setInstagramUrl(String instagramUrl) {
    this.instagramUrl = instagramUrl;
  }

  public ZonedDateTime getCreatedAt() {
    return this.createdAt;
  }

  public EventSponsors createdAt(ZonedDateTime createdAt) {
    this.setCreatedAt(createdAt);
    return this;
  }

  public void setCreatedAt(ZonedDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public ZonedDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public EventSponsors updatedAt(ZonedDateTime updatedAt) {
    this.setUpdatedAt(updatedAt);
    return this;
  }

  public void setUpdatedAt(ZonedDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Set<EventSponsorsJoin> getEventSponsorsJoins() {
    return this.eventSponsorsJoins;
  }

  public void setEventSponsorsJoins(Set<EventSponsorsJoin> eventSponsorsJoins) {
    if (this.eventSponsorsJoins != null) {
      this.eventSponsorsJoins.forEach(i -> i.setSponsor(null));
    }
    if (eventSponsorsJoins != null) {
      eventSponsorsJoins.forEach(i -> i.setSponsor(this));
    }
    this.eventSponsorsJoins = eventSponsorsJoins;
  }

  public EventSponsors eventSponsorsJoins(Set<EventSponsorsJoin> eventSponsorsJoins) {
    this.setEventSponsorsJoins(eventSponsorsJoins);
    return this;
  }

  public EventSponsors addEventSponsorsJoin(EventSponsorsJoin eventSponsorsJoin) {
    this.eventSponsorsJoins.add(eventSponsorsJoin);
    eventSponsorsJoin.setSponsor(this);
    return this;
  }

  public EventSponsors removeEventSponsorsJoin(EventSponsorsJoin eventSponsorsJoin) {
    this.eventSponsorsJoins.remove(eventSponsorsJoin);
    eventSponsorsJoin.setSponsor(null);
    return this;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
  // setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EventSponsors)) {
      return false;
    }
    return getId() != null && getId().equals(((EventSponsors) o).getId());
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
    return "EventSponsors{" +
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



