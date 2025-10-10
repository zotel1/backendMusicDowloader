package com.principal.backend.store;

import java.util.concurrent.ConcurrentHashMap;

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
