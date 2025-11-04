package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;

/**
 * Enterprise-grade request DTO for creating a new PayPal subscription.
 *
 * <p>This class encapsulates all the necessary information required to initiate
 * a subscription creation request with PayPal, including plan identification,
 * shipping preferences, and application context for handling the payment flow.
 *
 * <p>This class is immutable and thread-safe, ensuring consistent state throughout
 * the subscription creation process.
 *
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class CreateSubscriptionRequest {

    /**
     * The PayPal Plan ID that defines the subscription terms.
     * This ID must correspond to an active subscription plan in PayPal.
     */
    @NotNull(message = "planId must not be null")
    @NotBlank(message = "planId must not be blank")
    @Size(max = 128, message = "planId must not exceed 128 characters")
    private final String planId;

    /**
     * Optional shipping amount configuration for the subscription.
     * May be null if the subscription doesn't require shipping or has
     * shipping costs included in the plan.
     */
    private final ShippingAmount shippingAmount;

    /**
     * Application context containing URLs for payment flow handling.
     * Specifies where the user should be redirected after successful or
     * cancelled payment attempts.
     */
    @NotNull(message = "applicationContext must not be null")
    private final ApplicationContext applicationContext;

    /**
     * Constructs a new CreateSubscriptionRequest with all required parameters.
     *
     * @param planId             the PayPal Plan ID (required, non-null, non-blank, max 128 chars)
     * @param shippingAmount     the optional shipping amount configuration
     * @param applicationContext the application context with return/cancel URLs
     */
    private CreateSubscriptionRequest(
            String planId,
            ShippingAmount shippingAmount,
            ApplicationContext applicationContext) {
        this.planId = Objects.requireNonNull(planId, "planId must not be null");
        this.shippingAmount = shippingAmount;
        this.applicationContext = Objects.requireNonNull(applicationContext, "applicationContext must not be null");
    }

    /**
     * Creates a new builder for constructing CreateSubscriptionRequest instances.
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Returns the PayPal Plan ID.
     *
     * @return the plan ID, never null
     */
    @JsonProperty("plan_id")
    public String getPlanId() {
        return planId;
    }

    /**
     * Returns the shipping amount configuration if present.
     *
     * @return the shipping amount, may be null
     */
    @JsonProperty("shipping_amount")
    public ShippingAmount getShippingAmount() {
        return shippingAmount;
    }

    /**
     * Returns the application context configuration.
     *
     * @return the application context, never null
     */
    @JsonProperty("application_context")
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * {@inheritDoc}
     *
     * @implNote This implementation is based on all fields to ensure proper equality checking.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateSubscriptionRequest that = (CreateSubscriptionRequest) o;
        return Objects.equals(planId, that.planId)
                && Objects.equals(shippingAmount, that.shippingAmount)
                && Objects.equals(applicationContext, that.applicationContext);
    }

    /**
     * {@inheritDoc}
     *
     * @implNote The hash code is computed using all fields.
     */
    @Override
    public int hashCode() {
        return Objects.hash(planId, shippingAmount, applicationContext);
    }

    /**
     * {@inheritDoc}
     *
     * @implNote Returns a string representation suitable for logging and debugging.
     * Note: The planId is included in the output for debugging purposes, but be aware
     * that this may expose sensitive information in logs.
     */
    @Override
    public String toString() {
        return "CreateSubscriptionRequest{"
                + "planId='"
                + planId
                + '\''
                + ", shippingAmount="
                + shippingAmount
                + ", applicationContext="
                + applicationContext
                + '}';
    }

    /**
     * Builder class for constructing CreateSubscriptionRequest instances in a fluent manner.
     *
     * <p>This builder follows the builder pattern and provides:
     * <ul>
     *   <li>Type-safe construction with compile-time checking</li>
     *   <li>Clear separation of required and optional fields</li>
     *   <li>Validation of field constraints during construction</li>
     *   <li>Immutable configuration once built</li>
     * </ul>
     */
    public static final class Builder {

        private String planId;
        private ShippingAmount shippingAmount;
        private ApplicationContext applicationContext;

        /**
         * Private constructor to enforce static factory method usage.
         */
        private Builder() {
        }

        /**
         * Sets the PayPal Plan ID.
         *
         * @param planId the plan ID (required, non-null, non-blank, max 128 chars)
         * @return this builder instance for method chaining
         */
        public Builder planId(@NotNull String planId) {
            this.planId = Objects.requireNonNull(planId, "planId must not be null");
            return this;
        }

        /**
         * Sets the shipping amount configuration.
         *
         * @param shippingAmount the optional shipping amount
         * @return this builder instance for method chaining
         */
        public Builder shippingAmount(ShippingAmount shippingAmount) {
            this.shippingAmount = shippingAmount;
            return this;
        }

        /**
         * Sets the application context configuration.
         *
         * @param applicationContext the application context (required)
         * @return this builder instance for method chaining
         */
        public Builder applicationContext(@NotNull ApplicationContext applicationContext) {
            this.applicationContext = Objects.requireNonNull(applicationContext, "applicationContext must not be null");
            return this;
        }

        /**
         * Builds the CreateSubscriptionRequest instance with all configured properties.
         * Validates that all required fields are set before construction.
         *
         * @return a new immutable CreateSubscriptionRequest instance
         * @throws IllegalStateException if required fields are missing or invalid
         */
        public CreateSubscriptionRequest build() {
            // Validate required fields
            if (planId == null || planId.trim().isEmpty()) {
                throw new IllegalStateException("planId must be set and not blank");
            }
            if (applicationContext == null) {
                throw new IllegalStateException("applicationContext must be set");
            }

            return new CreateSubscriptionRequest(planId, shippingAmount, applicationContext);
        }
    }

    /**
     * Represents the shipping amount configuration for a subscription.
     *
     * <p>Shipping amounts may be required for physical goods subscriptions
     * or may be null for digital-only subscriptions.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class ShippingAmount {

        /**
         * ISO 4217 currency code (e.g., USD, EUR, GBP).
         */
        @NotBlank(message = "currencyCode must not be blank")
        @Pattern(regexp = "^[A-Z]{3}$", message = "currencyCode must be a valid 3-letter ISO 4217 code")
        private final String currencyCode;

        /**
         * The shipping amount value as a decimal string.
         * Must be a valid positive decimal number.
         */
        @NotBlank(message = "value must not be blank")
        @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "value must be a valid decimal number with up to 2 decimal places")
        private final String value;

        /**
         * Constructs a new ShippingAmount with currency code and value.
         *
         * @param currencyCode the 3-letter ISO 4217 currency code (required, non-blank)
         * @param value        the shipping amount as decimal string (required, non-blank)
         */
        private ShippingAmount(String currencyCode, String value) {
            this.currencyCode = Objects.requireNonNull(currencyCode, "currencyCode must not be null");
            this.value = Objects.requireNonNull(value, "value must not be null");
        }

        /**
         * Creates a new ShippingAmount builder.
         *
         * @return a new Builder instance
         */
        public static Builder builder() {
            return new Builder();
        }

        /**
         * Returns the currency code.
         *
         * @return the currency code, never null
         */
        @JsonProperty("currency_code")
        public String getCurrencyCode() {
            return currencyCode;
        }

        /**
         * Returns the shipping amount value.
         *
         * @return the value, never null
         */
        @JsonProperty("value")
        public String getValue() {
            return value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ShippingAmount that = (ShippingAmount) o;
            return Objects.equals(currencyCode, that.currencyCode)
                    && Objects.equals(value, that.value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return Objects.hash(currencyCode, value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "ShippingAmount{"
                    + "currencyCode='"
                    + currencyCode
                    + '\''
                    + ", value='"
                    + value
                    + '\''
                    + '}';
        }

        /**
         * Builder for ShippingAmount.
         */
        public static final class Builder {

            private String currencyCode;
            private String value;

            /**
             * Sets the currency code.
             *
             * @param currencyCode the 3-letter ISO 4217 currency code
             * @return this builder instance for method chaining
             */
            public Builder currencyCode(@NotNull String currencyCode) {
                this.currencyCode = Objects.requireNonNull(currencyCode, "currencyCode must not be null");
                return this;
            }

            /**
             * Sets the value.
             *
             * @param value the decimal string value
             * @return this builder instance for method chaining
             */
            public Builder value(@NotNull String value) {
                this.value = Objects.requireNonNull(value, "value must not be null");
                return this;
            }

            /**
             * Builds the ShippingAmount instance.
             *
             * @return a new ShippingAmount instance
             * @throws IllegalStateException if required fields are missing
             */
            public ShippingAmount build() {
                if (currencyCode == null || currencyCode.trim().isEmpty()) {
                    throw new IllegalStateException("currencyCode must be set and not blank");
                }
                if (value == null || value.trim().isEmpty()) {
                    throw new IllegalStateException("value must be set and not blank");
                }
                return new ShippingAmount(currencyCode, value);
            }
        }
    }

    /**
     * Represents the application context for handling the PayPal payment flow.
     *
     * <p>This class configures the URLs where users should be redirected after
     * completing or cancelling the payment process.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class ApplicationContext {

        /**
         * URL where the user should be redirected after successful payment.
         * Must be a valid HTTP or HTTPS URL.
         */
        @NotBlank(message = "returnUrl must not be blank")
        @Pattern(regexp = "^https?://.+", message = "returnUrl must be a valid HTTP or HTTPS URL")
        private final String returnUrl;

        /**
         * URL where the user should be redirected after cancelling payment.
         * Must be a valid HTTP or HTTPS URL.
         */
        @NotBlank(message = "cancelUrl must not be blank")
        @Pattern(regexp = "^https?://.+", message = "cancelUrl must be a valid HTTP or HTTPS URL")
        private final String cancelUrl;

        /**
         * Constructs a new ApplicationContext with return and cancel URLs.
         *
         * @param returnUrl the return URL (required, non-blank, valid HTTP/HTTPS URL)
         * @param cancelUrl the cancel URL (required, non-blank, valid HTTP/HTTPS URL)
         */
        private ApplicationContext(String returnUrl, String cancelUrl) {
            this.returnUrl = Objects.requireNonNull(returnUrl, "returnUrl must not be null");
            this.cancelUrl = Objects.requireNonNull(cancelUrl, "cancelUrl must not be null");
        }

        /**
         * Creates a new ApplicationContext builder.
         *
         * @return a new Builder instance
         */
        public static Builder builder() {
            return new Builder();
        }

        /**
         * Returns the return URL.
         *
         * @return the return URL, never null
         */
        @JsonProperty("return_url")
        public String getReturnUrl() {
            return returnUrl;
        }

        /**
         * Returns the cancel URL.
         *
         * @return the cancel URL, never null
         */
        @JsonProperty("cancel_url")
        public String getCancelUrl() {
            return cancelUrl;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ApplicationContext that = (ApplicationContext) o;
            return Objects.equals(returnUrl, that.returnUrl)
                    && Objects.equals(cancelUrl, that.cancelUrl);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return Objects.hash(returnUrl, cancelUrl);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "ApplicationContext{"
                    + "returnUrl='"
                    + returnUrl
                    + '\''
                    + ", cancelUrl='"
                    + cancelUrl
                    + '\''
                    + '}';
        }

        /**
         * Builder for ApplicationContext.
         */
        public static final class Builder {

            private String returnUrl;
            private String cancelUrl;

            /**
             * Sets the return URL.
             *
             * @param returnUrl the return URL
             * @return this builder instance for method chaining
             */
            public Builder returnUrl(@NotNull String returnUrl) {
                this.returnUrl = Objects.requireNonNull(returnUrl, "returnUrl must not be null");
                return this;
            }

            /**
             * Sets the cancel URL.
             *
             * @param cancelUrl the cancel URL
             * @return this builder instance for method chaining
             */
            public Builder cancelUrl(@NotNull String cancelUrl) {
                this.cancelUrl = Objects.requireNonNull(cancelUrl, "cancelUrl must not be null");
                return this;
            }

            /**
             * Builds the ApplicationContext instance.
             *
             * @return a new ApplicationContext instance
             * @throws IllegalStateException if required fields are missing
             */
            public ApplicationContext build() {
                if (returnUrl == null || returnUrl.trim().isEmpty()) {
                    throw new IllegalStateException("returnUrl must be set and not blank");
                }
                if (cancelUrl == null || cancelUrl.trim().isEmpty()) {
                    throw new IllegalStateException("cancelUrl must be set and not blank");
                }
                return new ApplicationContext(returnUrl, cancelUrl);
            }
        }
    }
}
