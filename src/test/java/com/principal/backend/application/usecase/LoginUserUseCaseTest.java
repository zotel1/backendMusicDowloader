package com.principal.backend.application.usecase;

import com.principal.backend.application.port.PasswordEncoder;
import com.principal.backend.domain.exception.InvalidCredentialsException;
import com.principal.backend.domain.model.User;
import com.principal.backend.domain.port.JwtProvider;
import com.principal.backend.domain.port.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private LoginUserUseCase useCase;

    @Test
    void loginUser_Success() {
        String email = "test@example.com";
        String password = "password123";
        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        User user = User.create(email, "encodedPassword");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        when(jwtProvider.generateAccessToken(user)).thenReturn(accessToken);
        when(jwtProvider.generateRefreshToken(user)).thenReturn(refreshToken);

        LoginUserUseCase.AuthResult result = useCase.execute(email, password);

        assertNotNull(result);
        assertEquals(accessToken, result.accessToken());
        assertEquals(refreshToken, result.refreshToken());

        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(password, user.getPassword());
        verify(jwtProvider).generateAccessToken(user);
        verify(jwtProvider).generateRefreshToken(user);
    }

    @Test
    void loginUser_InvalidEmail_Throws() {
        String email = "unknown@example.com";
        String password = "password123";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> useCase.execute(email, password));

        verify(userRepository).findByEmail(email);
        verifyNoInteractions(passwordEncoder, jwtProvider);
    }

    @Test
    void loginUser_WrongPassword_Throws() {
        String email = "test@example.com";
        String password = "wrongPassword";
        User user = User.create(email, "encodedPassword");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> useCase.execute(email, password));

        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(password, user.getPassword());
        verifyNoInteractions(jwtProvider);
    }
}
