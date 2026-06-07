package com.principal.backend.application.usecase;

import com.principal.backend.domain.model.AuthResult;
import com.principal.backend.domain.model.GoogleAccount;
import com.principal.backend.domain.model.GoogleTokenResponse;
import com.principal.backend.domain.port.GoogleAccountRepository;
import com.principal.backend.domain.port.GoogleAuthClient;
import com.principal.backend.domain.port.GoogleDriveClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GoogleAuthUseCase {

    private static final Logger log = LoggerFactory.getLogger(GoogleAuthUseCase.class);

    private final GoogleAuthClient authClient;
    private final GoogleAccountRepository accountRepository;
    private final GoogleDriveClient driveClient;

    public GoogleAuthUseCase(GoogleAuthClient authClient,
                             GoogleAccountRepository accountRepository,
                             GoogleDriveClient driveClient) {
        this.authClient = authClient;
        this.accountRepository = accountRepository;
        this.driveClient = driveClient;
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

            triggerFolderCreation(account.getAccessToken());

            return new AuthResult(tokenResponse.getEmail(), userId.toString(), true);

        } catch (Exception e) {
            return new AuthResult(null, null, false);
        }
    }

    @Async
    public void triggerFolderCreation(String accessToken) {
        try {
            driveClient.createFolders(accessToken);
        } catch (Exception e) {
            log.warn("Non-blocking folder creation failed: {}", e.getMessage());
        }
    }
}
