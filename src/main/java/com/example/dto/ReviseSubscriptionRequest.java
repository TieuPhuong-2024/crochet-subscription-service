package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for revising PayPal subscriptions.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ReviseSubscriptionRequest {

    @JsonProperty("plan_id")
    private final String planId;

    private final String quantity;

    @JsonProperty("shipping_amount")
    private final CreateSubscriptionRequest.ShippingAmount shippingAmount;

    @JsonProperty("shipping_address")
    private final ShippingAddress shippingAddress;

    @JsonProperty("application_context")
    private final CreateSubscriptionRequest.ApplicationContext applicationContext;

    private final PlanOverride plan;

    public ReviseSubscriptionRequest(String planId, String quantity,
                                   CreateSubscriptionRequest.ShippingAmount shippingAmount,
                                   ShippingAddress shippingAddress,
                                   CreateSubscriptionRequest.ApplicationContext applicationContext,
                                   PlanOverride plan) {
        this.planId = planId;
        this.quantity = quantity;
        this.shippingAmount = shippingAmount;
        this.shippingAddress = shippingAddress;
        this.applicationContext = applicationContext;
        this.plan = plan;
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonProperty("plan_id")
    public String getPlanId() { return planId; }

    public String getQuantity() { return quantity; }

    @JsonProperty("shipping_amount")
    public CreateSubscriptionRequest.ShippingAmount getShippingAmount() { return shippingAmount; }

    @JsonProperty("shipping_address")
    public ShippingAddress getShippingAddress() { return shippingAddress; }

    @JsonProperty("application_context")
    public CreateSubscriptionRequest.ApplicationContext getApplicationContext() { return applicationContext; }

    public PlanOverride getPlan() { return plan; }

    public static final class Builder {
        private String planId;
        private String quantity;
        private CreateSubscriptionRequest.ShippingAmount shippingAmount;
        private ShippingAddress shippingAddress;
        private CreateSubscriptionRequest.ApplicationContext applicationContext;
        private PlanOverride plan;

        public Builder planId(String planId) {
            this.planId = planId;
            return this;
        }

        public Builder quantity(String quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder shippingAmount(CreateSubscriptionRequest.ShippingAmount shippingAmount) {
            this.shippingAmount = shippingAmount;
            return this;
        }

        public Builder shippingAddress(ShippingAddress shippingAddress) {
            this.shippingAddress = shippingAddress;
            return this;
        }

        public Builder applicationContext(CreateSubscriptionRequest.ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
            return this;
        }

        public Builder plan(PlanOverride plan) {
            this.plan = plan;
            return this;
        }

        public ReviseSubscriptionRequest build() {
            return new ReviseSubscriptionRequest(planId, quantity, shippingAmount, shippingAddress,
                    applicationContext, plan);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class ShippingAddress {
        private final Name name;
        private final Address address;

        public ShippingAddress(Name name, Address address) {
            this.name = name;
            this.address = address;
        }

        public static Builder builder() {
            return new Builder();
        }

        public Name getName() { return name; }
        public Address getAddress() { return address; }

        public static final class Builder {
            private Name name;
            private Address address;

            public Builder name(Name name) {
                this.name = name;
                return this;
            }

            public Builder address(Address address) {
                this.address = address;
                return this;
            }

            public ShippingAddress build() {
                return new ShippingAddress(name, address);
            }
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static final class Name {
            @JsonProperty("full_name")
            private final String fullName;

            public Name(String fullName) {
                this.fullName = fullName;
            }

            @JsonProperty("full_name")
            public String getFullName() { return fullName; }
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static final class Address {
            @JsonProperty("address_line_1")
            private final String addressLine1;

            @JsonProperty("address_line_2")
            private final String addressLine2;

            @JsonProperty("admin_area_2")
            private final String adminArea2;

            @JsonProperty("admin_area_1")
            private final String adminArea1;

            @JsonProperty("postal_code")
            private final String postalCode;

            @JsonProperty("country_code")
            private final String countryCode;

            public Address(String addressLine1, String addressLine2, String adminArea2,
                          String adminArea1, String postalCode, String countryCode) {
                this.addressLine1 = addressLine1;
                this.addressLine2 = addressLine2;
                this.adminArea2 = adminArea2;
                this.adminArea1 = adminArea1;
                this.postalCode = postalCode;
                this.countryCode = countryCode;
            }

            @JsonProperty("address_line_1")
            public String getAddressLine1() { return addressLine1; }

            @JsonProperty("address_line_2")
            public String getAddressLine2() { return addressLine2; }

            @JsonProperty("admin_area_2")
            public String getAdminArea2() { return adminArea2; }

            @JsonProperty("admin_area_1")
            public String getAdminArea1() { return adminArea1; }

            @JsonProperty("postal_code")
            public String getPostalCode() { return postalCode; }

            @JsonProperty("country_code")
            public String getCountryCode() { return countryCode; }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class PlanOverride {
        @JsonProperty("billing_cycles")
        private final java.util.List<CreatePlanRequest.BillingCycle> billingCycles;

        @JsonProperty("payment_preferences")
        private final CreatePlanRequest.PaymentPreferences paymentPreferences;

        private final CreatePlanRequest.Taxes taxes;

        public PlanOverride(java.util.List<CreatePlanRequest.BillingCycle> billingCycles,
                          CreatePlanRequest.PaymentPreferences paymentPreferences,
                          CreatePlanRequest.Taxes taxes) {
            this.billingCycles = billingCycles;
            this.paymentPreferences = paymentPreferences;
            this.taxes = taxes;
        }

        @JsonProperty("billing_cycles")
        public java.util.List<CreatePlanRequest.BillingCycle> getBillingCycles() { return billingCycles; }

        @JsonProperty("payment_preferences")
        public CreatePlanRequest.PaymentPreferences getPaymentPreferences() { return paymentPreferences; }

        public CreatePlanRequest.Taxes getTaxes() { return taxes; }
    }
}
