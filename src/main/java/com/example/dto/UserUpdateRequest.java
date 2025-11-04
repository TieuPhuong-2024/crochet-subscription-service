package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

/**
 * Enterprise-grade request DTO for updating user information.
 *
 * <p>This class encapsulates the data required to update a user's profile information,
 * including their ID (for identification), display name, and role assignment.
 * It supports partial updates where only the fields that need to be changed
 * should be provided.
 *
 * <p>This class is immutable and thread-safe, ensuring consistent state
 * throughout the user update process.
 *
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class UserUpdateRequest {

    /**
     * The unique identifier of the user to update.
     * This field is required and must match an existing user in the system.
     */
    @NotNull(message = "id must not be null")
    @NotBlank(message = "id must not be blank")
    @Size(max = 255, message = "id must not exceed 255 characters")
    private final String id;

    /**
     * The user's display name.
     * This field is optional and will only be updated if provided (non-null).
     */
    @Size(max = 255, message = "name must not exceed 255 characters")
    private final String name;

    /**
     * The user's role assignment.
     * This field is optional and will only be updated if provided (non-null).
     * Common roles might include: USER, VIP_USER, ADMIN, MODERATOR, etc.
     */
    @Size(max = 100, message = "role must not exceed 100 characters")
    private final String role;

    /**
     * Constructs a new UserUpdateRequest with all fields.
     * This constructor is private to enforce use of the builder pattern.
     *
     * @param id the user ID (required, non-null, non-blank, max 255 chars)
     * @param name the optional display name (max 255 chars)
     * @param role the optional role assignment (max 100 chars)
     */
    private UserUpdateRequest(String id, String name, String role) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.name = name;
        this.role = role;
    }

    /**
     * Creates a new builder for constructing UserUpdateRequest instances.
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Returns the user ID.
     *
     * @return the user ID, never null
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the display name if present.
     *
     * @return the name, may be null
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the role assignment if present.
     *
     * @return the role, may be null
     */
    public String getRole() {
        return role;
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
        UserUpdateRequest that = (UserUpdateRequest) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(role, that.role);
    }

    /**
     * {@inheritDoc}
     *
     * @implNote The hash code is computed using all fields.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, role);
    }

    /**
     * {@inheritDoc}
     *
     * @implNote Returns a string representation suitable for logging and debugging.
     * The role field is included but name is masked for privacy.
     */
    @Override
    public String toString() {
        return "UserUpdateRequest{"
                + "id='"
                + id
                + '\''
                + ", name="
                + (name != null ? "[PROTECTED]" : "null")
                + ", role='"
                + role
                + '\''
                + '}';
    }

    /**
     * Builder class for constructing UserUpdateRequest instances in a fluent manner.
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
        private String name;
        private String role;

        /**
         * Private constructor to enforce static factory method usage.
         */
        private Builder() {
        }

        /**
         * Sets the user ID.
         *
         * @param id the user ID (required, non-null, non-blank, max 255 chars)
         * @return this builder instance for method chaining
         */
        public Builder id(@NotNull String id) {
            this.id = Objects.requireNonNull(id, "id must not be null");
            return this;
        }

        /**
         * Sets the display name.
         *
         * @param name the optional display name (max 255 chars)
         * @return this builder instance for method chaining
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the role assignment.
         *
         * @param role the optional role assignment (max 100 chars)
         * @return this builder instance for method chaining
         */
        public Builder role(String role) {
            this.role = role;
            return this;
        }

        /**
         * Builds the UserUpdateRequest instance with all configured properties.
         * Validates that all required fields are set before construction.
         *
         * @return a new immutable UserUpdateRequest instance
         * @throws IllegalStateException if required fields are missing or invalid
         */
        public UserUpdateRequest build() {
            // Validate required fields
            if (id == null || id.trim().isEmpty()) {
                throw new IllegalStateException("id must be set and not blank");
            }

            return new UserUpdateRequest(id, name, role);
        }
    }
}
