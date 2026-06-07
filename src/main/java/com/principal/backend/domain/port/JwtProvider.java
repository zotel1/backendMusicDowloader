package com.principal.backend.domain.port;

import com.principal.backend.domain.model.User;

import java.util.UUID;

public interface JwtProvider {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    boolean validateToken(String token);
    UUID getUserIdFromToken(String token);
}
