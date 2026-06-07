package com.principal.backend.presentation.controller;

import com.principal.backend.application.usecase.LoginUserUseCase;
import com.principal.backend.application.usecase.RefreshTokenUseCase;
import com.principal.backend.application.usecase.RegisterUserUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase,
                          LoginUserUseCase loginUserUseCase,
                          RefreshTokenUseCase refreshTokenUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserUseCase.AuthResult> register(
            @Valid @RequestBody RegisterRequest request) {
        RegisterUserUseCase.AuthResult result =
                registerUserUseCase.execute(request.email(), request.password());
        return ResponseEntity.status(201).body(result);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserUseCase.AuthResult> login(
            @Valid @RequestBody LoginRequest request) {
        LoginUserUseCase.AuthResult result =
                loginUserUseCase.execute(request.email(), request.password());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenUseCase.AuthResult> refresh(
            @Valid @RequestBody RefreshTokenRequest request) {
        RefreshTokenUseCase.AuthResult result =
                refreshTokenUseCase.execute(request.refreshToken());
        return ResponseEntity.ok(result);
    }

    // --- DTOs ---

    public record RegisterRequest(
            @NotBlank String email,
            @NotBlank @Size(min = 6) String password
    ) {}

    public record LoginRequest(
            @NotBlank String email,
            @NotBlank String password
    ) {}

    public record RefreshTokenRequest(
            @NotBlank String refreshToken
    ) {}
}
