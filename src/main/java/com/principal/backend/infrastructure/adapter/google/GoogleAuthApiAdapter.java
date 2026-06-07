package com.principal.backend.infrastructure.adapter.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.principal.backend.domain.model.GoogleTokenResponse;
import com.principal.backend.domain.port.GoogleAuthClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class GoogleAuthApiAdapter implements GoogleAuthClient {

    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    public GoogleAuthApiAdapter(
            @Value("${google.oauth.clientId}") String clientId,
            @Value("${google.oauth.clientSecret}") String clientSecret,
            @Value("${google.oauth.redirectUri:postmessage}") String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    @Override
    public GoogleTokenResponse exchangeCode(String code) {
        try {
            var tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JSON_FACTORY,
                    "https://oauth2.googleapis.com/token",
                    clientId,
                    code,
                    redirectUri
            ).execute();

            String accessToken = tokenResponse.getAccessToken();
            String refreshToken = tokenResponse.getRefreshToken();
            Long expiresInSeconds = tokenResponse.getExpiresInSeconds();
            Instant expiresAt = (expiresInSeconds != null)
                    ? Instant.now().plusSeconds(expiresInSeconds)
                    : Instant.now();

            String idTokenString = tokenResponse.getIdToken();

            if (idTokenString != null) {
                GoogleIdToken idToken = GoogleIdToken.parse(JSON_FACTORY, idTokenString);
                GoogleIdToken.Payload payload = idToken.getPayload();
                String googleId = payload.getSubject();
                String email = payload.getEmail();

                return new GoogleTokenResponse(googleId, email, accessToken, refreshToken, expiresAt);
            }

            return new GoogleTokenResponse(null, null, accessToken, refreshToken, expiresAt);

        } catch (Exception e) {
            throw new RuntimeException("Failed to exchange authorization code", e);
        }
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
        // Will be implemented in Phase 4 (PR 2) with transparent refresh logic
        throw new UnsupportedOperationException("Token refresh not yet implemented");
    }
}
