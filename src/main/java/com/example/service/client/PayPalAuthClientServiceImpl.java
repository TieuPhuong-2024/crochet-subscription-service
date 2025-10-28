package com.example.service.client;

import com.example.client.PayPalAuthClient;
import com.example.config.PayPalConfig;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;

@Slf4j
@ApplicationScoped
public class PayPalAuthClientServiceImpl implements PayPalAuthClientService {
    @Inject
    PayPalConfig cfg;

    @Inject
    @RestClient
    PayPalAuthClient authClient;

    private Cache<String, CachedToken> cache;

    private static final String CACHE_KEY = "paypal_token";

    record CachedToken(String token, long expiryTime) {}

    @PostConstruct
    void initCache() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofHours(1))
                .build();
    }

    @Override
    public String getAccessToken() {
        CachedToken cached = cache.getIfPresent(CACHE_KEY);
        long currentTime = System.currentTimeMillis();

        if (cached != null && currentTime < cached.expiryTime) {
            log.info("Returning cached PayPal access token");
            return cached.token;
        }

        log.info("Fetching new PayPal access token");
        String basic = Base64.getEncoder().encodeToString(
                (cfg.clientId() + ":" + cfg.clientSecret()).getBytes(StandardCharsets.UTF_8));
        Map<String, Object> resp = authClient.token("Basic " + basic, "client_credentials");
        String accessToken = (String) resp.get("access_token");
        Integer expiresIn = (Integer) resp.get("expires_in");
        long expiryTime;
        if (expiresIn != null) {
            // Cache the token with a buffer (e.g., expire 10 seconds early to be safe)
            expiryTime = currentTime + (expiresIn * 1000L) - 10000;
        } else {
            // If no expires_in, cache for 1 hour as fallback
            expiryTime = currentTime + 3600000;
        }
        String token = "Bearer " + accessToken;
        CachedToken newCached = new CachedToken(token, expiryTime);
        cache.put(CACHE_KEY, newCached);
        return token;
    }
}
