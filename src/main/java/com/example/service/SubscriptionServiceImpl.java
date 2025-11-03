package com.example.service;

import com.example.client.PayPalSubscriptionsClient;
import com.example.config.AppConfig;
import com.example.dto.UserUpdateRequest;
import com.example.dto.subscription.CreatePayPalSubscriptionResponse;
import com.example.dto.subscription.CreateSubscriptionRequest;
import com.example.dto.subscription.PayPalSubscriptionDetails;
import com.example.dto.subscription.UserSubscription;
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
import jakarta.enterprise.concurrent.Asynchronous;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

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

    @Asynchronous
    private void updateUserRole(String userId, String role) {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setId(userId);
        request.setRole(role);
        userClientService.update(cfg.apiKey(), request);
        log.info("User role updated to {}: {}", role, userId);
    }

    @Transactional
    @Override
    public CreatePayPalSubscriptionResponse create(String authHeader, CreateSubscriptionRequest request) {
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

        CreatePayPalSubscriptionResponse createSubRes = paypalSubClient.create(authClientService.getAccessToken(),
                request);
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
            updateUserRole(sub.getUserId(), "VIP_USER");
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
                updateUserRole(sub.getUserId(), "VIP_USER");
            } else if (newStatus == SubscriptionStatus.EXPIRED) {
                updateUserRole(sub.getUserId(), "USER");
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
        PayPalSubscriptionDetails payPalDetails = paypalSubClient.getSubscription(
                authClientService.getAccessToken(),
                subscription.getId()
        );

        if (payPalDetails == null) {
            throw new ValidationException("Failed to fetch subscription details from PayPal");
        }

        return mapToUserSubscription(subscription, payPalDetails);
    }

    private UserSubscription mapToUserSubscription(com.example.entity.Subscription subscription,
                                                     PayPalSubscriptionDetails payPalDetails) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .withZone(ZoneOffset.UTC);

        return UserSubscription.builder()
                .id(subscription.getId())
                .userId(subscription.getUserId())
                .planId(subscription.getPlanId())
                .paypalSubscriptionId(subscription.getId())
                .status(mapStatusToFrontend(subscription.getStatus()))
                .startDate(payPalDetails.getStartTime() != null ?
                        formatter.format(payPalDetails.getStartTime()) : null)
                .endDate(null) // PayPal doesn't always provide end date; can be calculated based on billing cycles if needed
                .createdAt(payPalDetails.getCreateTime() != null ?
                        formatter.format(payPalDetails.getCreateTime()) : null)
                .updatedAt(payPalDetails.getUpdateTime() != null ?
                        formatter.format(payPalDetails.getUpdateTime()) : null)
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
}
