package com.example.dto.subscription;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscription {

    private String id;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("plan_id")
    private String planId;

    @JsonProperty("paypal_subscription_id")
    private String paypalSubscriptionId;

    private String status;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;
}
