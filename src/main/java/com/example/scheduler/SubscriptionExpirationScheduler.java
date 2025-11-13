package com.example.scheduler;

import com.example.entity.Subscription;
import com.example.entity.SubscriptionStatus;
import com.example.repository.SubscriptionRepository;
import com.example.service.client.UserClientService;
import com.example.dto.UserUpdateRequest;
import com.example.config.AppConfig;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;

import io.quarkus.scheduler.Scheduled;

@Slf4j
@ApplicationScoped
public class SubscriptionExpirationScheduler {

    @Inject
    SubscriptionRepository subscriptionRepository;

    @Inject
    UserClientService userClientService;

    @Inject
    AppConfig config;

    @Scheduled(cron = "0 0 0 * * ?") // Run daily at midnight
    @Transactional
    public void checkExpiredSubscriptions() {
        log.info("Starting scheduled check for expired subscriptions");

        Instant now = Instant.now();
        List<Subscription> expiredSubscriptions = subscriptionRepository.findExpiredSubscriptions(now);

        if (expiredSubscriptions.isEmpty()) {
            log.info("No expired subscriptions found");
            return;
        }

        log.info("Found {} expired subscriptions to process", expiredSubscriptions.size());

        for (Subscription subscription : expiredSubscriptions) {
            try {
                processExpiredSubscription(subscription);
            } catch (Exception e) {
                log.error("Failed to process expired subscription for user: {}", subscription.getUserId(), e);
            }
        }

        log.info("Completed scheduled check for expired subscriptions");
    }

    private void processExpiredSubscription(Subscription subscription) {
        log.info("Processing expired subscription for user: {}", subscription.getUserId());

        subscription.setStatus(SubscriptionStatus.EXPIRED);
        subscriptionRepository.persist(subscription);

        UserUpdateRequest request = UserUpdateRequest.builder()
                .id(subscription.getUserId())
                .role("USER")
                .build();

        userClientService.update(config.apiKey(), request)
                .subscribe().with(
                        success -> log.info("User role downgraded to USER for user: {}", subscription.getUserId()),
                        failure -> log.error("Failed to update user role for user: {}", subscription.getUserId(), failure)
                );
    }
}
