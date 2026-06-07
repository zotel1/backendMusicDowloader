package com.principal.backend.application.usecase;

import com.principal.backend.domain.exception.InvalidTokenException;
import com.principal.backend.domain.model.User;
import com.principal.backend.domain.port.JwtProvider;
import com.principal.backend.domain.port.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenUseCaseTest {

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RefreshTokenUseCase useCase;

    @Test
    void refreshToken_Success() {
        String oldRefreshToken = "valid-refresh-token";
        UUID userId = UUID.randomUUID();
        User user = User.create("test@example.com", "encodedPassword");
        String newAccessToken = "new-access-token";
        String newRefreshToken = "new-refresh-token";

        when(jwtProvider.validateToken(oldRefreshToken)).thenReturn(true);
        when(jwtProvider.getUserIdFromToken(oldRefreshToken)).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(jwtProvider.generateAccessToken(user)).thenReturn(newAccessToken);
        when(jwtProvider.generateRefreshToken(user)).thenReturn(newRefreshToken);

        RefreshTokenUseCase.AuthResult result = useCase.execute(oldRefreshToken);

        assertNotNull(result);
        assertEquals(newAccessToken, result.accessToken());
        assertEquals(newRefreshToken, result.refreshToken());

        verify(jwtProvider).validateToken(oldRefreshToken);
        verify(jwtProvider).getUserIdFromToken(oldRefreshToken);
        verify(userRepository).findById(userId);
        verify(jwtProvider).generateAccessToken(user);
        verify(jwtProvider).generateRefreshToken(user);
    }

    @Test
    void refreshToken_InvalidToken_Throws() {
        String invalidRefreshToken = "invalid-token";

        when(jwtProvider.validateToken(invalidRefreshToken)).thenReturn(false);

        assertThrows(InvalidTokenException.class, () -> useCase.execute(invalidRefreshToken));

        verify(jwtProvider).validateToken(invalidRefreshToken);
        verify(jwtProvider, never()).getUserIdFromToken(anyString());
        verifyNoInteractions(userRepository);
    }
}
