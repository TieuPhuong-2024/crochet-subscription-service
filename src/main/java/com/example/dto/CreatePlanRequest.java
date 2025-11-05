package com.example.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating a PayPal billing plan.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class CreatePlanRequest {

    @NotBlank
    @JsonProperty("product_id")
    private final String productId;

    @NotBlank
    @Size(max = 127)
    private final String name;

    @NotBlank
    @Size(max = 24)
    private final String status;

    @Size(max = 127)
    private final String description;

    @NotNull
    @JsonProperty("billing_cycles")
    private final List<BillingCycle> billingCycles;

    @JsonProperty("quantity_supported")
    private final Boolean quantitySupported;

    @NotNull
    @JsonProperty("payment_preferences")
    private final PaymentPreferences paymentPreferences;

    @JsonProperty("merchant_preferences")
    private final MerchantPreferences merchantPreferences;

    private final Taxes taxes;

    private CreatePlanRequest(String productId, String name, String status, String description,
            List<BillingCycle> billingCycles, Boolean quantitySupported,
            PaymentPreferences paymentPreferences, MerchantPreferences merchantPreferences,
            Taxes taxes) {
        this.productId = productId;
        this.name = name;
        this.status = status;
        this.description = description;
        this.billingCycles = billingCycles;
        this.quantitySupported = quantitySupported;
        this.paymentPreferences = paymentPreferences;
        this.merchantPreferences = merchantPreferences;
        this.taxes = taxes;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters with JsonProperty annotations
    @JsonProperty("product_id")
    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    @JsonProperty("billing_cycles")
    public List<BillingCycle> getBillingCycles() {
        return billingCycles;
    }

    @JsonProperty("quantity_supported")
    public Boolean getQuantitySupported() {
        return quantitySupported;
    }

    @JsonProperty("payment_preferences")
    public PaymentPreferences getPaymentPreferences() {
        return paymentPreferences;
    }

    @JsonProperty("merchant_preferences")
    public MerchantPreferences getMerchantPreferences() {
        return merchantPreferences;
    }

    public Taxes getTaxes() {
        return taxes;
    }

    public static final class Builder {
        private String productId;
        private String name;
        private String status;
        private String description;
        private List<BillingCycle> billingCycles;
        private Boolean quantitySupported;
        private PaymentPreferences paymentPreferences;
        private MerchantPreferences merchantPreferences;
        private Taxes taxes;

        public Builder productId(String productId) {
            this.productId = productId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder billingCycles(List<BillingCycle> billingCycles) {
            this.billingCycles = billingCycles;
            return this;
        }

        public Builder quantitySupported(Boolean quantitySupported) {
            this.quantitySupported = quantitySupported;
            return this;
        }

        public Builder paymentPreferences(PaymentPreferences paymentPreferences) {
            this.paymentPreferences = paymentPreferences;
            return this;
        }

        public Builder merchantPreferences(MerchantPreferences merchantPreferences) {
            this.merchantPreferences = merchantPreferences;
            return this;
        }

        public Builder taxes(Taxes taxes) {
            this.taxes = taxes;
            return this;
        }

        public CreatePlanRequest build() {
            return new CreatePlanRequest(productId, name, status, description, billingCycles,
                    quantitySupported, paymentPreferences, merchantPreferences, taxes);
        }
    }

    // Nested classes for billing cycle, payment preferences, etc.
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class BillingCycle {
        @NotBlank
        @JsonProperty("tenure_type")
        private final String tenureType;

        @NotNull
        private final Integer sequence;

        private final Integer totalCycles;

        @NotNull
        @JsonProperty("pricing_scheme")
        private final PricingScheme pricingScheme;

        @NotNull
        private final Frequency frequency;

        private BillingCycle(String tenureType, Integer sequence, Integer totalCycles,
                PricingScheme pricingScheme, Frequency frequency) {
            this.tenureType = tenureType;
            this.sequence = sequence;
            this.totalCycles = totalCycles;
            this.pricingScheme = pricingScheme;
            this.frequency = frequency;
        }

        public static Builder builder() {
            return new Builder();
        }

        @JsonProperty("tenure_type")
        public String getTenureType() {
            return tenureType;
        }

        public Integer getSequence() {
            return sequence;
        }

        @JsonProperty("total_cycles")
        public Integer getTotalCycles() {
            return totalCycles;
        }

        @JsonProperty("pricing_scheme")
        public PricingScheme getPricingScheme() {
            return pricingScheme;
        }

        public Frequency getFrequency() {
            return frequency;
        }

        public static final class Builder {
            private String tenureType;
            private Integer sequence;
            private Integer totalCycles;
            private PricingScheme pricingScheme;
            private Frequency frequency;

            public Builder tenureType(String tenureType) {
                this.tenureType = tenureType;
                return this;
            }

            public Builder sequence(Integer sequence) {
                this.sequence = sequence;
                return this;
            }

            public Builder totalCycles(Integer totalCycles) {
                this.totalCycles = totalCycles;
                return this;
            }

            public Builder pricingScheme(PricingScheme pricingScheme) {
                this.pricingScheme = pricingScheme;
                return this;
            }

            public Builder frequency(Frequency frequency) {
                this.frequency = frequency;
                return this;
            }

            public BillingCycle build() {
                return new BillingCycle(tenureType, sequence, totalCycles, pricingScheme, frequency);
            }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class PricingScheme {
        private final FixedPrice fixedPrice;
        private final String pricingModel;
        private final List<Tier> tiers;

        private PricingScheme(FixedPrice fixedPrice, String pricingModel, List<Tier> tiers) {
            this.fixedPrice = fixedPrice;
            this.pricingModel = pricingModel;
            this.tiers = tiers;
        }

        public static Builder builder() {
            return new Builder();
        }

        @JsonProperty("fixed_price")
        public FixedPrice getFixedPrice() {
            return fixedPrice;
        }

        @JsonProperty("pricing_model")
        public String getPricingModel() {
            return pricingModel;
        }

        public List<Tier> getTiers() {
            return tiers;
        }

        public static final class Builder {
            private FixedPrice fixedPrice;
            private String pricingModel;
            private List<Tier> tiers;

            public Builder fixedPrice(FixedPrice fixedPrice) {
                this.fixedPrice = fixedPrice;
                return this;
            }

            public Builder pricingModel(String pricingModel) {
                this.pricingModel = pricingModel;
                return this;
            }

            public Builder tiers(List<Tier> tiers) {
                this.tiers = tiers;
                return this;
            }

            public PricingScheme build() {
                return new PricingScheme(fixedPrice, pricingModel, tiers);
            }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class FixedPrice {
        @NotBlank
        @JsonProperty("currency_code")
        private final String currencyCode;

        @NotBlank
        private final String value;

        private FixedPrice(String currencyCode, String value) {
            this.currencyCode = currencyCode;
            this.value = value;
        }

        public static Builder builder() {
            return new Builder();
        }

        @JsonProperty("currency_code")
        public String getCurrencyCode() {
            return currencyCode;
        }

        public String getValue() {
            return value;
        }

        public static final class Builder {
            private String currencyCode;
            private String value;

            public Builder currencyCode(String currencyCode) {
                this.currencyCode = currencyCode;
                return this;
            }

            public Builder value(String value) {
                this.value = value;
                return this;
            }

            public FixedPrice build() {
                return new FixedPrice(currencyCode, value);
            }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class Tier {
        @JsonProperty("starting_quantity")
        private final String startingQuantity;

        @JsonProperty("ending_quantity")
        private final String endingQuantity;

        @NotNull
        private final FixedPrice amount;

        private Tier(String startingQuantity, String endingQuantity, FixedPrice amount) {
            this.startingQuantity = startingQuantity;
            this.endingQuantity = endingQuantity;
            this.amount = amount;
        }

        public static Builder builder() {
            return new Builder();
        }

        @JsonProperty("starting_quantity")
        public String getStartingQuantity() {
            return startingQuantity;
        }

        @JsonProperty("ending_quantity")
        public String getEndingQuantity() {
            return endingQuantity;
        }

        public FixedPrice getAmount() {
            return amount;
        }

        public static final class Builder {
            private String startingQuantity;
            private String endingQuantity;
            private FixedPrice amount;

            public Builder startingQuantity(String startingQuantity) {
                this.startingQuantity = startingQuantity;
                return this;
            }

            public Builder endingQuantity(String endingQuantity) {
                this.endingQuantity = endingQuantity;
                return this;
            }

            public Builder amount(FixedPrice amount) {
                this.amount = amount;
                return this;
            }

            public Tier build() {
                return new Tier(startingQuantity, endingQuantity, amount);
            }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class Frequency {
        @NotBlank
        @JsonProperty("interval_unit")
        private final String intervalUnit;

        @NotNull
        @JsonProperty("interval_count")
        private final Integer intervalCount;

        private Frequency(String intervalUnit, Integer intervalCount) {
            this.intervalUnit = intervalUnit;
            this.intervalCount = intervalCount;
        }

        public static Builder builder() {
            return new Builder();
        }

        @JsonProperty("interval_unit")
        public String getIntervalUnit() {
            return intervalUnit;
        }

        @JsonProperty("interval_count")
        public Integer getIntervalCount() {
            return intervalCount;
        }

        public static final class Builder {
            private String intervalUnit;
            private Integer intervalCount;

            public Builder intervalUnit(String intervalUnit) {
                this.intervalUnit = intervalUnit;
                return this;
            }

            public Builder intervalCount(Integer intervalCount) {
                this.intervalCount = intervalCount;
                return this;
            }

            public Frequency build() {
                return new Frequency(intervalUnit, intervalCount);
            }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class PaymentPreferences {
        @JsonProperty("auto_bill_outstanding")
        private final Boolean autoBillOutstanding;

        @JsonProperty("setup_fee")
        private final FixedPrice setupFee;

        @JsonProperty("setup_fee_failure_action")
        private final String setupFeeFailureAction;

        @JsonProperty("payment_failure_threshold")
        private final Integer paymentFailureThreshold;

        private PaymentPreferences(Boolean autoBillOutstanding, FixedPrice setupFee,
                String setupFeeFailureAction, Integer paymentFailureThreshold) {
            this.autoBillOutstanding = autoBillOutstanding;
            this.setupFee = setupFee;
            this.setupFeeFailureAction = setupFeeFailureAction;
            this.paymentFailureThreshold = paymentFailureThreshold;
        }

        public static Builder builder() {
            return new Builder();
        }

        @JsonProperty("auto_bill_outstanding")
        public Boolean getAutoBillOutstanding() {
            return autoBillOutstanding;
        }

        @JsonProperty("setup_fee")
        public FixedPrice getSetupFee() {
            return setupFee;
        }

        @JsonProperty("setup_fee_failure_action")
        public String getSetupFeeFailureAction() {
            return setupFeeFailureAction;
        }

        @JsonProperty("payment_failure_threshold")
        public Integer getPaymentFailureThreshold() {
            return paymentFailureThreshold;
        }

        public static final class Builder {
            private Boolean autoBillOutstanding;
            private FixedPrice setupFee;
            private String setupFeeFailureAction;
            private Integer paymentFailureThreshold;

            public Builder autoBillOutstanding(Boolean autoBillOutstanding) {
                this.autoBillOutstanding = autoBillOutstanding;
                return this;
            }

            public Builder setupFee(FixedPrice setupFee) {
                this.setupFee = setupFee;
                return this;
            }

            public Builder setupFeeFailureAction(String setupFeeFailureAction) {
                this.setupFeeFailureAction = setupFeeFailureAction;
                return this;
            }

            public Builder paymentFailureThreshold(Integer paymentFailureThreshold) {
                this.paymentFailureThreshold = paymentFailureThreshold;
                return this;
            }

            public PaymentPreferences build() {
                return new PaymentPreferences(autoBillOutstanding, setupFee, setupFeeFailureAction,
                        paymentFailureThreshold);
            }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class MerchantPreferences {
        @JsonProperty("auto_bill_outstanding")
        private final Boolean autoBillOutstanding;

        @JsonProperty("setup_fee")
        private final FixedPrice setupFee;

        @JsonProperty("setup_fee_failure_action")
        private final String setupFeeFailureAction;

        @JsonProperty("payment_failure_threshold")
        private final Integer paymentFailureThreshold;

        private MerchantPreferences(Boolean autoBillOutstanding, FixedPrice setupFee,
                String setupFeeFailureAction, Integer paymentFailureThreshold) {
            this.autoBillOutstanding = autoBillOutstanding;
            this.setupFee = setupFee;
            this.setupFeeFailureAction = setupFeeFailureAction;
            this.paymentFailureThreshold = paymentFailureThreshold;
        }

        public static Builder builder() {
            return new Builder();
        }

        @JsonProperty("auto_bill_outstanding")
        public Boolean getAutoBillOutstanding() {
            return autoBillOutstanding;
        }

        @JsonProperty("setup_fee")
        public FixedPrice getSetupFee() {
            return setupFee;
        }

        @JsonProperty("setup_fee_failure_action")
        public String getSetupFeeFailureAction() {
            return setupFeeFailureAction;
        }

        @JsonProperty("payment_failure_threshold")
        public Integer getPaymentFailureThreshold() {
            return paymentFailureThreshold;
        }

        public static final class Builder {
            private Boolean autoBillOutstanding;
            private FixedPrice setupFee;
            private String setupFeeFailureAction;
            private Integer paymentFailureThreshold;

            public Builder autoBillOutstanding(Boolean autoBillOutstanding) {
                this.autoBillOutstanding = autoBillOutstanding;
                return this;
            }

            public Builder setupFee(FixedPrice setupFee) {
                this.setupFee = setupFee;
                return this;
            }

            public Builder setupFeeFailureAction(String setupFeeFailureAction) {
                this.setupFeeFailureAction = setupFeeFailureAction;
                return this;
            }

            public Builder paymentFailureThreshold(Integer paymentFailureThreshold) {
                this.paymentFailureThreshold = paymentFailureThreshold;
                return this;
            }

            public MerchantPreferences build() {
                return new MerchantPreferences(autoBillOutstanding, setupFee, setupFeeFailureAction,
                        paymentFailureThreshold);
            }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class Taxes {
        private final String percentage;
        private final Boolean inclusive;

        private Taxes(String percentage, Boolean inclusive) {
            this.percentage = percentage;
            this.inclusive = inclusive;
        }

        public static Builder builder() {
            return new Builder();
        }

        public String getPercentage() {
            return percentage;
        }

        public Boolean getInclusive() {
            return inclusive;
        }

        public static final class Builder {
            private String percentage;
            private Boolean inclusive;

            public Builder percentage(String percentage) {
                this.percentage = percentage;
                return this;
            }

            public Builder inclusive(Boolean inclusive) {
                this.inclusive = inclusive;
                return this;
            }

            public Taxes build() {
                return new Taxes(percentage, inclusive);
            }
        }
    }
}
