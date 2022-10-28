package com.zfx.data;

import com.github.tonivade.purefun.Tuple2;
import com.github.tonivade.purefun.data.ImmutableSet;
import com.github.tonivade.purefun.data.Sequence;

public interface Database {

    int size();

    boolean isEmpty();

    boolean containsKey(DatabaseKey key);

    DatabaseValue get(DatabaseKey key);

    DatabaseValue put(DatabaseKey key, DatabaseValue value);

    DatabaseValue remove(DatabaseKey key);

    void clear();

    ImmutableSet<DatabaseKey> keySet();

    Sequence<DatabaseValue> values();

    ImmutableSet<Tuple2<DatabaseKey, DatabaseValue>> entrySet();

    default boolean isType(DatabaseKey key, DataType type) {
        DatabaseValue dataBaseValue = get(key);
        return dataBaseValue == null || dataBaseValue.getType() == type;
    }


}
