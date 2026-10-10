package com.eventsitemanager.service.impl;

import static com.eventsitemanager.service.TenantOnboardingSupport.ENTITY_NAME;

import com.eventsitemanager.domain.SatelliteDomain;
import com.eventsitemanager.domain.TenantEmailAddress;
import com.eventsitemanager.domain.TenantOnboardingRequest;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.domain.enumeration.OnboardingRequestStatus;
import com.eventsitemanager.domain.enumeration.TenantEmailType;
import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.SatelliteDomainRepository;
import com.eventsitemanager.repository.TenantEmailAddressRepository;
import com.eventsitemanager.repository.TenantOnboardingRequestRepository;
import com.eventsitemanager.repository.TenantOrganizationRepository;
import com.eventsitemanager.repository.TenantSettingsRepository;
import com.eventsitemanager.repository.UserProfileRepository;
import com.eventsitemanager.service.TenantOnboardingProvisioningService;
import com.eventsitemanager.service.TenantOnboardingSupport;
import com.eventsitemanager.service.TenantOrganizationService;
import com.eventsitemanager.service.TenantSettingsService;
import com.eventsitemanager.service.dto.TenantOnboardingApproveDTO;
import com.eventsitemanager.service.dto.TenantOnboardingRequestDTO;
import com.eventsitemanager.service.dto.TenantOrganizationDTO;
import com.eventsitemanager.service.dto.TenantSettingsDTO;
import com.eventsitemanager.service.mapper.TenantOnboardingRequestMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Provisions a new tenant from an onboarding request. The whole provisioning runs in one transaction
 * ({@link #approveTx}); if anything fails the request is marked FAILED in a separate transaction so the
 * reason survives the rollback.
 */
@Service
public class TenantOnboardingProvisioningServiceImpl implements TenantOnboardingProvisioningService {

    private static final Logger LOG = LoggerFactory.getLogger(TenantOnboardingProvisioningServiceImpl.class);

    private static final Set<OnboardingRequestStatus> APPROVABLE = EnumSet.of(
        OnboardingRequestStatus.PENDING,
        OnboardingRequestStatus.FAILED
    );

    private static final List<String> CLONED_ADMIN_ROLES = List.of("ADMIN", "SUPER_ADMIN");

    /** Spring caches that may hold "not found" / stale lists for a tenant or satellite that now exists. */
    private static final List<String> CACHES_TO_CLEAR = List.of(
        "satelliteDomains",
        "tenantEmailAddresses",
        "tenantSettings",
        "userProfiles",
        "userProfilesByUserId",
        "userProfilesByEmail"
    );

    private final TenantOnboardingRequestRepository requestRepository;
    private final TenantOnboardingRequestMapper requestMapper;
    private final TenantOrganizationRepository tenantOrganizationRepository;
    private final TenantOrganizationService tenantOrganizationService;
    private final TenantSettingsRepository tenantSettingsRepository;
    private final TenantSettingsService tenantSettingsService;
    private final TenantEmailAddressRepository tenantEmailAddressRepository;
    private final SatelliteDomainRepository satelliteDomainRepository;
    private final UserProfileRepository userProfileRepository;
    private final CacheManager cacheManager;
    private final ObjectMapper objectMapper;
    private final TransactionTemplate approveTx;
    private final TransactionTemplate failureTx;

    @Value("${onboarding.admin-source-tenant-id:mosc_malankara_orthodox_2}")
    private String defaultAdminSourceTenantId;

    public TenantOnboardingProvisioningServiceImpl(
        TenantOnboardingRequestRepository requestRepository,
        TenantOnboardingRequestMapper requestMapper,
        TenantOrganizationRepository tenantOrganizationRepository,
        TenantOrganizationService tenantOrganizationService,
        TenantSettingsRepository tenantSettingsRepository,
        TenantSettingsService tenantSettingsService,
        TenantEmailAddressRepository tenantEmailAddressRepository,
        SatelliteDomainRepository satelliteDomainRepository,
        UserProfileRepository userProfileRepository,
        CacheManager cacheManager,
        ObjectMapper objectMapper,
        PlatformTransactionManager transactionManager
    ) {
        this.requestRepository = requestRepository;
        this.requestMapper = requestMapper;
        this.tenantOrganizationRepository = tenantOrganizationRepository;
        this.tenantOrganizationService = tenantOrganizationService;
        this.tenantSettingsRepository = tenantSettingsRepository;
        this.tenantSettingsService = tenantSettingsService;
        this.tenantEmailAddressRepository = tenantEmailAddressRepository;
        this.satelliteDomainRepository = satelliteDomainRepository;
        this.userProfileRepository = userProfileRepository;
        this.cacheManager = cacheManager;
        this.objectMapper = objectMapper;
        this.approveTx = new TransactionTemplate(transactionManager);
        this.failureTx = new TransactionTemplate(transactionManager);
        this.failureTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    @Override
    public TenantOnboardingRequestDTO approve(Long requestId, TenantOnboardingApproveDTO in) {
        TenantOnboardingRequestDTO result;
        try {
            result = approveTx.execute(status -> provision(requestId, in));
        } catch (BadRequestAlertException e) {
            // Validation / conflict detected before any write: request stays as-is so the admin can fix inputs.
            throw e;
        } catch (RuntimeException e) {
            String reason = rootMessage(e);
            LOG.error("[ONBOARDING] Provisioning failed for request id={}: {}", requestId, reason, e);
            markFailed(requestId, in, reason);
            throw new BadRequestAlertException("Provisioning failed and was rolled back: " + reason, ENTITY_NAME, "provisioningFailed");
        }
        clearCaches();
        return result;
    }

    private TenantOnboardingRequestDTO provision(Long requestId, TenantOnboardingApproveDTO in) {
        TenantOnboardingRequest request = requestRepository
            .findByIdForUpdate(requestId)
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
        if (!APPROVABLE.contains(request.getStatus())) {
            throw new BadRequestAlertException(
                "Only PENDING or FAILED requests can be approved (current: " + request.getStatus() + ")",
                ENTITY_NAME,
                "invalidTransition"
            );
        }

        Resolved r = resolve(request, in);
        precheck(r, request.getId());

        ZonedDateTime now = ZonedDateTime.now();
        Map<String, Object> outcome = new LinkedHashMap<>();
        outcome.put("tenantId", r.tenantId);

        TenantOrganizationDTO org = createOrganization(request, r, now);
        tenantOrganizationRepository.flush();
        outcome.put("tenantOrganizationId", org.getId());

        TenantSettingsDTO settings = createSettings(request, r, org, now);
        outcome.put("tenantSettingsId", settings.getId());

        Map<String, Long> emailIds = new LinkedHashMap<>();
        emailIds.put("CONTACT", createEmail(r, TenantEmailType.CONTACT, r.contactEmail, r.displayName + " Contact").getId());
        emailIds.put("INFO", createEmail(r, TenantEmailType.INFO, r.infoEmail, r.displayName + " Info").getId());
        emailIds.put("NOREPLY", createEmail(r, TenantEmailType.NOREPLY, r.noreplyEmail, r.displayName + " No Reply").getId());
        outcome.put("tenantEmailAddressIds", emailIds);

        SatelliteDomain satellite = createSatellite(request, r, now);
        outcome.put("satelliteDomainId", satellite.getId());
        outcome.put("satelliteKey", r.satelliteKey);
        outcome.put("hostname", r.hostname);

        if (r.cloneAdmins) {
            outcome.put("adminSourceTenantId", r.adminSourceTenantId);
            outcome.put("clonedAdmins", cloneAdmins(r, now));
        } else {
            outcome.put("clonedAdmins", List.of());
        }

        request.setStatus(OnboardingRequestStatus.APPROVED);
        request.setAssignedTenantId(r.tenantId);
        request.setSatelliteKey(r.satelliteKey);
        request.setRequestedHostname(r.hostname);
        request.setNoreplyEmail(r.noreplyEmail);
        request.setApprovedAt(now);
        request.setReviewedAt(now);
        applyReviewer(request, in);
        request.setProvisioningResult(toJson(outcome));
        request = requestRepository.saveAndFlush(request);

        LOG.info(
            "[ONBOARDING] Approved request {} (id={}) -> tenant {} / satellite {} ({})",
            request.getRequestCode(),
            request.getId(),
            r.tenantId,
            r.satelliteKey,
            r.hostname
        );
        return requestMapper.toDto(request);
    }

    private Resolved resolve(TenantOnboardingRequest request, TenantOnboardingApproveDTO in) {
        Resolved r = new Resolved();
        r.tenantId = in.getTenantId().trim();
        r.satelliteKey = in.getSatelliteKey().trim();
        String hostSource = TenantOnboardingSupport.trimToNull(in.getHostname()) != null
            ? in.getHostname()
            : request.getRequestedHostname();
        r.hostname = TenantOnboardingSupport.normalizeHostname(hostSource);
        if (!TenantOnboardingSupport.isValidHostname(r.hostname)) {
            throw new BadRequestAlertException("Hostname is not valid: " + hostSource, ENTITY_NAME, "hostnameinvalid");
        }
        String orgDomain = TenantOnboardingSupport.normalizeHostname(in.getOrganizationDomain());
        r.organizationDomain = orgDomain != null ? orgDomain : TenantOnboardingSupport.stripWww(r.hostname);
        String displayName = TenantOnboardingSupport.trimToNull(in.getDisplayName());
        r.displayName = displayName != null ? displayName : request.getOrganizationName();
        String contact = TenantOnboardingSupport.normalizeEmail(in.getContactEmail());
        r.contactEmail = contact != null ? contact : request.getContactEmail();
        String info = TenantOnboardingSupport.normalizeEmail(in.getInfoEmail());
        r.infoEmail = info != null ? info : r.contactEmail;
        String noreply = TenantOnboardingSupport.normalizeEmail(in.getNoreplyEmail());
        if (noreply == null) {
            noreply = TenantOnboardingSupport.normalizeEmail(request.getNoreplyEmail());
        }
        r.noreplyEmail = noreply != null ? noreply : "noreply@" + r.organizationDomain;
        String source = TenantOnboardingSupport.trimToNull(in.getAdminSourceTenantId());
        r.adminSourceTenantId = source != null ? source : defaultAdminSourceTenantId;
        r.cloneAdmins = !Boolean.FALSE.equals(in.getCloneAdmins());
        return r;
    }

    private void precheck(Resolved r, Long requestId) {
        if (tenantOrganizationRepository.existsByTenantId(r.tenantId) || tenantSettingsRepository.findByTenantId(r.tenantId).isPresent()) {
            throw new BadRequestAlertException("Tenant ID already exists: " + r.tenantId, ENTITY_NAME, "tenantidtaken");
        }
        if (requestRepository.existsByAssignedTenantIdAndIdNot(r.tenantId, requestId)) {
            throw new BadRequestAlertException(
                "Tenant ID is already assigned to another onboarding request: " + r.tenantId,
                ENTITY_NAME,
                "tenantidtaken"
            );
        }
        if (tenantOrganizationRepository.existsByDomainIgnoreCase(r.organizationDomain)) {
            throw new BadRequestAlertException(
                "Organization domain already belongs to another tenant: " + r.organizationDomain,
                ENTITY_NAME,
                "domaintaken"
            );
        }
        if (satelliteDomainRepository.findBySatelliteKey(r.satelliteKey).isPresent()) {
            throw new BadRequestAlertException("Satellite key already exists: " + r.satelliteKey, ENTITY_NAME, "satellitekeytaken");
        }
        String bare = TenantOnboardingSupport.stripWww(r.hostname);
        if (
            satelliteDomainRepository.findByHostname(r.hostname).isPresent() ||
            satelliteDomainRepository.findByHostname(bare).isPresent() ||
            satelliteDomainRepository.findByHostname("www." + bare).isPresent()
        ) {
            throw new BadRequestAlertException("Hostname is already registered: " + r.hostname, ENTITY_NAME, "hostnametaken");
        }
    }

    private TenantOrganizationDTO createOrganization(TenantOnboardingRequest request, Resolved r, ZonedDateTime now) {
        TenantOrganizationDTO dto = new TenantOrganizationDTO();
        dto.setTenantId(r.tenantId);
        dto.setOrganizationName(request.getOrganizationName());
        dto.setDomain(r.organizationDomain);
        dto.setPrimaryColor(request.getPrimaryColor());
        dto.setSecondaryColor(request.getSecondaryColor());
        dto.setLogoUrl(request.getLogoUrl());
        dto.setContactEmail(r.contactEmail);
        dto.setContactPhone(request.getContactPhone());
        dto.setDescription(request.getDescription());
        dto.setAddressLine1(request.getAddressLine1());
        dto.setAddressLine2(request.getAddressLine2());
        dto.setCity(request.getCity());
        dto.setStateProvince(request.getStateProvince());
        dto.setZipCode(request.getZipCode());
        dto.setCountry(request.getCountry());
        dto.setWebsiteUrl("https://" + r.hostname);
        dto.setIsActive(true);
        dto.setSiteType(request.getSiteType());
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);
        return tenantOrganizationService.save(dto);
    }

    private TenantSettingsDTO createSettings(TenantOnboardingRequest request, Resolved r, TenantOrganizationDTO org, ZonedDateTime now) {
        TenantSettingsDTO dto = new TenantSettingsDTO();
        dto.setTenantId(r.tenantId);
        TenantOrganizationDTO orgRef = new TenantOrganizationDTO();
        orgRef.setId(org.getId());
        dto.setTenantOrganization(orgRef);
        dto.setAllowUserRegistration(true);
        dto.setRequireAdminApproval(false);
        dto.setEnableEmailMarketing(false);
        dto.setIsMembershipSubscriptionEnabled(false);
        dto.setEnableGuestRegistration(true);
        dto.setShowEventsSectionInHomePage(true);
        dto.setShowTeamMembersSectionInHomePage(true);
        dto.setShowSponsorsSectionInHomePage(true);
        dto.setEmail(r.contactEmail);
        dto.setPhoneNumber(request.getContactPhone());
        dto.setHomepageCacheVersion(0L);
        dto.setDisplayEventHeroImages(true);
        dto.setEnableGoogleAdsense(false);
        dto.setShowPublicProfileHeroSection(false);
        dto.setShowProfileWritingsSection(false);
        dto.setShowProfileAchievementsSection(false);
        dto.setShowProfileAffiliationsSection(false);
        dto.setShowProfileMediaDownloadsSection(false);
        dto.setShowProfileContactSection(false);
        dto.setShowProfileProjectsSection(false);
        dto.setShowProfileServicesSection(false);
        dto.setShowProfileFamilySection(false);
        dto.setEnableGasStationModule(false);
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);
        return tenantSettingsService.save(dto);
    }

    private TenantEmailAddress createEmail(Resolved r, TenantEmailType type, String address, String displayName) {
        TenantEmailAddress email = new TenantEmailAddress();
        email.setTenantId(r.tenantId);
        email.setEmailType(type);
        email.setEmailAddress(address);
        email.setDisplayName(displayName);
        email.setIsActive(true);
        email.setIsDefault(true);
        email.setDescription("Created by onboarding approval");
        return tenantEmailAddressRepository.save(email);
    }

    private SatelliteDomain createSatellite(TenantOnboardingRequest request, Resolved r, ZonedDateTime now) {
        SatelliteDomain s = new SatelliteDomain();
        s.setSatelliteKey(r.satelliteKey);
        s.setDomain("https://" + r.hostname);
        s.setHostname(r.hostname);
        s.setDisplayName(r.displayName);
        s.setTenantId(r.tenantId);
        s.setEnabled(true);
        s.setAddedDate(now);
        s.setOrgName(request.getOrganizationName());
        s.setFullName(request.getOrganizationName());
        s.setLogoType(request.getLogoUrl() != null ? "image" : "text");
        s.setLogoUrl(request.getLogoUrl());
        s.setLogoPrimaryColor(request.getPrimaryColor());
        s.setLogoSecondaryColor(request.getSecondaryColor());
        s.setThemePrimaryColor(request.getPrimaryColor());
        s.setContactEmail(r.contactEmail);
        s.setContactPhone(request.getContactPhone());
        s.setContactAddress(joinAddress(request));
        s.setShowOnAuthHeader(true);
        s.setShowOnAuthFooter(true);
        s.setCreatedAt(now);
        s.setUpdatedAt(now);
        return satelliteDomainRepository.save(s);
    }

    private List<Map<String, Object>> cloneAdmins(Resolved r, ZonedDateTime now) {
        List<Map<String, Object>> cloned = new ArrayList<>();
        List<UserProfile> admins = userProfileRepository.findByTenantIdAndUserRoleIn(r.adminSourceTenantId, CLONED_ADMIN_ROLES);
        for (UserProfile src : admins) {
            if (src.getUserId() == null) {
                continue;
            }
            boolean exists =
                userProfileRepository.findByUserIdAndTenantId(src.getUserId(), r.tenantId).isPresent() ||
                (src.getEmail() != null && userProfileRepository.findByEmailAndTenantId(src.getEmail(), r.tenantId).isPresent());
            if (exists) {
                continue;
            }
            UserProfile p = new UserProfile();
            p.setTenantId(r.tenantId);
            p.setUserId(src.getUserId());
            p.setFirstName(src.getFirstName());
            p.setLastName(src.getLastName());
            p.setEmail(src.getEmail());
            p.setPhone(src.getPhone());
            p.setProfileImageUrl(src.getProfileImageUrl());
            p.setIsEmailSubscribed(false);
            p.setUserRole(src.getUserRole());
            p.setUserStatus("APPROVED");
            p.setStatus("APPROVED");
            p.setRequestId(null);
            p.setClerkUserId(src.getClerkUserId());
            p.setAuthProvider(src.getAuthProvider());
            p.setAuthProviderUserId(src.getAuthProviderUserId());
            p.setEmailVerified(src.getEmailVerified());
            p.setProfileImageUrlClerk(src.getProfileImageUrlClerk());
            p.setApprovedAt(now);
            p.setCreatedAt(now);
            p.setUpdatedAt(now);
            p = userProfileRepository.save(p);
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("userProfileId", p.getId());
            row.put("userId", p.getUserId());
            row.put("email", p.getEmail());
            row.put("userRole", p.getUserRole());
            cloned.add(row);
        }
        if (admins.isEmpty()) {
            LOG.warn("[ONBOARDING] No ADMIN/SUPER_ADMIN profiles found in source tenant {}", r.adminSourceTenantId);
        }
        return cloned;
    }

    private void markFailed(Long requestId, TenantOnboardingApproveDTO in, String reason) {
        try {
            failureTx.executeWithoutResult(status ->
                requestRepository
                    .findById(requestId)
                    .ifPresent(request -> {
                        if (APPROVABLE.contains(request.getStatus())) {
                            request.setStatus(OnboardingRequestStatus.FAILED);
                            request.setReviewedAt(ZonedDateTime.now());
                            applyReviewer(request, in);
                            Map<String, Object> outcome = new LinkedHashMap<>();
                            outcome.put("error", reason);
                            outcome.put("attemptedTenantId", in.getTenantId());
                            outcome.put("attemptedSatelliteKey", in.getSatelliteKey());
                            request.setProvisioningResult(toJson(outcome));
                            requestRepository.save(request);
                        }
                    })
            );
        } catch (RuntimeException e) {
            LOG.error("[ONBOARDING] Could not mark request id={} as FAILED", requestId, e);
        }
    }

    private static void applyReviewer(TenantOnboardingRequest request, TenantOnboardingApproveDTO in) {
        String clerkId = TenantOnboardingSupport.trimToNull(in.getReviewedByClerkUserId());
        if (clerkId != null) {
            request.setReviewedByClerkUserId(clerkId);
        }
        String email = TenantOnboardingSupport.trimToNull(in.getReviewedByEmail());
        if (email != null) {
            request.setReviewedByEmail(email);
        }
        String comments = TenantOnboardingSupport.trimToNull(in.getAdminComments());
        if (comments != null) {
            request.setAdminComments(comments);
        }
    }

    private void clearCaches() {
        for (String name : CACHES_TO_CLEAR) {
            try {
                Cache cache = cacheManager.getCache(name);
                if (cache != null) {
                    cache.clear();
                }
            } catch (RuntimeException e) {
                LOG.warn("[ONBOARDING] Could not clear cache {}: {}", name, e.getMessage());
            }
        }
    }

    private String toJson(Map<String, Object> value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return String.valueOf(value);
        }
    }

    private static String joinAddress(TenantOnboardingRequest request) {
        StringJoiner j = new StringJoiner(", ");
        for (String part : new String[] {
            request.getAddressLine1(),
            request.getAddressLine2(),
            request.getCity(),
            request.getStateProvince(),
            request.getZipCode(),
        }) {
            if (part != null && !part.isBlank()) {
                j.add(part.trim());
            }
        }
        String joined = j.toString();
        return joined.isEmpty() ? null : joined;
    }

    private static String rootMessage(Throwable e) {
        Throwable t = e;
        while (t.getCause() != null && t.getCause() != t) {
            t = t.getCause();
        }
        String msg = t.getMessage() != null ? t.getMessage() : t.getClass().getSimpleName();
        return msg.length() > 1000 ? msg.substring(0, 1000) : msg;
    }

    private static final class Resolved {

        String tenantId;
        String satelliteKey;
        String hostname;
        String organizationDomain;
        String displayName;
        String contactEmail;
        String infoEmail;
        String noreplyEmail;
        String adminSourceTenantId;
        boolean cloneAdmins;
    }
}
