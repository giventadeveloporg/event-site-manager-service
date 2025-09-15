package com.nextjstemplate.service;

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
import org.springframework.stereotype.Service;

@Service
public class QRCodeService {

    private final AmazonS3 amazonS3;
    private final S3Service s3Service;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Autowired
    public QRCodeService(AmazonS3 amazonS3, S3Service s3Service) {
        this.amazonS3 = amazonS3;
        this.s3Service = s3Service;
    }

    public String generateAndUploadQRCode(String qrScanUrlContent, Long eventId, String transactionId, String tenantId) throws IOException {
        int width = 300;
        int height = 300;
        String fileType = "png";
        String fileName = "qrcode_" + transactionId + ".png";

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrScanUrlContent, BarcodeFormat.QR_CODE, width, height, hints);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            javax.imageio.ImageIO.write(qrImage, fileType, baos);
            baos.flush();
            byte[] imageBytes = baos.toByteArray();
            baos.close();

            String uniqueFilename = generateUniqueFilename(tenantId, eventId, fileName);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/png");
            metadata.setContentLength(imageBytes.length);
            metadata.addUserMetadata("title", transactionId);
            metadata.addUserMetadata("event-id", String.valueOf(eventId));
            metadata.addUserMetadata("original-filename", fileName);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            PutObjectRequest putRequest = new PutObjectRequest(bucketName, uniqueFilename, inputStream, metadata);
            amazonS3.putObject(putRequest);
            URL url = amazonS3.getUrl(bucketName, uniqueFilename);
            return url.toString();
        } catch (WriterException e) {
            throw new IOException("Failed to generate QR code image", e);
        }
    }

    private String generateUniqueFilename(String tenantId, Long eventId, String originalFilename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(originalFilename);
        String baseName = getBaseFileName(originalFilename);

        if (eventId != null) {
            return String.format(
                "events/tenantId/%s/event-id/%d/tickets/%s_%s_%s%s",
                tenantId,
                eventId,
                baseName,
                timestamp,
                uuid,
                extension
            );
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
        return baseName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
