package com.principal.backend.domain.port;

import com.principal.backend.domain.model.GoogleAccount;

import java.util.Optional;
import java.util.UUID;

public interface GoogleAccountRepository {

    Optional<GoogleAccount> findByUserId(UUID userId);

    void save(GoogleAccount account);

    void deleteByUserId(UUID userId);
}
