package com.principal.backend.infrastructure.adapter.security;

import com.principal.backend.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtProviderAdapterTest {

    private JwtProviderAdapter jwtProviderAdapter;

    @BeforeEach
    void setUp() {
        jwtProviderAdapter = new JwtProviderAdapter(
                "40434563354a586e3272357538782f413442a46269404937244c635166546a576e5a7134743777217a25432646294a404e635266556a586e327235753878",
                900000L,
                604800000L
        );
    }

    @Test
    void generateAndValidateToken_Success() {
        User user = User.create("test@example.com", "encodedPassword");

        String accessToken = jwtProviderAdapter.generateAccessToken(user);

        assertNotNull(accessToken);
        assertTrue(jwtProviderAdapter.validateToken(accessToken));
    }

    @Test
    void validateToken_InvalidToken_ReturnsFalse() {
        assertFalse(jwtProviderAdapter.validateToken("invalid-token"));
    }
}
