package com.principal.backend.application.usecase;

import com.principal.backend.domain.model.AuthResult;
import com.principal.backend.domain.model.GoogleAccount;
import com.principal.backend.domain.model.GoogleTokenResponse;
import com.principal.backend.domain.port.GoogleAccountRepository;
import com.principal.backend.domain.port.GoogleAuthClient;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GoogleAuthUseCase {

    private final GoogleAuthClient authClient;
    private final GoogleAccountRepository accountRepository;

    public GoogleAuthUseCase(GoogleAuthClient authClient, GoogleAccountRepository accountRepository) {
        this.authClient = authClient;
        this.accountRepository = accountRepository;
    }

    public AuthResult execute(String code, UUID userId) {
        try {
            GoogleTokenResponse tokenResponse = authClient.exchangeCode(code);

            if (tokenResponse.getGoogleId() == null) {
                return new AuthResult(null, null, false);
            }

            GoogleAccount account = new GoogleAccount(
                    UUID.randomUUID(),
                    userId,
                    tokenResponse.getGoogleId(),
                    tokenResponse.getEmail(),
                    tokenResponse.getAccessToken(),
                    tokenResponse.getRefreshToken(),
                    tokenResponse.getExpiresAt()
            );

            accountRepository.save(account);

            return new AuthResult(tokenResponse.getEmail(), userId.toString(), true);

        } catch (Exception e) {
            return new AuthResult(null, null, false);
        }
    }
}
