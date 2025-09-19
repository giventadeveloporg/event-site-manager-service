package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.domain.enumeration.UserRoleType;
import com.nextjstemplate.domain.enumeration.UserStatusType;
import com.nextjstemplate.repository.UserProfileRepository;
import com.nextjstemplate.service.EmailSubscriptionTokenService;
import com.nextjstemplate.service.UserProfileService;
import com.nextjstemplate.service.dto.UserProfileDTO;
import com.nextjstemplate.service.mapper.UserProfileMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing
 * {@link com.nextjstemplate.domain.UserProfile}.
 */
@Service
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private final Logger log = LoggerFactory.getLogger(UserProfileServiceImpl.class);

    private final UserProfileRepository userProfileRepository;

    private final UserProfileMapper userProfileMapper;

    private final EmailSubscriptionTokenService emailSubscriptionTokenService;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository, UserProfileMapper userProfileMapper,
            EmailSubscriptionTokenService emailSubscriptionTokenService) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileMapper = userProfileMapper;
        this.emailSubscriptionTokenService = emailSubscriptionTokenService;
    }

    @Override
    public UserProfileDTO save(UserProfileDTO userProfileDTO) {
        log.debug("Request to save UserProfile : {}", userProfileDTO);
        UserProfile userProfile = userProfileMapper.toEntity(userProfileDTO);
        if (userProfile.getUserRole() == null) {
            userProfile.setUserRole(UserRoleType.MEMBER.name()); // Set a default role
        }

        if (userProfile.getUserStatus() == null) {
            userProfile.setUserStatus(UserStatusType.PENDING_APPROVAL.name()); // Set a default status
        }

        // Set email subscription to true for new users with email
        if (userProfile.getId() == null && userProfile.getEmail() != null && !userProfile.getEmail().trim().isEmpty()) {
            userProfile.setIsEmailSubscribed(true);
            log.debug("Set isEmailSubscribed to true for new user with email: {}", userProfile.getEmail());
        }

        // Generate JWT token for email subscription
        generateEmailSubscriptionTokenIfNeeded(userProfile);

        userProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.toDto(userProfile);
    }

    @Override
    public UserProfileDTO update(UserProfileDTO userProfileDTO) {
        log.debug("Request to update UserProfile : {}", userProfileDTO);
        UserProfile userProfile = userProfileMapper.toEntity(userProfileDTO);

        // Generate JWT token for email subscription
        generateEmailSubscriptionTokenIfNeeded(userProfile);

        userProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.toDto(userProfile);
    }

    @Override
    public Optional<UserProfileDTO> partialUpdate(UserProfileDTO userProfileDTO) {
        log.debug("Request to partially update UserProfile : {}", userProfileDTO);

        return userProfileRepository
                .findById(userProfileDTO.getId())
                .map(existingUserProfile -> {
                    userProfileMapper.partialUpdate(existingUserProfile, userProfileDTO);

                    // Generate JWT token for email subscription
                    generateEmailSubscriptionTokenIfNeeded(existingUserProfile);

                    return existingUserProfile;
                })
                .map(userProfileRepository::save)
                .map(userProfileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserProfiles");
        return userProfileRepository.findAll(pageable).map(userProfileMapper::toDto);
    }

    /**
     * Get all the userProfiles where UserSubscription is {@code null}.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserProfileDTO> findAllWhereUserSubscriptionIsNull() {
        log.debug("Request to get all userProfiles where UserSubscription is null");
        return StreamSupport
                .stream(userProfileRepository.findAll().spliterator(), false)
                .filter(userProfile -> userProfile.getUserSubscription() == null)
                .map(userProfileMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional
    public Optional<UserProfileDTO> findOne(Long id) {
        log.debug("Request to get UserProfile : {}", id);
        return userProfileRepository.findById(id)
                .map(this::refreshEmailSubscriptionTokenIfNeeded)
                .map(userProfileMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserProfile : {}", id);
        userProfileRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<UserProfileDTO> findByUserId(String userId) {
        log.debug("Request to get UserProfile by user ID : {}", userId);
        return userProfileRepository.findByUserId(userId)
                .map(this::refreshEmailSubscriptionTokenIfNeeded)
                .map(userProfileMapper::toDto);
    }

    @Override
    @Transactional
    public Optional<UserProfileDTO> findByEmail(String email) {
        log.debug("Request to get UserProfile by email : {}", email);
        return userProfileRepository.findByEmail(email)
                .map(this::refreshEmailSubscriptionTokenIfNeeded)
                .map(userProfileMapper::toDto);
    }

    @Override
    @Transactional
    public Optional<UserProfileDTO> findByEmailAndTenantId(String email, String tenantId) {
        log.debug("Request to get UserProfile by email {} and tenantId {}", email, tenantId);
        return userProfileRepository.findByEmailAndTenantId(email, tenantId)
                .map(this::refreshEmailSubscriptionTokenIfNeeded)
                .map(userProfileMapper::toDto);
    }

    @Override
    @Transactional
    public String getUnsubscribeTokenByEmailAndTenantId(String email, String tenantId) {
        log.debug("Request to get unsubscribe token by email {} and tenantId {}", email, tenantId);
        return userProfileRepository.findByEmailAndTenantId(email, tenantId)
                .map(this::refreshEmailSubscriptionTokenIfNeeded)
                .map(UserProfile::getEmailSubscriptionToken)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findSubscribedEmailsByTenantId(String tenantId) {
        log.debug("Request to get all subscribed emails for tenantId {}", tenantId);
        return userProfileRepository.findSubscribedEmailsByTenantId(tenantId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileDTO> findSubscribedUsersByTenantIdWithPagination(String tenantId, int limit, int offset) {
        log.debug("Request to get subscribed users for tenantId {} with limit {} and offset {}", tenantId, limit,
                offset);
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<UserProfile> page = userProfileRepository.findSubscribedUsersByTenantIdWithPagination(tenantId, pageable);
        return page.getContent().stream()
                .map(userProfileMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    /**
     * Used by the excel bulk upload template to insert multiple users at the same
     * time in the user profile table
     * Also makes sure to insert only users with a combination of the tenant ID and
     * the email
     * that don't already exist in the table
     * column names tenant_id and email.
     */
    public List<UserProfileDTO> saveBulkUploadUsers(List<UserProfileDTO> userProfileDTOs) {
        log.debug("Request to save bulk upload users : {}", userProfileDTOs.size());

        // Filter out users with email and tenant ID combination that already exist in
        // the database
        List<UserProfileDTO> filteredUsers = userProfileDTOs.stream()
                .filter(userProfileDTO -> {
                    if (userProfileDTO.getEmail() == null || userProfileDTO.getEmail().trim().isEmpty()) {
                        return false; // Skip users without email
                    }
                    if (userProfileDTO.getTenantId() == null || userProfileDTO.getTenantId().trim().isEmpty()) {
                        return false; // Skip users without tenant ID
                    }
                    Optional<UserProfile> existingUser = userProfileRepository.findByEmailAndTenantId(
                            userProfileDTO.getEmail(), userProfileDTO.getTenantId());
                    return existingUser.isEmpty(); // Only include if email and tenant ID combination doesn't exist
                })
                .collect(Collectors.toList());

        log.debug("Filtered {} users to {} after removing existing email and tenant ID combinations",
                userProfileDTOs.size(), filteredUsers.size());

        // Map each DTO to a domain/entity object
        List<UserProfile> domainList = filteredUsers.stream()
                .map(userProfileMapper::toEntity)
                .peek(userProfile -> {
                    if (userProfile.getUserRole() == null) {
                        userProfile.setUserRole(UserRoleType.MEMBER.name()); // Set a default role
                    }
                    if (userProfile.getUserStatus() == null) {
                        userProfile.setUserStatus(UserStatusType.PENDING_APPROVAL.name()); // Set a default status
                    }
                    // Set email subscription to true for bulk uploaded users with email
                    if (userProfile.getEmail() != null && !userProfile.getEmail().trim().isEmpty()) {
                        userProfile.setIsEmailSubscribed(true);
                    }
                    // Generate JWT token for email subscription
                    generateEmailSubscriptionTokenIfNeeded(userProfile);
                })
                .collect(Collectors.toList());

        // Save all domain objects in the repository
        List<UserProfile> saved = userProfileRepository.saveAll(domainList);

        // Map saved entities back to DTOs
        return saved.stream()
                .map(userProfileMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Helper method to generate JWT token for email subscription if needed.
     * Generates a new token if the user has an email and tenantId, and the current
     * token is null or empty.
     *
     * @param userProfile the user profile entity
     */
    private void generateEmailSubscriptionTokenIfNeeded(UserProfile userProfile) {
        if (userProfile != null &&
                userProfile.getEmail() != null &&
                !userProfile.getEmail().trim().isEmpty() &&
                userProfile.getTenantId() != null &&
                !userProfile.getTenantId().trim().isEmpty()) {

            // Always generate a new token on insert/update operations
            try {
                String token = emailSubscriptionTokenService.generateEmailSubscriptionToken(
                        userProfile.getEmail(),
                        userProfile.getTenantId(),
                        userProfile.getUserId());
                userProfile.setEmailSubscriptionToken(token);
                userProfile.setIsEmailSubscriptionTokenUsed(false); // Reset token usage flag
                log.debug("Generated new email subscription token for user with email: {}", userProfile.getEmail());
            } catch (Exception e) {
                log.error("Failed to generate email subscription token for user with email: {}",
                        userProfile.getEmail(), e);
                // Don't fail the operation if token generation fails
            }
        }
    }

    /**
     * Helper method to refresh email subscription token if it's null or empty
     * during GET operations.
     * This method checks if a token needs to be generated and updates the record if
     * necessary.
     *
     * @param userProfile the user profile entity to check and potentially update
     * @return the updated UserProfile entity if token was refreshed, or the
     *         original entity if no update was needed
     */
    private UserProfile refreshEmailSubscriptionTokenIfNeeded(UserProfile userProfile) {
        if (userProfile != null &&
                userProfile.getEmail() != null &&
                !userProfile.getEmail().trim().isEmpty() &&
                userProfile.getTenantId() != null &&
                !userProfile.getTenantId().trim().isEmpty() &&
                (userProfile.getEmailSubscriptionToken() == null
                        || userProfile.getEmailSubscriptionToken().trim().isEmpty())) {

            try {
                String token = emailSubscriptionTokenService.generateEmailSubscriptionToken(
                        userProfile.getEmail(),
                        userProfile.getTenantId(),
                        userProfile.getUserId());
                userProfile.setEmailSubscriptionToken(token);
                userProfile.setIsEmailSubscriptionTokenUsed(false); // Reset token usage flag

                // Save the updated record
                userProfile = userProfileRepository.save(userProfile);
                log.debug("Refreshed email subscription token for user with email: {}", userProfile.getEmail());
            } catch (Exception e) {
                log.error("Failed to refresh email subscription token for user with email: {}",
                        userProfile.getEmail(), e);
                // Don't fail the operation if token generation fails
            }
        }
        return userProfile;
    }
}
