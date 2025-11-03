package com.example.dto.subscription;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayPalSubscriptionDetails {

    private String id;
    private String status;
    @JsonProperty("status_change_note")
    private String statusChangeNote;
    @JsonProperty("status_change_time")
    private Instant statusChangeTime;
    @JsonProperty("plan_id")
    private String planId;
    @JsonProperty("start_time")
    private Instant startTime;
    @JsonProperty("create_time")
    private Instant createTime;
    @JsonProperty("update_time")
    private Instant updateTime;

    @JsonProperty("subscriber")
    private Subscriber subscriber;

    @JsonProperty("billing_info")
    private BillingInfo billingInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Subscriber {
        @JsonProperty("email_address")
        private String emailAddress;
        @JsonProperty("payer_id")
        private String payerId;
        @JsonProperty("payer_name")
        private PayerName payerName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PayerName {
        @JsonProperty("given_name")
        private String givenName;
        @JsonProperty("surname")
        private String surname;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BillingInfo {
        @JsonProperty("cycle_executions")
        private List<CycleExecution> cycleExecutions;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CycleExecution {
        @JsonProperty("tenure_type")
        private String tenureType;
        @JsonProperty("sequence")
        private Integer sequence;
        @JsonProperty("cycles_completed")
        private Integer cyclesCompleted;
        @JsonProperty("cycles_remaining")
        private Integer cyclesRemaining;
        @JsonProperty("total_cycles")
        private Integer totalCycles;
    }
}
