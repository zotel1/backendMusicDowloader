package com.principal.backend.infrastructure.adapter.jpa.entity;

import com.principal.backend.domain.model.GoogleAccount;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GoogleAccountEntityTest {

    @Test
    void shouldConvertFromDomainToEntity() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Instant expiresAt = Instant.now().plusSeconds(3600);
        GoogleAccount domain = new GoogleAccount(id, userId, "google-id", "user@test.com",
                "access-token", "refresh-token", expiresAt);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);

        GoogleAccountEntity entity = GoogleAccountEntity.fromDomain(domain, userEntity);

        assertEquals(id, entity.getId());
        assertEquals(userId, entity.getUser().getId());
        assertEquals("google-id", entity.getGoogleId());
        assertEquals("user@test.com", entity.getEmail());
        assertEquals("access-token", entity.getAccessToken());
        assertEquals("refresh-token", entity.getRefreshToken());
        assertEquals(expiresAt, entity.getExpiresAt());
    }

    @Test
    void shouldConvertFromEntityToDomain() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Instant expiresAt = Instant.now().plusSeconds(3600);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);

        GoogleAccountEntity entity = new GoogleAccountEntity();
        entity.setId(id);
        entity.setUser(userEntity);
        entity.setGoogleId("google-id");
        entity.setEmail("user@test.com");
        entity.setAccessToken("access-token");
        entity.setRefreshToken("refresh-token");
        entity.setExpiresAt(expiresAt);

        GoogleAccount domain = entity.toDomain();

        assertEquals(id, domain.getId());
        assertEquals(userId, domain.getUserId());
        assertEquals("google-id", domain.getGoogleId());
        assertEquals("user@test.com", domain.getEmail());
        assertEquals("access-token", domain.getAccessToken());
        assertEquals("refresh-token", domain.getRefreshToken());
        assertEquals(expiresAt, domain.getExpiresAt());
    }

    @Test
    void shouldHandleNullRefreshToken() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Instant expiresAt = Instant.now().plusSeconds(3600);

        GoogleAccount domain = new GoogleAccount(id, userId, "google-id", "user@test.com",
                "access-token", null, expiresAt);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);

        GoogleAccountEntity entity = GoogleAccountEntity.fromDomain(domain, userEntity);

        assertEquals(id, entity.getId());
        assertNull(entity.getRefreshToken());

        // Convert back
        GoogleAccount roundTrip = entity.toDomain();
        assertNull(roundTrip.getRefreshToken());
    }
}
