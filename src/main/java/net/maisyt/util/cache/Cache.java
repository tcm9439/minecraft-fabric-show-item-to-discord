package net.maisyt.util.cache;

public abstract class Cache <K,V>{
    public static final int NO_LIMIT = -1;
    private int capacity = NO_LIMIT;

    public Cache(int capacity) {
        this.capacity = capacity;
    }

    abstract public V get(K key);
    abstract public void put(K key, V value);
    abstract public int size();
    abstract public void clear();

    abstract public void delete(K key);

    public int getCapacity() {
        return capacity;
    }

    public boolean isFull(){
        return size() >= getCapacity();
    }
}
