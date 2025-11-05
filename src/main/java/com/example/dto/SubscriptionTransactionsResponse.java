package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * DTO for PayPal subscription transactions response.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class SubscriptionTransactionsResponse {

    private final List<Transaction> transactions;
    private final List<PlanResponse.Link> links;

    public SubscriptionTransactionsResponse(List<Transaction> transactions, List<PlanResponse.Link> links) {
        this.transactions = transactions;
        this.links = links;
    }

    public List<Transaction> getTransactions() { return transactions; }
    public List<PlanResponse.Link> getLinks() { return links; }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class Transaction {
        private final String id;
        private final String status;

        @JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
        @com.fasterxml.jackson.annotation.JsonProperty("payer_email")
        private final String payerEmail;

        @JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
        @com.fasterxml.jackson.annotation.JsonProperty("payer_name")
        private final PayerName payerName;

        @JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
        @com.fasterxml.jackson.annotation.JsonProperty("amount_with_breakdown")
        private final AmountWithBreakdown amountWithBreakdown;

        private final String time;

        public Transaction(String id, String status, String payerEmail, PayerName payerName,
                          AmountWithBreakdown amountWithBreakdown, String time) {
            this.id = id;
            this.status = status;
            this.payerEmail = payerEmail;
            this.payerName = payerName;
            this.amountWithBreakdown = amountWithBreakdown;
            this.time = time;
        }

        public String getId() { return id; }
        public String getStatus() { return status; }

        @com.fasterxml.jackson.annotation.JsonProperty("payer_email")
        public String getPayerEmail() { return payerEmail; }

        @com.fasterxml.jackson.annotation.JsonProperty("payer_name")
        public PayerName getPayerName() { return payerName; }

        @com.fasterxml.jackson.annotation.JsonProperty("amount_with_breakdown")
        public AmountWithBreakdown getAmountWithBreakdown() { return amountWithBreakdown; }

        public String getTime() { return time; }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static final class PayerName {
            @com.fasterxml.jackson.annotation.JsonProperty("given_name")
            private final String givenName;

            @com.fasterxml.jackson.annotation.JsonProperty("surname")
            private final String surname;

            public PayerName(String givenName, String surname) {
                this.givenName = givenName;
                this.surname = surname;
            }

            @com.fasterxml.jackson.annotation.JsonProperty("given_name")
            public String getGivenName() { return givenName; }

            @com.fasterxml.jackson.annotation.JsonProperty("surname")
            public String getSurname() { return surname; }
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static final class AmountWithBreakdown {
            @com.fasterxml.jackson.annotation.JsonProperty("gross_amount")
            private final CreateSubscriptionRequest.ShippingAmount grossAmount;

            @com.fasterxml.jackson.annotation.JsonProperty("fee_amount")
            private final CreateSubscriptionRequest.ShippingAmount feeAmount;

            @com.fasterxml.jackson.annotation.JsonProperty("net_amount")
            private final CreateSubscriptionRequest.ShippingAmount netAmount;

            public AmountWithBreakdown(CreateSubscriptionRequest.ShippingAmount grossAmount,
                                     CreateSubscriptionRequest.ShippingAmount feeAmount,
                                     CreateSubscriptionRequest.ShippingAmount netAmount) {
                this.grossAmount = grossAmount;
                this.feeAmount = feeAmount;
                this.netAmount = netAmount;
            }

            @com.fasterxml.jackson.annotation.JsonProperty("gross_amount")
            public CreateSubscriptionRequest.ShippingAmount getGrossAmount() { return grossAmount; }

            @com.fasterxml.jackson.annotation.JsonProperty("fee_amount")
            public CreateSubscriptionRequest.ShippingAmount getFeeAmount() { return feeAmount; }

            @com.fasterxml.jackson.annotation.JsonProperty("net_amount")
            public CreateSubscriptionRequest.ShippingAmount getNetAmount() { return netAmount; }
        }
    }
}
