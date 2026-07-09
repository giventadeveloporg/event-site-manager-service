package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.ProfileAudienceContact;
import com.eventsitemanager.domain.PublicProfile;
import com.eventsitemanager.domain.enumeration.ProfileAudienceContactOptInStatus;
import com.eventsitemanager.domain.enumeration.ProfileAudienceContactSource;
import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.ProfileAudienceContactRepository;
import com.eventsitemanager.repository.PublicProfileRepository;
import com.eventsitemanager.service.EmailSubscriptionTokenService;
import com.eventsitemanager.service.ProfileAudienceContactService;
import com.eventsitemanager.service.dto.ProfileAudienceBulkImportResultDTO;
import com.eventsitemanager.service.dto.ProfileAudienceContactDTO;
import com.eventsitemanager.service.dto.ProfileAudienceSubscribeRequestDTO;
import com.eventsitemanager.service.mapper.ProfileAudienceContactMapper;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileAudienceContactServiceImpl implements ProfileAudienceContactService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileAudienceContactServiceImpl.class);
    private static final String ENTITY_NAME = "profileAudienceContact";
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private final ProfileAudienceContactRepository profileAudienceContactRepository;
    private final PublicProfileRepository publicProfileRepository;
    private final ProfileAudienceContactMapper profileAudienceContactMapper;
    private final EmailSubscriptionTokenService emailSubscriptionTokenService;

    public ProfileAudienceContactServiceImpl(
        ProfileAudienceContactRepository profileAudienceContactRepository,
        PublicProfileRepository publicProfileRepository,
        ProfileAudienceContactMapper profileAudienceContactMapper,
        EmailSubscriptionTokenService emailSubscriptionTokenService
    ) {
        this.profileAudienceContactRepository = profileAudienceContactRepository;
        this.publicProfileRepository = publicProfileRepository;
        this.profileAudienceContactMapper = profileAudienceContactMapper;
        this.emailSubscriptionTokenService = emailSubscriptionTokenService;
    }

    @Override
    public ProfileAudienceContactDTO save(ProfileAudienceContactDTO dto) {
        LOG.debug("Request to save ProfileAudienceContact : {}", dto);
        ProfileAudienceContact entity = profileAudienceContactMapper.toEntity(dto);
        if (entity.getId() != null) {
            entity.setId(null);
        }
        prepareForSave(entity, dto.getSource() != null ? dto.getSource() : ProfileAudienceContactSource.ADMIN_MANUAL);
        entity = profileAudienceContactRepository.save(entity);
        return profileAudienceContactMapper.toDto(entity);
    }

    @Override
    public ProfileAudienceContactDTO update(ProfileAudienceContactDTO dto) {
        LOG.debug("Request to update ProfileAudienceContact : {}", dto);
        ProfileAudienceContact entity = profileAudienceContactMapper.toEntity(dto);
        entity.setUpdatedAt(ZonedDateTime.now());
        entity = profileAudienceContactRepository.save(entity);
        return profileAudienceContactMapper.toDto(entity);
    }

    @Override
    public Optional<ProfileAudienceContactDTO> partialUpdate(ProfileAudienceContactDTO dto) {
        LOG.debug("Request to partially update ProfileAudienceContact : {}", dto);
        return profileAudienceContactRepository
            .findById(dto.getId())
            .map(existing -> {
                profileAudienceContactMapper.partialUpdate(existing, dto);
                existing.setUpdatedAt(ZonedDateTime.now());
                return existing;
            })
            .map(profileAudienceContactRepository::save)
            .map(profileAudienceContactMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProfileAudienceContactDTO> findOne(Long id) {
        return profileAudienceContactRepository.findById(id).map(profileAudienceContactMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        profileAudienceContactRepository.deleteById(id);
    }

    @Override
    public ProfileAudienceBulkImportResultDTO bulkImport(String tenantId, List<ProfileAudienceContactDTO> contacts) {
        ProfileAudienceBulkImportResultDTO result = new ProfileAudienceBulkImportResultDTO();
        PublicProfile profile = resolvePublicProfile(tenantId);
        ZonedDateTime now = ZonedDateTime.now();

        for (ProfileAudienceContactDTO row : contacts) {
            if (row == null || row.getEmail() == null || row.getEmail().isBlank()) {
                result.setErrorCount(result.getErrorCount() + 1);
                continue;
            }
            String email = row.getEmail().trim().toLowerCase();
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                result.setErrorCount(result.getErrorCount() + 1);
                continue;
            }

            Optional<ProfileAudienceContact> existing = profileAudienceContactRepository.findByTenantIdAndEmail(tenantId, email);
            if (existing.isPresent()) {
                ProfileAudienceContact contact = existing.orElseThrow();
                if (row.getFirstName() != null) contact.setFirstName(row.getFirstName());
                if (row.getLastName() != null) contact.setLastName(row.getLastName());
                if (row.getNotes() != null) contact.setNotes(row.getNotes());
                contact.setSource(ProfileAudienceContactSource.CSV_IMPORT);
                contact.setUpdatedAt(now);
                profileAudienceContactRepository.save(contact);
                result.setUpdatedCount(result.getUpdatedCount() + 1);
            } else {
                ProfileAudienceContact contact = new ProfileAudienceContact();
                contact.setTenantId(tenantId);
                contact.setPublicProfileId(profile.getId());
                contact.setEmail(email);
                contact.setFirstName(row.getFirstName());
                contact.setLastName(row.getLastName());
                contact.setNotes(row.getNotes());
                contact.setSource(ProfileAudienceContactSource.CSV_IMPORT);
                contact.setOptInStatus(ProfileAudienceContactOptInStatus.OPTED_IN);
                contact.setUnsubscribeToken(emailSubscriptionTokenService.generateEmailSubscriptionToken(email, tenantId));
                contact.setCreatedAt(now);
                contact.setUpdatedAt(now);
                profileAudienceContactRepository.save(contact);
                result.setCreatedCount(result.getCreatedCount() + 1);
            }
        }
        return result;
    }

    @Override
    public ProfileAudienceContactDTO publicSubscribe(
        String tenantId,
        ProfileAudienceSubscribeRequestDTO request,
        ProfileAudienceContactSource source
    ) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new BadRequestAlertException("Email is required", ENTITY_NAME, "emailrequired");
        }
        String email = request.getEmail().trim().toLowerCase();
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new BadRequestAlertException("Invalid email address", ENTITY_NAME, "emailinvalid");
        }

        PublicProfile profile = resolvePublicProfile(tenantId);
        ZonedDateTime now = ZonedDateTime.now();
        String notes = request.getMessage();

        Optional<ProfileAudienceContact> existing = profileAudienceContactRepository.findByTenantIdAndEmail(tenantId, email);
        if (existing.isPresent()) {
            ProfileAudienceContact contact = existing.orElseThrow();
            if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
                contact.setFirstName(request.getFirstName());
            }
            if (request.getLastName() != null && !request.getLastName().isBlank()) {
                contact.setLastName(request.getLastName());
            }
            if (notes != null && !notes.isBlank()) {
                contact.setNotes(notes);
            }
            contact.setSource(source);
            contact.setOptInStatus(ProfileAudienceContactOptInStatus.OPTED_IN);
            contact.setUpdatedAt(now);
            if (contact.getUnsubscribeToken() == null || contact.getUnsubscribeToken().isBlank()) {
                contact.setUnsubscribeToken(emailSubscriptionTokenService.generateEmailSubscriptionToken(email, tenantId));
            }
            return profileAudienceContactMapper.toDto(profileAudienceContactRepository.save(contact));
        }

        ProfileAudienceContact contact = new ProfileAudienceContact();
        contact.setTenantId(tenantId);
        contact.setPublicProfileId(profile.getId());
        contact.setEmail(email);
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setNotes(notes);
        contact.setSource(source);
        contact.setOptInStatus(ProfileAudienceContactOptInStatus.OPTED_IN);
        contact.setUnsubscribeToken(emailSubscriptionTokenService.generateEmailSubscriptionToken(email, tenantId));
        contact.setCreatedAt(now);
        contact.setUpdatedAt(now);
        return profileAudienceContactMapper.toDto(profileAudienceContactRepository.save(contact));
    }

    @Override
    public Map<String, Object> unsubscribe(String tenantId, String email, String token) {
        Map<String, Object> response = new HashMap<>();
        if (email == null || token == null) {
            response.put("success", false);
            response.put("message", "Email and token are required.");
            return response;
        }

        Optional<ProfileAudienceContact> contactOpt = profileAudienceContactRepository.findByTenantIdAndEmailAndUnsubscribeToken(
            tenantId,
            email.trim().toLowerCase(),
            token
        );

        if (contactOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Invalid unsubscribe link.");
            return response;
        }

        ProfileAudienceContact contact = contactOpt.orElseThrow();
        if (contact.getOptInStatus() == ProfileAudienceContactOptInStatus.OPTED_OUT) {
            response.put("success", true);
            response.put("message", "You have already unsubscribed.");
            return response;
        }

        contact.setOptInStatus(ProfileAudienceContactOptInStatus.OPTED_OUT);
        contact.setUpdatedAt(ZonedDateTime.now());
        profileAudienceContactRepository.save(contact);
        response.put("success", true);
        response.put("message", "You have been unsubscribed from profile updates.");
        return response;
    }

    private PublicProfile resolvePublicProfile(String tenantId) {
        return publicProfileRepository
            .findFirstByTenantId(tenantId)
            .orElseThrow(() -> new BadRequestAlertException("Public profile not found for tenant", ENTITY_NAME, "profilenotfound"));
    }

    private void prepareForSave(ProfileAudienceContact entity, ProfileAudienceContactSource source) {
        ZonedDateTime now = ZonedDateTime.now();
        if (entity.getTenantId() == null || entity.getTenantId().isBlank()) {
            throw new BadRequestAlertException("tenantId is required", ENTITY_NAME, "tenantidrequired");
        }
        if (entity.getEmail() != null) {
            entity.setEmail(entity.getEmail().trim().toLowerCase());
        }
        if (entity.getPublicProfileId() == null) {
            entity.setPublicProfileId(resolvePublicProfile(entity.getTenantId()).getId());
        }
        if (entity.getSource() == null) {
            entity.setSource(source);
        }
        if (entity.getOptInStatus() == null) {
            entity.setOptInStatus(ProfileAudienceContactOptInStatus.OPTED_IN);
        }
        if (entity.getUnsubscribeToken() == null || entity.getUnsubscribeToken().isBlank()) {
            entity.setUnsubscribeToken(
                emailSubscriptionTokenService.generateEmailSubscriptionToken(entity.getEmail(), entity.getTenantId())
            );
        }
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(now);
        }
        entity.setUpdatedAt(now);
    }
}
