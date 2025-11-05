package com.example.service;

import com.example.dto.*;

public interface SubscriptionService {
    // Plan operations
    PlanResponse createPlan(String token, CreatePlanRequest request);

    ListPlansResponse listPlans(String token, String productId, Integer pageSize, Integer page, Boolean totalRequired);

    PlanResponse getPlan(String token, String planId);

    PlanResponse updatePlan(String token, String planId, UpdateSubscriptionRequest request);

    void activatePlan(String token, String planId);

    void deactivatePlan(String token, String planId);

    void updatePricing(String token, String planId, UpdatePricingRequest request);

    // Subscription operations
    PayPalResponse createSubscription(String token, CreateSubscriptionRequest request);

    ListSubscriptionsResponse listSubscriptions(String token, String planIds, String statuses, String createdAfter,
                                               String createdBefore, String statusUpdatedBefore, String statusUpdatedAfter,
                                               String filter, Integer pageSize, Integer page, String customerIds);

    SubscriptionResponse getSubscription(String token, String subscriptionId, String fields);

    void updateSubscription(String token, String subscriptionId, UpdateSubscriptionRequest request);

    PayPalResponse reviseSubscription(String token, String subscriptionId, ReviseSubscriptionRequest request);

    void suspendSubscription(String token, String subscriptionId, SubscriptionActionRequest request);

    void cancelSubscription(String token, String subscriptionId, SubscriptionActionRequest request);

    void activateSubscription(String token, String subscriptionId, SubscriptionActionRequest request);

    PayPalResponse capturePayment(String token, String subscriptionId, CaptureSubscriptionRequest request);

    SubscriptionTransactionsResponse getTransactions(String token, String subscriptionId, String startTime, String endTime);

    // Existing methods
    PayPalResponse create(String authHeader, CreateSubscriptionRequest request);

    void handleSubscriptionReturn(String subscriptionId);

    void handleWebhookEvent(String payload);

    UserSubscription getUserSubscription(String userId);
}
