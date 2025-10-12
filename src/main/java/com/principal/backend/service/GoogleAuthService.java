package com.principal.backend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.principal.backend.store.InMemoryTokenStore;
import com.principal.backend.store.StoredToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class GoogleAuthService {

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final InMemoryTokenStore tokenStore;

    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public GoogleAuthService(
            @Value("${google.oauth.clientId") String clientId,
            @Value("${google.oauth.clientSecret}") String clientSecret,
            @Value("${google.oauth.redirectUri:postmessage}") String redirectUri,
            InMemoryTokenStore tokenStore) {
        this.clientId = clientId;
        this.clientSecret =clientSecret;
        this.redirectUri = redirectUri;
        this.tokenStore = tokenStore;
    }

    public String exchangeCodeAndStoreTokens(String code) throws Exception {
        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                "https://oauth2.googleapis.com/token",
                clientId,
                code,
                redirectUri // "postmessage" si el code viene de unb SPA
        ).execute();

        String accessToken = tokenResponse.getAccessToken();
        String refreshToken = tokenResponse.getRefreshToken();
        Long expiresInSeconds = tokenResponse.getExpiresInSeconds();
        long expiresAt = (expiresInSeconds != null) ? Instant.now().plusSeconds(expiresInSeconds).toEpochMilli() : 0L;

        String idTokenString = tokenResponse.getIdToken();
        String userId = null;
        String email = null;
        if (idTokenString != null) {
            GoogleIdToken idToken = GoogleIdToken.parse(JSON_FACTORY, idTokenString);
            GoogleIdToken.Payload payload = idToken.getPayload();
            userId = payload.getSubject();
            email = payload.getEmail();
        } else {
            throw new RuntimeException("No se reciobio id_token de Google");
        }

        StoredToken stored = new StoredToken(userId, accessToken, refreshToken, expiresAt);
        tokenStore.save(userId, stored);

        return email;
    }
}
