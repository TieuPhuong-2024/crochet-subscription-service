package com.example.service;

import com.example.dto.*;

public interface SubscriptionService {
    // Plan operations
    PlanResponse createPlan(CreatePlanRequest request);

    ListPlansResponse listPlans(String productId, Integer pageSize, Integer page, Boolean totalRequired);

    PlanResponse getPlan(String planId);

    PlanResponse updatePlan(String planId, UpdateSubscriptionRequest request);

    void activatePlan(String planId);

    void deactivatePlan(String planId);

    void updatePricing(String planId, UpdatePricingRequest request);

    // Subscription operations
    PayPalResponse createSubscription(String token, CreateSubscriptionRequest request);

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
