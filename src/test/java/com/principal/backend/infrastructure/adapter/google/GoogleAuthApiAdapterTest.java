package com.principal.backend.infrastructure.adapter.google;

import com.principal.backend.domain.port.GoogleAuthClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoogleAuthApiAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    private GoogleAuthApiAdapter adapter;

    @Captor
    private ArgumentCaptor<HttpEntity<MultiValueMap<String, String>>> requestCaptor;

    @BeforeEach
    void setUp() {
        adapter = new GoogleAuthApiAdapter(restTemplate,
                "test-client-id", "test-client-secret", "postmessage");
    }

    @Test
    void refreshAccessToken_ReturnsNewAccessToken() {
        String refreshToken = "refresh-token-value";
        String expectedToken = "new-access-token-123";

        Map<String, String> responseBody = Map.of("access_token", expectedToken);
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        when(restTemplate.postForEntity(
                anyString(),
                any(),
                eq(Map.class)))
                .thenReturn(responseEntity);

        String result = adapter.refreshAccessToken(refreshToken);

        assertEquals(expectedToken, result);
    }

    @Test
    void refreshAccessToken_SendsCorrectRequestBody() {
        String refreshToken = "my-refresh-token";

        when(restTemplate.postForEntity(
                anyString(),
                requestCaptor.capture(),
                eq(Map.class)))
                .thenReturn(new ResponseEntity<>(Map.of("access_token", "token"), HttpStatus.OK));

        adapter.refreshAccessToken(refreshToken);

        HttpEntity<MultiValueMap<String, String>> captured = requestCaptor.getValue();
        assertEquals(MediaType.APPLICATION_FORM_URLENCODED, captured.getHeaders().getContentType());

        MultiValueMap<String, String> body = captured.getBody();
        assertNotNull(body);
        assertEquals("refresh_token", body.getFirst("grant_type"));
        assertEquals("test-client-id", body.getFirst("client_id"));
        assertEquals("test-client-secret", body.getFirst("client_secret"));
        assertEquals(refreshToken, body.getFirst("refresh_token"));
    }

    @Test
    void refreshAccessToken_ThrowsWhenResponseMissingAccessToken() {
        when(restTemplate.postForEntity(
                anyString(),
                any(),
                eq(Map.class)))
                .thenReturn(new ResponseEntity<>(Map.of(), HttpStatus.OK));

        assertThrows(RuntimeException.class,
                () -> adapter.refreshAccessToken("refresh-token"));
    }

    @Test
    void refreshAccessToken_ThrowsOnHttpError() {
        when(restTemplate.postForEntity(
                anyString(),
                any(),
                eq(Map.class)))
                .thenThrow(new RuntimeException("HTTP 400"));

        assertThrows(RuntimeException.class,
                () -> adapter.refreshAccessToken("refresh-token"));
    }

    @Test
    void adapterImplementsGoogleAuthClient() {
        assertInstanceOf(GoogleAuthClient.class, adapter);
    }
}
