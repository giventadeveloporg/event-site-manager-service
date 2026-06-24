package com.eventsitemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserSubscription.
 */
@Entity
@Table(name = "user_subscription")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "public.sequence_generator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "tenant_id", length = 255)
    private String tenantId;

    @Size(max = 255)
    @Column(name = "stripe_customer_id", length = 255, unique = true)
    private String stripeCustomerId;

    @Size(max = 255)
    @Column(name = "stripe_subscription_id", length = 255, unique = true)
    private String stripeSubscriptionId;

    @Size(max = 255)
    @Column(name = "stripe_price_id", length = 255)
    private String stripePriceId;

    @Column(name = "stripe_current_period_end")
    private ZonedDateTime stripeCurrentPeriodEnd;

    @NotNull
    @Size(max = 255)
    @Column(name = "status", length = 255, nullable = false)
    private String status;

    @JsonIgnoreProperties(value = { "reviewedByAdmin", "userSubscription" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private UserProfile userProfile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserSubscription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public UserSubscription tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getStripeCustomerId() {
        return this.stripeCustomerId;
    }

    public UserSubscription stripeCustomerId(String stripeCustomerId) {
        this.setStripeCustomerId(stripeCustomerId);
        return this;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public String getStripeSubscriptionId() {
        return this.stripeSubscriptionId;
    }

    public UserSubscription stripeSubscriptionId(String stripeSubscriptionId) {
        this.setStripeSubscriptionId(stripeSubscriptionId);
        return this;
    }

    public void setStripeSubscriptionId(String stripeSubscriptionId) {
        this.stripeSubscriptionId = stripeSubscriptionId;
    }

    public String getStripePriceId() {
        return this.stripePriceId;
    }

    public UserSubscription stripePriceId(String stripePriceId) {
        this.setStripePriceId(stripePriceId);
        return this;
    }

    public void setStripePriceId(String stripePriceId) {
        this.stripePriceId = stripePriceId;
    }

    public ZonedDateTime getStripeCurrentPeriodEnd() {
        return this.stripeCurrentPeriodEnd;
    }

    public UserSubscription stripeCurrentPeriodEnd(ZonedDateTime stripeCurrentPeriodEnd) {
        this.setStripeCurrentPeriodEnd(stripeCurrentPeriodEnd);
        return this;
    }

    public void setStripeCurrentPeriodEnd(ZonedDateTime stripeCurrentPeriodEnd) {
        this.stripeCurrentPeriodEnd = stripeCurrentPeriodEnd;
    }

    public String getStatus() {
        return this.status;
    }

    public UserSubscription status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UserSubscription userProfile(UserProfile userProfile) {
        this.setUserProfile(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserSubscription)) {
            return false;
        }
        return getId() != null && getId().equals(((UserSubscription) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserSubscription{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", stripeCustomerId='" + getStripeCustomerId() + "'" +
            ", stripeSubscriptionId='" + getStripeSubscriptionId() + "'" +
            ", stripePriceId='" + getStripePriceId() + "'" +
            ", stripeCurrentPeriodEnd='" + getStripeCurrentPeriodEnd() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
