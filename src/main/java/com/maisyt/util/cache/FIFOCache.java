package com.maisyt.util.cache;

import java.util.*;

public class FIFOCache<K,V> extends Cache<K,V>{
    /**
     * The queue of th key as an index to remove items in a FIFO way
     */
    Queue<CacheKey<K>> keyQueue;
    Map<K, CacheItem<V>> cacheMap;

    public FIFOCache(int capacity) {
        super(capacity);
        keyQueue = new LinkedList<>();
        cacheMap = new HashMap<>();
    }

    @Override
    public V get(K key) {
        CacheItem<V> cacheItem = cacheMap.get(key);
        return cacheItem == null ? null : cacheItem.getValue();
    }

    void removeOverflowItemIfNeeded(){
        while (isFull() && keyQueue.size() > 0){
            // poll the first item in the queue
            CacheKey<K> cacheKey = keyQueue.poll();
            // check if the item in the map is still there, id by uuid
            CacheItem<V> cacheItem = cacheMap.get(cacheKey.getKey());
            if (cacheItem != null && cacheItem.getUuid().equals(cacheKey.getUuid())){
                // remove the item from the map
                cacheMap.remove(cacheKey.getKey());
            }
        }
    }

    @Override
    public void put(K key, V value) {
        // remove the overflow item if it (id by uuid) is still there
        removeOverflowItemIfNeeded();

        UUID uuid = UUID.randomUUID();
        keyQueue.add(new CacheKey<>(uuid, key));
        cacheMap.put(key, new CacheItem<>(uuid, value));
    }

    @Override
    public int size() {
        return cacheMap.size();
    }

    @Override
    public void clear() {
        keyQueue.clear();
        cacheMap.clear();
    }

    @Override
    public void delete(K key) {
        cacheMap.remove(key);
        // don't care the index queue
    }
}
