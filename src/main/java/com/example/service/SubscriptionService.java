package com.example.service;

import com.example.dto.subscription.CreatePayPalSubscriptionResponse;
import com.example.dto.subscription.CreateSubscriptionRequest;
import com.example.dto.subscription.UserSubscription;

public interface SubscriptionService {
    CreatePayPalSubscriptionResponse create(String token, CreateSubscriptionRequest request);

    void handleSubscriptionReturn(String subscriptionId);

    void handleWebhookEvent(String payload);

    UserSubscription getUserSubscription(String userId);
}
