package com.maisyt.util.cache;

import java.util.UUID;

class CacheKey<K> {
    private UUID uuid;
    private K key;

    public CacheKey(UUID uuid, K key) {
        this.uuid = uuid;
        this.key = key;
    }

    public UUID getUuid() {
        return uuid;
    }

    public K getKey() {
        return key;
    }
}
