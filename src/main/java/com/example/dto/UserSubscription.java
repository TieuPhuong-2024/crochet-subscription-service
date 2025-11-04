package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * Enterprise-grade response DTO representing a user's subscription information.
 *
 * <p>This class encapsulates all relevant subscription data that is exposed to clients,
 * including subscription identifiers, plan details, status, and timing information.
 * It provides a comprehensive view of both the local subscription record and
 * PayPal subscription details.
 *
 * <p>This class is immutable and thread-safe, ensuring consistent subscription
 * information is presented to clients.
 *
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class UserSubscription {

    /**
     * ISO 8601 date-time formatter for consistent timestamp formatting.
     */
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneOffset.UTC);

    /**
     * The local database ID for this subscription record.
     * This is the primary key in the local subscription table.
     */
    @NotNull(message = "id must not be null")
    @NotBlank(message = "id must not be blank")
    @Size(max = 255, message = "id must not exceed 255 characters")
    private final String id;

    /**
     * The unique identifier of the user who owns this subscription.
     * This ID links the subscription to a specific user account.
     */
    @NotNull(message = "userId must not be null")
    @NotBlank(message = "userId must not be blank")
    @Size(max = 255, message = "userId must not exceed 255 characters")
    @JsonProperty("user_id")
    private final String userId;

    /**
     * The PayPal Plan ID that defines the subscription terms.
     * This ID corresponds to the subscription plan in PayPal.
     */
    @NotNull(message = "planId must not be null")
    @NotBlank(message = "planId must not be blank")
    @Size(max = 128, message = "planId must not exceed 128 characters")
    @JsonProperty("plan_id")
    private final String planId;

    /**
     * The PayPal subscription ID for tracking in PayPal's system.
     * This ID is used for all PayPal API interactions related to this subscription.
     */
    @NotNull(message = "paypalSubscriptionId must not be null")
    @NotBlank(message = "paypalSubscriptionId must not be blank")
    @Size(max = 255, message = "paypalSubscriptionId must not exceed 255 characters")
    @JsonProperty("paypal_subscription_id")
    private final String paypalSubscriptionId;

    /**
     * The current status of the subscription.
     * Common values include: "active", "pending", "cancelled", "expired", "suspended".
     */
    @NotNull(message = "status must not be null")
    @NotBlank(message = "status must not be blank")
    @Size(max = 50, message = "status must not exceed 50 characters")
    private final String status;

    /**
     * The subscription start date in ISO 8601 format (yyyy-MM-dd'T'HH:mm:ss'Z').
     * Represents when the subscription became active.
     */
    @JsonProperty("start_date")
    private final String startDate;

    /**
     * The subscription end date in ISO 8601 format (yyyy-MM-dd'T'HH:mm:ss'Z').
     * May be null for active subscriptions without a fixed end date.
     */
    @JsonProperty("end_date")
    private final String endDate;

    /**
     * The subscription creation timestamp in ISO 8601 format (yyyy-MM-dd'T'HH:mm:ss'Z').
     * Represents when the subscription was first created in the system.
     */
    @JsonProperty("created_at")
    private final String createdAt;

    /**
     * The last update timestamp in ISO 8601 format (yyyy-MM-dd'T'HH:mm:ss'Z').
     * Represents when the subscription was last modified.
     */
    @JsonProperty("updated_at")
    private final String updatedAt;

    /**
     * Constructs a new UserSubscription with all fields.
     * This constructor is private to enforce use of the builder pattern.
     *
     * @param id the local subscription ID (required, non-null, non-blank)
     * @param userId the user ID (required, non-null, non-blank)
     * @param planId the PayPal Plan ID (required, non-null, non-blank)
     * @param paypalSubscriptionId the PayPal subscription ID (required, non-null, non-blank)
     * @param status the subscription status (required, non-null, non-blank)
     * @param startDate the start date as ISO 8601 string (optional)
     * @param endDate the end date as ISO 8601 string (optional)
     * @param createdAt the creation timestamp as ISO 8601 string (optional)
     * @param updatedAt the update timestamp as ISO 8601 string (optional)
     */
    private UserSubscription(
            String id,
            String userId,
            String planId,
            String paypalSubscriptionId,
            String status,
            String startDate,
            String endDate,
            String createdAt,
            String updatedAt) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.userId = Objects.requireNonNull(userId, "userId must not be null");
        this.planId = Objects.requireNonNull(planId, "planId must not be null");
        this.paypalSubscriptionId = Objects.requireNonNull(paypalSubscriptionId, "paypalSubscriptionId must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Creates a new builder for constructing UserSubscription instances.
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a new UserSubscription from Instant timestamps.
     * This is a convenience factory method that handles Instant to String conversion.
     *
     * @param id the local subscription ID
     * @param userId the user ID
     * @param planId the PayPal Plan ID
     * @param paypalSubscriptionId the PayPal subscription ID
     * @param status the subscription status
     * @param startTime the start time as Instant (optional)
     * @param createdAtTime the creation time as Instant (optional)
     * @param updatedAtTime the update time as Instant (optional)
     * @return a new UserSubscription instance
     */
    public static UserSubscription of(
            String id,
            String userId,
            String planId,
            String paypalSubscriptionId,
            String status,
            Instant startTime,
            Instant createdAtTime,
            Instant updatedAtTime) {
        return UserSubscription.builder()
                .id(id)
                .userId(userId)
                .planId(planId)
                .paypalSubscriptionId(paypalSubscriptionId)
                .status(status)
                .startDate(startTime != null ? TIMESTAMP_FORMATTER.format(startTime) : null)
                .createdAt(createdAtTime != null ? TIMESTAMP_FORMATTER.format(createdAtTime) : null)
                .updatedAt(updatedAtTime != null ? TIMESTAMP_FORMATTER.format(updatedAtTime) : null)
                .build();
    }

    /**
     * Returns the local subscription ID.
     *
     * @return the subscription ID, never null
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the user ID.
     *
     * @return the user ID, never null
     */
    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
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
     * Returns the PayPal subscription ID.
     *
     * @return the PayPal subscription ID, never null
     */
    @JsonProperty("paypal_subscription_id")
    public String getPaypalSubscriptionId() {
        return paypalSubscriptionId;
    }

    /**
     * Returns the subscription status.
     *
     * @return the status, never null
     */
    public String getStatus() {
        return status;
    }

    /**
     * Returns the start date.
     *
     * @return the start date, may be null
     */
    @JsonProperty("start_date")
    public String getStartDate() {
        return startDate;
    }

    /**
     * Returns the end date.
     *
     * @return the end date, may be null
     */
    @JsonProperty("end_date")
    public String getEndDate() {
        return endDate;
    }

    /**
     * Returns the creation timestamp.
     *
     * @return the creation timestamp, may be null
     */
    @JsonProperty("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the update timestamp.
     *
     * @return the update timestamp, may be null
     */
    @JsonProperty("updated_at")
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Checks if the subscription is active.
     *
     * @return true if status is "active"
     */
    public boolean isActive() {
        return "active".equalsIgnoreCase(status);
    }

    /**
     * Checks if the subscription is pending (approval or activation).
     *
     * @return true if status indicates pending state
     */
    public boolean isPending() {
        return "pending".equalsIgnoreCase(status);
    }

    /**
     * Checks if the subscription is cancelled.
     *
     * @return true if status is "cancelled"
     */
    public boolean isCancelled() {
        return "cancelled".equalsIgnoreCase(status);
    }

    /**
     * Checks if the subscription is expired.
     *
     * @return true if status is "expired"
     */
    public boolean isExpired() {
        return "expired".equalsIgnoreCase(status);
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
        UserSubscription that = (UserSubscription) o;
        return Objects.equals(id, that.id)
                && Objects.equals(userId, that.userId)
                && Objects.equals(planId, that.planId)
                && Objects.equals(paypalSubscriptionId, that.paypalSubscriptionId)
                && Objects.equals(status, that.status)
                && Objects.equals(startDate, that.startDate)
                && Objects.equals(endDate, that.endDate)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(updatedAt, that.updatedAt);
    }

    /**
     * {@inheritDoc}
     *
     * @implNote The hash code is computed using all fields.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, userId, planId, paypalSubscriptionId, status, startDate, endDate, createdAt, updatedAt);
    }

    /**
     * {@inheritDoc}
     *
     * @implNote Returns a string representation suitable for logging and debugging.
     * Sensitive fields like subscription IDs are partially masked.
     */
    @Override
    public String toString() {
        return "UserSubscription{"
                + "id='"
                + mask(id)
                + '\''
                + ", userId='"
                + mask(userId)
                + '\''
                + ", planId='"
                + planId
                + '\''
                + ", paypalSubscriptionId='"
                + mask(paypalSubscriptionId)
                + '\''
                + ", status='"
                + status
                + '\''
                + ", startDate="
                + startDate
                + ", endDate="
                + endDate
                + ", createdAt="
                + createdAt
                + ", updatedAt="
                + updatedAt
                + '}';
    }

    /**
     * Masks sensitive identifiers for safe logging.
     *
     * @param value the value to mask
     * @return masked value showing only first and last 4 characters
     */
    private String mask(String value) {
        if (value == null || value.length() <= 8) {
            return "***";
        }
        return value.substring(0, 4) + "..." + value.substring(value.length() - 4);
    }

    /**
     * Builder class for constructing UserSubscription instances in a fluent manner.
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

        private String id;
        private String userId;
        private String planId;
        private String paypalSubscriptionId;
        private String status;
        private String startDate;
        private String endDate;
        private String createdAt;
        private String updatedAt;

        /**
         * Private constructor to enforce static factory method usage.
         */
        private Builder() {
        }

        /**
         * Sets the local subscription ID.
         *
         * @param id the subscription ID (required, non-null, non-blank)
         * @return this builder instance for method chaining
         */
        public Builder id(@NotNull String id) {
            this.id = Objects.requireNonNull(id, "id must not be null");
            return this;
        }

        /**
         * Sets the user ID.
         *
         * @param userId the user ID (required, non-null, non-blank)
         * @return this builder instance for method chaining
         */
        public Builder userId(@NotNull String userId) {
            this.userId = Objects.requireNonNull(userId, "userId must not be null");
            return this;
        }

        /**
         * Sets the PayPal Plan ID.
         *
         * @param planId the plan ID (required, non-null, non-blank)
         * @return this builder instance for method chaining
         */
        public Builder planId(@NotNull String planId) {
            this.planId = Objects.requireNonNull(planId, "planId must not be null");
            return this;
        }

        /**
         * Sets the PayPal subscription ID.
         *
         * @param paypalSubscriptionId the PayPal subscription ID (required, non-null, non-blank)
         * @return this builder instance for method chaining
         */
        public Builder paypalSubscriptionId(@NotNull String paypalSubscriptionId) {
            this.paypalSubscriptionId = Objects.requireNonNull(paypalSubscriptionId, "paypalSubscriptionId must not be null");
            return this;
        }

        /**
         * Sets the subscription status.
         *
         * @param status the status (required, non-null, non-blank)
         * @return this builder instance for method chaining
         */
        public Builder status(@NotNull String status) {
            this.status = Objects.requireNonNull(status, "status must not be null");
            return this;
        }

        /**
         * Sets the start date.
         *
         * @param startDate the start date as ISO 8601 string
         * @return this builder instance for method chaining
         */
        public Builder startDate(String startDate) {
            this.startDate = startDate;
            return this;
        }

        /**
         * Sets the end date.
         *
         * @param endDate the end date as ISO 8601 string
         * @return this builder instance for method chaining
         */
        public Builder endDate(String endDate) {
            this.endDate = endDate;
            return this;
        }

        /**
         * Sets the creation timestamp.
         *
         * @param createdAt the creation timestamp as ISO 8601 string
         * @return this builder instance for method chaining
         */
        public Builder createdAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        /**
         * Sets the update timestamp.
         *
         * @param updatedAt the update timestamp as ISO 8601 string
         * @return this builder instance for method chaining
         */
        public Builder updatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        /**
         * Builds the UserSubscription instance with all configured properties.
         * Validates that all required fields are set before construction.
         *
         * @return a new immutable UserSubscription instance
         * @throws IllegalStateException if required fields are missing or invalid
         */
        public UserSubscription build() {
            // Validate required fields
            if (id == null || id.trim().isEmpty()) {
                throw new IllegalStateException("id must be set and not blank");
            }
            if (userId == null || userId.trim().isEmpty()) {
                throw new IllegalStateException("userId must be set and not blank");
            }
            if (planId == null || planId.trim().isEmpty()) {
                throw new IllegalStateException("planId must be set and not blank");
            }
            if (paypalSubscriptionId == null || paypalSubscriptionId.trim().isEmpty()) {
                throw new IllegalStateException("paypalSubscriptionId must be set and not blank");
            }
            if (status == null || status.trim().isEmpty()) {
                throw new IllegalStateException("status must be set and not blank");
            }

            return new UserSubscription(id, userId, planId, paypalSubscriptionId, status, startDate, endDate, createdAt, updatedAt);
        }
    }
}
