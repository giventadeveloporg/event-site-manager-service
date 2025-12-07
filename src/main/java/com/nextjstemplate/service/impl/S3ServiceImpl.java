package com.nextjstemplate.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.nextjstemplate.service.S3Service;
import java.net.URL;
import java.util.Date;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing S3 operations.
 */
@Service
public class S3ServiceImpl implements S3Service {

    private final Logger log = LoggerFactory.getLogger(S3ServiceImpl.class);

    private final AmazonS3 amazonS3;

    private final Environment environment;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3ServiceImpl(AmazonS3 amazonS3, Environment environment) {
        this.amazonS3 = amazonS3;
        this.environment = environment;
    }

    @Override
    public String uploadFile(MultipartFile file, Long eventId, String title, String tenantId, Boolean isTeamMemberProfileImage) {
        try {
            String originalFilename = file.getOriginalFilename();
            String uniqueFilename = generateUniqueFilename(tenantId, eventId, originalFilename, isTeamMemberProfileImage);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            metadata.addUserMetadata("title", title);
            metadata.addUserMetadata("event-id", String.valueOf(eventId));
            metadata.addUserMetadata("original-filename", originalFilename);

            PutObjectRequest putRequest = new PutObjectRequest(bucketName, uniqueFilename, file.getInputStream(), metadata);

            amazonS3.putObject(putRequest);

            URL url = amazonS3.getUrl(bucketName, uniqueFilename);
            return url.toString();
        } catch (Exception e) {
            log.error("Error uploading file to S3", e);
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    @Override
    public String uploadFileWithEntityPath(
        MultipartFile file,
        Long eventId,
        Long entityId,
        String entityType,
        String imageType,
        String title,
        String tenantId
    ) {
        try {
            String originalFilename = file.getOriginalFilename();
            String uniqueFilename = generateEntitySpecificFilename(tenantId, eventId, entityId, entityType, imageType, originalFilename);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            metadata.addUserMetadata("title", title);
            metadata.addUserMetadata("event-id", String.valueOf(eventId));
            metadata.addUserMetadata("entity-id", String.valueOf(entityId));
            metadata.addUserMetadata("entity-type", entityType);
            metadata.addUserMetadata("image-type", imageType);
            metadata.addUserMetadata("original-filename", originalFilename);

            PutObjectRequest putRequest = new PutObjectRequest(bucketName, uniqueFilename, file.getInputStream(), metadata);

            amazonS3.putObject(putRequest);

            URL url = amazonS3.getUrl(bucketName, uniqueFilename);
            return url.toString();
        } catch (Exception e) {
            log.error("Error uploading file to S3 with entity path", e);
            throw new RuntimeException("Failed to upload file to S3 with entity path", e);
        }
    }

    @Override
    public String generatePresignedUrl(String fileUrl, int expirationHours) {
        try {
            String fileName = extractFileNameFromUrl(fileUrl);
            Date expiration = new Date();
            expiration.setTime(expiration.getTime() + (expirationHours * 60 * 60 * 1000L));

            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);

            URL presignedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
            return presignedUrl.toString();
        } catch (Exception e) {
            log.error("Error generating presigned URL", e);
            throw new RuntimeException("Failed to generate presigned URL", e);
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        try {
            String fileName = extractFileNameFromUrl(fileUrl);
            amazonS3.deleteObject(bucketName, fileName);
            log.info("Successfully deleted file from S3: {}", fileName);
        } catch (Exception e) {
            log.error("Error deleting file from S3: {}", fileUrl, e);
            throw new RuntimeException("Failed to delete file from S3", e);
        }
    }

    @Override
    public ObjectMetadata getFileMetadata(String fileUrl) {
        try {
            String fileName = extractFileNameFromUrl(fileUrl);
            return amazonS3.getObjectMetadata(bucketName, fileName);
        } catch (Exception e) {
            log.error("Error getting file metadata from S3: {}", fileUrl, e);
            throw new RuntimeException("Failed to get file metadata from S3", e);
        }
    }

    @Override
    public boolean fileExists(String fileUrl) {
        try {
            String fileName = extractFileNameFromUrl(fileUrl);
            return amazonS3.doesObjectExist(bucketName, fileName);
        } catch (Exception e) {
            log.error("Error checking file existence in S3: {}", fileUrl, e);
            return false;
        }
    }

    @Override
    public String downloadHtmlFromUrl(String url) {
        try {
            String fileName = extractFileNameFromUrl(url);
            // Check if file exists before attempting to download
            if (!amazonS3.doesObjectExist(bucketName, fileName)) {
                log.debug("HTML file not found in S3: {}, returning empty string", url);
                return "";
            }
            S3Object s3Object = amazonS3.getObject(bucketName, fileName);
            try (
                java.io.InputStream inputStream = s3Object.getObjectContent();
                java.util.Scanner scanner = new java.util.Scanner(inputStream, java.nio.charset.StandardCharsets.UTF_8)
            ) {
                scanner.useDelimiter("\\A");
                return scanner.hasNext() ? scanner.next() : "";
            }
        } catch (com.amazonaws.services.s3.model.AmazonS3Exception s3Exception) {
            // Handle S3-specific exceptions (e.g., NoSuchKey) gracefully
            if (s3Exception.getStatusCode() == 404) {
                log.debug("HTML file not found in S3 (404): {}, returning empty string", url);
                return "";
            }
            log.warn("S3 error downloading HTML from S3: {}, returning empty string", url);
            return "";
        } catch (Exception e) {
            log.warn("Error downloading HTML from S3: {}, returning empty string", url);
            return "";
        }
    }

    // Private helper methods

    /**
     * Get the active Spring profile prefix for S3 paths.
     * Returns the first active profile, "dev" for development, or "prod" for production.
     * Defaults to "dev" if no profile is set (for local development).
     *
     * @return the active profile prefix
     */
    private String getActiveProfilePrefix() {
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length > 0) {
            String profile = activeProfiles[0];
            // Map common profile names to S3 path prefixes
            if ("prod".equalsIgnoreCase(profile) || "production".equalsIgnoreCase(profile)) {
                return "prod";
            }
            // Default to "dev" for dev, local, or any other profile
            return "dev";
        }
        // Default to "dev" for local development when no profile is set
        return "dev";
    }

    private String generateUniqueFilename(String tenantId, Long eventId, String originalFilename, Boolean isTeamMemberProfileImage) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(originalFilename);
        String profilePrefix = getActiveProfilePrefix();

        if (eventId != null && eventId > 0) {
            // For general event media uploads (from /admin/events/[id]/media page), use "event_media" prefix
            // instead of original filename to avoid exposing user's file naming conventions
            String baseName = "event_media";
            return String.format(
                "%s/events/tenantId/%s/event-id/%d/%s_%s_%s%s",
                profilePrefix,
                tenantId,
                eventId,
                baseName,
                timestamp,
                uuid,
                extension
            );
        } else {
            String baseName = getBaseFileName(originalFilename);
            if (isTeamMemberProfileImage != null && isTeamMemberProfileImage) {
                return String.format(
                    "%s/media/tenantId/%s/executive-team-members/%s_%s_%s%s",
                    profilePrefix,
                    tenantId,
                    baseName,
                    timestamp,
                    uuid,
                    extension
                );
            }
            return String.format("%s/media/%s_%s_%s%s", profilePrefix, baseName, timestamp, uuid, extension);
        }
    }

    private String generateEntitySpecificFilename(
        String tenantId,
        Long eventId,
        Long entityId,
        String entityType,
        String imageType,
        String originalFilename
    ) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(originalFilename);
        String profilePrefix = getActiveProfilePrefix();

        // Determine base name based on entity type and image type
        String baseName;
        if (entityType != null && entityType.equalsIgnoreCase("contact") && imageType != null && imageType.equalsIgnoreCase("photo")) {
            // For contact photos (email header images), use "event_media" prefix instead of original filename
            baseName = "event_media";
        } else {
            // For other entity types, use sanitized original filename
            baseName = getBaseFileName(originalFilename);
        }

        // Build the path structure with profile prefix:
        // {profile}/events/tenantId/{tenantId}/event-id/{eventId}/{entityType}/{entityId}/{imageType}/{filename}
        return String.format(
            "%s/events/tenantId/%s/event-id/%d/%s/%d/%s/%s_%s_%s%s",
            profilePrefix,
            tenantId,
            eventId,
            entityType,
            entityId,
            imageType,
            baseName,
            timestamp,
            uuid,
            extension
        );
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    private String getBaseFileName(String filename) {
        if (filename == null) {
            return "file";
        }

        String baseName = filename.contains(".") ? filename.substring(0, filename.lastIndexOf(".")) : filename;

        // Clean filename for S3 (remove special characters)
        return baseName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private String extractFileNameFromUrl(String url) {
        try {
            URL s3Url = new URL(url);
            String path = s3Url.getPath();
            return path.startsWith("/") ? path.substring(1) : path;
        } catch (Exception e) {
            log.error("Error extracting filename from URL: {}", url, e);
            throw new RuntimeException("Invalid file URL");
        }
    }

    @Override
    public String uploadFile(String s3Path, MultipartFile file) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            metadata.addUserMetadata("original-filename", file.getOriginalFilename());

            PutObjectRequest putRequest = new PutObjectRequest(bucketName, s3Path, file.getInputStream(), metadata);
            amazonS3.putObject(putRequest);

            URL url = amazonS3.getUrl(bucketName, s3Path);
            return url.toString();
        } catch (Exception e) {
            log.error("Error uploading file to S3 with specific path", e);
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    @Override
    public String generateSponsorImagePath(String tenantId, Long sponsorId, String originalFilename, String imageType) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(originalFilename);
        String profilePrefix = getActiveProfilePrefix();

        // Use imageType if provided (logo, hero, banner), otherwise use sanitized original filename
        String baseName;
        if (imageType != null && !imageType.isEmpty()) {
            // Normalize imageType: convert LOGO_IMAGE -> logo, HERO_IMAGE -> hero, BANNER_IMAGE -> banner
            String normalizedImageType = imageType.toUpperCase();
            String imageTypeBase;
            if (normalizedImageType.equals("LOGO_IMAGE")) {
                imageTypeBase = "logo";
            } else if (normalizedImageType.equals("HERO_IMAGE")) {
                imageTypeBase = "hero";
            } else if (normalizedImageType.equals("BANNER_IMAGE")) {
                imageTypeBase = "banner";
            } else {
                // Already normalized (logo, hero, banner) or other value
                imageTypeBase = sanitizeFilename(imageType.toLowerCase());
            }
            // Prefix with "sponsor_" for clarity: sponsor_logo, sponsor_banner, sponsor_hero
            baseName = "sponsor_" + imageTypeBase;
        } else {
            // Fallback to original filename base
            baseName = sanitizeFilename(getBaseFileName(originalFilename));
        }

        return String.format(
            "%s/media/tenantId/%s/sponsor/sponsor_id/%d/%s_%s_%s%s",
            profilePrefix,
            tenantId,
            sponsorId,
            baseName,
            timestamp,
            uuid,
            extension
        );
    }

    @Override
    public String generateSponsorImagePath(String tenantId, Long sponsorId, String originalFilename) {
        return generateSponsorImagePath(tenantId, sponsorId, originalFilename, null);
    }

    @Override
    public String generatePerformerImagePath(String tenantId, Long performerId, String originalFilename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(originalFilename);
        String baseName = sanitizeFilename(getBaseFileName(originalFilename));
        String profilePrefix = getActiveProfilePrefix();

        return String.format(
            "%s/media/tenantId/%s/performer/performer_id/%d/%s_%s_%s%s",
            profilePrefix,
            tenantId,
            performerId,
            baseName,
            timestamp,
            uuid,
            extension
        );
    }

    @Override
    public String generateDirectorImagePath(String tenantId, Long directorId, String originalFilename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(originalFilename);
        String baseName = sanitizeFilename(getBaseFileName(originalFilename));
        String profilePrefix = getActiveProfilePrefix();

        return String.format(
            "%s/media/tenantId/%s/director/director_id/%d/%s_%s_%s%s",
            profilePrefix,
            tenantId,
            directorId,
            baseName,
            timestamp,
            uuid,
            extension
        );
    }

    @Override
    public String generateEventSponsorJoinImagePath(String tenantId, Long eventId, Long sponsorId, String originalFilename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(originalFilename);
        // Use "sponsor_poster" as the base name instead of the original filename
        String baseName = "sponsor_poster";
        String profilePrefix = getActiveProfilePrefix();

        return String.format(
            "%s/events/tenantId/%s/event-id/%d/sponsors/sponsor_id/%d/%s_%s_%s%s",
            profilePrefix,
            tenantId,
            eventId,
            sponsorId,
            baseName,
            timestamp,
            uuid,
            extension
        );
    }

    @Override
    public String generateEventDirectorPosterPath(String tenantId, Long eventId, Long directorId, String originalFilename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(originalFilename);
        // Use "director_poster" as the base name instead of the original filename
        String baseName = "director_poster";
        String profilePrefix = getActiveProfilePrefix();

        return String.format(
            "%s/events/tenantId/%s/event-id/%d/program-directors/director_id/%d/%s_%s_%s%s",
            profilePrefix,
            tenantId,
            eventId,
            directorId,
            baseName,
            timestamp,
            uuid,
            extension
        );
    }

    @Override
    public String generateEventPerformerPosterPath(String tenantId, Long eventId, Long performerId, String originalFilename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(originalFilename);
        // Use "performer_poster" as the base name instead of the original filename
        String baseName = "performer_poster";
        String profilePrefix = getActiveProfilePrefix();

        return String.format(
            "%s/events/tenantId/%s/event-id/%d/performers/performer_id/%d/%s_%s_%s%s",
            profilePrefix,
            tenantId,
            eventId,
            performerId,
            baseName,
            timestamp,
            uuid,
            extension
        );
    }

    @Override
    public String generateEmailHeaderImagePath(String tenantId, Long eventId, String originalFilename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(originalFilename);
        // Default to .jpeg if no extension found
        if (extension == null || extension.isEmpty()) {
            extension = ".jpeg";
        }
        String profilePrefix = getActiveProfilePrefix();

        return String.format(
            "%s/events/tenantId/%s/event-id/%d/tickets/email-templates/email_header_image_%s_%s%s",
            profilePrefix,
            tenantId,
            eventId,
            timestamp,
            uuid,
            extension
        );
    }

    @Override
    public String generatePromotionalEmailHeaderImagePath(String tenantId, Long eventId, Long promotionId, String originalFilename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(originalFilename);
        // Default to .jpeg if no extension found
        if (extension == null || extension.isEmpty()) {
            extension = ".jpeg";
        }
        String profilePrefix = getActiveProfilePrefix();

        return String.format(
            "%s/events/tenantId/%s/event-id/%d/promotional_email_templates/promotion-id/%d/email_header_image_%s_%s%s",
            profilePrefix,
            tenantId,
            eventId,
            promotionId,
            timestamp,
            uuid,
            extension
        );
    }

    @Override
    public String generatePromotionalEmailFooterImagePath(String tenantId, Long eventId, Long promotionId, String originalFilename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(originalFilename);
        // Default to .jpeg if no extension found
        if (extension == null || extension.isEmpty()) {
            extension = ".jpeg";
        }
        String profilePrefix = getActiveProfilePrefix();

        return String.format(
            "%s/events/tenantId/%s/event-id/%d/promotional_email_templates/promotion-id/%d/email_footer_image_%s_%s%s",
            profilePrefix,
            tenantId,
            eventId,
            promotionId,
            timestamp,
            uuid,
            extension
        );
    }

    /**
     * Sanitize filename - remove special characters, keep alphanumeric, dots, hyphens, underscores
     */
    private String sanitizeFilename(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "file";
        }
        // Remove special characters, keep alphanumeric, dots, hyphens, underscores
        return filename.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    @Override
    public String generateTenantEmailFooterHtmlPath(String tenantId) {
        String profilePrefix = getActiveProfilePrefix();
        return String.format("%s/media/tenantId/%s/email_templates/email_footer.html", profilePrefix, tenantId);
    }

    @Override
    public String generateTenantLogoImagePath(String tenantId, String originalFilename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(originalFilename);
        // Default to .png if no extension found
        if (extension == null || extension.isEmpty()) {
            extension = ".png";
        }
        String profilePrefix = getActiveProfilePrefix();
        String baseName = sanitizeFilename(getBaseFileName(originalFilename));

        return String.format("%s/media/tenantId/%s/tenant_logo/%s_%s_%s%s", profilePrefix, tenantId, baseName, timestamp, uuid, extension);
    }

    @Override
    public String generateTenantEmailHeaderImagePath(String tenantId, String originalFilename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(originalFilename);
        // Default to .png if no extension found
        if (extension == null || extension.isEmpty()) {
            extension = ".png";
        }
        String profilePrefix = getActiveProfilePrefix();
        String baseName = sanitizeFilename(getBaseFileName(originalFilename));

        return String.format(
            "%s/media/tenantId/%s/email_header_image/%s_%s_%s%s",
            profilePrefix,
            tenantId,
            baseName,
            timestamp,
            uuid,
            extension
        );
    }

    @Override
    public String generateFocusGroupCoverImagePath(String tenantId, Long focusGroupId, String originalFilename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(originalFilename);
        // Default to .jpeg if no extension found
        if (extension == null || extension.isEmpty()) {
            extension = ".jpeg";
        }
        String profilePrefix = getActiveProfilePrefix();

        return String.format(
            "%s/media/tenantId/%s/focus-groups/focus-group-id/%d/cover_image_%s_%s%s",
            profilePrefix,
            tenantId,
            focusGroupId,
            timestamp,
            uuid,
            extension
        );
    }
}
