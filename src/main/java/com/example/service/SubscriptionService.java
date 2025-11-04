package com.example.service;

import com.example.dto.CreateSubscriptionRequest;
import com.example.dto.PayPalResponse;
import com.example.dto.UserSubscription;

public interface SubscriptionService {
    PayPalResponse create(String token, CreateSubscriptionRequest request);

    void handleSubscriptionReturn(String subscriptionId);

    void handleWebhookEvent(String payload);

    UserSubscription getUserSubscription(String userId);
}
