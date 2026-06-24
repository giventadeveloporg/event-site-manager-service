package com.eventsitemanager.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class QRCodeService {

    private final AmazonS3 amazonS3;
    private final S3Service s3Service;
    private final Environment environment;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Autowired
    public QRCodeService(AmazonS3 amazonS3, S3Service s3Service, Environment environment) {
        this.amazonS3 = amazonS3;
        this.s3Service = s3Service;
        this.environment = environment;
    }

    public String generateAndUploadQRCode(String qrScanUrlContent, Long eventId, String transactionId, String tenantId) throws IOException {
        org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(QRCodeService.class);

        log.info(
            "Enter: generateAndUploadQRCode() with argument[s] = [qrScanUrlContent={}, eventId={}, transactionId={}, tenantId={}]",
            qrScanUrlContent,
            eventId,
            transactionId,
            tenantId
        );

        // Log configuration values
        log.info(
            "QRCodeService configuration - bucketName: {}, activeProfiles: {}",
            bucketName,
            java.util.Arrays.toString(environment.getActiveProfiles())
        );

        int width = 300;
        int height = 300;
        String fileType = "png";
        String fileName = "qrcode_" + transactionId + ".png";

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        try {
            log.debug("Generating QR code image for content: {}", qrScanUrlContent);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrScanUrlContent, BarcodeFormat.QR_CODE, width, height, hints);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            javax.imageio.ImageIO.write(qrImage, fileType, baos);
            baos.flush();
            byte[] imageBytes = baos.toByteArray();
            baos.close();

            log.debug("QR code image generated successfully: {} bytes", imageBytes.length);

            String uniqueFilename = generateUniqueFilename(tenantId, eventId, fileName);
            log.info("Generated S3 filename: {}", uniqueFilename);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/png");
            metadata.setContentLength(imageBytes.length);
            metadata.addUserMetadata("title", transactionId);
            metadata.addUserMetadata("event-id", String.valueOf(eventId));
            metadata.addUserMetadata("original-filename", fileName);

            log.debug("Uploading QR code image to S3: bucket={}, key={}", bucketName, uniqueFilename);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            PutObjectRequest putRequest = new PutObjectRequest(bucketName, uniqueFilename, inputStream, metadata);
            amazonS3.putObject(putRequest);

            URL url = amazonS3.getUrl(bucketName, uniqueFilename);
            String s3Url = url.toString();
            log.info("QR code image uploaded successfully to S3: {}", s3Url);
            log.info("Exit: generateAndUploadQRCode() with result = {}", s3Url);
            return s3Url;
        } catch (WriterException e) {
            log.error("Failed to generate QR code image: {}", e.getMessage(), e);
            throw new IOException("Failed to generate QR code image", e);
        } catch (Exception e) {
            log.error("Unexpected error during QR code generation/upload: {}", e.getMessage(), e);
            throw new IOException("Failed to upload QR code image to S3", e);
        }
    }

    /**
     * Get the active Spring profile prefix for S3 paths.
     * Returns the first active profile, "dev" for development, or "prod" for production.
     * Defaults to "dev" if no profile is set (for local development).
     *
     * @return the active profile prefix
     */
    private String getActiveProfilePrefix() {
        org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(QRCodeService.class);
        String[] activeProfiles = environment.getActiveProfiles();
        log.debug("Active Spring profiles: {}", java.util.Arrays.toString(activeProfiles));

        if (activeProfiles.length > 0) {
            String profile = activeProfiles[0];
            // Map common profile names to S3 path prefixes
            if ("prod".equalsIgnoreCase(profile) || "production".equalsIgnoreCase(profile)) {
                log.debug("Using profile prefix: prod");
                return "prod";
            }
            // Default to "dev" for dev, local, or any other profile
            log.debug("Using profile prefix: dev (profile={})", profile);
            return "dev";
        }
        // Default to "dev" for local development when no profile is set
        log.warn("No active Spring profiles found, defaulting to 'dev' prefix");
        return "dev";
    }

    private String generateUniqueFilename(String tenantId, Long eventId, String originalFilename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(originalFilename);
        String baseName = getBaseFileName(originalFilename);
        String profilePrefix = getActiveProfilePrefix();

        if (eventId != null) {
            return String.format(
                "%s/events/tenantId/%s/event-id/%d/tickets/%s_%s_%s%s",
                profilePrefix,
                tenantId,
                eventId,
                baseName,
                timestamp,
                uuid,
                extension
            );
        } else {
            return String.format("%s/media/%s_%s_%s%s", profilePrefix, baseName, timestamp, uuid, extension);
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
        return baseName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
