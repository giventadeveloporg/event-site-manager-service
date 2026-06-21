package com.nextjstemplate.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing S3 operations.
 */
public interface S3Service {
    /**
     * Upload a file to S3.
     *
     * @param file    the file to upload.
     * @param eventId the event ID for organization.
     * @param title   the title of the file.
     * @return the S3 URL of the uploaded file.
     */
    String uploadFile(MultipartFile file, Long eventId, String title, String tenantId, Boolean isTeamMemberProfileImage);

    /**
     * Upload a file to S3 with entity-specific path construction.
     *
     * @param file       the file to upload.
     * @param eventId    the event ID for organization.
     * @param entityId   the entity ID (performer, sponsor, contact, etc.).
     * @param entityType the type of entity (featured-performer, sponsor, contact,
     *                   etc.).
     * @param imageType  the type of image (portrait, performance, gallery, logo,
     *                   hero, banner, photo).
     * @param title      the title of the file.
     * @param tenantId   the tenant ID.
     * @return the S3 URL of the uploaded file.
     */
    String uploadFileWithEntityPath(
        MultipartFile file,
        Long eventId,
        Long entityId,
        String entityType,
        String imageType,
        String title,
        String tenantId
    );

    /**
     * Generate a presigned URL for S3 file access.
     *
     * @param fileUrl         the S3 file URL.
     * @param expirationHours the expiration time in hours.
     * @return the presigned URL.
     */
    String generatePresignedUrl(String fileUrl, int expirationHours);

    /**
     * Delete a file from S3.
     *
     * @param fileUrl the S3 file URL.
     */
    void deleteFile(String fileUrl);

    /**
     * Get file metadata from S3.
     *
     * @param fileUrl the S3 file URL.
     * @return the file metadata.
     */
    ObjectMetadata getFileMetadata(String fileUrl);

    /**
     * Check if a file exists in S3.
     *
     * @param fileUrl the S3 file URL.
     * @return true if file exists, false otherwise.
     */
    boolean fileExists(String fileUrl);

    /**
     * Download an HTML file from S3 by its URL and return its content as a String.
     *
     * @param url the S3 file URL.
     * @return the HTML content as a String.
     */
    String downloadHtmlFromUrl(String url);

    /**
     * Upload a file to S3 with a specific path.
     *
     * @param file the file to upload.
     * @param s3Path the S3 path (key) where the file should be stored.
     * @return the S3 URL of the uploaded file.
     */
    String uploadFile(String s3Path, MultipartFile file);

    /**
     * Generate S3 path for sponsor images.
     * Path format: {profilePrefix}/media/tenantId/{tenantId}/sponsor/sponsor_id/{sponsorId}/{imageType}_{timestamp}_{uuid}.{ext}
     *
     * @param tenantId the tenant ID.
     * @param sponsorId the sponsor ID.
     * @param originalFilename the original filename (used for extension).
     * @param imageType optional image type (logo, hero, banner, LOGO_IMAGE, HERO_IMAGE, BANNER_IMAGE) - if provided, used as filename base instead of original filename.
     * @return the S3 path for the sponsor image.
     */
    String generateSponsorImagePath(String tenantId, Long sponsorId, String originalFilename, String imageType);

    /**
     * Generate S3 path for sponsor images (backward compatibility - uses original filename).
     * Path format: {profilePrefix}/media/tenantId/{tenantId}/sponsor/sponsor_id/{sponsorId}/{filename}_{timestamp}_{uuid}.{ext}
     *
     * @param tenantId the tenant ID.
     * @param sponsorId the sponsor ID.
     * @param originalFilename the original filename.
     * @return the S3 path for the sponsor image.
     */
    default String generateSponsorImagePath(String tenantId, Long sponsorId, String originalFilename) {
        return generateSponsorImagePath(tenantId, sponsorId, originalFilename, null);
    }

    /**
     * Generate S3 path for event-sponsor join images.
     * Path format: {profile}/events/tenantId/{tenantId}/event-id/{eventId}/sponsor/sponsor_id/{sponsorId}/{filename}
     *
     * @param tenantId the tenant ID.
     * @param eventId the event ID.
     * @param sponsorId the sponsor ID.
     * @param originalFilename the original filename.
     * @return the S3 path for the event-sponsor join image.
     */
    String generateEventSponsorJoinImagePath(String tenantId, Long eventId, Long sponsorId, String originalFilename);

    /**
     * Generate S3 path for event-director poster images.
     * Path format: {profile}/events/tenantId/{tenantId}/event-id/{eventId}/program-directors/director_id/{directorId}/{filename}
     *
     * @param tenantId Tenant ID
     * @param eventId Event ID
     * @param directorId Director ID
     * @param originalFilename Original filename from upload
     * @return S3 path string
     */
    String generateEventDirectorPosterPath(String tenantId, Long eventId, Long directorId, String originalFilename);

    /**
     * Generate S3 path for event-performer poster images.
     * Path format: {profile}/events/tenantId/{tenantId}/event-id/{eventId}/performers/performer_id/{performerId}/{filename}
     *
     * @param tenantId Tenant ID
     * @param eventId Event ID
     * @param performerId Performer ID
     * @param originalFilename Original filename from upload
     * @return S3 path string
     */
    String generateEventPerformerPosterPath(String tenantId, Long eventId, Long performerId, String originalFilename);

    /**
     * Generate S3 path for performer images.
     * Path format: {profile}/media/tenantId/{tenantId}/performer/performer_id/{performerId}/{filename}
     *
     * @param tenantId the tenant ID.
     * @param performerId the performer ID.
     * @param originalFilename the original filename.
     * @return the S3 path for the performer image.
     */
    String generatePerformerImagePath(String tenantId, Long performerId, String originalFilename);

    /**
     * Generate S3 path for director images.
     * Path format: {profile}/media/tenantId/{tenantId}/director/director_id/{directorId}/{filename}
     *
     * @param tenantId the tenant ID.
     * @param directorId the director ID.
     * @param originalFilename the original filename.
     * @return the S3 path for the director image.
     */
    String generateDirectorImagePath(String tenantId, Long directorId, String originalFilename);

    /**
     * Generate S3 path for email header images.
     * Path format: {profile}/events/tenantId/{tenantId}/event-id/{eventId}/tickets/email-templates/email_header_image_{timestamp}_{uuid}.{ext}
     *
     * @param tenantId Tenant ID
     * @param eventId Event ID
     * @param originalFilename Original filename from upload
     * @return S3 path string
     */
    String generateEmailHeaderImagePath(String tenantId, Long eventId, String originalFilename);

    /**
     * Generate S3 path for promotional email header images.
     * Path format: {profile}/events/tenantId/{tenantId}/event-id/{eventId}/promotional_email_templates/promotion-id/{promotionId}/email_header_image_{timestamp}_{uuid}.{ext}
     *
     * @param tenantId Tenant ID
     * @param eventId Event ID
     * @param promotionId Promotion email template ID
     * @param originalFilename Original filename from upload
     * @return S3 path string
     */
    String generatePromotionalEmailHeaderImagePath(String tenantId, Long eventId, Long promotionId, String originalFilename);

    /**
     * Generate S3 path for promotional email footer images.
     * Path format: {profile}/events/tenantId/{tenantId}/event-id/{eventId}/promotional_email_templates/promotion-id/{promotionId}/email_footer_image_{timestamp}_{uuid}.{ext}
     *
     * @param tenantId Tenant ID
     * @param eventId Event ID
     * @param promotionId Promotion email template ID
     * @param originalFilename Original filename from upload
     * @return S3 path string
     */
    String generatePromotionalEmailFooterImagePath(String tenantId, Long eventId, Long promotionId, String originalFilename);

    /**
     * Generate S3 path for tenant email footer HTML.
     * Path format: {profile}/media/tenantId/{tenantId}/email_templates/email_footer.html
     *
     * @param tenantId Tenant ID
     * @return S3 path string
     */
    String generateTenantEmailFooterHtmlPath(String tenantId);

    /**
     * Generate S3 path for tenant logo image.
     * Path format: {profile}/media/tenantId/{tenantId}/tenant_logo/{filename}_{timestamp}_{uuid}.{ext}
     *
     * @param tenantId Tenant ID
     * @param originalFilename Original filename from upload
     * @return S3 path string
     */
    String generateTenantLogoImagePath(String tenantId, String originalFilename);

    /**
     * Generate S3 path for tenant email header image.
     * Path format: {profile}/media/tenantId/{tenantId}/email_header_image/{filename}_{timestamp}_{uuid}.{ext}
     *
     * @param tenantId Tenant ID
     * @param originalFilename Original filename from upload
     * @return S3 path string
     */
    String generateTenantEmailHeaderImagePath(String tenantId, String originalFilename);

    /**
     * Generate S3 path for focus group cover images.
     * Path format: {profile}/media/tenantId/{tenantId}/focus-groups/focus-group-id/{focusGroupId}/cover_image_{timestamp}_{uuid}.{ext}
     *
     * @param tenantId Tenant ID
     * @param focusGroupId Focus Group ID
     * @param originalFilename Original filename from upload
     * @return S3 path string
     */
    String generateFocusGroupCoverImagePath(String tenantId, Long focusGroupId, String originalFilename);

    /**
     * Generate S3 path for gallery album cover images.
     * Path format: {profile}/media/tenantId/{tenantId}/gallery-album/album-id/{albumId}/cover_{timestamp}_{uuid}.{ext}
     *
     * @param tenantId Tenant ID
     * @param albumId Gallery album ID
     * @param originalFilename Original filename from upload
     * @return S3 path string
     */
    String generateGalleryAlbumCoverPath(String tenantId, Long albumId, String originalFilename);

    /**
     * Generate S3 path for event attendee registration attachments.
     * Path format:
     * {profile}/events/tenantId/{tenantId}/event-id/{eventId}/attendees/attendee-id/{attendeeId}/attachments/{base}_{timestamp}_{uuid}.{ext}
     */
    String generateEventAttendeeAttachmentPath(String tenantId, Long eventId, Long attendeeId, String originalFilename);

    /**
     * Generate S3 path for tenant-level official documents.
     *
     * Path format:
     * {profile}/media/tenantId/{tenantId}/official_document/{categorySlug}/{officialDocumentYear}/{sanitizedFileName}_{timestamp}_{uuid}{ext}
     */
    String generateTenantOfficialDocumentPath(String tenantId, String categorySlug, Integer officialDocumentYear, String originalFilename);

    /**
     * Upload a tenant-level official document with tenant/category/year user metadata.
     */
    String uploadTenantOfficialDocumentFile(
        MultipartFile file,
        String tenantId,
        String categorySlug,
        Integer officialDocumentYear,
        String title
    );

    /**
     * S3 path for official-document thumbnail images under the category/year folder.
     */
    String generateTenantOfficialDocumentThumbnailPath(
        String tenantId,
        String categorySlug,
        Integer officialDocumentYear,
        String originalFilename
    );

    /**
     * Upload a thumbnail image for a tenant official document.
     */
    String uploadTenantOfficialDocumentThumbnailFile(
        MultipartFile file,
        String tenantId,
        String categorySlug,
        Integer officialDocumentYear,
        String label
    );
}
