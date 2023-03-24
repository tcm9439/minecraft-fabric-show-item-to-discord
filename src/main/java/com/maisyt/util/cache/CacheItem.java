package com.maisyt.util.cache;

import java.util.UUID;

class CacheItem<V> {
    private UUID uuid;
    private V value;

    public CacheItem(UUID uuid, V value) {
        this.uuid = uuid;
        this.value = value;
    }

    public UUID getUuid() {
        return uuid;
    }

    public V getValue() {
        return value;
    }
}
