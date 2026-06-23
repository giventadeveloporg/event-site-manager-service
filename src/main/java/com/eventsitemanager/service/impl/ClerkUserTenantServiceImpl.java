package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.ClerkUserTenant;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.repository.ClerkUserTenantRepository;
import com.eventsitemanager.repository.UserProfileRepository;
import com.eventsitemanager.service.ClerkUserTenantService;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ClerkUserTenant}.
 */
@Service
@Transactional
public class ClerkUserTenantServiceImpl implements ClerkUserTenantService {

    private final Logger log = LoggerFactory.getLogger(ClerkUserTenantServiceImpl.class);

    private final ClerkUserTenantRepository clerkUserTenantRepository;
    private final UserProfileRepository userProfileRepository;

    public ClerkUserTenantServiceImpl(ClerkUserTenantRepository clerkUserTenantRepository, UserProfileRepository userProfileRepository) {
        this.clerkUserTenantRepository = clerkUserTenantRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public ClerkUserTenant save(ClerkUserTenant clerkUserTenant) {
        log.debug("Request to save ClerkUserTenant : {}", clerkUserTenant);
        return clerkUserTenantRepository.save(clerkUserTenant);
    }

    @Override
    public ClerkUserTenant update(ClerkUserTenant clerkUserTenant) {
        log.debug("Request to update ClerkUserTenant : {}", clerkUserTenant);
        clerkUserTenant.setUpdatedAt(ZonedDateTime.now());
        return clerkUserTenantRepository.save(clerkUserTenant);
    }

    @Override
    public Optional<ClerkUserTenant> partialUpdate(ClerkUserTenant clerkUserTenant) {
        log.debug("Request to partially update ClerkUserTenant : {}", clerkUserTenant);

        return clerkUserTenantRepository
            .findById(clerkUserTenant.getId())
            .map(existingClerkUserTenant -> {
                if (clerkUserTenant.getTenantId() != null) {
                    existingClerkUserTenant.setTenantId(clerkUserTenant.getTenantId());
                }
                if (clerkUserTenant.getRole() != null) {
                    existingClerkUserTenant.setRole(clerkUserTenant.getRole());
                }
                if (clerkUserTenant.getStatus() != null) {
                    existingClerkUserTenant.setStatus(clerkUserTenant.getStatus());
                }
                if (clerkUserTenant.getJoinedAt() != null) {
                    existingClerkUserTenant.setJoinedAt(clerkUserTenant.getJoinedAt());
                }
                existingClerkUserTenant.setUpdatedAt(ZonedDateTime.now());

                return existingClerkUserTenant;
            })
            .map(clerkUserTenantRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClerkUserTenant> findAll() {
        log.debug("Request to get all ClerkUserTenants");
        return clerkUserTenantRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClerkUserTenant> findOne(Long id) {
        log.debug("Request to get ClerkUserTenant : {}", id);
        return clerkUserTenantRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ClerkUserTenant : {}", id);
        clerkUserTenantRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClerkUserTenant> findByUserProfile(UserProfile userProfile) {
        log.debug("Request to get all ClerkUserTenants for UserProfile : {}", userProfile.getId());
        return clerkUserTenantRepository.findByUserProfile(userProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClerkUserTenant> findByUserProfileId(Long userProfileId) {
        log.debug("Request to get all ClerkUserTenants for UserProfile ID : {}", userProfileId);
        return clerkUserTenantRepository.findByUserProfileId(userProfileId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClerkUserTenant> findByTenantId(String tenantId) {
        log.debug("Request to get all ClerkUserTenants for Tenant : {}", tenantId);
        return clerkUserTenantRepository.findByTenantId(tenantId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClerkUserTenant> findByUserProfileAndTenantId(UserProfile userProfile, String tenantId) {
        log.debug("Request to get ClerkUserTenant for UserProfile : {} and Tenant : {}", userProfile.getId(), tenantId);
        return clerkUserTenantRepository.findByUserProfileAndTenantId(userProfile, tenantId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClerkUserTenant> findByUserProfileIdAndTenantId(Long userProfileId, String tenantId) {
        log.debug("Request to get ClerkUserTenant for UserProfile ID : {} and Tenant : {}", userProfileId, tenantId);
        return clerkUserTenantRepository.findByUserProfileIdAndTenantId(userProfileId, tenantId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasAccessToTenant(Long userProfileId, String tenantId) {
        log.debug("Request to check if UserProfile ID : {} has access to Tenant : {}", userProfileId, tenantId);
        return clerkUserTenantRepository.existsActiveByUserProfileIdAndTenantId(userProfileId, tenantId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClerkUserTenant> findActiveByUserProfileId(Long userProfileId) {
        log.debug("Request to get all active ClerkUserTenants for UserProfile ID : {}", userProfileId);
        return clerkUserTenantRepository.findActiveByUserProfileId(userProfileId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClerkUserTenant> findActiveByTenantId(String tenantId) {
        log.debug("Request to get all active ClerkUserTenants for Tenant : {}", tenantId);
        return clerkUserTenantRepository.findActiveByTenantId(tenantId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveByTenantId(String tenantId) {
        log.debug("Request to count active members for Tenant : {}", tenantId);
        return clerkUserTenantRepository.countActiveByTenantId(tenantId);
    }

    @Override
    public ClerkUserTenant getOrCreateMembership(UserProfile userProfile, String tenantId, String defaultRole) {
        log.debug("Request to get or create ClerkUserTenant for UserProfile : {} and Tenant : {}", userProfile.getId(), tenantId);

        return clerkUserTenantRepository
            .findByUserProfileAndTenantId(userProfile, tenantId)
            .orElseGet(() -> {
                log.info(
                    "Creating new tenant membership for user {} in tenant {} with role {}",
                    userProfile.getId(),
                    tenantId,
                    defaultRole
                );

                ClerkUserTenant newMembership = new ClerkUserTenant();
                newMembership.setUserProfile(userProfile);
                newMembership.setTenantId(tenantId);
                newMembership.setRole(defaultRole != null ? defaultRole : "member");
                newMembership.setStatus("active");
                newMembership.setJoinedAt(ZonedDateTime.now());
                newMembership.setUpdatedAt(ZonedDateTime.now());

                return clerkUserTenantRepository.save(newMembership);
            });
    }

    @Override
    public ClerkUserTenant getOrCreateMembershipByUserProfileId(Long userProfileId, String tenantId, String defaultRole) {
        log.debug("Request to get or create ClerkUserTenant for UserProfile ID : {} and Tenant : {}", userProfileId, tenantId);

        UserProfile userProfile = userProfileRepository
            .findById(userProfileId)
            .orElseThrow(() -> new IllegalArgumentException("UserProfile not found with ID: " + userProfileId));

        return getOrCreateMembership(userProfile, tenantId, defaultRole);
    }

    @Override
    public Optional<ClerkUserTenant> updateRole(Long userProfileId, String tenantId, String newRole) {
        log.debug("Request to update role for UserProfile ID : {} in Tenant : {} to {}", userProfileId, tenantId, newRole);

        return clerkUserTenantRepository
            .findByUserProfileIdAndTenantId(userProfileId, tenantId)
            .map(membership -> {
                membership.setRole(newRole);
                membership.setUpdatedAt(ZonedDateTime.now());
                return clerkUserTenantRepository.save(membership);
            });
    }

    @Override
    public Optional<ClerkUserTenant> updateStatus(Long userProfileId, String tenantId, String newStatus) {
        log.debug("Request to update status for UserProfile ID : {} in Tenant : {} to {}", userProfileId, tenantId, newStatus);

        return clerkUserTenantRepository
            .findByUserProfileIdAndTenantId(userProfileId, tenantId)
            .map(membership -> {
                membership.setStatus(newStatus);
                membership.setUpdatedAt(ZonedDateTime.now());
                return clerkUserTenantRepository.save(membership);
            });
    }

    @Override
    public void revokeAccess(Long userProfileId, String tenantId) {
        log.debug("Request to revoke access for UserProfile ID : {} from Tenant : {}", userProfileId, tenantId);

        clerkUserTenantRepository
            .findByUserProfileIdAndTenantId(userProfileId, tenantId)
            .ifPresent(membership -> {
                membership.setStatus("revoked");
                membership.setUpdatedAt(ZonedDateTime.now());
                clerkUserTenantRepository.save(membership);
            });
    }

    @Override
    public ClerkUserTenant grantAccess(Long userProfileId, String tenantId, String role) {
        log.debug("Request to grant access for UserProfile ID : {} to Tenant : {} with role {}", userProfileId, tenantId, role);

        UserProfile userProfile = userProfileRepository
            .findById(userProfileId)
            .orElseThrow(() -> new IllegalArgumentException("UserProfile not found with ID: " + userProfileId));

        // Check if membership already exists
        return clerkUserTenantRepository
            .findByUserProfileAndTenantId(userProfile, tenantId)
            .map(membership -> {
                // Reactivate existing membership
                membership.setStatus("active");
                membership.setRole(role);
                membership.setUpdatedAt(ZonedDateTime.now());
                return clerkUserTenantRepository.save(membership);
            })
            .orElseGet(() -> {
                // Create new membership
                ClerkUserTenant newMembership = new ClerkUserTenant();
                newMembership.setUserProfile(userProfile);
                newMembership.setTenantId(tenantId);
                newMembership.setRole(role);
                newMembership.setStatus("active");
                newMembership.setJoinedAt(ZonedDateTime.now());
                newMembership.setUpdatedAt(ZonedDateTime.now());
                return clerkUserTenantRepository.save(newMembership);
            });
    }
}
