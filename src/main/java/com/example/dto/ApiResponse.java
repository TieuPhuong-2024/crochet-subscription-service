package com.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Enterprise-grade API response wrapper class that provides a consistent structure
 * for all API responses across the application.
 *
 * <p>This class is immutable and thread-safe, following enterprise best practices
 * for API response handling. It supports both successful and error responses with
 * comprehensive metadata.
 *
 * @param <T> the type of the response data payload
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ApiResponse<T> {

    /**
     * Standard success message constant.
     */
    public static final String DEFAULT_SUCCESS_MESSAGE = "Operation completed successfully";

    /**
     * Standard error message constant.
     */
    public static final String DEFAULT_ERROR_MESSAGE = "An error occurred while processing the request";

    /**
     * HTTP status indicator for success.
     */
    public static final boolean SUCCESS_STATUS = true;

    /**
     * HTTP status indicator for failure.
     */
    public static final boolean ERROR_STATUS = false;

    /**
     * Success status of the operation.
     * True indicates successful operation, false indicates failure.
     */
    @NotNull
    private final Boolean success;

    /**
     * Human-readable message describing the result of the operation.
     * Provides context about what happened during the operation.
     */
    @Size(max = 1000)
    private final String message;

    /**
     * The payload data returned by the operation.
     * Can be null for operations that don't return data (e.g., delete operations).
     */
    private final T data;

    /**
     * Optional error code for programmatic error handling.
     * Typically used on error responses to provide specific error identifiers
     * that clients can use for conditional error handling.
     */
    @Size(max = 100)
    private final String errorCode;

    /**
     * Timestamp when the response was generated.
     * Uses Instant for proper timezone handling and serialization to ISO-8601 format.
     */
    @NotNull
    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private final Instant timestamp;

    /**
     * Unique identifier for tracing requests across distributed systems.
     * Useful for debugging and monitoring. Can be null if not provided.
     */
    @Size(max = 100)
    private final String requestId;

    /**
     * Constructs a new ApiResponse with all fields.
     * This constructor is private to enforce use of the builder pattern.
     *
     * @param success   the success status (required, non-null)
     * @param message   the response message (required, max 1000 chars)
     * @param data      the response payload (optional)
     * @param errorCode the error code (optional, max 100 chars)
     * @param timestamp the response timestamp (required, non-null)
     * @param requestId the request tracing ID (optional, max 100 chars)
     */
    private ApiResponse(
            @NotNull Boolean success,
            @NotNull String message,
            T data,
            String errorCode,
            @NotNull Instant timestamp,
            String requestId) {
        this.success = Objects.requireNonNull(success, "success must not be null");
        this.message = Objects.requireNonNull(message, "message must not be null");
        this.data = data;
        this.errorCode = errorCode;
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp must not be null");
        this.requestId = requestId;
    }

    /**
     * Creates a successful response with data.
     *
     * @param <T>  the type of the data
     * @param data the response payload
     * @return a new successful ApiResponse instance
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(DEFAULT_SUCCESS_MESSAGE)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    /**
     * Creates a successful response with custom message and data.
     *
     * @param <T>     the type of the data
     * @param message the success message
     * @param data    the response payload
     * @return a new successful ApiResponse instance
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(Objects.requireNonNull(message, "message must not be null"))
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    /**
     * Creates a successful response with message only (no data).
     * Useful for operations that don't return a payload (e.g., create, update, delete).
     *
     * @param message the success message
     * @return a new successful ApiResponse instance with void data
     */
    public static ApiResponse<Void> success(String message) {
        return ApiResponse.<Void>builder()
                .success(true)
                .message(Objects.requireNonNull(message, "message must not be null"))
                .timestamp(Instant.now())
                .build();
    }

    /**
     * Creates an error response with message only.
     *
     * @param <T>     the type of the data (typically unused for errors)
     * @param message the error message
     * @return a new error ApiResponse instance
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(Objects.requireNonNull(message, "message must not be null"))
                .timestamp(Instant.now())
                .build();
    }

    /**
     * Creates an error response with message and error code.
     *
     * @param <T>       the type of the data (typically unused for errors)
     * @param message   the error message
     * @param errorCode the error code for programmatic handling
     * @return a new error ApiResponse instance
     */
    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(Objects.requireNonNull(message, "message must not be null"))
                .errorCode(Objects.requireNonNull(errorCode, "errorCode must not be null"))
                .timestamp(Instant.now())
                .build();
    }

    /**
     * Creates an error response with message, data, and error code.
     *
     * @param <T>       the type of the data
     * @param message   the error message
     * @param data      optional error-related data (e.g., validation errors, partial results)
     * @param errorCode the error code for programmatic handling
     * @return a new error ApiResponse instance
     */
    public static <T> ApiResponse<T> error(String message, T data, String errorCode) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(Objects.requireNonNull(message, "message must not be null"))
                .data(data)
                .errorCode(Objects.requireNonNull(errorCode, "errorCode must not be null"))
                .timestamp(Instant.now())
                .build();
    }

    /**
     * Creates a new builder for constructing ApiResponse instances.
     *
     * @param <T> the type of the response data
     * @return a new Builder instance
     */
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    /**
     * Returns the success status.
     *
     * @return true if the operation was successful, false otherwise
     */
    @JsonProperty("success")
    public boolean isSuccess() {
        return success;
    }

    /**
     * Returns the response message.
     *
     * @return the message, never null
     */
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    /**
     * Returns the response data payload.
     *
     * @return the data, may be null
     */
    @JsonProperty("data")
    public T getData() {
        return data;
    }

    /**
     * Returns an Optional containing the error code if present.
     * This is the recommended way to check for error codes to avoid NullPointerException.
     *
     * @return an Optional containing the error code if present
     */
    @JsonProperty("errorCode")
    public Optional<String> getErrorCode() {
        return Optional.ofNullable(errorCode);
    }

    /**
     * Returns the error code directly (for compatibility).
     * Consider using {@link #getErrorCode()} for safer Optional-based access.
     *
     * @return the error code, may be null
     */
    public String getRawErrorCode() {
        return errorCode;
    }

    /**
     * Returns the timestamp when this response was created.
     *
     * @return the timestamp, never null
     */
    @JsonProperty("timestamp")
    public Instant getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the request ID for tracing.
     *
     * @return the request ID, may be null
     */
    @JsonProperty("requestId")
    public Optional<String> getRequestId() {
        return Optional.ofNullable(requestId);
    }

    /**
     * Returns the request ID directly (for compatibility).
     *
     * @return the request ID, may be null
     */
    public String getRawRequestId() {
        return requestId;
    }

    /**
     * Returns true if this is a successful response.
     * This is an alias for {@link #isSuccess()} for better readability in conditional checks.
     *
     * @return true if successful, false otherwise
     */
    public boolean isError() {
        return !success;
    }

    /**
     * Checks if this response has an error code.
     *
     * @return true if an error code is present
     */
    public boolean hasErrorCode() {
        return errorCode != null;
    }

    /**
     * Checks if this response has a request ID.
     *
     * @return true if a request ID is present
     */
    public boolean hasRequestId() {
        return requestId != null;
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
        ApiResponse<?> that = (ApiResponse<?>) o;
        return Objects.equals(success, that.success)
                && Objects.equals(message, that.message)
                && Objects.equals(data, that.data)
                && Objects.equals(errorCode, that.errorCode)
                && Objects.equals(timestamp, that.timestamp)
                && Objects.equals(requestId, that.requestId);
    }

    /**
     * {@inheritDoc}
     *
     * @implNote The hash code is computed using all fields.
     */
    @Override
    public int hashCode() {
        return Objects.hash(success, message, data, errorCode, timestamp, requestId);
    }

    /**
     * {@inheritDoc}
     *
     * @implNote Returns a string representation suitable for logging and debugging.
     * The data field is excluded from the string representation to avoid circular
     * references and excessive output.
     */
    @Override
    public String toString() {
        return "ApiResponse{"
                + "success="
                + success
                + ", message='"
                + message
                + '\''
                + ", data="
                + (data != null ? "[" + data.getClass().getSimpleName() + "]" : "null")
                + ", errorCode='"
                + errorCode
                + '\''
                + ", timestamp="
                + timestamp
                + ", requestId='"
                + requestId
                + '\''
                + '}';
    }

    /**
     * Builder class for constructing ApiResponse instances in a fluent, type-safe manner.
     *
     * <p>This builder follows the builder pattern and provides:
     * <ul>
     *   <li>Type-safe construction with compile-time type checking</li>
     *   <li>Optional parameters with sensible defaults</li>
     *   <li>Validation of required fields before building</li>
     *   <li>Immutable configuration once built</li>
     * </ul>
     *
     * @param <T> the type of the response data
     */
    public static final class Builder<T> {

        private Boolean success;
        private String message;
        private T data;
        private String errorCode;
        private Instant timestamp;
        private String requestId;

        /**
         * Private constructor to enforce static factory method usage.
         */
        private Builder() {
        }

        /**
         * Sets the success status.
         *
         * @param success true for success, false for error
         * @return this builder instance for method chaining
         */
        public Builder<T> success(@NotNull Boolean success) {
            this.success = Objects.requireNonNull(success, "success must not be null");
            return this;
        }

        /**
         * Sets the response message.
         *
         * @param message the message (max 1000 characters)
         * @return this builder instance for method chaining
         */
        public Builder<T> message(@NotNull String message) {
            this.message = Objects.requireNonNull(message, "message must not be null");
            return this;
        }

        /**
         * Sets the response data payload.
         *
         * @param data the data payload
         * @return this builder instance for method chaining
         */
        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        /**
         * Sets the error code for error responses.
         *
         * @param errorCode the error code (max 100 characters)
         * @return this builder instance for method chaining
         */
        public Builder<T> errorCode(@NotNull String errorCode) {
            this.errorCode = Objects.requireNonNull(errorCode, "errorCode must not be null");
            return this;
        }

        /**
         * Sets the timestamp. If not set, defaults to current time when building.
         *
         * @param timestamp the timestamp (must be in the past or present)
         * @return this builder instance for method chaining
         */
        public Builder<T> timestamp(@NotNull Instant timestamp) {
            this.timestamp = Objects.requireNonNull(timestamp, "timestamp must not be null");
            return this;
        }

        /**
         * Sets the request ID for tracing requests across systems.
         *
         * @param requestId the request ID (max 100 characters)
         * @return this builder instance for method chaining
         */
        public Builder<T> requestId(@NotNull String requestId) {
            this.requestId = Objects.requireNonNull(requestId, "requestId must not be null");
            return this;
        }

        /**
         * Builds the ApiResponse instance with all configured properties.
         * Validates that all required fields are set before construction.
         *
         * @return a new immutable ApiResponse instance
         * @throws IllegalStateException if required fields are missing or invalid
         */
        public ApiResponse<T> build() {
            // Validate required fields
            if (success == null) {
                throw new IllegalStateException("success must be set before building");
            }
            if (message == null || message.trim().isEmpty()) {
                message = success ? DEFAULT_SUCCESS_MESSAGE : DEFAULT_ERROR_MESSAGE;
            }
            if (timestamp == null) {
                timestamp = Instant.now();
            }

            return new ApiResponse<>(success, message, data, errorCode, timestamp, requestId);
        }
    }
}
