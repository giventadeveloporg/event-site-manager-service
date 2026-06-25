package com.eventsitemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Tracks active user sessions from Clerk.
 * Used for session management and audit trail.
 */
@Entity
@Table(name = "clerk_session")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClerkSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "public.sequence_generator", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "session_id", length = 255, nullable = false, unique = true)
    private String sessionId;

    @NotNull
    @Size(max = 255)
    @Column(name = "clerk_user_id", length = 255, nullable = false)
    private String clerkUserId;

    @Size(max = 255)
    @Column(name = "tenant_id", length = 255)
    private String tenantId;

    @Size(max = 255)
    @Column(name = "client_id", length = 255)
    private String clientId;

    @Size(max = 45)
    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "text")
    private String userAgent;

    @Size(max = 50)
    @Column(name = "status", length = 50)
    private String status;

    @NotNull
    @Column(name = "expires_at", nullable = false)
    private ZonedDateTime expiresAt;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "last_active_at", nullable = false)
    private ZonedDateTime lastActiveAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id")
    @JsonIgnoreProperties(value = { "reviewedByAdmin", "userSubscription" }, allowSetters = true)
    private UserProfile userProfile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ClerkSession id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public ClerkSession sessionId(String sessionId) {
        this.setSessionId(sessionId);
        return this;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getClerkUserId() {
        return this.clerkUserId;
    }

    public ClerkSession clerkUserId(String clerkUserId) {
        this.setClerkUserId(clerkUserId);
        return this;
    }

    public void setClerkUserId(String clerkUserId) {
        this.clerkUserId = clerkUserId;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public ClerkSession tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getClientId() {
        return this.clientId;
    }

    public ClerkSession clientId(String clientId) {
        this.setClientId(clientId);
        return this;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public ClerkSession ipAddress(String ipAddress) {
        this.setIpAddress(ipAddress);
        return this;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public ClerkSession userAgent(String userAgent) {
        this.setUserAgent(userAgent);
        return this;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getStatus() {
        return this.status;
    }

    public ClerkSession status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getExpiresAt() {
        return this.expiresAt;
    }

    public ClerkSession expiresAt(ZonedDateTime expiresAt) {
        this.setExpiresAt(expiresAt);
        return this;
    }

    public void setExpiresAt(ZonedDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public ClerkSession createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getLastActiveAt() {
        return this.lastActiveAt;
    }

    public ClerkSession lastActiveAt(ZonedDateTime lastActiveAt) {
        this.setLastActiveAt(lastActiveAt);
        return this;
    }

    public void setLastActiveAt(ZonedDateTime lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public ClerkSession userProfile(UserProfile userProfile) {
        this.setUserProfile(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClerkSession)) {
            return false;
        }
        return getId() != null && getId().equals(((ClerkSession) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClerkSession{" +
            "id=" + getId() +
            ", sessionId='" + getSessionId() + "'" +
            ", clerkUserId='" + getClerkUserId() + "'" +
            ", tenantId='" + getTenantId() + "'" +
            ", clientId='" + getClientId() + "'" +
            ", ipAddress='" + getIpAddress() + "'" +
            ", userAgent='" + getUserAgent() + "'" +
            ", status='" + getStatus() + "'" +
            ", expiresAt='" + getExpiresAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", lastActiveAt='" + getLastActiveAt() + "'" +
            "}";
    }
}
