package com.principal.backend.domain.port;

import com.principal.backend.domain.model.GoogleTokenResponse;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class GoogleAuthClientTest {

    @Test
    void shouldSupportExchangeCodeReturningGoogleTokenResponse() {
        GoogleAuthClient client = new GoogleAuthClient() {
            @Override
            public GoogleTokenResponse exchangeCode(String code) {
                return new GoogleTokenResponse(
                        "google-id-123", "user@gmail.com",
                        "access-token", "refresh-token", Instant.now().plusSeconds(3600));
            }

            @Override
            public String refreshAccessToken(String refreshToken) {
                return null;
            }
        };

        GoogleTokenResponse response = client.exchangeCode("auth-code");

        assertNotNull(response);
        assertEquals("google-id-123", response.getGoogleId());
        assertEquals("user@gmail.com", response.getEmail());
    }

    @Test
    void shouldSupportRefreshAccessToken() {
        GoogleAuthClient client = new GoogleAuthClient() {
            @Override
            public GoogleTokenResponse exchangeCode(String code) {
                return null;
            }

            @Override
            public String refreshAccessToken(String refreshToken) {
                return "new-access-token";
            }
        };

        String newToken = client.refreshAccessToken("old-refresh-token");

        assertEquals("new-access-token", newToken);
    }
}
