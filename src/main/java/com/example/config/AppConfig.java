package com.example.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "app")
public interface AppConfig {
    String apiKey();
    Jwt jwt();
    PayPal paypal();

    interface Jwt {
        String jwtKey();
    }

    interface PayPal {
        String baseUrl();
        String clientId();
        String clientSecret();
    }
}
