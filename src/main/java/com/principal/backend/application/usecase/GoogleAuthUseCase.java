package com.principal.backend.application.usecase;

import com.principal.backend.domain.model.AuthResult;
import com.principal.backend.domain.port.GoogleAuthClient;
import org.springframework.stereotype.Service;

@Service
public class GoogleAuthUseCase {

    private final GoogleAuthClient authClient;

    public GoogleAuthUseCase(GoogleAuthClient authClient) {
        this.authClient = authClient;
    }

    public AuthResult execute(String code) {
        return authClient.exchangeCode(code);
    }
}
