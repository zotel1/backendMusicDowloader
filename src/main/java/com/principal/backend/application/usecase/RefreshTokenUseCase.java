package com.principal.backend.application.usecase;

import com.principal.backend.domain.exception.InvalidTokenException;
import com.principal.backend.domain.model.User;
import com.principal.backend.domain.port.JwtProvider;
import com.principal.backend.domain.port.UserRepository;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RefreshTokenUseCase {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public RefreshTokenUseCase(JwtProvider jwtProvider, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    public AuthResult execute(String refreshToken) {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }
        UUID userId = jwtProvider.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidTokenException("User not found"));
        String newAccessToken = jwtProvider.generateAccessToken(user);
        String newRefreshToken = jwtProvider.generateRefreshToken(user);
        return new AuthResult(newAccessToken, newRefreshToken);
    }

    public record AuthResult(String accessToken, String refreshToken) {}
}
