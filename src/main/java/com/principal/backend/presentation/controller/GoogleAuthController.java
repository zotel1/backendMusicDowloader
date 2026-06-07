package com.principal.backend.presentation.controller;

import com.principal.backend.application.usecase.GoogleAuthUseCase;
import com.principal.backend.domain.model.AuthResult;
import com.principal.backend.domain.model.User;
import com.principal.backend.presentation.dto.AuthCodeRequest;
import com.principal.backend.presentation.dto.AuthExchangeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth/google")
public class GoogleAuthController {

    private final GoogleAuthUseCase googleAuthUseCase;

    public GoogleAuthController(GoogleAuthUseCase googleAuthUseCase) {
        this.googleAuthUseCase = googleAuthUseCase;
    }

    @PostMapping("/exchange")
    public ResponseEntity<AuthExchangeResponse> exchangeCode(@RequestBody AuthCodeRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof User user)) {
            return ResponseEntity.status(401).body(new AuthExchangeResponse(null, "Authentication required"));
        }

        UUID userId = user.getId();
        AuthResult result = googleAuthUseCase.execute(request.getCode(), userId);
        String message = result.isSuccess()
                ? "Token almacenados correctamente"
                : "Error intercambiando code";
        AuthExchangeResponse response = new AuthExchangeResponse(result.getEmail(), message);
        return ResponseEntity.ok(response);
    }
}
