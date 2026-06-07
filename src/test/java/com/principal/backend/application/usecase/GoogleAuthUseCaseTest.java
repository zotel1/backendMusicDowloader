package com.principal.backend.application.usecase;

import com.principal.backend.domain.model.AuthResult;
import com.principal.backend.domain.model.GoogleAccount;
import com.principal.backend.domain.model.GoogleTokenResponse;
import com.principal.backend.domain.port.GoogleAccountRepository;
import com.principal.backend.domain.port.GoogleAuthClient;
import com.principal.backend.domain.port.GoogleDriveClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.annotation.Async;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoogleAuthUseCaseTest {

    @Mock
    private GoogleAuthClient authClient;

    @Mock
    private GoogleAccountRepository accountRepository;

    @Mock
    private GoogleDriveClient driveClient;

    @Captor
    private ArgumentCaptor<GoogleAccount> accountCaptor;

    private GoogleAuthUseCase useCase;
    private UUID userId;

    @BeforeEach
    void setUp() {
        useCase = new GoogleAuthUseCase(authClient, accountRepository, driveClient);
        userId = UUID.randomUUID();
    }

    @Test
    void execute_SavesAccountAndTriggersFolderCreation() {
        String code = "auth-code";
        String accessToken = "access-token-123";
        GoogleTokenResponse tokenResponse = new GoogleTokenResponse(
                "google-id-1", "user@example.com", accessToken, "refresh-token", Instant.now().plusSeconds(3600));

        when(authClient.exchangeCode(code)).thenReturn(tokenResponse);

        AuthResult result = useCase.execute(code, userId);

        assertTrue(result.isSuccess());
        assertEquals("user@example.com", result.getEmail());

        // Verify account was saved
        verify(accountRepository).save(accountCaptor.capture());
        GoogleAccount savedAccount = accountCaptor.getValue();
        assertEquals(userId, savedAccount.getUserId());
        assertEquals("google-id-1", savedAccount.getGoogleId());
        assertEquals(accessToken, savedAccount.getAccessToken());

        // Verify folder creation was triggered
        verify(driveClient).createFolders(accessToken);
    }

    @Test
    void execute_ReturnsFailureWhenExchangeFails() {
        when(authClient.exchangeCode("bad-code")).thenThrow(new RuntimeException("Invalid code"));

        AuthResult result = useCase.execute("bad-code", userId);

        assertFalse(result.isSuccess());
        assertNull(result.getEmail());

        verify(accountRepository, never()).save(any());
        verify(driveClient, never()).createFolders(any());
    }

    @Test
    void execute_ReturnsFailureWhenNoGoogleId() {
        GoogleTokenResponse tokenResponse = new GoogleTokenResponse(
                null, null, "access-token", null, Instant.now().plusSeconds(3600));

        when(authClient.exchangeCode("code")).thenReturn(tokenResponse);

        AuthResult result = useCase.execute("code", userId);

        assertFalse(result.isSuccess());
        verify(accountRepository, never()).save(any());
        verify(driveClient, never()).createFolders(any());
    }

    @Test
    void execute_FolderCreationFailureDoesNotBreakAuth() {
        GoogleTokenResponse tokenResponse = new GoogleTokenResponse(
                "google-id-2", "test@example.com", "token-456", "refresh", Instant.now().plusSeconds(3600));

        when(authClient.exchangeCode("code")).thenReturn(tokenResponse);
        doThrow(new RuntimeException("Drive unavailable")).when(driveClient).createFolders(anyString());

        AuthResult result = useCase.execute("code", userId);

        // Auth should still succeed even if folder creation fails
        assertTrue(result.isSuccess());
        assertEquals("test@example.com", result.getEmail());

        // Account should have been saved
        verify(accountRepository).save(any());
        // Folder creation was attempted but failed
        verify(driveClient).createFolders(anyString());
    }

    @Test
    void triggerFolderCreationMethodIsAsync() throws NoSuchMethodException {
        assertTrue(useCase.getClass().getMethod("triggerFolderCreation", String.class)
                        .isAnnotationPresent(Async.class),
                "triggerFolderCreation should be annotated with @Async");
    }
}
