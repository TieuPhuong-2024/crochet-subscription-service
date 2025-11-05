package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * DTO for PayPal subscription responses.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class SubscriptionResponse {

    private final String id;
    private final String status;

    @JsonProperty("status_change_note")
    private final String statusChangeNote;

    @JsonProperty("status_update_time")
    private final String statusUpdateTime;

    @JsonProperty("plan_id")
    private final String planId;

    @JsonProperty("plan_overridden")
    private final Boolean planOverridden;

    @JsonProperty("start_time")
    private final String startTime;

    private final String quantity;

    @JsonProperty("shipping_amount")
    private final CreateSubscriptionRequest.ShippingAmount shippingAmount;

    private final Subscriber subscriber;

    @JsonProperty("billing_info")
    private final BillingInfo billingInfo;

    @JsonProperty("create_time")
    private final String createTime;

    @JsonProperty("update_time")
    private final String updateTime;

    private final List<PlanResponse.Link> links;

    public SubscriptionResponse(String id, String status, String statusChangeNote, String statusUpdateTime,
                              String planId, Boolean planOverridden, String startTime, String quantity,
                              CreateSubscriptionRequest.ShippingAmount shippingAmount, Subscriber subscriber,
                              BillingInfo billingInfo, String createTime, String updateTime, List<PlanResponse.Link> links) {
        this.id = id;
        this.status = status;
        this.statusChangeNote = statusChangeNote;
        this.statusUpdateTime = statusUpdateTime;
        this.planId = planId;
        this.planOverridden = planOverridden;
        this.startTime = startTime;
        this.quantity = quantity;
        this.shippingAmount = shippingAmount;
        this.subscriber = subscriber;
        this.billingInfo = billingInfo;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.links = links;
    }

    // Getters
    public String getId() { return id; }
    public String getStatus() { return status; }

    @JsonProperty("status_change_note")
    public String getStatusChangeNote() { return statusChangeNote; }

    @JsonProperty("status_update_time")
    public String getStatusUpdateTime() { return statusUpdateTime; }

    @JsonProperty("plan_id")
    public String getPlanId() { return planId; }

    @JsonProperty("plan_overridden")
    public Boolean getPlanOverridden() { return planOverridden; }

    @JsonProperty("start_time")
    public String getStartTime() { return startTime; }

    public String getQuantity() { return quantity; }

    @JsonProperty("shipping_amount")
    public CreateSubscriptionRequest.ShippingAmount getShippingAmount() { return shippingAmount; }

    public Subscriber getSubscriber() { return subscriber; }

    @JsonProperty("billing_info")
    public BillingInfo getBillingInfo() { return billingInfo; }

    @JsonProperty("create_time")
    public String getCreateTime() { return createTime; }

    @JsonProperty("update_time")
    public String getUpdateTime() { return updateTime; }

    public List<PlanResponse.Link> getLinks() { return links; }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class Subscriber {
        @JsonProperty("email_address")
        private final String emailAddress;

        private final Name name;
        private final String tenant;

        @JsonProperty("shipping_address")
        private final ReviseSubscriptionRequest.ShippingAddress shippingAddress;

        @JsonProperty("payment_source")
        private final PaymentSource paymentSource;

        @JsonProperty("payer_id")
        private final String payerId;

        public Subscriber(String emailAddress, Name name, String tenant,
                         ReviseSubscriptionRequest.ShippingAddress shippingAddress,
                         PaymentSource paymentSource, String payerId) {
            this.emailAddress = emailAddress;
            this.name = name;
            this.tenant = tenant;
            this.shippingAddress = shippingAddress;
            this.paymentSource = paymentSource;
            this.payerId = payerId;
        }

        @JsonProperty("email_address")
        public String getEmailAddress() { return emailAddress; }

        public Name getName() { return name; }
        public String getTenant() { return tenant; }

        @JsonProperty("shipping_address")
        public ReviseSubscriptionRequest.ShippingAddress getShippingAddress() { return shippingAddress; }

        @JsonProperty("payment_source")
        public PaymentSource getPaymentSource() { return paymentSource; }

        @JsonProperty("payer_id")
        public String getPayerId() { return payerId; }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static final class Name {
            @JsonProperty("given_name")
            private final String givenName;

            @JsonProperty("surname")
            private final String surname;

            public Name(String givenName, String surname) {
                this.givenName = givenName;
                this.surname = surname;
            }

            @JsonProperty("given_name")
            public String getGivenName() { return givenName; }

            @JsonProperty("surname")
            public String getSurname() { return surname; }
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static final class PaymentSource {
            private final Card card;

            public PaymentSource(Card card) {
                this.card = card;
            }

            public Card getCard() { return card; }

            @JsonInclude(JsonInclude.Include.NON_NULL)
            public static final class Card {
                private final Name name;
                @JsonProperty("last_digits")
                private final String lastDigits;
                private final String brand;
                private final List<String> availableNetworks;
                private final Attributes attributes;
                private final String expiry;

                public Card(Name name, String lastDigits, String brand, List<String> availableNetworks,
                           Attributes attributes, String expiry) {
                    this.name = name;
                    this.lastDigits = lastDigits;
                    this.brand = brand;
                    this.availableNetworks = availableNetworks;
                    this.attributes = attributes;
                    this.expiry = expiry;
                }

                public Name getName() { return name; }

                @JsonProperty("last_digits")
                public String getLastDigits() { return lastDigits; }

                public String getBrand() { return brand; }

                @JsonProperty("available_networks")
                public List<String> getAvailableNetworks() { return availableNetworks; }

                public Attributes getAttributes() { return attributes; }
                public String getExpiry() { return expiry; }

                @JsonInclude(JsonInclude.Include.NON_NULL)
                public static final class Attributes {
                    private final Vault vault;

                    public Attributes(Vault vault) {
                        this.vault = vault;
                    }

                    public Vault getVault() { return vault; }

                    @JsonInclude(JsonInclude.Include.NON_NULL)
                    public static final class Vault {
                        private final String id;
                        private final Customer customer;

                        public Vault(String id, Customer customer) {
                            this.id = id;
                            this.customer = customer;
                        }

                        public String getId() { return id; }
                        public Customer getCustomer() { return customer; }

                        @JsonInclude(JsonInclude.Include.NON_NULL)
                        public static final class Customer {
                            private final String id;

                            public Customer(String id) {
                                this.id = id;
                            }

                            public String getId() { return id; }
                        }
                    }
                }
            }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class BillingInfo {
        @JsonProperty("outstanding_balance")
        private final CreateSubscriptionRequest.ShippingAmount outstandingBalance;

        @JsonProperty("cycle_executions")
        private final List<CycleExecution> cycleExecutions;

        @JsonProperty("last_payment")
        private final Payment lastPayment;

        @JsonProperty("next_billing_time")
        private final String nextBillingTime;

        @JsonProperty("final_payment_time")
        private final String finalPaymentTime;

        @JsonProperty("failed_payments_count")
        private final Integer failedPaymentsCount;

        public BillingInfo(CreateSubscriptionRequest.ShippingAmount outstandingBalance,
                          List<CycleExecution> cycleExecutions, Payment lastPayment,
                          String nextBillingTime, String finalPaymentTime, Integer failedPaymentsCount) {
            this.outstandingBalance = outstandingBalance;
            this.cycleExecutions = cycleExecutions;
            this.lastPayment = lastPayment;
            this.nextBillingTime = nextBillingTime;
            this.finalPaymentTime = finalPaymentTime;
            this.failedPaymentsCount = failedPaymentsCount;
        }

        @JsonProperty("outstanding_balance")
        public CreateSubscriptionRequest.ShippingAmount getOutstandingBalance() { return outstandingBalance; }

        @JsonProperty("cycle_executions")
        public List<CycleExecution> getCycleExecutions() { return cycleExecutions; }

        @JsonProperty("last_payment")
        public Payment getLastPayment() { return lastPayment; }

        @JsonProperty("next_billing_time")
        public String getNextBillingTime() { return nextBillingTime; }

        @JsonProperty("final_payment_time")
        public String getFinalPaymentTime() { return finalPaymentTime; }

        @JsonProperty("failed_payments_count")
        public Integer getFailedPaymentsCount() { return failedPaymentsCount; }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static final class CycleExecution {
            @JsonProperty("tenure_type")
            private final String tenureType;

            private final Integer sequence;

            @JsonProperty("cycles_completed")
            private final Integer cyclesCompleted;

            @JsonProperty("cycles_remaining")
            private final Integer cyclesRemaining;

            @JsonProperty("current_pricing_scheme_version")
            private final Integer currentPricingSchemeVersion;

            @JsonProperty("total_cycles")
            private final Integer totalCycles;

            public CycleExecution(String tenureType, Integer sequence, Integer cyclesCompleted,
                                Integer cyclesRemaining, Integer currentPricingSchemeVersion, Integer totalCycles) {
                this.tenureType = tenureType;
                this.sequence = sequence;
                this.cyclesCompleted = cyclesCompleted;
                this.cyclesRemaining = cyclesRemaining;
                this.currentPricingSchemeVersion = currentPricingSchemeVersion;
                this.totalCycles = totalCycles;
            }

            @JsonProperty("tenure_type")
            public String getTenureType() { return tenureType; }

            public Integer getSequence() { return sequence; }

            @JsonProperty("cycles_completed")
            public Integer getCyclesCompleted() { return cyclesCompleted; }

            @JsonProperty("cycles_remaining")
            public Integer getCyclesRemaining() { return cyclesRemaining; }

            @JsonProperty("current_pricing_scheme_version")
            public Integer getCurrentPricingSchemeVersion() { return currentPricingSchemeVersion; }

            @JsonProperty("total_cycles")
            public Integer getTotalCycles() { return totalCycles; }
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static final class Payment {
            private final CreateSubscriptionRequest.ShippingAmount amount;
            private final String time;

            public Payment(CreateSubscriptionRequest.ShippingAmount amount, String time) {
                this.amount = amount;
                this.time = time;
            }

            public CreateSubscriptionRequest.ShippingAmount getAmount() { return amount; }
            public String getTime() { return time; }
        }
    }
}
