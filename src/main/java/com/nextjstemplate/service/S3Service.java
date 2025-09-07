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
}
