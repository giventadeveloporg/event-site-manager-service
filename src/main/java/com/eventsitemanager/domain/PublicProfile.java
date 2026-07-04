package com.eventsitemanager.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PublicProfile.
 */
@Entity
@Table(name = "public_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PublicProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "publicProfileSeq")
    @SequenceGenerator(name = "publicProfileSeq", sequenceName = "public.public_profile_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    @Column(name = "display_name", length = 255, nullable = false)
    private String displayName;

    @Size(max = 500)
    @Column(name = "tagline", length = 500)
    private String tagline;

    @Size(max = 500)
    @Column(name = "headline", length = 500)
    private String headline;

    @Lob
    @Column(name = "bio_markdown")
    private String bioMarkdown;

    @Size(max = 1024)
    @Column(name = "profile_image_url", length = 1024)
    private String profileImageUrl;

    @Size(max = 1024)
    @Column(name = "cover_image_url", length = 1024)
    private String coverImageUrl;

    @Size(max = 255)
    @Column(name = "location", length = 255)
    private String location;

    @Size(max = 255)
    @Column(name = "languages", length = 255)
    private String languages;

    @Size(max = 100)
    @Column(name = "public_slug", length = 100)
    private String publicSlug;

    @Size(max = 255)
    @Column(name = "contact_email", length = 255)
    private String contactEmail;

    @NotNull
    @Column(name = "contact_form_enabled", nullable = false)
    private Boolean contactFormEnabled = Boolean.FALSE;

    @Size(max = 500)
    @Column(name = "linkedin_url", length = 500)
    private String linkedinUrl;

    @Size(max = 500)
    @Column(name = "twitter_url", length = 500)
    private String twitterUrl;

    @Size(max = 500)
    @Column(name = "facebook_url", length = 500)
    private String facebookUrl;

    @Size(max = 500)
    @Column(name = "instagram_url", length = 500)
    private String instagramUrl;

    @Size(max = 500)
    @Column(name = "youtube_url", length = 500)
    private String youtubeUrl;

    @Size(max = 500)
    @Column(name = "website_url", length = 500)
    private String websiteUrl;

    @Size(max = 1024)
    @Column(name = "cv_document_url", length = 1024)
    private String cvDocumentUrl;

    @Size(max = 255)
    @Column(name = "meta_title", length = 255)
    private String metaTitle;

    @Size(max = 500)
    @Column(name = "meta_description", length = 500)
    private String metaDescription;

    @NotNull
    @Column(name = "is_published", nullable = false)
    private Boolean isPublished = Boolean.FALSE;

    @Column(name = "owner_user_profile_id")
    private Long ownerUserProfileId;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getBioMarkdown() {
        return bioMarkdown;
    }

    public void setBioMarkdown(String bioMarkdown) {
        this.bioMarkdown = bioMarkdown;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getPublicSlug() {
        return publicSlug;
    }

    public void setPublicSlug(String publicSlug) {
        this.publicSlug = publicSlug;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Boolean getContactFormEnabled() {
        return contactFormEnabled;
    }

    public void setContactFormEnabled(Boolean contactFormEnabled) {
        this.contactFormEnabled = contactFormEnabled;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
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

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getCvDocumentUrl() {
        return cvDocumentUrl;
    }

    public void setCvDocumentUrl(String cvDocumentUrl) {
        this.cvDocumentUrl = cvDocumentUrl;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public Boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

    public Long getOwnerUserProfileId() {
        return ownerUserProfileId;
    }

    public void setOwnerUserProfileId(Long ownerUserProfileId) {
        this.ownerUserProfileId = ownerUserProfileId;
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
        if (this == o) return true;
        if (!(o instanceof PublicProfile)) return false;
        return id != null && id.equals(((PublicProfile) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
