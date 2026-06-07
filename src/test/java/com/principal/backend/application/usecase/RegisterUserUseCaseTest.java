package com.principal.backend.application.usecase;

import com.principal.backend.application.port.PasswordEncoder;
import com.principal.backend.domain.exception.EmailAlreadyExistsException;
import com.principal.backend.domain.model.User;
import com.principal.backend.domain.port.JwtProvider;
import com.principal.backend.domain.port.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private RegisterUserUseCase useCase;

    @Test
    void registerUser_Success() {
        String email = "test@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword";
        String accessToken = "access-token";
        String refreshToken = "refresh-token";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(jwtProvider.generateAccessToken(any(User.class))).thenReturn(accessToken);
        when(jwtProvider.generateRefreshToken(any(User.class))).thenReturn(refreshToken);

        RegisterUserUseCase.AuthResult result = useCase.execute(email, password);

        assertNotNull(result);
        assertEquals(accessToken, result.accessToken());
        assertEquals(refreshToken, result.refreshToken());

        verify(userRepository).existsByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(userRepository).save(any(User.class));
        verify(jwtProvider).generateAccessToken(any(User.class));
        verify(jwtProvider).generateRefreshToken(any(User.class));
    }

    @Test
    void registerUser_EmailAlreadyExists_Throws() {
        String email = "existing@example.com";
        String password = "password123";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> useCase.execute(email, password));

        verify(userRepository).existsByEmail(email);
        verifyNoInteractions(passwordEncoder, jwtProvider);
    }
}
