package com.principal.backend.domain.port;

import com.principal.backend.domain.model.Token;

import java.util.Optional;

public interface TokenRepository {

    void save(Token token);

    Optional<Token> findById(String userId);

    void deleteById(String userId);
}
