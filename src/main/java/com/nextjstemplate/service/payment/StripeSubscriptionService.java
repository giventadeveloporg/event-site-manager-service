package com.nextjstemplate.service.payment;

import com.nextjstemplate.domain.MembershipPlan;
import com.nextjstemplate.domain.enumeration.PaymentProvider;
import com.nextjstemplate.service.payment.PaymentProviderConfigService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerSearchParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.SubscriptionCancelParams;
import com.stripe.param.SubscriptionCreateParams;
import com.stripe.param.SubscriptionUpdateParams;
import com.stripe.param.checkout.SessionCreateParams;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service for managing Stripe subscriptions.
 * Handles customer creation, subscription creation/update/cancellation, and Stripe product/price management.
 */
@Service
public class StripeSubscriptionService {

    private static final Logger log = LoggerFactory.getLogger(StripeSubscriptionService.class);

    private final PaymentProviderConfigService configService;

    public StripeSubscriptionService(PaymentProviderConfigService configService) {
        this.configService = configService;
    }

    /**
     * Initialize Stripe API key for tenant.
     */
    private void initStripe(String tenantId) throws PaymentException {
        Map<String, Object> config = configService
            .getProviderConfig(tenantId, PaymentProvider.STRIPE)
            .orElseThrow(() -> new PaymentException("STRIPE_CONFIG_ERROR", "Stripe configuration not found for tenant: " + tenantId));

        String apiKey = (String) config.get("secretKey");
        if (apiKey == null || apiKey.isEmpty()) {
            throw new PaymentException("STRIPE_CONFIG_ERROR", "Stripe secret key not configured for tenant: " + tenantId);
        }
        Stripe.apiKey = apiKey;
    }

    // ==================== CUSTOMER MANAGEMENT ====================

    /**
     * Create or retrieve a Stripe customer.
     *
     * @param tenantId Tenant ID
     * @param email Customer email
     * @param name Customer name
     * @param userProfileId User profile ID for metadata
     * @return Stripe customer ID
     */
    public String createOrRetrieveCustomer(String tenantId, String email, String name, Long userProfileId) throws PaymentException {
        try {
            initStripe(tenantId);

            // Search for existing customer by email
            CustomerSearchParams searchParams = CustomerSearchParams.builder().setQuery("email:'" + email + "'").setLimit(1L).build();

            var searchResult = Customer.search(searchParams);
            if (!searchResult.getData().isEmpty()) {
                String existingCustomerId = searchResult.getData().get(0).getId();
                log.info("Found existing Stripe customer {} for email {}", existingCustomerId, email);
                return existingCustomerId;
            }

            // Create new customer
            CustomerCreateParams.Builder customerParams = CustomerCreateParams.builder().setEmail(email);

            if (name != null && !name.isEmpty()) {
                customerParams.setName(name);
            }

            // Add metadata
            Map<String, String> metadata = new HashMap<>();
            metadata.put("tenantId", tenantId);
            if (userProfileId != null) {
                metadata.put("userProfileId", userProfileId.toString());
            }
            customerParams.putAllMetadata(metadata);

            Customer customer = Customer.create(customerParams.build());
            log.info("Created Stripe customer {} for email {}", customer.getId(), email);
            return customer.getId();
        } catch (StripeException e) {
            log.error("Failed to create/retrieve Stripe customer for email {}: {}", email, e.getMessage(), e);
            throw new PaymentException("STRIPE_ERROR", "Failed to create Stripe customer: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieve a Stripe customer by ID.
     */
    public Customer retrieveCustomer(String tenantId, String customerId) throws PaymentException {
        try {
            initStripe(tenantId);
            return Customer.retrieve(customerId);
        } catch (StripeException e) {
            log.error("Failed to retrieve Stripe customer {}: {}", customerId, e.getMessage(), e);
            throw new PaymentException("STRIPE_ERROR", "Failed to retrieve Stripe customer: " + e.getMessage(), e);
        }
    }

    // ==================== PRODUCT/PRICE MANAGEMENT ====================

    /**
     * Create Stripe Product and Price for a membership plan.
     *
     * @param tenantId Tenant ID
     * @param plan Membership plan
     * @return Map containing stripeProductId and stripePriceId
     */
    public Map<String, String> createProductAndPrice(String tenantId, MembershipPlan plan) throws PaymentException {
        try {
            initStripe(tenantId);

            // Create Product
            ProductCreateParams productParams = ProductCreateParams
                .builder()
                .setName(plan.getPlanName())
                .setDescription(plan.getDescription())
                .putMetadata("tenantId", tenantId)
                .putMetadata("planCode", plan.getPlanCode())
                .build();

            Product product = Product.create(productParams);
            log.info("Created Stripe product {} for plan {}", product.getId(), plan.getPlanCode());

            // Create Price
            PriceCreateParams.Builder priceParamsBuilder = PriceCreateParams
                .builder()
                .setProduct(product.getId())
                .setUnitAmount(convertToStripeAmount(plan.getPrice(), plan.getCurrency()))
                .setCurrency(plan.getCurrency().toLowerCase())
                .putMetadata("tenantId", tenantId)
                .putMetadata("planId", plan.getId().toString());

            // Set recurring interval based on billing interval
            if (!"ONE_TIME".equals(plan.getBillingInterval())) {
                PriceCreateParams.Recurring.Interval interval = mapBillingIntervalToStripe(plan.getBillingInterval());
                priceParamsBuilder.setRecurring(PriceCreateParams.Recurring.builder().setInterval(interval).build());
            }

            Price price = Price.create(priceParamsBuilder.build());
            log.info("Created Stripe price {} for plan {}", price.getId(), plan.getPlanCode());

            Map<String, String> result = new HashMap<>();
            result.put("stripeProductId", product.getId());
            result.put("stripePriceId", price.getId());
            return result;
        } catch (StripeException e) {
            log.error("Failed to create Stripe product/price for plan {}: {}", plan.getPlanCode(), e.getMessage(), e);
            throw new PaymentException("STRIPE_ERROR", "Failed to create Stripe product/price: " + e.getMessage(), e);
        }
    }

    // ==================== SUBSCRIPTION MANAGEMENT ====================

    /**
     * Create a Stripe subscription.
     *
     * @param tenantId Tenant ID
     * @param customerId Stripe customer ID
     * @param priceId Stripe price ID
     * @param trialDays Number of trial days (0 for no trial)
     * @param metadata Additional metadata
     * @return Stripe Subscription object
     */
    public Subscription createSubscription(String tenantId, String customerId, String priceId, int trialDays, Map<String, String> metadata)
        throws PaymentException {
        try {
            initStripe(tenantId);

            SubscriptionCreateParams.Builder subscriptionParams = SubscriptionCreateParams
                .builder()
                .setCustomer(customerId)
                .addItem(SubscriptionCreateParams.Item.builder().setPrice(priceId).build());

            // Add trial period if applicable
            if (trialDays > 0) {
                subscriptionParams.setTrialPeriodDays((long) trialDays);
            }

            // Add metadata
            if (metadata == null) {
                metadata = new HashMap<>();
            }
            metadata.put("tenantId", tenantId);
            subscriptionParams.putAllMetadata(metadata);

            Subscription subscription = Subscription.create(subscriptionParams.build());
            log.info("Created Stripe subscription {} for customer {}", subscription.getId(), customerId);
            return subscription;
        } catch (StripeException e) {
            log.error("Failed to create Stripe subscription for customer {}: {}", customerId, e.getMessage(), e);
            throw new PaymentException("STRIPE_ERROR", "Failed to create Stripe subscription: " + e.getMessage(), e);
        }
    }

    /**
     * Create a Stripe Checkout Session for subscription.
     *
     * @param tenantId Tenant ID
     * @param customerId Stripe customer ID (optional)
     * @param priceId Stripe price ID
     * @param successUrl Success URL after checkout
     * @param cancelUrl Cancel URL if checkout is cancelled
     * @param trialDays Number of trial days
     * @param metadata Additional metadata
     * @return Checkout Session with URL
     */
    public Session createCheckoutSession(
        String tenantId,
        String customerId,
        String priceId,
        String successUrl,
        String cancelUrl,
        int trialDays,
        Map<String, String> metadata
    ) throws PaymentException {
        try {
            initStripe(tenantId);

            SessionCreateParams.Builder sessionParams = SessionCreateParams
                .builder()
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addLineItem(SessionCreateParams.LineItem.builder().setPrice(priceId).setQuantity(1L).build());

            // Set existing customer if provided
            if (customerId != null && !customerId.isEmpty()) {
                sessionParams.setCustomer(customerId);
            } else {
                sessionParams.setCustomerCreation(SessionCreateParams.CustomerCreation.ALWAYS);
            }

            // Add trial period if applicable
            if (trialDays > 0) {
                sessionParams.setSubscriptionData(
                    SessionCreateParams.SubscriptionData.builder().setTrialPeriodDays((long) trialDays).build()
                );
            }

            // Add metadata
            if (metadata == null) {
                metadata = new HashMap<>();
            }
            metadata.put("tenantId", tenantId);
            sessionParams.putAllMetadata(metadata);

            Session session = Session.create(sessionParams.build());
            log.info("Created Stripe Checkout Session {} for price {}", session.getId(), priceId);
            return session;
        } catch (StripeException e) {
            log.error("Failed to create Stripe Checkout Session: {}", e.getMessage(), e);
            throw new PaymentException("STRIPE_ERROR", "Failed to create Stripe Checkout Session: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieve a Stripe subscription.
     */
    public Subscription retrieveSubscription(String tenantId, String subscriptionId) throws PaymentException {
        try {
            initStripe(tenantId);
            return Subscription.retrieve(subscriptionId);
        } catch (StripeException e) {
            log.error("Failed to retrieve Stripe subscription {}: {}", subscriptionId, e.getMessage(), e);
            throw new PaymentException("STRIPE_ERROR", "Failed to retrieve Stripe subscription: " + e.getMessage(), e);
        }
    }

    /**
     * Update a Stripe subscription's price (for upgrade/downgrade).
     *
     * @param tenantId Tenant ID
     * @param subscriptionId Stripe subscription ID
     * @param newPriceId New Stripe price ID
     * @param prorationBehavior Proration behavior (create_prorations, none, always_invoice)
     * @return Updated Subscription
     */
    public Subscription updateSubscriptionPrice(String tenantId, String subscriptionId, String newPriceId, String prorationBehavior)
        throws PaymentException {
        try {
            initStripe(tenantId);

            Subscription subscription = Subscription.retrieve(subscriptionId);
            String currentItemId = subscription.getItems().getData().get(0).getId();

            SubscriptionUpdateParams.Builder updateParams = SubscriptionUpdateParams
                .builder()
                .addItem(SubscriptionUpdateParams.Item.builder().setId(currentItemId).setPrice(newPriceId).build());

            // Set proration behavior
            if (prorationBehavior != null) {
                switch (prorationBehavior.toLowerCase()) {
                    case "none":
                        updateParams.setProrationBehavior(SubscriptionUpdateParams.ProrationBehavior.NONE);
                        break;
                    case "always_invoice":
                        updateParams.setProrationBehavior(SubscriptionUpdateParams.ProrationBehavior.ALWAYS_INVOICE);
                        break;
                    default:
                        updateParams.setProrationBehavior(SubscriptionUpdateParams.ProrationBehavior.CREATE_PRORATIONS);
                }
            }

            Subscription updated = subscription.update(updateParams.build());
            log.info("Updated Stripe subscription {} to price {}", subscriptionId, newPriceId);
            return updated;
        } catch (StripeException e) {
            log.error("Failed to update Stripe subscription {}: {}", subscriptionId, e.getMessage(), e);
            throw new PaymentException("STRIPE_ERROR", "Failed to update Stripe subscription: " + e.getMessage(), e);
        }
    }

    /**
     * Cancel a Stripe subscription.
     *
     * @param tenantId Tenant ID
     * @param subscriptionId Stripe subscription ID
     * @param cancelAtPeriodEnd If true, subscription will remain active until period end
     * @return Cancelled Subscription
     */
    public Subscription cancelSubscription(String tenantId, String subscriptionId, boolean cancelAtPeriodEnd) throws PaymentException {
        try {
            initStripe(tenantId);

            Subscription subscription = Subscription.retrieve(subscriptionId);

            if (cancelAtPeriodEnd) {
                // Cancel at period end
                SubscriptionUpdateParams updateParams = SubscriptionUpdateParams.builder().setCancelAtPeriodEnd(true).build();

                Subscription updated = subscription.update(updateParams);
                log.info("Set Stripe subscription {} to cancel at period end", subscriptionId);
                return updated;
            } else {
                // Cancel immediately
                Subscription cancelled = subscription.cancel();
                log.info("Cancelled Stripe subscription {} immediately", subscriptionId);
                return cancelled;
            }
        } catch (StripeException e) {
            log.error("Failed to cancel Stripe subscription {}: {}", subscriptionId, e.getMessage(), e);
            throw new PaymentException("STRIPE_ERROR", "Failed to cancel Stripe subscription: " + e.getMessage(), e);
        }
    }

    /**
     * Resume a cancelled subscription (only if cancel_at_period_end was true).
     */
    public Subscription resumeSubscription(String tenantId, String subscriptionId) throws PaymentException {
        try {
            initStripe(tenantId);

            Subscription subscription = Subscription.retrieve(subscriptionId);

            SubscriptionUpdateParams updateParams = SubscriptionUpdateParams.builder().setCancelAtPeriodEnd(false).build();

            Subscription updated = subscription.update(updateParams);
            log.info("Resumed Stripe subscription {}", subscriptionId);
            return updated;
        } catch (StripeException e) {
            log.error("Failed to resume Stripe subscription {}: {}", subscriptionId, e.getMessage(), e);
            throw new PaymentException("STRIPE_ERROR", "Failed to resume Stripe subscription: " + e.getMessage(), e);
        }
    }

    // ==================== HELPER METHODS ====================

    /**
     * Convert BigDecimal amount to Stripe amount (cents).
     */
    private Long convertToStripeAmount(BigDecimal amount, String currency) {
        // Stripe amounts are in smallest currency unit (cents for USD)
        return amount.multiply(BigDecimal.valueOf(100)).longValue();
    }

    /**
     * Map billing interval to Stripe recurring interval.
     */
    private PriceCreateParams.Recurring.Interval mapBillingIntervalToStripe(String billingInterval) {
        return switch (billingInterval.toUpperCase()) {
            case "MONTHLY" -> PriceCreateParams.Recurring.Interval.MONTH;
            case "QUARTERLY" -> PriceCreateParams.Recurring.Interval.MONTH; // Note: Stripe doesn't have quarterly, use month with interval_count
            case "YEARLY" -> PriceCreateParams.Recurring.Interval.YEAR;
            default -> PriceCreateParams.Recurring.Interval.MONTH;
        };
    }

    /**
     * Get Stripe publishable key for tenant.
     */
    public String getPublishableKey(String tenantId) throws PaymentException {
        Map<String, Object> config = configService
            .getProviderConfig(tenantId, PaymentProvider.STRIPE)
            .orElseThrow(() -> new PaymentException("STRIPE_CONFIG_ERROR", "Stripe configuration not found for tenant: " + tenantId));

        String publishableKey = (String) config.get("publishableKey");
        if (publishableKey == null || publishableKey.isEmpty()) {
            throw new PaymentException("STRIPE_CONFIG_ERROR", "Stripe publishable key not configured for tenant: " + tenantId);
        }
        return publishableKey;
    }
}
