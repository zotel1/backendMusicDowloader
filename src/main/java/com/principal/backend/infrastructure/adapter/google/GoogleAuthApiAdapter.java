package com.principal.backend.infrastructure.adapter.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.principal.backend.domain.model.AuthResult;
import com.principal.backend.domain.model.Token;
import com.principal.backend.domain.port.GoogleAuthClient;
import com.principal.backend.domain.port.TokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class GoogleAuthApiAdapter implements GoogleAuthClient {

    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final TokenRepository tokenRepository;

    public GoogleAuthApiAdapter(
            @Value("${google.oauth.clientId}") String clientId,
            @Value("${google.oauth.clientSecret}") String clientSecret,
            @Value("${google.oauth.redirectUri:postmessage}") String redirectUri,
            TokenRepository tokenRepository) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public AuthResult exchangeCode(String code) {
        try {
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
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
            long expiresAt = (expiresInSeconds != null)
                    ? Instant.now().plusSeconds(expiresInSeconds).toEpochMilli()
                    : 0L;

            String idTokenString = tokenResponse.getIdToken();
            String userId;
            String email;

            if (idTokenString != null) {
                GoogleIdToken idToken = GoogleIdToken.parse(JSON_FACTORY, idTokenString);
                GoogleIdToken.Payload payload = idToken.getPayload();
                userId = payload.getSubject();
                email = payload.getEmail();
            } else {
                return new AuthResult(null, null, false);
            }

            Token token = new Token(userId, accessToken, refreshToken, expiresAt);
            tokenRepository.save(token);

            return new AuthResult(email, userId, true);

        } catch (Exception e) {
            return new AuthResult(null, null, false);
        }
    }
}
