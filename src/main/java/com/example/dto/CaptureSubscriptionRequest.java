package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for capturing payment on a PayPal subscription.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class CaptureSubscriptionRequest {

    @NotBlank
    private final String note;

    @NotBlank
    @JsonProperty("capture_type")
    private final String captureType;

    @NotNull
    private final CreateSubscriptionRequest.ShippingAmount amount;

    public CaptureSubscriptionRequest(String note, String captureType, CreateSubscriptionRequest.ShippingAmount amount) {
        this.note = note;
        this.captureType = captureType;
        this.amount = amount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getNote() { return note; }

    @JsonProperty("capture_type")
    public String getCaptureType() { return captureType; }

    public CreateSubscriptionRequest.ShippingAmount getAmount() { return amount; }

    public static final class Builder {
        private String note;
        private String captureType;
        private CreateSubscriptionRequest.ShippingAmount amount;

        public Builder note(String note) {
            this.note = note;
            return this;
        }

        public Builder captureType(String captureType) {
            this.captureType = captureType;
            return this;
        }

        public Builder amount(CreateSubscriptionRequest.ShippingAmount amount) {
            this.amount = amount;
            return this;
        }

        public CaptureSubscriptionRequest build() {
            return new CaptureSubscriptionRequest(note, captureType, amount);
        }
    }
}
