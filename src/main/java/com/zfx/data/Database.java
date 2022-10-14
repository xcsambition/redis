package com.zfx.data;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.StampedLock;
import java.util.function.BiFunction;

public class Database implements IDatabase {

    private StampedLock lock = new StampedLock();

    private final Map<String, DatabaseValue> cache;

    public Database(Map<String, DatabaseValue> cache) {
        this.cache = cache;
    }

    /**
     * @return
     * @see java.util.Map#size()
     */
    @Override
    public int size() {
        long stamp = lock.readLock();
        try {
            return cache.size();
        } finally {
            lock.unlockRead(stamp);
        }
    }

    /**
     * @return
     * @see java.util.Map#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        long stamp = lock.readLock();
        try {
            return cache.isEmpty();
        } finally {
            lock.unlockRead(stamp);
        }
    }

    /**
     * @param key
     * @return
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    @Override
    public boolean containsKey(Object key) {
        long stamp = lock.readLock();
        try {
            return cache.containsKey(key);
        } finally {
            lock.unlockRead(stamp);
        }
    }

    /**
     * @param value
     * @return
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    @Override
    public boolean containsValue(Object value) {
        long stamp = lock.readLock();
        try {
            return cache.containsValue(value);
        } finally {
            lock.unlockRead(stamp);
        }
    }

    /**
     * @param key
     * @return
     * @see java.util.Map#get(java.lang.Object)
     */
    @Override
    public DatabaseValue get(Object key) {
        DatabaseValue value = null;

        long optimistic = lock.tryOptimisticRead();
        value = cache.get(key);
        if (!lock.validate(optimistic)) {
            long stamp = lock.readLock();
            try {
                value = cache.get(key);
            } finally {
                lock.unlockRead(stamp);
            }
        }

        return value;
    }

    /**
     * @param key
     * @param value
     * @return
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public DatabaseValue put(String key, DatabaseValue value) {
        long stamp = lock.writeLock();
        try {
            return cache.put(key, value);
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    /**
     * @param key
     * @return
     * @see java.util.Map#remove(java.lang.Object)
     */
    @Override
    public DatabaseValue remove(Object key) {
        long stamp = lock.writeLock();
        try {
            return cache.remove(key);
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    /**
     * @param m
     * @see java.util.Map#putAll(java.util.Map)
     */
    @Override
    public void putAll(Map<? extends String, ? extends DatabaseValue> m) {
        long stamp = lock.writeLock();
        try {
            cache.putAll(m);
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    /**
     * @see java.util.Map#clear()
     */
    @Override
    public void clear() {
        long stamp = lock.writeLock();
        try {
            cache.clear();
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    /**
     * @return
     * @see java.util.Map#keySet()
     */
    @Override
    public Set<String> keySet() {
        return cache.keySet();
    }

    /**
     * @return
     * @see java.util.Map#values()
     */
    @Override
    public Collection<DatabaseValue> values() {
        return cache.values();
    }

    /**
     * @return
     * @see java.util.Map#entrySet()
     */
    @Override
    public Set<java.util.Map.Entry<String, DatabaseValue>> entrySet() {
        return cache.entrySet();
    }

    @Override
    public DatabaseValue putIfAbsent(String key, DatabaseValue value) {
        long stamp = lock.writeLock();
        try {
            return cache.putIfAbsent(key, value);
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    @Override
    public DatabaseValue merge(
            String key,
            DatabaseValue value,
            BiFunction<? super DatabaseValue, ? super DatabaseValue, ? extends DatabaseValue> remappingFunction) {
        long stamp = lock.writeLock();
        try {
            return cache.merge(key, value, remappingFunction);
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    @Override
    public boolean isType(String key, DataType type) {
        long stamp = lock.readLock();
        try {
            return cache.getOrDefault(key, new DatabaseValue(type)).getType() == type;
        } finally {
            lock.unlockRead(stamp);
        }
    }

}