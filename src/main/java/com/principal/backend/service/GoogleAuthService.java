package com.principal.backend.service;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.principal.backend.store.InMemoryTokenStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
}
