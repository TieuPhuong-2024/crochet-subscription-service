package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * DTO for PayPal plan responses.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class PlanResponse {

    private final String id;

    @JsonProperty("product_id")
    private final String productId;

    private final String name;
    private final String status;
    private final String description;

    @JsonProperty("billing_cycles")
    private final List<CreatePlanRequest.BillingCycle> billingCycles;

    @JsonProperty("quantity_supported")
    private final Boolean quantitySupported;

    @JsonProperty("payment_preferences")
    private final CreatePlanRequest.PaymentPreferences paymentPreferences;

    @JsonProperty("merchant_preferences")
    private final CreatePlanRequest.MerchantPreferences merchantPreferences;

    private final CreatePlanRequest.Taxes taxes;

    @JsonProperty("create_time")
    private final String createTime;

    @JsonProperty("update_time")
    private final String updateTime;

    private final List<Link> links;

    public PlanResponse(String id, String productId, String name, String status, String description,
                       List<CreatePlanRequest.BillingCycle> billingCycles, Boolean quantitySupported,
                       CreatePlanRequest.PaymentPreferences paymentPreferences,
                       CreatePlanRequest.MerchantPreferences merchantPreferences,
                       CreatePlanRequest.Taxes taxes, String createTime, String updateTime, List<Link> links) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.status = status;
        this.description = description;
        this.billingCycles = billingCycles;
        this.quantitySupported = quantitySupported;
        this.paymentPreferences = paymentPreferences;
        this.merchantPreferences = merchantPreferences;
        this.taxes = taxes;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.links = links;
    }

    // Getters
    public String getId() { return id; }

    @JsonProperty("product_id")
    public String getProductId() { return productId; }

    public String getName() { return name; }

    public String getStatus() { return status; }

    public String getDescription() { return description; }

    @JsonProperty("billing_cycles")
    public List<CreatePlanRequest.BillingCycle> getBillingCycles() { return billingCycles; }

    @JsonProperty("quantity_supported")
    public Boolean getQuantitySupported() { return quantitySupported; }

    @JsonProperty("payment_preferences")
    public CreatePlanRequest.PaymentPreferences getPaymentPreferences() { return paymentPreferences; }

    @JsonProperty("merchant_preferences")
    public CreatePlanRequest.MerchantPreferences getMerchantPreferences() { return merchantPreferences; }

    public CreatePlanRequest.Taxes getTaxes() { return taxes; }

    @JsonProperty("create_time")
    public String getCreateTime() { return createTime; }

    @JsonProperty("update_time")
    public String getUpdateTime() { return updateTime; }

    public List<Link> getLinks() { return links; }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class Link {
        private final String href;
        private final String rel;
        private final String method;

        public Link(String href, String rel, String method) {
            this.href = href;
            this.rel = rel;
            this.method = method;
        }

        public String getHref() { return href; }
        public String getRel() { return rel; }
        public String getMethod() { return method; }
    }
}
