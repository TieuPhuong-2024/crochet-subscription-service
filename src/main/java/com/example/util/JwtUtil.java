package com.example.util;

import com.example.config.AppConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@ApplicationScoped
public class JwtUtil {

    @Inject
    AppConfig appConfig;

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(appConfig.jwt().jwtKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractSubject(String token) {
        return extractClaims(token).getSubject();
    }

    public String subString(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer")) {
            throw new IllegalStateException("Token is null");
        }

        var authHeader = bearerToken.substring(7);
        if (authHeader.isEmpty()) {
            throw new IllegalStateException("Token is not valid");
        }
        return authHeader;
    }

    public boolean isExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Date getExpiration(String token) {
        var claims = extractClaims(token);
        return claims.getExpiration();
    }
}
