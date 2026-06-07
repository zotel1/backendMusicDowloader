package com.principal.backend.application.usecase;

import com.principal.backend.application.port.PasswordEncoder;
import com.principal.backend.domain.exception.EmailAlreadyExistsException;
import com.principal.backend.domain.model.User;
import com.principal.backend.domain.port.JwtProvider;
import com.principal.backend.domain.port.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public RegisterUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public AuthResult execute(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
        String encodedPassword = passwordEncoder.encode(password);
        User user = User.create(email, encodedPassword);
        userRepository.save(user);
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);
        return new AuthResult(accessToken, refreshToken);
    }

    public record AuthResult(String accessToken, String refreshToken) {}
}
