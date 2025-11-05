package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO for subscription action requests (suspend, cancel, activate).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class SubscriptionActionRequest {

    private final String reason;

    public SubscriptionActionRequest(String reason) {
        this.reason = reason;
    }

    public String getReason() { return reason; }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String reason;

        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public SubscriptionActionRequest build() {
            return new SubscriptionActionRequest(reason);
        }
    }
}
