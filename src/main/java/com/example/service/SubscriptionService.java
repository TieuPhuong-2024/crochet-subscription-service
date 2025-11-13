package com.example.service;

import com.example.dto.CaptureSubscriptionRequest;
import com.example.dto.CreatePlanRequest;
import com.example.dto.CreateSubscriptionRequest;
import com.example.dto.ListPlansResponse;
import com.example.dto.ListSubscriptionsResponse;
import com.example.dto.PayPalResponse;
import com.example.dto.PlanResponse;
import com.example.dto.ReviseSubscriptionRequest;
import com.example.dto.SubscriptionActionRequest;
import com.example.dto.SubscriptionResponse;
import com.example.dto.SubscriptionTransactionsResponse;
import com.example.dto.UpdatePricingRequest;
import com.example.dto.UpdateSubscriptionRequest;
import com.example.dto.UserSubscription;

public interface SubscriptionService {
    // Plan operations
    PlanResponse createPlan(CreatePlanRequest request);

    ListPlansResponse listPlans(String productId, Integer pageSize, Integer page, Boolean totalRequired);

    PlanResponse getPlan(String planId);

    PlanResponse updatePlan(String planId, UpdateSubscriptionRequest request);

    void activatePlan(String planId);

    void deactivatePlan(String planId);

    void updatePricing(String planId, UpdatePricingRequest request);

    ListSubscriptionsResponse listSubscriptions(String planIds, String statuses, String createdAfter,
                                                String createdBefore, String statusUpdatedBefore, String statusUpdatedAfter,
                                                String filter, Integer pageSize, Integer page, String customerIds);

    SubscriptionResponse getSubscription(String subscriptionId, String fields);

    void updateSubscription(String subscriptionId, UpdateSubscriptionRequest request);

    PayPalResponse reviseSubscription(String subscriptionId, ReviseSubscriptionRequest request);

    void suspendSubscription(String subscriptionId, SubscriptionActionRequest request);

    void cancelSubscription(String subscriptionId, SubscriptionActionRequest request);

    void activateSubscription(String subscriptionId, SubscriptionActionRequest request);

    PayPalResponse capturePayment(String subscriptionId, CaptureSubscriptionRequest request);

    SubscriptionTransactionsResponse getTransactions(String subscriptionId, String startTime, String endTime);

    // Existing methods
    PayPalResponse create(String authHeader, CreateSubscriptionRequest request);

    void handleSubscriptionReturn(String subscriptionId);

    void handleWebhookEvent(String payload);

    UserSubscription getUserSubscription(String userId);
}
