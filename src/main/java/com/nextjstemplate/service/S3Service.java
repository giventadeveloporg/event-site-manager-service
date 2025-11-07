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
     * Path format: {profile}/media/tenantId/{tenantId}/sponsor/sponsor_id/{sponsorId}/{filename}
     *
     * @param tenantId the tenant ID.
     * @param sponsorId the sponsor ID.
     * @param originalFilename the original filename.
     * @return the S3 path for the sponsor image.
     */
    String generateSponsorImagePath(String tenantId, Long sponsorId, String originalFilename);

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
}
