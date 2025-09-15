# Enhanced Upload System Documentation

## Overview

The upload system has been enhanced to support dynamic path construction for new entity types while maintaining backward compatibility with existing functionality. The system now supports entity-specific uploads with organized folder structures in AWS S3.

## Key Features

### 1. Dynamic Path Construction
The system now creates organized folder structures based on entity type and image type:

```
events/tenantId/{tenantId}/event-id/{eventId}/{entityType}/{entityId}/{imageType}/{filename}
```

### 2. Entity-Specific Upload Support
- **EventFeaturedPerformers**: portrait, performance, gallery images
- **EventSponsors**: logo, hero, banner images
- **EventContacts**: photo images
- **EventProgramDirectors**: photo images

### 3. Backward Compatibility
All existing upload functionality remains intact and unchanged.

## API Endpoints

### Enhanced General Upload Endpoint
```
POST /api/event-medias/upload
```

**New Parameters Added:**
- `isFeaturedPerformerPortrait` (Boolean)
- `isFeaturedPerformerPerformance` (Boolean)
- `isFeaturedPerformerGallery` (Boolean)
- `isSponsorLogo` (Boolean)
- `isSponsorHero` (Boolean)
- `isSponsorBanner` (Boolean)
- `isContactPhoto` (Boolean)
- `isProgramDirectorPhoto` (Boolean)
- `entityId` (Long)
- `entityType` (String)
- `imageType` (String)

### Dedicated Entity Upload Endpoints

#### Featured Performer Images
```
POST /api/event-medias/upload/featured-performer/{entityId}/{imageType}
```
- **imageType**: `portrait`, `performance`, `gallery`
- **Path**: `events/tenantId/{tenantId}/event-id/{eventId}/featured-performer/{entityId}/{imageType}/`

#### Sponsor Images
```
POST /api/event-medias/upload/sponsor/{entityId}/{imageType}
```
- **imageType**: `logo`, `hero`, `banner`
- **Path**: `events/tenantId/{tenantId}/event-id/{eventId}/sponsor/{entityId}/{imageType}/`

#### Contact Photos
```
POST /api/event-medias/upload/contact/{entityId}/photo
```
- **Path**: `events/tenantId/{tenantId}/event-id/{eventId}/contact/{entityId}/photo/`

#### Program Director Photos
```
POST /api/event-medias/upload/program-director/{entityId}/photo
```
- **Path**: `events/tenantId/{tenantId}/event-id/{eventId}/program-director/{entityId}/photo/`

## Implementation Details

### S3Service Enhancements

#### New Method Added
```java
String uploadFileWithEntityPath(
    MultipartFile file,
    Long eventId,
    Long entityId,
    String entityType,
    String imageType,
    String title,
    String tenantId
);
```

#### Path Generation Logic
```java
private String generateEntitySpecificFilename(String tenantId, Long eventId, Long entityId, String entityType, String imageType, String originalFilename) {
    String timestamp = String.valueOf(System.currentTimeMillis());
    String uuid = UUID.randomUUID().toString().substring(0, 8);
    String extension = getFileExtension(originalFilename);
    String baseName = getBaseFileName(originalFilename);

    return String.format("events/tenantId/%s/event-id/%d/%s/%d/%s/%s_%s_%s%s",
        tenantId, eventId, entityType, entityId, imageType, baseName, timestamp, uuid, extension);
}
```

### EventMediaService Enhancements

#### Updated Method Signature
The `uploadFile` method now accepts additional parameters for entity-specific uploads:

```java
EventMediaDTO uploadFile(
    // ... existing parameters ...
    // New entity-specific parameters
    Boolean isFeaturedPerformerPortrait,
    Boolean isFeaturedPerformerPerformance,
    Boolean isFeaturedPerformerGallery,
    Boolean isSponsorLogo,
    Boolean isSponsorHero,
    Boolean isSponsorBanner,
    Boolean isContactPhoto,
    Boolean isProgramDirectorPhoto,
    Long entityId,
    String entityType,
    String imageType
);
```

#### Smart Upload Logic
The service automatically detects when to use entity-specific upload:

```java
if (entityId != null && entityType != null && imageType != null) {
    // Use entity-specific upload with dynamic path construction
    fileUrl = s3Service.uploadFileWithEntityPath(file, eventId, entityId, entityType, imageType, title, tenantId);
} else {
    // Use existing upload method for backward compatibility
    fileUrl = s3Service.uploadFile(file, eventId, title, tenantId, isTeamMemberProfileImage);
}
```

## Path Examples

### Featured Performer Portrait
```
events/tenantId/tenant_demo_001/event-id/123/featured-performer/456/portrait/portrait_1640995200000_abc12345.jpg
```

### Sponsor Logo
```
events/tenantId/tenant_demo_001/event-id/123/sponsor/789/logo/logo_1640995200000_def67890.png
```

### Contact Photo
```
events/tenantId/tenant_demo_001/event-id/123/contact/101/photo/photo_1640995200000_ghi11111.jpg
```

## Metadata Enhancement

The S3Service now includes additional metadata for entity-specific uploads:

```java
metadata.addUserMetadata("entity-id", String.valueOf(entityId));
metadata.addUserMetadata("entity-type", entityType);
metadata.addUserMetadata("image-type", imageType);
```

## Validation

### Image Type Validation
Each entity type has specific valid image types:

- **featured-performer**: `portrait`, `performance`, `gallery`
- **sponsor**: `logo`, `hero`, `banner`
- **contact**: `photo`
- **program-director**: `photo`

### Helper Method
```java
private boolean isValidImageType(String imageType, String entityType) {
    return switch (entityType) {
        case "featured-performer" -> Set.of("portrait", "performance", "gallery").contains(imageType);
        case "sponsor" -> Set.of("logo", "hero", "banner").contains(imageType);
        case "contact", "program-director" -> "photo".equals(imageType);
        default -> false;
    };
}
```

## Backward Compatibility

### Existing Functionality Preserved
- All existing upload parameters remain functional
- Existing upload paths are unchanged
- Current API contracts are maintained
- No breaking changes to existing clients

### Existing Path Examples
```
events/tenantId/tenant_demo_001/event-id/123/filename_1640995200000_abc12345.jpg
media/tenantId/tenant_demo_001/executive-team-members/filename_1640995200000_def67890.jpg
```

## Usage Examples

### Frontend Integration

#### Featured Performer Portrait Upload
```javascript
const formData = new FormData();
formData.append('file', portraitFile);
formData.append('eventId', '123');
formData.append('title', 'Portrait Image');
formData.append('tenantId', 'tenant_demo_001');
formData.append('isPublic', 'true');

fetch('/api/event-medias/upload/featured-performer/456/portrait', {
    method: 'POST',
    body: formData,
    headers: {
        'Authorization': 'Bearer ' + token
    }
});
```

#### Sponsor Logo Upload
```javascript
const formData = new FormData();
formData.append('file', logoFile);
formData.append('eventId', '123');
formData.append('title', 'Company Logo');
formData.append('tenantId', 'tenant_demo_001');

fetch('/api/event-medias/upload/sponsor/789/logo', {
    method: 'POST',
    body: formData,
    headers: {
        'Authorization': 'Bearer ' + token
    }
});
```

### Backend Integration

#### Using Enhanced General Upload
```java
EventMediaDTO result = eventMediaService.uploadFile(
    file, eventId, userProfileId, title, description, tenantId, isPublic,
    null, null, null, null, null, null, false, false, false, null,
    // Entity-specific parameters
    true, false, false,  // isFeaturedPerformerPortrait = true
    null, null, null,    // sponsor flags
    null, null,          // contact and program director flags
    entityId, "featured-performer", "portrait"
);
```

## Benefits

1. **Organized Storage**: Images are stored in logical, hierarchical folder structures
2. **Easy Management**: Entity-specific images can be easily located and managed
3. **Scalability**: System can easily accommodate new entity types
4. **Backward Compatibility**: Existing functionality remains unchanged
5. **Type Safety**: Validation ensures correct image types for each entity
6. **Metadata Rich**: Enhanced metadata for better file management
7. **Flexible API**: Both general and entity-specific upload endpoints available

## Migration Guide

### For Existing Clients
No changes required. All existing upload functionality continues to work as before.

### For New Entity Uploads
Use the new dedicated endpoints or enhanced general upload endpoint with entity-specific parameters.

### For Frontend Developers
1. Use dedicated endpoints for cleaner, more specific uploads
2. Use enhanced general endpoint for flexibility
3. Implement proper validation for image types
4. Handle entity-specific responses appropriately

## Testing

### Test Cases Covered
- ✅ Existing upload functionality (backward compatibility)
- ✅ Entity-specific upload with dynamic paths
- ✅ Image type validation
- ✅ Metadata inclusion
- ✅ Error handling for invalid image types
- ✅ Multiple entity types support
- ✅ Path generation accuracy

### Test Scenarios
1. Upload featured performer portrait
2. Upload sponsor logo
3. Upload contact photo
4. Upload program director photo
5. Upload with invalid image type (should fail)
6. Upload with existing general endpoint (should work)
7. Verify path structure in S3
8. Verify metadata inclusion

## Future Enhancements

1. **Bulk Upload**: Support for uploading multiple images for the same entity
2. **Image Processing**: Automatic resizing and optimization
3. **CDN Integration**: CloudFront distribution for faster access
4. **Image Gallery**: Support for gallery image management
5. **Image Replacement**: Easy replacement of existing images
6. **Image Analytics**: Usage tracking and analytics
