package com.nextjstemplate.service.impl;

import com.amazonaws.HttpMethod;
import com.nextjstemplate.service.S3Service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

/**
 * Service Implementation for managing S3 operations.
 */
@Service
public class S3ServiceImpl implements S3Service {

    private final Logger log = LoggerFactory.getLogger(S3ServiceImpl.class);

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3ServiceImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public String uploadFile(MultipartFile file, Long eventId, String title, String tenantId) {
        try {
            String originalFilename = file.getOriginalFilename();
            String uniqueFilename = generateUniqueFilename(tenantId, eventId, originalFilename);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            metadata.addUserMetadata("title", title);
            metadata.addUserMetadata("event-id", String.valueOf(eventId));
            metadata.addUserMetadata("original-filename", originalFilename);

            PutObjectRequest putRequest = new PutObjectRequest(
                    bucketName,
                    uniqueFilename,
                    file.getInputStream(),
                    metadata);

            amazonS3.putObject(putRequest);

            URL url = amazonS3.getUrl(bucketName, uniqueFilename);
            return url.toString();

        } catch (Exception e) {
            log.error("Error uploading file to S3", e);
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    @Override
    public String generatePresignedUrl(String fileUrl, int expirationHours) {
        try {
            String fileName = extractFileNameFromUrl(fileUrl);
            Date expiration = new Date();
            expiration.setTime(expiration.getTime() + (expirationHours * 60 * 60 * 1000L));

            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName,
                    fileName)
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
            S3Object s3Object = amazonS3.getObject(bucketName, fileName);
            try (java.io.InputStream inputStream = s3Object.getObjectContent();
                    java.util.Scanner scanner = new java.util.Scanner(inputStream,
                            java.nio.charset.StandardCharsets.UTF_8)) {
                scanner.useDelimiter("\\A");
                return scanner.hasNext() ? scanner.next() : "";
            }
        } catch (Exception e) {
            log.error("Error downloading HTML from S3: {}", url, e);
            throw new RuntimeException("Failed to download HTML from S3", e);
        }
    }

    // Private helper methods

    private String generateUniqueFilename(String tenantId, Long eventId, String originalFilename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(originalFilename);
        String baseName = getBaseFileName(originalFilename);

        if (eventId != null) {
            return String.format("events/tenantId/%s/event-id/%d/%s_%s_%s%s", tenantId, eventId, baseName, timestamp,
                    uuid, extension);
        } else {
            return String.format("media/%s_%s_%s%s", baseName, timestamp, uuid, extension);
        }
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
}
