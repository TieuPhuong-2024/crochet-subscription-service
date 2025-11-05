package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * DTO for updating pricing schemes on a PayPal plan.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class UpdatePricingRequest {

    @NotNull
    @JsonProperty("pricing_schemes")
    private final List<PricingSchemeUpdate> pricingSchemes;

    public UpdatePricingRequest(List<PricingSchemeUpdate> pricingSchemes) {
        this.pricingSchemes = pricingSchemes;
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonProperty("pricing_schemes")
    public List<PricingSchemeUpdate> getPricingSchemes() { return pricingSchemes; }

    public static final class Builder {
        private List<PricingSchemeUpdate> pricingSchemes;

        public Builder pricingSchemes(List<PricingSchemeUpdate> pricingSchemes) {
            this.pricingSchemes = pricingSchemes;
            return this;
        }

        public UpdatePricingRequest build() {
            return new UpdatePricingRequest(pricingSchemes);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class PricingSchemeUpdate {
        @NotNull
        @JsonProperty("billing_cycle_sequence")
        private final Integer billingCycleSequence;

        @NotNull
        @JsonProperty("pricing_scheme")
        private final CreatePlanRequest.PricingScheme pricingScheme;

        public PricingSchemeUpdate(Integer billingCycleSequence, CreatePlanRequest.PricingScheme pricingScheme) {
            this.billingCycleSequence = billingCycleSequence;
            this.pricingScheme = pricingScheme;
        }

        @JsonProperty("billing_cycle_sequence")
        public Integer getBillingCycleSequence() { return billingCycleSequence; }

        @JsonProperty("pricing_scheme")
        public CreatePlanRequest.PricingScheme getPricingScheme() { return pricingScheme; }

        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {
            private Integer billingCycleSequence;
            private CreatePlanRequest.PricingScheme pricingScheme;

            public Builder billingCycleSequence(Integer billingCycleSequence) {
                this.billingCycleSequence = billingCycleSequence;
                return this;
            }

            public Builder pricingScheme(CreatePlanRequest.PricingScheme pricingScheme) {
                this.pricingScheme = pricingScheme;
                return this;
            }

            public PricingSchemeUpdate build() {
                return new PricingSchemeUpdate(billingCycleSequence, pricingScheme);
            }
        }
    }
}
