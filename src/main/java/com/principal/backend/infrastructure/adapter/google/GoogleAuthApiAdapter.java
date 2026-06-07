package com.principal.backend.infrastructure.adapter.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.principal.backend.domain.model.GoogleTokenResponse;
import com.principal.backend.domain.port.GoogleAuthClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;

@Component
public class GoogleAuthApiAdapter implements GoogleAuthClient {

    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";

    private final RestTemplate restTemplate;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    public GoogleAuthApiAdapter(
            RestTemplate restTemplate,
            @Value("${google.oauth.clientId}") String clientId,
            @Value("${google.oauth.clientSecret}") String clientSecret,
            @Value("${google.oauth.redirectUri:postmessage}") String redirectUri) {
        this.restTemplate = restTemplate;
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
                    TOKEN_URL,
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
    @SuppressWarnings("unchecked")
    public String refreshAccessToken(String refreshToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "refresh_token");
            body.add("client_id", clientId);
            body.add("client_secret", clientSecret);
            body.add("refresh_token", refreshToken);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    TOKEN_URL, request, Map.class);

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("access_token")) {
                return (String) responseBody.get("access_token");
            }

            throw new RuntimeException("No access_token in refresh response");
        } catch (Exception e) {
            throw new RuntimeException("Failed to refresh access token", e);
        }
    }
}
