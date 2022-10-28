package com.zfx.data;

import com.github.tonivade.purefun.Tuple;
import com.github.tonivade.purefun.Tuple2;
import com.github.tonivade.purefun.data.ImmutableList;
import com.github.tonivade.purefun.data.ImmutableSet;
import com.github.tonivade.purefun.data.Sequence;

import java.util.Map;

import static java.util.Objects.requireNonNull;

public class DefaultDatabase implements Database {

    private final Map<DatabaseKey, DatabaseValue> cache;

    public DefaultDatabase(Map<DatabaseKey, DatabaseValue> cache) {
        this.cache = requireNonNull(cache);
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public boolean isEmpty() {
        return cache.isEmpty();
    }

    @Override
    public boolean containsKey(DatabaseKey key) {
        return cache.containsKey(key);
    }

    @Override
    public DatabaseValue get(DatabaseKey key) {
        return cache.get(key);
    }

    @Override
    public DatabaseValue put(DatabaseKey key, DatabaseValue value) {
        System.out.println("数据库改动" + Thread.currentThread().getName());
        return cache.put(key, value);
    }

    @Override
    public DatabaseValue remove(DatabaseKey key) {
        return cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public ImmutableSet<DatabaseKey> keySet() {
        return ImmutableSet.from(cache.keySet());
    }

    @Override
    public Sequence<DatabaseValue> values() {

        return ImmutableList.from(cache.values());
    }

    @Override
    public ImmutableSet<Tuple2<DatabaseKey, DatabaseValue>> entrySet() {
        return keySet().map(key -> Tuple.of(key, get(key)));
    }
}
