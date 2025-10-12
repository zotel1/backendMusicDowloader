package com.principal.backend.store;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryTokenStore {

    private final ConcurrentHashMap<String, StoredToken> store = new ConcurrentHashMap<>();


    public void save(String userId, StoredToken token) {
        store.put(userId, token);
    }

    public StoredToken findById(String userId){
        return store.get(userId);
    }

    public void deleteById(String userId) {
        store.remove(userId);
    }
}
