package com.example.bookswapplatform.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

public class JwtTokenUtil {
    private static final Key API_KEY_SECRET = Keys.hmacShaKeyFor("VlBuUXIyaWswaXdxNGhWeWlwNnNTTnBMNzRDaDVUR08=".getBytes());

    public static String generateAccessToken(String apiKeySid, long expirationTimeSeconds) {

        String jti = generateJti(apiKeySid);

        Date now = new Date();
        Date expirationDate = Date.from(Instant.ofEpochSecond(now.getTime() / 1000 + expirationTimeSeconds));

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("cty", "stringee-api;v=1")
                .claim("jti", jti)
                .claim("iss", apiKeySid)
                .claim("exp", expirationDate)
                .claim("rest_api", true)
                .signWith(API_KEY_SECRET)
                .compact();
    }
    private static String generateJti(String apiKeySid) {
        // Combine apiKeySid with current timestamp
        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        return apiKeySid + "_" + timestamp;
    }

}
