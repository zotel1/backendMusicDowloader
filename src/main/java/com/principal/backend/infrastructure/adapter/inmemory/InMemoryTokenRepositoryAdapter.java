package com.principal.backend.infrastructure.adapter.inmemory;

import com.principal.backend.domain.model.Token;
import com.principal.backend.domain.port.TokenRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryTokenRepositoryAdapter implements TokenRepository {

    private final ConcurrentHashMap<String, Token> store = new ConcurrentHashMap<>();

    public InMemoryTokenRepositoryAdapter() {
    }

    @Override
    public void save(Token token) {
        store.put(token.getUserId(), token);
    }

    @Override
    public Optional<Token> findById(String userId) {
        return Optional.ofNullable(store.get(userId));
    }

    @Override
    public void deleteById(String userId) {
        store.remove(userId);
    }
}
