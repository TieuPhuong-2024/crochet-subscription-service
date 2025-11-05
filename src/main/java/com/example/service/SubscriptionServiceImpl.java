package com.example.service;

import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.example.client.PayPalSubscriptionsClient;
import com.example.config.AppConfig;
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
import com.example.dto.UserUpdateRequest;
import com.example.entity.SubscriptionStatus;
import com.example.exception.ResourceNotFoundException;
import com.example.exception.ValidationException;
import com.example.mapping.SubscriptionMapper;
import com.example.repository.SubscriptionRepository;
import com.example.service.client.PayPalAuthClientService;
import com.example.service.client.UserClientService;
import com.example.util.JwtUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class SubscriptionServiceImpl implements SubscriptionService {

    private static final Map<String, SubscriptionStatus> EVENT_TO_STATUS = Map.of(
            "BILLING.SUBSCRIPTION.ACTIVATED", SubscriptionStatus.ACTIVE,
            "BILLING.SUBSCRIPTION.RE-ACTIVATED", SubscriptionStatus.ACTIVE,
            "BILLING.SUBSCRIPTION.SUSPENDED", SubscriptionStatus.SUSPENDED,
            "BILLING.SUBSCRIPTION.CANCELLED", SubscriptionStatus.CANCELLED,
            "BILLING.SUBSCRIPTION.EXPIRED", SubscriptionStatus.EXPIRED);

    @Inject
    PayPalAuthClientService authClientService;

    @Inject
    @RestClient
    PayPalSubscriptionsClient paypalSubClient;

    @Inject
    UserClientService userClientService;

    @Inject
    SubscriptionRepository subRepo;

    @Inject
    SubscriptionMapper subMapper;

    @Inject
    ObjectMapper om;

    @Inject
    JwtUtil jwtUtil;

    @ConfigProperty(name = "quarkus.profile")
    String profile;

    @Inject
    AppConfig cfg;

    private Uni<Void> updateUserRole(String userId, String role) {
        UserUpdateRequest request = UserUpdateRequest.builder()
                .id(userId)
                .role(role)
                .build();
        return userClientService.update(cfg.apiKey(), request)
                .onItem().invoke(() -> log.info("User role updated to {}: {}", role, userId));
    }

    @Transactional
    public PayPalResponse create(String authHeader, CreateSubscriptionRequest request) {
        log.info("Create subscription with token: {}", authHeader);

        var crochetJwtToken = jwtUtil.subString(authHeader);
        if (jwtUtil.isExpired(crochetJwtToken)) {
            throw new ValidationException("Authentication token has expired");
        }

        String userId = jwtUtil.extractSubject(crochetJwtToken);
        if (userId == null) {
            throw new ValidationException("Invalid or missing authentication token");
        }

        subRepo.findByUserId(userId)
                .filter(sub -> sub.getStatus() == SubscriptionStatus.ACTIVE
                        || sub.getStatus() == SubscriptionStatus.APPROVAL_PENDING)
                .ifPresent(sub -> {
                    String message = sub.getStatus() == SubscriptionStatus.ACTIVE
                            ? "User already has an active subscription"
                            : "User already has an approval pending subscription";
                    throw new ValidationException(message);
                });

        PayPalResponse createSubRes = createSubscription(authClientService.getAccessToken(), request);
        if (createSubRes == null) {
            throw new ValidationException("Failed to create subscription with PayPal");
        }

        var sub = subMapper.toEntity(createSubRes);
        sub.setPlanId(request.getPlanId());
        sub.setUserId(userId);
        subRepo.persist(sub);

        log.info("Subscription created successfully for user: {}", userId);
        return createSubRes;
    }

    @Transactional
    @Override
    public void handleSubscriptionReturn(String subscriptionId) {
        log.info("Handle subscription return: {}", subscriptionId);

        var sub = subRepo.findBySubscriptionId(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", subscriptionId));
        sub.setStatus(SubscriptionStatus.ACTIVE);
        subRepo.persist(sub);

        // Update role only in non-prod environments for local testing; prod relies on
        // webhooks
        if (!"prod".equals(profile)) {
            updateUserRole(sub.getUserId(), "VIP_USER").subscribe().with(
                    success -> {
                    },
                    failure -> log.error("Failed to update user role", failure));
        }

        subRepo.flush();
    }

    @SneakyThrows
    @Transactional
    @Override
    public void handleWebhookEvent(String payload) {
        // Parse event type and subscription ID from payload
        JsonNode jsonNode = om.readTree(payload);
        String eventType = jsonNode.get("event_type").asText();
        String subscriptionId = jsonNode.get("resource").get("id").asText();
        log.info("Webhook event received: {}, subscription ID: {}", eventType, subscriptionId);

        // Check if a subscription exists in a database
        var sub = subRepo.findBySubscriptionId(subscriptionId).orElse(null);
        if (sub == null) {
            log.warn("Subscription not found in DB: {}", subscriptionId);
            return;
        }

        // Update subscription status based on an event type
        SubscriptionStatus newStatus = EVENT_TO_STATUS.get(eventType);
        if (newStatus != null) {
            sub.setStatus(newStatus);
            subRepo.persist(sub);
            subRepo.flush();
            String action = eventType.substring(eventType.lastIndexOf('.') + 1).toLowerCase().replace("-", "");
            log.info("Subscription {}: {}", action, subscriptionId);

            // Update user role based on status change
            if (newStatus == SubscriptionStatus.ACTIVE) {
                updateUserRole(sub.getUserId(), "VIP_USER").subscribe().with(
                        success -> {
                        },
                        failure -> log.error("Failed to update user role", failure));
            } else if (newStatus == SubscriptionStatus.EXPIRED) {
                updateUserRole(sub.getUserId(), "USER").subscribe().with(
                        success -> {
                        },
                        failure -> log.error("Failed to update user role", failure));
            }
        } else {
            log.info("Unhandled webhook event type: {}", eventType);
        }
    }

    @Override
    public UserSubscription getUserSubscription(String userId) {
        if (userId == null) {
            throw new ValidationException("User id cannot blank");
        }

        var subscription = subRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found for user", userId));

        // Get PayPal subscription details to get additional information
        SubscriptionResponse payPalDetails = paypalSubClient.getSubscription(
                authClientService.getAccessToken(),
                subscription.getId(),
                null);

        if (payPalDetails == null) {
            throw new ValidationException("Failed to fetch subscription details from PayPal");
        }

        return mapToUserSubscription(subscription, payPalDetails);
    }

    private UserSubscription mapToUserSubscription(com.example.entity.Subscription subscription,
            SubscriptionResponse payPalDetails) {
        return UserSubscription.builder()
                .id(subscription.getId())
                .userId(subscription.getUserId())
                .planId(subscription.getPlanId())
                .paypalSubscriptionId(subscription.getId())
                .status(mapStatusToFrontend(subscription.getStatus()))
                .startDate(payPalDetails.getStartTime())
                .endDate(null) // PayPal doesn't always provide end date; can be calculated based on billing
                               // cycles if needed
                .createdAt(payPalDetails.getCreateTime())
                .updatedAt(payPalDetails.getUpdateTime())
                .build();
    }

    private String mapStatusToFrontend(SubscriptionStatus status) {
        if (status == null) {
            return null;
        }
        return switch (status) {
            case CREATED, APPROVAL_PENDING, SUSPENDED -> "pending";
            case ACTIVE -> "active";
            case CANCELLED -> "cancelled";
            case EXPIRED -> "expired";
        };
    }

    // Plan operations
    @Override
    public PlanResponse createPlan(CreatePlanRequest request) {
        log.info("Creating PayPal plan");
        return paypalSubClient.createPlan(authClientService.getAccessToken(), null, request);
    }

    @Override
    public ListPlansResponse listPlans(String productId, Integer pageSize, Integer page,
            Boolean totalRequired) {
        log.info("Listing PayPal plans");
        return paypalSubClient.listPlans(authClientService.getAccessToken(), productId, pageSize, page, totalRequired);
    }

    @Override
    public PlanResponse getPlan(String planId) {
        log.info("Getting PayPal plan: {}", planId);
        return paypalSubClient.getPlan(authClientService.getAccessToken(), planId);
    }

    @Override
    public PlanResponse updatePlan(String planId, UpdateSubscriptionRequest request) {
        log.info("Updating PayPal plan: {}", planId);
        return paypalSubClient.updatePlan(authClientService.getAccessToken(), planId, request);
    }

    @Override
    public void activatePlan(String planId) {
        log.info("Activating PayPal plan: {}", planId);
        paypalSubClient.activatePlan(authClientService.getAccessToken(), planId);
    }

    @Override
    public void deactivatePlan(String planId) {
        log.info("Deactivating PayPal plan: {}", planId);
        paypalSubClient.deactivatePlan(authClientService.getAccessToken(), planId);
    }

    @Override
    public void updatePricing(String planId, UpdatePricingRequest request) {
        log.info("Updating pricing for PayPal plan: {}", planId);
        paypalSubClient.updatePricing(authClientService.getAccessToken(), planId, request);
    }

    // Subscription operations
    @Override
    public PayPalResponse createSubscription(String token, CreateSubscriptionRequest request) {
        log.info("Creating PayPal subscription");
        return paypalSubClient.createSubscription(authClientService.getAccessToken(), null, null, request);
    }

    @Override
    public ListSubscriptionsResponse listSubscriptions(String planIds, String statuses,
            String createdAfter,
            String createdBefore, String statusUpdatedBefore, String statusUpdatedAfter,
            String filter, Integer pageSize, Integer page, String customerIds) {
        log.info("Listing PayPal subscriptions");
        return paypalSubClient.listSubscriptions(authClientService.getAccessToken(), planIds, statuses, createdAfter,
                createdBefore,
                statusUpdatedBefore, statusUpdatedAfter, filter, pageSize, page, customerIds);
    }

    @Override
    public SubscriptionResponse getSubscription(String subscriptionId, String fields) {
        log.info("Getting PayPal subscription: {}", subscriptionId);
        return paypalSubClient.getSubscription(authClientService.getAccessToken(), subscriptionId, fields);
    }

    @Override
    public void updateSubscription(String subscriptionId, UpdateSubscriptionRequest request) {
        log.info("Updating PayPal subscription: {}", subscriptionId);
        paypalSubClient.updateSubscription(authClientService.getAccessToken(), subscriptionId, request);
    }

    @Override
    public PayPalResponse reviseSubscription(String subscriptionId, ReviseSubscriptionRequest request) {
        log.info("Revising PayPal subscription: {}", subscriptionId);
        return paypalSubClient.reviseSubscription(authClientService.getAccessToken(), subscriptionId, request);
    }

    @Override
    public void suspendSubscription(String subscriptionId, SubscriptionActionRequest request) {
        log.info("Suspending PayPal subscription: {}", subscriptionId);
        paypalSubClient.suspendSubscription(authClientService.getAccessToken(), subscriptionId, request);
    }

    @Override
    public void cancelSubscription(String subscriptionId, SubscriptionActionRequest request) {
        log.info("Cancelling PayPal subscription: {}", subscriptionId);
        paypalSubClient.cancelSubscription(authClientService.getAccessToken(), subscriptionId, request);
    }

    @Override
    public void activateSubscription(String subscriptionId, SubscriptionActionRequest request) {
        log.info("Activating PayPal subscription: {}", subscriptionId);
        paypalSubClient.activateSubscription(authClientService.getAccessToken(), subscriptionId, request);
    }

    @Override
    public PayPalResponse capturePayment(String subscriptionId, CaptureSubscriptionRequest request) {
        log.info("Capturing payment for PayPal subscription: {}", subscriptionId);
        return paypalSubClient.capturePayment(authClientService.getAccessToken(), null, subscriptionId, request);
    }

    @Override
    public SubscriptionTransactionsResponse getTransactions(String subscriptionId, String startTime,
            String endTime) {
        log.info("Getting transactions for PayPal subscription: {}", subscriptionId);
        return paypalSubClient.getTransactions(authClientService.getAccessToken(), subscriptionId, startTime, endTime);
    }

}
